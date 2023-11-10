package ru.practicum.android.diploma.data.dto

import ru.practicum.android.diploma.data.dto.filter.AreasDto
import ru.practicum.android.diploma.data.dto.filter.IndustryDto

open class Response {
    var resultCode = 0
    var resultAreas = listOf<AreasDto>()
    var resultIndustries = listOf<IndustryDto>()

}