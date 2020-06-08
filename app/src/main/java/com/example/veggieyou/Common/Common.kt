package com.example.veggieyou.Common

import com.example.veggieyou.Model.CategoriaModel
import com.example.veggieyou.Model.FoodModel
import java.math.RoundingMode
import java.text.DecimalFormat

object Common {
    var categoriaSelecionada: CategoriaModel? = null
    val CATEGORIA_REFERENCE: String = "Categoria"
    val POPULARES_REF: String = "MostPopular"
    val PROMOCOES_REF: String = "Promocoes"
    var UTILIZADOR_REF = "Utilizadores"
    val COLUNA_DEFAULT_CONTADOR: Int = 0
    val LARGURA_COLUNA: Int = 0
    //Por exemplo massas como está sozinho nas categorias a nivel de colunas
    //Iria aparecer a ocupar uma fila inteira


    var comidaSelecionada: FoodModel? = null

    fun formatoPreco(preco: Double): String {
        if (preco != 0.toDouble()) {
            val df = DecimalFormat("#,##0.00")
            df.roundingMode = RoundingMode.HALF_UP
            val precoFinal = StringBuilder(df.format(preco)).toString()
            return precoFinal.replace(".", ",")
        } else {
            return "0,00"
        }
    }
}