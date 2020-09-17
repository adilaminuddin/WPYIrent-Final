package com.wpy.wpy_irent.Auth
import android.content.Context
import com.wpy.wpy_irent.Model.Histori.Histori
import com.wpy.wpy_irent.Model.Paket.Paket

class HistoriPref private constructor(private val hCtx: Context) {

    val berlangganan: Boolean
        get() {
            val sharedPreferences = hCtx.getSharedPreferences(SHARED_PREF_PAKET, Context.MODE_PRIVATE)
            return sharedPreferences.getInt("id_paket", -1) != -1
        }
    val paket:Paket
        get() {
            val sharedPreferences = hCtx.getSharedPreferences(SHARED_PREF_PAKET, Context.MODE_PRIVATE)
            return Paket(
                sharedPreferences.getInt("id_paket",-1),
                sharedPreferences.getString("nama_paket",null),
                sharedPreferences.getString("harga_paket",null),
                sharedPreferences.getInt("durasi", 0),
                sharedPreferences.getString("ket",null)
            )

        }
    fun saveHistori(paket:Paket){
        val sharedPreferences = hCtx.getSharedPreferences(SHARED_PREF_PAKET, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        paket.id_paket.let {editor.putInt("id_paket", it)}
        editor.putString("nama_pak", paket.nama_pak)
        editor.putString("harga_pak", paket.harga_pak)
        editor.putInt("durasi", paket.durasi)
        editor.putString("ket", paket.ket)
        editor.apply()
    }
    fun clear() {
        val sharedPreferences = hCtx.getSharedPreferences(SHARED_PREF_PAKET, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    companion object {
        private const val SHARED_PREF_PAKET = "my_paket_preff"
        private var mInstance: HistoriPref? = null
        @Synchronized
        fun getInstance(hCtx: Context): HistoriPref {
            if (mInstance == null) {
                mInstance = HistoriPref(hCtx)
            }
            return mInstance as HistoriPref
        }
    }
}