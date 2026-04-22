package com.silkfitness.app.domain.usecase;

import com.silkfitness.app.data.firestore.WorkoutRepository;
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
public final class GetLastSetForExerciseUseCase_Factory implements Factory<GetLastSetForExerciseUseCase> {
  private final Provider<WorkoutRepository> workoutRepositoryProvider;

  public GetLastSetForExerciseUseCase_Factory(
      Provider<WorkoutRepository> workoutRepositoryProvider) {
    this.workoutRepositoryProvider = workoutRepositoryProvider;
  }

  @Override
  public GetLastSetForExerciseUseCase get() {
    return newInstance(workoutRepositoryProvider.get());
  }

  public static GetLastSetForExerciseUseCase_Factory create(
      javax.inject.Provider<WorkoutRepository> workoutRepositoryProvider) {
    return new GetLastSetForExerciseUseCase_Factory(Providers.asDaggerProvider(workoutRepositoryProvider));
  }

  public static GetLastSetForExerciseUseCase_Factory create(
      Provider<WorkoutRepository> workoutRepositoryProvider) {
    return new GetLastSetForExerciseUseCase_Factory(workoutRepositoryProvider);
  }

  public static GetLastSetForExerciseUseCase newInstance(WorkoutRepository workoutRepository) {
    return new GetLastSetForExerciseUseCase(workoutRepository);
  }
}
