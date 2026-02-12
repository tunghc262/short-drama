package com.example.core_api.di;

import com.example.core_api.auth.AuthenApi;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class ApiModule_ProvideAuthApiFactory implements Factory<AuthenApi> {
  @Override
  public AuthenApi get() {
    return provideAuthApi();
  }

  public static ApiModule_ProvideAuthApiFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static AuthenApi provideAuthApi() {
    return Preconditions.checkNotNullFromProvides(ApiModule.INSTANCE.provideAuthApi());
  }

  private static final class InstanceHolder {
    static final ApiModule_ProvideAuthApiFactory INSTANCE = new ApiModule_ProvideAuthApiFactory();
  }
}
