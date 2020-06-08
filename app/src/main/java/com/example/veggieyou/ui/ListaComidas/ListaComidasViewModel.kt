package com.example.veggieyou.ui.ListaComidas

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.veggieyou.Common.Common
import com.example.veggieyou.Model.FoodModel

class ListaComidasViewModel : ViewModel() {

    private var mutableComidaModelListData: MutableLiveData<List<FoodModel>>? = null

    fun getMutableComidaModelListaData(): MutableLiveData<List<FoodModel>> {
        if (mutableComidaModelListData == null)
            mutableComidaModelListData = MutableLiveData()
        mutableComidaModelListData!!.value = Common.categoriaSelecionada!!.foods
        return mutableComidaModelListData!!
    }
}
