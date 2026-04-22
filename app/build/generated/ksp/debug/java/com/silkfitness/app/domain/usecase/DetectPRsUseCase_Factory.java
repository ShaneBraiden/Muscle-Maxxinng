package com.silkfitness.app.domain.usecase;

import com.silkfitness.app.data.firestore.PRRepository;
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
public final class DetectPRsUseCase_Factory implements Factory<DetectPRsUseCase> {
  private final Provider<PRRepository> prRepositoryProvider;

  public DetectPRsUseCase_Factory(Provider<PRRepository> prRepositoryProvider) {
    this.prRepositoryProvider = prRepositoryProvider;
  }

  @Override
  public DetectPRsUseCase get() {
    return newInstance(prRepositoryProvider.get());
  }

  public static DetectPRsUseCase_Factory create(
      javax.inject.Provider<PRRepository> prRepositoryProvider) {
    return new DetectPRsUseCase_Factory(Providers.asDaggerProvider(prRepositoryProvider));
  }

  public static DetectPRsUseCase_Factory create(Provider<PRRepository> prRepositoryProvider) {
    return new DetectPRsUseCase_Factory(prRepositoryProvider);
  }

  public static DetectPRsUseCase newInstance(PRRepository prRepository) {
    return new DetectPRsUseCase(prRepository);
  }
}
