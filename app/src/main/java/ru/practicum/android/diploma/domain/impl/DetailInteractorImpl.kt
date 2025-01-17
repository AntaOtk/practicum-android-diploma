package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.domain.ExternalNavigator
import ru.practicum.android.diploma.domain.api.DetailRepository
import ru.practicum.android.diploma.domain.detail.DetailInteractor
import ru.practicum.android.diploma.domain.favorite.FavouriteRepository
import ru.practicum.android.diploma.domain.models.Phone
import ru.practicum.android.diploma.domain.models.detail.FullVacancy
import ru.practicum.android.diploma.util.Resource

class DetailInteractorImpl(
    val repository: DetailRepository,
    val favouriteRepository: FavouriteRepository,
    private val navigator: ExternalNavigator
) : DetailInteractor {

    override fun getVacancy(id: String): Flow<Pair<FullVacancy?, String?>> {
        return repository.getVacancy(id).map { result ->
            when (result) {
                is Resource.Success<*> -> {
                    favouriteRepository.updateVacancy(result.data)
                    Pair(result.data, null)
                }

                is Resource.Error<*> -> {
                    val favouriteVacancy = favouriteRepository.getFullVacancy(id)
                    if (favouriteVacancy != null) Pair(favouriteVacancy, null) else Pair(
                        null,
                        result.message
                    )
                }
            }
        }
    }

    override fun sharePhone(phone: Phone) {
        val textPhone = "+" + phone.country + phone.city + phone.number
        navigator.sharePhone(textPhone)
    }

    override fun shareEmail(email: String) {
        navigator.shareEmail(email)
    }

    override fun shareVacancyUrl(vacancyUrl: String) {
        navigator.shareVacancyUrl(vacancyUrl)
    }
}