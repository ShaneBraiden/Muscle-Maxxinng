package com.musclemax.app.domain.usecase;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class ComputeVolumeUseCase_Factory implements Factory<ComputeVolumeUseCase> {
  @Override
  public ComputeVolumeUseCase get() {
    return newInstance();
  }

  public static ComputeVolumeUseCase_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static ComputeVolumeUseCase newInstance() {
    return new ComputeVolumeUseCase();
  }

  private static final class InstanceHolder {
    static final ComputeVolumeUseCase_Factory INSTANCE = new ComputeVolumeUseCase_Factory();
  }
}
