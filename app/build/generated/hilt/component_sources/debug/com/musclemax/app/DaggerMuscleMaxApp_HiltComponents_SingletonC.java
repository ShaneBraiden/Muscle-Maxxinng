package com.musclemax.app;

import android.app.Activity;
import android.app.Service;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import com.musclemax.app.data.auth.AuthRepository;
import com.musclemax.app.data.local.ExerciseLibrary;
import com.musclemax.app.data.remote.BodyweightRepository;
import com.musclemax.app.data.remote.PRRepository;
import com.musclemax.app.data.remote.RoutineRepository;
import com.musclemax.app.data.remote.UserRepository;
import com.musclemax.app.data.remote.WorkoutRepository;
import com.musclemax.app.di.SupabaseModule_ProvideSupabaseClientFactory;
import com.musclemax.app.domain.usecase.CalculateCaloriesUseCase;
import com.musclemax.app.domain.usecase.ComputeVolumeUseCase;
import com.musclemax.app.domain.usecase.DetectPRsUseCase;
import com.musclemax.app.domain.usecase.GetLastSetForExerciseUseCase;
import com.musclemax.app.domain.usecase.SaveWorkoutUseCase;
import com.musclemax.app.ui.auth.AuthViewModel;
import com.musclemax.app.ui.auth.AuthViewModel_HiltModules;
import com.musclemax.app.ui.auth.AuthViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.musclemax.app.ui.auth.AuthViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.musclemax.app.ui.bodyweight.BodyweightViewModel;
import com.musclemax.app.ui.bodyweight.BodyweightViewModel_HiltModules;
import com.musclemax.app.ui.bodyweight.BodyweightViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.musclemax.app.ui.bodyweight.BodyweightViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.musclemax.app.ui.dashboard.DashboardViewModel;
import com.musclemax.app.ui.dashboard.DashboardViewModel_HiltModules;
import com.musclemax.app.ui.dashboard.DashboardViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.musclemax.app.ui.dashboard.DashboardViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.musclemax.app.ui.history.HistoryViewModel;
import com.musclemax.app.ui.history.HistoryViewModel_HiltModules;
import com.musclemax.app.ui.history.HistoryViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.musclemax.app.ui.history.HistoryViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.musclemax.app.ui.history.WorkoutDetailViewModel;
import com.musclemax.app.ui.history.WorkoutDetailViewModel_HiltModules;
import com.musclemax.app.ui.history.WorkoutDetailViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.musclemax.app.ui.history.WorkoutDetailViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.musclemax.app.ui.log.ExercisePickerViewModel;
import com.musclemax.app.ui.log.ExercisePickerViewModel_HiltModules;
import com.musclemax.app.ui.log.ExercisePickerViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.musclemax.app.ui.log.ExercisePickerViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.musclemax.app.ui.log.LogWorkoutViewModel;
import com.musclemax.app.ui.log.LogWorkoutViewModel_HiltModules;
import com.musclemax.app.ui.log.LogWorkoutViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.musclemax.app.ui.log.LogWorkoutViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.musclemax.app.ui.profile.ProfileViewModel;
import com.musclemax.app.ui.profile.ProfileViewModel_HiltModules;
import com.musclemax.app.ui.profile.ProfileViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.musclemax.app.ui.profile.ProfileViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.musclemax.app.ui.progression.ProgressionViewModel;
import com.musclemax.app.ui.progression.ProgressionViewModel_HiltModules;
import com.musclemax.app.ui.progression.ProgressionViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.musclemax.app.ui.progression.ProgressionViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.musclemax.app.ui.routines.RoutineEditorViewModel;
import com.musclemax.app.ui.routines.RoutineEditorViewModel_HiltModules;
import com.musclemax.app.ui.routines.RoutineEditorViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.musclemax.app.ui.routines.RoutineEditorViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.musclemax.app.ui.routines.RoutineListViewModel;
import com.musclemax.app.ui.routines.RoutineListViewModel_HiltModules;
import com.musclemax.app.ui.routines.RoutineListViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.musclemax.app.ui.routines.RoutineListViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.LazyClassKeyMap;
import dagger.internal.MapBuilder;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import io.github.jan.supabase.SupabaseClient;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

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
public final class DaggerMuscleMaxApp_HiltComponents_SingletonC {
  private DaggerMuscleMaxApp_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public MuscleMaxApp_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements MuscleMaxApp_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public MuscleMaxApp_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements MuscleMaxApp_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public MuscleMaxApp_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements MuscleMaxApp_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public MuscleMaxApp_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements MuscleMaxApp_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public MuscleMaxApp_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements MuscleMaxApp_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public MuscleMaxApp_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements MuscleMaxApp_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public MuscleMaxApp_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements MuscleMaxApp_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public MuscleMaxApp_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends MuscleMaxApp_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends MuscleMaxApp_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends MuscleMaxApp_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends MuscleMaxApp_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    @Override
    public void injectMainActivity(MainActivity arg0) {
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Map<Class<?>, Boolean> getViewModelKeys() {
      return LazyClassKeyMap.<Boolean>of(MapBuilder.<String, Boolean>newMapBuilder(11).put(AuthViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, AuthViewModel_HiltModules.KeyModule.provide()).put(BodyweightViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, BodyweightViewModel_HiltModules.KeyModule.provide()).put(DashboardViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, DashboardViewModel_HiltModules.KeyModule.provide()).put(ExercisePickerViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, ExercisePickerViewModel_HiltModules.KeyModule.provide()).put(HistoryViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, HistoryViewModel_HiltModules.KeyModule.provide()).put(LogWorkoutViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, LogWorkoutViewModel_HiltModules.KeyModule.provide()).put(ProfileViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, ProfileViewModel_HiltModules.KeyModule.provide()).put(ProgressionViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, ProgressionViewModel_HiltModules.KeyModule.provide()).put(RoutineEditorViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, RoutineEditorViewModel_HiltModules.KeyModule.provide()).put(RoutineListViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, RoutineListViewModel_HiltModules.KeyModule.provide()).put(WorkoutDetailViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, WorkoutDetailViewModel_HiltModules.KeyModule.provide()).build());
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }
  }

