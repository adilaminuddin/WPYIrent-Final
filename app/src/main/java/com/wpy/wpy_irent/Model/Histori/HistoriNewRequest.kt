package com.wpy.wpy_irent.Model.Histori

import com.google.gson.annotations.SerializedName

class HistoriNewRequest(@SerializedName("id_user") var id_user: String,
                        @SerializedName("id_ponsel") var id_ponsel: String,
                        @SerializedName("id_paket") var id_paket: String,
                        @SerializedName("waktu_mulai") var waktu_mulai: String,
                        @SerializedName("waktu_berakhir") var waktu_berakhir: String)
{
}