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

package com.bmd.android.europewelcome.ui.profile;

import android.net.Uri;

import com.bmd.android.europewelcome.ui.base.MvpPresenter;

/**
 * Presenter interface for {@link ProfilePresenter}
 */
public interface ProfileMvpPresenter<V extends ProfileMvpView> extends MvpPresenter<V> {

    void loadUserProfile();

    void newUserName(String userName);

    void newUserEmail(String userEmail);

    void uploadUserImageToStorage(Uri uri);

    void setUserId(String userId);
}
