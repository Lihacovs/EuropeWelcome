/*
 * Copyright (C) 2017. Baltic Mobile Development
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

import com.bmd.android.europewelcome.di.PerActivity;
import com.bmd.android.europewelcome.di.module.ActivityModule;
import com.bmd.android.europewelcome.ui.about.AboutFragment;
import com.bmd.android.europewelcome.ui.auth.LoginActivity;
import com.bmd.android.europewelcome.ui.posts.PostsActivity;
import com.bmd.android.europewelcome.ui.posts.rating.RateUsDialog;

import dagger.Component;

/**
 * Links Activities and the {@link ActivityModule}
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(PostsActivity activity);

    void inject(LoginActivity activity);

    //void inject(SplashActivity activity);

    //void inject(FeedActivity activity);

    void inject(AboutFragment fragment);

    //void inject(OpenSourceFragment fragment);

    //void inject(BlogFragment fragment);

    void inject(RateUsDialog dialog);

}
