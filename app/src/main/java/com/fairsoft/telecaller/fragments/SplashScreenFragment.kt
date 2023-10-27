package com.fairsoft.telecaller.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.fairsoft.telecaller.databinding.FragmentSplashScreenBinding
import com.fairsoft.telecaller.datastore.LoggedUserDataStore

@SuppressLint("CustomSplashScreen")
class SplashScreenFragment : Fragment() {

    private var _binding: FragmentSplashScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().window.statusBarColor = Color.WHITE
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var isUserLogged = false

        val loggedDataStore = LoggedUserDataStore(requireContext())
        loggedDataStore.isUserLogged.asLiveData().observe(this.viewLifecycleOwner) {value ->
            isUserLogged = value
        }

        Thread {
            Thread.sleep(1000)
            requireActivity().runOnUiThread {
                binding.checkLoginMessage.visibility = View.VISIBLE
                binding.loadingAnim.visibility = View.VISIBLE
            }

            Thread.sleep(2000)
            requireActivity().runOnUiThread {
                if (!isUserLogged) {
                    val action = SplashScreenFragmentDirections.actionSplashScreenFragmentToLoginFragment()
                    this.findNavController().navigate(action)
                } else {
                    val action = SplashScreenFragmentDirections.actionSplashScreenFragmentToDashboardFragment()
                    this.findNavController().navigate(action)
                }
            }
        }.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}