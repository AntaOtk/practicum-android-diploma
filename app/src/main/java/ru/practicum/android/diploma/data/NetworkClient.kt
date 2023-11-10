package ru.practicum.android.diploma.data

import ru.practicum.android.diploma.data.dto.Response

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
    suspend fun getAres(dto:Any):Response
    suspend fun getIndustries(dto: Any):Response
}