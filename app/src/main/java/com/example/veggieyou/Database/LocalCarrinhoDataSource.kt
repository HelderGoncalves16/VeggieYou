package com.example.veggieyou.Database

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

class LocalCarrinhoDataSource(private var carrinhoDao: CarrinhoDAO) : CarrinhoDataSource {
    override fun getAllCarrinho(uid: String): Flowable<List<CarrinhoItem>> {
        return carrinhoDao.getAllCarrinho(uid)
    }

    override fun countItemCarrinho(uid: String): Single<Int> {
        return carrinhoDao.countItemCarrinho(uid)
    }

    override fun somarPreco(uid: String): Single<Double> {
        return carrinhoDao.somarPreco(uid)
    }

    override fun getItemCarrinho(foodId: String, uid: String): Single<CarrinhoItem> {
        return carrinhoDao.getItemCarrinho(foodId, uid)
    }

    override fun inserirOuTrocarTodos(vararg carrinhoItem: CarrinhoItem): Completable {
        return carrinhoDao.inserirOuTrocarTodos(*carrinhoItem)
    }

    override fun updateCarrinho(carrinho: CarrinhoItem): Single<Int> {
        return carrinhoDao.updateCarrinho(carrinho)
    }

    override fun deleteCarrinho(carrinho: CarrinhoItem): Single<Int> {
        return carrinhoDao.deleteCarrinho(carrinho)
    }

    override fun limparCarrinho(uid: String): Single<Int> {
        return carrinhoDao.limparCarrinho(uid)
    }
}