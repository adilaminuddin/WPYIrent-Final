package com.wpy.wpy_irent.Model.User

data class User(
    var id_user: Int,
    var nama: String?,
    var username: String?,
    var alamat: String?,
    var password:String?
)
{
    override fun toString(): String {
        return "user(id_user=$id_user, nama=$nama, username=$username, alamat=$alamat, password=$password)"
    }

}
