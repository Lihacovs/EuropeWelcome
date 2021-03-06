/*
 * Copyright (C) 2018 Baltic Information Technologies
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.balticit.android.europewelcome.di.module;

import android.app.Application;
import android.content.Context;

import eu.balticit.android.europewelcome.data.AppDataManager;
import eu.balticit.android.europewelcome.data.DataManager;
import eu.balticit.android.europewelcome.data.firebase.AppFirebaseHelper;
import eu.balticit.android.europewelcome.data.firebase.FirebaseHelper;
import eu.balticit.android.europewelcome.data.network.AppNetworkHelper;
import eu.balticit.android.europewelcome.data.network.NetworkHelper;
import eu.balticit.android.europewelcome.data.prefs.AppPreferencesHelper;
import eu.balticit.android.europewelcome.data.prefs.PreferencesHelper;
import eu.balticit.android.europewelcome.di.ApplicationContext;
import eu.balticit.android.europewelcome.di.DatabaseInfo;
import eu.balticit.android.europewelcome.di.PreferenceInfo;
import eu.balticit.android.europewelcome.utils.AppConstants;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Provides dependencies expressed in the {@link eu.balticit.android.europewelcome.MyApp}
 */

@Module
public class ApplicationModule {

    private final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @DatabaseInfo
    String provideDatabaseName() {
        return AppConstants.DB_NAME;
    }

    @Provides
    @PreferenceInfo
    String providePreferenceName() {
        return AppConstants.PREF_NAME;
    }

    @Provides
    @Singleton
    DataManager provideDataManager(AppDataManager appDataManager) {
        return appDataManager;
    }

    @Provides
    @Singleton
    FirebaseHelper provideFirebaseHelper(AppFirebaseHelper appFirebaseHelper) {
        return appFirebaseHelper;
    }

    @Provides
    @Singleton
    PreferencesHelper providePreferencesHelper(AppPreferencesHelper appPreferencesHelper) {
        return appPreferencesHelper;
    }

    @Provides
    @Singleton
    NetworkHelper provideNetworkHelper(AppNetworkHelper appNetworkHelper) {
        return appNetworkHelper;
    }

    //TODO: remove calligraphy from app completely
    /*@Provides
    @Singleton
    CalligraphyConfig provideCalligraphyDefaultConfig() {
        return new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/source-sans-pro/SourceSansPro-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build();
    }*/
}
