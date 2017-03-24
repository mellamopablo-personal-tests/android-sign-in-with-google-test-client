package io.github.mellamopablo.signinwithgoogletest.helpers

import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.result.Result
import org.json.JSONObject

abstract class ApiClient { companion object {

    val BASE_URL = "localhost"

    fun authenticate(idToken: String): ApiResponse {
        val apiResult = ApiResponse()

        val headers = HashMap<String, String>()
        headers.put("Content-Type", "application/json")

        Fuel.post(/*"$BASE_URL/auth"*/ "http://httpbin.org/post")
            .header(headers)
            .body(JSONObject().put("idToken", idToken).toString())
            .responseString { request, response, result ->
                when (result) {
                    is Result.Success -> {
                        val json = JSONObject(result.get())
                        apiResult.data = json
                    }

                    is Result.Failure -> {
                        Log.d("REQUEST", "Failure $result")
                        apiResult.onFailureCallback(result.error)
                    }
                }
            }

        return apiResult

    }

} }