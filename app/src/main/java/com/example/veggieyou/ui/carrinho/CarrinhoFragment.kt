package com.example.veggieyou.ui.carrinho

import android.graphics.Color
import android.os.Bundle
import android.os.Parcelable
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.veggieyou.Adapter.CarrinhoAdapter
import com.example.veggieyou.Callback.IMyButtonCallback
import com.example.veggieyou.Common.MySwipeHelper
import com.example.veggieyou.Database.CarrinhoDataSource
import com.example.veggieyou.Database.CarrinhoDatabase
import com.example.veggieyou.Database.LocalCarrinhoDataSource
import com.example.veggieyou.EventBus.CountCartEvent
import com.example.veggieyou.EventBus.RemoveFabCarrinho
import com.example.veggieyou.EventBus.UpdateItemCarrinho
import com.example.veggieyou.R
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class CarrinhoFragment : Fragment() {

    private var carrinhoDataSource: CarrinhoDataSource? = null
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()
    private var recyclerViewState: Parcelable? = null
    private lateinit var carrinhoViewModel: CarrinhoViewModel

    var txt_carrinho_vazio: TextView? = null
    var txt_preco_total: TextView? = null
    var group_place_holder: CardView? = null
    var recycler_cart: RecyclerView? = null

    var adapter: CarrinhoAdapter? = null

    override fun onResume() {
        super.onResume()
        calcularPrecoTotal()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        EventBus.getDefault().postSticky(RemoveFabCarrinho(true))

        carrinhoViewModel = ViewModelProviders.of(this).get(CarrinhoViewModel::class.java)
        //Data Source
        carrinhoViewModel.initCarrinhoDataSource(context!!)

        val root = inflater.inflate(R.layout.carrinho_fragment, container, false)

        initViews(root)
        carrinhoViewModel.getMutableLiveDataCarrinhoItem().observe(viewLifecycleOwner, Observer {
            if (it == null || it.isEmpty()) {
                recycler_cart!!.visibility = View.GONE
                group_place_holder!!.visibility = View.GONE
                txt_carrinho_vazio!!.visibility = View.VISIBLE
            } else {
                recycler_cart!!.visibility = View.VISIBLE
                group_place_holder!!.visibility = View.VISIBLE
                txt_carrinho_vazio!!.visibility = View.GONE

                adapter = CarrinhoAdapter(context!!, it)
                recycler_cart!!.adapter = adapter
            }
        })
        return root

    }

    private fun initViews(root: View) {

        setHasOptionsMenu(true) //Para aparecer o menu de limpar o carrinho
        carrinhoDataSource =
            LocalCarrinhoDataSource(CarrinhoDatabase.getInstance(context!!).carrinhoDao())
        recycler_cart = root.findViewById(R.id.recycler_cart) as RecyclerView
        recycler_cart!!.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(context)
        recycler_cart!!.layoutManager = layoutManager
        recycler_cart!!.addItemDecoration(DividerItemDecoration(context, layoutManager.orientation))

        val swipe = object : MySwipeHelper(context!!, recycler_cart!!, 200) {
            override fun instantiateMyButton(
                viewHolder: RecyclerView.ViewHolder,
                buffer: MutableList<MyButton>
            ) {
                buffer.add(
                    MyButton(context!!, "Remover", 30, 0, Color.parseColor("#FF3C30"),
                        object : IMyButtonCallback {
                            override fun onClick(pos: Int) {
                                val apagarItem = adapter!!.getItemAtPosition(pos)
                                carrinhoDataSource!!.deleteCarrinho(apagarItem)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(object : SingleObserver<Int> {
                                        override fun onSuccess(t: Int) {
                                            somarPrecoDelete()
                                            adapter!!.notifyItemRemoved(pos)
                                            EventBus.getDefault().postSticky(CountCartEvent(true))
                                            Toast.makeText(
                                                context,
                                                "Item removido!",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                        }

                                        override fun onSubscribe(d: Disposable) {
                                            TODO("Not yet implemented")
                                        }

                                        override fun onError(e: Throwable) {
                                            Toast.makeText(
                                                context,
                                                "" + e.message,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }

                                    })
                            }

                        })
                )
            }

        }

        txt_carrinho_vazio = root.findViewById(R.id.txt_empty_cart) as TextView
        txt_preco_total = root.findViewById(R.id.txt_preco_total) as TextView
        group_place_holder = root.findViewById(R.id.groupe_place_holder) as CardView

    }

    private fun somarPrecoDelete() {
        carrinhoDataSource!!.somarPreco(FirebaseAuth.getInstance().currentUser!!.uid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<Double> {
                override fun onSuccess(t: Double) {
                    txt_preco_total!!.text = StringBuilder("Total: ").append(t)
                }

                override fun onSubscribe(d: Disposable) {
                    TODO("Not yet implemented")
                }

                override fun onError(e: Throwable) {
                    if (!e.message!!.contains("Query returned empty"))
                        Toast.makeText(context, "" + e.message!!, Toast.LENGTH_SHORT).show()
                }

            })
    }

    override fun onStart() {
        super.onStart()
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        carrinhoViewModel.onStop()
        compositeDisposable.clear()
        EventBus.getDefault().postSticky(RemoveFabCarrinho(false))
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this)
    }


    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onUpdateItemCarrinho(event: UpdateItemCarrinho) {
        if (event.carrinhoItems != null) {
            recyclerViewState = recycler_cart!!.layoutManager!!.onSaveInstanceState()
            carrinhoDataSource!!.updateCarrinho(event.carrinhoItems)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<Int> {
                    override fun onSuccess(preco: Int) {
                        calcularPrecoTotal()
                        recycler_cart!!.layoutManager!!.onRestoreInstanceState(recyclerViewState)
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onError(e: Throwable) {
                        Toast.makeText(context, "[CARRINHO ERRO]" + e.message, Toast.LENGTH_SHORT)
                            .show()
                    }

                })
        }
    }

    private fun calcularPrecoTotal() {
        carrinhoDataSource!!.somarPreco(FirebaseAuth.getInstance().uid!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<Double> {
                override fun onSuccess(preco: Double) {
                    txt_preco_total!!.text = StringBuilder("Total: ").append(preco)
                }

                override fun onError(e: Throwable) {
                    if (!e.message!!.contains("Query returned empty"))
                        Toast.makeText(context, "[CALCURA PRECO ERRO]", Toast.LENGTH_SHORT)
                            .show()
                }

                override fun onSubscribe(d: Disposable) {
                }
            })
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.action_limpar_carrinho).isVisible = true //Esconder o menu
        super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.carrinho_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_limpar_carrinho) {

            carrinhoDataSource!!.limparCarrinho(FirebaseAuth.getInstance().uid!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<Int> {
                    override fun onSuccess(t: Int) {
                        Toast.makeText(context, "CARRINHO ESVAZIADO", Toast.LENGTH_SHORT)
                        EventBus.getDefault().postSticky(CountCartEvent(true))

                    }

                    override fun onSubscribe(d: Disposable) {
                        TODO("Not yet implemented")
                    }

                    override fun onError(e: Throwable) {
                        Toast.makeText(context, "" + e.message, Toast.LENGTH_SHORT)
                    }

                })
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}



