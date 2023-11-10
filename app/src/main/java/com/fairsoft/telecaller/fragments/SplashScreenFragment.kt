package com.fairsoft.telecaller.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.fairsoft.telecaller.R
import com.fairsoft.telecaller.databinding.FragmentSplashScreenBinding
import com.fairsoft.telecaller.datastore.AppDataStore
import com.fairsoft.telecaller.utils.TAG

class SplashScreenFragment : Fragment() {

    private var _binding: FragmentSplashScreenBinding? = null
    private val binding get() = _binding!!

    private lateinit var loggedDataStore: AppDataStore
    private var isUserLogged = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.pale_white)
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

        loggedDataStore = AppDataStore(requireActivity())
        loggedDataStore.loginStatus.asLiveData().observe(this.viewLifecycleOwner) { value ->
            Log.i(TAG, "SplashScreenFragment -> loginStatus -> $value")
            isUserLogged = value
        }
    }

    override fun onResume() {
        super.onResume()
        checkUserLogin()
    }

    private fun checkUserLogin() {

        Handler(Looper.getMainLooper()).postDelayed({
            binding.checkLoginMessage.visibility = View.VISIBLE
            binding.loadingAnim.visibility = View.VISIBLE
        }, 1000)

        Handler(Looper.getMainLooper()).postDelayed({
            if (isUserLogged) {
                val action =
                    SplashScreenFragmentDirections.actionSplashScreenFragmentToDashboardFragment()
                this@SplashScreenFragment.findNavController().navigate(action)
            } else {
                val action =
                    SplashScreenFragmentDirections.actionSplashScreenFragmentToLoginFragment()
                this@SplashScreenFragment.findNavController().navigate(action)
            }
        }, 2200)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}