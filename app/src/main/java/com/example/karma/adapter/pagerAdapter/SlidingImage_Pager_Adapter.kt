package com.example.karma.adapter.pagerAdapter

import android.content.Context
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.example.karma.R
import com.example.karma.interfaces.SliderClickListner
import com.example.karma.responseModel.SliderInfoModel
import com.squareup.picasso.Picasso
import java.util.*

class SlidingImage_Pager_Adapter(
    private var context: Context,
    private val imageModelArrayList: ArrayList<SliderInfoModel>,
    private val SliderClickListner: SliderClickListner
) : PagerAdapter() {
    private val inflater: LayoutInflater
    override fun destroyItem(
        container: ViewGroup,
        position: Int,
        `object`: Any
    ) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return imageModelArrayList.size
    }

    override fun instantiateItem(view: ViewGroup, position: Int): Any {
        val imageLayout: View =
            inflater.inflate(R.layout.slidingimage_layout, view, false)!!
        val imageView = imageLayout
            .findViewById<ImageView>(R.id.image)
        Picasso.get()
            .load("http://207.244.244.64/CategoryImages/" + imageModelArrayList[position].image)
            .into(imageView)
        view.addView(imageLayout, 0)
        imageView!!.setOnClickListener {
            SliderClickListner.sliderClick(
                imageModelArrayList[position].catId,
                imageModelArrayList[position].name
            )
        }
        return imageLayout
    }

    override fun isViewFromObject(
        view: View,
        `object`: Any
    ): Boolean {
        return view == `object`
    }

    override fun restoreState(
        state: Parcelable?,
        loader: ClassLoader?
    ) {
    }

    override fun saveState(): Parcelable? {
        return null
    }

    init {
        inflater = LayoutInflater.from(context)
    }
}