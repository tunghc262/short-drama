package com.example.core_api.di;

import com.example.core_api.auth.TokenRefresher;
import com.example.core_api.auth.TokenStore;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import okhttp3.OkHttpClient;

@ScopeMetadata("javax.inject.Singleton")
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
public final class ApiModule_ProvideOkHttpFactory implements Factory<OkHttpClient> {
  private final Provider<TokenStore> storeProvider;

  private final Provider<TokenRefresher> refresherProvider;

  private ApiModule_ProvideOkHttpFactory(Provider<TokenStore> storeProvider,
      Provider<TokenRefresher> refresherProvider) {
    this.storeProvider = storeProvider;
    this.refresherProvider = refresherProvider;
  }

  @Override
  public OkHttpClient get() {
    return provideOkHttp(storeProvider.get(), refresherProvider.get());
  }

  public static ApiModule_ProvideOkHttpFactory create(Provider<TokenStore> storeProvider,
      Provider<TokenRefresher> refresherProvider) {
    return new ApiModule_ProvideOkHttpFactory(storeProvider, refresherProvider);
  }

  public static OkHttpClient provideOkHttp(TokenStore store, TokenRefresher refresher) {
    return Preconditions.checkNotNullFromProvides(ApiModule.INSTANCE.provideOkHttp(store, refresher));
  }
}
