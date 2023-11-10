package com.fairsoft.telecaller.fragments

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.fairsoft.telecaller.R
import com.fairsoft.telecaller.databinding.FragmentDashboardBinding
import com.fairsoft.telecaller.datastore.AppDataStore
import kotlinx.coroutines.launch

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
                binding.companyName.text = resources.getString(R.string.comp_0)
            }
        }

        binding.logoutBtn.setOnClickListener { showLogoutDialog() }
    }

    // Displays the Logout dialog
    private fun showLogoutDialog() {
        val dialogView = requireActivity().layoutInflater.inflate(R.layout.custom_logout_dialog, null)
        val yesTv = dialogView.findViewById<TextView>(R.id.yes_tv)
        val noTv = dialogView.findViewById<TextView>(R.id.no_tv)

        val builder = AlertDialog.Builder(requireContext())
        builder.setView(dialogView)
        builder.setCancelable(true)

        val logoutDialog = builder.create()
        logoutDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        logoutDialog.show()

        yesTv.setOnClickListener {
            logOut()
            logoutDialog.dismiss()
        }

        noTv.setOnClickListener {
            logoutDialog.dismiss()
        }
    }

    /*
    * On Logout, it will clear all the preferences stored, and
    * Restarts the app as new
    */
    private fun logOut() {
        lifecycleScope.launch {
            appDataStore.clearAllPreferences(requireContext())
            val context = requireActivity().applicationContext
            val pm = context.packageManager
            val intent = pm.getLaunchIntentForPackage(context.packageName)
            val mainIntent = Intent.makeRestartActivityTask(intent!!.component)
            context.startActivity(mainIntent)
            Runtime.getRuntime().exit(0)
        }
        Toast.makeText(requireContext(), "Account Logged Out...", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}