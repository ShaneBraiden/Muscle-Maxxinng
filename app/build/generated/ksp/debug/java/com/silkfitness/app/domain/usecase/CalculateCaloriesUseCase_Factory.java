package com.silkfitness.app.domain.usecase;

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
public final class CalculateCaloriesUseCase_Factory implements Factory<CalculateCaloriesUseCase> {
  private final Provider<ExerciseLibrary> libraryProvider;

  public CalculateCaloriesUseCase_Factory(Provider<ExerciseLibrary> libraryProvider) {
    this.libraryProvider = libraryProvider;
  }

  @Override
  public CalculateCaloriesUseCase get() {
    return newInstance(libraryProvider.get());
  }

  public static CalculateCaloriesUseCase_Factory create(
      javax.inject.Provider<ExerciseLibrary> libraryProvider) {
    return new CalculateCaloriesUseCase_Factory(Providers.asDaggerProvider(libraryProvider));
  }

  public static CalculateCaloriesUseCase_Factory create(Provider<ExerciseLibrary> libraryProvider) {
    return new CalculateCaloriesUseCase_Factory(libraryProvider);
  }

  public static CalculateCaloriesUseCase newInstance(ExerciseLibrary library) {
    return new CalculateCaloriesUseCase(library);
  }
}
