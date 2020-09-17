package com.wpy.wpy_irent.Model.Ponsel

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Ponsel
{
    @SerializedName("id_ponsel")
    var id_ponsel: Int? = null
    @SerializedName("ids")
    var ids: String? = null
    @SerializedName("imei")
    var imei: String? = ""
    @SerializedName("merk_ponsel")
    var merk_ponsel: String? = ""
    @SerializedName("tipe_ponsel")
    var tipe_ponsel: String? = ""
    @SerializedName("status")
    var status: Int = 0

//    override fun toString(): String {
//        return "ponsel(id_ponsel=$id_ponsel, ids=$ids, imei=$imei, merk_ponsel=$merk_ponsel, tipe_ponsel=$tipe_ponsel)"
//    }
}