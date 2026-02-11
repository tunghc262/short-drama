package com.module.core_api_storage.database.collection_data

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.module.core_api_storage.database.entity.DramaEntity
import com.module.core_api_storage.database.entity.DramaGenresEntity
import com.module.core_api_storage.database.relational_entity.DramaGenresCrossRefEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class DramaWithGenres(
    @Embedded val drama: DramaEntity,
    @Relation(
        parentColumn = "dramaId",
        entityColumn = "genresId",
        associateBy = Junction(DramaGenresCrossRefEntity::class)
    )
    val genres: List<DramaGenresEntity>
) : Parcelable