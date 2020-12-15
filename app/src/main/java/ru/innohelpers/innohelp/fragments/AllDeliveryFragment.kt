package ru.innohelpers.innohelp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.facebook.shimmer.ShimmerFrameLayout
import ru.innohelpers.innohelp.R
import ru.innohelpers.innohelp.activity.MainActivity
import ru.innohelpers.innohelp.activity.NewDeliveryActivity
import ru.innohelpers.innohelp.activity.ViewDeliveryActivity
import ru.innohelpers.innohelp.activity.ViewOrderActivity
import ru.innohelpers.innohelp.adapters.DeliveryRecyclerViewAdapter
import ru.innohelpers.innohelp.adapters.listeners.IItemSelectedListener
import ru.innohelpers.innohelp.view_data.delivery.DeliveryViewData

class AllDeliveryFragment : MainFragmentBase() {

    private lateinit var allDeliveryRecyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var shimmer: ShimmerFrameLayout
    private lateinit var deliveryAdapter: DeliveryRecyclerViewAdapter

    override fun newItemActivity(): Class<*> {
        return NewDeliveryActivity::class.java
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_all_delivery, container, false)

        allDeliveryRecyclerView = view.findViewById(R.id.all_delivery_recycler_view)
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh)
        shimmer = view.findViewById(R.id.all_delivery_shimmer)

        clearOrders()
        initAdapter()

        val viewModel = (activity as MainActivity).deliveryViewModel

        viewModel.allDeliveries.observe(viewLifecycleOwner, itemsInsertedListener = {collection, args ->
            for (itemPosition in args.startPosition until args.startPosition + args.count)
                addItem(collection[itemPosition], itemPosition)
        }, itemsRemovedListener = {_, args ->
            for (item in args.items) {
                removeItem(0)
            }
        })

        viewModel.loadingDeliveries.observe(viewLifecycleOwner, {_:Boolean?, value: Boolean? ->
            if (value == null) return@observe
            if (!value) {
                shimmer.stopShimmer()
                allDeliveryRecyclerView.visibility = View.VISIBLE
                shimmer.visibility = View.GONE
            }
            else {
                shimmer.startShimmer()
                shimmer.visibility = View.VISIBLE
                allDeliveryRecyclerView.visibility = View.GONE
            }
        })

        swipeRefreshLayout.setOnRefreshListener {
            viewModel.loadAllDeliveries(true)
            swipeRefreshLayout.isRefreshing = false
        }

        viewModel.loadAllDeliveries(false)
        initData()
        setHasOptionsMenu(true)

        return view
    }

    private fun initData() {
        val viewModel = (activity as MainActivity).deliveryViewModel
        clearOrders()
        initAdapter()
        for (item in viewModel.allDeliveries)
            addItem(item, 0)

        if (viewModel.loadingDeliveries.value == null) return
        if (viewModel.loadingDeliveries.value!!) {
            shimmer.startShimmer()
            shimmer.visibility = View.VISIBLE
            allDeliveryRecyclerView.visibility = View.GONE
        } else {
            shimmer.stopShimmer()
            allDeliveryRecyclerView.visibility = View.VISIBLE
            shimmer.visibility = View.GONE
        }
    }

    private fun addItem(item: DeliveryViewData, position: Int) {
        deliveryAdapter.data.add(position, item)
        deliveryAdapter.notifyItemInserted(position)
    }

    private fun removeItem(position: Int){
        deliveryAdapter.data.removeAt(position)
        deliveryAdapter.notifyItemRemoved(position)
    }

    private fun initAdapter() {
        deliveryAdapter = DeliveryRecyclerViewAdapter(arrayListOf())
        deliveryAdapter.setItemSelectedListener(object : IItemSelectedListener {
            override fun itemSelected(itemId: String) {
                val intent = Intent(context, ViewDeliveryActivity::class.java)
                intent.putExtra("deliveryId", itemId)
                startActivity(intent)
            }
        })
        allDeliveryRecyclerView.layoutManager = LinearLayoutManager(context)
        allDeliveryRecyclerView.adapter = deliveryAdapter
    }

    private fun clearOrders() {
        allDeliveryRecyclerView.adapter = null
    }

}