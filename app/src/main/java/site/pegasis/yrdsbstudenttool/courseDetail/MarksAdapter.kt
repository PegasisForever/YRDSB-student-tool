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

package site.pegasis.yrdsbstudenttool.courseDetail

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.CardView
import android.widget.TextView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import site.pegasis.yrdsbstudenttool.R
import site.pegasis.yrdsbstudenttool.bean.CourseDetail
import site.pegasis.yrdsbstudenttool.bean.CourseSummary
import site.pegasis.yrdsbstudenttool.bean.Mark
import site.pegasis.yrdsbstudenttool.bean.Percentage
import site.pegasis.yrdsbstudenttool.courseDetail.CourseDetailActivity
import site.pegasis.yrdsbstudenttool.keep1Decimal
import site.pegasis.yrdsbstudenttool.login.LoginActivity


class MarksAdapter(private var data: ArrayList<Mark>, val context: Context) :
    RecyclerView.Adapter<MarksAdapter.ViewHolder>() {

    fun updateData(data: ArrayList<Mark>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // 实例化展示的view
        val v = LayoutInflater.from(parent.context).inflate(R.layout.mark_item, parent, false)
        // 实例化viewholder
        return ViewHolder(v)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // 绑定数据
        val mark = data[position]
        holder.name_tv.text = mark.name
        holder.mark_tv.text = keep1Decimal(mark.getInAccuratePrecentage()*100) + "%"
        setPercentage(holder.ku_tv, mark.ku)
        setPercentage(holder.t_tv, mark.t)
        setPercentage(holder.c_tv, mark.c)
        setPercentage(holder.a_tv, mark.a)
    }

    fun setPercentage(tv: TextView, p: Percentage) {
        val s = p.toString()
        tv.text = s
        if (s == "N/A") {
            tv.setTextColor(context.resources.getColor(R.color.grey))
        } else {
            tv.setTextColor(context.resources.getColor(R.color.black))
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var name_tv: TextView = itemView.findViewById(R.id.name_tv)
        internal var mark_tv: TextView = itemView.findViewById(R.id.mark_tv)
        internal var ku_tv: TextView = itemView.findViewById(R.id.ku_tv)
        internal var t_tv: TextView = itemView.findViewById(R.id.t_tv)
        internal var c_tv: TextView = itemView.findViewById(R.id.c_tv)
        internal var a_tv: TextView = itemView.findViewById(R.id.a_tv)

    }
}