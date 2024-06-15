package org.d3if3015.galerimobilsport.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.d3if3015.galerimobilsport.model.Mobil
import org.d3if3015.galerimobilsport.model.OpStatus
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

private const val BASE_URL = "https://galerimobilsport.000webhostapp.com/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface MobilApiService {
    @GET("api/mobil.php")
    suspend fun getMobil(
        @Header("Authorization") userId: String
    ): List<Mobil>

    @Multipart
    @POST("api/mobil.php")
    suspend fun postMobil(
        @Header("Authorization") userId: String,
        @Part("nama") nama: RequestBody,
        @Part("merek") merek: RequestBody,
        @Part image: MultipartBody.Part,
    ): OpStatus

    @FormUrlEncoded
    @POST("api/deleteMobil.php")
    suspend fun deleteMobil(
        @Header("Authorization") userId: String,
        @Field("id") id: String
    ) : OpStatus
}
object MobilApi {
    val service: MobilApiService by lazy {
        retrofit.create(MobilApiService::class.java)
    }

    fun getMobilUrl(imageId: String): String{
        return "${BASE_URL}api/image.php?id=$imageId"
    }

}
enum class ApiStatus{ LOADING, SUCCES, FAILED }