package com.cerentekin.bitcointracker.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.cerentekin.bitcointracker.R
import com.cerentekin.bitcointracker.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.tvWelcome.text = "Hoş geldin, Bitcoin takip ekranına yönlendirildin!"
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                AlertDialog.Builder(requireContext())
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to log out?")
                    .setPositiveButton("Yes") { _, _ ->
                        firebaseAuth.signOut()

                        val navOptions = NavOptions.Builder()
                            .setPopUpTo(R.id.homeFragment, inclusive = true)
                            .build()

                        findNavController().navigate(
                            R.id.loginFragment,
                            null,
                            navOptions
                        )
                    }
                    .setNegativeButton("Cancel", null)
                    .show()

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