  private static final class ViewModelCImpl extends MuscleMaxApp_HiltComponents.ViewModelC {
    private final SavedStateHandle savedStateHandle;

    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<AuthViewModel> authViewModelProvider;

    private Provider<BodyweightViewModel> bodyweightViewModelProvider;

    private Provider<DashboardViewModel> dashboardViewModelProvider;

    private Provider<ExercisePickerViewModel> exercisePickerViewModelProvider;

    private Provider<HistoryViewModel> historyViewModelProvider;

    private Provider<LogWorkoutViewModel> logWorkoutViewModelProvider;

    private Provider<ProfileViewModel> profileViewModelProvider;

    private Provider<ProgressionViewModel> progressionViewModelProvider;

    private Provider<RoutineEditorViewModel> routineEditorViewModelProvider;

    private Provider<RoutineListViewModel> routineListViewModelProvider;

    private Provider<WorkoutDetailViewModel> workoutDetailViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.savedStateHandle = savedStateHandleParam;
      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    private GetLastSetForExerciseUseCase getLastSetForExerciseUseCase() {
      return new GetLastSetForExerciseUseCase(singletonCImpl.workoutRepositoryProvider.get());
    }

    private CalculateCaloriesUseCase calculateCaloriesUseCase() {
      return new CalculateCaloriesUseCase(singletonCImpl.exerciseLibraryProvider.get());
    }

    private DetectPRsUseCase detectPRsUseCase() {
      return new DetectPRsUseCase(singletonCImpl.pRRepositoryProvider.get());
    }

