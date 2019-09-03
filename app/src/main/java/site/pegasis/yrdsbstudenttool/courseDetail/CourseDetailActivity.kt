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
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.telecom.Call
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_course_detail.*
import site.pegasis.yrdsbstudenttool.R
import site.pegasis.yrdsbstudenttool.bean.ASLSException
import site.pegasis.yrdsbstudenttool.bean.CallBack
import site.pegasis.yrdsbstudenttool.bean.CourseDetail
import site.pegasis.yrdsbstudenttool.bean.CourseSummary
import site.pegasis.yrdsbstudenttool.network.Network
import site.pegasis.yrdsbstudenttool.views.NoActionBarThemeActivity
import java.util.ArrayList

class CourseDetailActivity : NoActionBarThemeActivity() {
    lateinit var courseSummary: CourseSummary
    private var isRefreshing=false
    private val TAG = "ASLS-CourseDetailAct"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_detail)

        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        courseSummary = intent.extras!!.getParcelable("summary")!!
        title = if (courseSummary.className == "") {
            courseSummary.classCode
        } else {
            courseSummary.className
        }

        setupViewPager(viewpager)
        tabs.setupWithViewPager(viewpager)
        tabs.setSelectedTabIndicatorColor(resources.getColor(R.color.white))

        updateCourseDetail()

    }

    fun updateCourseDetail() {
        if (!isRefreshing) {
            isRefreshing=true
            Network.getCourseDetail(courseSummary, object : CallBack<CourseDetail>() {
                override fun success(result: CourseDetail) {
                    isRefreshing=false
                    (supportFragmentManager.fragments[0] as MarksFragment).getDataCallBack.success(result)
                    (supportFragmentManager.fragments[1] as TrendsFragment).getDataCallBack.success(result)
                }

                override fun noResult() {
                    isRefreshing=false
                    (supportFragmentManager.fragments[0] as MarksFragment).getDataCallBack.noResult()
                    (supportFragmentManager.fragments[1] as TrendsFragment).getDataCallBack.noResult()
                }

                override fun error(e: ASLSException) {
                    isRefreshing=false
                    (supportFragmentManager.fragments[0] as MarksFragment).getDataCallBack.error(e)
                    (supportFragmentManager.fragments[1] as TrendsFragment).getDataCallBack.error(e)
                }

            })
        }
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = Adapter(supportFragmentManager)
        if (supportFragmentManager.fragments.isEmpty()) {
            val mf=MarksFragment()
            mf.retainInstance = true
            adapter.addFragment(mf, getString(R.string.marks))
            val tf=TrendsFragment()
            tf.retainInstance = true
            adapter.addFragment(tf, getString(R.string.trends))
            val af=AboutFragment()
            af.retainInstance = true
            adapter.addFragment(af, getString(R.string.about))
        }else{
            adapter.addFragment(supportFragmentManager.fragments[0], getString(R.string.marks))
            adapter.addFragment(supportFragmentManager.fragments[1], getString(R.string.trends))
            adapter.addFragment(supportFragmentManager.fragments[2], getString(R.string.about))
        }
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit=3
    }

    //返回按钮回调
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                this.finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    internal class Adapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        private val mFragments = ArrayList<Fragment>()
        private val mFragmentTitles = ArrayList<String>()

        fun addFragment(fragment: Fragment, title: String) {
            mFragments.add(fragment)
            mFragmentTitles.add(title)
        }

        override fun getItem(position: Int): Fragment {
            return mFragments[position]
        }

        override fun getCount(): Int {
            return mFragments.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mFragmentTitles[position]
        }
    }

}
