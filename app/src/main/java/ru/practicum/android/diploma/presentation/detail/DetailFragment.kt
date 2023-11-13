package ru.practicum.android.diploma.presentation.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.Phone
import ru.practicum.android.diploma.databinding.FragmentVacancyBinding
import ru.practicum.android.diploma.domain.DetailState
import ru.practicum.android.diploma.domain.models.detail.FullVacancy
import ru.practicum.android.diploma.presentation.SalaryPresenter
import ru.practicum.android.diploma.presentation.detail.adapter.PhoneAdapter
import ru.practicum.android.diploma.presentation.search.SearchFragment
import ru.practicum.android.diploma.presentation.similar.SimilarVacanciesFragment
import ru.practicum.android.diploma.util.debounce


class DetailFragment : Fragment() {
    private val viewModel by viewModel<DetailViewModel>()
    private var _binding: FragmentVacancyBinding? = null
    private val binding get() = _binding!!

    private var fullVacancy: FullVacancy? = null
    lateinit var onItemClickDebounce: (Phone) -> Unit

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVacancyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getVacancy(vacancyId)
        viewModel.getStatus(vacancyId)
        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
        viewModel.observedFavouriteState().observe(viewLifecycleOwner) {
            showFavouriteStatus(it)
        }
        binding.toolbarInclude.favourite.setOnClickListener {
            fullVacancy?.let { it1 -> viewModel.changedFavourite(it1) }
        }
        binding.searchButton.setOnClickListener {
            SimilarVacanciesFragment.addArgs(vacancyId)
            findNavController().navigate(R.id.action_detailFragment_to_similarVacanciesFragment)
        }

        val shareButton = view.findViewById<ImageView>(R.id.share)
        shareButton.setOnClickListener {
            fullVacancy?.alternate_url?.let { url ->
                viewModel.shareVacancyUrl(url)
            }
        }



        binding.toolbarInclude.back.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun render(state: DetailState) {
        when (state) {
            is DetailState.Loading -> showLoading()
            is DetailState.Content -> showContent(state.vacancy)
            is DetailState.Error -> showError(state.errorMessage)
        }
    }

    private fun showLoading() {

    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun showContent(vacancy: FullVacancy) {
        fullVacancy = vacancy
        binding.jobNameTv.text = vacancy.name
        binding.jobPaymentTv.text = SalaryPresenter().showSalary(vacancy.salary)
        binding.experienceTv.text = vacancy.experience
        binding.employerNameTv.text = vacancy.employerName
        Glide.with(requireContext())
            .load(vacancy.employerLogoUrl)
            .placeholder(R.drawable.logo)
            .centerCrop()
            .transform(RoundedCorners(requireContext().resources.getDimensionPixelSize(R.dimen.logo_corner_radius)))
            .into(binding.logo)
        binding.locationTv.text = vacancy.city
        binding.contactPersonName.text = vacancy.contacts?.name ?: ""
        binding.emailAddress.text = vacancy.contacts?.email ?: ""
        binding.phone.layoutManager =
            GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false)
        binding.phone.adapter = vacancy.contacts?.let {
            PhoneAdapter(it.phones) { phone ->
                onItemClickDebounce(phone)
            }
        }
        onItemClickDebounce = debounce(
            SearchFragment.CLICK_DEBOUNCE_DELAY_MILLIS,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { phone ->
            viewModel.sharePhone(phone)

        }
        binding.emailAddress.setOnClickListener {
            if (vacancy.contacts?.email != null)
                viewModel.shareEmail(vacancy.contacts.email)
        }
        binding.vacancyDescriptionTv.settings.javaScriptEnabled = true
        val descriptionHtml = vacancy.description
        binding.skillsTv.text = vacancy.skills
        binding.employmentTv.text = vacancy.employment
        if (descriptionHtml != null) {
            binding.vacancyDescriptionTv.loadDataWithBaseURL(
                null,
                descriptionHtml,
                "text/html",
                "UTF-8",
                null
            )
        }

    }


    private fun showError(errorMessage: String) {
    }

    private fun showFavouriteStatus(isFavorite: Boolean) {
        if (isFavorite) binding.toolbarInclude.favourite.setImageResource(R.drawable.ic_favourite_on) else binding.toolbarInclude.favourite.setImageResource(
            R.drawable.ic_favourite
        )
    }


    companion object {
        private var vacancyId: String = ""

        fun addArgs(id: String) {
            vacancyId = id
        }
    }
}
