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

package site.pegasis.yrdsbstudenttool.main

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
import site.pegasis.yrdsbstudenttool.bean.CourseSummary
import site.pegasis.yrdsbstudenttool.courseDetail.CourseDetailActivity
import site.pegasis.yrdsbstudenttool.login.LoginActivity


class CourseSummaryAdapter(private var data: ArrayList<CourseSummary>, val context: Context) : RecyclerView.Adapter<CourseSummaryAdapter.ViewHolder>() {

    fun updateData(data: ArrayList<CourseSummary>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // 实例化展示的view
        val v = LayoutInflater.from(parent.context).inflate(R.layout.course_summary_item, parent, false)
        // 实例化viewholder
        return ViewHolder(v)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // 绑定数据
        val className = if (data[position].className==""){
            data[position].classCode
        }else{
            data[position].className
        }
        holder.name_tv.text=className

        var location="Period ${data[position].block}"
        if (data[position].room!=""){
            location +=" - Room ${data[position].room}"
        }
        holder.location_tv.text=location

        val mark =if (data[position].mark!="N/A"){
            holder.mark_tv.setTextColor(context.resources.getColor(R.color.black))
            "${data[position].mark}%"
        }else{
            holder.mark_tv.setTextColor(context.resources.getColor(R.color.grey))
            data[position].mark
        }
        holder.mark_tv.text=mark

        holder.card_view.setOnClickListener {
            val intent = Intent(context, CourseDetailActivity::class.java)
            intent.putExtra("summary",data[position])
            context.startActivity(intent)
        }
        holder.card_view.setOnCreateContextMenuListener { menu, _, _ ->
            val copyMenu= menu.add(0, 1, 0, context.resources.getString(R.string.copy))
            val copyIntent=Intent()
            copyIntent.putExtra("text","$className \n$location \n${context.getString(R.string.current_mark)}$mark")
            copyMenu.intent=copyIntent
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var name_tv: TextView = itemView.findViewById(R.id.name_tv)
        internal var location_tv: TextView = itemView.findViewById(R.id.location_tv)
        internal var mark_tv: TextView = itemView.findViewById(R.id.mark_tv)
        internal var card_view:CardView = itemView.findViewById(R.id.card_view)

    }
}