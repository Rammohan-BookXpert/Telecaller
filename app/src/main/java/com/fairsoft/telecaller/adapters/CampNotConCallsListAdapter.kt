package com.fairsoft.telecaller.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.fairsoft.telecaller.databinding.ListItemNccBinding
import com.fairsoft.telecaller.model.CampNotConnected

class CampNotConCallsListAdapter(
    private val campNotConCallsList: List<CampNotConnected>,
    private val onItemClicked: (CampNotConnected) -> Unit
): RecyclerView.Adapter<CampNotConCallsListAdapter.ViewHolder>(), Filterable {

    private var filteredList = ArrayList<CampNotConnected>()

    init {
        filteredList = campNotConCallsList as ArrayList<CampNotConnected>
    }

    inner class ViewHolder(
        private val binding: ListItemNccBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(campNcc: CampNotConnected) {
            binding.compName.text = campNcc.companyName
            binding.phoneNum.text = campNcc.mobileNumber
            binding.phoneNum.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            binding.prodName.text = campNcc.product
            binding.dealingProdName.text = campNcc.dealingProduct
            binding.reason.text = campNcc.reason
            binding.remarks.text = campNcc.remarks
            binding.campName.text = campNcc.campaignName
            binding.phoneNum.setOnClickListener { onItemClicked(campNcc) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemNccBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val campNcc = filteredList[position]
        holder.bind(campNcc)
    }

    override fun getFilter(): Filter {
        return object: Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                filteredList = if (charSearch.isEmpty()) {
                    campNotConCallsList as ArrayList<CampNotConnected>
                } else {
                    val resultList = ArrayList<CampNotConnected>()
                    for(campNcc in campNotConCallsList) {
                        if (campNcc.companyName.lowercase().contains(constraint.toString().lowercase()) ||
                            campNcc.mobileNumber.lowercase().contains(constraint.toString().lowercase())) {
                            resultList.add(campNcc)
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList = results?.values as ArrayList<CampNotConnected>
                notifyDataSetChanged()
            }
        }
    }
}