package com.fairsoft.telecaller.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import com.fairsoft.telecaller.R
import com.fairsoft.telecaller.databinding.FragmentDashboardBinding
import com.fairsoft.telecaller.datastore.AppDataStore

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private lateinit var appDataStore: AppDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.blue)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appDataStore = AppDataStore(requireContext())
        appDataStore.username.asLiveData().observe(this.viewLifecycleOwner) { value ->
            binding.username.text = value
        }
        appDataStore.companyLogged.asLiveData().observe(this.viewLifecycleOwner) { value ->
            if (value == "0") {
                binding.companyName.text = resources.getString(R.string.comp_0)
            } else {
                binding.companyName.text = resources.getString(R.string.comp_1)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}