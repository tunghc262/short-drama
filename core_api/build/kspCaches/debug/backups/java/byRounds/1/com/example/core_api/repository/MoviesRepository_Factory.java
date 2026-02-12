package com.example.core_api.repository;

import com.example.core_api.api.MoviesApiService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("jakarta.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class MoviesRepository_Factory implements Factory<MoviesRepository> {
  private final Provider<MoviesApiService> apiServiceProvider;

  private MoviesRepository_Factory(Provider<MoviesApiService> apiServiceProvider) {
    this.apiServiceProvider = apiServiceProvider;
  }

  @Override
  public MoviesRepository get() {
    return newInstance(apiServiceProvider.get());
  }

  public static MoviesRepository_Factory create(Provider<MoviesApiService> apiServiceProvider) {
    return new MoviesRepository_Factory(apiServiceProvider);
  }

  public static MoviesRepository newInstance(MoviesApiService apiService) {
    return new MoviesRepository(apiService);
  }
}
