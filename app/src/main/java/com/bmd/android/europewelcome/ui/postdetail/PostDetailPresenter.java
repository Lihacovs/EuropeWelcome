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

package com.bmd.android.europewelcome.ui.postdetail;

import android.support.annotation.NonNull;

import com.bmd.android.europewelcome.data.DataManager;
import com.bmd.android.europewelcome.data.firebase.model.Post;
import com.bmd.android.europewelcome.data.firebase.model.PostComment;
import com.bmd.android.europewelcome.ui.base.BasePresenter;
import com.bmd.android.europewelcome.utils.CommonUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
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
    public PostDetailPresenter(DataManager dataManager) {
        super(dataManager);
    }


    @Override
    public void setPostId(String postId) {
        mPostId = postId;
    }

    @Override
    public void getPost(String postId) {
        getDataManager().getPost(postId).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                mPost = documentSnapshot.toObject(Post.class);

                getMvpView().setPostUserImage(mPost.getPostAuthorImageUrl());
                getMvpView().setPostUserName(mPost.getPostAuthorName());
                getMvpView().setPostCreationDate(mPost.getPostCreationDate());
                getMvpView().setPostTitle(mPost.getPostTitle());

                getMvpView().setPostNewCommentUserImage(getDataManager()
                        .getCurrentUserProfilePicUrl());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                getMvpView().showMessage("Unable to get Data");
            }
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
    public void saveComment(String postId, PostComment postComment) {
        getDataManager().saveComment(postId, postComment)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
    }

    @Override
    public PostComment newPostComment() {
        return new PostComment(
                mPostId,
                getDataManager().getCurrentUserId(),
                getDataManager().getCurrentUserProfilePicUrl(),
                getDataManager().getCurrentUserName(),
                CommonUtils.getCurrentDate(),
                CommonUtils.getTimeStamp(),
                null,
                "0"
        );
    }
}
