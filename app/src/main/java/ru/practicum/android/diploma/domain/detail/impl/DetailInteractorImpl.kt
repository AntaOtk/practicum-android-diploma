package ru.practicum.android.diploma.domain.detail.impl

import ru.practicum.android.diploma.domain.detail.DetailRepository
import ru.practicum.android.diploma.domain.detail.DetailInteractor
import ru.practicum.android.diploma.domain.models.detail.Vacancy

class DetailInteractorImpl(val repository: DetailRepository, private val navigator: ExternalNavigator) : DetailInteractor {
    override fun getVacancy(): Vacancy {
        return repository.getVavancy()
    }

    override fun sharePhone(phone: String){
        navigator.sharePhone(phone)
    }

    override fun shareEmail(email: String) {
        navigator.shareEmail(email)
    }
}