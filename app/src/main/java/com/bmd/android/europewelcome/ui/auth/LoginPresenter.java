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
import android.util.Log;

import com.bmd.android.europewelcome.R;
import com.bmd.android.europewelcome.data.DataManager;
import com.bmd.android.europewelcome.ui.base.BasePresenter;
import com.bmd.android.europewelcome.utils.CommonUtils;
import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.GoogleAuthProvider;

import javax.inject.Inject;

/**
 * Login Presenter
 */

public class LoginPresenter<V extends LoginMvpView> extends BasePresenter<V>
        implements LoginMvpPresenter<V>{

    private static final String TAG = "LoginPresenter";

    @Inject
    public LoginPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void onServerLoginClick(String email, String password) {
        //validate email and password
        if (email == null || email.isEmpty()) {
            getMvpView().onError(R.string.empty_email);
            return;
        }
        if (!CommonUtils.isEmailValid(email)) {
            getMvpView().onError(R.string.invalid_email);
            return;
        }
        if (password == null || password.isEmpty()) {
            getMvpView().onError(R.string.empty_password);
            return;
        }
        getMvpView().showLoading();
        signInFirebaseUser(email, password);
    }

    @Override
    public Intent getGoogleSignInIntent() {
        return getDataManager().getGoogleSignInIntent();
    }

    private void signInFirebaseUser(final String email, final String password){
        getDataManager().signInUser(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    if (!isViewAttached()) {
                        return;
                    }

                    // Sign in success, update UI with the signed-in user's information
                    getMvpView().showMessage("user signedIn");

                    getDataManager().updateUserInfo(
                            null,
                            getDataManager().getUserId(),
                            DataManager.LoggedInMode.LOGGED_IN_MODE_SERVER,
                            getDataManager().getUserName(),
                            getDataManager().getUserEmail(),
                            getDataManager().getUserImageUrl().toString()
                    );

                    getMvpView().hideLoading();
                    getMvpView().openMainActivity();
                } else {
                    if (!isViewAttached()) {
                        return;
                    }
                    // If sign in fails, create user.
                    createFirebaseUser(email, password);
                    getMvpView().hideLoading();
                }
            }
        });
    }

    private void createFirebaseUser(String email, String password){
        getDataManager().createUser(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    if (!isViewAttached()) {
                        return;
                    }

                    // Sign in success, update UI with the signed-in user's information
                    getMvpView().showMessage("user created");

                    getDataManager().updateUserInfo(
                            null,
                            getDataManager().getUserId(),
                            DataManager.LoggedInMode.LOGGED_IN_MODE_SERVER,
                            getDataManager().getUserName(),
                            getDataManager().getUserEmail(),
                            null
                    );

                    getMvpView().hideLoading();
                    getMvpView().openMainActivity();
                } else {
                    // If sign in fails, display a message to the user.
                    if (!isViewAttached()) {
                        return;
                    }
                    getMvpView().showMessage("user creation failed");
                    getMvpView().hideLoading();
                }
            }
        });
    }

    @Override
    public void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        // instruct LoginActivity to initiate google login
        //TODO: implement firebase login with Google in AppDataManager class
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        getMvpView().showLoading();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        getDataManager().signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            Log.d(TAG, "UserName:" + getDataManager().getUserName());
                            Log.d(TAG, "UserEmail:" + getDataManager().getUserEmail());
                            Log.d(TAG, "UserPicUrl:" + getDataManager().getUserImageUrl());



                            getDataManager().updateUserInfo(
                                    null,
                                    getDataManager().getUserId(),
                                    DataManager.LoggedInMode.LOGGED_IN_MODE_GOOGLE,
                                    getDataManager().getUserName(),
                                    getDataManager().getUserEmail(),
                                    getDataManager().getUserImageUrl().toString()
                            );

                            Log.d(TAG, "signInWithCredential: current User: ");
                            Log.d(TAG, "CurrentUserName:" + getDataManager().getCurrentUserName());
                            Log.d(TAG, "CurrentUserEmail:" + getDataManager().getCurrentUserEmail());
                            Log.d(TAG, "CurrentUserPicUrl:" + getDataManager().getCurrentUserProfilePicUrl());

                            if (!isViewAttached()) {
                                return;
                            }

                            getMvpView().hideLoading();
                            getMvpView().openMainActivity();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            /*Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);*/
                        }

                        // [START_EXCLUDE]
                        getMvpView().hideLoading();
                        // [END_EXCLUDE]
                    }
                });


        getMvpView().openMainActivity();

    }

    @Override
    public void firebaseAuthWithFacebook(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        // [START_EXCLUDE silent]
        getMvpView().showLoading();
        // [END_EXCLUDE]

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        getDataManager().signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            Log.d(TAG, "UserName:" + getDataManager().getUserName());
                            Log.d(TAG, "UserEmail:" + getDataManager().getUserEmail());
                            Log.d(TAG, "UserPicUrl:" + getDataManager().getUserImageUrl());



                            getDataManager().updateUserInfo(
                                    null,
                                    getDataManager().getUserId(),
                                    DataManager.LoggedInMode.LOGGED_IN_MODE_GOOGLE,
                                    getDataManager().getUserName(),
                                    getDataManager().getUserEmail(),
                                    getDataManager().getUserImageUrl().toString()
                            );

                            Log.d(TAG, "signInWithCredential: current User: ");
                            Log.d(TAG, "CurrentUserName:" + getDataManager().getCurrentUserName());
                            Log.d(TAG, "CurrentUserEmail:" + getDataManager().getCurrentUserEmail());
                            Log.d(TAG, "CurrentUserPicUrl:" + getDataManager().getCurrentUserProfilePicUrl());

                            if (!isViewAttached()) {
                                return;
                            }

                            getMvpView().hideLoading();
                            //getMvpView().openMainActivity();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            /*Toast.makeText(FacebookLoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);*/
                        }

                        // [START_EXCLUDE]
                        getMvpView().hideLoading();
                        // [END_EXCLUDE]
                    }
                });
    }

    @Override
    public void onFacebookLoginClick() {
        // instruct LoginActivity to initiate facebook login
        getMvpView().showLoading();
        //TODO: implement firebase login with FB in AppDataManager class
        getMvpView().hideLoading();
        getMvpView().openMainActivity();
    }
}
