package ru.practicum.android.diploma.data.filter

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.data.NetworkClient
import ru.practicum.android.diploma.data.ResourceProvider
import ru.practicum.android.diploma.data.dto.filter.AreasDto
import ru.practicum.android.diploma.data.dto.filter.CountryDto
import ru.practicum.android.diploma.data.dto.filter.FiltersDto
import ru.practicum.android.diploma.data.dto.filter.IndustryDto
import ru.practicum.android.diploma.data.dto.search.AreaSearchRequest
import ru.practicum.android.diploma.data.dto.search.IndustriesSearchRequest
import ru.practicum.android.diploma.data.filter.DirectoryRepositoryImpl.Companion.ERROR
import ru.practicum.android.diploma.data.filter.DirectoryRepositoryImpl.Companion.SUCCESS
import ru.practicum.android.diploma.data.filter.local.LocalStorage
import ru.practicum.android.diploma.domain.models.filter.Area
import ru.practicum.android.diploma.domain.models.filter.Country
import ru.practicum.android.diploma.domain.models.filter.Filters
import ru.practicum.android.diploma.domain.filter.FiltersRepository
import ru.practicum.android.diploma.domain.models.filter.Industry
import ru.practicum.android.diploma.domain.models.filter.Region
import ru.practicum.android.diploma.util.Resource

class FiltersRepositoryImpl(
    private val networkClient: NetworkClient,
    private val resourceProvider: ResourceProvider,
    private val filtersStorage: LocalStorage
) : FiltersRepository {

    override suspend fun getAres(): Flow<Resource<List<Area>>> = flow {
        val response = networkClient.getAres(AreaSearchRequest)
        when (response.resultCode) {
            ERROR -> {
                emit(Resource.Error(resourceProvider.getString(R.string.check_connection)))
            }

            SUCCESS -> {
                with(response) {
                    val regionList = response.resultAreas.map { mapAresFromDto(it) }
                    emit(ru.practicum.android.diploma.util.Resource.Success(regionList))
                }
            }

            else -> {
                Log.d("myLog", response.resultCode.toString())
                emit(ru.practicum.android.diploma.util.Resource.Error(resourceProvider.getString(R.string.server_error)))
            }
        }
    }

    override suspend fun getFilters(): Flow<Filters> = flow {
        emit(mapFiltersFromDto(filtersStorage.doRequest()))
    }

    override suspend fun writeFilters(filters: Filters) {
        filtersStorage.doWrite(mapFiltersDtoFromFilters(filters))
    }

    override suspend fun getIndustries(): Flow<Resource<List<Industry>>> = flow {
        val response = networkClient.getIndustries(IndustriesSearchRequest)
        when (response.resultCode) {
            ERROR -> {
                emit(Resource.Error(resourceProvider.getString(R.string.check_connection)))
            }

            SUCCESS -> {
                with(response) {
                    val industriesList = response.resultIndustries.map { mapIndustriesFromDto(it) }
                    emit(ru.practicum.android.diploma.util.Resource.Success(industriesList))
                }
            }
            else -> {

                emit(ru.practicum.android.diploma.util.Resource.Error(resourceProvider.getString(R.string.server_error)))
            }
        }
    }


    private fun mapVacancyFromDto(countryDto: CountryDto): Country {
        return Country(
            countryDto.url,
            countryDto.id,
            countryDto.name

        )
    }

    private fun mapAresFromDto(areasDto: AreasDto): Area {
        return Area(
            areasDto.id,
            areasDto.name,
            areasDto.areas.map {
                Region(
                    it.id,
                    it.parent_id,
                    it.name
                )
            }
        )
    }

    private fun mapFiltersFromDto(filtersDto: FiltersDto): Filters {
        return Filters(
            filtersDto.countryName,
            filtersDto.countryId,
            filtersDto.areasNames,
            filtersDto.areasId,
            filtersDto.industriesName,
            filtersDto.industriesId,
            filtersDto.salary,
            filtersDto.onlyWithSalary
        )
    }

    private fun mapFiltersDtoFromFilters(filters: Filters): FiltersDto {
        return FiltersDto(
            filters.countryName,
            filters.countryId,
            filters.areasNames,
            filters.areasId,
            filters.industriesName,
            filters.industriesId,
            filters.salary,
            filters.onlyWithSalary
        )
    }

    private fun mapIndustriesFromDto(industries: IndustryDto): Industry {
        return Industry(
            industries.id,
            industries.industries.map {
                Industries(
                    it.id,
                    it.name
                )
            },
            industries.name

        )
    }

}