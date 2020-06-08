package com.example.veggieyou.Model

class CategoriasPopularesModel {
    var food_id: String? = null
    var menu_id: String? = null
    var name: String? = null
    var image: String? = null

    constructor()
    constructor(comida_id: String?, menu_id: String?, nome: String?, imagem: String?) {
        this.food_id = comida_id
        this.menu_id = menu_id
        this.name = nome
        this.image = imagem
    }


}