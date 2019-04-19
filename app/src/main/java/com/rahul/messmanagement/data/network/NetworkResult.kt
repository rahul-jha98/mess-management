package com.rahul.messmanagement.data.network

import okhttp3.Response
import retrofit2.HttpException

sealed class NetworkResult<out T : Any> {
    /**
     * Successful result of request without errors
     */
    class Ok<out T : Any>(
        public val value: T,
        override val response: Response
    ) : NetworkResult<T>(), ResponseResult {
        override fun toString() = "CoroutineResult.Ok{value=$value, response=$response}"
    }

    /**
     * HTTP error
     */
    class Error(
        override val exception: HttpException,
        override val response: Response
    ) : NetworkResult<Nothing>(), ErrorResult, ResponseResult {
        override fun toString() = "CoroutineResult.Error{exception=$exception}"
    }

    /**
     * Network exception occurred talking to the server or when an unexpected
     * exception occurred creating the request or processing the response
     */
    class Exception(
        override val exception: Throwable
    ) : NetworkResult<Nothing>(), ErrorResult {
        override fun toString() = "CoroutineResult.Exception{$exception}"
    }
}

/**
 * Interface for [CoroutineResult] classes with [okhttp3.Response]: [CoroutineResult.Ok] and [CoroutineResult.Error]
 */
interface ResponseResult {
    val response: Response
}

/**
 * Interface for [CoroutineResult] classes that contains [Throwable]: [CoroutineResult.Error] and [CoroutineResult.Exception]
 */
interface ErrorResult {
    val exception: Throwable
}

/**
 * Returns [CoroutineResult.Ok.value] or `null`
 */
fun <T : Any> NetworkResult<T>.getOrNull(): T? =
    if (this is NetworkResult.Ok) this.value else null

/**
 * Returns [CoroutineResult.Ok.value] or [default]
 */
fun <T : Any> NetworkResult<T>.getOrDefault(default: T): T =
    getOrNull() ?: default

/**
 * Returns [CoroutineResult.Ok.value] or throw [throwable] or [ErrorResult.exception]
 */
fun <T : Any> NetworkResult<T>.getOrThrow(throwable: Throwable? = null): T {
    return when (this) {
        is NetworkResult.Ok -> value
        is NetworkResult.Error -> throw throwable ?: exception
        is NetworkResult.Exception -> throw throwable ?: exception
    }
}