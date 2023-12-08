package com.fairsoft.telecaller.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.fairsoft.telecaller.R
import com.fairsoft.telecaller.databinding.FragmentReportStatusBinding
import com.fairsoft.telecaller.utils.currentDay
import com.fairsoft.telecaller.utils.currentMonth
import com.fairsoft.telecaller.utils.currentYear
import com.fairsoft.telecaller.utils.getFormattedDate
import com.fairsoft.telecaller.utils.sdf
import com.fairsoft.telecaller.utils.toServerDate
import com.fairsoft.telecaller.viewmodel.AppViewModel

class ReportStatusFragment : Fragment() {

    private var _binding: FragmentReportStatusBinding? = null
    private val binding get() = _binding!!

    private val appViewModel: AppViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportStatusBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backIcon.setOnClickListener { this.findNavController().navigateUp() }

        binding.fromDate.text = appViewModel.fromDate
        binding.toDate.text = appViewModel.toDate
        getData()

        binding.fromDate.setOnClickListener { showDatePickerDialog("From") }
        binding.toDate.setOnClickListener { showDatePickerDialog("To") }

        appViewModel.userSummary.observe(this.viewLifecycleOwner) {
            binding.username.text = it.username
            binding.totalCalls.text = it.totalCalls.toString()
            binding.conCalls.text = it.connected.toString()
            binding.notConCalls.text = it.notConnected.toString()
            binding.totalEnq.text = it.noOfEnquiries.toString()
        }

        binding.getDataBtn.setOnClickListener { getData() }
    }

    private fun getData() {
        val fromDate = binding.fromDate.text.toString()
        val toDate = binding.toDate.text.toString()

        if(sdf.parse(fromDate)!!.before(sdf.parse(toDate)) || sdf.parse(fromDate)!! == sdf.parse(toDate)) {
            appViewModel.getUserSummaryByDates(requireActivity(), toServerDate(fromDate), toServerDate(toDate))
        } else Toast.makeText(requireContext(), "Please select correct dates", Toast.LENGTH_SHORT).show()
    }

    private fun showDatePickerDialog(dateType: String) {
        when(dateType) {
            "From" -> {
                val datePickerDialog = DatePickerDialog(requireContext(), R.style.datePicker, { _, sYear, sMonth, sDay ->
                    binding.fromDate.text = getFormattedDate(sDay, sMonth, sYear)
                    appViewModel.fromDate = getFormattedDate(sDay, sMonth, sYear)
                }, currentYear, currentMonth, currentDay)
                datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
                datePickerDialog.show()
            }
            "To" -> {
                val datePickerDialog = DatePickerDialog(requireContext(), R.style.datePicker, { _, sYear, sMonth, sDay ->
                    binding.toDate.text = getFormattedDate(sDay, sMonth, sYear)
                    appViewModel.toDate = getFormattedDate(sDay, sMonth, sYear)
                }, currentYear, currentMonth, currentDay)
                datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
                datePickerDialog.show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}