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

package com.bmd.android.europewelcome.ui.profile;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.bmd.android.europewelcome.data.DataManager;
import com.bmd.android.europewelcome.ui.base.BasePresenter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.UploadTask;

import javax.inject.Inject;

/**
 * Created by Konstantins on 12/23/2017.
 */

public class ProfilePresenter<V extends ProfileMvpView> extends BasePresenter<V> implements
        ProfileMvpPresenter<V> {

    private static final String TAG = "ProfilePresenter";

    @Inject
    public ProfilePresenter(DataManager dataManager) {
        super(dataManager);

    }

    @Override
    public void loadUserProfile() {

        final String currentUserName = getDataManager().getCurrentUserName();
        if (currentUserName != null && !currentUserName.isEmpty()) {
            getMvpView().loadUserName(currentUserName);
        }

        final String currentUserEmail = getDataManager().getCurrentUserEmail();
        if (currentUserEmail != null && !currentUserEmail.isEmpty()) {
            getMvpView().loadUserEmail(currentUserEmail);
        }

        final String profilePicUrl = getDataManager().getCurrentUserProfilePicUrl();
        if (profilePicUrl != null && !profilePicUrl.isEmpty()) {
            getMvpView().loadUserImageUrl(profilePicUrl);
        }
    }

    @Override
    public void newUserName(String userName) {
        if(!userName.equals(getDataManager().getCurrentUserName())) {
            getDataManager().setUserName(userName);
            getDataManager().setCurrentUserName(userName);
        }
    }

    @Override
    public void newUserEmail(String userEmail) {
        if(!userEmail.equals(getDataManager().getCurrentUserEmail())) {
            getDataManager().setUserEmail(userEmail);
            getDataManager().setCurrentUserEmail(userEmail);
        }
    }

    @Override
    public void uploadUserImageToStorage(Uri uri) {
        getMvpView().showMessage("Uploading...");
        getDataManager().uploadFileToStorage(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d(TAG, "uploadPhoto:onSuccess:" +
                                taskSnapshot.getMetadata().getReference().getPath());

                        Uri downloadUri = taskSnapshot.getDownloadUrl();
                        String downloadUrlString = downloadUri.toString();
                        getDataManager().setCurrentUserProfilePicUrl(downloadUrlString);
                        getDataManager().setUserImageUrl(downloadUri);
                        getMvpView().loadUserImageUrl(downloadUrlString);

                        getMvpView().showMessage("Image uploaded");
                    }
                }).removeOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "uploadPhoto:onError", e);
                getMvpView().showMessage("Upload failed");
            }
        });
    }
}
