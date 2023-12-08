package com.fairsoft.telecaller.fragments

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.fairsoft.telecaller.R
import com.fairsoft.telecaller.databinding.FragmentDashboardBinding
import com.fairsoft.telecaller.datastore.AppDataStore
import com.fairsoft.telecaller.utils.TAG
import com.fairsoft.telecaller.viewmodel.AppViewModel
import kotlinx.coroutines.launch

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private lateinit var appDataStore: AppDataStore
    private val appViewModel: AppViewModel by activityViewModels()

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
            if (value == 0) {
                binding.companyName.text = resources.getString(R.string.comp_0)
            } else if (value == 1) {
                binding.companyName.text = resources.getString(R.string.comp_1)
            }
            appViewModel.companyLogged = value
        }

        appDataStore.username.asLiveData().observe(this.viewLifecycleOwner) { value ->
            appViewModel.username = value
        }

        appDataStore.userId.asLiveData().observe(this.viewLifecycleOwner) { value ->
            appViewModel.userId = value
        }

        Handler(Looper.getMainLooper()).postDelayed({
            appViewModel.getCampaignsList(requireActivity())
            appViewModel.getCampNotConnectedCallsList(requireActivity())
        }, 1000)

        appViewModel.campaignsList.observe(this.viewLifecycleOwner) { list ->
            binding.csTv.text = HtmlCompat.fromHtml(resources.getString(R.string.campaigns_status, list.size), HtmlCompat.FROM_HTML_MODE_COMPACT)
        }

        appViewModel.notConnectedCallsList.observe(this.viewLifecycleOwner) { list ->
            binding.nccTv.text = HtmlCompat.fromHtml(resources.getString(R.string.not_connected_calls, list.size), HtmlCompat.FROM_HTML_MODE_COMPACT)
        }

        binding.logoutBtn.setOnClickListener { showLogoutDialog() }

        binding.csCard.setOnClickListener {
            val action = DashboardFragmentDirections.actionDashboardFragmentToCampaignsFragment()
            this.findNavController().navigate(action)
        }

        binding.nccCard.setOnClickListener {
            val action = DashboardFragmentDirections.actionDashboardFragmentToCampNotConCallsFragment()
            this.findNavController().navigate(action)
        }

        binding.rsCard.setOnClickListener {
            val action = DashboardFragmentDirections.actionDashboardFragmentToReportStatusFragment()
            this.findNavController().navigate(action)
        }

        binding.uploadExcelBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            intent.putExtra(Intent.EXTRA_MIME_TYPES, arrayListOf("application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
            excelPicker.launch(intent)
        }
    }

    private val excelPicker = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        Log.i(TAG, "excelPicker -> uri -> $it")
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