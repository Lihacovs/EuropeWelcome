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
import com.bmd.android.europewelcome.data.firebase.model.PostImage;
import com.bmd.android.europewelcome.data.firebase.model.PostPlace;
import com.bmd.android.europewelcome.data.firebase.model.PostText;
import com.bmd.android.europewelcome.ui.base.BasePresenter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Konstantins on 12/7/2017.
 */

public class PostDetailPresenter<V extends PostDetailMvpView> extends BasePresenter<V> implements
        PostDetailMvpPresenter<V> {

    private static final String TAG = "PostDetailPresenter";

    private String mPostId;
    private Post mPost;
    private List<PostText> mPostTextList;
    private List<PostImage> mPostImageList;
    private List<PostPlace> mPostPlaceList;
    private int mChildLayoutNum;

    @Inject
    public PostDetailPresenter(DataManager dataManager) {
        super(dataManager);
        mPostTextList = new ArrayList<>();
        mPostImageList = new ArrayList<>();
        mPostPlaceList = new ArrayList<>();
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
                mChildLayoutNum= mPost.getChildLayoutNum();

                getMvpView().setPostUserImage(mPost.getPostAuthorImageUrl());
                getMvpView().setPostUserName(mPost.getPostAuthorName());
                getMvpView().setPostCreationDate(mPost.getPostCreationDate());
                getMvpView().setPostTitle(mPost.getPostTitle());

                getPostTextList(mPost.getPostId());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                getMvpView().showMessage("Unable to get Data");
            }
        });
    }

    @Override
    public void getPostTextList(String postId){
        getDataManager().getPostTextList(postId).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot document : task.getResult()) {
                    mPostTextList.add(document.toObject(PostText.class));
                }
                getPostImageList(mPost.getPostId());
            }
        });
    }

    @Override
    public void getPostImageList(String postId){
        getDataManager().getPostImageList(postId).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot document : task.getResult()) {
                    mPostImageList.add(document.toObject(PostImage.class));
                }
                getPostPlaceList(mPost.getPostId());
            }
        });
    }

    @Override
    public void getPostPlaceList(String postId) {
        getDataManager().getPostPlaceList(postId).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot document : task.getResult()) {
                    mPostPlaceList.add(document.toObject(PostPlace.class));
                }
                attachContentToLayout();
            }
        });
    }

    /**
     * Attaches Post's content {@link PostImage}, {@link PostText} to layout in particular order
     */
    //TODO: Error on activity close before loop finish
    @Override
    public void attachContentToLayout() {
        for(int i=0; i<=mChildLayoutNum; i++){
            for (PostText postText : mPostTextList){
                if(postText.getLayoutOrderNum() == i) {
                    getMvpView().attachPostTextLayout(postText);
                    break;
                }
            }

            for(PostImage postImage: mPostImageList){
                if(postImage.getLayoutOrderNum() == i) {
                    getMvpView().attachPostImageLayout(postImage);
                    break;
                }
            }

            for(PostPlace postPlace: mPostPlaceList){
                if(postPlace.getLayoutOrderNum() == i) {
                    getMvpView().attachPostPlaceLayout(postPlace);
                    break;
                }
            }

        }
    }
}
