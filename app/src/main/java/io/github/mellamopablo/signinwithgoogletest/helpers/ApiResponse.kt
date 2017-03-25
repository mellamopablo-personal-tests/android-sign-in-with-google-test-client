package io.github.mellamopablo.signinwithgoogletest.helpers

import android.util.Log
import com.github.kittinunf.fuel.core.FuelError
import org.json.JSONObject

class ApiResponse {

    private var onSuccessCallback: ((JSONObject) -> Unit) = {}
    public /* TODO something like package private ?*/
        var onFailureCallback: ((FuelError) -> Unit) = { error ->
            Log.e("ApiResponse", "Unhandled error!")
            Log.e("ApiResponse", error.toString())
        }

    var data: JSONObject? = null
        set(value) {
            field = value
            onSuccessCallback(this.data as JSONObject)
        }

    public fun then(callback: (JSONObject) -> Unit): ApiResponse {
        this.onSuccessCallback = callback
        return this
    }

    public fun catch(callback: (FuelError) -> Unit): ApiResponse {
        this.onFailureCallback = callback
        return this
    }

}