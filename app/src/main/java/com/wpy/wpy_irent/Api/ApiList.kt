package com.wpy.wpy_irent.Api

import com.wpy.wpy_irent.Auth.Login.LoginRequest
import com.wpy.wpy_irent.Auth.Login.LoginResponse
import com.wpy.wpy_irent.Auth.Register.RegisterRequest
import com.wpy.wpy_irent.Auth.Register.RegisterResponse
import com.wpy.wpy_irent.Model.Histori.HistoriNewRequest
import com.wpy.wpy_irent.Model.Histori.HistoriNewResponse
import com.wpy.wpy_irent.Model.Histori.HistoriRequest
import com.wpy.wpy_irent.Model.Histori.HistoriResponse
import com.wpy.wpy_irent.Model.Paket.PaketBatal
import com.wpy.wpy_irent.Model.Paket.PaketBatalRespon
import com.wpy.wpy_irent.Model.Paket.PaketRespon2
import com.wpy.wpy_irent.Model.Paket.PaketResponse
import com.wpy.wpy_irent.Model.Ponsel.DigunakanRequest
import com.wpy.wpy_irent.Model.Ponsel.PonselResponse
import com.wpy.wpy_irent.Model.User.UserResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiList {
    //TODO : Register User
    @POST("register.php")
    fun doRegister(
        @Body signupRequest: RegisterRequest
    ): Call<RegisterResponse> // body data

    //TODO : Login User
    @POST("login.php")
    fun doLogin(
        @Body signinRequest: LoginRequest
    ): Call<LoginResponse> // body data

    //TODO : Get User
    @GET("user.php")
    fun getUser(@Query("id_user") id_user: Int?
    ): Call<UserResponse>

    //TODO : Get paket
    @GET("paket.php")
    fun getPaket(): Call<PaketResponse?>?

    //TODO : Get histori
    @GET("histori.php")
    fun getHistori(
        @Query("id_user") id_user: Int?
    ): Call<HistoriResponse>

    //TODO : Push New Histori
    @POST("histori.php")
    fun newHistori(
        @Body newHistoriReq: HistoriNewRequest
    ): Call<PaketRespon2> // body data

    //TODO : Get detail ponsel
    @GET("ponsel.php")
    fun getPonsel(@Query("ids") ids: String
    ):Call<PonselResponse>

    @POST("ponsel.php")
    fun postStatusPonsel(
        @Body ponseldigunakan: DigunakanRequest
    ):Call<PonselResponse>

    @POST("ponseltoter.php")
    fun Ponseltoter(@Query("ids") ids: String
    ):Call<PonselResponse>

    @POST("batalpaket.php")
    fun paketBatal(
        @Body batalPaket: PaketBatal
    ):Call<PaketBatalRespon>


}