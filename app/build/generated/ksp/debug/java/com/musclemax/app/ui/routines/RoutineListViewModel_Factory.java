package com.musclemax.app.ui.routines;

import com.musclemax.app.data.remote.RoutineRepository;
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
public final class RoutineListViewModel_Factory implements Factory<RoutineListViewModel> {
  private final Provider<RoutineRepository> routineRepositoryProvider;

  public RoutineListViewModel_Factory(Provider<RoutineRepository> routineRepositoryProvider) {
    this.routineRepositoryProvider = routineRepositoryProvider;
  }

  @Override
  public RoutineListViewModel get() {
    return newInstance(routineRepositoryProvider.get());
  }

  public static RoutineListViewModel_Factory create(
      javax.inject.Provider<RoutineRepository> routineRepositoryProvider) {
    return new RoutineListViewModel_Factory(Providers.asDaggerProvider(routineRepositoryProvider));
  }

  public static RoutineListViewModel_Factory create(
      Provider<RoutineRepository> routineRepositoryProvider) {
    return new RoutineListViewModel_Factory(routineRepositoryProvider);
  }

  public static RoutineListViewModel newInstance(RoutineRepository routineRepository) {
    return new RoutineListViewModel(routineRepository);
  }
}
