package com.example.veggieyou.Callback

import com.example.veggieyou.Model.CategoriasPopularesModel

interface ICategoriasPopularesCallback {

    fun categoriasCarregadasComSucesso(popularPopularesModelList: List<CategoriasPopularesModel>)
    fun categoriasCarregasErro(mensagem: String)
}