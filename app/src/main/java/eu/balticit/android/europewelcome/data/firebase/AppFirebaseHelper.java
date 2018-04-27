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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
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

import eu.balticit.android.europewelcome.data.firebase.model.Post;
import eu.balticit.android.europewelcome.data.firebase.model.PostComment;
import eu.balticit.android.europewelcome.data.firebase.model.PostSection;
import eu.balticit.android.europewelcome.data.firebase.model.Rating;
import eu.balticit.android.europewelcome.data.firebase.model.User;
import eu.balticit.android.europewelcome.utils.AppConstants;

/**
 * Reads and writes the data from Firebase database.
 */

public class AppFirebaseHelper implements FirebaseHelper {

    private final FirebaseFirestore mFirestore;
    private final FirebaseAuth mAuth;
    private final FirebaseStorage mStorage;

    @Inject
    AppFirebaseHelper() {
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
    public Query getPostsQuery() {
        return mFirestore.collection("posts")
                .whereEqualTo("postPublished", true)
                .orderBy("postCreationTimestamp", Query.Direction.DESCENDING);
    }

    @Override
    public Query getFreePostsQueryOrderedByStars() {
        return mFirestore
                .collection(AppConstants.POSTS_COLLECTION)
                .whereEqualTo("postAccepted", true)
                .whereEqualTo("postPublished", true)
                .whereEqualTo("postPremium", false)
                .orderBy("postStars", Query.Direction.DESCENDING)
                .orderBy("postCreationTimestamp", Query.Direction.DESCENDING);
    }

    @Override
    public Query getFreePostsQueryOrderedByViews() {
        return mFirestore
                .collection(AppConstants.POSTS_COLLECTION)
                .whereEqualTo("postAccepted", true)
                .whereEqualTo("postPublished", true)
                .whereEqualTo("postPremium", false)
                .orderBy("postWatches", Query.Direction.DESCENDING);
    }

    @Override
    public Query getFreePostsQueryOrderedByDate() {
        return mFirestore.collection(AppConstants.POSTS_COLLECTION)
                .whereEqualTo("postAccepted", true)
                .whereEqualTo("postPublished", true)
                .whereEqualTo("postPremium", false)
                .orderBy("postCreationTimestamp", Query.Direction.DESCENDING);
    }

    @Override
    public Query getFreePostsQueryOrderedByComments() {
        return mFirestore.collection(AppConstants.POSTS_COLLECTION)
                .whereEqualTo("postAccepted", true)
                .whereEqualTo("postPublished", true)
                .whereEqualTo("postPremium", false)
                .orderBy("postComments", Query.Direction.DESCENDING)
                .orderBy("postCreationTimestamp", Query.Direction.DESCENDING);
    }

    @Override
    public Query getNotAcceptedPostsQuery() {
        return mFirestore.collection(AppConstants.POSTS_COLLECTION)
                .whereEqualTo("postAccepted", false)
                .whereEqualTo("postPublished", true)
                .whereEqualTo("postPremium", false)
                .orderBy("postCreationTimestamp", Query.Direction.DESCENDING);
    }

    @Override
    public Query getPremiumPostsQueryOrderedByStars() {
        return mFirestore
                .collection(AppConstants.POSTS_COLLECTION)
                .whereEqualTo("postAccepted", true)
                .whereEqualTo("postPublished", true)
                .whereEqualTo("postPremium", true)
                .orderBy("postStars", Query.Direction.DESCENDING)
                .orderBy("postCreationTimestamp", Query.Direction.DESCENDING);
    }

    @Override
    public Query getPremiumPostsQueryOrderedByDate() {
        return mFirestore.collection(AppConstants.POSTS_COLLECTION)
                .whereEqualTo("postAccepted", true)
                .whereEqualTo("postPublished", true)
                .whereEqualTo("postPremium", true)
                .orderBy("postCreationTimestamp", Query.Direction.DESCENDING);
    }

    @Override
    public Query getPremiumPostsQueryOrderedByComments() {
        return mFirestore.collection(AppConstants.POSTS_COLLECTION)
                .whereEqualTo("postAccepted", true)
                .whereEqualTo("postPublished", true)
                .whereEqualTo("postPremium", true)
                .orderBy("postComments", Query.Direction.DESCENDING)
                .orderBy("postCreationTimestamp", Query.Direction.DESCENDING);
    }

    @Override
    public Query getPostCommentsQuery(String postId) {
        return mFirestore
                .collection(AppConstants.POSTS_COLLECTION)
                .document(postId)
                .collection(AppConstants.POST_COMMENTS_COLLECTION)
                .orderBy("postCommentStars", Query.Direction.DESCENDING)
                .orderBy("postCommentTimestamp", Query.Direction.ASCENDING);
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
    public Query getPostAsDraftQuery(String userId) {
        return mFirestore
                .collection(AppConstants.POSTS_COLLECTION)
                .whereEqualTo("postAsDraft", true)
                .whereEqualTo("postAuthorId", userId)
                .orderBy("postCreationTimestamp", Query.Direction.DESCENDING);
    }

    @Override
    public Query getBookmarkedPostsQuery(String userId) {
        return  mFirestore
                .collection(AppConstants.USERS_COLLECTION)
                .document(userId)
                .collection(AppConstants.BOOKMARKS_COLLECTION)
                .orderBy("postCreationTimestamp", Query.Direction.DESCENDING);
    }

    @Override
    public Query getUserPostsQuery(String userId) {
        return mFirestore
                .collection(AppConstants.POSTS_COLLECTION)
                .whereEqualTo("postAuthorId", userId)
                .whereEqualTo("postPublished", true)
                .orderBy("postCreationTimestamp", Query.Direction.DESCENDING);
    }

    @Override
    public Query getUserCommentsQuery(String userId) {
        return mFirestore
                .collection(AppConstants.USERS_COLLECTION)
                .document(userId)
                .collection(AppConstants.USER_COMMENTS_COLLECTION)
                .orderBy("postCommentTimestamp", Query.Direction.DESCENDING);
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
                .collection(AppConstants.POST_COMMENTS_COLLECTION)
                .document(postComment.getPostCommentId())
                .set(postComment);
    }

    @Override
    public Task<Void> saveBookmark(String userId, Post post) {
        return mFirestore.collection(AppConstants.USERS_COLLECTION)
                .document(userId)
                .collection(AppConstants.BOOKMARKS_COLLECTION)
                .document(post.getPostId())
                .set(post);
    }

    @Override
    public Task<Void> saveStar(String userId, Post post) {
        return mFirestore.collection(AppConstants.USERS_COLLECTION)
                .document(userId)
                .collection(AppConstants.STARS_COLLECTION)
                .document(post.getPostId())
                .set(post);
    }

    @Override
    public Task<Void> saveCommentLike(String userId, PostComment postComment) {
        return mFirestore.collection(AppConstants.USERS_COLLECTION)
                .document(userId)
                .collection(AppConstants.COMMENT_LIKES_COLLECTION)
                .document(postComment.getPostCommentId())
                .set(postComment);
    }

    @Override
    public Task<Void> saveUserComment(String userId, PostComment postComment) {
        return mFirestore.collection(AppConstants.USERS_COLLECTION)
                .document(userId)
                .collection(AppConstants.USER_COMMENTS_COLLECTION)
                .document(postComment.getPostCommentId())
                .set(postComment);
    }

    @Override
    public Task<Void> saveRating(Rating rating) {
        return mFirestore.collection(AppConstants.RATINGS_COLLECTION)
                .document(rating.getRatingId())
                .set(rating);
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
    public Task<Void> updatePostComment(String postId, PostComment postComment) {
        return mFirestore.collection(AppConstants.POSTS_COLLECTION)
                .document(postId)
                .collection(AppConstants.POST_COMMENTS_COLLECTION)
                .document(postComment.getPostCommentId())
                .set(postComment, SetOptions.merge());
    }

    @Override
    public Task<Void> deletePost(Post post) {
        //Firstly delete collection under the Post document

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
    public Task<Void> deleteBookmark(String userId, Post post) {
        return mFirestore.collection(AppConstants.USERS_COLLECTION)
                .document(userId)
                .collection(AppConstants.BOOKMARKS_COLLECTION)
                .document(post.getPostId())
                .delete();
    }

    @Override
    public Task<Void> deleteStar(String userId, Post post) {
        return mFirestore.collection(AppConstants.USERS_COLLECTION)
                .document(userId)
                .collection(AppConstants.STARS_COLLECTION)
                .document(post.getPostId())
                .delete();
    }

    @Override
    public Task<Void> deleteCommentLike(String userId, PostComment postComment) {
        return mFirestore.collection(AppConstants.USERS_COLLECTION)
                .document(userId)
                .collection(AppConstants.COMMENT_LIKES_COLLECTION)
                .document(postComment.getPostCommentId())
                .delete();
    }

    @Override
    public Task<DocumentSnapshot> getPost(String postId) {
        DocumentReference docRef
                = mFirestore.collection(AppConstants.POSTS_COLLECTION).document(postId);
        return docRef.get();
    }

    @Override
    public Task<DocumentSnapshot> getBookmark(String userId, String postId) {
        return mFirestore.collection(AppConstants.USERS_COLLECTION)
                .document(userId)
                .collection(AppConstants.BOOKMARKS_COLLECTION)
                .document(postId)
                .get();
    }

    @Override
    public Task<DocumentSnapshot> getStar(String userId, String postId) {
        return mFirestore.collection(AppConstants.USERS_COLLECTION)
                .document(userId)
                .collection(AppConstants.STARS_COLLECTION)
                .document(postId)
                .get();
    }

    @Override
    public Task<DocumentSnapshot> getCommentLike(String userId, String commentId) {
        return mFirestore.collection(AppConstants.USERS_COLLECTION)
                .document(userId)
                .collection(AppConstants.COMMENT_LIKES_COLLECTION)
                .document(commentId)
                .get();
    }

    @Override
    public Task<QuerySnapshot> getFirstPostSectionCollection(String postId) {
        return mFirestore
                .collection(AppConstants.POSTS_COLLECTION)
                .document(postId)
                .collection(AppConstants.POST_SECTION_COLLECTION)
                .get();
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
    public Task<QuerySnapshot> getUserDrafts(String userId) {
        return mFirestore
                .collection(AppConstants.POSTS_COLLECTION)
                .whereEqualTo("postAuthorId", userId)
                .whereEqualTo("postAsDraft", true)
                .get();
    }

    @Override
    public Task<QuerySnapshot> getUserBookmarks(String userId) {
        return mFirestore
                .collection(AppConstants.USERS_COLLECTION)
                .document(userId)
                .collection(AppConstants.BOOKMARKS_COLLECTION)
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

    @Override
    public Task<Void> updateUser(User user) {
        return mFirestore.collection(AppConstants.USERS_COLLECTION)
                .document(user.getUserId())
                .set(user, SetOptions.merge());
    }

    //=//=// F I R E B A S E  -  S T O R A G E //=//=//

    public UploadTask uploadFileToStorage(Uri uri, String path) {
        String uuid = UUID.randomUUID().toString();
        StorageReference imageRef = mStorage.getReference().child(path + uuid);
        return imageRef.putFile(uri);
    }
}
