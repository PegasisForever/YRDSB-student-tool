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
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_marks.view.*
import site.pegasis.yrdsbstudenttool.R
import site.pegasis.yrdsbstudenttool.bean.ASLSException
import site.pegasis.yrdsbstudenttool.bean.CallBack
import site.pegasis.yrdsbstudenttool.bean.CourseDetail
import site.pegasis.yrdsbstudenttool.dataBase.UnitedDBH
import android.support.v7.widget.DividerItemDecoration
import android.util.Log
import site.pegasis.yrdsbstudenttool.bean.Mark


class MarksFragment : Fragment() {
    lateinit var getDataCallBack:CallBack<CourseDetail>
    lateinit var marks:ArrayList<Mark>
    private val TAG="ASLS-MarksFragment"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_marks, container, false)

        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        val adapter = MarksAdapter(ArrayList(0), activity!!)

        val marks_rv = view.marks_rv
        marks_rv.layoutManager = layoutManager
        marks_rv.adapter = adapter
        marks_rv.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))

        val nodata_tv=view.nodata_tv
        getDataCallBack=object :CallBack<CourseDetail>(){
            override fun success(result: CourseDetail) {
                marks=result.marks
                activity?.runOnUiThread {
                    view.refresh.isRefreshing = false
                    (marks_rv.adapter as MarksAdapter).updateData(marks)
                    checkIsListEmpty(marks_rv,nodata_tv)
                }
            }

            override fun noResult() {
                activity?.runOnUiThread {
                    view.refresh.isRefreshing = false
                    checkIsListEmpty(marks_rv,nodata_tv)
                }
            }

            override fun error(e: ASLSException) {
                Log.e(TAG,e.toString())
                activity?.runOnUiThread {
                    view.refresh.isRefreshing = false
                    checkIsListEmpty(marks_rv,nodata_tv)
                }
            }
        }

        view.refresh.setColorSchemeColors(resources.getColor((activity as CourseDetailActivity).getColorAccentId()))
        view.refresh.setOnRefreshListener {
            (activity as CourseDetailActivity).updateCourseDetail()
        }

        if (!this::marks.isInitialized){
            marks = UnitedDBH.Marks.getAll((activity as CourseDetailActivity).courseSummary.classCode)
            val courseDetail=CourseDetail()
            courseDetail.marks = marks
            getDataCallBack.success(courseDetail)

            view.refresh.isRefreshing=true
            (activity as CourseDetailActivity).updateCourseDetail()
        }else{
            val courseDetail=CourseDetail()
            courseDetail.marks = marks
            getDataCallBack.success(courseDetail)
        }

        return view
    }

    fun checkIsListEmpty(marks_rv: RecyclerView, nodata_tv: TextView) {
        nodata_tv.visibility = if (marks_rv.adapter!!.itemCount == 0) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}
