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

package eu.balticit.android.europewelcome.ui.posts.freeposts;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import eu.balticit.android.europewelcome.R;
import eu.balticit.android.europewelcome.data.firebase.model.Post;
import eu.balticit.android.europewelcome.di.module.GlideApp;
import eu.balticit.android.europewelcome.ui.base.BaseViewHolder;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestoreException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * FreePosts Adapter.
 */
public class FreePostsAdapter extends FirestoreRecyclerAdapter<Post, FreePostsAdapter.ViewHolder> {

    private static final String TAG = "FreePostsAdapter";
    private Callback mCallback;
    private FreePostsFragment mFragment;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See
     * {@link FirestoreRecyclerOptions} for configuration options.
     *
     * @param options - data to query from DB
     */
    public FreePostsAdapter(FirestoreRecyclerOptions<Post> options, FreePostsFragment fragment) {
        super(options);
        mFragment = fragment;
    }

    public void setAdapterCallback(Callback callback) {
        mCallback = callback;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Post model) {
        holder.bind(model);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate
                        (R.layout.item_post_view, parent, false));
    }

    @Override
    public void onDataChanged() {
        // Called each time there is a new query snapshot. You may want to use this method
        // to hide a loading spinner or check for the "no documents" state and update your UI.
        // ...
        if (mCallback != null)
            mCallback.hideLoadingSpinner();
    }

    @Override
    public void onError(@NonNull FirebaseFirestoreException e) {
        super.onError(e);
    }


    public interface Callback {

        void hideLoadingSpinner();

        void onPostItemViewClick(Post post);

        void onStarIconClick(Post post, ViewHolder holder);

        void onBookmarkIconClick(Post post, ViewHolder holder);

        void checkPostBookmarkedByUser(Post post, ViewHolder holder);

        void checkPostStarRatedByUser(Post post, ViewHolder holder);
    }

    public class ViewHolder extends BaseViewHolder implements FreePostsFragment.Callback{

        @BindView(R.id.iv_post_item_author_photo)
        ImageView mUserPhotoIv;

        @BindView(R.id.tv_post_item_author_name)
        TextView mPostAuthorTv;

        @BindView(R.id.tv_post_item_post_date)
        TextView mPostCreationDateTv;

        @BindView(R.id.iv_post_item_post_image)
        ImageView mPostImageIv;

        @BindView(R.id.tv_post_item_post_title)
        TextView mPostTitleTv;

        @BindView(R.id.tv_post_item_post_text)
        TextView mPostTextTv;

        @BindView(R.id.tv_post_item_stars_count)
        TextView mPostStarsTv;

        @BindView(R.id.tv_post_item_comment_count)
        TextView mCommentsCountTv;

        @BindView(R.id.iv_post_item_star_image)
        ImageView mStarIv;

        @BindView(R.id.iv_post_item_comment_image)
        ImageView mCommentIv;

        @BindView(R.id.iv_post_item_bookmark_image)
        ImageView mBookmarkIv;

        @BindView(R.id.cl_post_item_star_container)
        ConstraintLayout mStarContainerCl;

        private Post mPost;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            //TODO: should not point to Activity directly
            mFragment.setFreePostFragmentCallback(this);
        }

        protected void clear() {
            mPostTitleTv.setText("");
            mPostTextTv.setText("");
            mPostAuthorTv.setText("");
            mPostCreationDateTv.setText("");
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

            mPostStarsTv.setText(String.valueOf(post.getPostStars()));
            mCommentsCountTv.setText(String.valueOf(post.getPostComments()));

            //updates UI for current user
            if(mCallback != null) {
                mCallback.checkPostBookmarkedByUser(mPost, this);
                mCallback.checkPostStarRatedByUser(mPost, this);
            }

            itemView.setOnClickListener(v -> {
                if (mCallback != null)
                    mCallback.onPostItemViewClick(mPost);
            });
        }

        @OnClick(R.id.cl_post_item_star_container)
        void onStarIconClick(){
            if(mCallback != null){
                mStarContainerCl.setEnabled(false);
                mCallback.onStarIconClick(mPost, this);
            }

        }

        @OnClick(R.id.iv_post_item_bookmark_image)
        void onBookmarkIconClick(){
            if(mCallback != null){
                mBookmarkIv.setEnabled(false);
                mCallback.onBookmarkIconClick(mPost, this);
            }
        }

        /**
         * Callback method launched from {@link FreePostsFragment#setBookmarkedIcon(ViewHolder)}
         * @param holder - appropriate holder to update
         */
        @Override
        public void setBookmarkedIcon(ViewHolder holder) {
            holder.mBookmarkIv.setImageResource(R.drawable.ic_fill_bookmark_blue_24px);
            holder.mBookmarkIv.setEnabled(true);
        }

        /**
         * Callback method launched from {@link FreePostsFragment#removeBookmarkedIcon(ViewHolder)}
         * @param holder - appropriate holder to update
         */
        @Override
        public void removeBookmarkedIcon(ViewHolder holder) {
            holder.mBookmarkIv.setImageResource(R.drawable.ic_border_bookmark_gray_24px);
            holder.mBookmarkIv.setEnabled(true);
        }

        /**
         * Callback method launched from {@link FreePostsFragment#setStarRatedIcon(ViewHolder)}
         * @param holder - appropriate holder to update
         */
        @Override
        public void setStarRatedIcon(ViewHolder holder) {
            holder.mStarIv.setImageResource(R.drawable.ic_fill_star_gold_24px);
            holder.mPostStarsTv.setTextColor(itemView
                    .getContext().getResources().getColor(R.color.orange));
            holder.mStarContainerCl.setEnabled(true);
        }

        /**
         * Callback method launched from {@link FreePostsFragment#removeStarRatedIcon(ViewHolder)}
         * @param holder - appropriate holder to update
         */
        @Override
        public void removeStarRatedIcon(ViewHolder holder) {
            holder.mStarIv.setImageResource(R.drawable.ic_border_star_gray_24px);
            holder.mPostStarsTv.setTextColor(itemView
                    .getContext().getResources().getColor(R.color.gray_500));
            holder.mStarContainerCl.setEnabled(true);
        }
    }
}
