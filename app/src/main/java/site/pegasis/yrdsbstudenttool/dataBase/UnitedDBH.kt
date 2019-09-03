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

package site.pegasis.yrdsbstudenttool.dataBase

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import site.pegasis.yrdsbstudenttool.bean.Mark
import kotlin.collections.ArrayList

class UnitedDBH(context: Context, studentNumber: String, version: Int) :
    SQLiteOpenHelper(context, "United-${studentNumber}.db", null, version, null) {

    init {
        db = writableDatabase
    }

    companion object {
        private lateinit var db: SQLiteDatabase
        private val TAG = "ASLS-UnitedDBH"
    }


    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL(
            "create table course_summary ("
                    + "classCode text, "
                    + "className text, "
                    + "block text,"
                    + "room text, "
                    + "startDate text, "
                    + "endDate text, "
                    + "mark text,"
                    + "link text)"
        )
        db.execSQL(
            "create table marks ("
                    + "classCode text, "
                    + "data text)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        var nowVersion = oldVersion
        while (nowVersion < newVersion) {

            nowVersion++
        }
    }

    object CourseSummary {
        private val TAG = "ASLS-U-CourseSummary"

        fun delete(classCode: String) {
            db.delete("course_summary", "classCode = ?", arrayOf(classCode))
        }

        fun add(courseSummary: site.pegasis.yrdsbstudenttool.bean.CourseSummary) {
            if (isItemExist(courseSummary)) {
                db.update("course_summary", courseSummary.toValues(), "classCode = ?", arrayOf(courseSummary.classCode))

            } else {
                db.insert("course_summary", null, courseSummary.toValues())
            }
        }

        private fun isItemExist(courseSummary: site.pegasis.yrdsbstudenttool.bean.CourseSummary): Boolean {
            val cursor =
                db.query("course_summary", null, "classCode = ?", arrayOf(courseSummary.classCode), null, null, null)

            val isPass = cursor.moveToFirst()
            cursor.close()

            return isPass
        }

        fun getAll(): ArrayList<site.pegasis.yrdsbstudenttool.bean.CourseSummary> {

            val cursor = db.query("course_summary", null, null, null, null, null, null)
            val allCourses: ArrayList<site.pegasis.yrdsbstudenttool.bean.CourseSummary> = ArrayList()

            //如果有数据，循环读取
            if (cursor.moveToFirst()) {
                do {
                    allCourses.add(site.pegasis.yrdsbstudenttool.bean.CourseSummary(cursor))
                } while (cursor.moveToNext())
            }
            //关闭指针
            cursor.close()

            return allCourses
        }
    }

    object Marks {
        private val TAG = "ASLS-U-Marks"

        fun delete(classCode: String) {
            db.delete("marks", "classCode = ?", arrayOf(classCode))
        }

        fun add(classCode: String, marks: ArrayList<Mark>) {
            delete(classCode)
            for (mark in marks) {
                db.insert("marks", null, mark.toValues(classCode))
            }
        }

        fun getAll(classCode: String): ArrayList<Mark> {

            val cursor = db.query("marks", null, "classCode = ?", arrayOf(classCode), null, null, null)
            val allMarks = ArrayList<Mark>()

            //如果有数据，循环读取
            if (cursor.moveToFirst()) {
                do {
                    allMarks.add(Mark(cursor))
                } while (cursor.moveToNext())
            }
            //关闭指针
            cursor.close()

            return allMarks
        }
    }

}