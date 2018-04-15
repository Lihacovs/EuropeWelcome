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

import dagger.Component;
import eu.balticit.android.europewelcome.di.PerActivity;
import eu.balticit.android.europewelcome.di.module.ActivityModule;
import eu.balticit.android.europewelcome.ui.about.AboutFragment;
import eu.balticit.android.europewelcome.ui.auth.LoginActivity;
import eu.balticit.android.europewelcome.ui.auth.register.RegisterFragment;
import eu.balticit.android.europewelcome.ui.bookmarks.BookmarksActivity;
import eu.balticit.android.europewelcome.ui.drafts.DraftsActivity;
import eu.balticit.android.europewelcome.ui.newpost.NewPostActivity;
import eu.balticit.android.europewelcome.ui.postdetail.PostDetailActivity;
import eu.balticit.android.europewelcome.ui.posts.PostsActivity;
import eu.balticit.android.europewelcome.ui.posts.free.FreePostsFragment;
import eu.balticit.android.europewelcome.ui.posts.premium.PremiumPostsFragment;
import eu.balticit.android.europewelcome.ui.posts.rating.RateUsDialog;
import eu.balticit.android.europewelcome.ui.profile.ProfileActivity;
import eu.balticit.android.europewelcome.ui.profile.changeprofile.ChangeProfileFragment;
import eu.balticit.android.europewelcome.ui.profile.usercomments.UserCommentsFragment;
import eu.balticit.android.europewelcome.ui.profile.userposts.UserPostsFragment;

/**
 * Links Activities and the {@link ActivityModule}
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(PostsActivity activity);

    void inject(LoginActivity activity);

    void inject(PostDetailActivity activity);

    void inject(DraftsActivity activity);

    void inject(BookmarksActivity activity);

    void inject(NewPostActivity activity);

    void inject(ProfileActivity activity);

    void inject(AboutFragment fragment);

    void inject(RegisterFragment fragment);

    void inject(ChangeProfileFragment fragment);

    void inject(PremiumPostsFragment fragment);

    void inject(UserPostsFragment fragment);

    void inject(UserCommentsFragment fragment);

    void inject(FreePostsFragment fragment);

    void inject(RateUsDialog dialog);

}
