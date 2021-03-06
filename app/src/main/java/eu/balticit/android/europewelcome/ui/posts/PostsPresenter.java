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

package eu.balticit.android.europewelcome.ui.posts;

import eu.balticit.android.europewelcome.R;
import eu.balticit.android.europewelcome.data.DataManager;
import eu.balticit.android.europewelcome.data.firebase.model.User;
import eu.balticit.android.europewelcome.ui.base.BasePresenter;

import javax.inject.Inject;

/**
 * Posts Presenter
 */

public class PostsPresenter<V extends PostsMvpView> extends BasePresenter<V>
        implements PostsMvpPresenter<V> {

    @SuppressWarnings("unused")
    private static final String TAG = "PostsPresenter";

    @Inject
    PostsPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public boolean checkUserSigned() {
        return getDataManager().getCurrentUserId() != null && !getDataManager().getCurrentUserId().isEmpty();
    }

    @Override
    public void onDrawerOptionProfileClick() {
        if (checkUserSigned()) {
            getMvpView().closeNavigationDrawer();
            getMvpView().openProfileActivity();
        } else {
            getMvpView().closeNavigationDrawer();
            getMvpView().openLoginActivity();
        }
    }

    @Override
    public void onDrawerOptionBookmarksClick() {
        if (checkUserSigned()) {
            getMvpView().closeNavigationDrawer();
            getMvpView().openBookmarksActivity();
        } else {
            getMvpView().closeNavigationDrawer();
            getMvpView().openLoginActivity();
        }
    }

    @Override
    public void onDrawerOptionDraftsClick() {
        if (checkUserSigned()) {
            getMvpView().closeNavigationDrawer();
            getMvpView().openDraftsActivity();
        } else {
            getMvpView().closeNavigationDrawer();
            getMvpView().openLoginActivity();
        }
    }

    @Override
    public void onDrawerOptionPremiumClick() {
        getMvpView().closeNavigationDrawer();
        getMvpView().showLoading();
        if (checkUserSigned()) {
            getDataManager().getUser(getCurrentUserId())
                    .addOnSuccessListener(documentSnapshot -> {
                        User user = documentSnapshot.toObject(User.class);
                        if (user != null) {
                            if (user.isUserPremium()) {
                                getMvpView().hideLoading();
                                getMvpView().openPremiumActivity();
                            } else {
                                getMvpView().hideLoading();
                                getMvpView().showBuyPremiumDialog();
                            }
                        } else {
                            getMvpView().hideLoading();
                        }
                    })
                    .addOnFailureListener(e -> {
                        getMvpView().hideLoading();
                        getMvpView().onError(R.string.posts_some_error);
                    });
        } else {
            getMvpView().hideLoading();
            getMvpView().showBuyPremiumDialog();
        }
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
        getMvpView().reselectTab(0);
        getMvpView().disablePagerScroll();
        getMvpView().openLoginActivity();

    }

    @Override
    public void onNewPostClick() {
        if (checkUserSigned()) {
            getMvpView().openNewPostActivity();
        } else {
            getMvpView().openLoginActivity();
        }
    }

    @Override
    public void onViewInitialized() {
        //get all posts here
    }

    @Override
    public void onNavMenuCreated() {
        if (!isViewAttached()) {
            return;
        }
        getMvpView().updateAppVersion();

        if (checkUserSigned()) {
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
        } else {
            getMvpView().updateUserName("EuropeWelcome");
            getMvpView().updateUserEmail("Your way to Europe");
            getMvpView().setDefaultUserImage();
        }

        getMvpView().chooseLoginAction();
    }

    @Override
    public String getCurrentUserId() {
        return getDataManager().getCurrentUserId();
    }

    @Override
    public void checkUserHasPremium(int tabPosition) {
        getMvpView().showLoading();
        if (checkUserSigned()) {
            getDataManager().getUser(getCurrentUserId())
                    .addOnSuccessListener(documentSnapshot -> {
                        User user = documentSnapshot.toObject(User.class);
                        if (user != null) {
                            if (user.isUserPremium()) {
                                getMvpView().hideLoading();
                                getMvpView().showPremiumTab(tabPosition);
                                getMvpView().enablePagerScroll();
                            } else {
                                getMvpView().hideLoading();
                                getMvpView().reselectTab(tabPosition - 1);
                                getMvpView().showBuyPremiumDialog();
                            }
                        } else {
                            getMvpView().hideLoading();
                        }
                    })
                    .addOnFailureListener(e -> {
                        getMvpView().hideLoading();
                        getMvpView().reselectTab(tabPosition - 1);
                        getMvpView().onError(R.string.posts_some_error);
                    });
        } else {
            getMvpView().hideLoading();
            getMvpView().reselectTab(tabPosition - 1);
            getMvpView().showBuyPremiumDialog();
        }
    }

    @Override
    public void makeUserPremium() {
        getMvpView().showLoading();
        getDataManager().getUser(getCurrentUserId())
                .addOnSuccessListener(documentSnapshot -> {
                    User user = documentSnapshot.toObject(User.class);
                    if (user != null) {
                        user.setUserPremium(true);
                        getDataManager().updateUser(user)
                                .addOnSuccessListener(aVoid -> {
                                    getMvpView().hideLoading();
                                    getMvpView().showPremiumTab(1);
                                    getMvpView().enablePagerScroll();
                                    getMvpView().showMessage(R.string.posts_thanks_for_premium);
                                })
                                .addOnFailureListener(e -> {
                                    getMvpView().hideLoading();
                                    getMvpView().onError(R.string.posts_some_error);
                                });
                    } else {
                        getMvpView().hideLoading();
                    }
                })
                .addOnFailureListener(e -> {
                    getMvpView().hideLoading();
                    getMvpView().onError(R.string.posts_some_error);
                });
    }

    @Override
    public void onDrawerRateUsClick() {
        getMvpView().closeNavigationDrawer();
        getMvpView().showRateUsDialog();
    }
}
