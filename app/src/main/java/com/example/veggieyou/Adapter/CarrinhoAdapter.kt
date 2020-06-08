package com.example.veggieyou.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton
import com.example.veggieyou.Database.CarrinhoDataSource
import com.example.veggieyou.Database.CarrinhoDatabase
import com.example.veggieyou.Database.CarrinhoItem
import com.example.veggieyou.Database.LocalCarrinhoDataSource
import com.example.veggieyou.EventBus.UpdateItemCarrinho
import com.example.veggieyou.R
import io.reactivex.disposables.CompositeDisposable
import org.greenrobot.eventbus.EventBus


class CarrinhoAdapter(
    internal var context: Context,
    internal var carrinhoItems: List<CarrinhoItem>
) : RecyclerView.Adapter<CarrinhoAdapter.MyViewHolder>() {

    internal var compositeDisposable: CompositeDisposable
    internal var carrinhoDataSource: CarrinhoDataSource

    init {
        compositeDisposable = CompositeDisposable()
        carrinhoDataSource =
            LocalCarrinhoDataSource(CarrinhoDatabase.getInstance(context).carrinhoDao())
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var imagem_carrinho: ImageView
        lateinit var txt_comida_nome: TextView
        lateinit var txt_comida_preco: TextView
        lateinit var number_button: ElegantNumberButton

        init {
            imagem_carrinho = itemView.findViewById(R.id.imagem_carrinho) as ImageView
            txt_comida_nome = itemView.findViewById(R.id.txt_comida_nome) as TextView
            txt_comida_preco = itemView.findViewById(R.id.txt_comida_preco) as TextView
            number_button = itemView.findViewById(R.id.number_button) as ElegantNumberButton
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.layout_carrinho_items, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return carrinhoItems.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(context).load(carrinhoItems[position].foodImage).into(holder.imagem_carrinho)
        holder.txt_comida_nome.text = StringBuilder(carrinhoItems[position].foodName!!)
        holder.txt_comida_preco.text =
            StringBuilder("").append(carrinhoItems[position].foodPrice + carrinhoItems[position].foodExtraPrice)
        holder.number_button.number = carrinhoItems[position].foodQuantity.toString()

        holder.number_button.setOnValueChangeListener { view, oldValue, newValue ->
            carrinhoItems[position].foodQuantity = newValue
            EventBus.getDefault().postSticky(UpdateItemCarrinho(carrinhoItems[position]))
        }
    }

    fun getItemAtPosition(pos: Int): CarrinhoItem {
        return carrinhoItems[pos]
    }
}