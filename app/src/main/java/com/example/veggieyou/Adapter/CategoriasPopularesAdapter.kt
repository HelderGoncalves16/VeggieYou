package com.example.veggieyou.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.veggieyou.Model.CategoriasPopularesModel
import com.example.veggieyou.R
import de.hdodenhof.circleimageview.CircleImageView


class CategoriasPopularesAdapter(
    internal var context: Context,
    internal var categoriasPopularesModel: List<CategoriasPopularesModel>
) : RecyclerView.Adapter<CategoriasPopularesAdapter.MyViewHolder>() {
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var nome_categoria: TextView? = null
        var categoria_imagem: CircleImageView? = null

        init {
            nome_categoria = itemView.findViewById(R.id.texto_categoria) as TextView
            categoria_imagem = itemView.findViewById(R.id.imagem_categoria) as CircleImageView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.layout_categorias_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return categoriasPopularesModel.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(context).load(categoriasPopularesModel.get(position).image)
            .into(holder.categoria_imagem!!)
        holder.nome_categoria!!.text = categoriasPopularesModel.get(position).name
    }
}