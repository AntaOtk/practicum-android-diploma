package ru.practicum.android.diploma.presentation.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSelectWorkplaceBinding
import ru.practicum.android.diploma.domain.models.filter.Country
import ru.practicum.android.diploma.presentation.filter.selectArea.SelectCountryViewModel


class SelectWorkplaceFragment : Fragment() {

    private var _binding: FragmentSelectWorkplaceBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SelectCountryViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectWorkplaceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            toolbarInclude.headerTitle.text = getString(R.string.work_place)
            toolbarInclude.favourite.isVisible = false
            toolbarInclude.share.isVisible = false

        }
        binding.toolbarInclude
        // Получаем сохраненную страну
        val savedCountry: Country? = viewModel.interactor.getSelectedCountry()
        // Устанавливаем текст в поле ввода страны
        binding.countryTextInputEditText.setText(savedCountry?.name.orEmpty())
        if (binding.countryTextInputEditText.text?.isNotEmpty() == true){
            binding.selectButton.isVisible = true
        }
        // Обработчик нажатия на кнопку "Назад"
        binding.toolbarInclude.back.setOnClickListener {
            findNavController().popBackStack()
        }

        // Обработчик нажатия на поле ввода региона
        binding.regionTextInputEditText.setOnClickListener {
            findNavController().navigate(R.id.action_selectWorkplaceFragment_to_selectAreaFragment)
        }

        // Обработчик нажатия на поле ввода страны
        binding.countryTextInputEditText.setOnClickListener {
            findNavController().navigate(R.id.action_selectWorkplaceFragment_to_selectCountryFragment)
        }

        viewModel.loadSelectedCountry()
        // Наблюдение за изменениями в выбранной стране
        viewModel.selectedCountry.observe(viewLifecycleOwner) { selectedCountry ->
            binding.countryTextInputEditText.setText(selectedCountry?.name.orEmpty())
        }

        viewModel.loadSelectedArea()
        viewModel.selectedArea.observe(viewLifecycleOwner) { selectedArea ->
            binding.regionTextInputEditText.setText(selectedArea?.name.orEmpty())
        }
        binding.selectButton.setOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.getCountries()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
