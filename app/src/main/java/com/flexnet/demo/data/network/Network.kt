package com.flexnet.demo.data.network

import com.flexnet.api.FlexNetInterceptor
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class Network @Inject constructor(private val flexNetInterceptor: FlexNetInterceptor) {

    private val BASE_URL = "https://jsonplaceholder.typicode.com/"

    companion object {

        fun init(flexNetInterceptor: FlexNetInterceptor): Network {
            return Network(flexNetInterceptor)
        }
    }

    private fun retrofit(): Retrofit {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient())
            .build()
    }

    private fun httpClient(): OkHttpClient {

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .writeTimeout(1, TimeUnit.MINUTES)
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .addInterceptor(flexNetInterceptor)
            .addInterceptor(logging).addNetworkInterceptor(Interceptor { chain ->
                val requestBuilder: Request.Builder = chain.request().newBuilder()
                requestBuilder.header("Content-Type", "application/json")
                runBlocking {
                    delay(10000)
                }
                chain.proceed(requestBuilder.build())
            }).build()
    }

    val apiService: RetrofitAPI = retrofit().create()
}



