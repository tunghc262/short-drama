package com.module.core_api_storage.di

import android.content.Context
import com.module.core_api_storage.database.DramaDatabase
import com.module.core_api_storage.database.dao.CommonDao
import com.module.core_api_storage.database.dao.DramaCategoryCrossDao
import com.module.core_api_storage.database.dao.DramaCategoryDao
import com.module.core_api_storage.database.dao.DramaDao
import com.module.core_api_storage.database.dao.DramaEpisodeDao
import com.module.core_api_storage.database.dao.DramaGenresCrossDao
import com.module.core_api_storage.database.dao.DramaGenresDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DramaModule {

    @Provides
    @Singleton
    fun provideDramaDatabase(context: Context): DramaDatabase = DramaDatabase.getDatabase(context)

    //drama table
    @Provides
    fun provideDrama(dramaDatabase: DramaDatabase): DramaDao {
        return dramaDatabase.dramaDao
    }

    //drama category table
    @Provides
    fun provideDramaCategory(dramaDatabase: DramaDatabase): DramaCategoryDao {
        return dramaDatabase.dramaCategoryDao
    }

    //drama episode table
    @Provides
    fun provideDramaEpisode(dramaDatabase: DramaDatabase): DramaEpisodeDao {
        return dramaDatabase.dramaEpisodeDao
    }

    //drama genres table
    @Provides
    fun provideDramaGenres(dramaDatabase: DramaDatabase): DramaGenresDao {
        return dramaDatabase.dramaGenresDao
    }

    //drama category cross table
    @Provides
    fun provideDramaCategoryCross(dramaDatabase: DramaDatabase): DramaCategoryCrossDao {
        return dramaDatabase.dramaCategoryCrossDao
    }

    //drama genres cross table
    @Provides
    fun provideDramaGenresCross(dramaDatabase: DramaDatabase): DramaGenresCrossDao {
        return dramaDatabase.dramaGenresCrossDao
    }

    //common dao
    @Provides
    fun provideDramaCommon(dramaDatabase: DramaDatabase): CommonDao {
        return dramaDatabase.commonDao
    }
}
