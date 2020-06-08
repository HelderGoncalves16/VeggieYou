package com.example.veggieyou

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.veggieyou.Common.Common
import com.example.veggieyou.Model.UtilizadorModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_registar.setOnClickListener {
            fazerRegisto()
        }


        already_have_account.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
        }
    }

    private fun fazerRegisto() {
        val email = email_register.text.toString()
        val password = password_register.text.toString()

        if (password.length < 8) {
            Toast.makeText(
                this,
                "Password não pode ser inferior a 8 caracteres",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Um dos campos encontra-se vazio", Toast.LENGTH_SHORT).show()
            return
        }

        //Autenticação - Firebase
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener
                guardarUtilizadorParaDatabase()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao efetuar registo! " + it.message, Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun guardarUtilizadorParaDatabase() {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference(Common.UTILIZADOR_REF).child(uid)

        val utilizador = UtilizadorModel()
        utilizador.uid = uid
        utilizador.morada = morada_register.text.toString()
        utilizador.telefone = telefone_register.text.toString()
        utilizador.nome = username_register.text.toString()
        utilizador.email = email_register.text.toString()

        ref.setValue(utilizador).addOnSuccessListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao efetuar registo! " + it.message, Toast.LENGTH_SHORT)
                    .show()
                return@addOnFailureListener
            }
    }
}

