@file:Suppress("DEPRECATION")

package com.wpy.wpy_irent.Auth

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.wpy.wpy_irent.Model.Paket.Paket
import com.wpy.wpy_irent.R
import com.yarolegovich.lovelydialog.LovelyStandardDialog


object Utils {
    var mProgressDialog: ProgressDialog? = null
    fun showSimpleProgressDialog(
        context: Context?,
        title: String?,
        msg: String?,
        isCancelable: Boolean
    ) {
        try {
            if (mProgressDialog == null) {
                mProgressDialog = ProgressDialog.show(context, title, msg)
                mProgressDialog?.setCancelable(isCancelable)
            }
            if (!mProgressDialog?.isShowing!!) {
                mProgressDialog?.show()
            }
        } catch (ie: Exception) {
            ie.printStackTrace()
        }
    }

    fun showSimpleProgressDialog(context: Context?) {
        showSimpleProgressDialog(context, null, "Loading...", true)
    }

    fun removeSimpleProgressDialog() {
        try {
            if (mProgressDialog != null) {
                if (mProgressDialog!!.isShowing) {
                    mProgressDialog!!.dismiss()
                    mProgressDialog = null
                }
            }
        } catch (ie: java.lang.Exception) {
            ie.printStackTrace()
        }
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivity = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = connectivity.allNetworkInfo
        for (networkInfo in info) {
            if (networkInfo.state == NetworkInfo.State.CONNECTED){
                val capabilities = connectivity.getNetworkCapabilities(connectivity.activeNetwork)
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                        return true
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                        return true
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                        Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                        return true
                    }
                }
                return true
            }
        }
        return false
    }

    fun sendPaketToActivity(
        c: Context, paket: Paket?,
        clazz: Class<*>?
    ) {
        val i = Intent(c, clazz)
            i.putExtra("Paket",paket)
        c.startActivity(i)
    }

    fun showInfoDialog(
        activity: AppCompatActivity, title: String?,
        message: String?
    ) {
        LovelyStandardDialog(activity, LovelyStandardDialog.ButtonLayout.HORIZONTAL)
            .setTopColorRes(R.color.colorAccent)
            .setButtonsColorRes(R.color.colorPrimaryDark)
            .setIcon(R.drawable.input)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(
                "Ok"
            ) { activity.finish() }
//            .setNeutralButton(
//                "Home"
//            ) {
//                openActivity(
//                    activity,
//                    MainActivity::class.java
//                )
//            }
            .setNegativeButton(
                "Kembali"
            ) { v: View? -> activity.finish() }
            .show()
    }
    fun openActivity(c: Context, clazz: Class<*>?) {
        val intent = Intent(c, clazz)
        c.startActivity(intent)
    }

    fun receivePaket(intent: Intent): Paket? {
        try {
            return intent.getSerializableExtra("Paket") as Paket
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
//    fun receivePonsel(intent: Intent, c: Context): Ponsel? {
//        try {
//            return intent.getSerializableExtra("Ponsel") as Ponsel
//        } catch (e:Exception){
//            e.printStackTrace()
//        }
//        return null
//    }


}