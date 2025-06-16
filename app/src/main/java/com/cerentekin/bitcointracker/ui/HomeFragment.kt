package com.cerentekin.bitcointracker.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cerentekin.bitcointracker.R
import com.cerentekin.bitcointracker.VM.CoinVM
import com.cerentekin.bitcointracker.databinding.FragmentHomeBinding
import com.cerentekin.bitcointracker.ui.adapter.CoinAdapter
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val viewModel: CoinVM by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val toolbar = binding.toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Bitcoin Tracker"

        toolbar.setNavigationOnClickListener {
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.homeFragment, inclusive = true) // Home'u siler
                .build()

            findNavController().navigate(
                R.id.loginFragment,
                null,
                navOptions
            )
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)


        binding.rvCoins.layoutManager = LinearLayoutManager(requireContext())

        viewModel.fetchCoins()

        viewModel.coinList.observe(viewLifecycleOwner) { coins ->
            binding.rvCoins.adapter = CoinAdapter(coins) { selectedCoin ->
                val action = HomeFragmentDirections.actionHomeFragmentToCoinDetailFragment(selectedCoin)
                findNavController().navigate(action)
            }
        }


        viewModel.error.observe(viewLifecycleOwner) { errorMsg ->
            errorMsg?.let {
                Toast.makeText(requireContext(), "Error: $it", Toast.LENGTH_SHORT).show()
            }
        }
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
            R.id.action_favourites -> {
                findNavController().navigate(R.id.favouritesFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (requireActivity() as AppCompatActivity).setSupportActionBar(null)
        _binding = null
    }
}
