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

package com.bmd.android.europewelcome.ui.auth;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.bmd.android.europewelcome.R;
import com.bmd.android.europewelcome.data.DataManager;
import com.bmd.android.europewelcome.ui.base.BasePresenter;
import com.bmd.android.europewelcome.utils.CommonUtils;
import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.GoogleAuthProvider;

import javax.inject.Inject;

/**
 * Login Presenter
 */

public class LoginPresenter<V extends LoginMvpView> extends BasePresenter<V>
        implements LoginMvpPresenter<V> {

    private static final String TAG = "LoginPresenter";

    @Inject
    public LoginPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void onServerLoginClick(String email, String password) {
        //validate email and password
        if (email == null || email.isEmpty()) {
            getMvpView().onError(R.string.login_empty_email);
            return;
        }
        if (!CommonUtils.isEmailValid(email)) {
            getMvpView().onError(R.string.login_invalid_email);
            return;
        }
        if (password == null || password.isEmpty()) {
            getMvpView().onError(R.string.login_empty_password);
            return;
        }

        signInFirebaseUser(email, password);
    }

    private void signInFirebaseUser(final String email, final String password) {
        getMvpView().showLoading();
        getDataManager().signInFirebaseUser(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        if (!isViewAttached()) {
                            return;
                        }

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
                if (!isViewAttached()) {
                    return;
                }
                getMvpView().hideLoading();
                if (e instanceof FirebaseAuthInvalidUserException) {
                    if (((FirebaseAuthInvalidUserException) e)
                            .getErrorCode().equals("ERROR_USER_NOT_FOUND")) {
                        getMvpView().onError(R.string.login_email_not_exist);
                        return;
                    }
                    getMvpView().onError(e.getMessage());
                    return;
                }
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    getMvpView().onError(R.string.login_invalid_password);
                    return;
                }
                if (e instanceof FirebaseAuthUserCollisionException) {
                    getMvpView().onError(R.string.login_email_already_used);
                    return;
                }
                getMvpView().onError(R.string.login_some_error);
            }
        });
    }

    @Override
    public Intent getGoogleSignInIntent() {
        return getDataManager().getGoogleSignInIntent();
    }

    @Override
    public void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        getMvpView().showLoading();

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        getDataManager().signInFirebaseWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        if (!isViewAttached()) {
                            return;
                        }

                        getDataManager().updateUserInfo(
                                null,
                                getDataManager().getFirebaseUserId(),
                                DataManager.LoggedInMode.LOGGED_IN_MODE_GOOGLE,
                                getDataManager().getFirebaseUserName(),
                                getDataManager().getFirebaseUserEmail(),
                                getDataManager().getFirebaseUserImageUrl()
                        );

                        getMvpView().hideLoading();
                        getMvpView().openMainActivity();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (!isViewAttached()) {
                    return;
                }
                getMvpView().hideLoading();
                if (e instanceof FirebaseAuthUserCollisionException) {
                    getMvpView().onError(R.string.login_email_already_used);
                    return;
                }
                getMvpView().onError(R.string.login_some_error);
            }
        });
    }

    @Override
    public void firebaseAuthWithFacebook(AccessToken token) {
        getMvpView().showLoading();
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        getDataManager().signInFirebaseWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        if (!isViewAttached()) {
                            return;
                        }

                        getDataManager().updateUserInfo(
                                null,
                                getDataManager().getFirebaseUserId(),
                                DataManager.LoggedInMode.LOGGED_IN_MODE_FB,
                                getDataManager().getFirebaseUserName(),
                                getDataManager().getFirebaseUserEmail(),
                                getDataManager().getFirebaseUserImageUrl()
                        );

                        getMvpView().hideLoading();
                        getMvpView().openMainActivity();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (!isViewAttached()) {
                    return;
                }
                getMvpView().hideLoading();
                if (e instanceof FirebaseAuthUserCollisionException) {
                    getMvpView().onError(R.string.login_email_already_used);
                    return;
                }
                getMvpView().onError(R.string.login_some_error);
            }
        });
    }

    @Override
    public String getLastUsedEmail() {
        return getDataManager().getLastUsedEmail();
    }
}
