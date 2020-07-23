package com.example.karma.dummy

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.karma.R
import com.example.karma.response.PCK
import com.example.karma.responseModel.VendorListModel
import com.example.karma.utils.PreferenceManager
import com.google.android.material.button.MaterialButton
import com.squareup.picasso.Picasso

internal class MediaItemAdapter(
    private val context: Context,
    private val mItems: List<VendorListModel>,
    private val mListener: ItemListener?
) : RecyclerView.Adapter<MediaItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.bottom_sheet_item, parent, false))
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_bottom_sheet, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txt_title.text = mItems[position].appName
        Picasso.get().load("http://207.244.244.64/LogoImage/" + mItems[position].logo)
            .into(holder.img_product)

        holder.select_icon?.setBackgroundColor(
            Color.parseColor(
                PreferenceManager.getBackgroundColor(
                    context
                )
            )
        )
        holder.txt_title.setTextColor(
            Color.parseColor(
                PreferenceManager.getBackgroundColor(
                    context
                )
            )
        )

        holder.select_icon?.setTextColor(
            Color.parseColor(
                PreferenceManager.getButtonFontColor(
                    context
                )
            )
        )
//        holder.select_icon?.visibility = View.GONE

        if (PreferenceManager.getVendorId(context) != mItems[position].vendorId) {
            holder.select_icon?.visibility = View.GONE
        } else {
            holder.select_icon?.visibility = View.VISIBLE
        }

    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    internal inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        var txt_title: TextView
        var img_product: ImageView
        var txt_productdesc: TextView? = null
        var select_icon: MaterialButton? = null

        init {
            itemView.setOnClickListener(this)
            txt_title = itemView.findViewById(R.id.txt_title)
            img_product = itemView.findViewById(R.id.img_product)
            txt_productdesc = itemView.findViewById(R.id.txt_productdesc)
            select_icon = itemView.findViewById(R.id.select_icon)
        }


        override fun onClick(v: View) {
            mListener?.onItemClick(
                mItems[position].vendorId,
                mItems[position].appName,
                mItems[position].logo,
                mItems[position].pck,
                mItems[position].sck,
                mItems[position].plpf,
                mItems[position].plpk,
                mItems[position].phone,
                mItems[position].defaultPaymentType,
                mItems[position].defaultTypeBank,
                mItems[position].paymentTypeName,
                mItems[position].add
            )
        }
    }

    internal interface ItemListener {
        fun onItemClick(
            vendorId: String?,
            vendorName: String,
            vendorImage: String,
            pck: List<PCK>,
            sck: String,
            plpf: String,
            plpk: String,
            Phone: String,
            defaultPaymentType: String,
            defaultTypeBank:PCK,
            paymentTypeName:String,
            add:String
        )
    }
}
