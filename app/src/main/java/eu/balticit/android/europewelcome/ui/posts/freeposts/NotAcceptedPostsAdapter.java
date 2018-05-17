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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestoreException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eu.balticit.android.europewelcome.R;
import eu.balticit.android.europewelcome.data.firebase.model.Post;
import eu.balticit.android.europewelcome.di.module.GlideApp;
import eu.balticit.android.europewelcome.ui.base.BaseViewHolder;

public class NotAcceptedPostsAdapter extends FirestoreRecyclerAdapter<Post, NotAcceptedPostsAdapter.ViewHolder> {

    @SuppressWarnings("unused")
    private static final String TAG = "FreePostsAdapter";
    private Callback mCallback;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See
     * {@link FirestoreRecyclerOptions} for configuration options.
     *
     * @param options - data to query from DB
     */
    NotAcceptedPostsAdapter(FirestoreRecyclerOptions<Post> options) {
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
                (R.layout.item_not_accepted_post_view, parent, false));
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

    public void setAdapterCallback(Callback callback) {
        mCallback = callback;
    }

    public interface Callback {

        void hideLoadingSpinner();

        void onPostItemViewClick(Post post);

        void acceptPost(Post post);

        void editPost(Post post);

        void unpublishPost(Post post);

        void deletePost(Post post);
    }

    public class ViewHolder extends BaseViewHolder{

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

        private Post mPost;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
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


            itemView.setOnClickListener(v -> {
                if (mCallback != null)
                    mCallback.onPostItemViewClick(mPost);
            });
        }

        @OnClick(R.id.btn_post_item_accept_post)
        void onAcceptPostBtnClick(){
            if(mCallback != null){
                mCallback.acceptPost(mPost);
            }
        }

        @OnClick(R.id.btn_post_item_delete_post)
        void onDeletePostBtnClick(){
            if(mCallback != null){
                mCallback.deletePost(mPost);
            }
        }

        @OnClick(R.id.btn_post_item_edit_post)
        void onEditPostBtnClick(){
            if(mCallback != null){
                mCallback.editPost(mPost);
            }
        }

        @OnClick(R.id.btn_post_item_unpublish_post)
        void onUnpublishPostBtnClick(){
            if(mCallback != null){
                mCallback.unpublishPost(mPost);
            }
        }
    }
}
