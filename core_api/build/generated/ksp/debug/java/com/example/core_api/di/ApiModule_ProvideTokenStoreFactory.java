package com.example.core_api.di;

import android.content.Context;
import com.example.core_api.auth.TokenStore;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class ApiModule_ProvideTokenStoreFactory implements Factory<TokenStore> {
  private final Provider<Context> contextProvider;

  private ApiModule_ProvideTokenStoreFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public TokenStore get() {
    return provideTokenStore(contextProvider.get());
  }

  public static ApiModule_ProvideTokenStoreFactory create(Provider<Context> contextProvider) {
    return new ApiModule_ProvideTokenStoreFactory(contextProvider);
  }

  public static TokenStore provideTokenStore(Context context) {
    return Preconditions.checkNotNullFromProvides(ApiModule.INSTANCE.provideTokenStore(context));
  }
}
