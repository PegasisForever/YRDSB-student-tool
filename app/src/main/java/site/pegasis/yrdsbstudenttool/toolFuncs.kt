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

package site.pegasis.yrdsbstudenttool

import android.content.Context
import android.widget.TextView
import site.pegasis.yrdsbstudenttool.dataBase.UnitedDBH
import java.math.RoundingMode
import java.text.NumberFormat

fun String.trimFirstSpace():String{
    return if (this.indexOf(" ")==0){
        this.substring(1,this.length)
    }else{
        this
    }
}

fun keep1Decimal(value: Double): String {

    val nf = NumberFormat.getNumberInstance()
    nf.maximumFractionDigits = 1
    nf.roundingMode = RoundingMode.HALF_UP
    return nf.format(value)
}

fun initUnitedDBH(context: Context, studentNumber: String){
    UnitedDBH(context,studentNumber,1)
}