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

package eu.balticit.android.europewelcome.ui.profile;

import eu.balticit.android.europewelcome.R;
import eu.balticit.android.europewelcome.data.DataManager;
import eu.balticit.android.europewelcome.data.firebase.model.User;
import eu.balticit.android.europewelcome.ui.base.BasePresenter;

import javax.inject.Inject;

import eu.balticit.android.europewelcome.data.DataManager;
import eu.balticit.android.europewelcome.data.firebase.model.User;

/**
 * Profile Presenter
 */
public class ProfilePresenter<V extends ProfileMvpView> extends BasePresenter<V> implements
        ProfileMvpPresenter<V> {

    private static final String TAG = "ProfilePresenter";

    private String mUserId;
    private User mUser;

    @Inject
    ProfilePresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void loadUserProfile() {

        if (mUserId.equals(getDataManager().getCurrentUserId())) {
            getMvpView().showChangeProfile();

            final String currentUserPhotoUrl = getDataManager().getCurrentUserProfilePicUrl();
            if (currentUserPhotoUrl != null && !currentUserPhotoUrl.isEmpty()) {
                getMvpView().loadUserImageUrl(currentUserPhotoUrl);
            }

            final String currentUserName = getDataManager().getCurrentUserName();
            if (currentUserName != null && !currentUserName.isEmpty()) {
                getMvpView().loadUserName(currentUserName);
            }

            //Just show email for current user or implement current user birth date in Shared Prefs
            final String profileUserBirthDate = getDataManager().getCurrentUserEmail();
            if (profileUserBirthDate != null && !profileUserBirthDate.isEmpty()) {
                getMvpView().loadUserBirthDate(profileUserBirthDate);
            }

            getMvpView().hideLoading();
            return;
        } else {
            getMvpView().hideChangeProfile();
        }

        getDataManager().getUser(mUserId).addOnSuccessListener(documentSnapshot -> {
            mUser = documentSnapshot.toObject(User.class);

            getMvpView().loadUserImageUrl(mUser.getUserPhotoUrl());
            getMvpView().loadUserName(mUser.getUserName());
            getMvpView().loadUserBirthDate(mUser.getUserBirthDate());

            getMvpView().hideLoading();
        }).addOnFailureListener(e -> {
            getMvpView().hideLoading();
            getMvpView().onError(R.string.profile_some_error);
        });


    }

    @Override
    public void setUserId(String userId) {
        mUserId = userId;
    }
}