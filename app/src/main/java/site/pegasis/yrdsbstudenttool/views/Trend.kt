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
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.trend_item.view.*
import site.pegasis.yrdsbstudenttool.R

class Trend(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {
    private val view = LayoutInflater.from(context).inflate(R.layout.trend_item, this)
    private val title_tv: TextView
    private val chart: LineChart

    init {
        title_tv = view.title_tv
        chart = view.chart

        chart.description.isEnabled = false
        chart.setTouchEnabled(false)
        chart.legend.isEnabled = false

        val xAxis = chart.xAxis
        xAxis.isEnabled = false

        val yAxis = chart.axisLeft
        yAxis.axisMaximum = 101f
        yAxis.axisMinimum = 0f
        yAxis.addLimitLine(LimitLine(50f, ""))
        yAxis.addLimitLine(LimitLine(60f, ""))
        yAxis.addLimitLine(LimitLine(70f, ""))
        yAxis.addLimitLine(LimitLine(80f, ""))

        val yAxisr = chart.axisRight
        yAxisr.axisMaximum = 100f
        yAxisr.axisMinimum = 0f
        yAxisr.setDrawGridLines(false)
        yAxisr.setLabelCount(11, true)
        yAxisr.setValueFormatter { value, axis ->
            when (value) {
                50f -> context.getString(R.string.level_1)
                60f -> context.getString(R.string.level_2)
                70f -> context.getString(R.string.level_3)
                80f -> context.getString(R.string.level_4)
                else -> ""
            }
        }
    }

    fun setData(data: ArrayList<Entry>,title:String) {
        title_tv.text=title

        val dataSet = LineDataSet(data, "")

        dataSet.lineWidth = 3f
        dataSet.setDrawCircleHole(true)
        dataSet.setCircleColor(resources.getColor(R.color.tealDark))
        dataSet.valueTextSize = 10f
        dataSet.color = resources.getColor(R.color.tealPrimary)

        chart.data = LineData(dataSet)
        chart.invalidate()
    }
}