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

package eu.balticit.android.europewelcome.ui.posts;

import eu.balticit.android.europewelcome.ui.base.MvpView;

/**
 * View interface for {@link PostsActivity}
 */

public interface PostsMvpView extends MvpView {

    void openLoginActivity();

    void showAboutFragment();

    void updateUserName(String currentUserName);

    void updateUserEmail(String currentUserEmail);

    void updateUserProfilePic(String currentUserProfilePicUrl);

    void setDefaultUserImage();

    void updateAppVersion();

    void chooseLoginAction();

    void showRateUsDialog();

    void openBookmarksActivity();

    void openDraftsActivity();

    void openNewPostActivity();

    void openProfileActivity();

    void closeNavigationDrawer();

    void lockDrawer();

    void unlockDrawer();
}
