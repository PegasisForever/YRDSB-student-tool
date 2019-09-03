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

package site.pegasis.yrdsbstudenttool.main

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import site.pegasis.yrdsbstudenttool.login.LoginActivity
import site.pegasis.yrdsbstudenttool.R
import site.pegasis.yrdsbstudenttool.bean.ASLSException
import site.pegasis.yrdsbstudenttool.bean.CallBack
import site.pegasis.yrdsbstudenttool.bean.CourseSummary
import site.pegasis.yrdsbstudenttool.dataBase.ProfileDBH
import site.pegasis.yrdsbstudenttool.dataBase.UnitedDBH
import site.pegasis.yrdsbstudenttool.network.Network
import site.pegasis.yrdsbstudenttool.views.ThemeActivity

//todo 下拉刷新
class MainActivity : ThemeActivity() {
    lateinit var storgedCourses: ArrayList<CourseSummary>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ProfileDBH.getCurrentUser() == null) {
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            val adapter = CourseSummaryAdapter(ArrayList(0), this)

            recyclerview.layoutManager = layoutManager
            recyclerview.adapter = adapter

            refresh.setColorSchemeResources(getColorAccentId())
            refresh.setOnRefreshListener {
                getCourseSummary()
            }

            if (savedInstanceState!=null){
                storgedCourses = savedInstanceState.getParcelableArrayList<CourseSummary>("courses")
                (recyclerview.adapter as CourseSummaryAdapter).updateData(storgedCourses)
            }else {
                storgedCourses = UnitedDBH.CourseSummary.getAll()
                (recyclerview.adapter as CourseSummaryAdapter).updateData(storgedCourses)

                refresh.isRefreshing = true
                getCourseSummary()
            }
        }
    }

    fun getCourseSummary() {
        Network.getCourseSummary(this, object : CallBack<ArrayList<CourseSummary>>() {
            override fun success(result: ArrayList<CourseSummary>) {
                storgedCourses=result
                runOnUiThread {
                    refresh.isRefreshing = false
                    (recyclerview.adapter as CourseSummaryAdapter).updateData(result)
                }
            }

            override fun error(e: ASLSException) {
                runOnUiThread {
                    refresh.isRefreshing = false
                }
            }

        })
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        super.onContextItemSelected(item)

        when(item.itemId){
            //复制
            1->{
                val text=item.intent.extras.getString("text")
                val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clipboard.primaryClip = ClipData.newPlainText("text", text)
                Toast.makeText(applicationContext, getString(R.string.copied), Toast.LENGTH_SHORT).show()
            }
        }

        return true
    }

    override fun isShowBackButton() = false

    override fun onSaveInstanceState(bundle: Bundle?) {
        super.onSaveInstanceState(bundle)
        bundle?.putParcelableArrayList("courses",storgedCourses)
    }
}
