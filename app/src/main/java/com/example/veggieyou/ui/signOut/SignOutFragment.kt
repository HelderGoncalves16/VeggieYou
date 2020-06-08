package com.example.veggieyou.ui.signOut

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.veggieyou.LoginActivity
import com.example.veggieyou.R
import com.google.firebase.auth.FirebaseAuth

class SignOutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.sign_out_fragment, container, false)

        sairDaApp(root)

        return root
    }

    fun sairDaApp(root: View?) {
        val btn_sign_out = root!!.findViewById(R.id.btn_sign_out) as Button

        btn_sign_out.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(activity, LoginActivity::class.java)
            activity!!.startActivity(intent)
            activity!!.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
        }
    }


}
