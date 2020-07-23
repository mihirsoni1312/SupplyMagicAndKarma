package com.example.karma.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.karma.R
import com.example.karma.response.deliverySlotResponse.Result

class SpinnerAdapter (context: Context?, counting: ArrayList<Result>) :
        BaseAdapter() {
        var inflator: LayoutInflater
        var mCounting: ArrayList<Result>
        override fun getCount(): Int {
            return mCounting.size
        }



        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getView(
            position: Int,
            convertView: View?,
            parent: ViewGroup
        ): View {

            var convertView = inflator.inflate(R.layout.row_spinner, null)
            val tvDate = convertView.findViewById<View>(R.id.tvDate) as TextView
            val tvTime = convertView.findViewById<View>(R.id.tvTime) as TextView
            tvDate.text = mCounting[position].dateDisplay
            tvTime.text = "${mCounting[position].startTime} - ${mCounting[position].endTime}"
            return convertView
        }

        override fun getItem(position: Int): Any? {
        return null
    }

    init {
            inflator = LayoutInflater.from(context)
            mCounting = counting
        }
    }
