package com.fairsoft.telecaller.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.fairsoft.telecaller.databinding.ListItemClBinding
import com.fairsoft.telecaller.model.Campaign

class CampaignListAdapter(
    private val campList: List<Campaign>,
    private val onItemClicked: (clickType: String, Campaign) -> Unit
): RecyclerView.Adapter<CampaignListAdapter.ViewHolder>(), Filterable {

    private var filteredList = ArrayList<Campaign>()

    init {
        filteredList = campList as ArrayList<Campaign>
    }

    inner class ViewHolder(
        private val binding: ListItemClBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(camp: Campaign) {
            binding.campName.text = camp.campaignName
            binding.totalCalls.text = camp.totalNoOfCustomer.toString()
            //binding.totalCalls.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            binding.conCalls.text = camp.connectedCustomers.toString()
            binding.pendCalls.text = camp.openCustomer.toString()
            //binding.pendCalls.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            binding.notConCalls.text = camp.notConnectedCustomers.toString()
            //binding.notConCalls.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            binding.totalCalls.setOnClickListener { onItemClicked("CampById", camp) }
            binding.conCalls.setOnClickListener { onItemClicked("CampConById", camp) }
            binding.notConCalls.setOnClickListener { onItemClicked("CampNotConById", camp) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemClBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val camp = filteredList[position]
        holder.bind(camp)
    }

    override fun getFilter(): Filter {
        return object: Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                filteredList = if (charSearch.isEmpty()) {
                    campList as ArrayList<Campaign>
                } else {
                    val resultList = ArrayList<Campaign>()
                    for(camp in campList) {
                        if (camp.campaignName.lowercase().contains(constraint.toString().lowercase())) {
                            resultList.add(camp)
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList = results?.values as ArrayList<Campaign>
                notifyDataSetChanged()
            }
        }
    }
}