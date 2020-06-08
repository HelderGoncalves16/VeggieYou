package com.example.veggieyou.Callback

import com.example.veggieyou.Model.CategoriasPromocoesModel

interface IPromocoesCallback {
    fun categoriasPromocoesCarregadasComSucesso(promocoesList: List<CategoriasPromocoesModel>)
    fun categoriasPromocoesCarregasErro(mensagem: String)
}