package com.example.karma.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.karma.response.PCK
import com.example.karma.utils.MyApp.Companion.getApplicationContext

object PreferenceManager {
    private var mSharedPreferences: SharedPreferences? = null
    private var mEditor: SharedPreferences.Editor? = null
    private val SHAREDPREFERENCE = "MY_SHAREDPREFERENCE"


    fun removePrefSingle(context: Context, keyToRemove: String?) {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        mEditor = mSharedPreferences?.edit()
        mEditor?.remove(keyToRemove)
        mEditor?.apply()
    }

    fun removePref(context: Context) {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        mEditor = mSharedPreferences!!.edit().clear()
        mEditor?.apply()
    }

    fun setPref(context: Context, value: String, key: String) {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        mEditor = mSharedPreferences?.edit()
        mEditor?.putString(key, value)
        mEditor?.apply()
    }

    fun getPref(context: Context, key: String): String? {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        return mSharedPreferences?.getString(key, "")
    }

    /**
     * ID save
     */
    fun setId(context: Context, value: String?) {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        mEditor = mSharedPreferences?.edit()
        if (value != null) {
            mEditor?.putString(VariableBag._id, value)
        }
        mEditor?.apply()
    }

    /**
     * ID get
     */
    fun getId(context: Context): String {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        return mSharedPreferences?.getString(VariableBag._id, "").toString()
    }


    /**
     * appId save
     */
    fun setAppId(context: Context, value: String?) {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        mEditor = mSharedPreferences?.edit()
        if (value != null) {
            mEditor?.putString(VariableBag.appId, value)
        }
        mEditor?.apply()
    }

    /**
     * appId get
     */
    fun getAppId(context: Context): String {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        return mSharedPreferences?.getString(VariableBag.appId, "").toString()
    }


    /**
     * appName save
     */
    fun setAppName(context: Context, value: String?) {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        mEditor = mSharedPreferences?.edit()
        if (value != null) {
            mEditor?.putString(VariableBag.appName, value)
        }
        mEditor?.apply()
    }

    /**
     * appName get
     */
    fun getAppName(context: Context): String {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        return mSharedPreferences?.getString(VariableBag.appName, "").toString()
    }

    /**
     * backgroundColor save
     */
    fun setBackgroundColor(context: Context, value: String?) {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        mEditor = mSharedPreferences?.edit()
        if (value != null) {
            mEditor?.putString(VariableBag.backgroundColor, value)
        }
        mEditor?.apply()
    }

    /**
     * backgroundColor get
     */
    fun getBackgroundColor(context: Context): String {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        return mSharedPreferences?.getString(VariableBag.backgroundColor, "").toString()
    }

    /**
     * fontColor save
     */
    fun setFontColor(context: Context, value: String?) {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        mEditor = mSharedPreferences?.edit()
        if (value != null) {
            mEditor?.putString(VariableBag.fontColor, value)
        }
        mEditor?.apply()
    }

    /**
     * fontColor get
     */
    fun getFontColor(context: Context): String {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        return mSharedPreferences?.getString(VariableBag.fontColor, "").toString()
    }

    /**
     * buttonFontColor save
     */
    fun setButtonFontColor(context: Context, value: String?) {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        mEditor = mSharedPreferences?.edit()
        if (value != null) {
            mEditor?.putString(VariableBag.buttonFontColor, value)
        }
        mEditor?.apply()
    }

    /**
     * buttonFontColor get
     */
    fun getButtonFontColor(context: Context): String {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        return mSharedPreferences?.getString(VariableBag.buttonFontColor, "").toString()
    }

    /**
     * loginScreeLogo save
     */
    fun setLoginScreeLogo(context: Context, value: String?) {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        mEditor = mSharedPreferences?.edit()
        if (value != null) {
            mEditor?.putString(VariableBag.loginScreeLogo, value)
        }
        mEditor?.apply()
    }

    /**
     * loginScreeLogo get
     */
    fun getLoginScreeLogo(context: Context): String {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        return mSharedPreferences?.getString(VariableBag.loginScreeLogo, "").toString()
    }

