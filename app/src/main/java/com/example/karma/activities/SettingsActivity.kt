package com.example.karma.activities

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.core.widget.ImageViewCompat
import com.example.karma.R
import com.example.karma.base.BaseActivity
import com.example.karma.utils.PreferenceManager
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        title_setting.setTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))
        txt_terms.setTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))
        txt_privacy.setTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))
        txt_linces.setTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))


        ImageViewCompat.setImageTintList(
            img_term,
            ColorStateList.valueOf(Color.parseColor(PreferenceManager.getFontColor(this)))
        )

        ImageViewCompat.setImageTintList(
            img_privacy,
            ColorStateList.valueOf(Color.parseColor(PreferenceManager.getFontColor(this)))
        )

        ImageViewCompat.setImageTintList(
            img_licence,
            ColorStateList.valueOf(Color.parseColor(PreferenceManager.getFontColor(this)))
        )

        btn_back.setOnClickListener {

            onBackPressed()
        }
        ImageViewCompat.setImageTintList(
            btn_back,
            ColorStateList.valueOf(Color.parseColor(PreferenceManager.getFontColor(this)))
        )

        ll_termsandcondition.setOnClickListener {

            val intent: Intent = Intent(this, CMSActivity::class.java)
            intent.putExtra("type", "2")
            startActivity(intent)

        }

        ll_privacy.setOnClickListener {
            val intent: Intent = Intent(this, CMSActivity::class.java)
            intent.putExtra("type", "5")
            startActivity(intent)
        }

        ll_licence.setOnClickListener {

            val intent: Intent = Intent(this, CMSActivity::class.java)
            intent.putExtra("type", "6")
            startActivity(intent)
        }

    }

    override fun onBackPressed() {
        finish()
//        super.onBackPressed()
//        val mainIntent = Intent(this@SettingsActivity, MainActivity::class.java)
//        mainIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
//        startActivity(mainIntent)
    }


}
