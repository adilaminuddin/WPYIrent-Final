package com.wpy.wpy_irent.Model.Paket

import com.google.gson.annotations.SerializedName

class PaketResponse {
    @SerializedName("paket")
    var result: ArrayList<Paket>? = ArrayList()

    @SerializedName("status")
    var message: String? = "UNKNOWN MESSAGE"

}