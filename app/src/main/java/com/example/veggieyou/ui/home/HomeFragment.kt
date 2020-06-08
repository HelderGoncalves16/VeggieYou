package com.example.veggieyou.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.asksira.loopingviewpager.LoopingViewPager
import com.example.veggieyou.Adapter.CategoriasPopularesAdapter
import com.example.veggieyou.Adapter.PromocoesAdapter
import com.example.veggieyou.R

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    var recyclerView: RecyclerView? = null
    var viewPager: LoopingViewPager? = null

    var layoutAnimationController: LayoutAnimationController? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        initView(root)
        //BIND DATA
        homeViewModel.listaCategoriasPopulares.observe(viewLifecycleOwner, Observer {
            val listaData = it
            val adapter = CategoriasPopularesAdapter(context!!, listaData)
            recyclerView!!.adapter = adapter
            recyclerView!!.layoutAnimation = layoutAnimationController

        })
        homeViewModel.listaPromocoes.observe(viewLifecycleOwner, Observer {
            val adapter = PromocoesAdapter(context!!, it, false)
            viewPager!!.adapter = adapter
        })
        return root
    }

    private fun initView(root: View) {

        layoutAnimationController =
            AnimationUtils.loadLayoutAnimation(context, R.anim.layout_item_esquerda)

        viewPager = root.findViewById(R.id.viewpager) as LoopingViewPager
        recyclerView = root.findViewById(R.id.recycler_popular) as RecyclerView
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
    }

    override fun onResume() {
        super.onResume()
        viewPager!!.resumeAutoScroll()
    }

    override fun onPause() {
        viewPager!!.pauseAutoScroll()
        super.onPause()
    }


}
