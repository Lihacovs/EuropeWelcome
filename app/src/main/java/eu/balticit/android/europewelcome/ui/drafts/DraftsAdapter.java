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

package eu.balticit.android.europewelcome.ui.drafts;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import eu.balticit.android.europewelcome.R;
import eu.balticit.android.europewelcome.data.firebase.model.Post;
import eu.balticit.android.europewelcome.ui.base.BaseViewHolder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestoreException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Bookmarks Adapted. Data queried from {users}->{userId}->{bookmarks}->{postId}
 * If Firestore team release "collection group query" data should be stored and queried from
 * {posts}->{postId}->{bookmarks}->{userId} to avoid Post entity duplication in DB.
 */
public class DraftsAdapter extends FirestoreRecyclerAdapter<Post, DraftsAdapter.DraftViewHolder> {

    @SuppressWarnings("unused")
    private static final String TAG = "DraftsAdapter";

    private DraftsAdapter.Callback mCallback;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See
     * {@link FirestoreRecyclerOptions} for configuration options.
     *
     * @param options Query in builder
     */
    DraftsAdapter(FirestoreRecyclerOptions<Post> options) {
        super(options);
    }

    void setAdapterCallback(Callback callback) {
        mCallback = callback;
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

    @Override
    protected void onBindViewHolder(@NonNull DraftViewHolder holder, int position, @NonNull Post model) {
        holder.bind(model);
    }


    @NonNull
    @Override
    public DraftViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DraftViewHolder(LayoutInflater.from(parent.getContext()).inflate
                (R.layout.item_draft, parent, false));
    }

    public interface Callback {

        void deleteDraft(Post post);

        void openDraft(Post post);

        void hideLoadingSpinner();
    }


    public class DraftViewHolder extends BaseViewHolder {

        Post mPost;

        @BindView(R.id.tv_draft_item_post_title)
        TextView mPostTitleTv;

        @BindView(R.id.tv_draft_item_post_date)
        TextView mPostCreationDateTv;

        DraftViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        protected void clear() {

        }

        public void bind(Post post) {
            mPost = post;

            if (post.getPostTitle() != null) {
                mPostTitleTv.setText(post.getPostTitle());
            }

            if (post.getPostCreationDate() != null) {
                mPostCreationDateTv.setText(post.getPostCreationDate());
            }

            itemView.setOnClickListener(v -> {
                if (mCallback != null)
                    mCallback.openDraft(mPost);
            });
        }

        @OnClick (R.id.iv_draft_item_delete_icon)
        void onDeleteIconClick(){
            if (mCallback != null)
                mCallback.deleteDraft(mPost);
        }
    }
}
