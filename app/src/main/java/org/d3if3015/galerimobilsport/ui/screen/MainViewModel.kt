package org.d3if3015.galerimobilsport.ui.screen

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.d3if3015.galerimobilsport.model.Mobil
import org.d3if3015.galerimobilsport.network.ApiStatus
import org.d3if3015.galerimobilsport.network.MobilApi
import java.io.ByteArrayOutputStream

class MainViewModel: ViewModel() {

    var data = mutableStateOf(emptyList<Mobil>())
        private set

    var status = MutableStateFlow(ApiStatus.LOADING)
        private set

    var errorMessage = mutableStateOf<String?>(null)
        private set

    fun retriveData(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            status.value = ApiStatus.LOADING
            try {
                data.value = MobilApi.service.getMobil(userId)
                status.value = ApiStatus.SUCCES
            } catch (e: Exception){
                Log.d("MainViewModel", "Failure: ${e.message}")
                status.value = ApiStatus.FAILED
            }
        }
    }

    fun saveData(userId: String, nama: String, merek: String, bitmap: Bitmap){
        viewModelScope.launch (Dispatchers.IO){
            try {
                val result = MobilApi.service.postMobil(
                    userId,
                    nama.toRequestBody("text/plain".toMediaTypeOrNull()),
                    merek.toRequestBody("text/plain".toMediaTypeOrNull()) ,
                            bitmap.toMultipartBody(),
                )
                if (result.status == "success")
                    retriveData(userId)
                else
                    throw Exception(result.message)
            } catch (e: Exception){
                Log.d("MainViewModel", "Failure: ${e.message}")
                errorMessage.value = "Error: ${e.message}"
            }
        }
    }

    fun deletingData(userId: String,id: Long){
        viewModelScope.launch(Dispatchers.IO){
            try {
                val result = MobilApi.service.deleteMobil(userId = userId,id = id.toString())
                if (result.status == "success")
                    retriveData(userId = userId)
                else
                    throw Exception(result.message)
            }catch (e: Exception){
                Log.d("MainViewModel", "Failure: ${e.message}")
                errorMessage.value = "Error: ${e.message}"
            }
        }
    }

    private fun Bitmap.toMultipartBody(): MultipartBody.Part{
        val stream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, 80, stream)
        val byteArray = stream.toByteArray()
        val requestBody = byteArray.toRequestBody(
            "image/jpg".toMediaTypeOrNull(),0 ,byteArray.size)
        return MultipartBody.Part.createFormData(
            "image","image.jpg", requestBody)
    }

    fun clearMessage() { errorMessage.value = null }
}