package com.jpmc.take.home.my_weather.core

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

/**
* Base repository is used to wrap api calls in the ResultWrapper object.
 *
 * Ideally the repository should sit behind interfaces, this is done just as an experiment.
*/
abstract class BaseRepository {


    suspend fun <T> invoke(apiCall: suspend () -> T): ResultWrapper<T> {
        return withContext(Dispatchers.IO) {
            try {
                ResultWrapper.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                when (throwable) {
                    is IOException -> ResultWrapper.NetworkError
                    is HttpException -> {
                        ResultWrapper.GenericError(null, null)
                    }
                    else -> {
                        ResultWrapper.GenericError(null, null)
                    }
                }
            }
        }
    }
}