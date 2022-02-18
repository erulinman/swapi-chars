package info.erulinman.swapichars.data.network

import com.google.gson.annotations.SerializedName
import info.erulinman.swapichars.presentation.CharacterUiEntity

data class CharacterApiEntity(
    @SerializedName("birth_year") val birthYear: String,
    @SerializedName("eye_color") val eyeColor: String,
    @SerializedName("hair_color") val hairColor: String,
    @SerializedName("skin_color") val skinColor: String,
    val name: String,
    val gender: String,
    val height: String,
    val mass: String,
    val created: String,
    val edited: String,
    val films: List<String>,
    val homeworld: String,
    val species: List<String>,
    val starships: List<String>,
    val url: String,
    val vehicles: List<String>
) {

    fun toCharacterUiEntity(favorite: Boolean) = CharacterUiEntity(
        this.name,
        this.birthYear,
        this.eyeColor,
        this.gender,
        this.hairColor,
        this.height,
        this.mass,
        this.skinColor,
        favorite
    )
}