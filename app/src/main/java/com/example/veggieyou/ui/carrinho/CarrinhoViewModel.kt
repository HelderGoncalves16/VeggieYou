package com.example.veggieyou.ui.carrinho

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.veggieyou.Database.CarrinhoDataSource
import com.example.veggieyou.Database.CarrinhoDatabase
import com.example.veggieyou.Database.CarrinhoItem
import com.example.veggieyou.Database.LocalCarrinhoDataSource
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CarrinhoViewModel : ViewModel() {

    private val compositeDisposable: CompositeDisposable
    private var carrinhoDataSource: CarrinhoDataSource? = null
    private var mutableLiveDataCarrinhoItem: MutableLiveData<List<CarrinhoItem>>? = null

    init {
        compositeDisposable = CompositeDisposable()
    }

    fun initCarrinhoDataSource(context: Context) {
        carrinhoDataSource =
            LocalCarrinhoDataSource(CarrinhoDatabase.getInstance(context).carrinhoDao())
    }

    fun getMutableLiveDataCarrinhoItem(): MutableLiveData<List<CarrinhoItem>> {
        if (mutableLiveDataCarrinhoItem == null)
            mutableLiveDataCarrinhoItem = MutableLiveData()
        getCarrinhoItems()
        return mutableLiveDataCarrinhoItem!!
    }

    private fun getCarrinhoItems() {
        val utilizador = FirebaseAuth.getInstance().currentUser
        compositeDisposable.addAll(
            carrinhoDataSource!!.getAllCarrinho(utilizador!!.uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ carrinhoItems ->
                    mutableLiveDataCarrinhoItem!!.value = carrinhoItems
                }, { t: Throwable? -> mutableLiveDataCarrinhoItem!!.value = null })
        )
    }

    fun onStop() {
        compositeDisposable.clear()
    }
}
