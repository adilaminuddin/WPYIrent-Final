package com.wpy.wpy_irent

import android.app.PendingIntent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.wpy.wpy_irent.Adapter.PaketAdapter
import com.wpy.wpy_irent.AlarmNotification.AlarmSoundService
import com.wpy.wpy_irent.AlarmNotification.NotificationUtil
import com.wpy.wpy_irent.Api.ApiList
import com.wpy.wpy_irent.Api.ApiService
import com.wpy.wpy_irent.Auth.HistoriPref
import com.wpy.wpy_irent.Auth.SharedPref
import com.wpy.wpy_irent.Auth.Utils.showInfoDialog
import com.wpy.wpy_irent.Model.Paket.Paket
import com.wpy.wpy_irent.Model.Paket.PaketResponse
import com.wpy.wpy_irent.Model.Ponsel.DigunakanRequest
import com.wpy.wpy_irent.Model.Ponsel.PonselResponse
import com.wpy.wpy_irent.Model.User.UserResponse
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private var pAdapter: PaketAdapter? = null
    var allPagesPaket: ArrayList<Paket> = ArrayList<Paket>()
    private var currentPagePaket: ArrayList<Paket>? = ArrayList()
    private var layoutManager: LinearLayoutManager? = null
    private val a: MainActivity = this

    private val userId: Int? = SharedPref.getInstance(this).user.id_user
    private lateinit var txtid: TextView
    private lateinit var txt_name: TextView
    private lateinit var txt_alamat: TextView
    private var btnlogout: LinearLayout? = null
    private var btnProfil: LinearLayout? = null
    private var btnHistori: LinearLayout? = null
    var pendingIntent: PendingIntent? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        txtid= findViewById(R.id.tvIdUser)
        val getInterval = intent.getStringExtra("id_user")
        txtid.text =  getInterval
        txt_name = findViewById(R.id.tv_username)
        txt_alamat = findViewById(R.id.tv_alamat)
        btnlogout= findViewById(R.id.btn_logout)
        btnProfil= findViewById(R.id.tbProfil)
        btnHistori= findViewById(R.id.tbHistori)

        getUser()

        btnlogout!!.setOnClickListener {
            SharedPref.getInstance(this).clear()
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            setPhoneToavaible()
            this@MainActivity.finish()
        }
        btnProfil!!.setOnClickListener {
            val intent = Intent(this@MainActivity, ProfilActivity::class.java)
            startActivity(intent)
        }
        btnHistori!!.setOnClickListener {
            val intent = Intent(this@MainActivity, HistoriActivity::class.java)
            startActivity(intent)
        }

        initializeViews()
        setupRecyclerView()
        retrieveAndFillRecyclerView("GET_PAKET")
