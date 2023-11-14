package com.fairsoft.telecaller.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.fairsoft.telecaller.databinding.ListItemConNotConBinding
import com.fairsoft.telecaller.model.CampConNotCon

class CampConNotConListAdapter(
    private val campConNotConList: List<CampConNotCon>,
    //private val onItemClicked: (CampConNotCon) -> Unit
): RecyclerView.Adapter<CampConNotConListAdapter.ViewHolder>(), Filterable {

    private var filteredList = ArrayList<CampConNotCon>()

    init {
        filteredList = campConNotConList as ArrayList<CampConNotCon>
    }

    inner class ViewHolder(
        private val binding: ListItemConNotConBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(campConNotCon: CampConNotCon) {
            binding.compName.text = campConNotCon.companyName
            binding.phoneNum.text = campConNotCon.mobileNumber
            binding.prodName.text = campConNotCon.product
            binding.dealingProdName.text = campConNotCon.dealingProduct
            binding.reason.text = campConNotCon.reason
            binding.remarks.text = campConNotCon.remarks
            if (campConNotCon.hasHistory == 1) {
                binding.historyIcon.visibility = View.VISIBLE
            } else binding.historyIcon.visibility = View.INVISIBLE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemConNotConBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val campConNotCon = filteredList[position]
        holder.bind(campConNotCon)
    }

    override fun getFilter(): Filter {
        return object: Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                filteredList = if (charSearch.isEmpty()) {
                    campConNotConList as ArrayList<CampConNotCon>
                } else {
                    val resultList = ArrayList<CampConNotCon>()
                    for(campNcc in campConNotConList) {
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
                filteredList = results?.values as ArrayList<CampConNotCon>
                notifyDataSetChanged()
            }
        }
    }
}