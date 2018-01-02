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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bmd.android.europewelcome.R;
import com.bmd.android.europewelcome.data.firebase.model.Post;
import com.bmd.android.europewelcome.data.firebase.model.PostComment;
import com.bmd.android.europewelcome.di.module.GlideApp;
import com.bmd.android.europewelcome.ui.base.BaseViewHolder;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestoreException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Konstantins on 1/2/2018.
 */

public class PostCommentsAdapter extends FirestoreRecyclerAdapter<PostComment, PostCommentsAdapter.ViewHolder> {

    private Callback mCallback;
    private FirestoreRecyclerOptions<Post> mOptions;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See
     * {@link FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public PostCommentsAdapter(FirestoreRecyclerOptions<PostComment> options) {
        super(options);
    }

    public void setAdapterCallback(Callback callback) {
        mCallback = callback;
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, int position, PostComment model) {
        holder.bind(model);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate
                (R.layout.item_postdetail_comment, parent, false));
    }

    @Override
    public void onDataChanged() {
        // Called each time there is a new query snapshot. You may want to use this method
        // to hide a loading spinner or check for the "no documents" state and update your UI.
        // ...
    }

    @Override
    public void onError(FirebaseFirestoreException e) {
        super.onError(e);
    }


    public interface Callback {

        void onStarIconClick(PostComment postComment);
    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.iv_postdetailitem_commentuserimage)
        ImageView mPostCommentUserImageIv;

        @BindView(R.id.tv_postdetailitem_commentusername)
        TextView mPostCommentUserNameTv;

        @BindView(R.id.tv_postdetailitem_commentcreationdate)
        TextView mPostCommentCreationDateTv;

        @BindView(R.id.tv_postdetailitem_comment)
        TextView mPostCommentTextTv;

        @BindView(R.id.tv_postitem_starcount)
        TextView mPostStarsTv;

        @BindView(R.id.iv_postitem_starimage)
        ImageView mStarIv;

        private PostComment mPostComment;

        public ViewHolder(View itemView) {
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


            mStarIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int newStarCount = Integer.parseInt(postComment.getPostCommentStars()) + 1;
                    mPostComment.setPostCommentStars(Integer.toString(newStarCount));
                    if(mCallback != null)
                        mCallback.onStarIconClick(mPostComment);
                }
            });

        }
    }
}
