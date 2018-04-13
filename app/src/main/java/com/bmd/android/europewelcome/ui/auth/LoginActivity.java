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

package com.bmd.android.europewelcome.ui.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.bmd.android.europewelcome.R;
import com.bmd.android.europewelcome.ui.about.AboutFragment;
import com.bmd.android.europewelcome.ui.auth.register.RegisterFragment;
import com.bmd.android.europewelcome.ui.base.BaseActivity;
import com.bmd.android.europewelcome.ui.posts.PostsActivity;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Login Activity. There are 3 methods to get user profile: Internal FireBase, Google, Facebook.
 * <p>
 * App authentication used overall flow described in:
 * 1.FireBase Password login docs: https://firebase.google.com/docs/auth/android/password-auth
 * 2.Google profile login docs: https://firebase.google.com/docs/auth/android/google-signin
 * 3.Facebook profile login docs: https://firebase.google.com/docs/auth/android/facebook-login
 */

public class LoginActivity extends BaseActivity implements LoginMvpView {

    private static final String TAG = "LoginActivityTag";
    private static final int RC_SIGN_IN = 9001;

    @Inject
    LoginMvpPresenter<LoginMvpView> mPresenter;

    @BindView(R.id.et_login_email)
    EditText mEmailEditText;

    @BindView(R.id.et_login_password)
    EditText mPasswordEditText;

    @BindView(R.id.button_facebook_login)
    LoginButton mFacebookLoginButton;

    private CallbackManager mCallbackManager;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        getActivityComponent().inject(this);

        setUnBinder(ButterKnife.bind(this));

        mPresenter.onAttach(LoginActivity.this);

        setUp();
    }

    @Override
    protected void setUp() {
        mEmailEditText.setText(mPresenter.getLastUsedEmail());

        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        mFacebookLoginButton.setReadPermissions("email", "public_profile");
        mFacebookLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                mPresenter.firebaseAuthWithFacebook(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                LoginActivity.this.onError(R.string.login_some_error);
            }
        });
        // [END initialize_fblogin]
    }

    @OnClick(R.id.btn_login_server)
    void onServerLoginClick(View v) {
        mPresenter.onServerLoginClick(mEmailEditText.getText().toString(),
                mPasswordEditText.getText().toString());
    }

    @OnClick(R.id.btn_login_google)
    void onGoogleLoginClick(View v) {
        Intent signInIntent = mPresenter.getGoogleSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @OnClick(R.id.btn_login_facebook)
    void onFbLoginClick(View v) {
        mFacebookLoginButton.performClick();
    }

    @OnClick(R.id.btn_login_register)
    void onRegisterButtonClick(View v) {
        hideKeyboard();
        showRegisterFragment();
    }

    //Gets user data with Facebook and Google login intents
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                mPresenter.firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                //TODO: add user message on fail
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed" + e.getMessage(), e);
            }
        }
    }

    @Override
    public void openMainActivity() {
        Intent intent = PostsActivity.getStartIntent(LoginActivity.this);
        startActivity(intent);
        finish();
    }

    /**
     * Detaches {@link RegisterFragment} from this activity
     *
     * @param tag {@link RegisterFragment}
     */
    @Override
    public void onFragmentDetached(String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment != null) {
            fragmentManager
                    .beginTransaction()
                    .disallowAddToBackStack()
                    .setCustomAnimations(R.anim.slide_left, R.anim.slide_right)
                    .remove(fragment)
                    .commitNow();
        }
    }

    /**
     * Detaches {@link RegisterFragment} from this activity on phone's back button click
     */
    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(RegisterFragment.TAG);
        Fragment fragment2 = fragmentManager.findFragmentByTag(AboutFragment.TAG);
        if(fragment2 != null){
            onFragmentDetached(AboutFragment.TAG);
        } else if(fragment != null){
            onFragmentDetached(RegisterFragment.TAG);
        } else{
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDetach();
    }

    /**
     * Attaches {@link RegisterFragment} to this activity
     */
    private void showRegisterFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .setCustomAnimations(R.anim.slide_left, R.anim.slide_right)
                .add(R.id.cl_root_view, RegisterFragment.newInstance(), RegisterFragment.TAG)
                .commit();
    }

    /**
     * Attaches {@link AboutFragment} to this activity
     */
    @Override
    public void showAboutFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .setCustomAnimations(R.anim.slide_left, R.anim.slide_right)
                .add(R.id.cl_root_view, AboutFragment.newInstance(), AboutFragment.TAG)
                .commit();
    }
}
