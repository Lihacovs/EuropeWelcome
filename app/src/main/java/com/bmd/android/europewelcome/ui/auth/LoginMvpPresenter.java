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

import com.bmd.android.europewelcome.ui.base.MvpPresenter;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

/**
 * Presenter interface for {@link LoginPresenter}
 */

public interface LoginMvpPresenter <V extends LoginMvpView> extends MvpPresenter<V> {

    void onServerLoginClick(String email, String password);

    void firebaseAuthWithGoogle(GoogleSignInAccount acct);

    void onFacebookLoginClick();

}
