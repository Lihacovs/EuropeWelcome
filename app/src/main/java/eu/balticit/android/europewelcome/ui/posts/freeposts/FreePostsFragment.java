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

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import eu.balticit.android.europewelcome.R;
import eu.balticit.android.europewelcome.data.firebase.model.Post;
import eu.balticit.android.europewelcome.di.component.ActivityComponent;
import eu.balticit.android.europewelcome.ui.base.BaseFragment;
import eu.balticit.android.europewelcome.ui.postdetail.PostDetailActivity;
import eu.balticit.android.europewelcome.ui.posts.PostsActivity;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * FreePosts Fragment.
 */
public class FreePostsFragment extends BaseFragment implements
        FreePostsMvpView, FreePostsAdapter.Callback, NotAcceptedPostsAdapter.Callback, PostsActivity.Callback {

    public static final String TAG = "FreePostsFragment";

    @Inject
    FreePostsMvpPresenter<FreePostsMvpView> mPresenter;

    @Inject
    LinearLayoutManager mLayoutManager;

    @BindView(R.id.free_posts_recycler_view)
    RecyclerView mRecyclerView;

    FreePostsAdapter mFreePostsAdapter;

    NotAcceptedPostsAdapter mNotAcceptedPostsAdapter;

    private Callback mCallback;

    public static FreePostsFragment newInstance() {
        Bundle args = new Bundle();
        FreePostsFragment fragment = new FreePostsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_free_posts, container, false);

        ActivityComponent component = getActivityComponent();
        if (component != null) {
            component.inject(this);
            setUnBinder(ButterKnife.bind(this, view));
            mPresenter.onAttach(this);
        }

        ((PostsActivity) getBaseActivity()).setActivityCallback(this);
        return view;
    }

    //sets callback for this tab fragment when user see it
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (getBaseActivity() != null)
                ((PostsActivity) getBaseActivity()).setActivityCallback(this);
        }
    }

    @Override
    protected void setUp(View view) {
        showLoading();
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mFreePostsAdapter = new FreePostsAdapter(
                new FirestoreRecyclerOptions.Builder<Post>()
                        .setQuery(mPresenter.getPostsQueryOrderedByDate(), Post.class)
                        .build(), this);
        mFreePostsAdapter.setAdapterCallback(this);
        mRecyclerView.setAdapter(mFreePostsAdapter);
        mPresenter.onViewPrepared();

        mFreePostsAdapter.startListening();
    }

    @Override
    public void onDestroyView() {
        mPresenter.onDetach();
        mFreePostsAdapter.stopListening();
        super.onDestroyView();
    }

    @Override
    public void onFilterPostsByStarsClick() {
        mFreePostsAdapter.stopListening();
        mFreePostsAdapter = new FreePostsAdapter(
                new FirestoreRecyclerOptions.Builder<Post>()
                        .setQuery(mPresenter.getPostsQueryOrderedByStars(), Post.class)
                        .build(), this);
        mFreePostsAdapter.setAdapterCallback(this);
        mRecyclerView.setAdapter(mFreePostsAdapter);
        mFreePostsAdapter.startListening();
    }

    //method not used since Post view count not implemented
    @Override
    public void onFilterPostsByViewsClick() {
        mFreePostsAdapter.stopListening();
        mFreePostsAdapter = new FreePostsAdapter(
                new FirestoreRecyclerOptions.Builder<Post>()
                        .setQuery(mPresenter.getPostsQueryOrderedByViews(), Post.class)
                        .build(), this);
        mFreePostsAdapter.setAdapterCallback(this);
        mRecyclerView.setAdapter(mFreePostsAdapter);
        mFreePostsAdapter.startListening();
    }

    @Override
    public void onFilterPostsByDateClick() {
        mFreePostsAdapter.stopListening();
        mFreePostsAdapter = new FreePostsAdapter(
                new FirestoreRecyclerOptions.Builder<Post>()
                        .setQuery(mPresenter.getPostsQueryOrderedByDate(), Post.class)
                        .build(), this);
        mFreePostsAdapter.setAdapterCallback(this);
        mRecyclerView.setAdapter(mFreePostsAdapter);
        mFreePostsAdapter.startListening();
    }

    @Override
    public void onFilterPostsByCommentsClick() {
        mFreePostsAdapter.stopListening();
        mFreePostsAdapter = new FreePostsAdapter(
                new FirestoreRecyclerOptions.Builder<Post>()
                        .setQuery(mPresenter.getPostsQueryOrderedByComments(), Post.class)
                        .build(), this);
        mFreePostsAdapter.setAdapterCallback(this);
        mRecyclerView.setAdapter(mFreePostsAdapter);
        mFreePostsAdapter.startListening();
    }

    @Override
    public void onTabLongClick() {
        mPresenter.checkUser();
    }

    //Loads posts to accept by Admin
    @Override
    public void loadNotAcceptedPosts() {
        mFreePostsAdapter.stopListening();
        mNotAcceptedPostsAdapter = new NotAcceptedPostsAdapter(
                new FirestoreRecyclerOptions.Builder<Post>()
                        .setQuery(mPresenter.getNotAcceptedPostsQuery(), Post.class)
                        .build());
        mNotAcceptedPostsAdapter.setAdapterCallback(this);
        mRecyclerView.setAdapter(mNotAcceptedPostsAdapter);
        mNotAcceptedPostsAdapter.startListening();
    }

    @Override
    public void acceptPost(Post post) {
        mPresenter.acceptPost(post);
    }

    @Override
    public void deletePost(Post post) {
        mPresenter.deletePost(post);
    }

    @Override
    public void hideLoadingSpinner() {
        hideLoading();
    }

    @Override
    public void onPostItemViewClick(Post post) {
        startActivity(PostDetailActivity.getStartIntent(this.getContext(), post.getPostId()));
    }

    @Override
    public void onStarIconClick(Post post, FreePostsAdapter.ViewHolder holder) {
        mPresenter.addOrRemoveStar(post, holder);
    }

    @Override
    public void onBookmarkIconClick(Post post, FreePostsAdapter.ViewHolder holder) {
        mPresenter.saveOrDeleteBookmark(post, holder);
    }

    @Override
    public void checkPostBookmarkedByUser(Post post, FreePostsAdapter.ViewHolder holder) {
        mPresenter.checkPostBookmarkedByUser(post, holder);
    }

    @Override
    public void checkPostStarRatedByUser(Post post, FreePostsAdapter.ViewHolder holder) {
        mPresenter.checkPostStarRatedByUser(post, holder);
    }

    @Override
    public void setBookmarkedIcon(FreePostsAdapter.ViewHolder holder) {
        if (mCallback != null) {
            mCallback.setBookmarkedIcon(holder);
        }
    }

    @Override
    public void removeBookmarkedIcon(FreePostsAdapter.ViewHolder holder) {
        if (mCallback != null) {
            mCallback.removeBookmarkedIcon(holder);
        }
    }

    @Override
    public void setStarRatedIcon(FreePostsAdapter.ViewHolder holder) {
        if (mCallback != null) {
            mCallback.setStarRatedIcon(holder);
        }
    }

    @Override
    public void removeStarRatedIcon(FreePostsAdapter.ViewHolder holder) {
        if (mCallback != null) {
            mCallback.removeStarRatedIcon(holder);
        }
    }

    /**
     * Callback for {@link FreePostsAdapter}
     */
    public void setFreePostFragmentCallback(Callback callback) {
        mCallback = callback;
    }

    public interface Callback {

        void setBookmarkedIcon(FreePostsAdapter.ViewHolder holder);

        void removeBookmarkedIcon(FreePostsAdapter.ViewHolder holder);

        void setStarRatedIcon(FreePostsAdapter.ViewHolder holder);

        void removeStarRatedIcon(FreePostsAdapter.ViewHolder holder);
    }
}