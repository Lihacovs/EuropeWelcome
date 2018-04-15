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

package eu.balticit.android.europewelcome.di.component;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import eu.balticit.android.europewelcome.MyApp;
import eu.balticit.android.europewelcome.data.DataManager;
import eu.balticit.android.europewelcome.di.ApplicationContext;
import eu.balticit.android.europewelcome.di.module.ApplicationModule;

/**
 * Links the {@link eu.balticit.android.europewelcome.MyApp} dependency and the {@link ApplicationModule}
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