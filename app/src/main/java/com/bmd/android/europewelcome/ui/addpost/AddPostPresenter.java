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

package com.bmd.android.europewelcome.ui.addpost;

import android.support.annotation.NonNull;

import com.bmd.android.europewelcome.data.DataManager;
import com.bmd.android.europewelcome.data.firebase.model.Post;
import com.bmd.android.europewelcome.data.firebase.model.PostImage;
import com.bmd.android.europewelcome.data.firebase.model.PostText;
import com.bmd.android.europewelcome.ui.base.BasePresenter;
import com.bmd.android.europewelcome.ui.base.MvpView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Konstantins on 12/7/2017.
 */

public class AddPostPresenter<V extends MvpView> extends BasePresenter<V> implements
        AddPostMvpPresenter<V> {

    private static final String TAG = "AddPostPresenter";

    private List<PostImage> mPostImageList;
    private List<PostText> mPostTextList;
    private Post mPost;

    @Inject
    public AddPostPresenter(DataManager dataManager) {
        super(dataManager);

        mPostTextList = new ArrayList<>();
        mPostImageList = new ArrayList<>();
        mPost = newPost();
    }


    @Override
    public void addPostTextToList(PostText postText) {
        mPostTextList.add(postText);
    }

    @Override
    public void removePostTextFromList(PostText postText) {
        mPostTextList.remove(postText);
    }

    @Override
    public void updatePostTextInList(PostText postText) {
        mPostTextList.set(mPostTextList.indexOf(postText),postText);
    }

    @Override
    public void addPostImageToList(PostImage postImage) {
        mPostImageList.add(postImage);
    }

    @Override
    public void removePostImageFromList(PostImage postImage) {
        mPostImageList.remove(postImage);
    }

    @Override
    public void updatePostImageInList(PostImage postImage) {
        mPostImageList.set(mPostImageList.indexOf(postImage), postImage);
    }

    @Override
    public void savePost() {
        getDataManager().savePost(mPost).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });

        for (PostImage postImage : mPostImageList) {
            savePostImage(postImage, mPost.getPostId());
        }

        for (PostText postText : mPostTextList) {
            savePostText(postText, mPost.getPostId());
        }
    }

    @Override
    public void savePostText(PostText postText, String postId) {
        getDataManager().savePostText(postId, postText).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }

    @Override
    public void savePostImage(PostImage postImage, String postId) {
        getDataManager().savePostImage(postId, postImage).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }

    private Post newPost(){
        return new Post(null
                ,"Jonathan Doherty"
                ,"Post Title Is Awesome"
                ,null
                ,"1"
                ,"1"
                ,null
                ,"18 Oct 2017");
    }
}

