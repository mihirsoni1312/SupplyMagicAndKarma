package com.example.karma.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.karma.R
import com.example.karma.response.Category
import java.util.*

internal class filteradapter(
    private val context:Context,
    private val mItems: ArrayList<Category>,
    private val mListener: ItemListener
) : RecyclerView.Adapter<filteradapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.bottom_sheet_item, parent, false))
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_cp, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.txt_titled.text = mItems[position].cName
//        holder.txt_titled.setOnClickListener {
//            mListener.onItemClick(mItems[position].id)
//        }

    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    internal inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        var txt_titled: TextView

        init {
            itemView.setOnClickListener(this)
            txt_titled = itemView.findViewById(R.id.txt_titled)
        }

        override fun onClick(p0: View?) {
            mListener?.onItemClick(
                mItems[position].id
            )
        }
    }

    internal interface ItemListener {
        fun onItemClick(
            _id: String
        )
    }
}