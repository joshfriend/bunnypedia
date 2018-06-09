package com.fueledbycaffeine.bunnypedia.ext.rx

import com.fueledbycaffeine.bunnypedia.database.QueryResult
import io.reactivex.Single

fun <T> Single<T>.mapToResult(): Single<QueryResult<T>> {
  return this.map { QueryResult.Found(it) as QueryResult<T> }
    .onErrorReturn { QueryResult.Error(it) }
}
