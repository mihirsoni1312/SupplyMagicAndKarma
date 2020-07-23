package com.example.karma.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.karma.R
import com.example.karma.responseModel.productByProductId.CPModel

internal class CPListAdapter(
    private val context: Context,
    private val mItems: List<CPModel>,
    private val mListener: ItemListenerCP?,
    private val id: String?
) : RecyclerView.Adapter<CPListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.bottom_sheet_item, parent, false))
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_cp, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

//        if (mItems[position]._id == id) {
//            holder.itemView.performClick()
//
//        }
        holder.txt_titled.text = mItems[position].name
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


        override fun onClick(v: View) {
            mListener?.onItemClick(
                mItems[position].name,
                mItems[position]._id,
                mItems[position].pI.n,
                mItems[position].isFavPro,
                mItems[position].qty,
                mItems[position].pI.o,
                mItems[position].iAv

            )
        }
    }

    internal interface ItemListenerCP {
        fun onItemClick(
            name: String?,
            _id: String,
            newPrice: String,
            isFavPro: Boolean,
            qty: String ,
            opp:String,
            iAv: Boolean
            )
    }
}
