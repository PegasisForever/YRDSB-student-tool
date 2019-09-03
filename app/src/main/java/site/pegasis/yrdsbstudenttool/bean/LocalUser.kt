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

class LocalUser() : Parcelable {
    lateinit var studentNumber: String
    lateinit var password: String

    constructor(parcel: Parcel) : this() {
        studentNumber = parcel.readString()!!
        password = parcel.readString()!!
    }

    constructor(studentNumber:String, password:String):this(){
        this.studentNumber=studentNumber
        this.password=password
    }

    constructor(cursor: Cursor):this(){
        studentNumber = cursor.getString(cursor.getColumnIndex("studentNumber"))
        password = cursor.getString(cursor.getColumnIndex("password"))
    }

    fun toValues(): ContentValues{
        val values=ContentValues()
        values.put("studentNumber",studentNumber)
        values.put("password",password)
        return values
    }

    override fun toString(): String {
        return "LocalUser: $studentNumber"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(studentNumber)
        parcel.writeString(password)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LocalUser> {
        override fun createFromParcel(parcel: Parcel): LocalUser {
            return LocalUser(parcel)
        }

        override fun newArray(size: Int): Array<LocalUser?> {
            return arrayOfNulls(size)
        }
    }
}