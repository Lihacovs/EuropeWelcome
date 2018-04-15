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

package eu.balticit.android.europewelcome.data.firebase;

import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.UploadTask;

import eu.balticit.android.europewelcome.data.firebase.model.Post;
import eu.balticit.android.europewelcome.data.firebase.model.PostComment;
import eu.balticit.android.europewelcome.data.firebase.model.PostSection;
import eu.balticit.android.europewelcome.data.firebase.model.Rating;
import eu.balticit.android.europewelcome.data.firebase.model.User;

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

    Query getFreePostsQueryOrderedByStars();

    Query getFreePostsQueryOrderedByViews();

    Query getFreePostsQueryOrderedByDate();

    Query getFreePostsQueryOrderedByComments();

    Query getPremiumPostsQueryOrderedByStars();

    Query getPremiumPostsQueryOrderedByDate();

    Query getPremiumPostsQueryOrderedByComments();

    Query getPostCommentsQuery(String postId);

    Query getPostSectionQuery(String postId);

    Query getPostAsDraftQuery(String userId);

    Query getBookmarkedPostsQuery(String userId);

    Query getUserPostsQuery(String userId);

    Query getUserCommentsQuery(String userId);

    Task<Void> savePost(Post post);

    Task<Void> savePostSection(PostSection postSection, String postId);

    Task<Void> saveComment(String postId, PostComment postComment);

    Task<Void> saveBookmark(String userId, Post post);

    Task<Void> saveStar(String userId, Post post);

    Task<Void> saveCommentLike(String userId, PostComment postComment);

    Task<Void> saveUserComment(String userId, PostComment postComment);

    Task<Void> saveRating(Rating rating);

    Task<Void> updatePost(Post post);

    Task<Void> updatePostSection(String postId ,PostSection postSection);

    Task<Void> updatePostComment(String postId ,PostComment postComment);

    Task<Void> deletePost(Post post);

    Task<Void> deletePostSection(String postId ,PostSection postSection);

    Task<Void> deleteBookmark(String userId ,Post post);

    Task<Void> deleteStar(String userId ,Post post);

    Task<Void> deleteCommentLike(String userId ,PostComment postComment);

    Task<DocumentSnapshot> getPost(String postId);

    Task<DocumentSnapshot> getBookmark(String userId, String postId);

    Task<DocumentSnapshot> getStar(String userId, String postId);

    Task<DocumentSnapshot> getCommentLike(String userId, String commentId);

    Task<QuerySnapshot> getFirstPostSectionCollection(String postId);

    Task<QuerySnapshot> getFirstPostSection(String postId, String sectionViewType);

    Task<QuerySnapshot> getUserDrafts(String userId);

    Task<QuerySnapshot> getUserBookmarks(String userId);

    Task<Void> saveUser(User user);

    Task<DocumentSnapshot> getUser(String userId);

    Task<Void> updateUser(User user);

    //=//=// F I R E B A S E  -  S T O R A G E //=//=//

    UploadTask uploadFileToStorage(Uri uri, String path);

}
