package com.example.veggieyou

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        relativeLayout_splash.alpha = 0f
        relativeLayout_splash.animate().setDuration(1500).alpha(1f).withEndAction {
            verificarSeUtilizadorEstaLogado()
        }

    }

    private fun abrirHomeActivity() {
        var intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }

    private fun abrirMainActivity() {
        var intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }

    private fun verificarSeUtilizadorEstaLogado() {
        val uid = FirebaseAuth.getInstance().uid
        if (uid == null) {
            abrirMainActivity()
        } else {
            abrirHomeActivity()
        }
    }
}
