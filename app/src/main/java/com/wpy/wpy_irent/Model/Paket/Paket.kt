package com.wpy.wpy_irent.Model.Paket

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Paket(
    @SerializedName ("id_paket") var id_paket: Int,
    @SerializedName ("nama_pak") var nama_pak: String? = "",
    @SerializedName ("harga_pak")var harga_pak: String? = "",
    @SerializedName ("durasi")var durasi: Int,
    @SerializedName ("ket") var ket: String? = ""
):Serializable {
    override fun toString(): String {
        return nama_pak!!
    }
}