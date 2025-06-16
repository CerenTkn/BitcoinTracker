package com.cerentekin.bitcointracker.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.cerentekin.bitcointracker.R
import com.cerentekin.bitcointracker.VM.LoginVM
import com.cerentekin.bitcointracker.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var binding: FragmentLoginBinding? = null

    private val viewModel: LoginVM by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), android.R.color.white)

        with(binding) {
            this?.btnLogin?.setOnClickListener {
                val email = binding!!.etEmail.text.toString()
                val password = binding!!.etPassword.text.toString()
                if (email.isBlank() || password.isBlank()) {
                    Toast.makeText(requireContext(), "Email and password can't be empty", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                viewModel.login(email, password)
            }
            this?.btnRegister?.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }

        }
        observeLoginResult()

        return binding!!.root
    }

    private fun observeLoginResult() {
        viewModel.loginResult.observe(viewLifecycleOwner) { success ->
            if (success) {
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            } else {
                Toast.makeText(requireContext(), "Hatalı giriş", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
