package com.musclemax.app.ui.log;

import com.musclemax.app.data.local.ExerciseLibrary;
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
public final class ExercisePickerViewModel_Factory implements Factory<ExercisePickerViewModel> {
  private final Provider<ExerciseLibrary> libraryProvider;

  public ExercisePickerViewModel_Factory(Provider<ExerciseLibrary> libraryProvider) {
    this.libraryProvider = libraryProvider;
  }

  @Override
  public ExercisePickerViewModel get() {
    return newInstance(libraryProvider.get());
  }

  public static ExercisePickerViewModel_Factory create(
      javax.inject.Provider<ExerciseLibrary> libraryProvider) {
    return new ExercisePickerViewModel_Factory(Providers.asDaggerProvider(libraryProvider));
  }

  public static ExercisePickerViewModel_Factory create(Provider<ExerciseLibrary> libraryProvider) {
    return new ExercisePickerViewModel_Factory(libraryProvider);
  }

  public static ExercisePickerViewModel newInstance(ExerciseLibrary library) {
    return new ExercisePickerViewModel(library);
  }
}
