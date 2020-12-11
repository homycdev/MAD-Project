package ru.innohelpers.innohelp.dialog_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import ru.innohelpers.innohelp.R

class NewOrderItemDialogFragment : DialogFragment() {

    private lateinit var linkEditText: TextInputEditText
    private lateinit var descriptionEditText: TextInputEditText
    private lateinit var priceEditText: TextInputEditText
    private lateinit var cancelButton: AppCompatButton
    private lateinit var addButton: AppCompatButton

    private var listener: OnNewItemListener? = null

    fun setOnAddListener(listener: OnNewItemListener) {
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_new_order_item, container, false)
        linkEditText = view.findViewById(R.id.dialog_new_order_item_link)
        descriptionEditText = view.findViewById(R.id.dialog_new_order_item_description)
        priceEditText = view.findViewById(R.id.dialog_new_order_item_price)
        cancelButton = view.findViewById(R.id.dialog_new_order_item_cancel)
        addButton = view.findViewById(R.id.dialog_new_order_item_add)

        cancelButton.setOnClickListener {
            dismiss()
        }

        addButton.setOnClickListener {
            val link = linkEditText.text.toString()
            val description = descriptionEditText.text.toString()
            val price = priceEditText.text.toString().toDouble()
            listener?.invoke(link, description, price)
            dismiss()
        }

        return view
    }
}

typealias OnNewItemListener = (String, String, Double) -> Unit