package ru.innohelpers.innohelp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.innohelpers.innohelp.InnoHelpApplication
import ru.innohelpers.innohelp.R
import ru.innohelpers.innohelp.view_models.care.CareViewModel
import ru.innohelpers.innohelp.view_models.delivery.DeliveryViewModel
import ru.innohelpers.innohelp.view_models.order.OrdersViewModel
import ru.innohelpers.innohelp.view_models.profile.ProfileViewModel
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    init {
        InnoHelpApplication.servicesComponent.inject(this)
    }

    @Inject
    lateinit var ordersViewModel: OrdersViewModel

    @Inject
    lateinit var deliveryViewModel: DeliveryViewModel

    @Inject
    lateinit var careViewModel: CareViewModel

    @Inject
    lateinit var profileViewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        val navView = findViewById<BottomNavigationView>(R.id.nav_view)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        //setupActionBarWithNavController(navController, navConfiguration)
        navView.setupWithNavController(navController)
    }
}