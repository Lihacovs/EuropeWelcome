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

package eu.balticit.android.europewelcome.ui.profile.changeprofile;

import android.content.Context;
import android.net.Uri;

import eu.balticit.android.europewelcome.ui.base.MvpPresenter;

/**
 * Presenter interface for {@link ChangeProfilePresenter}
 */
public interface ChangeProfileMvpPresenter<V extends ChangeProfileMvpView> extends MvpPresenter<V> {

    void loadCurrentUserData();

    void uploadFileToStorage(Uri uri, Context context);

    String getUserPhotoUrl();

    void onSaveChangesButtonClick(
            String photoUrl,
            String gender,
            String birthDate);
}