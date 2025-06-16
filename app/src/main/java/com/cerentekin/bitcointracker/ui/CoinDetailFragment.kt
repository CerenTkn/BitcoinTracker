package com.cerentekin.bitcointracker.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.cerentekin.bitcointracker.R
import com.cerentekin.bitcointracker.VM.CoinVM
import com.cerentekin.bitcointracker.data.model.Coin
import com.cerentekin.bitcointracker.databinding.FragmentCoinDetailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CoinDetailFragment : Fragment() {

    private var _binding: FragmentCoinDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CoinVM by viewModels()

    private val handler = Handler(Looper.getMainLooper())
    private var refreshIntervalSec = 30
    private var coinId: String? = null
    private lateinit var selectedCoin: Coin

    private val refreshRunnable = object : Runnable {
        override fun run() {
            coinId?.let { viewModel.getCoinDetail(it) }
            handler.postDelayed(this, refreshIntervalSec * 1000L)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCoinDetailBinding.inflate(inflater, container, false)

        arguments?.let {
            selectedCoin = CoinDetailFragmentArgs.fromBundle(it).coin
            bindCoinData(selectedCoin)
        }

        binding.btnFavourite.setOnClickListener {
            addCoinToFavourites(selectedCoin)
        }

        return binding.root
    }

    private fun bindCoinData(coin: Coin) {
        binding.tvName.text = coin.name
        binding.tvSymbol.text = coin.symbol
        binding.tvPrice.text = "$${coin.currentPrice}"

        Glide.with(binding.root.context)
            .load(coin.image)
            .placeholder(R.drawable.coin_placeholder)
            .error(R.drawable.error_image)
            .into(binding.ivCoinImage)
    }

    private fun addCoinToFavourites(coin: Coin) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        val db = FirebaseFirestore.getInstance()
        val coinData = hashMapOf(
            "id" to coin.id,
            "name" to coin.name,
            "symbol" to coin.symbol,
            "image" to coin.image,
            "current_price" to coin.currentPrice
        )

        db.collection("users")
            .document(userId)
            .collection("favourites")
            .document(coin.id)
            .set(coinData)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Added to favourites", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to add", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        coinId = arguments?.getString("coinId")
        coinId?.let { viewModel.getCoinDetail(it) }

        val toolbar = binding.toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.orange)

        setHasOptionsMenu(true)
        observeDetail()

        binding.etRefreshInterval.setOnEditorActionListener { _, _, _ ->
            val input = binding.etRefreshInterval.text.toString()
            if (input.isNotBlank()) {
                refreshIntervalSec = input.toIntOrNull() ?: 30
                Toast.makeText(requireContext(), "Refresh interval set to $refreshIntervalSec sec", Toast.LENGTH_SHORT).show()
                handler.removeCallbacks(refreshRunnable)
                handler.postDelayed(refreshRunnable, refreshIntervalSec * 1000L)
            }
            false
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                findNavController().navigateUp()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun observeDetail() {
        viewModel.coinDetail.observe(viewLifecycleOwner) { coin ->
            Log.d("CoinDetailJSON", Gson().toJson(coin))

            binding.tvName.text = coin.name
            binding.tvSymbol.text = "(${coin.symbol.uppercase()})"
            binding.tvHashing.text = "Hashing: ${coin.hashingAlgorithm ?: "N/A"}"
            binding.tvDescription.text = HtmlCompat.fromHtml(coin.description.en, HtmlCompat.FROM_HTML_MODE_LEGACY)
            binding.tvPrice.text = "Price: $${coin.marketData.currentPrice["usd"] ?: "N/A"}"

            val change24h = coin.marketData.priceChangePercentage24h
            if (change24h != null) {
                val formattedChange = "%.2f%%".format(change24h)
                binding.tvPriceChange.text = "24h: $formattedChange"
            } else {
                binding.tvPriceChange.text = "24h: N/A"
            }

            Log.d("DEBUG", "24h change: $change24h")
            Glide.with(requireContext())
                .load(coin.image.large)
                .placeholder(R.drawable.coin_placeholder)
                .error(R.drawable.error_image)
                .into(binding.ivCoinImage)
        }
    }


    override fun onResume() {
        super.onResume()
        handler.postDelayed(refreshRunnable, refreshIntervalSec * 1000L)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(refreshRunnable)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        (requireActivity() as AppCompatActivity).supportActionBar?.show()

    }
}
