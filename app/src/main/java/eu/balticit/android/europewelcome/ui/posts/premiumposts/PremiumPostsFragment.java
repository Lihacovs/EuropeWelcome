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

package eu.balticit.android.europewelcome.ui.posts.premiumposts;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import butterknife.BindView;
import eu.balticit.android.europewelcome.R;
import eu.balticit.android.europewelcome.data.firebase.model.Post;
import eu.balticit.android.europewelcome.di.component.ActivityComponent;
import eu.balticit.android.europewelcome.ui.base.BaseFragment;

import javax.inject.Inject;

import butterknife.ButterKnife;
import eu.balticit.android.europewelcome.ui.postdetail.PostDetailActivity;
import eu.balticit.android.europewelcome.ui.posts.PostsActivity;

/**
 * Created by BIT on 12/6/2017.
 */

public class PremiumPostsFragment extends BaseFragment implements
        PremiumPostsMvpView, PremiumPostsAdapter.Callback, PostsActivity.Callback {

    private static final String TAG = "PremiumPostsFragment";

    @Inject
    PremiumPostsMvpPresenter<PremiumPostsMvpView> mPresenter;

    @Inject
    LinearLayoutManager mLayoutManager;

    //can be used fragment_free_posts.xml
    @BindView(R.id.free_posts_recycler_view)
    RecyclerView mRecyclerView;

    PremiumPostsAdapter mPremiumPostsAdapter;

    private Callback mCallback;

    public static PremiumPostsFragment newInstance() {
        Bundle args = new Bundle();
        PremiumPostsFragment fragment = new PremiumPostsFragment();
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
            //mOpenSourceAdapter.setCallback(this);
        }

        //((PostsActivity) getActivity()).setActivityCallback(this);
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
        mPremiumPostsAdapter = new PremiumPostsAdapter(
                new FirestoreRecyclerOptions.Builder<Post>()
                        .setQuery(mPresenter.getPostsQueryOrderedByDate(), Post.class)
                        .build(), this);
        mPremiumPostsAdapter.setAdapterCallback(this);
        mRecyclerView.setAdapter(mPremiumPostsAdapter);
        mPresenter.onViewPrepared();

        mPremiumPostsAdapter.startListening();
    }

    @Override
    public void onDestroyView() {
        mPresenter.onDetach();
        mPremiumPostsAdapter.stopListening();
        super.onDestroyView();
    }

    @Override
    public void onFilterPostsByStarsClick() {
        mPremiumPostsAdapter.stopListening();
        mPremiumPostsAdapter = new PremiumPostsAdapter(
                new FirestoreRecyclerOptions.Builder<Post>()
                        .setQuery(mPresenter.getPostsQueryOrderedByStars(), Post.class)
                        .build(), this);
        mPremiumPostsAdapter.setAdapterCallback(this);
        mRecyclerView.setAdapter(mPremiumPostsAdapter);
        mPremiumPostsAdapter.startListening();
    }

    //method not used since Post view count not implemented
    @Override
    public void onFilterPostsByViewsClick() {
        mPremiumPostsAdapter.stopListening();
        mPremiumPostsAdapter = new PremiumPostsAdapter(
                new FirestoreRecyclerOptions.Builder<Post>()
                        .setQuery(mPresenter.getPostsQueryOrderedByViews(), Post.class)
                        .build(), this);
        mPremiumPostsAdapter.setAdapterCallback(this);
        mRecyclerView.setAdapter(mPremiumPostsAdapter);
        mPremiumPostsAdapter.startListening();
    }

    @Override
    public void onFilterPostsByDateClick() {
        mPremiumPostsAdapter.stopListening();
        mPremiumPostsAdapter = new PremiumPostsAdapter(
                new FirestoreRecyclerOptions.Builder<Post>()
                        .setQuery(mPresenter.getPostsQueryOrderedByDate(), Post.class)
                        .build(), this);
        mPremiumPostsAdapter.setAdapterCallback(this);
        mRecyclerView.setAdapter(mPremiumPostsAdapter);
        mPremiumPostsAdapter.startListening();
    }

    @Override
    public void onFilterPostsByCommentsClick() {
        mPremiumPostsAdapter.stopListening();
        mPremiumPostsAdapter = new PremiumPostsAdapter(
                new FirestoreRecyclerOptions.Builder<Post>()
                        .setQuery(mPresenter.getPostsQueryOrderedByComments(), Post.class)
                        .build(), this);
        mPremiumPostsAdapter.setAdapterCallback(this);
        mRecyclerView.setAdapter(mPremiumPostsAdapter);
        mPremiumPostsAdapter.startListening();
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
    public void onStarIconClick(Post post, PremiumPostsAdapter.ViewHolder holder) {
        mPresenter.addOrRemoveStar(post, holder);
    }

    @Override
    public void onBookmarkIconClick(Post post, PremiumPostsAdapter.ViewHolder holder) {
        mPresenter.saveOrDeleteBookmark(post, holder);
    }

    @Override
    public void checkPostBookmarkedByUser(Post post, PremiumPostsAdapter.ViewHolder holder) {
        mPresenter.checkPostBookmarkedByUser(post, holder);
    }

    @Override
    public void checkPostStarRatedByUser(Post post, PremiumPostsAdapter.ViewHolder holder) {
        mPresenter.checkPostStarRatedByUser(post, holder);
    }

    @Override
    public void setBookmarkedIcon(PremiumPostsAdapter.ViewHolder holder) {
        if (mCallback != null) {
            mCallback.setBookmarkedIcon(holder);
        }
    }

    @Override
    public void removeBookmarkedIcon(PremiumPostsAdapter.ViewHolder holder) {
        if (mCallback != null) {
            mCallback.removeBookmarkedIcon(holder);
        }
    }

    @Override
    public void setStarRatedIcon(PremiumPostsAdapter.ViewHolder holder) {
        if (mCallback != null) {
            mCallback.setStarRatedIcon(holder);
        }
    }

    @Override
    public void removeStarRatedIcon(PremiumPostsAdapter.ViewHolder holder) {
        if (mCallback != null) {
            mCallback.removeStarRatedIcon(holder);
        }
    }

    /**
     * Callback for {@link PremiumPostsAdapter}
     */
    public void setPremiumPostFragmentCallback(Callback callback) {
        mCallback = callback;
    }

    public interface Callback {

        void setBookmarkedIcon(PremiumPostsAdapter.ViewHolder holder);

        void removeBookmarkedIcon(PremiumPostsAdapter.ViewHolder holder);

        void setStarRatedIcon(PremiumPostsAdapter.ViewHolder holder);

        void removeStarRatedIcon(PremiumPostsAdapter.ViewHolder holder);
    }
}