package ru.practicum.android.diploma.domain.filter

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.filter.Area
import ru.practicum.android.diploma.domain.models.filter.Filters
import ru.practicum.android.diploma.domain.models.filter.Industry
import ru.practicum.android.diploma.util.Resource

interface FiltersRepository {
    suspend fun getAres(): Flow<Resource<List<Area>>>
    suspend fun getFilters():Flow<Filters>
    suspend fun writeFilters(filters: Filters)
    suspend fun getIndustries():Flow<Resource<List<Industry>>>
}