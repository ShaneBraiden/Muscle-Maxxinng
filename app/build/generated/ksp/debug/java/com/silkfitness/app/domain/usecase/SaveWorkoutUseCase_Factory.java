package com.silkfitness.app.domain.usecase;

import com.silkfitness.app.data.firestore.BodyweightRepository;
import com.silkfitness.app.data.firestore.UserRepository;
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
public final class SaveWorkoutUseCase_Factory implements Factory<SaveWorkoutUseCase> {
  private final Provider<WorkoutRepository> workoutRepositoryProvider;

  private final Provider<BodyweightRepository> bodyweightRepositoryProvider;

  private final Provider<UserRepository> userRepositoryProvider;

  private final Provider<CalculateCaloriesUseCase> calculateCaloriesProvider;

  private final Provider<ComputeVolumeUseCase> computeVolumeProvider;

  private final Provider<DetectPRsUseCase> detectPRsProvider;

  public SaveWorkoutUseCase_Factory(Provider<WorkoutRepository> workoutRepositoryProvider,
      Provider<BodyweightRepository> bodyweightRepositoryProvider,
      Provider<UserRepository> userRepositoryProvider,
      Provider<CalculateCaloriesUseCase> calculateCaloriesProvider,
      Provider<ComputeVolumeUseCase> computeVolumeProvider,
      Provider<DetectPRsUseCase> detectPRsProvider) {
    this.workoutRepositoryProvider = workoutRepositoryProvider;
    this.bodyweightRepositoryProvider = bodyweightRepositoryProvider;
    this.userRepositoryProvider = userRepositoryProvider;
    this.calculateCaloriesProvider = calculateCaloriesProvider;
    this.computeVolumeProvider = computeVolumeProvider;
    this.detectPRsProvider = detectPRsProvider;
  }

  @Override
  public SaveWorkoutUseCase get() {
    return newInstance(workoutRepositoryProvider.get(), bodyweightRepositoryProvider.get(), userRepositoryProvider.get(), calculateCaloriesProvider.get(), computeVolumeProvider.get(), detectPRsProvider.get());
  }

  public static SaveWorkoutUseCase_Factory create(
      javax.inject.Provider<WorkoutRepository> workoutRepositoryProvider,
      javax.inject.Provider<BodyweightRepository> bodyweightRepositoryProvider,
      javax.inject.Provider<UserRepository> userRepositoryProvider,
      javax.inject.Provider<CalculateCaloriesUseCase> calculateCaloriesProvider,
      javax.inject.Provider<ComputeVolumeUseCase> computeVolumeProvider,
      javax.inject.Provider<DetectPRsUseCase> detectPRsProvider) {
    return new SaveWorkoutUseCase_Factory(Providers.asDaggerProvider(workoutRepositoryProvider), Providers.asDaggerProvider(bodyweightRepositoryProvider), Providers.asDaggerProvider(userRepositoryProvider), Providers.asDaggerProvider(calculateCaloriesProvider), Providers.asDaggerProvider(computeVolumeProvider), Providers.asDaggerProvider(detectPRsProvider));
  }

  public static SaveWorkoutUseCase_Factory create(
      Provider<WorkoutRepository> workoutRepositoryProvider,
      Provider<BodyweightRepository> bodyweightRepositoryProvider,
      Provider<UserRepository> userRepositoryProvider,
      Provider<CalculateCaloriesUseCase> calculateCaloriesProvider,
      Provider<ComputeVolumeUseCase> computeVolumeProvider,
      Provider<DetectPRsUseCase> detectPRsProvider) {
    return new SaveWorkoutUseCase_Factory(workoutRepositoryProvider, bodyweightRepositoryProvider, userRepositoryProvider, calculateCaloriesProvider, computeVolumeProvider, detectPRsProvider);
  }

  public static SaveWorkoutUseCase newInstance(WorkoutRepository workoutRepository,
      BodyweightRepository bodyweightRepository, UserRepository userRepository,
      CalculateCaloriesUseCase calculateCalories, ComputeVolumeUseCase computeVolume,
      DetectPRsUseCase detectPRs) {
    return new SaveWorkoutUseCase(workoutRepository, bodyweightRepository, userRepository, calculateCalories, computeVolume, detectPRs);
  }
}
