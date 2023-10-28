package com.fairsoft.telecaller.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.fairsoft.telecaller.R
import com.fairsoft.telecaller.databinding.FragmentLoginBinding
import com.fairsoft.telecaller.utils.hideSoftKeyboard
import com.fairsoft.telecaller.viewmodel.LoginViewModel

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val shakeAnim by lazy {
        AnimationUtils.loadAnimation(requireContext(), R.anim.shake_anim)
    }

    private var username = ""
    private var password = ""
    private var selectedCompany = 1

    private val loginViewModel: LoginViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.pale_white)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginBtn.setOnClickListener { validateLogin() }

        binding.chooseCompanyRg.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.fair_soft_co) {
                selectedCompany = 0
            } else if (checkedId == R.id.book_xpert_co) {
                selectedCompany = 1
            }
        }

        val versionName = requireActivity().packageManager.getPackageInfo(requireActivity().packageName, 0).versionName
        binding.versionName.text = "v$versionName"

        loginViewModel.loginStatus.observe(this.viewLifecycleOwner) {status ->
            if (status == "Success") {
                val action = LoginFragmentDirections.actionLoginFragmentToDashboardFragment()
                this@LoginFragment.findNavController().navigate(action)
            }
        }
    }

    private fun validateLogin() {

        hideSoftKeyboard(requireActivity())

        username = binding.username.text.toString().trim()
        password = binding.password.text.toString()

        if (loginViewModel.isLoginValid(requireContext(), username, password)) {
            loginViewModel.verifyLogin(requireActivity(), requireContext(), username, password, selectedCompany)
        } else {
            binding.loginBtn.startAnimation(shakeAnim)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}