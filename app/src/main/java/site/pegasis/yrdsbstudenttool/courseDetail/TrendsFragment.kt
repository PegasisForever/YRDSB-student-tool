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

package site.pegasis.yrdsbstudenttool.courseDetail


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.data.Entry
import site.pegasis.yrdsbstudenttool.R
import site.pegasis.yrdsbstudenttool.dataBase.UnitedDBH
import kotlinx.android.synthetic.main.fragment_trends.view.*
import site.pegasis.yrdsbstudenttool.bean.ASLSException
import site.pegasis.yrdsbstudenttool.bean.CallBack
import site.pegasis.yrdsbstudenttool.bean.CourseDetail
import site.pegasis.yrdsbstudenttool.bean.Mark


class TrendsFragment : Fragment() {
    lateinit var getDataCallBack: CallBack<CourseDetail>
    lateinit var marks:ArrayList<Mark>
    private val TAG="ASLS-TrendsFragment"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_trends, container, false)


        getDataCallBack=object :CallBack<CourseDetail>(){
            override fun success(result: CourseDetail) {
                marks=result.marks

                val kus = ArrayList<Entry>()
                val ts = ArrayList<Entry>()
                val cs = ArrayList<Entry>()
                val `as` = ArrayList<Entry>()
                val lastIndex=marks.lastIndex
                for (i in 0..lastIndex) {
                    val reversedIndex=lastIndex-i
                    if (marks[reversedIndex].ku.isAvailable()) {
                        kus.add(Entry(i.toFloat(), marks[reversedIndex].ku.getInAccuratePrecentage().toFloat() * 100))
                    }
                    if (marks[reversedIndex].t.isAvailable()) {
                        ts.add(Entry(i.toFloat(), marks[reversedIndex].t.getInAccuratePrecentage().toFloat() * 100))
                    }
                    if (marks[reversedIndex].c.isAvailable()) {
                        cs.add(Entry(i.toFloat(), marks[reversedIndex].c.getInAccuratePrecentage().toFloat() * 100))
                    }
                    if (marks[reversedIndex].a.isAvailable()) {
                        `as`.add(Entry(i.toFloat(), marks[reversedIndex].a.getInAccuratePrecentage().toFloat() * 100))
                    }
                }
                activity?.runOnUiThread {
                    view.refresh.isRefreshing = false
                    view.ku_t.setData(kus, getString(R.string.knowledge_understanding))
                    view.t_t.setData(ts, getString(R.string.thinking))
                    view.c_t.setData(cs, getString(R.string.communication))
                    view.a_t.setData(`as`, getString(R.string.application))
                    checkIsListEmpty()
                }
            }

            override fun noResult() {
                activity?.runOnUiThread {
                    view.refresh.isRefreshing = false
                    checkIsListEmpty()
                }
            }

            override fun error(e: ASLSException) {
                Log.e(TAG,e.toString())
                activity?.runOnUiThread {
                    view.refresh.isRefreshing = false
                    checkIsListEmpty()
                }
            }
        }

        view.refresh.setColorSchemeColors(resources.getColor((activity as CourseDetailActivity).getColorAccentId()))
        view.refresh.setOnRefreshListener {
            (activity as CourseDetailActivity).updateCourseDetail()
        }

        if (!this::marks.isInitialized){
            marks=UnitedDBH.Marks.getAll((activity as CourseDetailActivity).courseSummary.classCode)
            view.refresh.isRefreshing=true
            val courseDetail=CourseDetail()
            courseDetail.marks = marks
            getDataCallBack.success(courseDetail)
            (activity as CourseDetailActivity).updateCourseDetail()
        }else{
            val courseDetail=CourseDetail()
            courseDetail.marks = marks
            getDataCallBack.success(courseDetail)
        }


        return view
    }

    fun checkIsListEmpty() {
        if (marks.isEmpty()) {
            view?.nodata_tv?.visibility = View.VISIBLE
            view?.scroll_view?.visibility = View.GONE
        }else{
            view?.nodata_tv?.visibility = View.GONE
            view?.scroll_view?.visibility = View.VISIBLE
        }
    }
}
