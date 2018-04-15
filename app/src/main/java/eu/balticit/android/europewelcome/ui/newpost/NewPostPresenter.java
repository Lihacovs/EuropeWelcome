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

package eu.balticit.android.europewelcome.ui.newpost;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import eu.balticit.android.europewelcome.R;
import eu.balticit.android.europewelcome.data.DataManager;
import eu.balticit.android.europewelcome.data.firebase.model.Post;
import eu.balticit.android.europewelcome.data.firebase.model.PostSection;
import eu.balticit.android.europewelcome.ui.base.BasePresenter;
import eu.balticit.android.europewelcome.ui.custom.ImageCompress;
import eu.balticit.android.europewelcome.utils.CommonUtils;
import com.google.android.gms.location.places.Place;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.io.File;

import javax.inject.Inject;

import eu.balticit.android.europewelcome.data.DataManager;
import eu.balticit.android.europewelcome.data.firebase.model.Post;
import eu.balticit.android.europewelcome.ui.base.BasePresenter;
import eu.balticit.android.europewelcome.ui.custom.ImageCompress;
import eu.balticit.android.europewelcome.utils.CommonUtils;

/**
 * NewPost Presenter
 */
public class NewPostPresenter<V extends NewPostMvpView> extends BasePresenter<V> implements
        NewPostMvpPresenter<V> {

    private static final String TAG = "NewPostPresenter";

    private String mPostId;
    private Post mPost;

    @Inject
    NewPostPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void setPost(@Nullable String postId) {
        //TODO: CHECK INTERNET CONNECTION
        if (postId == null || postId.isEmpty()) {
            mPost = newPost();
            mPostId = mPost.getPostId();
            getDataManager().savePost(mPost)
                    .addOnSuccessListener(aVoid -> newTitlePostSection())
                    .addOnFailureListener(e -> {
                        getMvpView().hideLoading();
                        getMvpView().onError("Unable to get Data");
                    });
        } else {
            mPostId = postId;
            getDataManager().getPost(postId)
                    .addOnSuccessListener(documentSnapshot ->
                            mPost = documentSnapshot.toObject(Post.class))
                    .addOnFailureListener(e -> {
                        getMvpView().hideLoading();
                        getMvpView().onError("Unable to get Data");
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
                }).addOnFailureListener(e -> {
            getMvpView().hideLoading();
            Log.d(TAG, "onFailure: ");
        });
    }

    @Override
    public void checkVideoPostSection() {
        //Checks for 1 video section. (Only one Youtube player allowed due to performance issues,
        // to add multiple videos to post, another implementation required...like video reload...)
        getMvpView().showLoading();
        getDataManager().getFirstPostSection(mPostId, "Video")
                .addOnSuccessListener(documentSnapshots -> {
                    if (documentSnapshots.getDocuments().isEmpty()) {
                        getMvpView().hideLoading();
                        getMvpView().showYouTubeUrlDialog();
                    } else {
                        getMvpView().hideLoading();
                        getMvpView().onError("Only one YouTube video allowed");
                    }
                }).addOnFailureListener(e -> {
            getMvpView().hideLoading();
            Log.d(TAG, "onFailure: " + e);
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
                .addOnSuccessListener(aVoid -> {
                })
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
    public void deletePost() {
        getMvpView().hideKeyboard();
        getMvpView().showLoading();

        //First delete PostSection collection with all documents under that post
        getDataManager().getFirstPostSectionCollection(mPostId).addOnSuccessListener(documentSnapshots -> {
            for (DocumentSnapshot doc : documentSnapshots.getDocuments()) {
                PostSection postSection = doc.toObject(PostSection.class);
                getDataManager().deletePostSection(mPostId, postSection);
            }
            //Then delete Post document itself
            getDataManager().deletePost(mPost).addOnSuccessListener(aVoid -> {
                getMvpView().hideLoading();
                getMvpView().finishActivity();
            }).addOnFailureListener(e -> {
                getMvpView().hideLoading();
                Log.d(TAG, "deletePost: " + e.getMessage());
            });
        }).addOnFailureListener(e -> {
            getMvpView().hideLoading();
            Log.d(TAG, "onFailure: " + e.getMessage());
        });
    }

    @Override
    public void savePostToDraft() {
        getMvpView().hideKeyboard();
        getMvpView().showLoading();
        mPost.setPostAsDraft(true);

        getDataManager().getFirstPostSection(mPostId, "Title")
                .addOnSuccessListener(documentSnapshots -> {
                    if (documentSnapshots.getDocuments().isEmpty()) {
                        getMvpView().hideLoading();
                    } else {
                        for (DocumentSnapshot doc : documentSnapshots.getDocuments()) {
                            PostSection postSection = doc.toObject(PostSection.class);
                            mPost.setPostTitle(postSection.getPostTitle());
                        }
                        getMvpView().showMessage("Saved to Drafts");
                        updatePost();
                    }
                }).addOnFailureListener(e -> {
            getMvpView().hideLoading();
            Log.d(TAG, "onFailure: " + e);
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
                        mPost.setPostPublished(true);
                        mPost.setPostAsDraft(false);
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

    @NonNull
    private Post newPost() {
        return new Post(getDataManager().getCurrentUserId(),
                getDataManager().getCurrentUserName(),
                getDataManager().getCurrentUserProfilePicUrl(),
                "",
                "",
                1,
                1,
                0,
                CommonUtils.getTimeStampInt(),
                null,
                CommonUtils.getCurrentDate(),
                false,
                false,
                false,
                false
        );
    }

    @NonNull
    private PostSection newPostSection() {
        return new PostSection(
                mPostId,
                null,
                CommonUtils.getCurrentDate(),
                CommonUtils.getTimeStamp(),
                CommonUtils.getTimeStampInt(),
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
