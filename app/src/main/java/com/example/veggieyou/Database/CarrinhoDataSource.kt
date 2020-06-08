package com.example.veggieyou.Database

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface CarrinhoDataSource {
    fun getAllCarrinho(uid: String): Flowable<List<CarrinhoItem>>
    fun countItemCarrinho(uid: String): Single<Int>
    fun somarPreco(uid: String): Single<Double>
    fun getItemCarrinho(foodId: String, uid: String): Single<CarrinhoItem>
    fun inserirOuTrocarTodos(vararg carrinhoItem: CarrinhoItem): Completable
    fun updateCarrinho(carrinho: CarrinhoItem): Single<Int>
    fun deleteCarrinho(carrinho: CarrinhoItem): Single<Int>
    fun limparCarrinho(uid: String): Single<Int>
}