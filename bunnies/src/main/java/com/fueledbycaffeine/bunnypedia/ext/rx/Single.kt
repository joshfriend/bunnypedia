package com.fueledbycaffeine.bunnypedia.ext.rx

import com.fueledbycaffeine.bunnypedia.database.QueryResult
import io.reactivex.Single

fun <T : Any> Single<T>.mapToResult(): Single<QueryResult<T>> {
  return this.map<QueryResult<T>> { QueryResult.Found(it) }
    .onErrorReturn { QueryResult.Error(it) }
}
