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

import android.annotation.SuppressLint
import android.app.Application
import android.webkit.WebView
import site.pegasis.yrdsbstudenttool.dataBase.ProfileDBH
class YSTApplication:Application() {
    lateinit var webView :WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate() {
        super.onCreate()

        ProfileDBH(this, 1)
        val currentUser=ProfileDBH.getCurrentUser()
        if (currentUser!=null){
            initUnitedDBH(this,currentUser.studentNumber)
        }
    }
}