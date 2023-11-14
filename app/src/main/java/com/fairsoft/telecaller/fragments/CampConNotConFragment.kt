package com.fairsoft.telecaller.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.fairsoft.telecaller.R
import com.fairsoft.telecaller.adapters.CampConNotConListAdapter
import com.fairsoft.telecaller.databinding.FragmentCampConNotConBinding
import com.fairsoft.telecaller.utils.hideSoftKeyboard
import com.fairsoft.telecaller.viewmodel.AppViewModel

class CampConNotConFragment : Fragment() {

    private var _binding: FragmentCampConNotConBinding? = null
    private val binding get() = _binding!!

    private val navArgs: CampConNotConFragmentArgs by navArgs()
    private val appViewModel: AppViewModel by activityViewModels()
    private lateinit var campConNotConListAdapter: CampConNotConListAdapter
    private val refreshAnim: Animation by lazy {
        AnimationUtils.loadAnimation(requireContext(), R.anim.refresh_anim)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCampConNotConBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fragmentLabel.text = navArgs.campaignName

        if (navArgs.clickType == "CampConById") {
            binding.callType.text = resources.getString(R.string.con_calls)
            binding.callType.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))

            appViewModel.getCampConByIdList(requireActivity(), navArgs.campaignId)
            appViewModel.campConByIdList.observe(this.viewLifecycleOwner) { list ->
                if (list.isNotEmpty()) {
                    binding.noRecFound.visibility = View.INVISIBLE
                    binding.recyclerViewConNotCon.visibility = View.VISIBLE
                    campConNotConListAdapter = CampConNotConListAdapter(list)
                    binding.recyclerViewConNotCon.adapter = campConNotConListAdapter
                } else {
                    binding.noRecFound.visibility = View.VISIBLE
                    binding.recyclerViewConNotCon.visibility = View.INVISIBLE
                }
            }
        } else if (navArgs.clickType == "CampNotConById") {
            binding.callType.text = resources.getString(R.string.not_con_calls)
            binding.callType.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))

            appViewModel.getCampNotConByIdList(requireActivity(), navArgs.campaignId)

            appViewModel.campNotConByIdList.observe(this.viewLifecycleOwner) { list ->
                if (list.isNotEmpty()) {
                    binding.noRecFound.visibility = View.INVISIBLE
                    binding.recyclerViewConNotCon.visibility = View.VISIBLE
                    campConNotConListAdapter = CampConNotConListAdapter(list)
                    binding.recyclerViewConNotCon.adapter = campConNotConListAdapter
                } else {
                    binding.noRecFound.visibility = View.VISIBLE
                    binding.recyclerViewConNotCon.visibility = View.INVISIBLE
                }
            }
        }

        binding.searchBar.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                hideSoftKeyboard(requireActivity())
                campConNotConListAdapter.filter.filter(query)
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                campConNotConListAdapter.filter.filter(query)
                return true
            }
        })

        binding.refreshButton.setOnClickListener {
            binding.refreshButton.startAnimation(refreshAnim)
            if (navArgs.clickType == "CampConById") {
                appViewModel.getCampConByIdList(requireActivity(), navArgs.campaignId)
            } else if (navArgs.clickType == "CampNotConById") {
                appViewModel.getCampNotConByIdList(requireActivity(), navArgs.campaignId)
            }
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            if (navArgs.clickType == "CampConById") {
                appViewModel.getCampConByIdList(requireActivity(), navArgs.campaignId)
            } else if (navArgs.clickType == "CampNotConById") {
                appViewModel.getCampNotConByIdList(requireActivity(), navArgs.campaignId)
            }
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}