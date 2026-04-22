package com.silkfitness.app.ui.dashboard;

import com.silkfitness.app.data.firestore.BodyweightRepository;
import com.silkfitness.app.data.firestore.PRRepository;
import com.silkfitness.app.data.firestore.RoutineRepository;
import com.silkfitness.app.data.firestore.UserRepository;
import com.silkfitness.app.data.firestore.WorkoutRepository;
import com.silkfitness.app.data.local.ExerciseLibrary;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.Providers;
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
public final class DashboardViewModel_Factory implements Factory<DashboardViewModel> {
  private final Provider<UserRepository> userRepositoryProvider;

  private final Provider<RoutineRepository> routineRepositoryProvider;

  private final Provider<WorkoutRepository> workoutRepositoryProvider;

  private final Provider<BodyweightRepository> bodyweightRepositoryProvider;

  private final Provider<PRRepository> prRepositoryProvider;

  private final Provider<ExerciseLibrary> libraryProvider;

  public DashboardViewModel_Factory(Provider<UserRepository> userRepositoryProvider,
      Provider<RoutineRepository> routineRepositoryProvider,
      Provider<WorkoutRepository> workoutRepositoryProvider,
      Provider<BodyweightRepository> bodyweightRepositoryProvider,
      Provider<PRRepository> prRepositoryProvider, Provider<ExerciseLibrary> libraryProvider) {
    this.userRepositoryProvider = userRepositoryProvider;
    this.routineRepositoryProvider = routineRepositoryProvider;
    this.workoutRepositoryProvider = workoutRepositoryProvider;
    this.bodyweightRepositoryProvider = bodyweightRepositoryProvider;
    this.prRepositoryProvider = prRepositoryProvider;
    this.libraryProvider = libraryProvider;
  }

  @Override
  public DashboardViewModel get() {
    return newInstance(userRepositoryProvider.get(), routineRepositoryProvider.get(), workoutRepositoryProvider.get(), bodyweightRepositoryProvider.get(), prRepositoryProvider.get(), libraryProvider.get());
  }

  public static DashboardViewModel_Factory create(
      javax.inject.Provider<UserRepository> userRepositoryProvider,
      javax.inject.Provider<RoutineRepository> routineRepositoryProvider,
      javax.inject.Provider<WorkoutRepository> workoutRepositoryProvider,
      javax.inject.Provider<BodyweightRepository> bodyweightRepositoryProvider,
      javax.inject.Provider<PRRepository> prRepositoryProvider,
      javax.inject.Provider<ExerciseLibrary> libraryProvider) {
    return new DashboardViewModel_Factory(Providers.asDaggerProvider(userRepositoryProvider), Providers.asDaggerProvider(routineRepositoryProvider), Providers.asDaggerProvider(workoutRepositoryProvider), Providers.asDaggerProvider(bodyweightRepositoryProvider), Providers.asDaggerProvider(prRepositoryProvider), Providers.asDaggerProvider(libraryProvider));
  }

  public static DashboardViewModel_Factory create(Provider<UserRepository> userRepositoryProvider,
      Provider<RoutineRepository> routineRepositoryProvider,
      Provider<WorkoutRepository> workoutRepositoryProvider,
      Provider<BodyweightRepository> bodyweightRepositoryProvider,
      Provider<PRRepository> prRepositoryProvider, Provider<ExerciseLibrary> libraryProvider) {
    return new DashboardViewModel_Factory(userRepositoryProvider, routineRepositoryProvider, workoutRepositoryProvider, bodyweightRepositoryProvider, prRepositoryProvider, libraryProvider);
  }

  public static DashboardViewModel newInstance(UserRepository userRepository,
      RoutineRepository routineRepository, WorkoutRepository workoutRepository,
      BodyweightRepository bodyweightRepository, PRRepository prRepository,
      ExerciseLibrary library) {
    return new DashboardViewModel(userRepository, routineRepository, workoutRepository, bodyweightRepository, prRepository, library);
  }
}
