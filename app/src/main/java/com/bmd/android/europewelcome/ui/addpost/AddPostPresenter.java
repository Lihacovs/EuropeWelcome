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

    /*private List<PostImage> mPostImageList;
    private List<PostText> mPostTextList;
    private List<PostPlace> mPostPlaceList;*/
    private List<PostSection> mPostSectionList;
    private Post mPost;
    private int mLayoutOrderNum = 1;

    @Inject
    public AddPostPresenter(DataManager dataManager) {
        super(dataManager);

        /*mPostTextList = new ArrayList<>();
        mPostImageList = new ArrayList<>();
        mPostPlaceList = new ArrayList<>();*/
        mPostSectionList = new ArrayList<>();
        mPost = newPost();
    }


    /*@Override
    public void addPostTextToList(PostText postText) {
        mPostTextList.add(postText);
    }

    @Override
    public void removePostTextFromList(PostText postText) {
        mPostTextList.remove(postText);
    }

    @Override
    public void updatePostTextInList(PostText postText) {
        mPostTextList.set(mPostTextList.indexOf(postText), postText);
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
    public void addPostPlaceToList(PostPlace postPlace) {
        mPostPlaceList.add(postPlace);
    }

    @Override
    public void removePostPlaceFromList(PostPlace postPlace) {
        mPostPlaceList.remove(postPlace);
    }*/

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

        /*if (mPost.getPostText() == null) {
            if (!mPostSectionList.isEmpty() && mPostSectionList.size() > 0) {
                mPost.setPostText(mPostSectionList.get(0).getPostText());
            }
        }

        if (mPost.getPostImageUrl() == null) {
            if (!mPostSectionList.isEmpty() && mPostSectionList.size() > 0) {
                mPost.setPostImageUrl(mPostSectionList.get(0).getPostImageUrl());
            }
        }*/

        mPost.setChildLayoutNum(mLayoutOrderNum);

        getDataManager().savePost(mPost).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                for (PostSection postSection : mPostSectionList) {
                    savePostSection(postSection, mPost.getPostId());
                }

                /*for (PostText postText : mPostTextList) {
                    savePostText(postText, mPost.getPostId());
                }

                for (PostImage postImage : mPostImageList) {
                    savePostImage(postImage, mPost.getPostId());
                }

                for (PostPlace postPlace : mPostPlaceList) {
                    savePostPlace(postPlace, mPost.getPostId());
                }*/

            }
        });
    }

    /*@Override
    public void savePostText(final PostText postText, final String postId) {
        getDataManager().savePostText(postId, postText).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });
    }

    @Override
    public void savePostImage(final PostImage postImage, String postId) {
        getDataManager().savePostImage(postId, postImage).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    @Override
    public void savePostPlace(PostPlace postPlace, String postId) {
        getDataManager().savePostPlace(postId, postPlace).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }*/

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
        getDataManager().uploadFileToStorage(uri)
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
                , "1"
                , "1"
                , null
                , CommonUtils.getCurrentDate()
                , 0
        );
    }

    /*@Override
    public PostText newPostText() {
        return new PostText(null
                , 14
                , false
                , false
                , CommonUtils.getTimeStamp()
                , mLayoutOrderNum++
        );
    }

    @Override
    public PostImage newPostImage() {
        return new PostImage(null
                , null
                , CommonUtils.getTimeStamp()
                , mLayoutOrderNum++
        );
    }

    @Override
    public PostPlace newPostPlace() {
        return new PostPlace(null
                , null
                , 0
                , 0
                , CommonUtils.getTimeStamp()
                , mLayoutOrderNum++
        );
    }*/

    @Override
    public PostSection newPostSection() {
        return new PostSection(null,
                CommonUtils.getCurrentDate(),
                CommonUtils.getTimeStamp(),
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
                0);
    }
}

