package com.example.karma.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.karma.R
import com.example.karma.interfaces.NavigationMenuItemClickListner
import com.example.karma.models.Item
import com.example.karma.utils.PreferenceManager
import kotlinx.android.synthetic.main.navigationlist.view.*


class NavigationDrawerAdapter(
    private val context: Context,
    private var items: ArrayList<Item>,
    private var navigationMenuItemClickListner: NavigationMenuItemClickListner


) : RecyclerView.Adapter<NavigationDrawerAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.navigationlist,
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.text.text = items[position].name

        holder.text.setTextColor(Color.parseColor(PreferenceManager.getslideMenuFontColor(context)))


        holder.icon.setImageResource(items[position].image)

        ImageViewCompat.setImageTintList(
            holder.icon,
            ColorStateList.valueOf(Color.parseColor(PreferenceManager.getslideMenuIconColor(context)))
        )
        holder.ll_root_navigation_item.setOnClickListener {
            navigationMenuItemClickListner.navigationItemClick(position)

        }

        holder.ll_root_navigation_item.setBackgroundColor(
            Color.parseColor(
                PreferenceManager.getslideMenuBackGroundColor(
                    context
                )
            )
        )

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
        val icon = view.icon!!
        val text = view.text!!
        val ll_root_navigation_item = view.ll_root_navigation_item


    }
}