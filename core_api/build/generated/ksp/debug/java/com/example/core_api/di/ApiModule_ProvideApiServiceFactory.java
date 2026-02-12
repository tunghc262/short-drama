package com.example.core_api.di;

import com.example.core_api.api.MoviesApiService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import retrofit2.Retrofit;

@ScopeMetadata
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
public final class ApiModule_ProvideApiServiceFactory implements Factory<MoviesApiService> {
  private final Provider<Retrofit> retrofitProvider;

  private ApiModule_ProvideApiServiceFactory(Provider<Retrofit> retrofitProvider) {
    this.retrofitProvider = retrofitProvider;
  }

  @Override
  public MoviesApiService get() {
    return provideApiService(retrofitProvider.get());
  }

  public static ApiModule_ProvideApiServiceFactory create(Provider<Retrofit> retrofitProvider) {
    return new ApiModule_ProvideApiServiceFactory(retrofitProvider);
  }

  public static MoviesApiService provideApiService(Retrofit retrofit) {
    return Preconditions.checkNotNullFromProvides(ApiModule.INSTANCE.provideApiService(retrofit));
  }
}
