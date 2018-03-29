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

package com.bmd.android.europewelcome.data;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.bmd.android.europewelcome.data.firebase.FirebaseHelper;
import com.bmd.android.europewelcome.data.firebase.model.Post;
import com.bmd.android.europewelcome.data.firebase.model.PostComment;
import com.bmd.android.europewelcome.data.firebase.model.PostSection;
import com.bmd.android.europewelcome.data.firebase.model.User;
import com.bmd.android.europewelcome.data.network.NetworkHelper;
import com.bmd.android.europewelcome.data.prefs.PreferencesHelper;
import com.bmd.android.europewelcome.di.ApplicationContext;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.UploadTask;

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
    private final NetworkHelper mNetworkHelper;

    @Inject
    public AppDataManager(@ApplicationContext Context context,
                          PreferencesHelper preferencesHelper,
                          FirebaseHelper firebaseHelper,
                          NetworkHelper networkHelper) {
        mContext = context;
        mPreferencesHelper = preferencesHelper;
        mFirebaseHelper = firebaseHelper;
        mNetworkHelper = networkHelper;
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
    public String getLastUsedEmail() {
        return mPreferencesHelper.getLastUsedEmail();
    }

    @Override
    public void setLastUsedEmail(String email) {
        mPreferencesHelper.setLastUsedEmail(email);
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
    public String getCurrentUserId() {
        return mPreferencesHelper.getCurrentUserId();
    }

    @Override
    public void setCurrentUserId(String userId) {
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
            String  userId,
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
    public Task<AuthResult> createFirebaseUser(String email, String password) {
        return mFirebaseHelper.createFirebaseUser(email, password);
    }

    @Override
    public Task<AuthResult> signInFirebaseUser(String email, String password) {
        return mFirebaseHelper.signInFirebaseUser(email, password);
    }

    @Override
    public Task<AuthResult> signInFirebaseWithCredential(AuthCredential credential) {
        return mFirebaseHelper.signInFirebaseWithCredential(credential);
    }

    @Override
    public void signOutFirebaseUser() {
        mFirebaseHelper.signOutFirebaseUser();
    }

    @Override
    public FirebaseUser getFirebaseUser() {
        return mFirebaseHelper.getFirebaseUser();
    }

    @Override
    public String getFirebaseUserId() {
        return mFirebaseHelper.getFirebaseUserId();
    }

    @Override
    public String getFirebaseUserName() {
        return mFirebaseHelper.getFirebaseUserName();
    }

    @Override
    public String getFirebaseUserEmail() {
        return mFirebaseHelper.getFirebaseUserEmail();
    }

    @Override
    public String getFirebaseUserImageUrl() {
        return mFirebaseHelper.getFirebaseUserImageUrl();
    }

    @Override
    public void setFirebaseUserName(String userName) {
        mFirebaseHelper.setFirebaseUserName(userName);
    }

    @Override
    public void setFirebaseUserEmail(String userEmail) {
        mFirebaseHelper.setFirebaseUserEmail(userEmail);
    }

    @Override
    public void setFirebaseUserImageUrl(String userImageUrl) {
        mFirebaseHelper.setFirebaseUserImageUrl(userImageUrl);
    }

    @Override
    public Task<Void> setFirebaseUserProfile(String userName, String userPhotoUrl) {
        return mFirebaseHelper.setFirebaseUserProfile(userName, userPhotoUrl);
    }

    @Override
    public Query getPostsQuery() {
        return mFirebaseHelper.getPostsQuery();
    }

    @Override
    public Query getPostsQueryOrderedByStars() {
        return mFirebaseHelper.getPostsQueryOrderedByStars();
    }

    @Override
    public Query getPostsQueryOrderedByViews() {
        return mFirebaseHelper.getPostsQueryOrderedByViews();
    }

    @Override
    public Query getPostsQueryOrderedByDate() {
        return mFirebaseHelper.getPostsQueryOrderedByDate();
    }

    @Override
    public Query getPostCommentsQuery(String postId) {
        return mFirebaseHelper.getPostCommentsQuery(postId);
    }

    @Override
    public Query getPostSectionQuery(String postId) {
        return mFirebaseHelper.getPostSectionQuery(postId);
    }

    @Override
    public Query getPostAsDraftQuery(String userId) {
        return mFirebaseHelper.getPostAsDraftQuery(userId);
    }

    @Override
    public Task<Void> savePost(Post post) {
        return mFirebaseHelper.savePost(post);
    }

    @Override
    public Task<Void> savePostSection(PostSection postSection, String postId) {
        return mFirebaseHelper.savePostSection(postSection, postId);
    }

    @Override
    public Task<Void> saveComment(String postId, PostComment postComment) {
        return mFirebaseHelper.saveComment(postId, postComment);
    }

    @Override
    public Task<Void> updatePost(Post post) {
        return mFirebaseHelper.updatePost(post);
    }

    @Override
    public Task<Void> updatePostSection(String postId, PostSection postSection) {
        return mFirebaseHelper.updatePostSection(postId, postSection);
    }

    @Override
    public Task<Void> deletePost(Post post) {
        return mFirebaseHelper.deletePost(post);
    }

    @Override
    public Task<Void> deletePostSection(String postId, PostSection postSection) {
        return mFirebaseHelper.deletePostSection(postId, postSection);
    }

    @Override
    public Task<DocumentSnapshot> getPost(String postId) {
        return mFirebaseHelper.getPost(postId);
    }

    @Override
    public Task<QuerySnapshot> getFirstPostSectionCollection(String postId) {
        return mFirebaseHelper.getFirstPostSectionCollection(postId);
    }

    @Override
    public Task<QuerySnapshot> getFirstPostSection(String postId, String sectionViewType) {
        return mFirebaseHelper.getFirstPostSection(postId, sectionViewType);
    }

    @Override
    public Task<Void> saveUser(User user) {
        return mFirebaseHelper.saveUser(user);
    }

    @Override
    public Task<DocumentSnapshot> getUser(String userId) {
        return mFirebaseHelper.getUser(userId);
    }

    @Override
    public UploadTask uploadFileToStorage(Uri uri, String path) {
        return mFirebaseHelper.uploadFileToStorage(uri, path);
    }

    @Override
    public Intent getGoogleSignInIntent() {
        return mNetworkHelper.getGoogleSignInIntent();
    }

    @Override
    public Task<Void> accountGoogleSignOut() {
        return mNetworkHelper.accountGoogleSignOut();
    }
}
