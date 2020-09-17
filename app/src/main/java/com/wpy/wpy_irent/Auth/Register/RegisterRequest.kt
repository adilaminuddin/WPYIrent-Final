package com.wpy.wpy_irent.Auth.Register

import com.google.gson.annotations.SerializedName

class RegisterRequest(@SerializedName("nama") var nama: String,
                      @SerializedName("username") var username: String,
                      @SerializedName("password") var password: String,
                      @SerializedName("alamat") var alamat: String) {
}