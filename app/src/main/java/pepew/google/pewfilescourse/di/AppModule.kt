package pepew.google.pewfilescourse.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import pepew.google.pewfilescourse.data.repository.FilesRepositoryImpl
import pepew.google.pewfilescourse.domain.repository.FilesRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFilesRepository(@ApplicationContext applicationContext: Context): FilesRepository {
        return FilesRepositoryImpl(applicationContext)
    }

}