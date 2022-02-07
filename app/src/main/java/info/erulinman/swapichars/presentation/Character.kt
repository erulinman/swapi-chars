package info.erulinman.swapichars.presentation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Character(
    val name: String,
    val birthYear: String,
    val eyeColor: String,
    val gender: String,
    val hairColor: String,
    val height: String,
    val mass: String,
    val skinColor: String,
): Parcelable