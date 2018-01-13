package com.fueledbycaffeine.bunnypedia.database.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RulesSection(val title: String, val text: String): Parcelable
