package com.silkfitness.app.ui.log;

import androidx.lifecycle.SavedStateHandle;
import com.silkfitness.app.data.firestore.RoutineRepository;
import com.silkfitness.app.data.local.ExerciseLibrary;
import com.silkfitness.app.domain.usecase.GetLastSetForExerciseUseCase;
import com.silkfitness.app.domain.usecase.SaveWorkoutUseCase;
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
public final class LogWorkoutViewModel_Factory implements Factory<LogWorkoutViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<RoutineRepository> routineRepositoryProvider;

  private final Provider<ExerciseLibrary> libraryProvider;

  private final Provider<GetLastSetForExerciseUseCase> getLastSetProvider;

  private final Provider<SaveWorkoutUseCase> saveWorkoutProvider;

  public LogWorkoutViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<RoutineRepository> routineRepositoryProvider,
      Provider<ExerciseLibrary> libraryProvider,
      Provider<GetLastSetForExerciseUseCase> getLastSetProvider,
      Provider<SaveWorkoutUseCase> saveWorkoutProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.routineRepositoryProvider = routineRepositoryProvider;
    this.libraryProvider = libraryProvider;
    this.getLastSetProvider = getLastSetProvider;
    this.saveWorkoutProvider = saveWorkoutProvider;
  }

  @Override
  public LogWorkoutViewModel get() {
    return newInstance(savedStateHandleProvider.get(), routineRepositoryProvider.get(), libraryProvider.get(), getLastSetProvider.get(), saveWorkoutProvider.get());
  }

  public static LogWorkoutViewModel_Factory create(
      javax.inject.Provider<SavedStateHandle> savedStateHandleProvider,
      javax.inject.Provider<RoutineRepository> routineRepositoryProvider,
      javax.inject.Provider<ExerciseLibrary> libraryProvider,
      javax.inject.Provider<GetLastSetForExerciseUseCase> getLastSetProvider,
      javax.inject.Provider<SaveWorkoutUseCase> saveWorkoutProvider) {
    return new LogWorkoutViewModel_Factory(Providers.asDaggerProvider(savedStateHandleProvider), Providers.asDaggerProvider(routineRepositoryProvider), Providers.asDaggerProvider(libraryProvider), Providers.asDaggerProvider(getLastSetProvider), Providers.asDaggerProvider(saveWorkoutProvider));
  }

  public static LogWorkoutViewModel_Factory create(
      Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<RoutineRepository> routineRepositoryProvider,
      Provider<ExerciseLibrary> libraryProvider,
      Provider<GetLastSetForExerciseUseCase> getLastSetProvider,
      Provider<SaveWorkoutUseCase> saveWorkoutProvider) {
    return new LogWorkoutViewModel_Factory(savedStateHandleProvider, routineRepositoryProvider, libraryProvider, getLastSetProvider, saveWorkoutProvider);
  }

  public static LogWorkoutViewModel newInstance(SavedStateHandle savedStateHandle,
      RoutineRepository routineRepository, ExerciseLibrary library,
      GetLastSetForExerciseUseCase getLastSet, SaveWorkoutUseCase saveWorkout) {
    return new LogWorkoutViewModel(savedStateHandle, routineRepository, library, getLastSet, saveWorkout);
  }
}
