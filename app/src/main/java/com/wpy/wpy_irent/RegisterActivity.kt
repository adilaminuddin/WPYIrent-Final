package com.wpy.wpy_irent

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.wpy.wpy_irent.Api.ApiService
import com.wpy.wpy_irent.Auth.Register.RegisterRequest
import com.wpy.wpy_irent.Auth.Register.RegisterResponse
import com.wpy.wpy_irent.Auth.SharedPref
import com.wpy.wpy_irent.Auth.Utils
import com.wpy.wpy_irent.Auth.Utils.showInfoDialog
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity(), View.OnClickListener  {

    private lateinit var ed_nama: TextInputEditText
    private lateinit var ed_username: TextInputEditText
    private lateinit var ed_alamat: TextInputEditText
    private lateinit var ed_password: TextInputEditText
    private lateinit var btn_signup: MaterialButton
    private lateinit var btn_login: TextView
    val a: RegisterActivity = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.hide()

        ed_nama = findViewById(R.id.ed_name)
        ed_username = findViewById(R.id.ed_username)
        ed_alamat = findViewById(R.id.ed_alamat)
        ed_password = findViewById(R.id.ed_password)
        btn_signup = findViewById(R.id.btn_signup)
        btn_login = findViewById(R.id.txt_Login)
        btn_login.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        btn_signup.setOnClickListener(this)
    }
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_signup -> {
                if (validation()) {
                    val json = JSONObject()
                    json.put("nama", ed_nama.text.toString())
                    json.put("username", ed_username.text.toString())
                    json.put("password", ed_password.text.toString())
                    json.put("alamat", ed_alamat.text.toString())

                    ApiService.loginApiCall().doRegister(
                        RegisterRequest(
                            ed_nama.text.toString(),
                            ed_username.text.toString(),
                            ed_password.text.toString(),
                            ed_alamat.text.toString()
                        )
                    ).enqueue(object : Callback<RegisterResponse> {
                        override fun onResponse(
                            call: Call<RegisterResponse>,
                            response: Response<RegisterResponse>
                        ) {
                           // Log.d("Response::::", response.body().toString())
                            val registerResponse : RegisterResponse = response.body()!!
                            if (registerResponse.status){
                                Toast.makeText(applicationContext, response.body()!!.message, Toast.LENGTH_LONG).show()
                                finish()
                            }else{
                                Toast.makeText(applicationContext, response.body()!!.message, Toast.LENGTH_LONG).show()
                            }
                        }

                        override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                            showInfoDialog(a, "ERROR", "Mohon periksa koneksi internet anda!!")

                        }

                    })
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if(SharedPref.getInstance(this).isLoggedIn){
            SharedPref.getInstance(this).user
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    private fun validation(): Boolean {
        var value = true

        val nama = ed_nama.text.toString().trim()
        val alamat = ed_nama.text.toString().trim()
        val password = ed_password.text.toString().trim()
        val username = ed_username.text.toString().trim()

        if (nama.isEmpty()) {
            ed_nama.error = "Nama Lengkap Wajib Di isi"
            ed_nama.requestFocus()
            value = false
        }
        if (password.isEmpty()) {
            ed_password.error = "Password Wajib Di isi"
            ed_password.requestFocus()
            value = false
        }
        if (username.isEmpty()) {
            ed_username.error = "Username Diperlukan untuk Login"
            ed_username.requestFocus()
            value = false
        }
        if (alamat.isEmpty()) {
            ed_alamat.error = "Alamat Lengkap Diperlukan"
            ed_alamat.requestFocus()
            value = false
        }

        return value
    }
}