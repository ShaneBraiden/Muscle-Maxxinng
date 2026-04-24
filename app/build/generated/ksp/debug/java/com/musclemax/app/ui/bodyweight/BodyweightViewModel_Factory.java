package com.musclemax.app.ui.bodyweight;

import com.musclemax.app.data.remote.BodyweightRepository;
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
public final class BodyweightViewModel_Factory implements Factory<BodyweightViewModel> {
  private final Provider<BodyweightRepository> repoProvider;

  public BodyweightViewModel_Factory(Provider<BodyweightRepository> repoProvider) {
    this.repoProvider = repoProvider;
  }

  @Override
  public BodyweightViewModel get() {
    return newInstance(repoProvider.get());
  }

  public static BodyweightViewModel_Factory create(
      javax.inject.Provider<BodyweightRepository> repoProvider) {
    return new BodyweightViewModel_Factory(Providers.asDaggerProvider(repoProvider));
  }

  public static BodyweightViewModel_Factory create(Provider<BodyweightRepository> repoProvider) {
    return new BodyweightViewModel_Factory(repoProvider);
  }

  public static BodyweightViewModel newInstance(BodyweightRepository repo) {
    return new BodyweightViewModel(repo);
  }
}
