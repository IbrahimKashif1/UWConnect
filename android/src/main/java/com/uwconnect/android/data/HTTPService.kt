package com.uwconnect.android.data

import android.util.Log
import com.uwconnect.android.domain.repository.APIService
import com.uwconnect.android.util.TokenManager
import com.google.gson.*
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDateTime
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type

class LocalDateTimeAdapter : JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
    override fun serialize(
        src: LocalDateTime?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): LocalDateTime {
        return LocalDateTime.parse(json!!.asString)
    }
}

// create a singleton object class for creating instances of HTTP Requests
object RetrofitClient {
    private const val URL = "https://vigilant-guru-414404.nn.r.appspot.com"
//  private const val URL = "http://10.0.2.2:8080"
    private val gson: Gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
        .setLenient()
        .create()

    private val authInterceptor = Interceptor { chain ->
        val token = runBlocking {
            TokenManager.getJwt()
        } // Caution: Blocking call, consider a better approach
        chain.proceed(
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        )
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        this.level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()

    // lazy initialize the retrofit instance (no re-initialization)
    val retrofit: Retrofit by lazy {
        Retrofit.Builder().baseUrl(URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
}

object ApiClient {
    val apiService: APIService by lazy {
        RetrofitClient.retrofit.create(APIService::class.java)
    }
}