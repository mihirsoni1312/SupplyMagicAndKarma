package com.example.karma.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.karma.R
import com.example.karma.interfaces.AddressClickListner
import com.example.karma.interfaces.AddressSelectClickListner
import com.example.karma.responseModel.AddressListResponseModel
import com.example.karma.utils.PreferenceManager
import kotlinx.android.synthetic.main.row_addresslist.view.*


class AddressListAdapter(
    private val context: Context,
    private var items: ArrayList<AddressListResponseModel>,
    private var addressClickListner: AddressClickListner,
    private var addressClick: AddressSelectClickListner


) : RecyclerView.Adapter<AddressListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.row_addresslist,
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txt_address_title.text = items[position].address

        if (items[position].isPrimary) {
            holder.ll_editDelete.visibility = View.GONE
            holder.img_circle.setImageResource(R.drawable.withselect)
            holder.txt_onlyEditText.visibility = View.VISIBLE

            PreferenceManager.setaddress(
                context,
                items[position].address
            )

            PreferenceManager.setzipCode(
                context,
                items[position].zipCode
            )

        } else {
            holder.ll_editDelete.visibility = View.VISIBLE
            holder.txt_onlyEditText.visibility = View.GONE
            holder.img_circle.setImageResource(R.drawable.withoutselect)
        }

        holder.edt_edit.setOnClickListener {

            addressClickListner.editAddress(items[position], position)


        }

        holder.txt_onlyEditText.setOnClickListener {
            addressClickListner.editAddress(items[position], position)
        }

        holder.img_circle.setOnClickListener {

            addressClick.click(items[position])

        }
        holder.txt_address_title.setOnClickListener {

            addressClick.click(items[position])

        }


    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val edt_delete = view.edt_delete!!
        val edt_edit = view.edt_edit!!
        val txt_address_title = view.txt_address_title!!
        val img_circle = view.img_circle
        val ll_editDelete = view.ll_editDelete
        val txt_onlyEditText = view.txt_onlyEditText


    }

}