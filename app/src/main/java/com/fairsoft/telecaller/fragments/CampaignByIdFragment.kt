package com.fairsoft.telecaller.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.fairsoft.telecaller.R
import com.fairsoft.telecaller.adapters.CampaignDetailListAdapter
import com.fairsoft.telecaller.databinding.FragmentCampaignByIdBinding
import com.fairsoft.telecaller.utils.hideSoftKeyboard
import com.fairsoft.telecaller.viewmodel.AppViewModel

class CampaignByIdFragment : Fragment() {

    private var _binding: FragmentCampaignByIdBinding? = null
    private val binding get() = _binding!!

    private val appViewModel: AppViewModel by activityViewModels()
    private val navArgs: CampaignByIdFragmentArgs by navArgs()
    private lateinit var campDetailListAdapter: CampaignDetailListAdapter
    private val refreshAnim: Animation by lazy {
        AnimationUtils.loadAnimation(requireContext(), R.anim.refresh_anim)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCampaignByIdBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backIcon.setOnClickListener { this.findNavController().navigateUp() }

        binding.fragmentLabel.text = navArgs.campaignName

        appViewModel.getCampaignById(requireActivity(), navArgs.campaignId)

        appViewModel.campaignByIdList.observe(this.viewLifecycleOwner) { list ->
            if (list.isNotEmpty()) {
                binding.noRecFound.visibility = View.INVISIBLE
                binding.recyclerViewClById.visibility = View.VISIBLE
                campDetailListAdapter = CampaignDetailListAdapter(list) {
                    Toast.makeText(requireContext(), "Item clicked...", Toast.LENGTH_SHORT).show()
                }
                binding.recyclerViewClById.adapter = campDetailListAdapter
            } else {
                binding.noRecFound.visibility = View.VISIBLE
                binding.recyclerViewClById.visibility = View.INVISIBLE
            }
        }

        binding.searchBar.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                hideSoftKeyboard(requireActivity())
                campDetailListAdapter.filter.filter(query)
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                campDetailListAdapter.filter.filter(query)
                return true
            }
        })


        binding.refreshButton.setOnClickListener {
            binding.refreshButton.startAnimation(refreshAnim)
            appViewModel.getCampaignById(requireActivity(), navArgs.campaignId)
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            appViewModel.getCampaignById(requireActivity(), navArgs.campaignId)
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}