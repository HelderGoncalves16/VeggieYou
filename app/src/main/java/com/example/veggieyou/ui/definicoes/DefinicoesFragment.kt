package com.example.veggieyou.ui.definicoes

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.veggieyou.Common.Common
import com.example.veggieyou.Model.UtilizadorModel
import com.example.veggieyou.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DefinicoesFragment : Fragment() {


    private lateinit var viewModel: DefinicoesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.definicoes_fragment, container, false)

        mostrarDadosUtilizador(root)

        return root
    }

    fun mostrarDadosUtilizador(root: View?) {

        val username = root!!.findViewById(R.id.username) as TextView
        val morada = root.findViewById(R.id.morada) as TextView
        val email = root.findViewById(R.id.email) as TextView
        val telefone = root.findViewById(R.id.telefone) as TextView
        val buttonAlterar = root.findViewById(R.id.button_alterar) as Button

        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val ref = FirebaseDatabase.getInstance().getReference(Common.UTILIZADOR_REF).child(uid)


        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(
                    context,
                    "Erro ao Carregar os Dados: " + p0.message,
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    val utilizador = p0.getValue(UtilizadorModel::class.java)
                    username.text = utilizador!!.nome
                    morada.text = utilizador.morada
                    telefone.text = utilizador.telefone
                    email.text = utilizador.email

                }
            }
        })

        buttonAlterar.setOnClickListener {

            val builder = AlertDialog.Builder(context)
            builder.setTitle("Confirmar")
            builder.setMessage("Deseja alterar os dados?")

            builder.setPositiveButton("SIM") { dialog, which ->
                ref.child("telefone").setValue(telefone.text.toString())
                ref.child("morada").setValue(morada.text.toString())
                Toast.makeText(context, "Dados Alterados!", Toast.LENGTH_SHORT).show()
            }
            builder.setNeutralButton("Cancelar") { dialog, which ->
                Toast.makeText(context, "Alteração de Dados Cancelada", Toast.LENGTH_SHORT).show()
            }

            builder.setNegativeButton("NÃO") { dialog, which ->
                Toast.makeText(context, "Alteração de Dados Rejeitada!", Toast.LENGTH_SHORT).show()
            }

            val dialog: AlertDialog = builder.create()

            dialog.show()
        }
    }


}
