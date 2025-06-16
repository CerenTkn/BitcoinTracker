package com.cerentekin.bitcointracker.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cerentekin.bitcointracker.R
import com.cerentekin.bitcointracker.VM.CoinVM
import com.cerentekin.bitcointracker.data.model.Coin
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
    private var fullCoinList: List<Coin> = emptyList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val toolbar = binding.toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Bitcoin Tracker"
        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.orange)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        binding.rvCoins.layoutManager = LinearLayoutManager(requireContext())

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().trim().lowercase()
                val filteredList = fullCoinList.filter {
                    it.name.lowercase().contains(query) || it.symbol.lowercase().contains(query)
                }
                updateCoinList(filteredList)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        viewModel.fetchCoins()

        viewModel.coinList.observe(viewLifecycleOwner) { coins ->
            fullCoinList = coins // Listeyi kaydet
            updateCoinList(coins) // İlk yükleme
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMsg ->
            errorMsg?.let {
                Toast.makeText(requireContext(), "Error: $it", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateCoinList(coins: List<Coin>) {
        binding.rvCoins.adapter = CoinAdapter(coins) { selectedCoin ->
            val action = HomeFragmentDirections.actionHomeFragmentToCoinDetailFragment(selectedCoin)
            findNavController().navigate(action)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Geri tuşu davranışı
                Log.d("HomeFragment","backbutton tıklandı")
                findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
                true
            }
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
        binding.etSearch.setText("")
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (requireActivity() as AppCompatActivity).setSupportActionBar(null)
        _binding = null
    }
}
