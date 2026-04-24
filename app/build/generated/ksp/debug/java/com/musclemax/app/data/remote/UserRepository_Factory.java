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
public final class UserRepository_Factory implements Factory<UserRepository> {
  private final Provider<SupabaseClient> supabaseProvider;

  private final Provider<AuthRepository> authProvider;

  public UserRepository_Factory(Provider<SupabaseClient> supabaseProvider,
      Provider<AuthRepository> authProvider) {
    this.supabaseProvider = supabaseProvider;
    this.authProvider = authProvider;
  }

  @Override
  public UserRepository get() {
    return newInstance(supabaseProvider.get(), authProvider.get());
  }

  public static UserRepository_Factory create(
      javax.inject.Provider<SupabaseClient> supabaseProvider,
      javax.inject.Provider<AuthRepository> authProvider) {
    return new UserRepository_Factory(Providers.asDaggerProvider(supabaseProvider), Providers.asDaggerProvider(authProvider));
  }

  public static UserRepository_Factory create(Provider<SupabaseClient> supabaseProvider,
      Provider<AuthRepository> authProvider) {
    return new UserRepository_Factory(supabaseProvider, authProvider);
  }

  public static UserRepository newInstance(SupabaseClient supabase, AuthRepository auth) {
    return new UserRepository(supabase, auth);
  }
}
