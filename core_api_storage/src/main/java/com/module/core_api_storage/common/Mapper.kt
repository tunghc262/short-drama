package com.module.core_api_storage.common

import com.module.core_api_storage.database.collection_data.DramaWithGenres
import com.module.core_api_storage.database.entity.DramaCategoryEntity
import com.module.core_api_storage.database.entity.DramaEntity
import com.module.core_api_storage.database.entity.DramaEpisodeEntity
import com.module.core_api_storage.database.entity.DramaGenresEntity
import com.module.core_api_storage.database.relational_entity.DramaCategoryCrossRefEntity
import com.module.core_api_storage.database.relational_entity.DramaGenresCrossRefEntity
import com.module.core_api_storage.model_dto.DramaCategoryModel
import com.module.core_api_storage.model_dto.DramaEpisodeModel
import com.module.core_api_storage.model_dto.DramaGenresModel
import com.module.core_api_storage.model_dto.DramaModel
import com.module.core_api_storage.model_dto_relational.DramaCategoryCrossModel
import com.module.core_api_storage.model_dto_relational.DramaGenresCrossModel
import com.module.core_api_storage.model_ui.DramaEpisodeUIModel
import com.module.core_api_storage.model_ui.DramaGenresUIModel
import com.module.core_api_storage.model_ui.DramaUIModel
import com.module.core_api_storage.model_ui.DramaWithGenresUIModel

fun DramaModel.toEntity(): DramaEntity {
    return DramaEntity(
        dramaId = dramaId,
        dramaName = dramaName,
        dramaDescription = dramaDescription,
        dramaThumb = dramaThumb,
        dramaTrailer = dramaTrailer,
        totalEpisode = totalEpisode
    )
}

fun DramaEpisodeModel.toEntity(): DramaEpisodeEntity {
    return DramaEpisodeEntity(
        episodeId = episodeId,
        dramaOwnerId = dramaOwnerId,
        urlStream = urlStream,
        serialNo = serialNo
    )
}

fun DramaCategoryModel.toEntity(): DramaCategoryEntity {
    return DramaCategoryEntity(
        categoryId = categoryId,
        categoryName = categoryName
    )
}

fun DramaGenresModel.toEntity(): DramaGenresEntity {
    return DramaGenresEntity(
        genresId = genresId,
        genresName = genresName
    )
}

fun DramaCategoryCrossModel.toEntity(): DramaCategoryCrossRefEntity {
    return DramaCategoryCrossRefEntity(
        dramaId = dramaId,
        categoryId = categoryId
    )
}


fun DramaGenresCrossModel.toEntity(): DramaGenresCrossRefEntity {
    return DramaGenresCrossRefEntity(
        dramaId = dramaId,
        genresId = genresId
    )
}

fun DramaEntity.toUIModel(): DramaUIModel {
    return DramaUIModel(
        dramaId = dramaId,
        dramaName = dramaName,
        dramaDescription = dramaDescription,
        dramaThumb = dramaThumb,
        dramaTrailer = dramaTrailer,
        totalEpisode = totalEpisode
    )
}

fun DramaEpisodeEntity.toUIModel(): DramaEpisodeUIModel {
    return DramaEpisodeUIModel(
        episodeId = episodeId,
        dramaOwnerId = dramaOwnerId,
        urlStream = urlStream,
        serialNo = serialNo
    )
}

fun DramaGenresEntity.toUIModel(): DramaGenresUIModel {
    return DramaGenresUIModel(
        genresId = genresId,
        genresName = genresName
    )
}

fun DramaWithGenres.toUIModel(): DramaWithGenresUIModel {
    return DramaWithGenresUIModel(
        dramaUIModel = drama.toUIModel(),
        dramaGenresUIModel = genres.map { it.toUIModel() }
    )
}