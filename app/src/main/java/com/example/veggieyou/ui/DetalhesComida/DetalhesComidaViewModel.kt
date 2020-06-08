package com.example.veggieyou.ui.DetalhesComida

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.veggieyou.Common.Common
import com.example.veggieyou.Model.FoodModel

class DetalhesComidaViewModel : ViewModel() {

    private var mutableLiveDataComida: MutableLiveData<FoodModel>? = null

    fun getMutableLiveDataComida(): MutableLiveData<FoodModel> {
        if (mutableLiveDataComida == null)
            mutableLiveDataComida = MutableLiveData()
        mutableLiveDataComida!!.value = Common.comidaSelecionada
        return mutableLiveDataComida!!
    }


}
