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

package eu.balticit.android.europewelcome.data;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;

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

import eu.balticit.android.europewelcome.data.firebase.FirebaseHelper;
import eu.balticit.android.europewelcome.data.firebase.model.Post;
import eu.balticit.android.europewelcome.data.firebase.model.PostComment;
import eu.balticit.android.europewelcome.data.firebase.model.PostSection;
import eu.balticit.android.europewelcome.data.firebase.model.Rating;
import eu.balticit.android.europewelcome.data.firebase.model.User;
import eu.balticit.android.europewelcome.data.network.NetworkHelper;
import eu.balticit.android.europewelcome.data.prefs.PreferencesHelper;
import eu.balticit.android.europewelcome.di.ApplicationContext;

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
    public String getCurrentUserBirthDate() {
        return mPreferencesHelper.getCurrentUserBirthDate();
    }

    @Override
    public void setCurrentUserBirthDate(String birthDate) {
        mPreferencesHelper.setCurrentUserBirthDate(birthDate);
    }

    @Override
    public String getCurrentUserGender() {
        return mPreferencesHelper.getCurrentUserGender();
    }

    @Override
    public void setCurrentUserGender(String gender) {
        mPreferencesHelper.setCurrentUserGender(gender);
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
            String profilePicPath,
            String userBirthDate,
            String userGender) {

        setAccessToken(accessToken);
        setCurrentUserId(userId);
        setCurrentUserLoggedInMode(loggedInMode);
        setCurrentUserName(userName);
        setCurrentUserEmail(email);
        setCurrentUserProfilePicUrl(profilePicPath);
        setCurrentUserBirthDate(userBirthDate);
        setCurrentUserGender(userGender);
    }

    @Override
    public void setUserAsLoggedOut() {
        updateUserInfo(
                null,
                null,
                DataManager.LoggedInMode.LOGGED_IN_MODE_LOGGED_OUT,
                null,
                null,
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
    public Query getFreePostsQueryOrderedByStars() {
        return mFirebaseHelper.getFreePostsQueryOrderedByStars();
    }

    @Override
    public Query getFreePostsQueryOrderedByViews() {
        return mFirebaseHelper.getFreePostsQueryOrderedByViews();
    }

    @Override
    public Query getFreePostsQueryOrderedByDate() {
        return mFirebaseHelper.getFreePostsQueryOrderedByDate();
    }

    @Override
    public Query getFreePostsQueryOrderedByComments() {
        return mFirebaseHelper.getFreePostsQueryOrderedByComments();
    }

    @Override
    public Query getPremiumPostsQueryOrderedByStars() {
        return mFirebaseHelper.getPremiumPostsQueryOrderedByStars();
    }

    @Override
    public Query getPremiumPostsQueryOrderedByDate() {
        return mFirebaseHelper.getPremiumPostsQueryOrderedByDate();
    }

    @Override
    public Query getPremiumPostsQueryOrderedByComments() {
        return mFirebaseHelper.getPremiumPostsQueryOrderedByComments();
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
    public Query getBookmarkedPostsQuery(String userId) {
        return mFirebaseHelper.getBookmarkedPostsQuery(userId);
    }

    @Override
    public Query getUserPostsQuery(String userId) {
        return mFirebaseHelper.getUserPostsQuery(userId);
    }

    @Override
    public Query getUserCommentsQuery(String userId) {
        return mFirebaseHelper.getUserCommentsQuery(userId);
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
    public Task<Void> saveBookmark(String userId, Post post) {
        return mFirebaseHelper.saveBookmark(userId, post);
    }

    @Override
    public Task<Void> saveStar(String userId, Post post) {
        return mFirebaseHelper.saveStar(userId, post);
    }

    @Override
    public Task<Void> saveCommentLike(String userId, PostComment postComment) {
        return mFirebaseHelper.saveCommentLike(userId, postComment);
    }

    @Override
    public Task<Void> saveUserComment(String userId, PostComment postComment) {
        return mFirebaseHelper.saveUserComment(userId, postComment);
    }

    @Override
    public Task<Void> saveRating(Rating rating) {
        return mFirebaseHelper.saveRating(rating);
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
    public Task<Void> updatePostComment(String postId, PostComment postComment) {
        return mFirebaseHelper.updatePostComment(postId, postComment);
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
    public Task<Void> deleteBookmark(String userId, Post post) {
        return mFirebaseHelper.deleteBookmark(userId, post);
    }

    @Override
    public Task<Void> deleteStar(String userId, Post post) {
        return mFirebaseHelper.deleteStar(userId, post);
    }

    @Override
    public Task<Void> deleteCommentLike(String userId, PostComment postComment) {
        return mFirebaseHelper.deleteCommentLike(userId, postComment);
    }

    @Override
    public Task<DocumentSnapshot> getPost(String postId) {
        return mFirebaseHelper.getPost(postId);
    }

    @Override
    public Task<DocumentSnapshot> getBookmark(String userId, String postId) {
        return mFirebaseHelper.getBookmark(userId, postId);
    }

    @Override
    public Task<DocumentSnapshot> getStar(String userId, String postId) {
        return mFirebaseHelper.getStar(userId, postId);
    }

    @Override
    public Task<DocumentSnapshot> getCommentLike(String userId, String commentId) {
        return mFirebaseHelper.getCommentLike(userId,commentId);
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
    public Task<QuerySnapshot> getUserDrafts(String userId) {
        return mFirebaseHelper.getUserDrafts(userId);
    }

    @Override
    public Task<QuerySnapshot> getUserBookmarks(String userId) {
        return mFirebaseHelper.getUserBookmarks(userId);
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
    public Task<Void> updateUser(User user) {
        return mFirebaseHelper.updateUser(user);
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
