package com.example.veggieyou.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.veggieyou.Callback.IRecyclerItemClickListener
import com.example.veggieyou.Common.Common
import com.example.veggieyou.Database.CarrinhoDataSource
import com.example.veggieyou.Database.CarrinhoDatabase
import com.example.veggieyou.Database.CarrinhoItem
import com.example.veggieyou.Database.LocalCarrinhoDataSource
import com.example.veggieyou.EventBus.ComidaClick
import com.example.veggieyou.EventBus.CountCartEvent
import com.example.veggieyou.Model.FoodModel
import com.example.veggieyou.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus

class ListaComidasAdapter(
    internal var context: Context,
    internal var comidaList: List<FoodModel>
) : RecyclerView.Adapter<ListaComidasAdapter.MyViewHolder>() {

    private val compositeDisposable: CompositeDisposable
    private val carrinhoDataSource: CarrinhoDataSource

    init {
        compositeDisposable = CompositeDisposable()
        carrinhoDataSource =
            LocalCarrinhoDataSource(CarrinhoDatabase.getInstance(context).carrinhoDao())
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        override fun onClick(view: View?) {
            listener!!.onItemClick(view!!, adapterPosition)
        }

        var txt_comida_nome: TextView? = null
        var txt_comida_preco: TextView? = null
        var imagem_fav: ImageView? = null
        var imagem_comida: ImageView? = null
        var img_carrinho: ImageView? = null

        internal var listener: IRecyclerItemClickListener? = null

        fun setListener(listener: IRecyclerItemClickListener) {
            this.listener = listener
        }

        init {
            txt_comida_nome = itemView.findViewById(R.id.txt_comida_nome) as TextView
            txt_comida_preco = itemView.findViewById(R.id.txt_comida_preco) as TextView
            imagem_comida = itemView.findViewById(R.id.imagem_comida) as ImageView
            imagem_fav = itemView.findViewById(R.id.imagem_fav) as ImageView
            img_carrinho = itemView.findViewById(R.id.imagem_carrinho) as ImageView

            itemView.setOnClickListener(this)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListaComidasAdapter.MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.layout_comida_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return comidaList.size
    }

    override fun onBindViewHolder(holder: ListaComidasAdapter.MyViewHolder, position: Int) {
        Glide.with(context).load(comidaList.get(position).image)
            .into(holder.imagem_comida!!)
        holder.txt_comida_nome!!.text = comidaList.get(position).name
        holder.txt_comida_preco!!.text = comidaList.get(position).price.toString()

        //Evento
        holder.setListener(object : IRecyclerItemClickListener {
            override fun onItemClick(view: View, pos: Int) {
                Common.comidaSelecionada = comidaList.get(pos)
                EventBus.getDefault().postSticky(ComidaClick(true, comidaList.get(pos)))
            }
        })

        holder.img_carrinho!!.setOnClickListener {
            val carrinhoItem = CarrinhoItem()
            carrinhoItem.uid = FirebaseAuth.getInstance().uid!!
            carrinhoItem.userPhone =
                FirebaseDatabase.getInstance().getReference(Common.UTILIZADOR_REF)
                    .child(FirebaseAuth.getInstance().uid!!).child("phone").toString()

            carrinhoItem.foodId = comidaList.get(position).id
            carrinhoItem.foodName = comidaList.get(position).name
            carrinhoItem.foodImage = comidaList.get(position).image
            carrinhoItem.foodPrice = comidaList.get(position).price.toDouble()
            carrinhoItem.foodQuantity = 1
            carrinhoItem.foodExtraPrice = 0.0
            carrinhoItem.foodAddon = "Default"
            carrinhoItem.foodSize = "Default"


            compositeDisposable.add(carrinhoDataSource.inserirOuTrocarTodos(carrinhoItem)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Toast.makeText(context, "Adicionado ao carrinho!", Toast.LENGTH_SHORT).show()
                    //Enviar uma notificacao para o HomeActivity para Atualizar o nosso CounterFab ou Icon do Carrinho

                    EventBus.getDefault().postSticky(CountCartEvent(true))
                }, { t: Throwable? ->
                    Toast.makeText(context, "ERRO: " + t!!.message, Toast.LENGTH_SHORT).show()
                })
            )
        }
    }

    fun onStop() {
        if (compositeDisposable != null)
            compositeDisposable.clear()
    }
}