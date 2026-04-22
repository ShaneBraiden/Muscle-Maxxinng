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
public final class PRRepository_Factory implements Factory<PRRepository> {
  private final Provider<FirebaseFirestore> firestoreProvider;

  private final Provider<AuthRepository> authProvider;

  public PRRepository_Factory(Provider<FirebaseFirestore> firestoreProvider,
      Provider<AuthRepository> authProvider) {
    this.firestoreProvider = firestoreProvider;
    this.authProvider = authProvider;
  }

  @Override
  public PRRepository get() {
    return newInstance(firestoreProvider.get(), authProvider.get());
  }

  public static PRRepository_Factory create(
      javax.inject.Provider<FirebaseFirestore> firestoreProvider,
      javax.inject.Provider<AuthRepository> authProvider) {
    return new PRRepository_Factory(Providers.asDaggerProvider(firestoreProvider), Providers.asDaggerProvider(authProvider));
  }

  public static PRRepository_Factory create(Provider<FirebaseFirestore> firestoreProvider,
      Provider<AuthRepository> authProvider) {
    return new PRRepository_Factory(firestoreProvider, authProvider);
  }

  public static PRRepository newInstance(FirebaseFirestore firestore, AuthRepository auth) {
    return new PRRepository(firestore, auth);
  }
}
