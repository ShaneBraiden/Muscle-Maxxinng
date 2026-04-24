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
public final class BodyweightRepository_Factory implements Factory<BodyweightRepository> {
  private final Provider<SupabaseClient> supabaseProvider;

  private final Provider<AuthRepository> authProvider;

  public BodyweightRepository_Factory(Provider<SupabaseClient> supabaseProvider,
      Provider<AuthRepository> authProvider) {
    this.supabaseProvider = supabaseProvider;
    this.authProvider = authProvider;
  }

  @Override
  public BodyweightRepository get() {
    return newInstance(supabaseProvider.get(), authProvider.get());
  }

  public static BodyweightRepository_Factory create(
      javax.inject.Provider<SupabaseClient> supabaseProvider,
      javax.inject.Provider<AuthRepository> authProvider) {
    return new BodyweightRepository_Factory(Providers.asDaggerProvider(supabaseProvider), Providers.asDaggerProvider(authProvider));
  }

  public static BodyweightRepository_Factory create(Provider<SupabaseClient> supabaseProvider,
      Provider<AuthRepository> authProvider) {
    return new BodyweightRepository_Factory(supabaseProvider, authProvider);
  }

  public static BodyweightRepository newInstance(SupabaseClient supabase, AuthRepository auth) {
    return new BodyweightRepository(supabase, auth);
  }
}
