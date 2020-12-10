package ru.innohelpers.innohelp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import ru.innohelpers.innohelp.InnoHelpApplication
import ru.innohelpers.innohelp.R
import ru.innohelpers.innohelp.dialog_fragments.NewOrderItemDialogFragment
import ru.innohelpers.innohelp.view_data.order.OrderItemViewData
import ru.innohelpers.innohelp.view_data.order.OrderViewData
import ru.innohelpers.innohelp.view_models.order.OrdersViewModel
import javax.inject.Inject

class ViewOrderActivity : AppCompatActivity() {

    init {
        InnoHelpApplication.servicesComponent.inject(this)
    }

    private lateinit var titleTextView: TextView
    private lateinit var creatorTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var closedTextView: TextView
    private lateinit var itemsLayout: LinearLayout
    private lateinit var joinButton: AppCompatButton
    private lateinit var leaveButton: AppCompatButton
    private lateinit var closeButton: AppCompatButton
    private lateinit var addItemButton: AppCompatButton
    private lateinit var contentContainer: ScrollView
    private lateinit var contentLoading: ProgressBar

    @Inject
    lateinit var viewModel: OrdersViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_order)

        titleTextView = findViewById(R.id.activity_view_order_title)
        creatorTextView = findViewById(R.id.activity_view_order_creator)
        descriptionTextView = findViewById(R.id.activity_view_order_description)
        closedTextView = findViewById(R.id.activity_view_order_closed)
        itemsLayout = findViewById(R.id.activity_view_order_items)
        joinButton = findViewById(R.id.activity_view_order_join)
        leaveButton = findViewById(R.id.activity_view_order_leave)
        closeButton = findViewById(R.id.activity_view_order_close)
        addItemButton = findViewById(R.id.activity_view_order_add_item)
        contentContainer = findViewById(R.id.activity_view_order_content_container)
        contentLoading = findViewById(R.id.activity_view_order_loading)

        val orderId = intent.extras!!.getString("orderId", "")
        viewModel.currentItem.observe(this, { _: OrderViewData?, value: OrderViewData? ->
            if (value != null)
                setOrderData(value)
        })
        viewModel.isOwner.observe(this, null, { _: Boolean?, _: Boolean? ->
                joinButton.visibility = View.GONE
                closeButton.visibility = View.GONE
                leaveButton.visibility = View.GONE
                addItemButton.visibility = View.INVISIBLE
            })
        viewModel.isOwner.observe(this, true, { _:Boolean?, _: Boolean? ->
            addItemButton.visibility = View.VISIBLE
            joinButton.visibility = View.GONE
            leaveButton.visibility = View.GONE
            closeButton.visibility = View.VISIBLE
        })
        viewModel.isOwner.observe(this, false, {_: Boolean?, _: Boolean? ->
            addItemButton.visibility = View.INVISIBLE
            joinButton.visibility = View.VISIBLE
            leaveButton.visibility = View.GONE
            closeButton.visibility = View.GONE
        })

        viewModel.isJoined.observe(this, true, { _: Boolean?, _: Boolean? ->
            joinButton.visibility = View.GONE
            addItemButton.visibility = View.VISIBLE
            leaveButton.visibility = View.VISIBLE
        })
        viewModel.isJoined.observe(this, false, {_: Boolean?, _: Boolean? ->
            joinButton.visibility = View.VISIBLE
            addItemButton.visibility = View.INVISIBLE
            leaveButton.visibility = View.GONE
        })

        viewModel.isClosed.observe(this, true, { _: Boolean?, _: Boolean? ->
            joinButton.visibility = View.GONE
            closeButton.visibility = View.GONE
            leaveButton.visibility = View.GONE
            addItemButton.visibility = View.GONE
            closedTextView.visibility = View.VISIBLE
        })

        viewModel.busy.observe(this, {_: Boolean?, busy: Boolean? ->
            if (busy == null) return@observe
            if (busy) {
                contentLoading.visibility = View.VISIBLE
                contentContainer.visibility = View.GONE
            } else {
                contentLoading.visibility = View.GONE
                contentContainer.visibility = View.VISIBLE
            }
        })
        joinButton.setOnClickListener { viewModel.joinOrder() }
        leaveButton.setOnClickListener { viewModel.leaveOrder() }

        addItemButton.setOnClickListener {
            val newItemFragment = NewOrderItemDialogFragment()
            newItemFragment.setOnAddListener { link, description, price ->
                viewModel.addItem(link, description, price)
            }
            newItemFragment.show(supportFragmentManager, "NewItemDialog")
        }
        closeButton.setOnClickListener {
            viewModel.closeOrder()
        }

        if (orderId != "") viewModel.loadOrder(orderId, false)
    }

    private fun setOrderData(order: OrderViewData) {
        titleTextView.text = order.title
        creatorTextView.text = order.creator.userName
        descriptionTextView.text = order.description
        if (order.closed) closedTextView.visibility = View.VISIBLE
        else closeButton.visibility = View.GONE

        val inflater = LayoutInflater.from(this)
        itemsLayout.removeAllViews()
        for (orderItem in order.items) {
            val itemView = inflater.inflate(R.layout.view_order_item, itemsLayout, false)
            setOrderItemData(itemView, orderItem)
            itemsLayout.addView(itemView)
        }
    }

    private fun setOrderItemData(itemView: View, orderItem: OrderItemViewData) {
        val itemAddedBy = itemView.findViewById<TextView>(R.id.view_order_item_added_by)
        val itemLink = itemView.findViewById<TextView>(R.id.view_order_item_link)
        val itemDescription = itemView.findViewById<TextView>(R.id.view_order_item_description)
        val itemPrice = itemView.findViewById<TextView>(R.id.view_order_item_price)

        itemAddedBy.text = orderItem.addedBy.userName
        itemLink.text = orderItem.link
        itemDescription.text = orderItem.description
        itemPrice.text = orderItem.price.toString()
    }
}