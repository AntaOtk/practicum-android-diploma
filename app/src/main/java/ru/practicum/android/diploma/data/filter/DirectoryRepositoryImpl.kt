package ru.practicum.android.diploma.data.filter

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.data.NetworkClient
import ru.practicum.android.diploma.data.ResourceProvider
import ru.practicum.android.diploma.data.dto.filter.CountryResponse
import ru.practicum.android.diploma.data.dto.filter.IndustryResponse
import ru.practicum.android.diploma.data.dto.filter.RegionListDto
import ru.practicum.android.diploma.domain.api.DirectoryRepository
import ru.practicum.android.diploma.domain.models.filter.Area
import ru.practicum.android.diploma.domain.models.filter.Country
import ru.practicum.android.diploma.domain.models.filter.Industry
import ru.practicum.android.diploma.util.ERROR
import ru.practicum.android.diploma.util.Resource
import ru.practicum.android.diploma.util.SUCCESS

class DirectoryRepositoryImpl(
    val networkClient: NetworkClient,
    val resourceProvider: ResourceProvider,
    val mapper: FiltersMapper
) : DirectoryRepository {
    override fun getIndustries(): Flow<Resource<List<Industry>>> = flow {
        val response = networkClient.doIndustryRequest()
        when (response.resultCode) {
            ERROR -> {
                emit(Resource.Error(resourceProvider.getString(R.string.check_connection)))
            }

            SUCCESS -> {
                with(response as IndustryResponse) {
                    val industryList = industries.map { mapper.mapIndustryFromDto(it) }
                    emit(Resource.Success(industryList))
                }

            }

            else -> {
                emit(Resource.Error(resourceProvider.getString(R.string.server_error)))
            }
        }
    }

    override fun getCountries(): Flow<Resource<List<Country>>> = flow {
        val response = networkClient.doCountryRequest()
        when (response.resultCode) {
            ERROR -> {
                emit(Resource.Error(resourceProvider.getString(R.string.check_connection)))
            }

            SUCCESS -> {
                with(response as CountryResponse) {
                    val countryList = countries.map { mapper.mapCoyntryFromDto(it) }
                    emit(Resource.Success(countryList))
                }
            }

            else -> {
                emit(Resource.Error(resourceProvider.getString(R.string.server_error)))
            }
        }
    }

    override fun getAreas(areaId: String): Flow<Resource<List<Area>>> = flow {
        val response = networkClient.doAreaRequest(areaId)
        when (response.resultCode) {
            ERROR -> {
                emit(Resource.Error(resourceProvider.getString(R.string.check_connection)))
            }

            SUCCESS -> {
                with(response as RegionListDto) {
                    val countryList = areas.map { mapper.mapAreasFromDto(it) }
                    emit(Resource.Success(countryList))
                }

            }

            else -> {
                emit(Resource.Error(resourceProvider.getString(R.string.server_error)))
            }
        }
    }

}