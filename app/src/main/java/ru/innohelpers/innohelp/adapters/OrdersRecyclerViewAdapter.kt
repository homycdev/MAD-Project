package ru.innohelpers.innohelp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import ru.innohelpers.innohelp.R
import ru.innohelpers.innohelp.adapters.listeners.IItemSelectedListener
import ru.innohelpers.innohelp.view_data.order.OrderViewData

class OrdersRecyclerViewAdapter(var data: ArrayList<OrderViewData>) : RecyclerView.Adapter<OrdersRecyclerViewAdapter.OrdersRecyclerViewViewHolder>() {

    private var selectedListener: IItemSelectedListener? = null

    fun setItemSelectedListener(listener: IItemSelectedListener) {
        selectedListener = listener
    }

    class OrdersRecyclerViewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var titleTextView: TextView = itemView.findViewById(R.id.order_recycler_view_item_title)
        private var ownerTextView: TextView = itemView.findViewById(R.id.order_recycler_view_item_owner)
        private var descriptionTextView: TextView = itemView.findViewById(R.id.order_recycler_view_item_description)
        private var container: CardView = itemView.findViewById(R.id.order_recycler_view_item_container)
        private var selectedListener: IItemSelectedListener? = null

        fun bindData(order: OrderViewData, selectedListener: IItemSelectedListener?) {
            titleTextView.text = order.title
            descriptionTextView.text = order.description
            ownerTextView.text = order.creator.userName
            container.setOnClickListener {
                selectedListener?.itemSelected(order.id)
            }
            this.selectedListener = selectedListener
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersRecyclerViewViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return OrdersRecyclerViewViewHolder(inflater.inflate(R.layout.order_recycler_view_item, parent, false))
    }

    override fun onBindViewHolder(holder: OrdersRecyclerViewViewHolder, position: Int) {
        holder.bindData(data[position], selectedListener)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}