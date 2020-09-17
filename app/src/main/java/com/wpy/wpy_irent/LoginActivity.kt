package com.wpy.wpy_irent

import android.Manifest.permission.READ_PHONE_STATE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.storage.StorageManager
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatTextView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.wpy.wpy_irent.Api.ApiService
import com.wpy.wpy_irent.Auth.*
import com.wpy.wpy_irent.Auth.SharedPref
import com.wpy.wpy_irent.Auth.Login.LoginRequest
import com.wpy.wpy_irent.Auth.Login.LoginResponse
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import androidx.core.app.ActivityCompat.checkSelfPermission
import androidx.core.app.ActivityCompat.requestPermissions
import com.wpy.wpy_irent.Auth.Utils.showInfoDialog

class LoginActivity : AppCompatActivity() {

    private lateinit var ed_username : TextInputEditText
    private lateinit var ed_password : TextInputEditText
    private lateinit var btn_signin : MaterialButton
    private lateinit var txt_sign_up : AppCompatTextView
//    var tvIds: TextView? = null
    val a: LoginActivity = this

    var telephonyManager: TelephonyManager? = null
    var sm:StorageManager?=null
    private val REQUEST_CODE = 101

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        deviceInfo()
        onRequestPermissionsResult(REQUEST_CODE, arrayOf(READ_PHONE_STATE), intArrayOf(REQUEST_CODE))

        ed_username = findViewById(R.id.edl_username)
        ed_password = findViewById(R.id.edl_password)
        btn_signin = findViewById(R.id.btn_signin)
        txt_sign_up = findViewById(R.id.txt_sign_up)
//        tvIds = findViewById(R.id.tvIds)

        txt_sign_up.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
        btn_signin.setOnClickListener {
            try {
                deviceInfo()
                login()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }


    }

    override fun onStart() {
        super.onStart()
        if(SharedPref.getInstance(this).isLoggedIn){
            if(HistoriPref.getInstance(this).berlangganan){
                HistoriPref.getInstance(this).paket
                val intent = Intent(applicationContext, PlayActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            } else {
                SharedPref.getInstance(this).user
                val intent = Intent(applicationContext, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun deviceInfo() {
        telephonyManager = this.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        sm = this.getSystemService(Context.STORAGE_SERVICE) as StorageManager
        @Suppress("DEPRECATED_IDENTITY_EQUALS")
        if (checkSelfPermission(this@LoginActivity, READ_PHONE_STATE
            ) !== PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(this@LoginActivity, arrayOf(READ_PHONE_STATE, WRITE_EXTERNAL_STORAGE), REQUEST_CODE
            )
            return
        }
        val reqString = (Build.MANUFACTURER)
        val reqString2 = (Build.MODEL + ", OS : " + Build.VERSION.RELEASE)
        val ids =(Build.ID)
        tvIds.text = ids

        Log.d("Merek ::",reqString)
        Log.d("Model ::",reqString2)
        //Log.d("ID ::",ids)
    }


    @Throws(IOException::class, JSONException::class)
    private fun login() {
        Utils.showSimpleProgressDialog(this@LoginActivity)
        Utils.isNetworkAvailable(this@LoginActivity)
        val json = JSONObject()
        json.put("username", edl_username.text.toString())
        json.put("password", edl_password.text.toString())
        if (validation()) {
            ApiService.loginApiCall().doLogin(
            LoginRequest(
                username = edl_username.text.toString(),
                password = edl_password.text.toString()
            )
        ).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.body()!!.status){
                    Utils.removeSimpleProgressDialog()
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.putExtra("id_user",response.body()!!.user.id_user)
                    Log.d("Cek Id :::", response.body()!!.user.id_user.toString())
                    SharedPref.getInstance(applicationContext).saveUser(response.body()!!.user).toString()
                    startActivity(intent)
                    Log.d("AADDD", SharedPref.getInstance(applicationContext).isLoggedIn.toString())
                }else{
                    Utils.removeSimpleProgressDialog()
                    Toast.makeText(applicationContext, response.body()!!.message, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                showInfoDialog(a, "ERROR", "Mohon periksa koneksi internet anda(lgn)!!")
                Utils.removeSimpleProgressDialog()
            }

        })
        }
    }

    private fun validation(): Boolean {
        var value = true
        val password = edl_password.text.toString().trim()
        val username = edl_username.text.toString().trim()

        if (password.isEmpty()) {
            edl_password.error = "Password Tidak Boleh Kosong"
            edl_password.requestFocus()
            value = false
        }

        if (username.isEmpty()) {
            edl_username.error = "Username Dibutuhkan"
            edl_username.requestFocus()
            value = false
        }

        return value
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted.", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }
}