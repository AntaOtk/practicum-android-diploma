package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.domain.filter.FiltersInteractor
import ru.practicum.android.diploma.domain.filter.FiltersRepository
import ru.practicum.android.diploma.domain.models.filter.Area
import ru.practicum.android.diploma.domain.models.filter.Filters
import ru.practicum.android.diploma.domain.models.filter.Industry
import ru.practicum.android.diploma.util.Resource

class FiltersInteractorImpl(val filtersRepository: FiltersRepository):FiltersInteractor {
    override suspend fun getAreas(): Flow<Pair<List<Area>?, String?>> {
        return filtersRepository.getAres().map{result ->
            when(result){
                is Resource.Success ->{
                    Pair(result.data, null)
                }
                is Resource.Error ->{
                    Pair(null, result.message)
                }
            }
        }
    }

    override suspend fun getFilters(): Flow<Filters> {
        return filtersRepository.getFilters()
    }

    override suspend fun writeFilters(filters: Filters) {
        filtersRepository.writeFilters(filters)
    }

    override suspend fun getIndustries(): Flow<Pair<List<Industry>?, String?>> {
        return filtersRepository.getIndustries().map { result ->
            when(result){
                is Resource.Success -> {
                    Pair(result.data, null)
                }
                is Resource.Error ->{
                    Pair(null, result.message)
                }
            }
        }
    }
}