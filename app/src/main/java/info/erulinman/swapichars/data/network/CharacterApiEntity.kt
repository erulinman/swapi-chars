package info.erulinman.swapichars.data.network

import com.google.gson.annotations.SerializedName
import info.erulinman.swapichars.presentation.Character

data class CharacterApiEntity(
    @SerializedName("name") val name: String,
    @SerializedName("birth_year") val birthYear: String,
    @SerializedName("eye_color") val eyeColor: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("hair_color") val hairColor: String,
    @SerializedName("height") val height: String,
    @SerializedName("mass") val mass: String,
    @SerializedName("skin_color") val skinColor: String
) {

    fun toCharacter() = Character(
        this.name,
        this.birthYear,
        this.eyeColor,
        this.gender,
        this.hairColor,
        this.height,
        this.mass,
        this.skinColor
    )
}