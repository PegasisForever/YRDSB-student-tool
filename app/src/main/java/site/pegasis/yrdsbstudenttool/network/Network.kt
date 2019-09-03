/*
 *  Copyright Pegasis 2018
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package site.pegasis.yrdsbstudenttool.network

import android.content.Context
import android.util.Log
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import site.pegasis.yrdsbstudenttool.bean.*
import site.pegasis.yrdsbstudenttool.dataBase.ProfileDBH
import site.pegasis.yrdsbstudenttool.dataBase.UnitedDBH
import kotlin.concurrent.thread

class Network {
    companion object {
        private val TAG = "ASLS-Network"
        private var session_token = ""
        private var student_id = ""


        fun login(name: String, password: String, c: CallBack<Unit>?) {
            thread(start = true) {
                try {
                    val res = Jsoup.connect("https://ta.yrdsb.ca/live/index.php")
                        .data("username", name)
                        .data("password", password)
                        .method(Connection.Method.POST)
                        .execute()

                    val doc = res.parse()

                    if (doc.title() == "Student Reports") {
                        session_token = res.cookie("session_token")
                        student_id = res.cookie("student_id")

                        c?.success(Unit)
                    } else {
                        c?.error(ASLSException(1, "Student number or password incorrect"))
                    }
                } catch (e: Exception) {
                    Log.w(TAG, e.toString())
                    when (e.message) {
                        "Unable to resolve host \"ta.yrdsb.ca\": No address associated with hostname" -> {
                            c?.error(ASLSException(-2, e.message, "java.net.UnknownHostException"))
                        }
                        else -> {
                            c?.error(ASLSException(0, e.message))
                        }
                    }
                }
            }
        }

        fun getCourseSummary(context: Context, c: CallBack<ArrayList<CourseSummary>>?) {
            thread(start = true) {
                login(
                    ProfileDBH.getCurrentUser()!!.studentNumber,
                    ProfileDBH.getCurrentUser()!!.password,
                    object : CallBack<Unit>() {
                        override fun error(e: ASLSException) {
                            //todo 登录失败时回到登陆界面
                            c?.error(e)
                        }

                        override fun success(result: Unit) {
                            var doc: Document? = null
                            try {
                                doc =
                                        Jsoup.connect("https://ta.yrdsb.ca/live/students/listReports.php?student_id=$student_id")
                                            .cookie("session_token", session_token)
                                            .cookie("student_id", student_id)
                                            .get()

                            } catch (e: Exception) {
                                Log.w(TAG, e.toString())
                                when (e.message) {
                                    "Unable to resolve host \"ta.yrdsb.ca\": No address associated with hostname" -> {
                                        c?.error(ASLSException(-2, e.message, "java.net.UnknownHostException"))
                                    }
                                    else -> {
                                        c?.error(ASLSException(0, e.message))
                                    }
                                }
                            }
                            if (doc != null) {
                                val table = doc.getElementsByAttributeValue("cellpadding", "5")

                                val courses = ArrayList<CourseSummary>()

                                val trs = table.select("tr")
                                for (tr in trs) {
                                    val tds = tr.select("td")
                                    if (tds.isNotEmpty()) {
                                        val link = try {
                                            tds[2].childNode(1).attr("abs:href")
                                        } catch (e: Exception) {
                                            Log.v(TAG, e.toString())
                                            ""
                                        }
                                        courses.add(
                                            CourseSummary(
                                                tds[0].text(),
                                                tds[1].text(),
                                                tds[2].text(),
                                                link
                                            )
                                        )
                                    }

                                }
                                for (course in courses) {
                                    UnitedDBH.CourseSummary.add(course)
                                }
                                c?.success(courses)
                            }

                        }

                    })

            }
        }

        fun getCourseDetail(courseSummary: CourseSummary, c: CallBack<CourseDetail>?) {
            thread(start = true) {
                login(
                    ProfileDBH.getCurrentUser()!!.studentNumber,
                    ProfileDBH.getCurrentUser()!!.password,
                    object : CallBack<Unit>() {
                        override fun error(e: ASLSException) {
                            c?.error(e)
                        }

                        override fun success(result: Unit) {
                            if (courseSummary.link == "") {
                                c?.noResult()
                            } else {
                                var doc: Document? = null
                                try {
                                    doc = Jsoup.connect(courseSummary.link)
                                        .cookie("session_token", session_token)
                                        .cookie("student_id", student_id)
                                        .get()

                                } catch (e: Exception) {
                                    Log.w(TAG, e.toString())
                                    when (e.message) {
                                        "Unable to resolve host \"ta.yrdsb.ca\": No address associated with hostname" -> {
                                            c?.error(ASLSException(-2, e.message, "java.net.UnknownHostException"))
                                        }
                                        else -> {
                                            c?.error(ASLSException(0, e.message))
                                        }
                                    }
                                }
                                if (doc != null) {
                                    val table = doc.getElementsByAttributeValue("width", "100%")
                                    val marks=ArrayList<Mark>()

                                    val trs = table.select("tr")
                                    for (tr in trs) {
                                        if ((tr.childNodeSize()==11 && tr.text()!="Assignment Knowledge / Understanding Thinking Communication Application") ||
                                            (tr.childNodeSize()==13 && tr.text()!="Assignment Knowledge / Understanding Thinking Communication Application Other/Culminating")) {
                                            val tds = tr.children()
                                            var name = ""
                                            var ku = Percentage()
                                            var t = Percentage()
                                            var comuni = Percentage()
                                            var a = Percentage()
                                            for (td in tds) {
                                                when (td.attr("bgcolor")) {
                                                    "" -> name = td.text()
                                                    "ffffaa" -> ku = Percentage(td.text())
                                                    "c0fea4" -> t = Percentage(td.text())
                                                    "afafff" -> comuni = Percentage(td.text())
                                                    "ffd490" -> a = Percentage(td.text())
                                                }
                                            }
                                            marks.add(Mark(name,ku,t,comuni,a))
                                        }
                                    }
                                    val courseDetail =CourseDetail()
                                    marks.reverse()
                                    courseDetail.marks=marks
                                    UnitedDBH.Marks.add(courseSummary.classCode,marks)
                                    c?.success(courseDetail)
                                }
                            }
                        }

                    })

            }
        }


    }
}