package com.example.veggieyou.ui.menu

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.veggieyou.Callback.ICategoriaCallbackListener
import com.example.veggieyou.Common.Common
import com.example.veggieyou.Model.CategoriaModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MenuViewModel : ViewModel(), ICategoriaCallbackListener {


    private var categoriasListMutableLiveData: MutableLiveData<List<CategoriaModel>>? = null
    private var mensagemErro: MutableLiveData<String> = MutableLiveData()
    private val categoriaCallbackListener: ICategoriaCallbackListener

    init {
        categoriaCallbackListener = this
    }

    fun recebeListaCategorias(): MutableLiveData<List<CategoriaModel>> {
        if (categoriasListMutableLiveData == null) {
            categoriasListMutableLiveData = MutableLiveData()
            carregarCategorias()
        }

        return categoriasListMutableLiveData!!
    }

    private fun carregarCategorias() {

        val tempList = ArrayList<CategoriaModel>()
        val categoriaRef = FirebaseDatabase.getInstance().getReference(Common.CATEGORIA_REFERENCE)

        categoriaRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                categoriaCallbackListener.onCategoriasCarregadasComErro((p0.message))
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (itemSnapShot in p0.children) {
                    val model = itemSnapShot.getValue<CategoriaModel>(CategoriaModel::class.java)
                    model!!.menu_id = itemSnapShot.key
                    tempList.add(model)
                }
                categoriaCallbackListener.onCategoriaCarregadaComSucesso(tempList)
            }
        })
    }

    fun getMensagemErro(): MutableLiveData<String> {
        return mensagemErro
    }

    override fun onCategoriaCarregadaComSucesso(categoriaList: List<CategoriaModel>) {
        categoriasListMutableLiveData!!.value = categoriaList
    }

    override fun onCategoriasCarregadasComErro(mensagem: String) {
        mensagemErro.value = mensagem
    }
}
