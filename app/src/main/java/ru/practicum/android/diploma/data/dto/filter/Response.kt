package ru.practicum.android.diploma.data.dto.filter

import ru.practicum.android.diploma.data.dto.search.Response
data class CountriesResponse(
    val items:List<CountryDto>
): Response()

data class AreasResponse(
    val items:List<AreasDto>
):Response()