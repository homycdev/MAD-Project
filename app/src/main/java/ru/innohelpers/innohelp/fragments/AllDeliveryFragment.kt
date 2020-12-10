package ru.innohelpers.innohelp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import ru.innohelpers.innohelp.R

class AllDeliveryFragment : MainFragmentBase() {

    private lateinit var allDeliveryRecyclerView: RecyclerView
    private lateinit var shimmer: ShimmerFrameLayout

    override fun newItemActivity(): Class<*> {
        TODO()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_all_delivery, container, false)

        allDeliveryRecyclerView = view.findViewById(R.id.all_delivery_recycler_view)
        shimmer = view.findViewById(R.id.all_delivery_shimmer)
        return view
    }

}