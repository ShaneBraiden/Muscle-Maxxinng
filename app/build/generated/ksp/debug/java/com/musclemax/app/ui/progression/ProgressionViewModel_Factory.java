package com.musclemax.app.ui.progression;

import com.musclemax.app.data.local.ExerciseLibrary;
import com.musclemax.app.data.remote.PRRepository;
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
public final class ProgressionViewModel_Factory implements Factory<ProgressionViewModel> {
  private final Provider<PRRepository> prRepositoryProvider;

  private final Provider<ExerciseLibrary> libraryProvider;

  public ProgressionViewModel_Factory(Provider<PRRepository> prRepositoryProvider,
      Provider<ExerciseLibrary> libraryProvider) {
    this.prRepositoryProvider = prRepositoryProvider;
    this.libraryProvider = libraryProvider;
  }

  @Override
  public ProgressionViewModel get() {
    return newInstance(prRepositoryProvider.get(), libraryProvider.get());
  }

  public static ProgressionViewModel_Factory create(
      javax.inject.Provider<PRRepository> prRepositoryProvider,
      javax.inject.Provider<ExerciseLibrary> libraryProvider) {
    return new ProgressionViewModel_Factory(Providers.asDaggerProvider(prRepositoryProvider), Providers.asDaggerProvider(libraryProvider));
  }

  public static ProgressionViewModel_Factory create(Provider<PRRepository> prRepositoryProvider,
      Provider<ExerciseLibrary> libraryProvider) {
    return new ProgressionViewModel_Factory(prRepositoryProvider, libraryProvider);
  }

  public static ProgressionViewModel newInstance(PRRepository prRepository,
      ExerciseLibrary library) {
    return new ProgressionViewModel(prRepository, library);
  }
}
