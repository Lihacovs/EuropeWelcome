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

package com.bmd.android.europewelcome.data.firebase;

import android.net.Uri;

import com.bmd.android.europewelcome.data.firebase.model.Post;
import com.bmd.android.europewelcome.data.firebase.model.PostImage;
import com.bmd.android.europewelcome.data.firebase.model.PostText;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.storage.UploadTask;

/**
 * Interface decouples any specific implementation of the {@link AppFirebaseHelper}
 * and hence makes it as plug and play unit
 */

public interface FirebaseHelper {
    //firebase methods wil go here
    Task<AuthResult> createUser(String email, String password);

    Task<AuthResult> signInUser(String email, String password);

    FirebaseUser getCurrentUser();

    String getUserId();

    String getUserName();

    String getUserEmail();

    CollectionReference getPostsColRef();

    Task<Void> savePost(Post post);

    Task<Void> savePostText(String postId, PostText postText);

    Task<Void> savePostImage(String postId, PostImage postImage);

    UploadTask uploadFileToStorage(Uri uri);
}
