package com.example.karma.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.karma.R
import com.example.karma.activities.ProductDetailActivity
import com.example.karma.interfaces.DeleteCartClickListner
import com.example.karma.responseModel.productByProductId.ProductInfoCartModel
import com.example.karma.utils.PreferenceManager
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_view_item.view.*
import kotlinx.android.synthetic.main.row_cartlist.view.*
import kotlinx.android.synthetic.main.row_cartlist.view.llmain
import kotlinx.android.synthetic.main.row_cartlist.view.txt_add
import kotlinx.android.synthetic.main.row_cartlist.view.txt_discountpersent
import kotlinx.android.synthetic.main.row_cartlist.view.txt_dispayDigit
import kotlinx.android.synthetic.main.row_cartlist.view.txt_minus
import kotlinx.android.synthetic.main.row_cartlist.view.txtdiscountPrice
import kotlinx.android.synthetic.main.row_search.view.*


class CartListAdapter(
    private val context: Context,
    private var items: ArrayList<ProductInfoCartModel>,
    private var deleteCartClickListner: DeleteCartClickListner,
    private var totalChange: TotalChange

) : RecyclerView.Adapter<CartListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.row_cartlist,
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txt_productTitle.text = items[position].name
        holder.txt_productTitle.setTextColor(
            Color.parseColor(
                PreferenceManager.getBackgroundColor(
                    context
                )
            )
        )
        Picasso.get().load("http://207.244.244.64/ItemImages/" + items[position].i1)
            .into(holder.img_cart)
        holder.txt_old_price.text = "${items[position].pI.c} ${items[position].pI.old}"
        holder.txt_new_price.text = "${items[position].pI.c} ${items[position].pI.new}"
        holder.txt_dispayDigit.text = items[position].qty.toString()
        holder.txt_headerPrice.text = "$ %.2f".format(items[position].qty.toFloat() * items[position].pI.new.toFloat())
        holder.txt_discountpersent.text =
            "Save " + "${items[position].pI.c} ${items[position].pI.da}" +
                    " (" + "${items[position].pI.dp}" + "%" + ")"
//        holder.txt_headerPrice.text = items[position].pI.new

        holder.txt_productdesc.text= "${items[position].sdesc}"

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

        holder.img_deleteCart.setOnClickListener {
            deleteCartClickListner.deleteCart(items[position]._id, items.size)
            items.removeAt(position)
            notifyDataSetChanged()
        }
       holder.unavailable.setTextColor(Color.parseColor(PreferenceManager.getFontColor(context)))
        if (items[position].iAv) {
            holder.unavailable.visibility = View.GONE
        } else {
            holder.unavailable.visibility = View.VISIBLE
        }

        holder.txt_add.setOnClickListener {
            var a = holder.txt_dispayDigit.text.toString().toInt()
            a += 1
            items[position].qty = a.toInt()
            holder.txt_headerPrice.text =
                "${items[position].pI.c} %.2f".format(a * items[position].pI.new.toFloat())
            holder.txt_dispayDigit.text = a.toString()
            totalChange.totalChangeListener(items[position]._id, a)
        }

        holder.txt_minus.setOnClickListener {
            var a = holder.txt_dispayDigit.text.toString().toInt()
            if (a > 1) {
                a -= 1
                items[position].qty = a.toInt()
                holder.txt_headerPrice.text =
                    "${items[position].pI.c} %.2f".format(a * items[position].pI.new.toFloat())
                holder.txt_dispayDigit.text = a.toString()
                totalChange.totalChangeListener(items[position]._id, a)

            } else if (a <= 1) {
                deleteCartClickListner.deleteCart(items[position]._id, items.size)
                items.removeAt(position)
                notifyDataSetChanged()
            }
        }

        holder.ll_productRow_search.setOnClickListener {
            val intent = Intent(context, ProductDetailActivity::class.java)
            intent.putExtra("pId", "" + items[position]._id)
            context.startActivity(intent)
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
        val txt_productTitle = view.txt_productTitle!!
        val txt_productdesc = view.txt_productdesc!!
        val img_cart = view.img_cart
        val txt_new_price = view.txt_new_price!!
        val txt_headerPrice = view.txt_headerPrice!!
        val txt_add = view.txt_add!!
        val txt_dispayDigit = view.txt_dispayDigit!!
        val txt_minus = view.txt_minus!!
        val img_deleteCart = view.img_deleteCart!!
        val ll_productRow_search = view.llmain!!
        val txt_old_price = view.txtdiscountPrice!!
        val txt_discountpersent = view.txt_discountpersent!!
        val vagnonvagIcon = view.vagnonvagIconcart!!
        val imgvagnonvagIcon = view.imgvagnonvagIconcart!!
        val unavailable = view.unavailablecart!!
    }

    fun getTotal(): Float {
        var total = 0f
        for (a in items) {
            if (total == 0f) {
                total = a.qty.toFloat() * a.pI.new.toFloat()
            } else {
                total += a.qty.toFloat() * a.pI.new.toFloat()
            }

        }
        return total
    }

    fun checkItemAvalable(): Boolean {

        var notavalableProduct = ""

        for (a in items) {
            if (!a.iAv) {

                if (notavalableProduct.equals("")) {
                    notavalableProduct = a.name
                } else {
                    notavalableProduct += "," + a.name
                }
            }
        }
        if (!notavalableProduct.equals("")) {
            Toast.makeText(
                context, "Please remove unavailable items", Toast.LENGTH_SHORT
            ).show()
            return false
        }
        return true
    }

    interface TotalChange {
        fun totalChangeListener(productId: String, qty: Int)
    }
}