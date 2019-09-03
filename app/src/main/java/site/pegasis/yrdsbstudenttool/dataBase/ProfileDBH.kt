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

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import site.pegasis.yrdsbstudenttool.bean.LocalUser

class ProfileDBH(context: Context, version: Int)
    : SQLiteOpenHelper(context, "profile.db", null, version, null) {

    init {
        writabledb = writableDatabase
    }

    companion object {
        private val TAG = "ProfileDBH"
        private var writabledb: SQLiteDatabase? = null


        fun startTransaction() {
            Log.v(TAG, "开始事务")
            val db = writabledb!!
            db.execSQL("BEGIN TRANSACTION")
        }

        fun endTransaction() {
            Log.v(TAG, "结束事务")
            val db = writabledb!!
            db.execSQL("END TRANSACTION")

        }

        fun rollTransaction() {
            Log.v(TAG, "回滚事务")
            val db = writabledb!!
            db.execSQL("ROLLBACK")
        }


        fun logout() {
            Log.v(TAG, "登出")
            deleteAll()
        }

        fun deleteAll() {
            Log.v(TAG, "清空当前user数据")
            val db = writabledb!!
            db.execSQL("DELETE FROM profile")
        }

        fun updateCurrentUser(localUser: LocalUser) {
            val db = writabledb!!

            startTransaction()
            deleteAll()
            db.insert("profile", null, localUser.toValues())
            endTransaction()
            Log.v(TAG, "已保存当前user数据")
        }

        fun getCurrentUser(): LocalUser? {


            val db = writabledb!!
            val cursor = db.query("profile", null, null, null, null, null, null)

            //读取数据
            val user= if (cursor.moveToFirst()) {
                val localUser=LocalUser(cursor)

                cursor.close()

                Log.v(TAG, "读取user数据成功")
                localUser
            } else {
                //关闭指针
                cursor.close()

                Log.w(TAG, "读取user数据失败")

                null
            }

            Log.v(TAG,"CurrentUser: $user")
            return user
        }

    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("create table profile ("
                + "studentNumber text, "
                + "password text)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        var tempVersion = oldVersion
        do {
            when (tempVersion) {

            }

            tempVersion++
        } while (tempVersion < newVersion)
    }

}