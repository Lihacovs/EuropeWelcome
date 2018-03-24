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

package com.bmd.android.europewelcome.ui.addpost;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.bmd.android.europewelcome.data.DataManager;
import com.bmd.android.europewelcome.data.firebase.model.Post;
import com.bmd.android.europewelcome.data.firebase.model.PostSection;
import com.bmd.android.europewelcome.ui.base.BasePresenter;
import com.bmd.android.europewelcome.utils.CommonUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Add Post Presenter
 */

public class AddPostPresenter<V extends AddPostMvpView> extends BasePresenter<V> implements
        AddPostMvpPresenter<V> {

    private static final String TAG = "AddPostPresenter";

    private List<PostSection> mPostSectionList;
    private Post mPost;
    private int mLayoutOrderNum = 1;

    @Inject
    public AddPostPresenter(DataManager dataManager) {
        super(dataManager);

        mPostSectionList = new ArrayList<>();
        mPost = newPost();
    }

    @Override
    public void addPostSectionToList(PostSection postSection) {
        mPostSectionList.add(postSection);
    }

    @Override
    public void removePostSectionFromList(PostSection postSection) {
        mPostSectionList.remove(postSection);
    }

    @Override
    public void updatePostSectionInList(PostSection postSection) {
        mPostSectionList.set(mPostSectionList.indexOf(postSection), postSection);
    }

    @Override
    public void setPostTitle(String postTitle) {
        mPost.setPostTitle(postTitle);
    }


    @Override
    public void setPostImageUrl(String downloadUrl) {
        if (mPost.getPostImageUrl() == null) {
            mPost.setPostImageUrl(downloadUrl);
        }
    }

    @Override
    public void setPostText(String postText) {
        if (mPost.getPostText() == null) {
            mPost.setPostText(postText);
        }
    }

    @Override
    public void savePost() {

        mPost.setChildLayoutNum(mLayoutOrderNum);

        getDataManager().savePost(mPost).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                for (PostSection postSection : mPostSectionList) {
                    savePostSection(postSection, mPost.getPostId());
                }

            }
        });
    }

    @Override
    public void savePostSection(PostSection postSection, String postId) {
        getDataManager().savePostSection(postSection, postId)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
    }

    @Override
    public void uploadFileToStorage(Uri uri) {
        getMvpView().showMessage("Uploading...");
        getDataManager().uploadFileToStorage(uri, "postImages/")
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d(TAG, "uploadPhoto:onSuccess:" +
                                taskSnapshot.getMetadata().getReference().getPath());

                        String downloadUrl = taskSnapshot.getDownloadUrl().toString();
                        PostSection postSection = newPostSection();
                        postSection.setPostSectionViewType("Image");
                        postSection.setPostImageUrl(downloadUrl);
                        getMvpView().attachPostImageLayout(postSection);

                        setPostImageUrl(downloadUrl);

                        getMvpView().showMessage("Image uploaded");
                    }
                }).removeOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "uploadPhoto:onError", e);
                getMvpView().showMessage("Upload failed");
            }
        });
    }

    @Override
    public Post newPost() {
        return new Post(getDataManager().getCurrentUserId()
                , getDataManager().getCurrentUserName()
                , getDataManager().getCurrentUserProfilePicUrl()
                , null
                , null
                , 0
                , 1
                , CommonUtils.getIntTimeStamp()
                , null
                , CommonUtils.getCurrentDate()
                , 0
                , false
                , false
        );
    }

    @Override
    public PostSection newPostSection() {
        return new PostSection(null,
                CommonUtils.getCurrentDate(),
                CommonUtils.getTimeStamp(),
                CommonUtils.getTimeStampInt(),
                mLayoutOrderNum++,
                null,
                14,
                false,
                false,
                null,
                null,
                null,
                null,
                0,
                0,
                null);
    }
}

