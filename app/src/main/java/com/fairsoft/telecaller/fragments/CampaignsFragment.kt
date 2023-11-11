package com.fairsoft.telecaller.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.fairsoft.telecaller.R
import com.fairsoft.telecaller.adapters.CampaignListAdapter
import com.fairsoft.telecaller.databinding.FragmentCampaignsBinding
import com.fairsoft.telecaller.utils.hideSoftKeyboard
import com.fairsoft.telecaller.viewmodel.AppViewModel

class CampaignsFragment : Fragment() {

    private var _binding: FragmentCampaignsBinding? = null
    private val binding get() = _binding!!

    private val appViewModel: AppViewModel by activityViewModels()
    private lateinit var campListAdapter: CampaignListAdapter
    private val refreshAnim: Animation by lazy {
        AnimationUtils.loadAnimation(requireContext(), R.anim.refresh_anim)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCampaignsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appViewModel.campaignsList.observe(this.viewLifecycleOwner) { list ->
            if (list.isNotEmpty()) {
                campListAdapter = CampaignListAdapter(list)
                binding.recyclerViewCl.adapter = campListAdapter
            }
        }

        binding.searchBar.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                hideSoftKeyboard(requireActivity())
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                campListAdapter.filter.filter(query)
                return true
            }
        })

        binding.refreshButton.setOnClickListener {
            binding.refreshButton.startAnimation(refreshAnim)
            appViewModel.getCampaignsList(requireActivity())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}