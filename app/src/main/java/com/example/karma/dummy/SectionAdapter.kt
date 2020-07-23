package com.example.karma.dummy

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.karma.R
import com.shuhart.stickyheader.StickyAdapter
import java.util.*

class SectionAdapter // inflater = LayoutInflater.from(context);
// this.context = context;
    (
    context: Context?,
    private val sectionArrayList: ArrayList<Section>
) :
    StickyAdapter<RecyclerView.ViewHolder, RecyclerView.ViewHolder?>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == LAYOUT_HEADER) {
            HeaderViewholder(
                inflater.inflate(
                    R.layout.recycler_view_header_item,
                    parent,
                    false
                )
            )
        } else {
            ItemViewHolder(
                inflater.inflate(
                    R.layout.recycler_view_item,
                    parent,
                    false
                )
            )
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        if (sectionArrayList[position].isHeader) {
            (holder as HeaderViewholder).textView.text = sectionArrayList[position].name
        } else {
            (holder as ItemViewHolder).textView.text = sectionArrayList[position].name
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (sectionArrayList[position].isHeader) {
            LAYOUT_HEADER
        } else LAYOUT_CHILD
    }

    override fun getItemCount(): Int {
        return sectionArrayList.size
    }

    override fun getHeaderPositionForItem(itemPosition: Int): Int {
        return sectionArrayList[itemPosition].sectionPosition()
    }

    @SuppressLint("SetTextI18n")
    override fun onBindHeaderViewHolder(
        holder: RecyclerView.ViewHolder,
        headerPosition: Int
    ) {
        (holder as HeaderViewholder).textView.text = sectionArrayList[headerPosition].name
    }

    override fun onCreateHeaderViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return createViewHolder(parent, LAYOUT_HEADER)
    }

    class HeaderViewholder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var textView: TextView

        init {
            textView = itemView.findViewById(R.id.text_view)
        }
    }

    class ItemViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var textView: TextView
        var txtdiscount: TextView

        init {
            textView = itemView.findViewById(R.id.txt_itemview)
            txtdiscount = itemView.findViewById(R.id.txtdiscountPrice)
            txtdiscount.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }


    }

    companion object {
        private const val LAYOUT_HEADER = 0
        private const val LAYOUT_CHILD = 1
    }

}
