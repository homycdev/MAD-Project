package ru.innohelpers.innohelp.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import ru.innohelpers.innohelp.InnoHelpApplication
import ru.innohelpers.innohelp.R
import ru.innohelpers.innohelp.view_data.delivery.DeliveryViewData
import ru.innohelpers.innohelp.view_models.delivery.DeliveryViewModel
import javax.inject.Inject

class ViewDeliveryActivity : AppCompatActivity() {

    init {
        InnoHelpApplication.servicesComponent.inject(this)
    }


    @Inject
    lateinit var viewModel: DeliveryViewModel

    private lateinit var titleTextView: TextView
    private lateinit var creatorTextView: TextView
    private lateinit var locationTextView: TextView
    private lateinit var closedTextView: TextView
    private lateinit var itemsLayout: LinearLayout
    private lateinit var takeButton: AppCompatButton
    private lateinit var cancelButton: AppCompatButton
    private lateinit var closeButton: AppCompatButton
    private lateinit var contentContainer: ScrollView
    private lateinit var contentLoading: ProgressBar

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_delivery)
        setSupportActionBar(findViewById(R.id.toolbar))

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        titleTextView = findViewById(R.id.activity_view_delivery_title)
        creatorTextView = findViewById(R.id.activity_view_delivery_creator)
        locationTextView = findViewById(R.id.activity_view_delivery_location)
        closedTextView = findViewById(R.id.activity_view_delivery_closed)
        itemsLayout = findViewById(R.id.activity_view_delivery_items)
        takeButton = findViewById(R.id.activity_view_delivery_take)
        closeButton = findViewById(R.id.activity_view_delivery_close)
        cancelButton = findViewById(R.id.activity_view_delivery_cancel)
        contentContainer = findViewById(R.id.activity_view_delivery_content_container)
        contentLoading = findViewById(R.id.activity_view_delivery_loading)

        val deliveryId = intent.extras!!.getString("deliveryId", "")
        viewModel.currentItem.observe(this, { _: DeliveryViewData?, value: DeliveryViewData? ->
            if (value != null)
                setDeliveryData(value)
        })

        viewModel.isOwner.observe(this, null, { _: Boolean?, _: Boolean? ->
            takeButton.visibility = View.GONE
            closeButton.visibility = View.GONE
            cancelButton.visibility = View.GONE
        })
        viewModel.isOwner.observe(this, true, { _:Boolean?, _: Boolean? ->
            takeButton.visibility = View.GONE
            cancelButton.visibility = View.GONE
            closeButton.visibility = View.VISIBLE
        })
        viewModel.isOwner.observe(this, false, {_: Boolean?, _: Boolean? ->
            takeButton.visibility = View.VISIBLE
            cancelButton.visibility = View.GONE
            closeButton.visibility = View.GONE
        })

        viewModel.isTaken.observe(this, true, { _: Boolean?, _: Boolean? ->
            takeButton.visibility = View.GONE
            cancelButton.visibility = View.VISIBLE
        })
        viewModel.isTaken.observe(this, false, {_: Boolean?, _: Boolean? ->
            takeButton.visibility = View.VISIBLE
            cancelButton.visibility = View.GONE
        })

        viewModel.isClosed.observe(this, true, { _: Boolean?, _: Boolean? ->
            takeButton.visibility = View.GONE
            closeButton.visibility = View.GONE
            cancelButton.visibility = View.GONE
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
        takeButton.setOnClickListener { viewModel.takeDelivery() }
        cancelButton.setOnClickListener { viewModel.cancelDelivery() }
        closeButton.setOnClickListener { viewModel.closeDelivery() }

        if (deliveryId != "") viewModel.loadDelivery(deliveryId, false)
    }

    private fun setDeliveryData(delivery: DeliveryViewData) {
        titleTextView.text = delivery.title
        creatorTextView.text = delivery.creator.userName
        locationTextView.text = delivery.location
        if (delivery.closed) closedTextView.visibility = View.VISIBLE
        else closeButton.visibility = View.GONE

        val inflater = LayoutInflater.from(this)
        itemsLayout.removeAllViews()
        for (deliveryItem in delivery.items) {
            val itemView = inflater.inflate(R.layout.activity_view_delivery_item_layout, itemsLayout, false)
            setDeliveryItemData(itemView, deliveryItem)
            itemsLayout.addView(itemView)
        }
    }

    private fun setDeliveryItemData(itemView: View, deliveryItem: String) {
        val itemAddedBy = itemView.findViewById<TextView>(R.id.activity_view_delivery_item_title)

        itemAddedBy.text = deliveryItem
    }
}