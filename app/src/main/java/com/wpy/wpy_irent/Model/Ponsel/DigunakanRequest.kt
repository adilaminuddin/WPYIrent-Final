package com.wpy.wpy_irent.Model.Ponsel
import com.google.gson.annotations.SerializedName

class DigunakanRequest(@SerializedName("ids") var ids: String,
                       @SerializedName("id_user") var id_user: Int) {
}