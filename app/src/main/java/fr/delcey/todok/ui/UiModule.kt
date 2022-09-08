package fr.delcey.todok.ui

import androidx.fragment.app.FragmentActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
class UiModule {

    @ActivityScoped
    @Provides
    fun provideNavigationListener(activity: FragmentActivity) = NavigationListener(activity)

}