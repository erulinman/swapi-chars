package info.erulinman.swapichars.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import info.erulinman.swapichars.presentation.Character

@Entity(tableName = "characters")
data class CharacterDbEntity(
    @PrimaryKey
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "birth_year") val birthYear: String,
    @ColumnInfo(name = "eye_color") val eyeColor: String,
    @ColumnInfo(name = "gender") val gender: String,
    @ColumnInfo(name = "hair_color") val hairColor: String,
    @ColumnInfo(name = "height") val height: String,
    @ColumnInfo(name = "mass") val mass: String,
    @ColumnInfo(name = "skin_color") val skinColor: String,
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