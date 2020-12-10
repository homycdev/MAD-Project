package ru.innohelpers.innohelp.fragments

import android.content.Intent
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import ru.innohelpers.innohelp.InnoHelpApplication
import ru.innohelpers.innohelp.R
import ru.innohelpers.innohelp.activity.LoginActivity
import ru.innohelpers.innohelp.activity.NewOrderActivity
import ru.innohelpers.innohelp.services.authentication.IUserProvider
import javax.inject.Inject

abstract class MainFragmentBase : Fragment() {

    init {
        InnoHelpApplication.servicesComponent.inject(this@MainFragmentBase)
    }

    @Inject
    lateinit var userProvider: IUserProvider

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_activity_menu, menu)
    }

    abstract fun newItemActivity() : Class<*>

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_new_request) {
            if (userProvider.user != null) {
                val intent = Intent(context, newItemActivity())
                startActivity(intent)
            } else {
                val intent = Intent(context, LoginActivity::class.java)
                startActivityForResult(intent, 1)
            }
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            1 -> {
                if (resultCode == 1) {
                    val intent = Intent(context, newItemActivity())
                    startActivityForResult(intent, 2)
                    return
                }
            }
        }
    }
}