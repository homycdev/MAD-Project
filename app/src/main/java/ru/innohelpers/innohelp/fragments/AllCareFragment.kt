package ru.innohelpers.innohelp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.facebook.shimmer.ShimmerFrameLayout
import ru.innohelpers.innohelp.R
import ru.innohelpers.innohelp.activity.MainActivity
import ru.innohelpers.innohelp.activity.NewCareActivity
import ru.innohelpers.innohelp.activity.ViewCareActivity
import ru.innohelpers.innohelp.adapters.CareRecyclerViewAdapter
import ru.innohelpers.innohelp.adapters.listeners.IItemSelectedListener
import ru.innohelpers.innohelp.view_data.care.CareViewData

class AllCareFragment : MainFragmentBase() {

    private lateinit var careAdapter: CareRecyclerViewAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var allCareRecyclerView: RecyclerView
    private lateinit var shimmer: ShimmerFrameLayout

    override fun newItemActivity(): Class<*> {
        return NewCareActivity::class.java
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.fragment_all_care, container, false)

        allCareRecyclerView = view.findViewById(R.id.all_care_recycler_view)
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh)
        shimmer = view.findViewById(R.id.all_care_shimmer)

        clearOrders()
        initAdapter()

        val viewModel = (activity as MainActivity).careViewModel

        viewModel.allCares.observe(viewLifecycleOwner, itemsInsertedListener = {collection, args ->
            for (itemPosition in args.startPosition until args.startPosition + args.count)
                addItem(collection[itemPosition], itemPosition)
        }, itemsRemovedListener = {_, args ->
            for (item in args.items) {
                removeItem(0)
            }
        })

        viewModel.loadingCares.observe(viewLifecycleOwner, {_:Boolean?, value: Boolean? ->
            if (value == null) return@observe
            if (!value) {
                shimmer.stopShimmer()
                allCareRecyclerView.visibility = View.VISIBLE
                shimmer.visibility = View.GONE
            }
            else {
                shimmer.startShimmer()
                shimmer.visibility = View.VISIBLE
                allCareRecyclerView.visibility = View.GONE
            }
        })

        swipeRefreshLayout.setOnRefreshListener {
            viewModel.loadAllCares(true)
            swipeRefreshLayout.isRefreshing = false
        }

        initData()
        viewModel.loadAllCares(false)
        setHasOptionsMenu(true)

        return view
    }

    private fun initData() {
        val viewModel = (activity as MainActivity).careViewModel
        clearOrders()
        initAdapter()
        for (item in viewModel.allCares)
            addItem(item, 0)

        if (viewModel.loadingCares.value == null) return
        if (viewModel.loadingCares.value!!) {
            shimmer.startShimmer()
            shimmer.visibility = View.VISIBLE
            allCareRecyclerView.visibility = View.GONE
        } else {
            shimmer.stopShimmer()
            allCareRecyclerView.visibility = View.VISIBLE
            shimmer.visibility = View.GONE
        }
    }

    private fun addItem(item: CareViewData, position: Int) {
        careAdapter.data.add(position, item)
        careAdapter.notifyItemInserted(position)
    }

    private fun removeItem(position: Int){
        careAdapter.data.removeAt(position)
        careAdapter.notifyItemRemoved(position)
    }

    private fun initAdapter() {
        careAdapter = CareRecyclerViewAdapter(arrayListOf())
        careAdapter.setOnItemSelectedListener(object : IItemSelectedListener {
            override fun itemSelected(itemId: String) {
                val intent = Intent(context, ViewCareActivity::class.java)
                intent.putExtra("careId", itemId)
                startActivity(intent)
            }
        })
        allCareRecyclerView.layoutManager = LinearLayoutManager(context)
        allCareRecyclerView.adapter = careAdapter
    }

    private fun clearOrders() {
        allCareRecyclerView.adapter = null
    }

}