    /**
     * registrationScreenLogo save
     */
    fun setRegistrationScreenLogo(context: Context, value: String?) {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        mEditor = mSharedPreferences?.edit()
        if (value != null) {
            mEditor?.putString(VariableBag.registrationScreenLogo, value)
        }
        mEditor?.apply()
    }

    /**
     * registrationScreenLogo get
     */
    fun getRegistrationScreenLogo(context: Context): String {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        return mSharedPreferences?.getString(VariableBag.registrationScreenLogo, "").toString()
    }

    /**
     * email save
     */
    fun setEmail(context: Context, value: String?) {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        mEditor = mSharedPreferences?.edit()
        if (value != null) {
            mEditor?.putString(VariableBag.email, value)
        }
        mEditor?.apply()
    }

    /**
     * email get
     */
    fun getEmail(context: Context): String {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        return mSharedPreferences?.getString(VariableBag.email, "").toString()
    }

    /**
     * mobileNumber save
     */
    fun setmobileNumber(context: Context, value: String?) {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        mEditor = mSharedPreferences?.edit()
        if (value != null) {
            mEditor?.putString(VariableBag.mobileNumber, value)
        }
        mEditor?.apply()
    }

    /**
     * mobileNumber get
     */
    fun getmobileNumber(context: Context): String {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        return mSharedPreferences?.getString(VariableBag.mobileNumber, "").toString()
    }

    /**
     * name save
     */
    fun setname(context: Context, value: String?) {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        mEditor = mSharedPreferences?.edit()
        if (value != null) {
            mEditor?.putString(VariableBag.name, value)
        }
        mEditor?.apply()
    }

    /**
     * name get
     */
    fun getname(context: Context): String {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        return mSharedPreferences?.getString(VariableBag.name, "").toString()
    }

    /**
     * address save
     */
    fun setaddress(context: Context, value: String?) {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        mEditor = mSharedPreferences?.edit()
        if (value != null) {
            mEditor?.putString(VariableBag.address, value)
        }
        mEditor?.apply()
    }

    /**
     * address get
     */
    fun getaddress(context: Context): String {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        return mSharedPreferences?.getString(VariableBag.address, "").toString()
    }

    /**
     * zipCode save
     */
    fun setzipCode(context: Context, value: String?) {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        mEditor = mSharedPreferences?.edit()
        if (value != null) {
            mEditor?.putString(VariableBag.zipCode, value)
        }
        mEditor?.apply()
    }

    /**
     * zipCode get
     */
    fun getzipCode(context: Context): String {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        return mSharedPreferences?.getString(VariableBag.zipCode, "").toString()
    }

    /**
     * isLogin save
     */
    fun setisLogin(context: Context, value: Boolean?) {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        mEditor = mSharedPreferences?.edit()
        if (value != null) {
            mEditor?.putBoolean(VariableBag.isLogin, value)
        }
        mEditor?.apply()
    }

    /**
     * isLogin get
     */
    fun getisLogin(context: Context): Boolean {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        return mSharedPreferences!!.getBoolean(VariableBag.isLogin, false)
    }


    /**
     * userImage save
     */
    fun setuserImage(context: Context, value: String?) {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        mEditor = mSharedPreferences?.edit()
        if (value != null) {
            mEditor?.putString(VariableBag.userImage, value)
        }
        mEditor?.apply()
    }

    /**
     * userImage get
     */
    fun getuserImage(context: Context): String {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        return mSharedPreferences?.getString(VariableBag.userImage, "").toString()
    }


    /**
     * slideMenuBackGroundColor save
     */
    fun setslideMenuBackGroundColor(context: Context, value: String?) {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        mEditor = mSharedPreferences?.edit()
        if (value != null) {
            mEditor?.putString(VariableBag.slideMenuBackGroundColor, value)
        }
        mEditor?.apply()
    }

    /**
     * slideMenuBackGroundColor get
     */
    fun getslideMenuBackGroundColor(context: Context): String {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        return mSharedPreferences?.getString(VariableBag.slideMenuBackGroundColor, "").toString()
    }


    /**
     * slideMenuFontColor save
     */
    fun setslideMenuFontColor(context: Context, value: String?) {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        mEditor = mSharedPreferences?.edit()
        if (value != null) {
            mEditor?.putString(VariableBag.slideMenuFontColor, value)
        }
        mEditor?.apply()
    }

