package com.example.core_api.di;

import com.example.core_api.auth.AuthenApi;
import com.example.core_api.auth.TokenRefresher;
import com.example.core_api.auth.TokenStore;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class ApiModule_ProvideTokenRefresherFactory implements Factory<TokenRefresher> {
  private final Provider<AuthenApi> apiProvider;

  private final Provider<TokenStore> storeProvider;

  private ApiModule_ProvideTokenRefresherFactory(Provider<AuthenApi> apiProvider,
      Provider<TokenStore> storeProvider) {
    this.apiProvider = apiProvider;
    this.storeProvider = storeProvider;
  }

  @Override
  public TokenRefresher get() {
    return provideTokenRefresher(apiProvider.get(), storeProvider.get());
  }

  public static ApiModule_ProvideTokenRefresherFactory create(Provider<AuthenApi> apiProvider,
      Provider<TokenStore> storeProvider) {
    return new ApiModule_ProvideTokenRefresherFactory(apiProvider, storeProvider);
  }

  public static TokenRefresher provideTokenRefresher(AuthenApi api, TokenStore store) {
    return Preconditions.checkNotNullFromProvides(ApiModule.INSTANCE.provideTokenRefresher(api, store));
  }
}
