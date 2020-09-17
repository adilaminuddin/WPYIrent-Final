package com.wpy.wpy_irent.Model.Histori

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Histori: Serializable {
    @SerializedName("id_histori")
    var id_histori: Int = 0
    @SerializedName("id_user")
    var id_user: String? = ""
    @SerializedName("id_paket")
    var id_paket: String? = ""
    @SerializedName("nama_pak")
    var nama_paket: String? = ""
    @SerializedName("harga_pak")
    var harga_paket: String? = ""
    @SerializedName("id_ponsel")
    var id_ponsel: String? = ""
    @SerializedName("ids")
    var ids: String? = ""
    @SerializedName("tipe_ponsel")
    var tipe_ponsel: String? = ""
    @SerializedName("waktu_mulai")
    var waktu_mulai: String? = ""
    @SerializedName("waktu_berakhir")
    var waktu_berakhir: String? = ""
}
//    "id_histori": "1",
//    "id_user": "2",
//    "nama": "ameng",
//    "id_paket": "1",
//    "nama_pak": "satu jam",
//    "harga_pak": "3000",
//    "id_ponsel": "1",
//    "ids": "QI.109.092",
//    "tipe_ponsel": "Redmi Note 7",
//    "waktu_mulai": "2020-07-01 01:52:44",
//    "waktu_berakhir": "2020-07-01 02:52:48"