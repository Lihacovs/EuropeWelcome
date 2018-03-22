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
import com.bmd.android.europewelcome.data.firebase.model.PostComment;
import com.bmd.android.europewelcome.data.firebase.model.PostSection;
import com.bmd.android.europewelcome.data.firebase.model.User;
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
    public Task<AuthResult> createFirebaseUser(String email, String password) {
        return mAuth.createUserWithEmailAndPassword(email, password);
    }

    @Override
    public Task<AuthResult> signInFirebaseUser(String email, String password) {
        return mAuth.signInWithEmailAndPassword(email, password);
    }

    @Override
    public Task<AuthResult> signInFirebaseWithCredential(AuthCredential credential) {
        return mAuth.signInWithCredential(credential);
    }

    @Override
    public void signOutFirebaseUser() {
        mAuth.signOut();
    }

    @Override
    public FirebaseUser getFirebaseUser() {
        return mAuth.getCurrentUser();
    }

    @Override
    public String getFirebaseUserId() {
        return mAuth.getCurrentUser().getUid();
    }

    @Override
    public String getFirebaseUserName() {
        return mAuth.getCurrentUser().getDisplayName();
    }

    @Override
    public String getFirebaseUserEmail() {
        return mAuth.getCurrentUser().getEmail();
    }

    //TODO: check lint's nullPointerExceptions
    @Override
    public String getFirebaseUserImageUrl() {
        return mAuth.getCurrentUser().getPhotoUrl().toString();
    }

    @Override
    public void setFirebaseUserName(String userName) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(userName).build();
        if (getFirebaseUser() != null) {
            getFirebaseUser().updateProfile(profileUpdates);
        }
    }

    @Override
    public void setFirebaseUserEmail(String userEmail) {
        if (getFirebaseUser() != null) {
            getFirebaseUser().updateEmail(userEmail);
        }
    }

    @Override
    public void setFirebaseUserImageUrl(String userImageUrl) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setPhotoUri(Uri.parse(userImageUrl)).build();
        if (getFirebaseUser() != null) {
            getFirebaseUser().updateProfile(profileUpdates);
        }
    }

    @Override
    public Task<Void> setFirebaseUserProfile(String userName, String userPhotoUrl) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(userName)
                .setPhotoUri(Uri.parse(userPhotoUrl))
                .build();
        if (getFirebaseUser() != null) {
            return getFirebaseUser().updateProfile(profileUpdates);
        } else {
            return null;
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
    public Query getPostCommentsQuery(String postId) {
        return mFirestore
                .collection(AppConstants.POSTS_COLLECTION)
                .document(postId)
                .collection(AppConstants.COMMENT_COLLECTION)
                .orderBy("postCommentTimestamp", Query.Direction.DESCENDING);
    }

    @Override
    public Query getPostSectionQuery(String postId) {
        return mFirestore
                .collection(AppConstants.POSTS_COLLECTION)
                .document(postId)
                .collection(AppConstants.POST_SECTION_COLLECTION)
                .orderBy("timeStamp", Query.Direction.ASCENDING);
    }

    @Override
    public Task<Void> savePost(Post post) {
        return mFirestore.collection("posts").document(post.getPostId()).set(post);
    }

    @Override
    public Task<Void> savePostSection(PostSection postSection, String postId) {
        return mFirestore
                .collection(AppConstants.POSTS_COLLECTION)
                .document(postId)
                .collection(AppConstants.POST_SECTION_COLLECTION)
                .document(postSection.getPostSectionId())
                .set(postSection);
    }

    @Override
    public Task<Void> saveComment(String postId, PostComment postComment) {
        return mFirestore
                .collection(AppConstants.POSTS_COLLECTION)
                .document(postId)
                .collection(AppConstants.COMMENT_COLLECTION)
                .document(postComment.getPostCommentId())
                .set(postComment);
    }

    @Override
    public Task<Void> updatePost(Post post) {
        return mFirestore.collection(AppConstants.POSTS_COLLECTION)
                .document(post.getPostId())
                .set(post, SetOptions.merge());
    }


    @Override
    public Task<Void> updatePostSection(String postId, PostSection postSection) {
        return mFirestore.collection(AppConstants.POSTS_COLLECTION)
                .document(postId)
                .collection(AppConstants.POST_SECTION_COLLECTION)
                .document(postSection.getPostSectionId())
                .set(postSection, SetOptions.merge());
    }

    @Override
    public Task<Void> deletePost(Post post) {
        return mFirestore.collection(AppConstants.POSTS_COLLECTION)
                .document(post.getPostId())
                .delete();
    }

    @Override
    public Task<Void> deletePostSection(String postId, PostSection postSection) {
        return mFirestore.collection(AppConstants.POSTS_COLLECTION)
                .document(postId)
                .collection(AppConstants.POST_SECTION_COLLECTION)
                .document(postSection.getPostSectionId())
                .delete();
    }

    @Override
    public Task<DocumentSnapshot> getPost(String postId) {
        DocumentReference docRef
                = mFirestore.collection(AppConstants.POSTS_COLLECTION).document(postId);
        return docRef.get();
    }

    @Override
    public Task<QuerySnapshot> getFirstPostSection(String postId, String sectionViewType) {
        return mFirestore
                .collection(AppConstants.POSTS_COLLECTION)
                .document(postId)
                .collection(AppConstants.POST_SECTION_COLLECTION)
                .whereEqualTo("postSectionViewType", sectionViewType)
                .orderBy("timeStamp", Query.Direction.ASCENDING)
                .limit(1)
                .get();
    }

    @Override
    public Task<Void> saveUser(User user) {
        return mFirestore.collection(AppConstants.USERS_COLLECTION)
                .document(user.getUserId())
                .set(user);
    }

    @Override
    public Task<DocumentSnapshot> getUser(String userId) {
        DocumentReference docRef
                = mFirestore.collection(AppConstants.USERS_COLLECTION).document(userId);
        return docRef.get();
    }

    //=//=// F I R E B A S E  -  S T O R A G E //=//=//

    public UploadTask uploadFileToStorage(Uri uri, String path) {
        String uuid = UUID.randomUUID().toString();
        StorageReference imageRef = mStorage.getReference().child(path + uuid);
        return imageRef.putFile(uri);
    }
}
