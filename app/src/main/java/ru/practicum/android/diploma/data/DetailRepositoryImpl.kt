package ru.practicum.android.diploma.data

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.data.dto.detail.FullVacancyDto
import ru.practicum.android.diploma.domain.api.DetailRepository
import ru.practicum.android.diploma.domain.models.detail.FullVacancy
import ru.practicum.android.diploma.util.ERROR
import ru.practicum.android.diploma.util.Resource
import ru.practicum.android.diploma.util.SUCCESS

class DetailRepositoryImpl(
    private val networkClient: NetworkClient,
    private val resourceProvider: ResourceProvider,
    private val mapper: VacancyMapper,
) : DetailRepository {
    override fun getVacancy(id: String): Flow<Resource<FullVacancy>> = flow {
        val response = networkClient.doDetailRequest(id)
        Log.d("vacancyResponse", "Response: ${response.resultCode}")
        when (response.resultCode) {
            ERROR -> {
                emit(Resource.Error(resourceProvider.getString(R.string.check_connection)))
            }

            SUCCESS -> {
                with(response as FullVacancyDto) {
                    val vacancy = mapper.mapVacancyFromFullVacancyDto(this)
                    Log.d("vacancyResponse", "Response: $response")
                    emit(Resource.Success(vacancy))
                }
            }

            else -> {
                emit(Resource.Error(resourceProvider.getString(R.string.server_error)))
            }
        }
    }
}

