package com.musclemax.app.ui.history;

import androidx.lifecycle.SavedStateHandle;
import com.musclemax.app.data.local.ExerciseLibrary;
import com.musclemax.app.data.remote.WorkoutRepository;
import com.musclemax.app.domain.usecase.GetLastSetForExerciseUseCase;
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
public final class WorkoutDetailViewModel_Factory implements Factory<WorkoutDetailViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<WorkoutRepository> workoutRepositoryProvider;

  private final Provider<ExerciseLibrary> libraryProvider;

  private final Provider<GetLastSetForExerciseUseCase> getLastSetProvider;

  public WorkoutDetailViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<WorkoutRepository> workoutRepositoryProvider,
      Provider<ExerciseLibrary> libraryProvider,
      Provider<GetLastSetForExerciseUseCase> getLastSetProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.workoutRepositoryProvider = workoutRepositoryProvider;
    this.libraryProvider = libraryProvider;
    this.getLastSetProvider = getLastSetProvider;
  }

  @Override
  public WorkoutDetailViewModel get() {
    return newInstance(savedStateHandleProvider.get(), workoutRepositoryProvider.get(), libraryProvider.get(), getLastSetProvider.get());
  }

  public static WorkoutDetailViewModel_Factory create(
      javax.inject.Provider<SavedStateHandle> savedStateHandleProvider,
      javax.inject.Provider<WorkoutRepository> workoutRepositoryProvider,
      javax.inject.Provider<ExerciseLibrary> libraryProvider,
      javax.inject.Provider<GetLastSetForExerciseUseCase> getLastSetProvider) {
    return new WorkoutDetailViewModel_Factory(Providers.asDaggerProvider(savedStateHandleProvider), Providers.asDaggerProvider(workoutRepositoryProvider), Providers.asDaggerProvider(libraryProvider), Providers.asDaggerProvider(getLastSetProvider));
  }

  public static WorkoutDetailViewModel_Factory create(
      Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<WorkoutRepository> workoutRepositoryProvider,
      Provider<ExerciseLibrary> libraryProvider,
      Provider<GetLastSetForExerciseUseCase> getLastSetProvider) {
    return new WorkoutDetailViewModel_Factory(savedStateHandleProvider, workoutRepositoryProvider, libraryProvider, getLastSetProvider);
  }

  public static WorkoutDetailViewModel newInstance(SavedStateHandle savedStateHandle,
      WorkoutRepository workoutRepository, ExerciseLibrary library,
      GetLastSetForExerciseUseCase getLastSet) {
    return new WorkoutDetailViewModel(savedStateHandle, workoutRepository, library, getLastSet);
  }
}