    private SaveWorkoutUseCase saveWorkoutUseCase() {
      return new SaveWorkoutUseCase(singletonCImpl.workoutRepositoryProvider.get(), singletonCImpl.bodyweightRepositoryProvider.get(), singletonCImpl.userRepositoryProvider.get(), calculateCaloriesUseCase(), new ComputeVolumeUseCase(), detectPRsUseCase());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.authViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.bodyweightViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.dashboardViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
      this.exercisePickerViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3);
      this.historyViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 4);
      this.logWorkoutViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 5);
      this.profileViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 6);
      this.progressionViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 7);
      this.routineEditorViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 8);
      this.routineListViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 9);
      this.workoutDetailViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 10);
    }

    @Override
    public Map<Class<?>, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return LazyClassKeyMap.<javax.inject.Provider<ViewModel>>of(MapBuilder.<String, javax.inject.Provider<ViewModel>>newMapBuilder(11).put(AuthViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) authViewModelProvider)).put(BodyweightViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) bodyweightViewModelProvider)).put(DashboardViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) dashboardViewModelProvider)).put(ExercisePickerViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) exercisePickerViewModelProvider)).put(HistoryViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) historyViewModelProvider)).put(LogWorkoutViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) logWorkoutViewModelProvider)).put(ProfileViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) profileViewModelProvider)).put(ProgressionViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) progressionViewModelProvider)).put(RoutineEditorViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) routineEditorViewModelProvider)).put(RoutineListViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) routineListViewModelProvider)).put(WorkoutDetailViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) workoutDetailViewModelProvider)).build());
    }

    @Override
    public Map<Class<?>, Object> getHiltViewModelAssistedMap() {
      return Collections.<Class<?>, Object>emptyMap();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.musclemax.app.ui.auth.AuthViewModel 
          return (T) new AuthViewModel(singletonCImpl.authRepositoryProvider.get(), singletonCImpl.userRepositoryProvider.get());

          case 1: // com.musclemax.app.ui.bodyweight.BodyweightViewModel 
          return (T) new BodyweightViewModel(singletonCImpl.bodyweightRepositoryProvider.get());

          case 2: // com.musclemax.app.ui.dashboard.DashboardViewModel 
          return (T) new DashboardViewModel(singletonCImpl.userRepositoryProvider.get(), singletonCImpl.routineRepositoryProvider.get(), singletonCImpl.workoutRepositoryProvider.get(), singletonCImpl.bodyweightRepositoryProvider.get(), singletonCImpl.pRRepositoryProvider.get(), singletonCImpl.exerciseLibraryProvider.get());

          case 3: // com.musclemax.app.ui.log.ExercisePickerViewModel 
          return (T) new ExercisePickerViewModel(singletonCImpl.exerciseLibraryProvider.get());

          case 4: // com.musclemax.app.ui.history.HistoryViewModel 
          return (T) new HistoryViewModel(singletonCImpl.workoutRepositoryProvider.get());

          case 5: // com.musclemax.app.ui.log.LogWorkoutViewModel 
          return (T) new LogWorkoutViewModel(viewModelCImpl.savedStateHandle, singletonCImpl.routineRepositoryProvider.get(), singletonCImpl.exerciseLibraryProvider.get(), viewModelCImpl.getLastSetForExerciseUseCase(), viewModelCImpl.saveWorkoutUseCase());

          case 6: // com.musclemax.app.ui.profile.ProfileViewModel 
          return (T) new ProfileViewModel(singletonCImpl.userRepositoryProvider.get());

          case 7: // com.musclemax.app.ui.progression.ProgressionViewModel 
          return (T) new ProgressionViewModel(singletonCImpl.pRRepositoryProvider.get(), singletonCImpl.exerciseLibraryProvider.get());

          case 8: // com.musclemax.app.ui.routines.RoutineEditorViewModel 
          return (T) new RoutineEditorViewModel(viewModelCImpl.savedStateHandle, singletonCImpl.routineRepositoryProvider.get(), singletonCImpl.exerciseLibraryProvider.get());

          case 9: // com.musclemax.app.ui.routines.RoutineListViewModel 
          return (T) new RoutineListViewModel(singletonCImpl.routineRepositoryProvider.get());

          case 10: // com.musclemax.app.ui.history.WorkoutDetailViewModel 
          return (T) new WorkoutDetailViewModel(viewModelCImpl.savedStateHandle, singletonCImpl.workoutRepositoryProvider.get(), singletonCImpl.exerciseLibraryProvider.get(), viewModelCImpl.getLastSetForExerciseUseCase());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends MuscleMaxApp_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends MuscleMaxApp_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }
  }

  private static final class SingletonCImpl extends MuscleMaxApp_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<SupabaseClient> provideSupabaseClientProvider;

    private Provider<AuthRepository> authRepositoryProvider;

    private Provider<UserRepository> userRepositoryProvider;

    private Provider<BodyweightRepository> bodyweightRepositoryProvider;

    private Provider<RoutineRepository> routineRepositoryProvider;

    private Provider<WorkoutRepository> workoutRepositoryProvider;

    private Provider<PRRepository> pRRepositoryProvider;

    private Provider<ExerciseLibrary> exerciseLibraryProvider;

    private SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.provideSupabaseClientProvider = DoubleCheck.provider(new SwitchingProvider<SupabaseClient>(singletonCImpl, 1));
      this.authRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<AuthRepository>(singletonCImpl, 0));
      this.userRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<UserRepository>(singletonCImpl, 2));
      this.bodyweightRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<BodyweightRepository>(singletonCImpl, 3));
      this.routineRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<RoutineRepository>(singletonCImpl, 4));
      this.workoutRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<WorkoutRepository>(singletonCImpl, 5));
      this.pRRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<PRRepository>(singletonCImpl, 6));
      this.exerciseLibraryProvider = DoubleCheck.provider(new SwitchingProvider<ExerciseLibrary>(singletonCImpl, 7));
    }

    @Override
    public void injectMuscleMaxApp(MuscleMaxApp arg0) {
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return Collections.<Boolean>emptySet();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.musclemax.app.data.auth.AuthRepository 
          return (T) new AuthRepository(singletonCImpl.provideSupabaseClientProvider.get());

          case 1: // io.github.jan.supabase.SupabaseClient 
          return (T) SupabaseModule_ProvideSupabaseClientFactory.provideSupabaseClient();

          case 2: // com.musclemax.app.data.remote.UserRepository 
          return (T) new UserRepository(singletonCImpl.provideSupabaseClientProvider.get(), singletonCImpl.authRepositoryProvider.get());

          case 3: // com.musclemax.app.data.remote.BodyweightRepository 
          return (T) new BodyweightRepository(singletonCImpl.provideSupabaseClientProvider.get(), singletonCImpl.authRepositoryProvider.get());

          case 4: // com.musclemax.app.data.remote.RoutineRepository 
          return (T) new RoutineRepository(singletonCImpl.provideSupabaseClientProvider.get(), singletonCImpl.authRepositoryProvider.get());

          case 5: // com.musclemax.app.data.remote.WorkoutRepository 
          return (T) new WorkoutRepository(singletonCImpl.provideSupabaseClientProvider.get(), singletonCImpl.authRepositoryProvider.get());

          case 6: // com.musclemax.app.data.remote.PRRepository 
          return (T) new PRRepository(singletonCImpl.provideSupabaseClientProvider.get(), singletonCImpl.authRepositoryProvider.get());

          case 7: // com.musclemax.app.data.local.ExerciseLibrary 
          return (T) new ExerciseLibrary(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
