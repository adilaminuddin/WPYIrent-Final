package com.wpy.wpy_irent.Api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.*
import javax.security.cert.CertificateException

object ApiWorker {
  private var mClient: OkHttpClient? = null
  private var mGsonConverter: GsonConverterFactory? = null


  /**
   * Don't forget to remove Interceptors (or change Logging Level to NONE)
   * in production! Otherwise people will be able to see your request and response on Log Cat.
   */
  val client: OkHttpClient
    @Throws(NoSuchAlgorithmException::class, KeyManagementException::class)
    get() {
      if (mClient == null) {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val httpBuilder = OkHttpClient.Builder()
        httpBuilder
          .connectTimeout(120, TimeUnit.SECONDS)
          .readTimeout(120, TimeUnit.SECONDS)
          .addInterceptor(interceptor)  /// show all JSON in logCat
          .addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
              .addHeader("Content-Type", "application/json")
              .method(original.method(), original.body())
            val request = requestBuilder.build()
            chain.proceed(request)
          }
        mClient = httpBuilder.build()

      }
      return mClient!!
    }


  val gsonConverter: GsonConverterFactory
    get() {
      if (mGsonConverter == null) {
        mGsonConverter = GsonConverterFactory
          .create(
            GsonBuilder()
              .setLenient()
              .disableHtmlEscaping()
              .create()
          )
      }
      return mGsonConverter!!
    }
}