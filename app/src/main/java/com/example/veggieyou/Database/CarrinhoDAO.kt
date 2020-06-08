package com.example.veggieyou.Database

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface CarrinhoDAO {
    @Query("SELECT * FROM Carrinho WHERE uid=:uid")
    fun getAllCarrinho(uid: String): Flowable<List<CarrinhoItem>>

    @Query("SELECT COUNT (*) FROM Carrinho WHERE uid=:uid")
    fun countItemCarrinho(uid: String): Single<Int>

    @Query("SELECT SUM((foodPrice+foodExtraPrice)*foodQuantity) FROM Carrinho WHERE uid=:uid")
    fun somarPreco(uid: String): Single<Double>

    @Query("SELECT * FROM Carrinho WHERE foodId=:foodId AND uid=:uid")
    fun getItemCarrinho(foodId: String, uid: String): Single<CarrinhoItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun inserirOuTrocarTodos(vararg carrinhoItem: CarrinhoItem): Completable

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateCarrinho(carrinho: CarrinhoItem): Single<Int>

    @Delete
    fun deleteCarrinho(carrinho: CarrinhoItem): Single<Int>

    @Query("DELETE FROM Carrinho WHERE uid=:uid")
    fun limparCarrinho(uid: String): Single<Int>
}