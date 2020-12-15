package ru.innohelpers.innohelp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import ru.innohelpers.innohelp.R
import ru.innohelpers.innohelp.adapters.listeners.IItemSelectedListener
import ru.innohelpers.innohelp.view_data.delivery.DeliveryViewData

class DeliveryRecyclerViewAdapter(var data: ArrayList<DeliveryViewData>) : RecyclerView.Adapter<DeliveryRecyclerViewAdapter.DeliveryRecyclerViewViewHolder>() {

    class DeliveryRecyclerViewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var titleTextView: TextView = itemView.findViewById(R.id.delivery_recycler_view_item_title)
        private var ownerTextView: TextView = itemView.findViewById(R.id.delivery_recycler_view_item_owner)
        private var locationTextView: TextView = itemView.findViewById(R.id.delivery_recycler_view_item_location)
        private var costTextView: TextView = itemView.findViewById(R.id.delivery_recycler_view_item_cost)
        private var benefitTextView: TextView = itemView.findViewById(R.id.delivery_recycler_view_item_benefit)

        private var container: CardView = itemView.findViewById(R.id.delivery_recycler_view_item_container)
        private var selectedListener: IItemSelectedListener? = null

        fun bindData(delivery: DeliveryViewData, selectedListener: IItemSelectedListener?) {
            titleTextView.text = delivery.title
            locationTextView.text = delivery.location
            ownerTextView.text = delivery.creator.userName
            costTextView.text = delivery.totalCost.toString()
            benefitTextView.text = delivery.benefit.toString()
            container.setOnClickListener {
                selectedListener?.itemSelected(delivery.id)
            }
            this.selectedListener = selectedListener
        }

    }

    private var selectedListener: IItemSelectedListener? = null

    fun setItemSelectedListener(listener: IItemSelectedListener) {
        selectedListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveryRecyclerViewViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return DeliveryRecyclerViewViewHolder(inflater.inflate(R.layout.delivery_recycler_view_item, parent, false))
    }

    override fun onBindViewHolder(holder: DeliveryRecyclerViewViewHolder, position: Int) {
        holder.bindData(data[position], selectedListener)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}