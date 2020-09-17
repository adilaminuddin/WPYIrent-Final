package com.wpy.wpy_irent.Model.Histori

import com.google.gson.annotations.SerializedName

class HistoriResponse
{
    @SerializedName("histori")
    var result: ArrayList<Histori>? = ArrayList()

    @SerializedName("status")
    var status: String? = "UNKNOWN MESSAGE"
}