package com.example.veggieyou.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.asksira.loopingviewpager.LoopingPagerAdapter
import com.bumptech.glide.Glide
import com.example.veggieyou.Model.CategoriasPromocoesModel
import com.example.veggieyou.R

class PromocoesAdapter(
    context: Context,
    itemList: List<CategoriasPromocoesModel>,
    isInfinite: Boolean
) : LoopingPagerAdapter<CategoriasPromocoesModel>(context, itemList, isInfinite) {
    override fun bindView(convertView: View, listPosition: Int, viewType: Int) {
        val imageView = convertView.findViewById<ImageView>(R.id.imagem_promocao)
        val textView = convertView.findViewById<TextView>(R.id.txt_promocao)

        //Atribuir dados
        Glide.with(context).load(itemList!![listPosition].image).into(imageView)
        textView.text = itemList!![listPosition].name
    }

    override fun inflateView(viewType: Int, container: ViewGroup, listPosition: Int): View {
        return LayoutInflater.from(context).inflate(R.layout.layout_promocoes, container, false)
    }

}