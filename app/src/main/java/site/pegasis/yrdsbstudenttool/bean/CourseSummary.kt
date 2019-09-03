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

package site.pegasis.yrdsbstudenttool.bean

import android.content.ContentValues
import android.database.Cursor
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import site.pegasis.yrdsbstudenttool.trimFirstSpace

class CourseSummary(s1:String,s2:String,s3:String,link:String):Comparable<CourseSummary>, Parcelable {
    private val TAG="ASLS-CourseSummary"
    lateinit var classCode:String
    lateinit var className:String
    lateinit var block:String
    lateinit var room:String
    lateinit var startDate:String
    lateinit var endDate:String
    lateinit var mark:String
    lateinit var link:String

    constructor(parcel: Parcel) : this("","","","") {
        classCode = parcel.readString()!!
        className = parcel.readString()!!
        block = parcel.readString()!!
        room = parcel.readString()!!
        startDate = parcel.readString()!!
        endDate = parcel.readString()!!
        mark = parcel.readString()!!
        link = parcel.readString()!!
    }

    constructor(cursor:Cursor):this("","","",""){
        classCode=cursor.getString(cursor.getColumnIndex("classCode"))
        className=cursor.getString(cursor.getColumnIndex("className"))
        block=cursor.getString(cursor.getColumnIndex("block"))
        room=cursor.getString(cursor.getColumnIndex("room"))
        startDate=cursor.getString(cursor.getColumnIndex("startDate"))
        endDate=cursor.getString(cursor.getColumnIndex("endDate"))
        mark=cursor.getString(cursor.getColumnIndex("mark"))
        link=cursor.getString(cursor.getColumnIndex("link"))
    }

    init {
        if (s1!="" && s2!="" && s3!="") {
            var lastIndex1 = 0
            var lastIndex2 = s1.indexOf(" ")

            //s1
            classCode = s1.substring(lastIndex1, lastIndex2)
            Log.v(TAG, classCode)

            lastIndex2 += 2
            lastIndex1 = s1.indexOf(" Block")
            className = s1.substring(lastIndex2, lastIndex1).trimFirstSpace()
            Log.v(TAG, className)

            lastIndex1 += 8
            lastIndex2 = lastIndex1 + 1
            block = s1.substring(lastIndex1, lastIndex2)
            Log.v(TAG, block)

            lastIndex2 += 6
            room = s1.substring(lastIndex2, s1.length).trimFirstSpace()
            Log.v(TAG, room)

            //s2
            lastIndex1 = 0
            lastIndex2 = s2.indexOf(" ~ ")
            startDate = s2.substring(lastIndex1, lastIndex2)
            Log.v(TAG, startDate)

            lastIndex2 += 3
            endDate = s2.substring(lastIndex2, s2.length)
            Log.v(TAG, endDate)

            //s3
            lastIndex1 = s3.indexOf(" = ")
            lastIndex2 = s3.indexOf("%")
            mark = if (lastIndex1 != -1) {
                s3.substring(lastIndex1 + 3, lastIndex2)
            } else {
                "N/A"
            }
            Log.v(TAG, mark)

            this.link=link
            Log.v(TAG,link)
        }
    }

    fun toValues():ContentValues{
        val values = ContentValues()
        values.put("classCode", classCode)
        values.put("className", className)
        values.put("block", block)
        values.put("room", room)
        values.put("startDate", startDate)
        values.put("endDate", endDate)
        values.put("mark", mark)
        values.put("link",link)
        return values
    }

    override fun compareTo(other: CourseSummary): Int {
        return block.compareTo(other.block)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(classCode)
        parcel.writeString(className)
        parcel.writeString(block)
        parcel.writeString(room)
        parcel.writeString(startDate)
        parcel.writeString(endDate)
        parcel.writeString(mark)
        parcel.writeString(link)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<CourseSummary> {
        override fun createFromParcel(parcel: Parcel): CourseSummary {
            return CourseSummary(parcel)
        }

        override fun newArray(size: Int): Array<CourseSummary?> {
            return arrayOfNulls(size)
        }
    }

}