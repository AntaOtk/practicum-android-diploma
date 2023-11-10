package ru.practicum.android.diploma.data.dto.search

import ru.practicum.android.diploma.data.dto.Response
import ru.practicum.android.diploma.data.dto.VacancyDto
import ru.practicum.android.diploma.data.dto.filter.AreasDto
import ru.practicum.android.diploma.data.dto.filter.CountryDto

open class Response {
    var resultCode = 0
    var resultAreas = listOf<AreasDto>()
    var resultCountries = listOf<CountryDto>()

}