package com.silkfitness.app.data.auth;

import android.content.Context;
import com.google.firebase.auth.FirebaseAuth;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.Providers;
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
public final class AuthRepository_Factory implements Factory<AuthRepository> {
  private final Provider<Context> appContextProvider;

  private final Provider<FirebaseAuth> firebaseAuthProvider;

  public AuthRepository_Factory(Provider<Context> appContextProvider,
      Provider<FirebaseAuth> firebaseAuthProvider) {
    this.appContextProvider = appContextProvider;
    this.firebaseAuthProvider = firebaseAuthProvider;
  }

  @Override
  public AuthRepository get() {
    return newInstance(appContextProvider.get(), firebaseAuthProvider.get());
  }

  public static AuthRepository_Factory create(javax.inject.Provider<Context> appContextProvider,
      javax.inject.Provider<FirebaseAuth> firebaseAuthProvider) {
    return new AuthRepository_Factory(Providers.asDaggerProvider(appContextProvider), Providers.asDaggerProvider(firebaseAuthProvider));
  }

  public static AuthRepository_Factory create(Provider<Context> appContextProvider,
      Provider<FirebaseAuth> firebaseAuthProvider) {
    return new AuthRepository_Factory(appContextProvider, firebaseAuthProvider);
  }

  public static AuthRepository newInstance(Context appContext, FirebaseAuth firebaseAuth) {
    return new AuthRepository(appContext, firebaseAuth);
  }
}
