package ru.innohelpers.innohelp.activity

import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputEditText
import ru.innohelpers.innohelp.R

class NewDelivery : AppCompatActivity() {

    private lateinit var addNewItemButton: AppCompatImageButton
    private lateinit var newItemTextView: TextInputEditText
    private lateinit var itemsLinearLayout: LinearLayout
    private var itemsCount = 0

    private var items: HashMap<Int, String> = HashMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_delivery)
        setSupportActionBar(findViewById(R.id.toolbar))

        itemsLinearLayout = findViewById(R.id.new_request_items_layout)
        newItemTextView = findViewById(R.id.new_request_new_item_edit_text)
        addNewItemButton = findViewById(R.id.new_request_add_item_button)
        addNewItemButton.setOnClickListener {
            val itemTitle =  newItemTextView.text.toString()
            addNewItem(itemTitle)
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