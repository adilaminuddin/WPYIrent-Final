package com.wpy.wpy_irent

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.wpy.wpy_irent.AlarmNotification.PrefUtil
import com.wpy.wpy_irent.Api.ApiService
import com.wpy.wpy_irent.Auth.HistoriPref
import com.wpy.wpy_irent.Auth.Utils
import com.wpy.wpy_irent.Model.Histori.HistoriNewRequest
import com.wpy.wpy_irent.Model.Paket.Paket
import com.wpy.wpy_irent.Model.Paket.PaketRespon2
import com.wpy.wpy_irent.Model.Ponsel.Ponsel
import com.wpy.wpy_irent.Model.Ponsel.PonselResponse
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class DetailActivity : AppCompatActivity() {
    val a: DetailActivity = this
    private var receivedPaket: Paket? = null
    private var receivedPonsel: Ponsel? = null
    private val userId: Int? = com.wpy.wpy_irent.Auth.SharedPref.getInstance(this).user.id_user


    var tvppnpaket: TextView? = null
    var tvppdpaket: TextView? = null
    var tvpphpaket: TextView? = null

    var ponMerk: TextView? =null
    var ponJenis: TextView? =null
    var ponId: TextView? =null

    private lateinit var didUser : TextView


    var telephonyManager: TelephonyManager? = null
    private val REQUEST_CODE = 101

    private var gidPOn: String? = null
    private var gidPaket :String? = null
    private var gidNOw: String?  = null
    private var gidNadd: String? = null

    private lateinit var didPaket : TextView
    private lateinit var didPonsel : TextView
    private var today = Calendar.getInstance()
    private var todayadd = Calendar.getInstance()

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        tvppnpaket = findViewById(R.id.tvPPnpaket)
        tvppdpaket = findViewById(R.id.tvPPdpaket)
        tvpphpaket = findViewById(R.id.tvPPhpaket)

        ponId = findViewById(R.id.tvIdPonsel)
        ponMerk = findViewById(R.id.tvMerk)
        ponJenis = findViewById(R.id.tvPonOs)

        didUser = findViewById(R.id.DidUser)
        didPaket = findViewById(R.id.DidPaket)
        didPonsel = findViewById(R.id.DidPonsel)
        didUser.setText(userId.toString())

        val btnplayp = findViewById<Button>(R.id.btnPlayP)
        receiveAndShowData()
        deviceInfo()
        onRequestPermissionsResult(REQUEST_CODE, arrayOf(Manifest.permission.READ_PHONE_STATE), intArrayOf(REQUEST_CODE))

        val getInterval = receivedPaket?.durasi!!.toInt()
        todayadd.add(Calendar.MINUTE,getInterval)

        val oke = SimpleDateFormat("Y-M-d H:m:s").format( today.time )
        val sip = SimpleDateFormat("Y-M-d H:m:s").format( todayadd.time )
        gidNOw = oke.toString()
        gidNadd = sip.toString()
        getPonsel()

        btnplayp.setOnClickListener {
            val intent = Intent(this@DetailActivity, PlayActivity::class.java)
            PrefUtil.setTimerLength(getInterval,applicationContext)
            val json = JSONObject()
            json.put("id_user",userId.toString())
            json.put("id_ponsel",gidPOn.toString())
            json.put("id_paket",gidPaket.toString())
            json.put("waktu_mulai",gidNOw.toString())
            json.put("waktu_berakhir",gidNadd.toString())
            ApiService.loginApiCall().newHistori(
                HistoriNewRequest(
                    userId.toString(),
                    gidPOn.toString(),
                    gidPaket.toString(),
                    gidNOw.toString(),
                    gidNadd.toString()
                )
            ).enqueue(object : Callback<PaketRespon2>{
                override fun onResponse(
                    call: Call<PaketRespon2>,
                    response: Response<PaketRespon2>
                ) {
                    if (response.body()!!.status){
                        HistoriPref.getInstance(applicationContext).saveHistori(response.body()!!.paket).toString()
                        Log.d("respon SF-Langganan2", HistoriPref.getInstance(applicationContext).berlangganan.toString())
                        //Toast.makeText(this@DetailActivity,"data direkam ke database",Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<PaketRespon2>, t: Throwable) {
                   // Utils.showInfoDialog(a, "ERROR", "Mohon Periksa kembali koneksi internet anda...")
                }
            })
            intent.putExtra("id_ponsel",gidPOn)
            startActivity(intent)
        }
    }

    private fun getPonsel() {
        val ids =(Build.ID)
        ApiService.loginApiCall().getPonsel(ids).enqueue(object : Callback<PonselResponse>{
            override fun onResponse(
                call: Call<PonselResponse>,
                response: Response<PonselResponse>
            ) {
                if (response.body()!!.status){
                    didPonsel.text = response.body()!!.ponsel.id_ponsel.toString()
                    gidPOn = response.body()!!.ponsel.id_ponsel.toString()
                } else {
                    Toast.makeText(applicationContext, response.body()!!.status.toString()+"mungkin ponsel belum di input datanya", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<PonselResponse>, t: Throwable) {
                Utils.showInfoDialog(a, "ERROR", "Mohon Periksa kembali koneksi internet anda...")
            }
        }
        )
    }

    private fun receiveAndShowData() {
        receivedPaket = Utils.receivePaket(intent)
        if (receivedPaket != null) {
            didPaket.text = receivedPaket?.id_paket.toString()
            gidPaket = receivedPaket?.id_paket.toString()
            tvppnpaket!!.text = receivedPaket?.nama_pak
            tvpphpaket!!.text = "Rp."+receivedPaket?.harga_pak
            tvppdpaket!!.text = receivedPaket?.durasi.toString()
            //intent.putExtra("durasi",receivedPaket?.durasi )
        }
    }

    private fun deviceInfo() {
        telephonyManager = this.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        @Suppress("DEPRECATED_IDENTITY_EQUALS")
        if (ActivityCompat.checkSelfPermission(
                this@DetailActivity, Manifest.permission.READ_PHONE_STATE
            ) !== PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@DetailActivity, arrayOf(Manifest.permission.READ_PHONE_STATE), REQUEST_CODE
            )
            return
        }
        val reqString = (Build.MANUFACTURER)
        val reqString2 = (Build.MODEL + ", OS : " + Build.VERSION.RELEASE)
        val ids =(Build.ID)
        ponId?.setText(ids)
        ponMerk?.setText(reqString)
        ponJenis?.setText(reqString2)
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
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
                    return
                }
            }
        }
    }
}