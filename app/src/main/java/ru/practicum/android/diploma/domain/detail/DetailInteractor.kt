package ru.practicum.android.diploma.domain.detail

import ru.practicum.android.diploma.domain.models.detail.Vacancy

interface DetailInteractor {
fun getVacancy(): Vacancy
}
