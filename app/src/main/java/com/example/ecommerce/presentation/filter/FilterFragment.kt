package com.example.ecommerce.presentation.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.ecommerce.R
import com.example.ecommerce.databinding.FragmentFilterBinding
import com.example.ecommerce.presentation.products.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentFilterBinding
    private val homeViewModel: HomeViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonPrimary.setOnClickListener {
            applyFilters()
        }
    }

    private fun applyFilters() {
        val filterOptions = FilterOptions(
            brands = getSelectedBrands(),
            models = getSelectedModels(),
            sortBy = getSelectedSortOption()
        )
        homeViewModel.setFilterOptions(filterOptions)
        findNavController().popBackStack()
    }

    private fun getSelectedBrands(): List<String> {
        val brands = mutableListOf<String>()
        if (binding.checkBoxLamborghini.isChecked) brands.add("Lamborghini")
        if (binding.checkBox12Ferrari.isChecked) brands.add("Ferrari")
        if (binding.checkBoxVolkswagen.isChecked) brands.add("Volkswagen")
        return brands
    }

    private fun getSelectedModels(): List<String> {
        val models = mutableListOf<String>()
        if (binding.checkBoxCTS.isChecked) models.add("CTS")
        if (binding.checkBoxRoadster.isChecked) models.add("Roadster")
        if (binding.checkBoxJetta.isChecked) models.add("Jetta")
        return models
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
}