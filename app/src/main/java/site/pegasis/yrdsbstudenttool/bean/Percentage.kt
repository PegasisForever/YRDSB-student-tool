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

import android.util.Log
import site.pegasis.yrdsbstudenttool.keep1Decimal
import site.pegasis.yrdsbstudenttool.trimFirstSpace

class Percentage() {
    var items = ArrayList<PercentageItem>()
    private val TAG = "ASLS-Percentage"

    constructor(s: String) : this() {
        var s = s
        try {
            do {
                Log.v(TAG, s)
                var lastIndex2 = s.indexOf("weight")
                lastIndex2 = s.indexOf(" ", lastIndex2 + 6)
                if (lastIndex2 == -1) {
                    lastIndex2 = s.length
                }
                val got = s.substring(0, lastIndex2)
                items.add(PercentageItem(got))

                s = s.substring(got.length, s.length)
            } while (s != "")
        }catch (e:Throwable){
            Log.w(TAG,e.toString())
        }
    }

    fun fromValueString(s: String) {
        val fragments = s.split("ξ")
        for (f in fragments) {
            val item = PercentageItem()
            item.fromValueString(f)
            items.add(item)
        }
    }

    fun isAvailable():Boolean{
        var available = false
        for (item in items) {
            if (item.available) {
                available=true
            }
        }
        return available
    }

    fun getWeight(): Double {
        var sum = 0.0
        var total = 0
        for (item in items) {
            if (item.available) {
                sum += item.weight!!
                total++
            }
        }
        return if (total == 0) {
            0.0
        } else {
            sum / total
        }
    }

    fun getPrecentage():Double{
        var sum = 0.0
        var total = 0.0
        for (item in items) {
            if (item.available) {
                sum += item.weight!!*item.precentage!!
                total += item.weight!!
            }
        }

        return if (total == 0.0) {
            0.0
        } else {
            sum / total
        }
    }

    fun getInAccuratePrecentage():Double{
        var sum = 0.0
        var total = 0.0
        for (item in items) {
            if (item.available) {
                var weight=item.weight!!
                if (weight==0.0 && items.size==1){
                    weight=1.0
                }
                sum += weight*item.precentage!!
                total += weight
            }
        }

        return sum / total
    }

    override fun toString(): String {
        var s = ""
        for (i in 0..items.lastIndex) {
            s += if (i == items.lastIndex) {
                items[i].toString()
            } else {
                items[i].toString() + "\n"
            }
        }
        return s
    }

    fun toValueString(): String {
        var s = ""
        for (i in 0..items.lastIndex) {
            s += if (i == items.lastIndex) {
                items[i].toValueString()
            } else {
                items[i].toValueString() + "ξ"
            }
        }
        return s
    }
}

class PercentageItem() {
    private val TAG = "ASLS-PercentageItem"
    var available = false
    var got: Double? = null
    var total: Double? = null
    var precentage: Double? = null
    var weight: Double? = null

    constructor(s: String) : this() {
        if (s != "") {
            val s = s.trimFirstSpace()
            Log.v(TAG, s)
            var lastIndex1 = 0
            var lastIndex2 = s.indexOf(" / ")
            got = s.substring(lastIndex1, lastIndex2).trimFirstSpace().toDouble()
            Log.v(TAG, got.toString())

            lastIndex2 += 3
            lastIndex1 = s.indexOf(" = ")
            total = s.substring(lastIndex2, lastIndex1).toDouble()
            Log.v(TAG, total.toString())

            precentage = got!! / total!!

            weight = if (s.indexOf("no weight") == -1) {
                s.substring(s.indexOf("weight=") + 7, s.length)
            } else {
                "0"
            }.toDouble()
            Log.v(TAG, weight.toString())

            available = true
        }
    }

    fun fromValueString(s: String) {
        val fragments = s.split("λ")
        if (fragments[0]!="null") {

            got = fragments[0].toDouble()
            total = fragments[1].toDouble()
            weight = fragments[2].toDouble()
            precentage = got!! / total!!
            available = true
        }
    }

    override fun toString(): String {
        return if (available) {
            "$got / $total\n${keep1Decimal(precentage!!*100)+"%"}"
        } else {
            "N/A"
        }
    }

    fun toValueString() = "${got}λ${total}λ${precentage}λ$weight"
}