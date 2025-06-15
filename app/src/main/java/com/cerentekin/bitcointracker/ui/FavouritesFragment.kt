package com.cerentekin.bitcointracker.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cerentekin.bitcointracker.VM.FavouritesVM
import com.cerentekin.bitcointracker.databinding.FragmentFavouritesBinding
import com.cerentekin.bitcointracker.ui.adapter.CoinAdapter

class FavouritesFragment : Fragment() {

    private var _binding: FragmentFavouritesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavouritesVM by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouritesBinding.inflate(inflater, container, false)

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
        binding.rvFavourites.layoutManager = LinearLayoutManager(requireContext())

        viewModel.favouriteCoins.observe(viewLifecycleOwner) { coinList ->
            binding.rvFavourites.adapter = CoinAdapter(coinList) { selectedCoin ->
                val action = FavouritesFragmentDirections
                    .actionFavouritesFragmentToCoinDetailFragment(selectedCoin)
                findNavController().navigate(action)
            }
        }

        viewModel.fetchFavourites()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (requireActivity() as AppCompatActivity).setSupportActionBar(null)
        _binding = null
    }
}
