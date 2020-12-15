package ru.innohelpers.innohelp.activity

import android.os.Bundle
import android.view.MenuItem
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputEditText
import ru.innohelpers.innohelp.InnoHelpApplication
import ru.innohelpers.innohelp.R
import ru.innohelpers.innohelp.view_models.delivery.DeliveryViewModel
import javax.inject.Inject

class NewDeliveryActivity : AppCompatActivity() {

    init {
        InnoHelpApplication.servicesComponent.inject(this)
    }

    @Inject
    lateinit var viewModel: DeliveryViewModel

    private lateinit var titleEditText: TextInputEditText
    private lateinit var locationEditText: TextInputEditText
    private lateinit var totalCostEditText: TextInputEditText
    private lateinit var benefitEditText: TextInputEditText
    private lateinit var addNewItemButton: AppCompatImageButton
    private lateinit var newItemTextView: TextInputEditText
    private lateinit var itemsLinearLayout: LinearLayout
    private lateinit var publishButton: AppCompatButton
    private var itemsCount = 0

    private var items: HashMap<Int, String> = HashMap()

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_delivery)
        setSupportActionBar(findViewById(R.id.toolbar))

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        totalCostEditText = findViewById(R.id.new_request_cost_edit_text)
        benefitEditText = findViewById(R.id.new_request_benefit_edit_text)
        titleEditText = findViewById(R.id.new_request_title_edit_text)
        locationEditText = findViewById(R.id.new_request_location_edit_text)
        itemsLinearLayout = findViewById(R.id.new_request_items_layout)
        newItemTextView = findViewById(R.id.new_request_new_item_edit_text)
        addNewItemButton = findViewById(R.id.new_request_add_item_button)
        publishButton = findViewById(R.id.new_request_publish_button)

        addNewItemButton.setOnClickListener {
            val itemTitle =  newItemTextView.text.toString()
            addNewItem(itemTitle)
        }

        publishButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val location = locationEditText.text.toString()
            val cost = totalCostEditText.text.toString().toDouble()
            val benefit = benefitEditText.text.toString().toDouble()
            val deliveryItems = ArrayList<String>()
            for (itemPair in items) deliveryItems.add(itemPair.value)
            viewModel.createDelivery(title, location, deliveryItems, cost, benefit)
            finish()
        }
    }

    private fun addNewItem(item: String) {
        items[itemsCount] = item

        val itemView = layoutInflater.inflate(R.layout.activity_new_delivery_item_layout, itemsLinearLayout, false)
        val itemViewText: TextInputEditText = itemView.findViewById(R.id.new_request_new_item_edit_text)
        val deleteButton: AppCompatImageButton = itemView.findViewById(R.id.new_request_delete_item_button)

        itemViewText.tag = itemsCount
        deleteButton.tag = itemsCount

        itemViewText.setText(item)
        newItemTextView.text = null

        itemViewText.addTextChangedListener(afterTextChanged = {
            items[itemViewText.tag as Int] = it.toString()
        })

        deleteButton.setOnClickListener {
            items.remove(it.tag as Int)
            itemsLinearLayout.removeView(itemView)
        }

        itemsCount++
        itemsLinearLayout.addView(itemView)
    }
}