package com.example.veggieyou.ui.ListaComidas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.veggieyou.Adapter.ListaComidasAdapter
import com.example.veggieyou.Common.Common
import com.example.veggieyou.R

class ListaComidasFragment : Fragment() {

    var recycler_comida_lista: RecyclerView? = null
    var layoutAnimationController: LayoutAnimationController? = null
    private lateinit var comidaListaViewModel: ListaComidasViewModel

    var adapter: ListaComidasAdapter? = null

    override fun onStop() {
        if (adapter != null)
            adapter!!.onStop()
        super.onStop()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        comidaListaViewModel = ViewModelProviders.of(this).get(ListaComidasViewModel::class.java)
        val root = inflater.inflate(R.layout.lista_comidas_fragment, container, false)

        initViews(root)

        comidaListaViewModel.getMutableComidaModelListaData().observe(viewLifecycleOwner, Observer {
            adapter = ListaComidasAdapter(context!!, it)
            recycler_comida_lista!!.adapter = adapter
            recycler_comida_lista!!.layoutAnimation = layoutAnimationController
        })
        return root
    }

    private fun initViews(root: View?) {
        recycler_comida_lista = root!!.findViewById(R.id.recycler_food_list) as RecyclerView
        recycler_comida_lista!!.setHasFixedSize(true)
        recycler_comida_lista!!.layoutManager = LinearLayoutManager(context)

        layoutAnimationController =
            AnimationUtils.loadLayoutAnimation(context, R.anim.layout_item_esquerda)

        (activity as AppCompatActivity).supportActionBar!!.title =
            Common.categoriaSelecionada!!.name
    }


}
