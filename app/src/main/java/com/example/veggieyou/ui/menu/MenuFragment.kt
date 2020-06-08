package com.example.veggieyou.ui.menu

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.veggieyou.Adapter.CategoriaAdapter
import com.example.veggieyou.Common.Common
import com.example.veggieyou.Common.EspacosDosItems
import com.example.veggieyou.R

class MenuFragment : Fragment() {

    private lateinit var menuViewModel: MenuViewModel
    private lateinit var dialog: AlertDialog
    private lateinit var layoutAnimatorController: LayoutAnimationController
    private var adapter: CategoriaAdapter? = null

    private var recycler_menu: RecyclerView? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        menuViewModel = ViewModelProviders.of(this).get(MenuViewModel::class.java)

        val root = inflater.inflate(R.layout.menu_fragment, container, false)
        initViews(root)

        menuViewModel.getMensagemErro().observe(viewLifecycleOwner, Observer {

            Toast.makeText(context, "Erro ao carregar as categorias! " + it, Toast.LENGTH_SHORT)
                .show()
        })

        menuViewModel.recebeListaCategorias().observe(viewLifecycleOwner, Observer {
            adapter = CategoriaAdapter(context!!, it)
            recycler_menu!!.adapter = adapter
        })

        return root
    }

    private fun initViews(root: View) {

        layoutAnimatorController =
            AnimationUtils.loadLayoutAnimation(context, R.anim.layout_item_esquerda)

        recycler_menu = root.findViewById(R.id.recycler_menu) as RecyclerView

        recycler_menu!!.setHasFixedSize(true)
        val layoutManager = GridLayoutManager(context, 2)
        layoutManager.orientation = RecyclerView.VERTICAL
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (adapter != null) {
                    when (adapter!!.getItemViewType(position)) {
                        Common.COLUNA_DEFAULT_CONTADOR -> 1
                        Common.LARGURA_COLUNA -> 2
                        else -> 1
                    }
                } else {
                    -1
                }
            }
        }
        recycler_menu!!.layoutManager = layoutManager
        recycler_menu!!.addItemDecoration(EspacosDosItems(8))
    }
}


