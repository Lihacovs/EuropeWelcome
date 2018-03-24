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

package com.bmd.android.europewelcome.ui.newpost;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.bmd.android.europewelcome.R;
import com.bmd.android.europewelcome.data.DataManager;
import com.bmd.android.europewelcome.data.firebase.model.Post;
import com.bmd.android.europewelcome.data.firebase.model.PostSection;
import com.bmd.android.europewelcome.ui.base.BasePresenter;
import com.bmd.android.europewelcome.ui.custom.ImageCompress;
import com.bmd.android.europewelcome.utils.CommonUtils;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.io.File;

import javax.inject.Inject;

/**
 * New Post Presenter
 */

public class NewPostPresenter<V extends NewPostMvpView> extends BasePresenter<V> implements
        NewPostMvpPresenter<V> {

    private static final String TAG = "NewPostPresenter";

    private String mPostId;
    private Post mPost;
    private int mLayoutOrderNum;

    @Inject
    NewPostPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void setPost(@Nullable String postId) {
        getMvpView().showLoading();
        //TODO: CHECK INTERNET CONNECTION
        if (postId == null || postId.isEmpty()) {
            mPost = newPost();
            mPostId = mPost.getPostId();
            getDataManager().savePost(mPost).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    newTitlePostSection();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    getMvpView().hideLoading();
                    getMvpView().onError("Unable to get Data");
                }
            });
        } else {
            mPostId = postId;
            getDataManager().getPost(postId)
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            mPost = documentSnapshot.toObject(Post.class);
                            getMvpView().hideLoading();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    getMvpView().hideLoading();
                    getMvpView().onError("Unable to get Data");
                }
            });
        }
    }

    @Override
    public Query getPostSectionQuery() {
        return getDataManager().getPostSectionQuery(mPostId);
    }

    private void newTitlePostSection() {
        PostSection postSection = newPostSection();
        postSection.setPostSectionViewType("Title");
        getDataManager().savePostSection(postSection, mPost.getPostId())
                .addOnSuccessListener(aVoid -> {
                    getMvpView().hideLoading();
                    Log.d(TAG, "newTitlePostSection: ");
                })
                .addOnFailureListener(e -> {
                    getMvpView().hideLoading();
                    Log.d(TAG, "newTextPostSection: Failure");
                });
    }

    @Override
    public void newTextPostSection() {
        getMvpView().showLoading();
        PostSection postSection = newPostSection();
        postSection.setPostSectionViewType("Text");
        getDataManager().savePostSection(postSection, mPost.getPostId())
                .addOnSuccessListener(aVoid -> {
                    getMvpView().hideLoading();
                    getMvpView().scrollViewToBottom();
                })
                .addOnFailureListener(e -> {
                    getMvpView().hideLoading();
                    Log.d(TAG, "newTextPostSection: Failure");
                });
    }

    private void newImagePostSection(String postImageUrl) {
        PostSection postSection = newPostSection();
        postSection.setPostSectionViewType("Image");
        postSection.setPostImageUrl(postImageUrl);
        getDataManager().savePostSection(postSection, mPost.getPostId())
                .addOnSuccessListener(aVoid -> {
                    getMvpView().hideLoading();
                    getMvpView().scrollViewToBottom();
                })
                .addOnFailureListener(e -> {
                    getMvpView().hideLoading();
                    Log.d(TAG, "onFailure: ");
                });
    }

    @Override
    public void newMapPostSection(Place place) {
        getMvpView().showLoading();
        PostSection postSection = newPostSection();
        postSection.setPostSectionViewType("Map");
        postSection.setPostPlaceAddress(place.getAddress().toString());
        postSection.setPostPlaceName(place.getName().toString());
        postSection.setPostPlaceLat(place.getLatLng().latitude);
        postSection.setPostPlaceLng(place.getLatLng().longitude);
        getDataManager().savePostSection(postSection, mPost.getPostId())
                .addOnSuccessListener(aVoid -> {
                    getMvpView().hideLoading();
                    getMvpView().scrollViewToBottom();
                }).addOnFailureListener(e -> {
            getMvpView().hideLoading();
            Log.d(TAG, "onFailure: ");
        });
    }

    @Override
    public void newVideoPostSection(String videoCode) {
        getMvpView().showLoading();
        PostSection postSection = newPostSection();
        postSection.setPostSectionViewType("Video");
        postSection.setYouTubeVideoCode(videoCode);
        getDataManager().savePostSection(postSection, mPost.getPostId())
                .addOnSuccessListener(aVoid -> {
                    getMvpView().hideLoading();
                    getMvpView().scrollViewToBottom();
                })
                .addOnFailureListener(e -> {
                    getMvpView().hideLoading();
                    Log.d(TAG, "onFailure: ");
                });
    }

    @Override
    public void deletePostSection(PostSection postSection) {
        getMvpView().showLoading();
        getDataManager().deletePostSection(mPostId, postSection)
                .addOnSuccessListener(aVoid -> getMvpView().hideLoading())
                .addOnFailureListener(e -> {
                    getMvpView().hideLoading();
                    Log.d(TAG, "deletePostSection: ");
                });
    }

    @Override
    public void updatePostSection(PostSection postSection) {
        getDataManager().updatePostSection(mPostId, postSection)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "updatePostSection: "))
                .addOnFailureListener(e -> Log.d(TAG, "onFailure: "));
    }

    @Override
    public void uploadImageToStorage(Uri uri, Context context) {
        getMvpView().showLoading();
        String compressedFile = new ImageCompress(context).compressImage(
                uri.toString(), 1280.0f, 720.0f);

        getDataManager().uploadFileToStorage(Uri.fromFile(new File(compressedFile)), "postImages/")
                .addOnSuccessListener(taskSnapshot -> {
                    if (taskSnapshot.getDownloadUrl() != null) {
                        String downloadUrl = taskSnapshot.getDownloadUrl().toString();
                        newImagePostSection(downloadUrl);
                    }
                }).removeOnFailureListener(e -> {
            getMvpView().hideLoading();
            getMvpView().onError(R.string.register_upload_error);
        });
    }

    @Override
    public void savePostToDraft() {
        getMvpView().hideKeyboard();
        getMvpView().showLoading();
        mPost.setPostAsDraft(true);
        updatePost();
    }

    @Override
    public void deletePost() {
        getMvpView().hideKeyboard();
        getMvpView().showLoading();
        getDataManager().deletePost(mPost).addOnSuccessListener(aVoid -> {
            getMvpView().hideLoading();
            getMvpView().finishActivity();
        }).addOnFailureListener(e -> {
            getMvpView().hideLoading();
            Log.d(TAG, "deletePost: " + e.getMessage());
        });
    }

    /**
     * Triggers {@link #setPostTitle()}, {@link #setPostText()}, {@link #setPostImage()} chain
     * to update Post fields
     */
    @Override
    public void publishPost() {
        getMvpView().hideKeyboard();
        getMvpView().showLoading();
        mPost.setPostPublished(true);
        setPostTitle();
    }

    private void setPostTitle() {
        getDataManager().getFirstPostSection(mPostId, "Title")
                .addOnSuccessListener(documentSnapshots -> {
                    if (documentSnapshots.getDocuments().isEmpty()) {
                        getMvpView().hideLoading();
                        getMvpView().onError(R.string.newpost_add_title);
                    } else {
                        for (DocumentSnapshot doc : documentSnapshots.getDocuments()) {
                            PostSection postSection = doc.toObject(PostSection.class);
                            Log.d(TAG, "onSuccess: " + postSection.getPostText());
                            mPost.setPostTitle(postSection.getPostTitle());
                        }
                        setPostText();
                    }
                }).addOnFailureListener(e -> {
            getMvpView().hideLoading();
            Log.d(TAG, "onFailure: " + e);
        });
    }

    private void setPostText() {
        getDataManager().getFirstPostSection(mPostId, "Text")
                .addOnSuccessListener(documentSnapshots -> {
                    if (documentSnapshots.getDocuments().isEmpty()) {
                        getMvpView().hideLoading();
                        getMvpView().onError(R.string.newpost_add_text);
                    } else {
                        for (DocumentSnapshot doc : documentSnapshots.getDocuments()) {
                            PostSection postSection = doc.toObject(PostSection.class);
                            Log.d(TAG, "onSuccess: " + postSection.getPostText());
                            mPost.setPostText(postSection.getPostText());
                        }
                        setPostImage();
                    }
                }).addOnFailureListener(e -> {
            getMvpView().hideLoading();
            Log.d(TAG, "onFailure: " + e);
        });
    }

    private void setPostImage() {
        getDataManager().getFirstPostSection(mPostId, "Image")
                .addOnSuccessListener(documentSnapshots -> {
                    if (documentSnapshots.getDocuments().isEmpty()) {
                        getMvpView().hideLoading();
                        getMvpView().onError(R.string.newpost_add_image);
                    } else {
                        for (DocumentSnapshot doc : documentSnapshots.getDocuments()) {
                            PostSection postSection = doc.toObject(PostSection.class);
                            Log.d(TAG, "onSuccess: " + postSection.getPostImageUrl());
                            mPost.setPostImageUrl(postSection.getPostImageUrl());
                        }
                        updatePost();
                    }
                }).addOnFailureListener(e -> {
            getMvpView().hideLoading();
            Log.d(TAG, "onFailure: " + e.getMessage());
        });
    }

    private void updatePost() {
        getDataManager().updatePost(mPost).addOnSuccessListener(aVoid -> {
            getMvpView().hideLoading();
            getMvpView().finishActivity();
        }).addOnFailureListener(e -> {
            getMvpView().hideLoading();
            Log.d(TAG, "updatePost: failure" + e.getMessage());
        });
    }

    private Post newPost() {
        return new Post(getDataManager().getCurrentUserId(),
                getDataManager().getCurrentUserName(),
                getDataManager().getCurrentUserProfilePicUrl(),
                "",
                "",
                0,
                1,
                CommonUtils.getIntTimeStamp(),
                null,
                CommonUtils.getCurrentDate(),
                0,
                false,
                false

        );
    }

    private PostSection newPostSection() {
        return new PostSection(null,
                CommonUtils.getCurrentDate(),
                CommonUtils.getTimeStamp(),
                CommonUtils.getTimeStampInt(),
                mLayoutOrderNum++,
                "",
                "",
                18,
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
