package com.example.core_api.auth;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class TokenAuthenticator_Factory implements Factory<TokenAuthenticator> {
  private final Provider<TokenRefresher> refresherProvider;

  private TokenAuthenticator_Factory(Provider<TokenRefresher> refresherProvider) {
    this.refresherProvider = refresherProvider;
  }

  @Override
  public TokenAuthenticator get() {
    return newInstance(refresherProvider.get());
  }

  public static TokenAuthenticator_Factory create(Provider<TokenRefresher> refresherProvider) {
    return new TokenAuthenticator_Factory(refresherProvider);
  }

  public static TokenAuthenticator newInstance(TokenRefresher refresher) {
    return new TokenAuthenticator(refresher);
  }
}
