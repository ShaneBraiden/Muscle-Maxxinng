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
public final class WorkoutRepository_Factory implements Factory<WorkoutRepository> {
  private final Provider<FirebaseFirestore> firestoreProvider;

  private final Provider<AuthRepository> authProvider;

  public WorkoutRepository_Factory(Provider<FirebaseFirestore> firestoreProvider,
      Provider<AuthRepository> authProvider) {
    this.firestoreProvider = firestoreProvider;
    this.authProvider = authProvider;
  }

  @Override
  public WorkoutRepository get() {
    return newInstance(firestoreProvider.get(), authProvider.get());
  }

  public static WorkoutRepository_Factory create(
      javax.inject.Provider<FirebaseFirestore> firestoreProvider,
      javax.inject.Provider<AuthRepository> authProvider) {
    return new WorkoutRepository_Factory(Providers.asDaggerProvider(firestoreProvider), Providers.asDaggerProvider(authProvider));
  }

  public static WorkoutRepository_Factory create(Provider<FirebaseFirestore> firestoreProvider,
      Provider<AuthRepository> authProvider) {
    return new WorkoutRepository_Factory(firestoreProvider, authProvider);
  }

  public static WorkoutRepository newInstance(FirebaseFirestore firestore, AuthRepository auth) {
    return new WorkoutRepository(firestore, auth);
  }
}
