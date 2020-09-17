package com.wpy.wpy_irent.Auth.Login

import com.google.gson.annotations.SerializedName

class LoginRequest(
    @SerializedName("username") var username: String,
    @SerializedName("password") var password: String
)