    /**
     * slideMenuFontColor get
     */
    fun getslideMenuFontColor(context: Context): String {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        return mSharedPreferences?.getString(VariableBag.slideMenuFontColor, "").toString()
    }


    /**
     * slideMenuIconColor save
     */
    fun setslideMenuIconColor(context: Context, value: String?) {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        mEditor = mSharedPreferences?.edit()
        if (value != null) {
            mEditor?.putString(VariableBag.slideMenuIconColor, value)
        }
        mEditor?.apply()
    }

    /**
     * slideMenuIconColor get
     */
    fun getslideMenuIconColor(context: Context): String {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        return mSharedPreferences?.getString(VariableBag.slideMenuIconColor, "").toString()
    }

    /**
     * setLoginAbusulatPath save
     */
    fun setLoginAbusulatPath(context: Context, value: String?) {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        mEditor = mSharedPreferences?.edit()
        if (value != null) {
            mEditor?.putString(VariableBag.loginAbsulatePath, value)
        }
        mEditor?.apply()
    }

    /**
     * getLoginAbusulatPath get
     */
    fun getLoginAbusulatPath(context: Context): String {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        return mSharedPreferences?.getString(VariableBag.loginAbsulatePath, "").toString()
    }

    /**
     * setRegisterAbusulatPath save
     */
    fun setRegisterAbusulatPath(context: Context, value: String?) {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        mEditor = mSharedPreferences?.edit()
        if (value != null) {
            mEditor?.putString(VariableBag.registerAbsulatePath, value)
        }
        mEditor?.apply()
    }

    /**
     * getLoginAbusulatPath get
     */
    fun getRegisterAbusulatPath(context: Context): String {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        return mSharedPreferences?.getString(VariableBag.registerAbsulatePath, "").toString()
    }

    /**
     * setVendorId save
     */
    fun setVendorId(context: Context, value: String?) {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        mEditor = mSharedPreferences?.edit()
        if (value != null) {
            mEditor?.putString(VariableBag.vendorId, value)
        }
        mEditor?.apply()
    }

    fun setVendorPhoneNumber(context: Context, value: String?) {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        mEditor = mSharedPreferences?.edit()
        if (value != null) {
            mEditor?.putString(VariableBag.vendorPhoneNumber, value)
        }
        mEditor?.apply()
    }

    fun setSck(context: Context, value: String?) {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        mEditor = mSharedPreferences?.edit()
        if (value != null) {
            mEditor?.putString(VariableBag.sck, value)
        }
        mEditor?.apply()
    }

    fun getSck(context: Context): String {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        return mSharedPreferences?.getString(VariableBag.sck, "").toString()
    }

    fun getVendorPhoneNumber(context: Context): String {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        return mSharedPreferences?.getString(VariableBag.vendorPhoneNumber, "").toString()
    }

    fun setTempVendorId(context: Context, value: String?) {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        mEditor = mSharedPreferences?.edit()
        if (value != null) {
            mEditor?.putString(VariableBag.vendorIdTemp, value)
        }
        mEditor?.apply()
    }

    /**
     * getVendorId get
     */
    fun getVendorId(context: Context): String {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        return mSharedPreferences?.getString(VariableBag.vendorId, "").toString()
    }

    fun getTempVendorId(context: Context): String {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        return mSharedPreferences?.getString(VariableBag.vendorIdTemp, "").toString()
    }


    fun setIsRestaurentApp(context: Context, value: Boolean) {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        mEditor = mSharedPreferences?.edit()
        if (value != null) {
            mEditor?.putBoolean(VariableBag.isRestaurentApp, value)
        }
        mEditor?.apply()
    }

    fun getIsRestaurentApp(context: Context): Boolean {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        return mSharedPreferences!!.getBoolean(VariableBag.isRestaurentApp, false)
    }

    fun setvendorName(context: Context, value: String) {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        mEditor = mSharedPreferences?.edit()
        if (value != null) {
            mEditor?.putString(VariableBag.vendorName, value)
        }
        mEditor?.apply()
    }

