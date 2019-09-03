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

package site.pegasis.yrdsbstudenttool.login

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.inputmethod.EditorInfo
import kotlinx.android.synthetic.main.activity_login.*
import site.pegasis.yrdsbstudenttool.R
import site.pegasis.yrdsbstudenttool.bean.ASLSException
import site.pegasis.yrdsbstudenttool.bean.CallBack
import site.pegasis.yrdsbstudenttool.bean.LocalUser
import site.pegasis.yrdsbstudenttool.dataBase.ProfileDBH
import site.pegasis.yrdsbstudenttool.dataBase.UnitedDBH
import site.pegasis.yrdsbstudenttool.initUnitedDBH
import site.pegasis.yrdsbstudenttool.main.MainActivity
import site.pegasis.yrdsbstudenttool.network.Network
import site.pegasis.yrdsbstudenttool.views.ThemeActivity

class LoginActivity : ThemeActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //设置键盘回车键作用
        nameet.imeOptions = EditorInfo.IME_ACTION_NEXT
        passwordet.imeOptions = EditorInfo.IME_ACTION_DONE
        passwordet.setOnEditorActionListener { v, actionId, event ->
            loginButtonOnClick()
            return@setOnEditorActionListener true
        }

        //显示密码按钮
        view_password_btn.setOnClickListener {
            val type = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            if (passwordet.inputType == type) {
                passwordet.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                passwordet.setSelection(passwordet.text.toString().length)
            } else {
                passwordet.inputType = type
                passwordet.setSelection(passwordet.text.toString().length)
            }
        }

        //确认按钮
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            send_btn.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.tealAccent))
        } else {
            send_btn.setBackgroundColor(resources.getColor(R.color.tealAccent))
        }
        send_btn.setOnClickListener {
            loginButtonOnClick()
        }

    }

    override fun isShowBackButton() = false

    fun loginButtonOnClick() {
        //获得文本框里的昵称密码
        val studentNumber = nameet.text.toString()
        val password = passwordet.text.toString()

        if (studentNumber == "") {
            //昵称为空
            warntv.visibility = View.VISIBLE
            warntv.text = getString(R.string.student_number_is_empty)
        } else if (password == "") {
            //密码为空
            warntv.visibility = View.VISIBLE
            warntv.text = getString(R.string.password_is_empty)
        } else {
            //全部符合条件
            //send_btn.isClickable = false

            warntv.visibility = View.GONE
            progressBar.visibility = View.VISIBLE

            //登录
            Network.login(studentNumber, password, object : CallBack<Unit>() {
                override fun success(result: Unit) {
                    ProfileDBH.updateCurrentUser(LocalUser(studentNumber,password))
                    initUnitedDBH(this@LoginActivity,studentNumber)

                    runOnUiThread {
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }

                override fun error(e: ASLSException) {
                    runOnUiThread {
                        send_btn.isClickable = true
                        progressBar.visibility = View.INVISIBLE
                        warntv.visibility = View.VISIBLE
                        warntv.text = when (e.errorCode) {
                            1 -> getString(R.string.student_number_or_password_incorrect)
                            2 -> getString(R.string.no_network)
                            else -> e.toString()
                        }
                    }
                }
            })
        }
    }
}
