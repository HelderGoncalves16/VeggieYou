package com.example.veggieyou.ui.DetalhesComida

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.andremion.counterfab.CounterFab
import com.bumptech.glide.Glide
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton
import com.example.veggieyou.Common.Common
import com.example.veggieyou.Model.FoodModel
import com.example.veggieyou.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.detalhes_comida_fragment.*

class DetalhesComidaFragment : Fragment(), TextWatcher {

    private lateinit var detalhesComidaViewModel: DetalhesComidaViewModel

    private lateinit var pedidoExtraBottomSheetDialog: BottomSheetDialog

    private var img_comida: ImageView? = null
    private var btnCarrinho: CounterFab? = null
    private var btnRating: FloatingActionButton? = null
    private var nome_comida: TextView? = null
    private var preco_comida: TextView? = null
    private var descricao_comida: TextView? = null
    private var botao_numero: ElegantNumberButton? = null
    private var ratingBar: RatingBar? = null
    private var mostrarComentarios: Button? = null
    private var rdi_group_dose: RadioGroup? = null
    private var img_pedidos_extra: ImageView? = null
    private var chip_group_utilizador_seleciona_extra: ChipGroup? = null

    //Extras layout
    private var chip_group_extras: ChipGroup? = null
    private var edt_pesquisa: EditText? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        detalhesComidaViewModel =
            ViewModelProviders.of(this).get(DetalhesComidaViewModel::class.java)
        val root = inflater.inflate(R.layout.detalhes_comida_fragment, container, false)

        initViews(root)

        detalhesComidaViewModel.getMutableLiveDataComida().observe(viewLifecycleOwner, Observer {
            displayInfo(it)
        })

