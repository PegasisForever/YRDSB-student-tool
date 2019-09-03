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

package site.pegasis.yrdsbstudenttool.views

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import site.pegasis.yrdsbstudenttool.R

open class ThemeActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //沉浸导航栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (getSharedPreferences("isTransNaviBar", Context.MODE_PRIVATE).getBoolean("isTransNaviBar", false)) {
                window.navigationBarColor = resources.getColor(R.color.background_material_light)
            } else {
                window.navigationBarColor = resources.getColor(getColorDarkId())
            }
        }

        //显示返回按钮
        if (isShowBackButton()) {
            supportActionBar?.setHomeButtonEnabled(true)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    open fun isShowBackButton() = true

    override fun setTheme(resid: Int) {
        val sp = getSharedPreferences("theme", Context.MODE_PRIVATE)
        val nowTheme = sp.getInt("theme", 0)

        val themeId = when (nowTheme) {
            0 -> R.style.TealTheme
            1 -> R.style.PurpleTheme
            2 -> R.style.OrangeTheme
            3 -> R.style.GreenTheme
            4 -> R.style.TardisTheme
            5 -> R.style.BlueGreyTheme
            6 -> R.style.MiracleOrangeTheme
            7 -> R.style.GritUIOrangeTheme
            else -> R.style.TealTheme
        }

        super.setTheme(themeId)
    }

    fun getColorPrimaryId(): Int {
        val sp = getSharedPreferences("theme", Context.MODE_PRIVATE)
        val nowTheme = sp.getInt("theme", 0)

        return when (nowTheme) {
            0 -> R.color.tealPrimary
            1 -> R.color.purplePrimary
            2 -> R.color.orangePrimary
            3 -> R.color.greenPrimary
            4 -> R.color.taidisPrimary
            5 -> R.color.blueGreyPrimary
            6 -> R.color.miracleOrangePrimary
            7 -> R.color.gritUIOrangePrimary
            else -> R.color.tealPrimary
        }
    }

    fun getColorDarkId(): Int {
        val sp = getSharedPreferences("theme", Context.MODE_PRIVATE)
        val nowTheme = sp.getInt("theme", 0)

        return when (nowTheme) {
            0 -> R.color.tealDark
            1 -> R.color.purpleDark
            2 -> R.color.orangeDark
            3 -> R.color.greenDark
            4 -> R.color.taidisDark
            5 -> R.color.blueGreyDark
            6 -> R.color.miracleOrangeDark
            7 -> R.color.gritUIOrangeDark
            else -> R.color.tealDark
        }
    }

    fun getColorAccentId(): Int {
        val sp = getSharedPreferences("theme", Context.MODE_PRIVATE)
        val nowTheme = sp.getInt("theme", 0)

        return when (nowTheme) {
            0 -> R.color.tealAccent
            1 -> R.color.purpleAccent
            2 -> R.color.orangeAccent
            3 -> R.color.greenAccent
            4 -> R.color.taidisAccent
            5 -> R.color.blueGreyAccent
            6 -> R.color.miracleOrangeAccent
            7 -> R.color.gritUIOrangeAccent
            else -> R.color.tealAccent
        }
    }

}