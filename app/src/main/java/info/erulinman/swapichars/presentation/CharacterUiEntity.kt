package info.erulinman.swapichars.presentation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CharacterUiEntity(
    val name: String,
    val birthYear: String,
    val eyeColor: String,
    val gender: String,
    val hairColor: String,
    val height: String,
    val mass: String,
    val skinColor: String,
    val favorite: Boolean
): Parcelable