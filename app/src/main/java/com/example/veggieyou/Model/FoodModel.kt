package com.example.veggieyou.Model

class FoodModel {
    var name: String? = null
    var image: String? = null
    var id: String? = null
    var description: String? = null
    var price: Long = 0
    var addon: List<AddonModel> = ArrayList<AddonModel>()
    var size: List<SizeModel> = ArrayList<SizeModel>()

    var utilizadorSelectedAddon: MutableList<AddonModel>? = null
    var utilizadorSelectedSize: SizeModel? = null

}
