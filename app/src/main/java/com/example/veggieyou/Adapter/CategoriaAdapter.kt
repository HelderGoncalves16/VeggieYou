package com.example.veggieyou.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.veggieyou.Callback.IRecyclerItemClickListener
import com.example.veggieyou.Common.Common
import com.example.veggieyou.EventBus.CategoriaClick
import com.example.veggieyou.Model.CategoriaModel
import com.example.veggieyou.R
import org.greenrobot.eventbus.EventBus


class CategoriaAdapter(
    internal var context: Context,
    internal var categoriasList: List<CategoriaModel>
) : RecyclerView.Adapter<CategoriaAdapter.MyViewHolder>() {
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        override fun onClick(view: View?) {
            listener!!.onItemClick(view!!, adapterPosition)
        }

        var categoria_nome: TextView? = null
        var categoria_imagem: ImageView? = null

        internal var listener: IRecyclerItemClickListener? = null

        fun setListener(listener: IRecyclerItemClickListener) {
            this.listener = listener
        }

        init {
            categoria_nome = itemView.findViewById(R.id.categoria_nome) as TextView
            categoria_imagem = itemView.findViewById(R.id.categoria_imagem) as ImageView
            itemView.setOnClickListener(this)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (categoriasList.size == 1) {
            Common.COLUNA_DEFAULT_CONTADOR
        } else {
            if (categoriasList.size % 2 == 0) {
                Common.COLUNA_DEFAULT_CONTADOR
            } else {
                if (position > 1 && position == categoriasList.size - 1) Common.LARGURA_COLUNA else Common.COLUNA_DEFAULT_CONTADOR
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoriaAdapter.MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.layout__categorias_item_menu, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return categoriasList.size
    }

    override fun onBindViewHolder(holder: CategoriaAdapter.MyViewHolder, position: Int) {
        Glide.with(context).load(categoriasList.get(position).image)
            .into(holder.categoria_imagem!!)
        holder.categoria_nome!!.text = categoriasList.get(position).name

        //Evento
        holder.setListener(object : IRecyclerItemClickListener {
            override fun onItemClick(view: View, pos: Int) {
                Common.categoriaSelecionada = categoriasList.get(pos)
                EventBus.getDefault()!!.postSticky(CategoriaClick(true, categoriasList.get(pos)))
            }
        })
    }
}