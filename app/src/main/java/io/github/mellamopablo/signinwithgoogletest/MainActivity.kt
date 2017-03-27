package io.github.mellamopablo.signinwithgoogletest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log

import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.GoogleApiClient
import io.github.mellamopablo.signinwithgoogletest.helpers.ApiClient
import io.github.mellamopablo.signinwithgoogletest.components.LogView

class MainActivity : Activity() {

    internal var gso: GoogleSignInOptions? = null
    internal var gApiClient: GoogleApiClient? = null
    internal var account: GoogleSignInAccount? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gso = GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(SERVER_CLIENT_ID)
                .build()

        gApiClient = GoogleApiClient
                .Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso as GoogleSignInOptions)
                .build()

        gApiClient!!.connect() // TODO handle connection failures

        addClickListeners()
    }

    override fun onStop() {
        super.onStop()
        gApiClient!!.disconnect()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            handleSignInResult(result)
        }
    }

    private fun handleSignInResult(result: GoogleSignInResult) {
        val log = findViewById(R.id.log) as LogView
        log.clear()

        if (result.isSuccess) {
            log.addLine("Log in success")

            account = result.signInAccount
            log.addLine("Account name: ${account!!.displayName}")
            log.addLine("Email: ${account!!.email}")
            log.addLine("Scopes:")
            account!!.grantedScopes.forEach { scope -> log.addLine(scope.toString()) }

            ApiClient.authenticate(account!!.idToken as String).then { data ->
                log.addLine("Backend authentication success")
                Log.d("REQUEST", "Response: $data")
            } .catch { error ->
                log.addLine("Backend authentication failure")
                Log.e("REQUEST", error.localizedMessage ?: error.toString())
            }
        } else {
            log.addLine("Log in failure")
        }
    }

    private fun addClickListeners() {

        val signInButton = findViewById(R.id.sign_in_button) as SignInButton
        signInButton.setOnClickListener {
            val signInIntent = Auth.GoogleSignInApi.getSignInIntent(gApiClient)
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

    }

    companion object {
        private val RC_SIGN_IN = 322
        private val SERVER_CLIENT_ID =
                "922325936396-gb9hvho675353pr218mbjvtvbuvnq649.apps.googleusercontent.com"
    }
}