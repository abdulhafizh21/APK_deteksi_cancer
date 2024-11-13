package com.dicoding.asclepius.data.db

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class HistoryResult(
    @PrimaryKey(autoGenerate = false)
    var image: String = "",
    var label: String = "",
    var score: String = ""
) : Parcelable