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

package eu.balticit.android.europewelcome.ui.postdetail;

import android.support.annotation.NonNull;

import eu.balticit.android.europewelcome.R;
import eu.balticit.android.europewelcome.data.DataManager;
import eu.balticit.android.europewelcome.data.firebase.model.Post;
import eu.balticit.android.europewelcome.data.firebase.model.PostComment;
import eu.balticit.android.europewelcome.ui.base.BasePresenter;
import eu.balticit.android.europewelcome.utils.CommonUtils;

import com.google.firebase.firestore.Query;

import javax.inject.Inject;


/**
 * Post Detail Presenter
 */
public class PostDetailPresenter<V extends PostDetailMvpView> extends BasePresenter<V> implements
        PostDetailMvpPresenter<V> {

    @SuppressWarnings("unused")
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

        }).addOnFailureListener(e -> {
            getMvpView().hideLoading();
            getMvpView().showMessage(R.string.post_detail_some_error);
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
                    }).addOnFailureListener(e -> {
                getMvpView().hideLoading();
                getMvpView().onError(R.string.post_detail_some_error);
            });
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
                        getMvpView().hideLoading();
                    }).addOnFailureListener(e -> {
                getMvpView().hideLoading();
                getMvpView().onError(R.string.post_detail_some_error);
            });
        } else {
            getMvpView().hideLoading();
        }
    }

    //Checks if user rated post with star and updates UI accordingly
    @Override
    public void addOrRemoveStar() {
        getMvpView().showLoading();
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
                                    .addOnSuccessListener(aVoid -> {
                                        getMvpView().hideLoading();
                                        getMvpView().onError(R.string.post_detail_star_removed);
                                    })
                                    .addOnFailureListener(e -> {
                                        getMvpView().hideLoading();
                                        getMvpView().onError(R.string.post_detail_some_error);
                                    });
                        } else {
                            int newStarCount = mPost.getPostStars() + 1;
                            mPost.setPostStars(newStarCount);
                            updatePost(mPost);
                            getMvpView().setStarRatedIcon();
                            getMvpView().setPostStars(String.valueOf(newStarCount));
                            getDataManager().saveStar(currentUserId, mPost)
                                    .addOnSuccessListener(aVoid -> {
                                        getMvpView().hideLoading();
                                        getMvpView().onError(R.string.post_detail_star_rated);
                                    })
                                    .addOnFailureListener(e -> {
                                        getMvpView().hideLoading();
                                        getMvpView().onError(R.string.post_detail_some_error);
                                    });
                        }
                    })
                    .addOnFailureListener(e -> {
                        getMvpView().hideLoading();
                        getMvpView().onError(R.string.post_detail_some_error);
                    });
        } else {
            getMvpView().hideLoading();
            getMvpView().onError(R.string.post_detail_login_to_rate);
        }
    }

    //Checks if user bookmarked post and updates UI accordingly
    @Override
    public void saveOrDeleteBookmark() {
        getMvpView().showLoading();
        String currentUserId = getDataManager().getCurrentUserId();
        if (currentUserId != null) {
            getDataManager().getBookmark(currentUserId, mPost.getPostId())
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            getMvpView().setNotBookmarkedIcon();
                            getDataManager().deleteBookmark(currentUserId, mPost)
                                    .addOnSuccessListener(aVoid -> {
                                        getMvpView().hideLoading();
                                        getMvpView().onError(R.string.post_detail_bookmark_removed);
                                    })
                                    .addOnFailureListener(e -> {
                                        getMvpView().hideLoading();
                                        getMvpView().onError(R.string.post_detail_some_error);
                                    });
                        } else {
                            getMvpView().setBookmarkedIcon();
                            getDataManager().saveBookmark(currentUserId, mPost)
                                    .addOnSuccessListener(aVoid -> {
                                        getMvpView().hideLoading();
                                        getMvpView().onError(R.string.post_detail_bookmark_saved);
                                    })
                                    .addOnFailureListener(e -> {
                                        getMvpView().hideLoading();
                                        getMvpView().onError(R.string.post_detail_some_error);
                                    });
                        }
                    })
                    .addOnFailureListener(e -> {
                        getMvpView().hideLoading();
                        getMvpView().onError(R.string.post_detail_some_error);
                    });
        } else {
            getMvpView().hideLoading();
            getMvpView().onError(R.string.post_detail_login_to_bookmark);
        }
    }

    @Override
    public void createNewComment(String comment) {
        getMvpView().showLoading();
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

                        getDataManager().saveUserComment(currentUserId, postComment)
                                .addOnSuccessListener(aVoid1 -> {
                                    getMvpView().hideLoading();
                                    getMvpView().onError(R.string.post_detail_comment_saved);
                                })
                                .addOnFailureListener(e -> {
                                    getMvpView().hideLoading();
                                    getMvpView().onError(R.string.post_detail_some_error);
                                });
                    })
                    .addOnFailureListener(e -> {
                        getMvpView().hideLoading();
                        getMvpView().onError(R.string.post_detail_some_error);
                    });
        } else {
            getMvpView().hideLoading();
            getMvpView().onError(R.string.post_detail_login_to_comment);
        }
    }

    @Override
    public void addOrRemoveCommentLike(PostComment postComment, PostCommentsAdapter.ViewHolder holder) {
        getMvpView().showLoading();
        String currentUserId = getDataManager().getCurrentUserId();
        if (currentUserId != null) {
            getDataManager().getCommentLike(currentUserId, postComment.getPostCommentId())
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            int newLikeCount = postComment.getPostCommentStars() - 1;
                            postComment.setPostCommentStars(newLikeCount);
                            updatePostComment(postComment);
                            //getMvpView().removeStarRatedIcon(holder);
                            getDataManager().deleteCommentLike(currentUserId, postComment)
                                    .addOnSuccessListener(aVoid -> getMvpView().hideLoading())
                                    .addOnFailureListener(e -> {
                                        getMvpView().hideLoading();
                                        getMvpView().onError(R.string.post_detail_some_error);
                                    });
                        } else {
                            int newLikeCount = postComment.getPostCommentStars() + 1;
                            postComment.setPostCommentStars(newLikeCount);
                            updatePostComment(postComment);
                            //getMvpView().setStarRatedIcon(holder);
                            getDataManager().saveCommentLike(currentUserId, postComment)
                                    .addOnSuccessListener(aVoid -> getMvpView().hideLoading())
                                    .addOnFailureListener(e -> {
                                        getMvpView().hideLoading();
                                        getMvpView().onError(R.string.post_detail_some_error);
                                    });
                        }
                    }).addOnFailureListener(e -> {
                getMvpView().hideLoading();
                getMvpView().onError(R.string.post_detail_some_error);
            });
        } else {
            getMvpView().hideLoading();
            getMvpView().onError(R.string.post_detail_login_to_like_comment);
        }
    }

    @Override
    public void checkCommentLikedByUser(PostComment postComment, PostCommentsAdapter.ViewHolder holder) {
        String currentUserId = getDataManager().getCurrentUserId();
        if (currentUserId != null) {
            getDataManager().getCommentLike(currentUserId, postComment.getPostCommentId())
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            getMvpView().setCommentLikeIcon(holder);
                        } else {
                            getMvpView().setNotCommentLikeIcon(holder);
                        }
                    }).addOnFailureListener(e -> getMvpView().onError(R.string.post_detail_some_error));
        }
    }

    private void updatePost(Post post) {
        getDataManager().updatePost(post)
                .addOnFailureListener(e -> getMvpView().onError(R.string.post_detail_some_error));
    }

    private void updatePostComment(PostComment postComment) {
        getDataManager().updatePostComment(mPostId, postComment)
                .addOnFailureListener(e -> getMvpView().onError(R.string.post_detail_some_error));
    }

    @NonNull
    private PostComment newPostComment() {
        return new PostComment(
                mPostId,
                getDataManager().getCurrentUserId(),
                getDataManager().getCurrentUserProfilePicUrl(),
                getDataManager().getCurrentUserName(),
                CommonUtils.getCurrentDate(),
                CommonUtils.getTimeStampInt(),
                "",
                0
        );
    }
}
