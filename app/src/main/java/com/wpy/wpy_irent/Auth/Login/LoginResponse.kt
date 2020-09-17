package com.wpy.wpy_irent.Auth.Login

import com.wpy.wpy_irent.Model.User.User

class LoginResponse(val status: Boolean, val message:String, val user: User) {
}