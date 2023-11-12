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
import androidx.navigation.fragment.findNavController
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

        binding.backIcon.setOnClickListener { this.findNavController().navigateUp() }

        appViewModel.campaignsList.observe(this.viewLifecycleOwner) { list ->
            if (list.isNotEmpty()) {
                binding.noRecFound.visibility = View.INVISIBLE
                binding.recyclerViewCl.visibility = View.VISIBLE
                campListAdapter = CampaignListAdapter(list) {
                    val action = CampaignsFragmentDirections
                        .actionCampaignsFragmentToCampaignByIdFragment(it.campaignId, it.campaignName)
                    this.findNavController().navigate(action)
                }
                binding.recyclerViewCl.adapter = campListAdapter
            } else {
                binding.noRecFound.visibility = View.VISIBLE
                binding.recyclerViewCl.visibility = View.INVISIBLE
            }
        }

        binding.searchBar.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                hideSoftKeyboard(requireActivity())
                campListAdapter.filter.filter(query)
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

        binding.swipeRefreshLayout.setOnRefreshListener {
            appViewModel.getCampaignsList(requireActivity())
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}