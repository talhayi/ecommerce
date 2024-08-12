package com.example.ecommerce.presentation.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.util.Result
import com.example.ecommerce.R
import com.example.ecommerce.databinding.FragmentFilterBinding
import com.example.ecommerce.presentation.adapter.FilterAdapter
import com.example.ecommerce.presentation.products.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
@AndroidEntryPoint
class FilterFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentFilterBinding
    private val filterViewModel: FilterViewModel by viewModels()
    private val homeViewModel: HomeViewModel by activityViewModels()
    private var brandAdapter: FilterAdapter? = null
    private var modelAdapter: FilterAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerViewScrollListeners()
        observeFilters()
        onPrimaryButton()
        onBackButton()
    }

    private fun setupRecyclerViews(brands: List<String?>, models: List<String?>) {
        brandAdapter = FilterAdapter(brands)
        binding.recyclerViewBrand.adapter = brandAdapter
        binding.recyclerViewBrand.layoutManager = LinearLayoutManager(requireContext())

        modelAdapter = FilterAdapter(models)
        binding.recyclerViewModel.adapter = modelAdapter
        binding.recyclerViewModel.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeFilters() {
        viewLifecycleOwner.lifecycleScope.launch {
            filterViewModel.filterState.collect { result ->
                when (result) {
                    is Result.Success -> {
                        val products = result.data
                        val uniqueBrands = filterViewModel.getBrands(products)
                        val uniqueModels = filterViewModel.getModels(products)
                        setupRecyclerViews(uniqueBrands, uniqueModels)
                        binding.progressBar.visibility = View.GONE
                        binding.textViewError.visibility = View.GONE
                    }
                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.textViewError.apply {
                            text = result.message
                            visibility = View.VISIBLE
                        }
                    }
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.textViewError.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun applyFilters() {
        val filterOptions = FilterOptions(
            brands = brandAdapter?.getSelectedItems() ?: emptyList(),
            models = modelAdapter?.getSelectedItems() ?: emptyList(),
            sortBy = getSelectedSortOption()
        )
        homeViewModel.setFilterOptions(filterOptions)
        findNavController().popBackStack()
    }

    private fun getSelectedSortOption(): SortOption {
        return when (binding.radioGroup.checkedRadioButtonId) {
            R.id.radioButtonOldToNew -> SortOption.OLD_TO_NEW
            R.id.radioButtonNewToOld -> SortOption.NEW_TO_OLD
            R.id.radioButtonPriceHighToLow -> SortOption.PRICE_HIGH_TO_LOW
            R.id.radioButtonPriceLowToHigh -> SortOption.PRICE_LOW_TO_HIGH
            else -> SortOption.NONE
        }
    }

    private fun onPrimaryButton(){
        binding.buttonPrimary.setOnClickListener {
            applyFilters()
        }
    }

    private fun onBackButton(){
        binding.imageViewBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupRecyclerViewScrollListeners() {
        val behavior = BottomSheetBehavior.from(view?.parent as View)

        binding.recyclerViewBrand.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                behavior.isDraggable = !recyclerView.canScrollVertically(-2)
            }
        })

        binding.recyclerViewModel.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                behavior.isDraggable = !recyclerView.canScrollVertically(-2)
            }
        })
    }
}