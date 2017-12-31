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
import com.bmd.android.europewelcome.data.firebase.model.PostPlace;
import com.bmd.android.europewelcome.data.firebase.model.PostText;
import com.bmd.android.europewelcome.utils.AppConstants;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

import javax.inject.Inject;

/**
 * Reads and writes the data from Firebase database.
 */

public class AppFirebaseHelper implements FirebaseHelper {

    private final FirebaseFirestore mFirestore;
    private final FirebaseAuth mAuth;
    private final FirebaseStorage mStorage;

    @Inject
    public AppFirebaseHelper() {
        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance();
    }

    //=//=// F I R E B A S E  -  A U T H E N T I C A T I O N //=//=//

    @Override
    public Task<AuthResult> createUser(String email, String password) {
        return mAuth.createUserWithEmailAndPassword(email, password);
    }

    @Override
    public Task<AuthResult> signInUser(String email, String password) {
        return mAuth.signInWithEmailAndPassword(email, password);
    }

    @Override
    public Task<AuthResult> signInWithCredential(AuthCredential credential) {
        return mAuth.signInWithCredential(credential);
    }

    @Override
    public void signOutUser() {
        mAuth.signOut();
    }

    @Override
    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    @Override
    public String getUserId(){
        return mAuth.getCurrentUser().getUid();
    }

    @Override
    public String getUserName(){
        return mAuth.getCurrentUser().getDisplayName();
    }

    @Override
    public String getUserEmail(){
        return mAuth.getCurrentUser().getEmail();
    }

    @Override
    public Uri getUserImageUrl() {
        return mAuth.getCurrentUser().getPhotoUrl();
    }

    @Override
    public void setUserName(String userName) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(userName).build();
        if(getCurrentUser()!=null){
            getCurrentUser().updateProfile(profileUpdates);
        }
    }

    @Override
    public void setUserEmail(String userEmail) {
        if(getCurrentUser()!=null) {
            getCurrentUser().updateEmail(userEmail);
        }
    }

    @Override
    public void setUserImageUrl(Uri userImageUrl) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setPhotoUri(userImageUrl).build();
        if(getCurrentUser()!=null){
            getCurrentUser().updateProfile(profileUpdates);
        }
    }


    //=//=// F I R E B A S E  -  F I R E S T O R E //=//=//

    @Override
    public CollectionReference getPostsColRef() {
        return mFirestore.collection("posts");
    }

    @Override
    public Query getPostsQueryOrderedByStars() {
        return mFirestore
                .collection(AppConstants.POSTS_COLLECTION)
                .orderBy("postStars", Query.Direction.DESCENDING);
    }

    @Override
    public Query getPostsQueryOrderedByViews() {
        return mFirestore
                .collection(AppConstants.POSTS_COLLECTION)
                .orderBy("postWatches", Query.Direction.DESCENDING);
    }

    @Override
    public Query getPostsQueryOrderedByDate() {
        return null;
    }

    @Override
    public Task<Void> savePost(Post post) {
        return mFirestore.collection("posts").document(post.getPostId()).set(post);
    }

    @Override
    public Task<Void> savePostText(String postId, PostText postText) {
        return mFirestore
                .collection(AppConstants.POSTS_COLLECTION)
                .document(postId)
                .collection(AppConstants.POST_TEXT_COLLECTION)
                .document(postText.getPostTextId())
                .set(postText);
    }

    @Override
    public Task<Void> savePostImage(String postId, PostImage postImage) {
        return mFirestore
                .collection(AppConstants.POSTS_COLLECTION)
                .document(postId)
                .collection(AppConstants.POST_IMAGE_COLLECTION)
                .document(postImage.getPostImageId())
                .set(postImage);
    }

    @Override
    public Task<Void> savePostPlace(String postId, PostPlace postPlace) {
        return mFirestore
                .collection(AppConstants.POSTS_COLLECTION)
                .document(postId)
                .collection(AppConstants.POST_PLACE_COLLECTION)
                .document(postPlace.getPostPlaceId())
                .set(postPlace);
    }

    @Override
    public Task<Void> updatePost(Post post) {
            return mFirestore.collection(AppConstants.POSTS_COLLECTION).document(post.getPostId())
                    .set(post, SetOptions.merge());
    }

    @Override
    public Task<DocumentSnapshot> getPost(String postId) {
        DocumentReference docRef
                = mFirestore.collection(AppConstants.POSTS_COLLECTION).document(postId);
        return docRef.get();
    }

    @Override
    public Task<QuerySnapshot> getPostTextList(String postId) {
        CollectionReference colRef = mFirestore
                .collection(AppConstants.POSTS_COLLECTION)
                .document(postId)
                .collection(AppConstants.POST_TEXT_COLLECTION);
        return colRef.get();
    }

    @Override
    public Task<QuerySnapshot> getPostImageList(String postId) {
        CollectionReference colRef = mFirestore
                .collection(AppConstants.POSTS_COLLECTION)
                .document(postId)
                .collection(AppConstants.POST_IMAGE_COLLECTION);
        return colRef.get();
    }

    @Override
    public Task<QuerySnapshot> getPostPlaceList(String postId) {
        CollectionReference colRef = mFirestore
                .collection(AppConstants.POSTS_COLLECTION)
                .document(postId)
                .collection(AppConstants.POST_PLACE_COLLECTION);
        return colRef.get();
    }

    //=//=// F I R E B A S E  -  S T O R A G E //=//=//


    public UploadTask uploadFileToStorage(Uri uri){
        String uuid = UUID.randomUUID().toString();
        StorageReference imageRef = mStorage.getReference().child("images/" + uuid);
        return imageRef.putFile(uri);
    }
}
