package com.rudraksha.internals

import okhttp3.*
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

fun sendCallLogToServer(call: CallLogEntry) {
    val gson = Gson()
    val client = OkHttpClient()

    val json = gson.toJson(call)
    val body = json.toRequestBody("application/json".toMediaTypeOrNull())

    val request = Request.Builder()
        .url("http://192.168.1.50:8000/log_call.php") // <-- replace with your actual URL
        .post(body)
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
        }

        override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful) {
                println("Call log sent successfully")
            } else {
                println("Server error: ${response.code}")
            }
        }
    })
}
