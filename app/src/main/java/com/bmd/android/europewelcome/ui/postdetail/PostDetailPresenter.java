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

package com.bmd.android.europewelcome.ui.postdetail;

import android.support.annotation.NonNull;

import com.bmd.android.europewelcome.R;
import com.bmd.android.europewelcome.data.DataManager;
import com.bmd.android.europewelcome.data.firebase.model.Post;
import com.bmd.android.europewelcome.data.firebase.model.PostComment;
import com.bmd.android.europewelcome.ui.base.BasePresenter;
import com.bmd.android.europewelcome.utils.CommonUtils;
import com.google.firebase.firestore.Query;

import javax.inject.Inject;

/**
 * Post Detail Presenter
 */
public class PostDetailPresenter<V extends PostDetailMvpView> extends BasePresenter<V> implements
        PostDetailMvpPresenter<V> {

    private static final String TAG = "PostDetailPresenter";

    private String mPostId;
    private Post mPost;

    @Inject
    PostDetailPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void setPostId(String postId) {
        mPostId = postId;
    }

    @Override
    public void getPost(String postId) {
        getDataManager().getPost(postId).addOnSuccessListener(documentSnapshot -> {
            mPost = documentSnapshot.toObject(Post.class);

            getMvpView().setPostUserImage(mPost.getPostAuthorImageUrl());
            getMvpView().setPostUserName(mPost.getPostAuthorName());
            getMvpView().setPostCreationDate(mPost.getPostCreationDate());

            getMvpView().setPostNewCommentUserImage(getDataManager()
                    .getCurrentUserProfilePicUrl());

            getMvpView().setPostStars(String.valueOf(mPost.getPostStars()));
            getMvpView().setPostComments(String.valueOf(mPost.getPostComments()));

            checkPostBookmarkedByUser(mPost);
            checkPostStarRatedByUser(mPost);

            getMvpView().hideLoading();
        }).addOnFailureListener(e -> {
            getMvpView().hideLoading();
            getMvpView().showMessage("Unable to get Data");
        });
    }

    @Override
    public Query getPostCommentsQuery() {
        return getDataManager().getPostCommentsQuery(mPostId);
    }

    @Override
    public Query getPostSectionQuery() {
        return getDataManager().getPostSectionQuery(mPostId);
    }

    @Override
    public String getPostAuthorId() {
        return mPost.getPostAuthorId();
    }

    //Checks if post bookmarked by current user and updates UI accordingly
    @Override
    public void checkPostBookmarkedByUser(Post post) {
        String currentUserId = getDataManager().getCurrentUserId();
        if (currentUserId != null) {
            getDataManager().getBookmark(currentUserId, post.getPostId())
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            getMvpView().setBookmarkedIcon();
                        } else {
                            getMvpView().setNotBookmarkedIcon();
                        }
                    }).addOnFailureListener(e -> getMvpView().onError("Some Error"));
        }
    }

    //Checks if post rated with star by current user and updates UI accordingly
    @Override
    public void checkPostStarRatedByUser(Post post) {
        String currentUserId = getDataManager().getCurrentUserId();
        if (currentUserId != null) {
            getDataManager().getStar(currentUserId, post.getPostId())
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            getMvpView().setStarRatedIcon();
                        } else {
                            getMvpView().setNotStarRatedIcon();
                        }
                    }).addOnFailureListener(e -> getMvpView().onError("Some Error"));
        }
    }

    //Checks if user rated post with star and updates UI accordingly
    @Override
    public void addOrRemoveStar() {
        String currentUserId = getDataManager().getCurrentUserId();
        if (currentUserId != null) {
            getDataManager().getStar(currentUserId, mPost.getPostId())
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            int newStarCount = mPost.getPostStars() - 1;
                            mPost.setPostStars(newStarCount);
                            updatePost(mPost);
                            getMvpView().setNotStarRatedIcon();
                            getMvpView().setPostStars(String.valueOf(newStarCount));
                            getDataManager().deleteStar(currentUserId, mPost)
                                    .addOnSuccessListener(aVoid -> getMvpView().onError("Star removed"));
                        } else {
                            int newStarCount = mPost.getPostStars() + 1;
                            mPost.setPostStars(newStarCount);
                            updatePost(mPost);
                            getMvpView().setStarRatedIcon();
                            getMvpView().setPostStars(String.valueOf(newStarCount));
                            getDataManager().saveStar(currentUserId, mPost)
                                    .addOnSuccessListener(aVoid -> getMvpView().onError("Star added"));
                        }
                    }).addOnFailureListener(e -> getMvpView().onError("Some Error"));
        } else {
            getMvpView().onError("Please login to rate Post");
        }
    }

    //Checks if user bookmarked post and updates UI accordingly
    @Override
    public void saveOrDeleteBookmark() {
        String currentUserId = getDataManager().getCurrentUserId();
        if (currentUserId != null) {
            getDataManager().getBookmark(currentUserId, mPost.getPostId())
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            getMvpView().setNotBookmarkedIcon();
                            getDataManager().deleteBookmark(currentUserId, mPost)
                                    .addOnSuccessListener(aVoid ->
                                            getMvpView().onError("Bookmark removed"));
                        } else {
                            getMvpView().setBookmarkedIcon();
                            getDataManager().saveBookmark(currentUserId, mPost)
                                    .addOnSuccessListener(aVoid ->
                                            getMvpView().onError("Bookmark saved"));
                        }
                    }).addOnFailureListener(e -> getMvpView().onError("Some error"));
        } else {
            getMvpView().onError("Please login to bookmark Post");
        }
    }

    @Override
    public void createNewComment(String comment) {
        String currentUserId = getDataManager().getCurrentUserId();
        if (currentUserId != null) {
            getMvpView().clearCommentInput();
            PostComment postComment = newPostComment();
            postComment.setPostCommentText(comment);
            getDataManager().saveComment(mPostId, postComment)
                    .addOnSuccessListener(aVoid -> {
                        int newCommentCount = mPost.getPostComments() + 1;
                        mPost.setPostComments(newCommentCount);
                        updatePost(mPost);
                        getMvpView().setPostComments(String.valueOf(newCommentCount));
                        getMvpView().onError("Comment added");})
                    .addOnFailureListener(e -> getMvpView().onError("Some error"));
        } else {
            getMvpView().onError("Please login to comment Post");
        }
    }

    private void updatePost(Post post) {
        getDataManager().updatePost(post)
                .addOnFailureListener(e -> getMvpView().onError("Some Error"));
    }

    @NonNull
    private PostComment newPostComment() {
        return new PostComment(
                mPostId,
                getDataManager().getCurrentUserId(),
                getDataManager().getCurrentUserProfilePicUrl(),
                getDataManager().getCurrentUserName(),
                CommonUtils.getCurrentDate(),
                CommonUtils.getTimeStamp(),
                "",
                "0"
        );
    }
}
