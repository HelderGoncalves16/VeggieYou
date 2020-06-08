package com.example.veggieyou


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        goto_register.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right)
        }

        button_login.setOnClickListener {
            fazerLogin()
        }

    }

    private fun fazerLogin() {
        val email = email_login.text.toString()
        val password = password_login.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Um dos campos encontra-se vazio", Toast.LENGTH_SHORT).show()
            return
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful)
                    Log.d("Main", "Utilizador logado com sucesso!")

                val intent = Intent(this, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao efetuar login! " + it.message, Toast.LENGTH_SHORT)
                    .show()
                return@addOnFailureListener
            }
    }
}