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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bmd.android.europewelcome.R;
import com.bmd.android.europewelcome.data.firebase.model.PostComment;
import com.bmd.android.europewelcome.di.module.GlideApp;
import com.bmd.android.europewelcome.ui.base.BaseViewHolder;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestoreException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Post Comments Adapter
 */
public class PostCommentsAdapter extends FirestoreRecyclerAdapter<PostComment, PostCommentsAdapter.ViewHolder> {

    private Callback mCallback;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See
     * {@link FirestoreRecyclerOptions} for configuration options.
     *
     * @param options Query in builder
     */
    PostCommentsAdapter(FirestoreRecyclerOptions<PostComment> options) {
        super(options);
    }

    public void setAdapterCallback(Callback callback) {
        mCallback = callback;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder,
                                    int position,
                                    @NonNull PostComment model) {
        holder.bind(model);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate
                (R.layout.item_comment, parent, false));
    }

    @Override
    public void onDataChanged() {
        // Called each time there is a new query snapshot. You may want to use this method
        // to hide a loading spinner or check for the "no documents" state and update your UI.
        // ...
    }

    @Override
    public void onError(@NonNull FirebaseFirestoreException e) {
        super.onError(e);
    }


    public interface Callback {

        void onLikeCommentClick(PostComment postComment);
    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.iv_comment_item_user_image)
        ImageView mPostCommentUserImageIv;

        @BindView(R.id.tv_comment_item_user_name)
        TextView mPostCommentUserNameTv;

        @BindView(R.id.tv_comment_item_creation_date)
        TextView mPostCommentCreationDateTv;

        @BindView(R.id.tv_comment_item_comment_text)
        TextView mPostCommentTextTv;

        @BindView(R.id.tv_comment_item_star_count)
        TextView mPostStarsTv;

        @BindView(R.id.iv_comment_item_star_icon)
        ImageView mStarIv;

        private PostComment mPostComment;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {
            //mPostCommentUserImageIv.setImageDrawable(null);
            mPostCommentUserNameTv.setText("");
            mPostCommentCreationDateTv.setText("");
            mPostCommentTextTv.setText("");
        }

        public void bind(PostComment postComment) {

            mPostComment = postComment;

            if (postComment.getPostCommentUserImageUrl() != null) {
                GlideApp.with(itemView.getContext())
                        .load(postComment.getPostCommentUserImageUrl())
                        .centerCrop()
                        .apply(RequestOptions.circleCropTransform())
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(mPostCommentUserImageIv);
            }

            if (postComment.getPostCommentUserName() != null) {
                mPostCommentUserNameTv.setText(postComment.getPostCommentUserName());
            }

            if (postComment.getPostCommentCreationDate() != null) {
                mPostCommentCreationDateTv.setText(postComment.getPostCommentCreationDate());
            }

            if (postComment.getPostCommentText() != null) {
                mPostCommentTextTv.setText(postComment.getPostCommentText());
            }

            if (postComment.getPostCommentStars() != null) {
                mPostStarsTv.setText(postComment.getPostCommentStars());
            }

        }

        @OnClick(R.id.cl_comment_item_like_comment)
        void onLikeCommentClick(){
            if(mCallback != null)
                mCallback.onLikeCommentClick(mPostComment);
        }
    }
}
