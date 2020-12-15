package ru.innohelpers.innohelp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import ru.innohelpers.innohelp.R
import ru.innohelpers.innohelp.data.user.User
import ru.innohelpers.innohelp.view_models.LoginActivityViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var loginEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var loginButton: AppCompatButton
    private lateinit var noAccountLayout: LinearLayout
    private lateinit var existingAccountLayout: LinearLayout
    private lateinit var createNewAccount: AppCompatButton
    private lateinit var loginExistingAccount: AppCompatButton
    private lateinit var passwordConfirmEditText: TextInputEditText
    private lateinit var passwordConfirmLayout: TextInputLayout
    private lateinit var registerButton: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        loadingProgressBar = findViewById(R.id.activity_login_loading)
        loginEditText = findViewById(R.id.activity_login_login)
        passwordEditText = findViewById(R.id.activity_login_password)
        passwordConfirmEditText = findViewById(R.id.activity_login_password_confirm)
        passwordConfirmLayout = findViewById(R.id.activity_login_password_confirm_layout)
        loginButton = findViewById(R.id.activity_login_login_button)
        noAccountLayout = findViewById(R.id.activity_login_new_account_layout)
        existingAccountLayout = findViewById(R.id.activity_login_login_existing_layout)
        createNewAccount = findViewById(R.id.activity_login_create_new)
        loginExistingAccount = findViewById(R.id.activity_login_login_existing)
        registerButton = findViewById(R.id.activity_login_register_button)

        createNewAccount.setOnClickListener {
            switchToRegister()
        }

        loginExistingAccount.setOnClickListener {
            switchToLogin()
        }

        val viewModel: LoginActivityViewModel by viewModels()
        viewModel.busy.observe(this, { _: Boolean?, busy: Boolean? ->
            if (busy == true) loadingProgressBar.visibility = View.VISIBLE
            else loadingProgressBar.visibility = View.GONE
        })
        viewModel.user.observe(this, { _: User?, user: User? ->
            if (user == null) return@observe
            setResult(1)
            finish()
        })

        loginButton.setOnClickListener {
            viewModel.login(loginEditText.text.toString(), passwordEditText.text.toString())
        }

        registerButton.setOnClickListener {
            if (passwordEditText.text.toString() == passwordConfirmEditText.text.toString())
                viewModel.register(loginEditText.text.toString(), passwordEditText.text.toString())
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    private fun switchToRegister() {
        noAccountLayout.visibility = View.GONE
        existingAccountLayout.visibility = View.VISIBLE
        passwordConfirmLayout.visibility = View.VISIBLE
        loginButton.visibility = View.GONE
        registerButton.visibility = View.VISIBLE
        passwordConfirmEditText.setText("")
    }

    private fun switchToLogin() {
        noAccountLayout.visibility = View.VISIBLE
        existingAccountLayout.visibility = View.GONE
        passwordConfirmLayout.visibility = View.GONE
        registerButton.visibility = View.GONE
        loginButton.visibility = View.VISIBLE
    }
}