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

package com.bmd.android.europewelcome.data.firebase;

import android.net.Uri;

import com.bmd.android.europewelcome.data.firebase.model.Post;
import com.bmd.android.europewelcome.data.firebase.model.PostComment;
import com.bmd.android.europewelcome.data.firebase.model.PostSection;
import com.bmd.android.europewelcome.data.firebase.model.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.UploadTask;

/**
 * Interface decouples any specific implementation of the {@link AppFirebaseHelper}
 * and hence makes it as plug and play unit
 */

public interface FirebaseHelper {

    //=//=// F I R E B A S E  -  A U T H E N T I C A T I O N //=//=//

    Task<AuthResult> createFirebaseUser(String email, String password);

    Task<AuthResult> signInFirebaseUser(String email, String password);

    Task<AuthResult> signInFirebaseWithCredential(AuthCredential credential);

    void signOutFirebaseUser();

    FirebaseUser getFirebaseUser();

    String getFirebaseUserId();

    String getFirebaseUserName();

    String getFirebaseUserEmail();

    String getFirebaseUserImageUrl();

    void setFirebaseUserName(String userName);

    void setFirebaseUserEmail(String userEmail);

    void setFirebaseUserImageUrl(String userImageUrl);

    Task<Void> setFirebaseUserProfile(String userName, String userPhotoUrl);


    //=//=// F I R E B A S E  -  F I R E S T O R E //=//=//

    Query getPostsQuery();

    Query getPostsQueryOrderedByStars();

    Query getPostsQueryOrderedByViews();

    Query getPostsQueryOrderedByDate();

    Query getPostCommentsQuery(String postId);

    Query getPostSectionQuery(String postId);

    Query getPostAsDraftQuery(String userId);

    Task<Void> savePost(Post post);

    Task<Void> savePostSection(PostSection postSection, String postId);

    Task<Void> saveComment(String postId, PostComment postComment);

    Task<Void> updatePost(Post post);

    Task<Void> updatePostSection(String postId ,PostSection postSection);

    Task<Void> deletePost(Post post);

    Task<Void> deletePostSection(String postId ,PostSection postSection);

    Task<DocumentSnapshot> getPost(String postId);

    Task<QuerySnapshot> getFirstPostSectionCollection(String postId);

    Task<QuerySnapshot> getFirstPostSection(String postId, String sectionViewType);

    Task<Void> saveUser(User user);

    Task<DocumentSnapshot> getUser(String userId);

    //=//=// F I R E B A S E  -  S T O R A G E //=//=//

    UploadTask uploadFileToStorage(Uri uri, String path);

}
