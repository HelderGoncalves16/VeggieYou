package com.example.veggieyou.ui.home


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.veggieyou.Callback.ICategoriasPopularesCallback
import com.example.veggieyou.Callback.IPromocoesCallback
import com.example.veggieyou.Common.Common
import com.example.veggieyou.Model.CategoriasPopularesModel
import com.example.veggieyou.Model.CategoriasPromocoesModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeViewModel : ViewModel(), ICategoriasPopularesCallback, IPromocoesCallback {

    private var listaCategoriasPopularesPopularesMutableLiveData: MutableLiveData<List<CategoriasPopularesModel>>? =
        null
    private lateinit var mensagemError: MutableLiveData<String>
    private var categoriasPopularesPopularesCallbackListener: ICategoriasPopularesCallback

    //Promocoes
    private var promocoesListMutableLiveData: MutableLiveData<List<CategoriasPromocoesModel>>? =
        null
    private var promocoesCallbackListener: IPromocoesCallback
    val listaPromocoes: LiveData<List<CategoriasPromocoesModel>>
        get() {
            if (promocoesListMutableLiveData == null) {
                promocoesListMutableLiveData = MutableLiveData()
                mensagemError = MutableLiveData()
                carregarListaPromocoes()
            }

            return promocoesListMutableLiveData!!
        }

    private fun carregarListaPromocoes() {
        val tempList = ArrayList<CategoriasPromocoesModel>()
        val promocoesRef = FirebaseDatabase.getInstance().getReference(Common.PROMOCOES_REF)

        promocoesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                promocoesCallbackListener.categoriasPromocoesCarregasErro((p0.message))
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (itemSnapShot in p0.children) {
                    val model =
                        itemSnapShot.getValue<CategoriasPromocoesModel>(CategoriasPromocoesModel::class.java)
                    tempList.add(model!!)
                }

                promocoesCallbackListener.categoriasPromocoesCarregadasComSucesso(tempList)
            }
        })
    }


    val listaCategoriasPopulares: LiveData<List<CategoriasPopularesModel>>
        get() {
            if (listaCategoriasPopularesPopularesMutableLiveData == null) {
                listaCategoriasPopularesPopularesMutableLiveData = MutableLiveData()
                mensagemError = MutableLiveData()
                carregarLista()
            }

            return listaCategoriasPopularesPopularesMutableLiveData!!
        }

    private fun carregarLista() {
        val tempList = ArrayList<CategoriasPopularesModel>()
        val popularesRef = FirebaseDatabase.getInstance().getReference(Common.POPULARES_REF)

        popularesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                categoriasPopularesPopularesCallbackListener.categoriasCarregasErro((p0.message))
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (itemSnapShot in p0.children) {
                    val model =
                        itemSnapShot.getValue<CategoriasPopularesModel>(CategoriasPopularesModel::class.java)
                    tempList.add(model!!)
                }

                categoriasPopularesPopularesCallbackListener.categoriasCarregadasComSucesso(tempList)
            }
        })
    }

    init {
        categoriasPopularesPopularesCallbackListener = this
        promocoesCallbackListener = this
    }

    override fun categoriasCarregadasComSucesso(popularPopularesModelList: List<CategoriasPopularesModel>) {
        listaCategoriasPopularesPopularesMutableLiveData!!.value = popularPopularesModelList
    }

    override fun categoriasCarregasErro(mensagem: String) {
        mensagemError.value = mensagem
    }

    override fun categoriasPromocoesCarregadasComSucesso(promocoesList: List<CategoriasPromocoesModel>) {
        promocoesListMutableLiveData!!.value = promocoesList
    }

    override fun categoriasPromocoesCarregasErro(mensagem: String) {
        mensagemError.value = mensagem
    }
}

