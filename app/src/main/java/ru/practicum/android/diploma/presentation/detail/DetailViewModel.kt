package ru.practicum.android.diploma.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.data.ResourceProvider
import ru.practicum.android.diploma.domain.DetailState
import ru.practicum.android.diploma.domain.detail.DetailInteractor
import ru.practicum.android.diploma.domain.favorite.FavouriteInteractor
import ru.practicum.android.diploma.domain.models.Phone
import ru.practicum.android.diploma.domain.models.detail.FullVacancy

class DetailViewModel(
    private val interactor: DetailInteractor,
    private val resourceProvider: ResourceProvider,
    private val favouriteInteractor: FavouriteInteractor
) : ViewModel() {

    private lateinit var vacancy: FullVacancy
    private var favouriteStateLiveData = MutableLiveData(false)
    fun observedFavouriteState(): LiveData<Boolean> = favouriteStateLiveData

    private val stateLiveData = MutableLiveData<DetailState>()
    fun observeState(): LiveData<DetailState> = stateLiveData
    private fun renderState(state: DetailState) {
        stateLiveData.postValue(state)
    }

    fun getStatus(id: String){
        viewModelScope.launch {
            favouriteInteractor.getFavoriteStatus(id)
                .collect{
                    renderFavouriteState(it)
                }
        }

    }
    fun getVacancy(id: String) {
        renderState(DetailState.Loading)
        viewModelScope.launch {
            interactor.getVacancy(id)
                .collect { pair ->
                    processResult(pair.first, pair.second)
                }
        }
    }

    private fun processResult(outVacancy: FullVacancy?, errorMessage: String?) {
        if (outVacancy != null) {
            vacancy = outVacancy
        }
        when {
            errorMessage != null -> {
                renderState(
                    DetailState.Error(
                        errorMessage = resourceProvider.getString(R.string.no_internet)
                    )
                )
            }

            else -> {
                renderState(
                    DetailState.Content(
                        vacancy
                    )
                )
            }
        }
    }

    fun sharePhone(phone: Phone) {
        interactor.sharePhone(phone)
    }

    fun shareEmail(email: String) {
        interactor.shareEmail(email)
    }

    fun shareVacancyUrl(vacancyUrl: String){
        interactor.shareVacancyUrl(vacancyUrl)
    }

    fun changedFavourite(fullVacancy: FullVacancy){
        if (favouriteStateLiveData.value == true)  deleteFromFavourite(fullVacancy) else addToFavourite(fullVacancy)
    }

    fun addToFavourite(fullVacancy: FullVacancy) {
        viewModelScope.launch {
            favouriteInteractor.addToFavourite(fullVacancy)
            renderFavouriteState(true)
        }
    }

    fun deleteFromFavourite(fullVacancy: FullVacancy){
        viewModelScope.launch {
            favouriteInteractor.deleteVacancy(fullVacancy)
            renderFavouriteState(false)
        }
    }

    private fun renderFavouriteState(isAdded: Boolean) {
        favouriteStateLiveData.postValue(isAdded)
    }



}