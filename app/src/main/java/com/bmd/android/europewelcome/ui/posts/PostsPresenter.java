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

package com.bmd.android.europewelcome.ui.posts;

import android.util.Log;

import com.bmd.android.europewelcome.data.DataManager;
import com.bmd.android.europewelcome.ui.base.BasePresenter;

import javax.inject.Inject;

/**
 * Posts Presenter
 */

public class PostsPresenter <V extends PostsMvpView> extends BasePresenter<V>
        implements PostsMvpPresenter<V>{

    private static final String TAG = "PostsPresenter";

    @Inject
    public PostsPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void onDrawerOptionProfileClick() {
        if(getDataManager().getCurrentUserId() == null || getDataManager().getCurrentUserId().isEmpty()){
            getMvpView().closeNavigationDrawer();
            getMvpView().openLoginActivity();
        }else{
            getMvpView().closeNavigationDrawer();
            getMvpView().openProfileActivity();
        }
    }

    @Override
    public void onDrawerOptionBookmarksClick() {

    }

    @Override
    public void onDrawerOptionDraftsClick() {

    }

    @Override
    public void onDrawerOptionPremiumClick() {

    }

    @Override
    public void onDrawerOptionAboutClick() {
        getMvpView().closeNavigationDrawer();
        getMvpView().showAboutFragment();
    }

    @Override
    public void onDrawerOptionLogoutClick() {
        //logout user here

        getMvpView().showLoading();

        if (!isViewAttached()) {
            return;
        }

        getDataManager().setUserAsLoggedOut();
        getDataManager().signOutFirebaseUser();
        getDataManager().accountGoogleSignOut();
        getMvpView().hideLoading();
        getMvpView().openLoginActivity();

    }

    @Override
    public void onFabClick() {
        if(getDataManager().getCurrentUserId() == null || getDataManager().getCurrentUserId().isEmpty()){
            getMvpView().openLoginActivity();
        }else{
            getMvpView().openNewPostActivity();
        }
    }

    @Override
    public void onViewInitialized() {
        //get all posts here
        Log.d(TAG, "UserName:" + getDataManager().getCurrentUserName());
        Log.d(TAG, "UserEmail:" + getDataManager().getCurrentUserEmail());
        Log.d(TAG, "UserPicUrl:" + getDataManager().getCurrentUserProfilePicUrl());
    }

    @Override
    public void onNavMenuCreated() {
        if (!isViewAttached()) {
            return;
        }
        getMvpView().updateAppVersion();

        String currentUserName = getDataManager().getCurrentUserName();
        if (currentUserName != null && !currentUserName.isEmpty()) {
            getMvpView().updateUserName(currentUserName);
        }

        String currentUserEmail = getDataManager().getCurrentUserEmail();
        if (currentUserEmail != null && !currentUserEmail.isEmpty()) {
            getMvpView().updateUserEmail(currentUserEmail);
        }

        String profilePicUrl = getDataManager().getCurrentUserProfilePicUrl();
        if (profilePicUrl != null && !profilePicUrl.isEmpty()) {
            getMvpView().updateUserProfilePic(profilePicUrl);
        }
    }

    @Override
    public String getCurrentUserId() {
        return getDataManager().getCurrentUserId();
    }

    @Override
    public void onDrawerRateUsClick() {
        getMvpView().closeNavigationDrawer();
        getMvpView().showRateUsDialog();
    }
}
