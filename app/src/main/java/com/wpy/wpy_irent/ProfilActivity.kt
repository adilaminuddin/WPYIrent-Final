package com.wpy.wpy_irent

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.storage.StorageManager
import android.telephony.TelephonyManager
import android.util.Log
import androidx.core.app.ActivityCompat
import com.wpy.wpy_irent.Api.ApiService
import com.wpy.wpy_irent.Auth.SharedPref
import com.wpy.wpy_irent.Model.User.UserResponse
import kotlinx.android.synthetic.main.activity_histori.*
import kotlinx.android.synthetic.main.activity_profil.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfilActivity : AppCompatActivity() {

    private val userId: Int? = SharedPref.getInstance(this).user.id_user
    var telephonyManager: TelephonyManager? = null
    var sm:StorageManager?=null
    private val REQUEST_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil)

        getUser()
        deviceInfo()
    }

    private fun deviceInfo() {
        telephonyManager = this.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        sm = this.getSystemService(Context.STORAGE_SERVICE) as StorageManager
        @Suppress("DEPRECATED_IDENTITY_EQUALS")
        if (ActivityCompat.checkSelfPermission(
                this@ProfilActivity,
                Manifest.permission.READ_PHONE_STATE
            ) !== PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@ProfilActivity,
                arrayOf(
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                REQUEST_CODE
            )
            return
        }
        val manufc = (Build.MANUFACTURER)
        val model = (Build.MODEL)
        val os = (Build.VERSION.RELEASE)
        val ids =(Build.ID)

        tvOsPon.setText(os)
        tvMerkPon.setText(manufc)
        tvJenisPon.setText(model)
        tvIdsPon.setText(ids)
    }

    private fun getUser() {
        ApiService.loginApiCall().getUser(userId)
            .enqueue(object : Callback<UserResponse> {
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    if (response.body()!!.status) {
                        // txtid.text = response.body()!!.user.id_user.toString()
                        tvNamaLeng.text = response.body()!!.user.nama
                        tvUserN.text = response.body()!!.user.username
                        tvAlamatP.text = response.body()!!.user.alamat
                        //tvhiIdUser.text = userId.toString()
                        Log.d(
                            "Response::::",
                            SharedPref.getInstance(applicationContext).isLoggedIn.toString()
                        )
                        SharedPref.getInstance(applicationContext).saveUser(response.body()!!.user)
                            .toString()
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
                    Log.d("error::::", t.message)
                }
            })
    }
}