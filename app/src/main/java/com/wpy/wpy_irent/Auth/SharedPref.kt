package com.wpy.wpy_irent.Auth

import android.content.Context
import com.wpy.wpy_irent.Model.User.User

class SharedPref private constructor(private val mCtx: Context) {

    val isLoggedIn: Boolean
        get() {
            val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getInt("id_user", -1) != -1
        }

    val user: User
        get() {
            val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return User(
                sharedPreferences.getInt("id_user", -1),
                sharedPreferences.getString("username", null)!!,
                sharedPreferences.getString("nama", null)!!,
                sharedPreferences.getString("alamat", null)!!,
                sharedPreferences.getString("password", null)!!
            )
        }

    fun saveUser(user: User) {

        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        user.id_user.let { editor.putInt("id_user", it) }
        editor.putString("username", user.username)
        editor.putString("nama", user.nama)
        editor.putString("alamat", user.alamat)
        editor.putString("password", user.password)

        editor.apply()

    }

    fun clear() {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    companion object {
        private val SHARED_PREF_NAME = "my_shared_preff"
        private var mInstance: SharedPref? = null
        @Synchronized
        fun getInstance(mCtx: Context): SharedPref {
            if (mInstance == null) {
                mInstance = SharedPref(mCtx)
            }
            return mInstance as SharedPref
        }
    }

}