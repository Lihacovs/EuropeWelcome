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

package com.bmd.android.europewelcome.data;


import android.content.Context;

import com.bmd.android.europewelcome.data.firebase.FirebaseHelper;
import com.bmd.android.europewelcome.data.firebase.model.Post;
import com.bmd.android.europewelcome.data.firebase.model.PostImage;
import com.bmd.android.europewelcome.data.firebase.model.PostText;
import com.bmd.android.europewelcome.data.prefs.PreferencesHelper;
import com.bmd.android.europewelcome.di.ApplicationContext;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * It is the one point of contact for any data related operation in the application.
 * Delegates all the operations specific to any Helper.
 */

@Singleton
public class AppDataManager implements DataManager {

    private static final String TAG = "AppDataManager";

    private final Context mContext;
    private final PreferencesHelper mPreferencesHelper;
    private final FirebaseHelper mFirebaseHelper;

    @Inject
    public AppDataManager(@ApplicationContext Context context,
                          PreferencesHelper preferencesHelper,
                          FirebaseHelper firebaseHelper) {
        mContext = context;
        mPreferencesHelper = preferencesHelper;
        mFirebaseHelper = firebaseHelper;
    }

    @Override
    public String getAccessToken() {
        return mPreferencesHelper.getAccessToken();
    }

    @Override
    public void setAccessToken(String accessToken) {
        mPreferencesHelper.setAccessToken(accessToken);
    }

    @Override
    public int getCurrentUserLoggedInMode() {
        return mPreferencesHelper.getCurrentUserLoggedInMode();
    }

    @Override
    public void setCurrentUserLoggedInMode(LoggedInMode mode) {
        mPreferencesHelper.setCurrentUserLoggedInMode(mode);
    }

    @Override
    public Long getCurrentUserId() {
        return mPreferencesHelper.getCurrentUserId();
    }

    @Override
    public void setCurrentUserId(Long userId) {
        mPreferencesHelper.setCurrentUserId(userId);
    }

    @Override
    public String getCurrentUserName() {
        return mPreferencesHelper.getCurrentUserName();
    }

    @Override
    public void setCurrentUserName(String userName) {
        mPreferencesHelper.setCurrentUserName(userName);
    }

    @Override
    public String getCurrentUserEmail() {
        return mPreferencesHelper.getCurrentUserEmail();
    }

    @Override
    public void setCurrentUserEmail(String email) {
        mPreferencesHelper.setCurrentUserEmail(email);
    }

    @Override
    public String getCurrentUserProfilePicUrl() {
        return mPreferencesHelper.getCurrentUserProfilePicUrl();
    }

    @Override
    public void setCurrentUserProfilePicUrl(String profilePicUrl) {
        mPreferencesHelper.setCurrentUserProfilePicUrl(profilePicUrl);
    }

    @Override
    public void updateUserInfo(
            String accessToken,
            Long userId,
            LoggedInMode loggedInMode,
            String userName,
            String email,
            String profilePicPath) {

        setAccessToken(accessToken);
        setCurrentUserId(userId);
        setCurrentUserLoggedInMode(loggedInMode);
        setCurrentUserName(userName);
        setCurrentUserEmail(email);
        setCurrentUserProfilePicUrl(profilePicPath);
    }

    @Override
    public void setUserAsLoggedOut() {
        updateUserInfo(
                null,
                null,
                DataManager.LoggedInMode.LOGGED_IN_MODE_LOGGED_OUT,
                null,
                null,
                null);
    }

    @Override
    public Task<AuthResult> createUser(String email, String password) {
        return mFirebaseHelper.createUser(email, password);
    }

    @Override
    public Task<AuthResult> signInUser(String email, String password) {
        return mFirebaseHelper.signInUser(email, password);
    }

    @Override
    public FirebaseUser getCurrentUser() {
        return mFirebaseHelper.getCurrentUser();
    }

    @Override
    public String getUserId() {
        return mFirebaseHelper.getUserId();
    }

    @Override
    public String getUserName() {
        return mFirebaseHelper.getUserName();
    }

    @Override
    public String getUserEmail() {
        return mFirebaseHelper.getUserEmail();
    }

    @Override
    public CollectionReference getPostsColRef() {
        return mFirebaseHelper.getPostsColRef();
    }

    @Override
    public Task<Void> savePost(Post post) {
        return mFirebaseHelper.savePost(post);
    }

    @Override
    public Task<Void> savePostText(String postId, PostText postText) {
        return mFirebaseHelper.savePostText(postId, postText);
    }

    @Override
    public Task<Void> savePostImage(String postId, PostImage postImage) {
        return mFirebaseHelper.savePostImage(postId, postImage);
    }
}
