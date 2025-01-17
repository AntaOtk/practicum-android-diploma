package ru.practicum.android.diploma.domain.models.detail

import ru.practicum.android.diploma.domain.models.Contact
import ru.practicum.android.diploma.domain.models.Salary

data class FullVacancy(
    val id: String,
    val name: String,
    val city: String,
    val employerName: String,
    val employerLogoUrl: String?,
    val salary: Salary?,
    val alternate_url: String?,
    val brandedDescription: String?,
    val contacts: Contact?,
    val description: String?,
    val experience: String?,
    val employment: String,
    val skills: String?,
)
