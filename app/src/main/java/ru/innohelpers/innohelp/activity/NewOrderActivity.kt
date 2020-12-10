package ru.innohelpers.innohelp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatButton
import ru.innohelpers.innohelp.InnoHelpApplication
import ru.innohelpers.innohelp.R
import ru.innohelpers.innohelp.view_models.order.OrdersViewModel
import javax.inject.Inject

class NewOrderActivity : AppCompatActivity() {

    init {
        InnoHelpApplication.servicesComponent.inject(this)
    }

    private lateinit var titleTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var publishButton: AppCompatButton

    @Inject
    lateinit var viewModel: OrdersViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_order)

        titleTextView = findViewById(R.id.activity_new_order_title)
        descriptionTextView = findViewById(R.id.activity_new_order_description)
        publishButton = findViewById(R.id.activity_new_order_publish)

        publishButton.setOnClickListener {
            val title = titleTextView.text.toString()
            val description = descriptionTextView.text.toString()
            if (title.isBlank() || description.isBlank()) {
                Toast.makeText(this, "Title and description cannot be blank", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.createOrder(title, description)
            finish()
        }
    }
}