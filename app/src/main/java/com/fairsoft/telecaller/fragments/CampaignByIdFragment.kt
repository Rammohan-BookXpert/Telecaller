package com.fairsoft.telecaller.fragments

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.fairsoft.telecaller.R
import com.fairsoft.telecaller.adapters.CampaignDetailListAdapter
import com.fairsoft.telecaller.databinding.CustomCallDialogBinding
import com.fairsoft.telecaller.databinding.FragmentCampaignByIdBinding
import com.fairsoft.telecaller.utils.PermissionCodes.CALL_PERMISSION_CODE
import com.fairsoft.telecaller.utils.TAG
import com.fairsoft.telecaller.utils.hideSoftKeyboard
import com.fairsoft.telecaller.viewmodel.AppViewModel
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog

class CampaignByIdFragment : Fragment(), EasyPermissions.PermissionCallbacks {

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
                    showContactHistoryDialog(it.mobileNumber)
                    appViewModel.getContactHistory(requireActivity(), it.mobileNumber)
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

    private fun showContactHistoryDialog(phoneNum: String) {
        val binding = CustomCallDialogBinding.inflate(LayoutInflater.from(requireActivity()))
        val dialog = AlertDialog.Builder(requireActivity())
            .setView(binding.root)
            .setCancelable(true)
            .create()

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        binding.phoneNum.text = phoneNum

        appViewModel.contactHistory.observe(this.viewLifecycleOwner) { list ->
            if (list.isNotEmpty()) {
                binding.noHistoryTv.visibility = View.INVISIBLE
                binding.historyCl.visibility = View.VISIBLE
                val lastCall = list[list.size - 1]
                binding.followUp.text = lastCall.followUpBy
                binding.remarks.text = lastCall.remarks
                binding.date.text = lastCall.fEnqDate.replace("T", " ")
            } else {
                binding.noHistoryTv.visibility = View.VISIBLE
                binding.historyCl.visibility = View.INVISIBLE
            }
        }

        binding.noTv.setOnClickListener { dialog.dismiss() }
        binding.yesTv.setOnClickListener {
            dialog.dismiss()
            if (hasCallPermission()) {
                makeCall(phoneNum)
            } else requestCallPermission()
        }
    }

    private fun makeCall(phoneNum: String) {
        showSimSelectionDialog()
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:$phoneNum")
        requireActivity().startActivity(intent)
    }

    private fun showSimSelectionDialog() {

    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.permissionPermanentlyDenied(this, perms.first())) {
            SettingsDialog.Builder(requireContext()).build().show()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Log.i(TAG, "CampaignByIdFragment -> onPermissionsGranted -> Call Permission is granted")
    }

    private fun hasCallPermission() =
        EasyPermissions.hasPermissions(requireContext(), Manifest.permission.CALL_PHONE)

    private fun requestCallPermission() {
        EasyPermissions.requestPermissions(
            this,
            "Permission is required to make call",
            CALL_PERMISSION_CODE,
            Manifest.permission.CALL_PHONE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        //EasyPermissions handles the request result
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}