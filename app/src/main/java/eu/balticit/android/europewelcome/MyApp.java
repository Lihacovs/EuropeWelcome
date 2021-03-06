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

package eu.balticit.android.europewelcome;

import android.support.multidex.MultiDexApplication;
import android.support.v7.app.AppCompatDelegate;

import com.androidnetworking.AndroidNetworking;
import com.google.android.gms.maps.MapsInitializer;

import javax.inject.Inject;

import eu.balticit.android.europewelcome.data.DataManager;
import eu.balticit.android.europewelcome.di.component.ApplicationComponent;
import eu.balticit.android.europewelcome.di.component.DaggerApplicationComponent;
import eu.balticit.android.europewelcome.di.module.ApplicationModule;
import eu.balticit.android.europewelcome.utils.AppLogger;

import static com.androidnetworking.interceptors.HttpLoggingInterceptor.Level;

/**
 * Application level instantiations
 */

public class MyApp extends MultiDexApplication {

    @Inject
    DataManager mDataManager;

    /*@Inject
    CalligraphyConfig mCalligraphyConfig;*/

    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        /*
          DaggerApplicationComponent is the generated class by the Dagger2
          We provide the ApplicationModule class that is used to construct the dependencies.
         */
        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this)).build();

        mApplicationComponent.inject(this);

        AppLogger.init();

        AndroidNetworking.initialize(getApplicationContext());
        if (BuildConfig.DEBUG) {
            AndroidNetworking.enableLogging(Level.BODY);
        }

        //CalligraphyConfig.initDefault(mCalligraphyConfig);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        try {
            MapsInitializer.initialize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ApplicationComponent getComponent() {
        return mApplicationComponent;
    }

    public DataManager getDataManager() {
        return mDataManager;
    }

    // Needed to replace the component with a test specific one
    public void setComponent(ApplicationComponent applicationComponent) {
        mApplicationComponent = applicationComponent;
    }
}
