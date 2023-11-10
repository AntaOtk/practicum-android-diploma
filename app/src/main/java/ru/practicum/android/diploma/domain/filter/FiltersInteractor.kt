package ru.practicum.android.diploma.domain.filter

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.filter.Area
import ru.practicum.android.diploma.domain.models.filter.Filters
import ru.practicum.android.diploma.domain.models.filter.Industry

interface FiltersInteractor {
    suspend fun getAreas(): Flow<Pair<List<Area>?, String?>>

    suspend fun getFilters():Flow<Filters>?

    suspend fun writeFilters(filters: Filters)
    suspend fun getIndustries():Flow<Pair<List<Industry>?, String?>>
}