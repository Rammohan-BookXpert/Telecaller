package com.fairsoft.telecaller.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.fairsoft.telecaller.databinding.FragmentCampaignByIdBinding
import com.fairsoft.telecaller.viewmodel.AppViewModel

class CampaignByIdFragment : Fragment() {

    private var _binding: FragmentCampaignByIdBinding? = null
    private val binding get() = _binding!!

    private val appViewModel: AppViewModel by activityViewModels()
    private val navArgs: CampaignByIdFragmentArgs by navArgs()

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
        appViewModel.getCampaignById(requireActivity(), navArgs.campaignId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}