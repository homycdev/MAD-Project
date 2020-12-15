package ru.innohelpers.innohelp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.textfield.TextInputEditText
import ru.innohelpers.innohelp.InnoHelpApplication
import ru.innohelpers.innohelp.R
import ru.innohelpers.innohelp.view_models.care.CareViewModel
import javax.inject.Inject

class NewCareActivity : AppCompatActivity() {

    init {
        InnoHelpApplication.servicesComponent.inject(this)
    }

    @Inject
    lateinit var viewModel: CareViewModel

    private lateinit var titleEditText: TextInputEditText
    private lateinit var descriptionEditText: TextInputEditText
    private lateinit var benefitEditText: TextInputEditText
    private lateinit var publishButton: AppCompatButton

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_care)
        setSupportActionBar(findViewById(R.id.toolbar))

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        titleEditText = findViewById(R.id.activity_new_care_title)
        descriptionEditText = findViewById(R.id.activity_new_care_description)
        benefitEditText = findViewById(R.id.activity_new_care_benefit)
        publishButton = findViewById(R.id.activity_new_care_publish)

        publishButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val description = descriptionEditText.text.toString()
            val benefit = benefitEditText.text.toString().toDouble()
            viewModel.createCare(title, description, benefit)
            finish()
        }
    }
}