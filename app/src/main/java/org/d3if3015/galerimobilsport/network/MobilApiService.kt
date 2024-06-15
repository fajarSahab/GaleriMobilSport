package org.d3if3015.galerimobilsport.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.d3if3015.galerimobilsport.model.Mobil
import org.d3if3015.galerimobilsport.model.OpStatus
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

private const val BASE_URL = "https://gh.d3ifcool.org/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface MobilApiService {
    @GET("api/car.php")
    suspend fun getMobil(
        @Header("Authorization") userId: String
    ): List<Mobil>

    @Multipart
    @POST("api/car.php")
    suspend fun postMobil(
        @Header("Authorization") userId: String,
        @Part("nama_mobil") nama: RequestBody,
        @Part ("id_gambar")image: MultipartBody.Part,
        @Part("merek_mobil") merek: RequestBody,
    ): OpStatus
    @DELETE("api/car.php")
    suspend fun deleteMobil(
        @Header("Authorization") userId: String,
        @Query("id") id: String
    ) : OpStatus
}
object MobilApi {
    val service: MobilApiService by lazy {
        retrofit.create(MobilApiService::class.java)
    }

    fun getMobilUrl(imageId: String): String{
        return "${BASE_URL}image.php?id=$imageId"
    }

}
enum class ApiStatus{ LOADING, SUCCES, FAILED }