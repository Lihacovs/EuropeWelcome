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

package com.bmd.android.europewelcome.ui.profile.changeprofile;

import android.content.Context;
import android.net.Uri;

import com.bmd.android.europewelcome.R;
import com.bmd.android.europewelcome.data.DataManager;
import com.bmd.android.europewelcome.data.firebase.model.User;
import com.bmd.android.europewelcome.ui.base.BasePresenter;
import com.bmd.android.europewelcome.ui.custom.ImageCompress;

import java.io.File;

import javax.inject.Inject;

public class ChangeProfilePresenter<V extends ChangeProfileMvpView> extends BasePresenter<V>
        implements ChangeProfileMvpPresenter<V> {

    private static final String TAG = "ChangeProfilePresenter";

    private String mPhotoUrl;

    @Inject
    ChangeProfilePresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void loadCurrentUserData() {
        getMvpView().loadUserPhoto(getDataManager().getCurrentUserProfilePicUrl());
        getMvpView().loadUserName(getDataManager().getCurrentUserName());
        getMvpView().loadUserEmail(getDataManager().getCurrentUserEmail());
        getMvpView().loadUserBirthDate(getDataManager().getCurrentUserBirthDate());
        getMvpView().loadUserGender(getDataManager().getCurrentUserGender());
    }


    //Uploads resized and compressed user Photo to FireBase storage
    @Override
    public void uploadFileToStorage(Uri uri, Context context) {
        String compressedFile = new ImageCompress(context).compressImage(
                uri.toString(), 544.0f, 408.0f);

        getDataManager().uploadFileToStorage(Uri.fromFile(new File(compressedFile)), "userPhotos/")
                .addOnSuccessListener(taskSnapshot -> {
                    if (taskSnapshot.getDownloadUrl() != null) {
                        String downloadUrl = taskSnapshot.getDownloadUrl().toString();
                        mPhotoUrl = downloadUrl;
                        if (getMvpView() != null) {
                            getMvpView().loadUserPhoto(downloadUrl);
                        }
                    }
                }).removeOnFailureListener(e -> getMvpView().onError(R.string.register_upload_error));
    }

    @Override
    public String getUserPhotoUrl() {
        return mPhotoUrl;
    }

    @Override
    public void onSaveChangesButtonClick(String photoUrl,
                                         String gender,
                                         String birthDate) {
        getMvpView().hideKeyboard();
        getMvpView().showLoading();

        //Updates User data in 3 places: Firebase Auth, Firestore DB, SharedPrefs:

        //1. Updates data in Firestore database {Users}->{UserId}
        getDataManager().getUser(getDataManager().getCurrentUserId())
                .addOnSuccessListener(documentSnapshot -> {
                    User user = documentSnapshot.toObject(User.class);

                    if (photoUrl != null) {
                        user.setUserPhotoUrl(photoUrl);
                    }
                    user.setUserGender(gender);
                    user.setUserBirthDate(birthDate);
                    getDataManager().updateUser(user)
                            .addOnSuccessListener(aVoid -> {

                                //2.3. Updates Firebase Auth, And SharedPrefs
                                if (photoUrl != null) {
                                    getDataManager().setFirebaseUserImageUrl(photoUrl);
                                    getDataManager().setCurrentUserProfilePicUrl(photoUrl);
                                }
                                getDataManager().setCurrentUserGender(gender);
                                getDataManager().setCurrentUserBirthDate(birthDate);

                                getMvpView().hideLoading();

                                getMvpView().detachFragment();

                            }).addOnFailureListener(e -> {
                        getMvpView().hideLoading();
                        getMvpView().onError(R.string.profile_some_error);
                    });
                }).addOnFailureListener(e -> {
            getMvpView().hideLoading();
            getMvpView().onError(R.string.profile_some_error);
        });
    }
}
