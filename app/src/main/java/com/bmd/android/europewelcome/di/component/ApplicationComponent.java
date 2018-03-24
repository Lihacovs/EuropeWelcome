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

package com.bmd.android.europewelcome.di.component;

import android.app.Application;
import android.content.Context;

import com.bmd.android.europewelcome.MyApp;
import com.bmd.android.europewelcome.data.DataManager;
import com.bmd.android.europewelcome.di.ApplicationContext;
import com.bmd.android.europewelcome.di.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Links the {@link com.bmd.android.europewelcome.MyApp} dependency and the {@link ApplicationModule}
 */

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(MyApp app);

    @ApplicationContext
    Context context();

    Application application();

    DataManager getDataManager();
}