        return root
    }

    private fun initViews(root: View?) {

        //Extras - Pedidos
        pedidoExtraBottomSheetDialog = BottomSheetDialog(context!!, R.style.DialogStyle)
        val layout_utilizador_seleciona_extra =
            layoutInflater.inflate(R.layout.layout_mostra_extra, null)
        chip_group_extras =
            layout_utilizador_seleciona_extra.findViewById(R.id.chip_group_extra) as ChipGroup
        edt_pesquisa = layout_utilizador_seleciona_extra.findViewById(R.id.edt_search) as EditText
        img_pedidos_extra = root!!.findViewById(R.id.img_pedidos_extras) as ImageView
        chip_group_utilizador_seleciona_extra =
            root.findViewById(R.id.chip_group_utilizador_seleciona_extra) as ChipGroup
        pedidoExtraBottomSheetDialog.setContentView(layout_utilizador_seleciona_extra)

        pedidoExtraBottomSheetDialog.setOnDismissListener { dialog ->
            mostrarExtrasUtilizador()
            calcularPrecoTotal()
        }

        //Detalhes
        btnCarrinho = root.findViewById(R.id.btnCart) as CounterFab
        img_comida = root.findViewById(R.id.img_food) as ImageView
        btnRating = root.findViewById(R.id.btn_rating) as FloatingActionButton
        nome_comida = root.findViewById(R.id.food_name) as TextView
        descricao_comida = root.findViewById(R.id.food_description) as TextView
        preco_comida = root.findViewById(R.id.food_price) as TextView
        botao_numero = root.findViewById(R.id.number_button) as ElegantNumberButton
        ratingBar = root.findViewById(R.id.ratingBar) as RatingBar
        mostrarComentarios = root.findViewById(R.id.btnMostrarComentarios) as Button
        rdi_group_dose = root.findViewById(R.id.rdi_dose) as RadioGroup

        //Eventos
        img_pedidos_extra!!.setOnClickListener {
            mostrarExtras()
            pedidoExtraBottomSheetDialog.show()
        }

    }

    private fun mostrarExtras() {
        if (Common.comidaSelecionada!!.addon.isNotEmpty()) {
            chip_group_extras!!.clearCheck()
            chip_group_extras!!.removeAllViews()
            edt_pesquisa!!.addTextChangedListener(this)

            for (addonModel in Common.comidaSelecionada!!.addon) {

                val chip = layoutInflater.inflate(R.layout.layout_chip, null, false) as Chip
                chip.text = StringBuilder(addonModel.name!!).append("(+€").append(addonModel.price)
                    .append(")").toString()
                chip.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        if (Common.comidaSelecionada!!.utilizadorSelectedAddon == null) {
                            Common.comidaSelecionada!!.utilizadorSelectedAddon = ArrayList()
                            Common.comidaSelecionada!!.utilizadorSelectedAddon!!.add(addonModel)
                        }
                    }
                    chip_group_extras!!.addView(chip)
                }
            }
        }
    }

    private fun mostrarExtrasUtilizador() {
        if (Common.comidaSelecionada!!.utilizadorSelectedAddon != null && Common.comidaSelecionada!!.utilizadorSelectedAddon!!.size > 0) {

            chip_group_utilizador_seleciona_extra!!.removeAllViews()

            for (addonModel in Common.comidaSelecionada!!.utilizadorSelectedAddon!!) {
                val chip =
                    layoutInflater.inflate(R.layout.layout_chip_eliminar, null, false) as Chip
                chip.text =
                    StringBuilder(addonModel.name!!).append("(+€").append(addonModel.price)
                        .append(")").toString()
                chip.isClickable = false
                chip.setOnCloseIconClickListener { view ->
                    chip_group_utilizador_seleciona_extra!!.removeView(view)
                    Common.comidaSelecionada!!.utilizadorSelectedAddon!!.remove(addonModel)
                    calcularPrecoTotal()
                }

                chip_group_utilizador_seleciona_extra!!.addView(chip)
            }
        } else if (Common.comidaSelecionada!!.utilizadorSelectedAddon!!.size == 0) {
            chip_group_utilizador_seleciona_extra!!.removeAllViews()
        }
    }

    private fun displayInfo(it: FoodModel?) {
        Glide.with(context!!).load(it!!.image).into(img_comida!!)
        nome_comida!!.text = StringBuilder(it.name!!)
        descricao_comida!!.text = StringBuilder(it.description!!)
        preco_comida!!.text = StringBuilder(it.price.toString())

        //Atribuir o nome da referencia da base de dados à toolbar
        (activity as AppCompatActivity).supportActionBar!!.title = Common.comidaSelecionada!!.name

        //Escolher Doses
        for (sizeModel in it.size) {
            val radioButton = RadioButton(context)
            radioButton.setOnCheckedChangeListener { _, b ->
                if (b) {
                    Common.comidaSelecionada!!.utilizadorSelectedSize = sizeModel
                    calcularPrecoTotal()
                }
                val params =
                    LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f)
                radioButton.layoutParams = params
                radioButton.text = sizeModel.name
                radioButton.tag = sizeModel.price

                rdi_group_dose!!.addView(radioButton)
            }

            //Default
            if (rdi_group_dose!!.childCount > 0) {
                val radioButton = rdi_group_dose!!.getChildAt(0) as RadioButton
                radioButton.isChecked = true
            }
        }
    }

    private fun calcularPrecoTotal() {
        var precoTotal = Common.comidaSelecionada!!.price.toDouble()
        var mostrarPreco = 0.0

        //Extras
        if (Common.comidaSelecionada!!.utilizadorSelectedAddon != null && Common.comidaSelecionada!!.utilizadorSelectedAddon!!.size > 0) {
            for (addonModel in Common.comidaSelecionada!!.utilizadorSelectedAddon!!) {
                precoTotal += addonModel.price.toDouble()
            }
        }

        //Doses
        precoTotal += Common.comidaSelecionada!!.utilizadorSelectedSize!!.price.toDouble()

        mostrarPreco = precoTotal * number_button.number.toInt()
        mostrarPreco = Math.round(mostrarPreco * 100.0) / 100.0

        food_price!!.text = StringBuilder("").append(Common.formatoPreco(mostrarPreco)).toString()

    }

    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        chip_group_extras!!.clearCheck()
        chip_group_extras!!.removeAllViews()
        for (addonModel in Common.comidaSelecionada!!.addon) {
            if (addonModel.name!!.toLowerCase().contains(s.toString().toLowerCase())) {
                val chip = layoutInflater.inflate(R.layout.layout_chip, null, false) as Chip
                chip.text = StringBuilder(addonModel.name!!).append("(+€").append(addonModel.price)
                    .append(")").toString()
                chip.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        if (Common.comidaSelecionada!!.utilizadorSelectedAddon == null) {
                            Common.comidaSelecionada!!.utilizadorSelectedAddon = ArrayList()
                            Common.comidaSelecionada!!.utilizadorSelectedAddon!!.add(addonModel)
                        }
                    }

                    chip_group_extras!!.addView(chip)
                }
            }
        }
    }
}
