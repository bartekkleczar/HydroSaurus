package com.example.hydrosaurus

import com.example.hydrosaurus.viewModels.FirestoreViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DayRecordsDataSource(
                           private val source: FirestoreViewModel,
                           private val refreshIntervalMs: Long = 5000
) {
    val latestRecords: Flow<MutableList<HashMap<String, Any>>> = flow {
        while(true) {
            val records = source.getFromUserListOfRecordsAccDays()
            emit(records) // Emits the result of the request to the flow
            delay(refreshIntervalMs) // Suspends the coroutine for some time
        }
    }
}

// Interface that provides a way to make network requests with suspend functions
interface NewsApi {
    suspend fun fetchLatestNews(): List<ArticleHeadline>
}