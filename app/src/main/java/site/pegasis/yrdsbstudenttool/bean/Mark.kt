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

class Mark() {
    lateinit var name: String
    lateinit var ku: Percentage
    lateinit var t: Percentage
    lateinit var c: Percentage
    lateinit var a: Percentage

    constructor(name: String, ku: Percentage, t: Percentage, c: Percentage, a: Percentage) : this() {
        this.name = name
        this.ku = ku
        this.t = t
        this.c = c
        this.a = a
    }

    constructor(cursor: Cursor):this(){
        val s = cursor.getString(cursor.getColumnIndex("data"))
        fromValueString(s)
    }

    fun fromValueString(s: String) {
        val fragments = s.split("ρ")
        name = fragments[0]

        val p1 = Percentage()
        p1.fromValueString(fragments[1])
        ku = p1

        val p2 = Percentage()
        p2.fromValueString(fragments[2])
        t = p2

        val p3 = Percentage()
        p3.fromValueString(fragments[3])
        c = p3

        val p4 = Percentage()
        p4.fromValueString(fragments[4])
        a = p4
    }


    fun getPrecentage(): Double {
        var got = 0.0
        var total = 0.0
        if (ku.isAvailable()) {
            got += ku.getPrecentage() * 35.7
            total += 35.7
        }
        if (t.isAvailable()) {
            got += t.getPrecentage() * 21.4
            total += 21.4
        }
        if (c.isAvailable()) {
            got += c.getPrecentage() * 14.3
            total += 14.3
        }
        if (a.isAvailable()) {
            got += a.getPrecentage() * 28.6
            total += 28.6
        }
        return got / total
    }

    fun getInAccuratePrecentage(): Double {
        var got = 0.0
        var total = 0.0
        if (ku.isAvailable()) {
            got += ku.getInAccuratePrecentage() * 35.7
            total += 35.7
        }
        if (t.isAvailable()) {
            got += t.getInAccuratePrecentage() * 21.4
            total += 21.4
        }
        if (c.isAvailable()) {
            got += c.getInAccuratePrecentage() * 14.3
            total += 14.3
        }
        if (a.isAvailable()) {
            got += a.getInAccuratePrecentage() * 28.6
            total += 28.6
        }
        return got / total
    }

    override fun toString() = "$name: \nku=$ku  \nt=$t  \nc=$c  \na=$a"

    fun toValueString() = "${name}ρ${ku.toValueString()}ρ${t.toValueString()}ρ${c.toValueString()}ρ${a.toValueString()}"

    fun toValues(classCode: String): ContentValues {
        val values = ContentValues()
        values.put("classCode", classCode)
        values.put("data", toValueString())
        return values
    }
}