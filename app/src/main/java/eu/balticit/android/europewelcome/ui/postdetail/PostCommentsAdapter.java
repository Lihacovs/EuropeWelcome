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
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import eu.balticit.android.europewelcome.R;
import eu.balticit.android.europewelcome.data.firebase.model.PostComment;
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
 * Post Comments Adapter
 */
public class PostCommentsAdapter extends FirestoreRecyclerAdapter<PostComment, PostCommentsAdapter.ViewHolder> {

    private Callback mCallback;

    private PostDetailActivity mActivity;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See
     * {@link FirestoreRecyclerOptions} for configuration options.
     *
     * @param options Query in builder
     */
    PostCommentsAdapter(FirestoreRecyclerOptions<PostComment> options, PostDetailActivity activity) {
        super(options);
        mActivity = activity;
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
        /*if (mCallback != null)
            mCallback.hideLoadingSpinner();*/
    }

    @Override
    public void onError(@NonNull FirebaseFirestoreException e) {
        super.onError(e);
    }


    public interface Callback {

        void onLikeCommentClick(PostComment postComment, ViewHolder holder);

        void checkCommentLikedByUser(PostComment postComment, ViewHolder holder);

        void onCommentUserImageClick(String userId);

        void hideLoadingSpinner();
    }

    public class ViewHolder extends BaseViewHolder implements PostDetailActivity.Callback {
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

        @BindView(R.id.cl_comment_item_star_container)
        ConstraintLayout mStarContainerCl;

        private PostComment mPostComment;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            //TODO: should not point to Activity directly
            mActivity.setPostDetailActivityCallback(this);
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

            mPostStarsTv.setText(String.valueOf(postComment.getPostCommentStars()));

            if (mCallback != null) {
                mCallback.checkCommentLikedByUser(mPostComment, this);
            }

        }

        @OnClick(R.id.cl_comment_item_star_container)
        void onLikeCommentClick() {
            if (mCallback != null) {
                mStarContainerCl.setEnabled(false);
                mCallback.onLikeCommentClick(mPostComment, this);
            }
        }

        @OnClick(R.id.iv_comment_item_user_image)
        void onUserImageClick() {
            if (mCallback != null) {
                mCallback.onCommentUserImageClick(mPostComment.getPostCommentUserId());
            }
        }

        @Override
        public void setCommentLikeIcon(ViewHolder holder) {
            holder.mStarIv.setImageResource(R.drawable.ic_fill_star_blue_24px);
            holder.mPostStarsTv.setTextColor(itemView
                    .getContext().getResources().getColor(R.color.orange));
            holder.mStarContainerCl.setEnabled(true);
        }

        @Override
        public void removeCommentLikeIcon(ViewHolder holder) {
            holder.mStarIv.setImageResource(R.drawable.ic_border_star_blue_24px);
            holder.mPostStarsTv.setTextColor(itemView
                    .getContext().getResources().getColor(R.color.gray_500));
            holder.mStarContainerCl.setEnabled(true);
        }
    }
}
