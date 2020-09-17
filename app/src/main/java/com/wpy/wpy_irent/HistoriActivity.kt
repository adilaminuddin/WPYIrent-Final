package com.wpy.wpy_irent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.wpy.wpy_irent.Adapter.HistoriAdapter
import com.wpy.wpy_irent.Api.ApiList
import com.wpy.wpy_irent.Api.ApiService
import com.wpy.wpy_irent.Auth.SharedPref
import com.wpy.wpy_irent.Auth.Utils
import com.wpy.wpy_irent.Model.Histori.Histori
import com.wpy.wpy_irent.Model.Histori.HistoriRequest
import com.wpy.wpy_irent.Model.Histori.HistoriResponse
import com.wpy.wpy_irent.Model.User.UserResponse
import kotlinx.android.synthetic.main.activity_histori.*
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoriActivity : AppCompatActivity() {

    private val userId: Int? = SharedPref.getInstance(this).user.id_user
    private var hAdapter: HistoriAdapter? = null

    var allPagesHistori: ArrayList<Histori> = ArrayList()
    private var cHistori: ArrayList<Histori> = ArrayList()
    private var layoutManagerH: LinearLayoutManager? = null
    private val b : HistoriActivity = this


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_histori)
        getUser()
        initializeViews()
        setupRecyclerView()
        retrieveAndFillRecyclerView("GET_HISTORI")
    }

    private fun initializeViews() {
        pbhi.isIndeterminate = true
        pbhi.visibility= View.VISIBLE
    }

    /**
     * This method will setup oir RecyclerView
     */
    private fun setupRecyclerView() {
        layoutManagerH = LinearLayoutManager(this)
        hAdapter = HistoriAdapter(this, allPagesHistori)
        rvhi.adapter = hAdapter
        rvhi.layoutManager = layoutManagerH
    }
    private fun retrieveAndFillRecyclerView(action: String){
        ApiService.loginApiCall().getHistori(userId)
        .enqueue(object : Callback<HistoriResponse?> {
                override fun onResponse(call: Call<HistoriResponse?>?, response: Response<HistoriResponse?>?) {
                    pbhi.visibility= View.GONE

                    if (response?.body() == null) {
                        Utils.showInfoDialog(b, "ERROR", "NULL BOSSS!!!")
                        return
                    }
                    cHistori = response.body()?.result!!
                    if (cHistori.size > 0) {
                        if (action.equals("GET", ignoreCase = true)) {
                            allPagesHistori.clear()
                        }
                        for (i in cHistori.indices) {
                            allPagesHistori.add(cHistori[i])
                        }
                    } else {
                        if (action.equals("GET", ignoreCase = true)) {
                            allPagesHistori.clear()
                        }
                    }
                    hAdapter!!.notifyDataSetChanged()
                }

                override fun onFailure(call: Call<HistoriResponse?>?, t: Throwable) {
                    pbhi.visibility= View.VISIBLE
                    Log.d("RETROFIT", "ERROR: " + t.message)
                    Utils.showInfoDialog(b, "Paket Anda Kosong?", "Harap menekan tombol kembali dan periksa koneksi internet anda, Terima Kasih...")
                    pbhi.visibility= View.GONE
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
                    if (response.body()!!.status) {
                        // txtid.text = response.body()!!.user.id_user.toString()
                        tvhi_username.text = response.body()!!.user.nama
                        tvhi_alamat.text = response.body()!!.user.alamat
                        tvhiIdUser.text = userId.toString()
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
                    Utils.showInfoDialog(b, "Paket Anda Kosong?", "Harap periksa koneksi internet anda, Terima Kasih...")
                }
            })
    }
}