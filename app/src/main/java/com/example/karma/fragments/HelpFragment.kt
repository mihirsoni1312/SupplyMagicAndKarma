package com.example.karma.fragments

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.karma.R
import com.example.karma.activities.MainActivity
import com.example.karma.response.Helplist
import com.example.karma.utils.PreferenceManager
import com.example.karma.utils.utils
import kotlinx.android.synthetic.main.fragment_help.*
import kotlinx.android.synthetic.main.fragment_help.view.*
import retrofit2.Call
import java.util.*

class HelpFragment : Fragment() {
    var call: Call<Helplist>? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_help, container, false)

        view.btn_back_help.setOnClickListener {

            val mainIntent = Intent(context, MainActivity::class.java)
            mainIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(mainIntent)

        }
        return view
    }

    override fun onResume() {
        super.onResume()
        title_productList.setTextColor(Color.parseColor(activity?.let {
            PreferenceManager.getFontColor(
                it
            )
        }))
        if (PreferenceManager.getIsRestaurentApp(context!!)) {
            cvGlassOfWater.visibility = View.VISIBLE
            cvSendInvoice.visibility = View.VISIBLE
            cvSendMessage.visibility = View.VISIBLE
            cvTalkToUs.visibility = View.VISIBLE

        } else {
            cvGlassOfWater.visibility = View.GONE
            cvSendInvoice.visibility = View.GONE
            cvSendMessage.visibility = View.VISIBLE
            cvTalkToUs.visibility = View.VISIBLE

        }

        cvTalkToUs.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            var n = PreferenceManager.getVendorPhoneNumber(activity!!)
            intent.data = Uri.parse("tel:$n")
            startActivity(intent)
        }
        cvSendMessage.setOnClickListener {
            utils.showMessageAlert("This service currently unavailable",activity?.resources?.getString(R.string.app_name),activity)
        }
        Objects.requireNonNull((Objects.requireNonNull(activity) as AppCompatActivity).supportActionBar)?.hide()
    }

    override fun onStop() {
        super.onStop()
        Objects.requireNonNull((Objects.requireNonNull(activity) as AppCompatActivity).supportActionBar)?.show()
    }

}