    fun getvendorName(context: Context): String? {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        return mSharedPreferences!!.getString(VariableBag.vendorName, "")
    }

    fun setvendorImage(context: Context, value: String) {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        mEditor = mSharedPreferences?.edit()
        if (value != null) {
            mEditor?.putString(VariableBag.vendorImage, value)
        }
        mEditor?.apply()
    }

    fun getvendorImage(context: Context): String? {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        return mSharedPreferences!!.getString(VariableBag.vendorImage, "")
    }

    fun setPlsk(context: Context, value: String) {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        mEditor = mSharedPreferences?.edit()
        if (value != null) {
            mEditor?.putString(VariableBag.plsk, value)
        }
        mEditor?.apply()
    }

    fun getPlsk(context: Context): String? {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        return mSharedPreferences!!.getString(VariableBag.plsk, "")
    }


    fun setDefaultPaymentType(context: Context, value: String) {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        mEditor = mSharedPreferences?.edit()
        if (value != null) {
            mEditor?.putString(VariableBag.defaultPaymentType, value)
        }
        mEditor?.apply()
    }

    fun getdefaultPaymentType(context: Context): String? {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        return mSharedPreferences!!.getString(VariableBag.defaultPaymentType, "")
    }

    fun setDefaultTypeBank(context: Context, value: PCK) {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        mEditor = mSharedPreferences?.edit()
        if (value != null) {
            mEditor?.putString(VariableBag.defaultTypeBank, utils.toJson(value))
        }
        mEditor?.apply()
    }

    fun getDefaultTypeBank(context: Context): String? {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        return mSharedPreferences!!.getString(VariableBag.defaultTypeBank, "")
    }

    fun setPaymentTypeName(context: Context, value: String) {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        mEditor = mSharedPreferences?.edit()
        if (value != null) {
            mEditor?.putString(VariableBag.paymentTypeName, value)
        }
        mEditor?.apply()
    }

    fun getPaymentTypeName(context: Context): String? {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        return mSharedPreferences!!.getString(VariableBag.paymentTypeName, "")
    }
    fun setPCK(context: Context, value:List<PCK>) {

        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        mEditor = mSharedPreferences?.edit()
        if (value != null) {
            mEditor?.putString(VariableBag.Pck, utils.toJson(value))
        }
        mEditor?.apply()
    }

    fun getPCK(context: Context): String? {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        return mSharedPreferences!!.getString(VariableBag.Pck, "")

    }
  fun setvandorAdd(context: Context, value:String) {

      mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
      mEditor = mSharedPreferences?.edit()
      if (value != null) {
          mEditor?.putString(VariableBag.vandorAdd, value)
      }
      mEditor?.apply()
    }

    fun getvandorAdd(context: Context): String? {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        return mSharedPreferences!!.getString(VariableBag.vandorAdd, "")

    }
  fun setsTokan(context: Context, value:String) {

      mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
      mEditor = mSharedPreferences?.edit()
      if (value != null) {
          mEditor?.putString(VariableBag.sTokan, value)
      }
      mEditor?.apply()
    }

    fun getsTokan(): String? {
        mSharedPreferences = getApplicationContext().getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        return mSharedPreferences!!.getString(VariableBag.sTokan, "")

    }

    fun clear(context: Context) {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        mEditor = mSharedPreferences?.edit()
        mEditor?.remove(VariableBag._id)
        mEditor?.remove(VariableBag.isLogin)
        mEditor?.remove(VariableBag.email)
        mEditor?.remove(VariableBag.mobileNumber)
        mEditor?.remove(VariableBag.name)
        mEditor?.remove(VariableBag.address)
        mEditor?.remove(VariableBag.zipCode)
        mEditor?.remove(VariableBag.userImage)
        mEditor?.remove(VariableBag.loginAbsulatePath)
        mEditor?.remove(VariableBag.registerAbsulatePath)
        mEditor?.remove(VariableBag.vendorId)
        mEditor?.remove(VariableBag.vendorName)
        mEditor?.remove(VariableBag.vendorImage)
        mEditor?.remove(VariableBag.sTokan)
        mEditor?.remove(VariableBag.sck)

        mEditor?.apply()

    }
}