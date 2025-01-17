package ru.practicum.android.diploma.presentation.filter

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSettingsFiltersBinding
import ru.practicum.android.diploma.domain.models.filter.Filters
import ru.practicum.android.diploma.ui.root.BottomNavigationVisibilityListener

class SettingsFilterFragment : Fragment() {
    private val viewModel by viewModel<FilterViewModel>()

    private var _binding: FragmentSettingsFiltersBinding? = null
    private val binding get() = _binding!!
    private var inputText: String = ""
    private var simpleTextWatcher: TextWatcher? = null

    private var bottomNavigationVisibilityListener: BottomNavigationVisibilityListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BottomNavigationVisibilityListener) {
            bottomNavigationVisibilityListener = context
        } else {
            Log.e("SettingsFilterFragment", "$context must implement BottomNavigationVisibilityListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsFiltersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomNavigationVisibilityListener?.setBottomNavigationVisibility(false)
        viewModel.getFilters()
        binding.confirmButton.isEnabled = false
        viewModel.observeChanges().observe(viewLifecycleOwner) {
            changeEnabled(it)
        }
        viewModel.observeFilters().observe(viewLifecycleOwner) {
            showFilters(it)
        }
        binding.salaryEt.isSelected = (inputText.isNotEmpty())
        simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                inputText = s?.toString() ?: ""
                viewModel.checkChanges(inputText)
                checkFieldsForResetVisibility()
                if (binding.salaryEt.text.isNotEmpty()) {
                    binding.clearButtonIcon.isVisible = true
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        simpleTextWatcher?.let { binding.salaryEt.addTextChangedListener(it) }
        binding.clearButtonIcon.setOnClickListener {
            binding.salaryEt.setText("")
            binding.clearButtonIcon.isVisible = false

        }

        binding.workPlaceClear.setOnClickListener {
            binding.workPlaceClear.isVisible = false
            viewModel.clearArea()
        }

        binding.industryClear.setOnClickListener {
            binding.industryClear.isVisible = false
            viewModel.clearIndustry()
        }

        binding.confirmButton.setOnClickListener {
            viewModel.setSalary(inputText)
            findNavController().popBackStack()
        }
        binding.settingsBackArrowImageview.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.workPlaceEditText.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFiltersFragment_to_selectWorkplaceFragment)
        }

        binding.industryEditText.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFiltersFragment_to_selectIndustryFragment)
        }

        binding.resetSettingsTextview.setOnClickListener {
            resetFields()
            viewModel.clearFilters()
        }

        binding.doNotShowWithoutSalaryCheckBox.setOnClickListener {
            viewModel.setSalaryStatus(binding.doNotShowWithoutSalaryCheckBox.isChecked)
        }
    }

    private fun changeEnabled(isEnabled: Boolean) {
        binding.confirmButton.isEnabled = isEnabled
        binding.resetSettingsTextview.isVisible = true
    }

    private fun showFilters(filters: Filters) {
        val countryName = filters.country?.name ?: ""
        val areaName = filters.area?.name ?: ""
        if (areaName.isNotEmpty()) {
            binding.workPlaceClear.isVisible = true
            binding.workPlaceEditText.setText(buildString {
                append(countryName)
                append(", ")
                append(areaName)
            })
        } else if (countryName.isNotEmpty()) {
            binding.workPlaceEditText.setText(countryName)
            binding.workPlaceClear.isVisible = true
        } else {
            binding.workPlaceEditText.setText("")
            binding.workPlaceClear.isVisible = false
        }
        if (!filters.industries.isNullOrEmpty()) {
            val sb = StringBuilder()
            for (item in filters.industries) {
                sb.append(item.name).append(",")
            }
            binding.industryEditText.setText(sb.toString())
            binding.industryClear.isVisible = true
        } else {
            binding.industryEditText.setText("")
            binding.industryClear.isVisible = false
        }
        binding.clearButtonIcon.isVisible = !filters.preferSalary.isNullOrEmpty()
        binding.salaryEt.setText(filters.preferSalary)
        binding.doNotShowWithoutSalaryCheckBox.isChecked = filters.isIncludeSalary
    }

    private fun resetFields() {
        binding.workPlaceEditText.text = null
        binding.industryEditText.text = null
        binding.salaryEt.text = null
        binding.doNotShowWithoutSalaryCheckBox.isChecked = false
        binding.clearButtonIcon.isVisible = false
        binding.industryClear.isVisible = false
        binding.workPlaceClear.isVisible = false
        checkFieldsForResetVisibility()
    }

    private fun checkFieldsForResetVisibility() {
        val isAnyFieldNotEmpty = binding.workPlaceEditText.text?.isNotEmpty() ?: false ||
                binding.industryEditText.text?.isNotEmpty() ?: false ||
                binding.salaryEt.text?.isNotEmpty() ?: false ||
                binding.doNotShowWithoutSalaryCheckBox.isChecked
        binding.resetSettingsTextview.isVisible = isAnyFieldNotEmpty
        if (!isAnyFieldNotEmpty) {
            binding.resetSettingsTextview.isVisible = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}