package com.silkfitness.app.ui.routines;

import androidx.lifecycle.SavedStateHandle;
import com.silkfitness.app.data.firestore.RoutineRepository;
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
public final class RoutineEditorViewModel_Factory implements Factory<RoutineEditorViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<RoutineRepository> routineRepositoryProvider;

  private final Provider<ExerciseLibrary> libraryProvider;

  public RoutineEditorViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<RoutineRepository> routineRepositoryProvider,
      Provider<ExerciseLibrary> libraryProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.routineRepositoryProvider = routineRepositoryProvider;
    this.libraryProvider = libraryProvider;
  }

  @Override
  public RoutineEditorViewModel get() {
    return newInstance(savedStateHandleProvider.get(), routineRepositoryProvider.get(), libraryProvider.get());
  }

  public static RoutineEditorViewModel_Factory create(
      javax.inject.Provider<SavedStateHandle> savedStateHandleProvider,
      javax.inject.Provider<RoutineRepository> routineRepositoryProvider,
      javax.inject.Provider<ExerciseLibrary> libraryProvider) {
    return new RoutineEditorViewModel_Factory(Providers.asDaggerProvider(savedStateHandleProvider), Providers.asDaggerProvider(routineRepositoryProvider), Providers.asDaggerProvider(libraryProvider));
  }

  public static RoutineEditorViewModel_Factory create(
      Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<RoutineRepository> routineRepositoryProvider,
      Provider<ExerciseLibrary> libraryProvider) {
    return new RoutineEditorViewModel_Factory(savedStateHandleProvider, routineRepositoryProvider, libraryProvider);
  }

  public static RoutineEditorViewModel newInstance(SavedStateHandle savedStateHandle,
      RoutineRepository routineRepository, ExerciseLibrary library) {
    return new RoutineEditorViewModel(savedStateHandle, routineRepository, library);
  }
}
