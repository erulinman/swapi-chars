package info.erulinman.swapichars.data.network

import info.erulinman.swapichars.presentation.Character

data class CharacterApiEntity(
    val name: String,
    val birth_year: String,
    val eye_color: String,
    val gender: String,
    val hair_color: String,
    val height: String,
    val mass: String,
    val skin_color: String,
) {

    fun toCharacter() = Character(
        this.name,
        this.birth_year,
        this.eye_color,
        this.gender,
        this.hair_color,
        this.height,
        this.mass,
        this.skin_color
    )
}