package com.fueledbycaffeine.bunnypedia.database

sealed class QueryResult<out T> {
  data class Error(val error: Throwable) : QueryResult<Nothing>()
  data class Found<T>(val item: T) : QueryResult<T>()
}
