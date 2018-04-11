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

package com.bmd.android.europewelcome.ui.profile.userposts;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bmd.android.europewelcome.R;
import com.bmd.android.europewelcome.data.firebase.model.Post;
import com.bmd.android.europewelcome.di.module.GlideApp;
import com.bmd.android.europewelcome.ui.base.BaseViewHolder;
import com.bmd.android.europewelcome.ui.postdetail.PostDetailActivity;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestoreException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * UserPosts Adapter.
 */
public class UserPostsAdapter extends FirestoreRecyclerAdapter<Post, UserPostsAdapter.ViewHolder> {

    private static final String TAG = "UserPostsAdapter";

    private Callback mCallback;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See
     * {@link FirestoreRecyclerOptions} for configuration options.
     *
     * @param options - data to query from DB
     */
    UserPostsAdapter(FirestoreRecyclerOptions<Post> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Post model) {
        holder.bind(model);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate
                (R.layout.item_user_post_view, parent, false));
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

    public void setAdapterCallback(Callback callback) {
        mCallback = callback;
    }

    public interface Callback {
        void onPostItemViewClick(Post post);
    }

    public class ViewHolder extends BaseViewHolder{
        private Post mPost;

        @BindView(R.id.iv_user_post_item_author_photo)
        ImageView mUserPhotoIv;

        @BindView(R.id.tv_user_post_item_author_name)
        TextView mPostAuthorTv;

        @BindView(R.id.tv_user_post_item_post_date)
        TextView mPostCreationDateTv;

        @BindView(R.id.iv_user_post_item_post_image)
        ImageView mPostImageIv;

        @BindView(R.id.tv_user_post_item_post_title)
        TextView mPostTitleTv;

        @BindView(R.id.tv_user_post_item_post_text)
        TextView mPostTextTv;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {
        }

        public void bind(Post post) {

            mPost = post;

            if (post.getPostAuthorImageUrl() != null) {
                GlideApp.with(itemView.getContext())
                        .load(post.getPostAuthorImageUrl())
                        .apply(RequestOptions.circleCropTransform())
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(mUserPhotoIv);
            }

            if (post.getPostImageUrl() != null) {
                GlideApp.with(itemView.getContext())
                        .load(post.getPostImageUrl())
                        .centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(mPostImageIv);
            }

            if (post.getPostTitle() != null) {
                mPostTitleTv.setText(post.getPostTitle());
            }

            if (post.getPostAuthorName() != null) {
                mPostAuthorTv.setText(post.getPostAuthorName());
            }

            if (post.getPostCreationDate() != null) {
                mPostCreationDateTv.setText(post.getPostCreationDate());
            }

            if (post.getPostText() != null) {
                mPostTextTv.setText(post.getPostText());
            }

            itemView.setOnClickListener(v -> {
                if (mCallback != null)
                    mCallback.onPostItemViewClick(mPost);
            });
        }
    }
}
