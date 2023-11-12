package com.fairsoft.telecaller.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.fairsoft.telecaller.databinding.ListItemCByIdBinding
import com.fairsoft.telecaller.model.CampaignDetailed

class CampaignDetailListAdapter(
    private val campDetailList: List<CampaignDetailed>,
    private val onItemClicked: (CampaignDetailed) -> Unit
): RecyclerView.Adapter<CampaignDetailListAdapter.ViewHolder>(), Filterable {

    private var filteredList = ArrayList<CampaignDetailed>()

    init {
        filteredList = campDetailList as ArrayList<CampaignDetailed>
    }

    inner class ViewHolder(
        private val binding: ListItemCByIdBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(campDetail: CampaignDetailed) {
            binding.compName.text = campDetail.companyName
            binding.phoneNum.text = campDetail.mobileNumber
            binding.prodName.text = campDetail.product
            binding.dealingProdName.text = campDetail.dealingProduct
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemCByIdBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val camp = filteredList[position]
        holder.bind(camp)
        holder.itemView.setOnClickListener { onItemClicked(camp) }
    }

    override fun getFilter(): Filter {
        return object: Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                filteredList = if (charSearch.isEmpty()) {
                    campDetailList as ArrayList<CampaignDetailed>
                } else {
                    val resultList = ArrayList<CampaignDetailed>()
                    for(campD in campDetailList) {
                        if (campD.companyName.lowercase().contains(constraint.toString().lowercase()) ||
                            campD.mobileNumber.lowercase().contains(constraint.toString().lowercase())) {
                            resultList.add(campD)
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList = results?.values as ArrayList<CampaignDetailed>
                notifyDataSetChanged()
            }
        }
    }
}