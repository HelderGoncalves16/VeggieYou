package com.example.veggieyou.Callback

import com.example.veggieyou.Model.CategoriaModel

interface ICategoriaCallbackListener {
    fun onCategoriaCarregadaComSucesso(categoriaList: List<CategoriaModel>)
    fun onCategoriasCarregadasComErro(mensagem: String)
}