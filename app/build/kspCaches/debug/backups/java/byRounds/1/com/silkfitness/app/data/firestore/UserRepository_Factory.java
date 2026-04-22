package com.silkfitness.app.data.firestore;

import com.google.firebase.firestore.FirebaseFirestore;
import com.silkfitness.app.data.auth.AuthRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.Providers;
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
public final class UserRepository_Factory implements Factory<UserRepository> {
  private final Provider<FirebaseFirestore> firestoreProvider;

  private final Provider<AuthRepository> authProvider;

  public UserRepository_Factory(Provider<FirebaseFirestore> firestoreProvider,
      Provider<AuthRepository> authProvider) {
    this.firestoreProvider = firestoreProvider;
    this.authProvider = authProvider;
  }

  @Override
  public UserRepository get() {
    return newInstance(firestoreProvider.get(), authProvider.get());
  }

  public static UserRepository_Factory create(
      javax.inject.Provider<FirebaseFirestore> firestoreProvider,
      javax.inject.Provider<AuthRepository> authProvider) {
    return new UserRepository_Factory(Providers.asDaggerProvider(firestoreProvider), Providers.asDaggerProvider(authProvider));
  }

  public static UserRepository_Factory create(Provider<FirebaseFirestore> firestoreProvider,
      Provider<AuthRepository> authProvider) {
    return new UserRepository_Factory(firestoreProvider, authProvider);
  }

  public static UserRepository newInstance(FirebaseFirestore firestore, AuthRepository auth) {
    return new UserRepository(firestore, auth);
  }
}
