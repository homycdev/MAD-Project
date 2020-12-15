package ru.innohelpers.innohelp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.facebook.shimmer.ShimmerFrameLayout
import ru.innohelpers.innohelp.R
import ru.innohelpers.innohelp.activity.MainActivity
import ru.innohelpers.innohelp.activity.NewOrderActivity
import ru.innohelpers.innohelp.activity.ViewOrderActivity
import ru.innohelpers.innohelp.adapters.OrdersRecyclerViewAdapter
import ru.innohelpers.innohelp.adapters.listeners.IItemSelectedListener
import ru.innohelpers.innohelp.view_data.order.OrderViewData

class AllOrdersFragment : MainFragmentBase() {

    private lateinit var allDeliveryRecyclerView: RecyclerView
    private lateinit var shimmer: ShimmerFrameLayout
    private lateinit var ordersAdapter: OrdersRecyclerViewAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    override fun newItemActivity(): Class<*> {
        return NewOrderActivity::class.java
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_all_delivery, container, false)


        allDeliveryRecyclerView = view.findViewById(R.id.all_delivery_recycler_view)
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh)
        shimmer = view.findViewById(R.id.all_delivery_shimmer)

        clearOrders()
        initAdapter()

        val viewModel = (activity as MainActivity).ordersViewModel

        viewModel.allOrders.observe(viewLifecycleOwner, itemsInsertedListener = {collection, args ->
            for (itemPosition in args.startPosition until args.startPosition + args.count)
                addItem(collection[itemPosition], itemPosition)
        }, itemsRemovedListener = {_, args ->
            for (item in args.items) {
                removeItem(0)
            }
        })

        viewModel.loadingOrders.observe(viewLifecycleOwner, {_:Boolean?, value: Boolean? ->
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
            viewModel.loadAllOrders(true)
            swipeRefreshLayout.isRefreshing = false
        }

        viewModel.loadAllOrders(false)
        initData()
        setHasOptionsMenu(true)
        return view
    }

    private fun initData() {
        val viewModel = (activity as MainActivity).ordersViewModel
        clearOrders()
        initAdapter()
        for (item in viewModel.allOrders)
            addItem(item, 0)

        if (viewModel.loadingOrders.value == null) return
        if (viewModel.loadingOrders.value!!) {
            shimmer.startShimmer()
            shimmer.visibility = View.VISIBLE
            allDeliveryRecyclerView.visibility = View.GONE
        } else {
            shimmer.stopShimmer()
            allDeliveryRecyclerView.visibility = View.VISIBLE
            shimmer.visibility = View.GONE
        }
    }

    private fun addItem(item: OrderViewData, position: Int) {
        ordersAdapter.data.add(position, item)
        ordersAdapter.notifyItemInserted(position)
    }

    private fun removeItem(position: Int){
        ordersAdapter.data.removeAt(position)
        ordersAdapter.notifyItemRemoved(position)
    }

    private fun initAdapter() {
        ordersAdapter = OrdersRecyclerViewAdapter(arrayListOf())
        ordersAdapter.setItemSelectedListener(object : IItemSelectedListener {
            override fun itemSelected(itemId: String) {
                val intent = Intent(context, ViewOrderActivity::class.java)
                intent.putExtra("orderId", itemId)
                startActivity(intent)
            }
        })
        allDeliveryRecyclerView.layoutManager = LinearLayoutManager(context)
        allDeliveryRecyclerView.adapter = ordersAdapter
    }

    private fun clearOrders() {
        allDeliveryRecyclerView.adapter = null
    }
}