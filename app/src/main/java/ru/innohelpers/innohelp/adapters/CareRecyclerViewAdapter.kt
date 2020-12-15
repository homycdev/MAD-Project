package ru.innohelpers.innohelp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import ru.innohelpers.innohelp.R
import ru.innohelpers.innohelp.adapters.listeners.IItemSelectedListener
import ru.innohelpers.innohelp.view_data.care.CareViewData

class CareRecyclerViewAdapter(val data: ArrayList<CareViewData>) : RecyclerView.Adapter<CareRecyclerViewAdapter.CareRecyclerViewViewHolder>() {


    private var listener: IItemSelectedListener? = null

    fun setOnItemSelectedListener(listener: IItemSelectedListener) {
        this.listener = listener
    }

    class CareRecyclerViewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.care_recycler_view_item_title)
        private val ownerTextView: TextView = itemView.findViewById(R.id.care_recycler_view_item_owner)
        private val container: CardView = itemView.findViewById(R.id.care_recycler_view_item_container)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.care_recycler_view_item_description)
        private val benefitTextView: TextView = itemView.findViewById(R.id.care_recycler_view_item_benefit)

        fun bindData(viewData: CareViewData, listener: IItemSelectedListener?) {
            titleTextView.text = viewData.title
            ownerTextView.text = viewData.creator.userName
            descriptionTextView.text = viewData.description
            benefitTextView.text = viewData.benefit.toString()

            container.setOnClickListener { listener?.itemSelected(viewData.id) }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CareRecyclerViewViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CareRecyclerViewViewHolder(inflater.inflate(R.layout.care_recycler_view_item, parent, false))
    }

    override fun onBindViewHolder(holder: CareRecyclerViewViewHolder, position: Int) {
        holder.bindData(data[position],  listener)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}