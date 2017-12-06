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

import com.bmd.android.europewelcome.data.firebase.model.Post;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.inject.Inject;

/**
 * Reads and writes the data from Firebase database.
 */

public class AppFirebaseHelper implements FirebaseHelper {

    private final FirebaseFirestore mFirestore;
    private final FirebaseAuth mAuth;

    @Inject
    public AppFirebaseHelper() {
        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public Task<AuthResult> createUser(String email, String password) {
        return mAuth.createUserWithEmailAndPassword(email, password);
    }

    @Override
    public Task<AuthResult> signInUser(String email, String password) {
        return mAuth.signInWithEmailAndPassword(email, password);
    }

    @Override
    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    public String getUserId(){
        return mAuth.getCurrentUser().getUid();
    }

    public String getUserName(){
        return mAuth.getCurrentUser().getDisplayName();
    }

    public String getUserEmail(){
        return mAuth.getCurrentUser().getEmail();
    }

    @Override
    public CollectionReference getPostsColRef() {
        return mFirestore.collection("posts");
    }

    @Override
    public Task<Void> savePost(Post post) {
        return mFirestore.collection("posts").document(post.getPostId()).set(post);
    }
}
