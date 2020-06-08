package com.example.veggieyou

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.veggieyou.Database.CarrinhoDataSource
import com.example.veggieyou.Database.CarrinhoDatabase
import com.example.veggieyou.Database.LocalCarrinhoDataSource
import com.example.veggieyou.EventBus.CategoriaClick
import com.example.veggieyou.EventBus.ComidaClick
import com.example.veggieyou.EventBus.CountCartEvent
import com.example.veggieyou.EventBus.RemoveFabCarrinho
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.app_bar_home.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class HomeActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var carrinhoDataSource: CarrinhoDataSource
    private lateinit var navController: NavController

    override fun onResume() {
        super.onResume()
        countCarrinhoItem()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        carrinhoDataSource =
            LocalCarrinhoDataSource(CarrinhoDatabase.getInstance(this).carrinhoDao())

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            navController.navigate(R.id.nav_carrinho)
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        navController = findNavController(R.id.nav_host_fragment)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_menu,
                R.id.nav_comida_lista,
                R.id.nav_comida_detalhes,
                R.id.nav_definicoes,
                R.id.nav_sign_out,
                R.id.nav_carrinho
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        //   countCarrinhoItem()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onCategoriaSelecionada(event: CategoriaClick) {
        if (event.isSucess) {
            //  Toast.makeText(this,"Click to "+event.categoria.name, Toast.LENGTH_SHORT).show()
            findNavController(R.id.nav_host_fragment).navigate(R.id.nav_comida_lista)
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onComidaSelecionada(event: ComidaClick) {
        if (event.isSucess) {
            //Teste
            // Toast.makeText(this,"Click to "+event.comidaModel.name, Toast.LENGTH_SHORT).show()
            findNavController(R.id.nav_host_fragment).navigate(R.id.nav_comida_detalhes)
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onCounterCarrinhoEvent(event: CountCartEvent) {
        if (event.isSucess) {
            countCarrinhoItem()
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onRemoveFabEvent(event: RemoveFabCarrinho) {
        if (event.estaRemovido) {
            fab.hide()
        } else {
            fab.show()
        }
    }

    private fun countCarrinhoItem() {
        carrinhoDataSource.countItemCarrinho(FirebaseAuth.getInstance().uid!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<Int> {
                override fun onSuccess(t: Int) {
                    fab.count = t
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    if (!e.message!!.contains("Query returned empty"))
                        Toast.makeText(this@HomeActivity, "ERRO: " + e.message, Toast.LENGTH_SHORT)
                            .show()
                    else
                        fab.count = 0
                }

            })
    }

}
