package io.github.mellamopablo.signinwithgoogletest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.TextView

import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.GoogleApiClient

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
        clearLog()

        if (result.isSuccess) {
            addLineToLog("Log in success")

            account = result.signInAccount
            addLineToLog("Account name: ${account!!.displayName}")
            addLineToLog("Email: ${account!!.email}")
            addLineToLog("Scopes:")
            account!!.grantedScopes.forEach { scope -> addLineToLog(scope.toString()) }
        } else {
            addLineToLog("Log in failure")
        }
    }

    private fun addClickListeners() {

        val signInButton = findViewById(R.id.sign_in_button) as SignInButton
        signInButton.setOnClickListener {
            val signInIntent = Auth.GoogleSignInApi.getSignInIntent(gApiClient)
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

    }

    private fun addLineToLog(line: String) {
        val log = findViewById(R.id.log_text) as TextView
        log.text = "${log.text}\n$line"
    }

    private fun clearLog() {
        val log = findViewById(R.id.log_text) as TextView
        log.text = ""
    }

    companion object {
        private val RC_SIGN_IN = 322
    }
}