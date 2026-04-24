package com.musclemax.app.data.remote;

import com.musclemax.app.data.auth.AuthRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.Providers;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import io.github.jan.supabase.SupabaseClient;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
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
public final class RoutineRepository_Factory implements Factory<RoutineRepository> {
  private final Provider<SupabaseClient> supabaseProvider;

  private final Provider<AuthRepository> authProvider;

  public RoutineRepository_Factory(Provider<SupabaseClient> supabaseProvider,
      Provider<AuthRepository> authProvider) {
    this.supabaseProvider = supabaseProvider;
    this.authProvider = authProvider;
  }

  @Override
  public RoutineRepository get() {
    return newInstance(supabaseProvider.get(), authProvider.get());
  }

  public static RoutineRepository_Factory create(
      javax.inject.Provider<SupabaseClient> supabaseProvider,
      javax.inject.Provider<AuthRepository> authProvider) {
    return new RoutineRepository_Factory(Providers.asDaggerProvider(supabaseProvider), Providers.asDaggerProvider(authProvider));
  }

  public static RoutineRepository_Factory create(Provider<SupabaseClient> supabaseProvider,
      Provider<AuthRepository> authProvider) {
    return new RoutineRepository_Factory(supabaseProvider, authProvider);
  }

  public static RoutineRepository newInstance(SupabaseClient supabase, AuthRepository auth) {
    return new RoutineRepository(supabase, auth);
  }
}
