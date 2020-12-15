package ru.innohelpers.innohelp.activity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import ru.innohelpers.innohelp.InnoHelpApplication
import ru.innohelpers.innohelp.R
import ru.innohelpers.innohelp.view_data.care.CareViewData
import ru.innohelpers.innohelp.view_models.care.CareViewModel
import javax.inject.Inject

class ViewCareActivity : AppCompatActivity() {

    init {
        InnoHelpApplication.servicesComponent.inject(this)
    }

    @Inject
    lateinit var viewModel: CareViewModel

    private lateinit var titleTextView: TextView
    private lateinit var ownerTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var benefitTextView: TextView
    private lateinit var container: ScrollView
    private lateinit var takeButton: AppCompatButton
    private lateinit var closeButton: AppCompatButton
    private lateinit var cancelButton: AppCompatButton
    private lateinit var loading: ProgressBar
    private lateinit var closedTextView: TextView

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_care)
        setSupportActionBar(findViewById(R.id.toolbar))

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        titleTextView = findViewById(R.id.activity_view_care_title)
        ownerTextView = findViewById(R.id.activity_view_care_creator)
        descriptionTextView = findViewById(R.id.activity_view_care_description)
        benefitTextView = findViewById(R.id.activity_view_care_benefit)
        container = findViewById(R.id.activity_view_care_content_container)
        takeButton = findViewById(R.id.activity_view_care_take)
        closeButton = findViewById(R.id.activity_view_care_close)
        cancelButton = findViewById(R.id.activity_view_care_cancel)
        loading = findViewById(R.id.activity_view_care_loading)
        closedTextView = findViewById(R.id.activity_view_care_closed)

        val careId = intent.extras!!.getString("careId", "")
        viewModel.currentItem.observe(this, { _: CareViewData?, value: CareViewData? ->
            if (value != null)
                setCareData(value)
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
                loading.visibility = View.VISIBLE
                container.visibility = View.GONE
            } else {
                loading.visibility = View.GONE
                container.visibility = View.VISIBLE
            }
        })
        takeButton.setOnClickListener { viewModel.takeDelivery(); }
        cancelButton.setOnClickListener { viewModel.cancelDelivery() }
        closeButton.setOnClickListener { viewModel.closeDelivery() }

        if (careId != "") viewModel.loadCare(careId, false)
    }

    private fun setCareData(care: CareViewData) {
        titleTextView.text = care.title
        ownerTextView.text = care.creator.userName
        descriptionTextView.text = care.description
        benefitTextView.text = care.benefit.toString()
        if (care.closed) closedTextView.visibility = View.VISIBLE
        else closeButton.visibility = View.GONE
    }
}