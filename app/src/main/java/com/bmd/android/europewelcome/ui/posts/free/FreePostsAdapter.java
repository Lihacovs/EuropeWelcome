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

package com.bmd.android.europewelcome.ui.posts.free;

import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bmd.android.europewelcome.R;
import com.bmd.android.europewelcome.data.firebase.model.Post;
import com.bmd.android.europewelcome.di.module.GlideApp;
import com.bmd.android.europewelcome.ui.base.BaseViewHolder;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestoreException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Konstantins on 12/6/2017.
 */

public class FreePostsAdapter extends FirestoreRecyclerAdapter<Post, FreePostsAdapter.ViewHolder> {

    private Callback mCallback;
    private FirestoreRecyclerOptions<Post> mOptions;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See
     * {@link FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public FreePostsAdapter(FirestoreRecyclerOptions<Post> options) {
        super(options);
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, int position, Post model) {
        holder.bind(model);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate
                        (R.layout.item_post_view, parent, false));
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
        void onPostItemViewClick(Post post);

        void onStarIconClick(Post post);
    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.iv_addpostitem_postimage)
        ImageView mPostImageIv;

        @BindView(R.id.tv_postitem_posttitle)
        TextView mPostTitleTv;

        @BindView(R.id.tv_postitem_postauthor)
        TextView mPostAuthorTv;

        @BindView(R.id.tv_postitem_postcreationdate)
        TextView mPostCreationDateTv;

        @BindView(R.id.tv_postitem_posttext)
        TextView mPostTextTv;

        @BindView(R.id.tv_postitem_starcount)
        TextView mPostStarsTv;

        @BindView(R.id.tv_postitem_watchcount)
        TextView mPostWatchesTv;

        @BindView(R.id.iv_postitem_starimage)
        ImageView mStarIv;

        @BindView(R.id.iv_postitem_eyeimage)
        ImageView mEyeIv;

        @BindView(R.id.iv_postitem_shareimage)
        ImageView mShareIv;

        private Post mPost;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {
            //coverImageView.setImageDrawable(null);
            mPostTitleTv.setText("");
            mPostTextTv.setText("");
            mPostAuthorTv.setText("");
            mPostCreationDateTv.setText("");
        }

        public void bind(Post post) {

            mPost = post;

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

            if (post.getPostStars() != null) {
                mPostStarsTv.setText(post.getPostStars());
            }

            if (post.getPostWatches() != null) {
                mPostWatchesTv.setText(post.getPostWatches());
            }

            mShareIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Drawable drawable = mShareIv.getDrawable();
                    if (drawable instanceof Animatable) {
                        ((Animatable) drawable).start();
                    }
                }
            });

            mStarIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int newStarCount = Integer.parseInt(post.getPostStars()) + 1;
                    mPost.setPostStars(Integer.toString(newStarCount));
                    if(mCallback != null)
                        mCallback.onStarIconClick(mPost);
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int newWatches = Integer.parseInt(post.getPostWatches()) + 1;
                    mPost.setPostWatches(Integer.toString(newWatches));
                    if (mCallback != null)
                        mCallback.onPostItemViewClick(mPost);
                }
            });
        }
    }
}
