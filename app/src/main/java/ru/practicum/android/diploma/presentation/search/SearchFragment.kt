package ru.practicum.android.diploma.presentation.search

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.domain.SearchState
import ru.practicum.android.diploma.domain.models.RowType
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.presentation.SalaryPresenter
import ru.practicum.android.diploma.util.CLICK_DEBOUNCE_DELAY_MILLIS
import ru.practicum.android.diploma.util.ID
import ru.practicum.android.diploma.util.debounce


class SearchFragment : Fragment() {


    private var _binding: FragmentSearchBinding? = null
    private val viewModel by viewModel<SearchViewModel>()
    private val salaryPresenter: SalaryPresenter by inject()
    private val binding get() = _binding!!
    private var inputText: String = ""
    private var simpleTextWatcher: TextWatcher? = null
    private lateinit var onItemClickDebounce: (Vacancy) -> Unit
    private val vacancies = mutableListOf<RowType>()
    private val adapter = SearchPagingAdapter(vacancies, salaryPresenter) { vacancy ->
        onItemClickDebounce(vacancy)
    }
    var isLastPage: Boolean = false
    var isLoading: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.updateFilters()

        viewModel.observeFilters().observe(viewLifecycleOwner) { filtersApplied ->
            if (filtersApplied) {
                binding.FilterButtonIcon.setImageResource(R.drawable.filter_on)
            } else {
                binding.FilterButtonIcon.setImageResource(R.drawable.filter_button)
            }
        }

        initRV()
        binding.clearButtonIcon.setOnClickListener {
            if (binding.searchEt.text.isNotEmpty()) {
                binding.searchEt.setText("")
                viewModel.clearInputEditText()
                val inputMethodManager =
                    requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                inputMethodManager?.hideSoftInputFromWindow(binding.clearButtonIcon.windowToken, 0)
            }
        }

        simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                inputText = s?.toString() ?: ""
                viewModel.searchDebounce(inputText)
                if (binding.searchEt.text.isNotEmpty()) {
                    binding.clearButtonIcon.setImageResource(R.drawable.ic_clear_et)
                } else clearEditText()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        simpleTextWatcher?.let { binding.searchEt.addTextChangedListener(it) }

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
        viewModel.observeNextState().observe(viewLifecycleOwner) {
            renderNext(it)
        }

        binding.searchEt.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (binding.searchEt.text.isNotEmpty()) {
                    viewModel.searchDebounce(inputText)
                    binding.clearButtonIcon.setImageResource(R.drawable.ic_clear_et)
                } else clearEditText()
            }
            false
        }
        binding.FilterButtonIcon.setOnClickListener {
            findNavController().navigate(R.id.action_searchFragment_to_settingsFilterFragment)

        }

    }

    private fun renderNext(state: SearchState) {
        when (state) {
            is SearchState.Loading -> showAddLoading()
            is SearchState.Content -> addContent(state.vacancies)
            is SearchState.Error -> showAddError(state.errorMessage)
            is SearchState.Empty -> {}
        }
    }

    private fun showAddError(errorMessage: String) {
        adapter.showError(errorMessage)
    }

    private fun showAddLoading() {
        adapter.showLoading()
    }

    private fun addContent(results: List<Vacancy>) {
        isLoading = false
        adapter.removeLoadingFooter()
        adapter.addData(results)
    }

    private fun initRV() {
        binding.rvSearch.adapter = adapter
        val linearLayoutManager = LinearLayoutManager(requireContext())
        binding.rvSearch.layoutManager = linearLayoutManager
        onItemClickDebounce = debounce(
            CLICK_DEBOUNCE_DELAY_MILLIS,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { vacancy ->
            val bundle = bundleOf(ID to vacancy.id)
            findNavController().navigate(R.id.action_searchFragment_to_detailFragment, bundle)
        }

        binding.rvSearch.addOnScrollListener(object :
            PaginationScrollListener(linearLayoutManager) {
            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun loadMoreItems() {
                isLoading = true
                viewModel.addSearch(inputText)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render(state: SearchState) {
        when (state) {
            is SearchState.Loading -> showLoading()
            is SearchState.Content -> showContent(state.vacancies, state.foundValue)
            is SearchState.Error -> showError(state.errorMessage)
            is SearchState.Empty -> showEmpty(state.message)
        }
    }

    private fun showLoading() {
        binding.startImage.isVisible = false
        binding.progressBar.isVisible = true
        binding.rvSearch.isVisible = false
        binding.searchCount.isVisible = false
        binding.placeholderMessage.isVisible = false
    }

    private fun showContent(searchVacancies: List<Vacancy>, foundValue: Int) {
        binding.startImage.isVisible = false
        binding.progressBar.isVisible = false
        binding.rvSearch.isVisible = true
        binding.searchCount.isVisible = true
        binding.searchCount.text = requireActivity().resources.getQuantityString(
            R.plurals.vacancies,
            foundValue, foundValue
        )
        binding.placeholderMessage.isVisible = false
        vacancies.clear()
        vacancies.addAll(searchVacancies)
        adapter.notifyDataSetChanged()
    }

    private fun showEmpty(message: String) {
        binding.startImage.isVisible = false
        binding.progressBar.isVisible = false
        binding.rvSearch.isVisible = false
        binding.searchCount.isVisible = false
        binding.placeholderMessage.isVisible = true
        binding.placeholderMessageImage.setImageResource(R.drawable.search_placeholder_nothing_found)
        binding.placeholderMessageText.text = message
    }

    private fun showError(errorMessage: String) {
        binding.startImage.isVisible = false
        binding.progressBar.isVisible = false
        binding.rvSearch.isVisible = false
        binding.searchCount.isVisible = false
        binding.placeholderMessage.isVisible = true
        binding.placeholderMessageImage.setImageResource(R.drawable.no_internet_error)
        binding.placeholderMessageText.text = errorMessage
    }

    private fun clearEditText() {
        binding.clearButtonIcon.setImageResource(R.drawable.ic_search)
        binding.startImage.isVisible = true
        binding.progressBar.isVisible = false
        binding.rvSearch.isVisible = false
        binding.searchCount.isVisible = false
        binding.placeholderMessage.isVisible = false
    }

}