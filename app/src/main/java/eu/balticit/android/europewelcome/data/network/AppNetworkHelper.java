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

package eu.balticit.android.europewelcome.data.network;

import android.content.Context;
import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;

import javax.inject.Inject;
import javax.inject.Singleton;

import eu.balticit.android.europewelcome.R;
import eu.balticit.android.europewelcome.di.ApplicationContext;

/**
 * Created by BIT on 12/26/2017.
 */
@Singleton
public class AppNetworkHelper implements NetworkHelper {

    private GoogleSignInClient mGoogleSignInClient;

    @Inject
    AppNetworkHelper(@ApplicationContext Context context) {

        //Creates GoogleSignInClient once per app run
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.GOOGLE_WEB_CLIENT_ID_TOKEN))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
    }


    @Override
    public Intent getGoogleSignInIntent() {
        return mGoogleSignInClient.getSignInIntent();
    }

    @Override
    public Task<Void> accountGoogleSignOut() {
        return mGoogleSignInClient.signOut();
    }


}
