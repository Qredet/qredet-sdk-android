package co.qredet.sdk

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import co.qredet.sdk.cardEmulation.QHostApduService
import co.qredet.sdk.nfc.Nfc
import com.squareup.otto.Subscribe
// Backend API integration imports - commented out for now
// import com.google.gson.Gson
// import kotlinx.coroutines.CoroutineScope
// import kotlinx.coroutines.Dispatchers
// import kotlinx.coroutines.async
// import kotlinx.coroutines.runBlocking
// import okhttp3.MediaType.Companion.toMediaType
// import okhttp3.OkHttpClient
// import okhttp3.Request
// import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

class Qredet(apiKey: String, onTransactionInitiated: (data: Map<String, *>) -> Unit, onTransactionCompleted: (data: String) -> Unit) {
    private lateinit var mNfc: Nfc

    // Qredet contactless sdk powers transactions Phone2Phone, Phone2POS
    // the flow is this -
    /*
    * Initiate Transaction - Merchant/Pos
    * Complete Transaction - User App
    * Initiate Transaction basically starts a transaction from a merchants pos/app
    * it starts a HCE session probably carrying a transaction identifier optionally encrypted
    *
    * Complete Transaction picks up that identifier, sends the identifier back to the integrating app
    * and completes the transaction (can be initiating a bank transfer or something)
    * */

    val onTransactionInitiated: (data: Map<String, *>) -> Unit
    val onTransactionCompleted: (data: String) -> Unit
    private val apiKey: String

    // Backend API integration - commented out for now
    // data class Transaction(
    //     val _id: String,
    //     val amount: Double,
    //     val success: Boolean,
    //     val reason: String?
    // )

    // data class Response(
    //     val error: Boolean,
    //     val data: Transaction,
    //     val msg: String?
    // )

    init {
        this.onTransactionInitiated = onTransactionInitiated
        this.onTransactionCompleted = onTransactionCompleted
        this.apiKey = apiKey
        EventBus.register(this)
    }

    fun startTransaction(context: Context, id: String, amount: Double, extra: Map<String, *>?) {
        val intent = Intent(context, QHostApduService::class.java)
        val data = mapOf("id" to id, "amount" to amount).plus(extra as Map<String, *>)
        if (extra.isNotEmpty()) {
            data + extra
        }
        val jsonData = JSONObject(data).toString()
        Log.d("HCE ACTIVITY", jsonData)
        intent.putExtra("ndefMessage", jsonData)
        context.startService(intent)
        this.onTransactionInitiated(data)
    }


    @Subscribe
    fun onComplete(event: Events.NfcReadResult) {
        val rawResult = event.getResult().trim()

        val normalizedResult = when {
            rawResult.isEmpty() -> rawResult
            rawResult.startsWith("{") && rawResult.endsWith("}") -> {
                try {
                    JSONObject(rawResult).toString()
                } catch (t: Throwable) {
                    rawResult
                }
            }
            else -> rawResult
        }

        this.onTransactionCompleted(normalizedResult)
    }


    fun completeTransaction(context: Context) {
        try {
            val intent = Intent(context, Nfc::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: Exception) {
            throw(e)
        }
    }


    // Backend API integration methods - commented out for now
    // private fun recordTransaction(amount: Double): Transaction? {
    //
    //     val url = "https://qredet-dev.fly.dev/api/v1/partner/transaction"
    //     val requestData = JSONObject(mapOf("amount" to amount)).toString().trimIndent()
    //     val apiKey = this.apiKey
    //     val mediaType = "application/json; charset=utf-8".toMediaType()
    //     val requestBody = requestData.toRequestBody(mediaType)
    //
    //     val client = OkHttpClient()
    //
    //     val request = Request.Builder()
    //         .url(url)
    //         .post(requestBody)
    //         .addHeader("Authorization", "Bearer $apiKey")
    //         .build()
    //
    //     try {
    //         val response = client.newCall(request).execute()
    //         if (response.isSuccessful) {
    //             val responseBody = response.body?.string()
    //             val gson = Gson()
    //             val resp = gson.fromJson(responseBody, Response::class.java)
    //             if (!resp.error) {
    //                 return resp.data
    //             }
    //         } else {
    //             println("Request failed with code: ${response.code}")
    //         }
    //     } catch (e: Exception) {
    //         println("ERROR: ${e.message}")
    //     }
    //
    //     return null
    // }

    // fun updateTransactionStatus(id: String, status: Boolean, reason: String?): Boolean {
    //     val url = "https://qredet-dev.fly.dev/api/v1/partner/transaction/$id"
    //     val requestData = JSONObject(mapOf("success" to status, "reason" to reason )).toString()
    //     val apiKey = this.apiKey
    //
    //     val mediaType = "application/json; charset=utf-8".toMediaType()
    //     val requestBody = requestData.toRequestBody(mediaType)
    //
    //     val client = OkHttpClient()
    //
    //     val request = Request.Builder()
    //         .url(url)
    //         .put(requestBody)
    //         .addHeader("Authorization", "Bearer $apiKey")
    //         .build()
    //
    //     try {
    //         val response = client.newCall(request).execute()
    //         if (response.isSuccessful) {
    //             val responseBody = response.body?.string()
    //             val gson = Gson()
    //             val resp = gson.fromJson(responseBody, Response::class.java)
    //             return !resp.error
    //         } else {
    //             println("Request failed with code: ${response.code}")
    //         }
    //     } catch (e: Exception) {
    //         println("ERROR: ${e.message}")
    //     }
    //
    //     return false
    // }

    fun JSONObject.toMap(): Map<String, *> = keys().asSequence().associateWith { it ->
        when (val value = this[it])
        {
            is JSONArray ->
            {
                val map = (0 until value.length()).associate { Pair(it.toString(), value[it]) }
                JSONObject(map).toMap().values.toList()
            }
            is JSONObject -> value.toMap()
            JSONObject.NULL -> null
            else            -> value
        }
    }
}
