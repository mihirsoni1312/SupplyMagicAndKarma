package com.example.karma.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.karma.R
import com.example.karma.activities.ProductDetailActivity
import com.example.karma.interfaces.AddToCartWishListListner
import com.example.karma.interfaces.RemoveWishListClickListner
import com.example.karma.responseModel.WishListProductListModel
import com.example.karma.utils.PreferenceManager
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_view_item.view.*


class WishListAdapter(
    private val context: Context,
    private var items: ArrayList<WishListProductListModel>,
    private var addToCartWishListListner: AddToCartWishListListner,
    private var RemoveWishListClickListner: RemoveWishListClickListner,
    private var relatedProduct: RelatedProduct


) : RecyclerView.Adapter<WishListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.recycler_view_item,
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txt_itemview.text = items[position].name
        holder.txt_itemview.setTextColor(
            Color.parseColor(
                PreferenceManager.getBackgroundColor(context)
            )
        )
        Picasso.get().load("http://207.244.244.64/ItemImages/" + items[position].image)
            .into(holder.img_wishlist)
        holder.txt_newPrice.text = "${items[position].pI.c} ${items[position].pI.new}"
        holder.txtdiscountPrice.text = "${items[position].pI.c} ${items[position].pI.old}"
        holder.txtdiscountPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        holder.txt_discountpersent.text =
            "Save " + "${items[position].pI.c} ${items[position].pI.da}" +
                    " (" + "${items[position].pI.dp}" + "%" + ")"

        holder.txtdescription.text= "${items[position].sdesc}"

        if (items[position].aI.size == 1) {
            Picasso.get()
                .load("http://207.244.244.64/AttributesImages/" + items[position].aI[0].image)
                .into(holder.vagnonvagIcon)

            holder.imgvagnonvagIcon.visibility = View.GONE
        } else if (items[position].aI.size == 2) {
            Picasso.get()
                .load("http://207.244.244.64/AttributesImages/" + items[position].aI[0].image)
                .into(holder.vagnonvagIcon)
            Picasso.get()
                .load("http://207.244.244.64/AttributesImages/" + items[position].aI[1].image)
                .into(holder.imgvagnonvagIcon)

        } else {
            holder.vagnonvagIcon.visibility = View.GONE
            holder.imgvagnonvagIcon.visibility = View.GONE
        }

        holder.unavailable.setTextColor(Color.parseColor(PreferenceManager.getFontColor(context)))
        if (items[position].iAv) {
            holder.unavailable.visibility = View.GONE
        } else {
            holder.unavailable.visibility = View.VISIBLE
        }

        holder.llmain.setOnClickListener {
            val intent = Intent(context, ProductDetailActivity::class.java)
            intent.putExtra("pId", "" + items[position]._id)
            context.startActivity(intent)
        }

        holder.txt_add.setOnClickListener {

            holder.txt_dispayDigit.text = (holder.txt_dispayDigit.text.toString().toInt() + 1).toString()

        }

        holder.txt_minus.setOnClickListener {

            if (holder.txt_dispayDigit.text.toString() == "0") {

            } else {
                holder.txt_dispayDigit.text = (holder.txt_dispayDigit.text.toString().toInt() - 1).toString()
                addToCartWishListListner.addToCartFromWishList(
                    items[position]._id,
                    holder.txt_dispayDigit.text.toString().toInt()
                )
            }
        }

        holder.relatedItems.setColorFilter(
            Color.parseColor(
                PreferenceManager.getBackgroundColor(
                    context
                )
            )
        )
        if (!items[position].relatedProduct.isNullOrEmpty()) {
            holder.relatedItems.setOnClickListener {
                items[position].relatedProduct?.let {
                    relatedProduct.openRelatedProduct(position, it)
                }
            }
        } else {
            holder.relatedItems.visibility = View.GONE
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
        val txt_itemview = view.txt_itemview!!
        val img_wishlist = view.img_wishlist
        val txt_newPrice = view.txt_newPrice!!
        val txtdiscountPrice = view.txtdiscountPrice!!
        val ll_addButton = view.ll_addButton!!
        val ll_plus_minus_ui = view.ll_plus_minus_ui!!
        val txt_add = view.txt_add!!
        val txt_dispayDigit = view.txt_dispayDigit!!
        val txt_minus = view.txt_minus!!
        val relatedItems = view.relatedItems!!
        val vagnonvagIcon = view.vagnonvagIcon!!
        val llmain = view.llmain!!
        val imgvagnonvagIcon = view.imgvagnonvagIcon!!
        val txt_discountpersent = view.txt_discountpersent!!
        val txtdescription = view.txtdescription!!
        val unavailable = view.unavailable!!
    }

    fun clearData() {

        items.clear()
    }

    interface RelatedProduct {
        fun openRelatedProduct(
            p: Int,
            relatedProductList: List<com.example.karma.responseModel.RelatedProduct>
        )
    }
}