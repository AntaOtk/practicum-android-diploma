package ru.practicum.android.diploma.presentation.filter.selectArea

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.filter.Area
import ru.practicum.android.diploma.presentation.ModelFragment
import ru.practicum.android.diploma.presentation.filter.selectArea.adaptor.AreaAdapter
import ru.practicum.android.diploma.presentation.filter.selectArea.state.AreasState

class SelectAreaFragment : ModelFragment() {

    private val viewModel: SelectAreaViewModel by viewModel()
    private val listArea = mutableListOf<Area>()
    private lateinit var clickFunction: (Area) -> Unit
    private var areasAdapter = AreaAdapter(listArea) { area ->
        clickFunction(area)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbarInclude.headerTitle.text = getString(R.string.select_area)
        viewModel.observeAreasState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is AreasState.DisplayAreas -> displayAreas(state.areas)
                is AreasState.Error -> displayError(state.errorText)
            }
        }
        viewModel.observeFilterAreasState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is AreasState.DisplayAreas -> displayAreas(state.areas)
                is AreasState.Error -> displayFilterError(state.errorText)
            }
        }
        setupSearchInput()
        clickFunction = { area ->
            viewModel.onAreaClicked(area)
            findNavController().popBackStack()
        }
        binding.RecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = areasAdapter
        }
    }

    private fun setupSearchInput() {
        binding.searchEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                viewModel.filterAreas(s?.toString() ?: "")
            }

            override fun afterTextChanged(editable: Editable?) {

            }
        })
    }

    private fun displayAreas(areas: ArrayList<Area>) {
        binding.apply {
            RecyclerView.visibility = View.VISIBLE
            placeholderMessage.visibility = View.GONE
        }
        listArea.clear()
        listArea.addAll(areas)
        areasAdapter.notifyDataSetChanged()
    }

    private fun displayError(errorText: String) {
        binding.apply {
            RecyclerView.isVisible = true
            placeholderMessage.isVisible = false
            placeholderMessageImage.setImageResource(R.drawable.search_placeholder_nothing_found)
            placeholderMessageText.text = errorText
        }
    }

    private fun displayFilterError(errorText: String) {
        binding.apply {
            RecyclerView.isVisible = false
            placeholderMessage.isVisible = true
            placeholderMessageImage.setImageResource(R.drawable.search_placeholder_nothing_found)
            placeholderMessageText.text = errorText
        }
    }
}
