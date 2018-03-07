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

package com.bmd.android.europewelcome.ui.auth.register;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.bmd.android.europewelcome.R;
import com.bmd.android.europewelcome.data.DataManager;
import com.bmd.android.europewelcome.ui.base.BasePresenter;
import com.bmd.android.europewelcome.ui.custom.ImageCompress;
import com.bmd.android.europewelcome.utils.CommonUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.storage.UploadTask;

import java.io.File;

import javax.inject.Inject;

/**
 * Register Presenter. Creates Firebase user.
 */

public class RegisterPresenter<V extends RegisterMvpView> extends BasePresenter<V>
        implements RegisterMvpPresenter<V> {

    private static final String TAG = "RegisterPresenter";

    @Inject
    public RegisterPresenter(DataManager dataManager) {
        super(dataManager);
    }

    private String mPhotoUrl;

    @Override
    public void onRegisterButtonClick(String email,
                                      String password,
                                      String photoUrl,
                                      String name,
                                      String surname,
                                      String gender,
                                      String birthDate) {
        //Validates register data
        if (email == null || email.isEmpty()) {
            getMvpView().onError(R.string.register_empty_email);
            return;
        }
        if (!CommonUtils.isEmailValid(email)) {
            getMvpView().onError(R.string.register_invalid_email);
            return;
        }
        if (password == null || password.isEmpty()) {
            getMvpView().onError(R.string.register_empty_password);
            return;
        }
        if (password.trim().length() < 6) {
            getMvpView().onError(R.string.register_short_password);
            return;
        }
        if (photoUrl == null || photoUrl.isEmpty()) {
            getMvpView().onError(R.string.register_no_photo);
            return;
        }
        if (name == null || name.isEmpty()) {
            getMvpView().onError(R.string.register_empty_name);
            return;
        }
        if (name.trim().length() < 2) {
            getMvpView().onError(R.string.register_short_name);
            return;
        }
        if (surname == null || surname.isEmpty()) {
            getMvpView().onError(R.string.register_empty_surname);
            return;
        }
        if (surname.trim().length() < 2) {
            getMvpView().onError(R.string.register_short_surname);
            return;
        }
        if (birthDate == null || birthDate.isEmpty()) {
            getMvpView().onError(R.string.register_empty_birth_date);
            return;
        }

        createFireBaseUser(email, password, photoUrl, name, surname);
    }

    //Uploads resized and compressed user Photo to FireBase storage
    @Override
    public void uploadFileToStorage(Uri uri, Context context) {
        String compressedFile = new ImageCompress(context).compressImage(
                uri.toString(), 544.0f, 408.0f);

        getDataManager().uploadFileToStorage(Uri.fromFile(new File(compressedFile)))
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        if (taskSnapshot.getDownloadUrl() != null) {
                            String downloadUrl = taskSnapshot.getDownloadUrl().toString();
                            mPhotoUrl = downloadUrl;
                            if (getMvpView() != null) {
                                getMvpView().loadUserPhoto(downloadUrl);
                            }
                        }
                    }
                }).removeOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                getMvpView().onError(R.string.register_upload_error);
            }
        });
    }

    @Override
    public String getUserPhotoUrl() {
        return mPhotoUrl;
    }

    private void createFireBaseUser(String email,
                                    String password,
                                    String photoUrl,
                                    String name,
                                    String surname) {
        getMvpView().showLoading();

        getDataManager().createFirebaseUser(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        if (!isViewAttached()) {
                            return;
                        }

                        //update user profile for Firebase authentication
                        getDataManager()
                                .setFirebaseUserProfile(name + " " + surname, photoUrl)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        //update user profile in SharedPrefs
                                        getDataManager().updateUserInfo(
                                                null,
                                                getDataManager().getFirebaseUserId(),
                                                DataManager.LoggedInMode.LOGGED_IN_MODE_SERVER,
                                                getDataManager().getFirebaseUserName(),
                                                getDataManager().getFirebaseUserEmail(),
                                                getDataManager().getFirebaseUserImageUrl()
                                        );

                                        getDataManager().setLastUsedEmail(email);
                                        getMvpView().hideLoading();
                                        getMvpView().openMainActivity();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                getMvpView().hideLoading();
                                getMvpView().onError(R.string.register_some_error);
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (!isViewAttached()) {
                    return;
                }
                getMvpView().hideLoading();
                if (e instanceof FirebaseAuthUserCollisionException) {
                    getMvpView().onError(R.string.register_email_already_used);
                    return;
                }
                if (e instanceof FirebaseAuthWeakPasswordException) {
                    getMvpView().onError(((FirebaseAuthWeakPasswordException) e).getReason());
                    return;
                }
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    getMvpView().onError(e.getMessage());
                    return;
                }
                getMvpView().onError(R.string.register_some_error);
            }
        });
    }
}
