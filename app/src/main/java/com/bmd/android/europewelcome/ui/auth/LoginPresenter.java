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

import com.bmd.android.europewelcome.R;
import com.bmd.android.europewelcome.data.DataManager;
import com.bmd.android.europewelcome.ui.base.BasePresenter;
import com.bmd.android.europewelcome.utils.CommonUtils;

import javax.inject.Inject;

/**
 * Login Presenter
 */

public class LoginPresenter<V extends LoginMvpView>
        extends BasePresenter<V>
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

        //TODO: implement firebase login in AppDataManager class
        //getDataManager().
        getMvpView().hideLoading();
        getMvpView().openMainActivity();
    }

    @Override
    public void onGoogleLoginClick() {
        // instruct LoginActivity to initiate google login
        getMvpView().showLoading();
        //TODO: implement firebase login with Google in AppDataManager class
        getMvpView().hideLoading();
        getMvpView().openMainActivity();

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