//        Toast.makeText(a,"Selamat Datang " + SharedPref.getInstance(this).user.nama,Toast.LENGTH_LONG).show()
        applicationContext.stopService(Intent(this, AlarmSoundService::class.java))
        PlayActivity.removeAlarm(this)
        NotificationUtil.hideTimerNotification(this)

    }
    private fun onTimer(){
        if(HistoriPref.getInstance(this).berlangganan){
            HistoriPref.getInstance(this).paket
            val intent = Intent(this@MainActivity, PlayActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
        else{
            applicationContext.stopService(Intent(this, AlarmSoundService::class.java))
            PlayActivity.removeAlarm(this)
        }
    }

    override fun onResume() {
        onTimer()
        super.onResume()
    }

    override fun onStart() {
       onTimer()
        super.onStart()
    }

    private fun setPhoneToavaible() {
        val ponId = Build.ID.toString()
        ApiService.loginApiCall().Ponseltoter(ponId).enqueue(object : Callback<PonselResponse>{
            override fun onResponse(
                call: Call<PonselResponse>,
                response: Response<PonselResponse>
            ) {
                if (response.body()!!.status){
                    val bebas = "Tersedia"
                    Toast.makeText(applicationContext, "Ponsel " + bebas, Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<PonselResponse>, t: Throwable) {
                //showInfoDialog(a, "ERROR",t.message +"Mohon periksa koneksi internet anda(pon stat)!!")
                Log.d("SetpHoneto", "ERROR: " + t.message)
                //Toast.makeText(applicationContext,t.message +"some error!!",Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun initializeViews() {
        pb.isIndeterminate = true
        pb.visibility= View.VISIBLE
        val ponId = Build.ID
        val json = JSONObject()
        json.put("ids", ponId)
        json.put("id_user", userId.toString())
        ApiService.loginApiCall().postStatusPonsel(
            DigunakanRequest(
                ponId, userId!!.toInt()
            )
        ).enqueue(object : Callback<PonselResponse>{
            override fun onResponse(
                call: Call<PonselResponse>,
                response: Response<PonselResponse>
            ) {
                if (response.body()!!.status){
                        val terpakai = "digunakan"
                        Toast.makeText(applicationContext, "ponsel " + terpakai, Toast.LENGTH_SHORT).show()
                } else {
                    val idle = "tersedia"
                    Toast.makeText(applicationContext, "ponsel " + idle, Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<PonselResponse>, t: Throwable) {
                showInfoDialog(a, "ERROR",t.message +"Mohon periksa koneksi internet anda(phn stts)!!")
            }

        })
    }

    /**
     * This method will setup oir RecyclerView
     */
    private fun setupRecyclerView() {
        layoutManager = LinearLayoutManager(this)
        pAdapter = PaketAdapter(this, allPagesPaket)
        rv.adapter = pAdapter
        rv.layoutManager = layoutManager
    }

    private fun retrieveAndFillRecyclerView(action: String) {
        val api: ApiList? = ApiService.loginApiCall()
        val retrievedData: Call<PaketResponse?>?
        retrievedData = api?.getPaket()
        retrievedData?.enqueue(object : Callback<PaketResponse?> {
            override fun onResponse(call: Call<PaketResponse?>?, response: Response<PaketResponse?>?) {
                pb.visibility= View.GONE

                if (response?.body() == null) {
                    showInfoDialog(a, "ERROR", "NULL BOSSS!!!")
                    return
                }
                currentPagePaket = response.body()?.result!!
 //               Toast.makeText(a,response.body()?.message,Toast.LENGTH_SHORT).show()
                if (currentPagePaket != null && currentPagePaket!!.size > 0) {
                    if (action.equals("GET", ignoreCase = true)) {
                        allPagesPaket.clear()
                    }
                    for (i in currentPagePaket!!.indices) {
                        allPagesPaket.add(currentPagePaket!![i])
                    }
                } else {
                    if (action.equals("GET", ignoreCase = true)) {
                        allPagesPaket.clear()
                    }
                }
                pAdapter!!.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<PaketResponse?>?, t: Throwable) {
                pb.visibility= View.VISIBLE
                Log.d("RETROFIT", "ERROR: " + t.message)
                showInfoDialog(a, "ERROR", "Mohon periksa koneksi internet anda(get pkt)!!")
            }
        })
    }

    private fun getUser() {
        ApiService.loginApiCall().getUser(userId)
            .enqueue(object : Callback<UserResponse> {
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    if (response.body()!!.status){
                       // txtid.text = response.body()!!.user.id_user.toString()
                        txt_name.text = response.body()!!.user.nama
                        txt_alamat.text = response.body()!!.user.alamat
                        Log.d("Respon SF-User::::", SharedPref.getInstance(applicationContext).isLoggedIn.toString())
                        Log.d("respon SF-Langganan", HistoriPref.getInstance(applicationContext).berlangganan.toString())
                        SharedPref.getInstance(applicationContext).saveUser(response.body()!!.user).toString()
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
                    Log.d("error::::", t.message)
                    showInfoDialog(a, "ERROR", "Mohon periksa koneksi internet anda(get usr)!!")
                }
            })
        txt_name.setText(SharedPref.getInstance(this).user.nama).toString()
        txt_alamat.setText(SharedPref.getInstance(this).user.alamat).toString()
    }

}