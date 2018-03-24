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

package com.bmd.android.europewelcome.ui.posts.free;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bmd.android.europewelcome.R;
import com.bmd.android.europewelcome.data.firebase.model.Post;
import com.bmd.android.europewelcome.di.component.ActivityComponent;
import com.bmd.android.europewelcome.ui.base.BaseFragment;
import com.bmd.android.europewelcome.ui.postdetail.PostDetailActivity;
import com.bmd.android.europewelcome.ui.posts.PostsActivity;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Konstantins on 12/6/2017.
 */

public class FreePostsFragment extends BaseFragment implements
        FreePostsMvpView, FreePostsAdapter.Callback, PostsActivity.Callback{

    private static final String TAG = "FreePostsFragment";

    @Inject
    FreePostsMvpPresenter<FreePostsMvpView> mPresenter;

    @Inject
    LinearLayoutManager mLayoutManager;

    @BindView(R.id.free_posts_recycler_view)
    RecyclerView mRecyclerView;

    FreePostsAdapter mFreePostsAdapter;

    public static FreePostsFragment newInstance() {
        Bundle args = new Bundle();
        FreePostsFragment fragment = new FreePostsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_free_posts, container, false);

        ActivityComponent component = getActivityComponent();
        if (component != null) {
            component.inject(this);
            setUnBinder(ButterKnife.bind(this, view));
            mPresenter.onAttach(this);
        }

        ((PostsActivity) getActivity()).setActivityCallback(this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mFreePostsAdapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        mFreePostsAdapter.stopListening();
    }

    @Override
    protected void setUp(View view) {
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mFreePostsAdapter = new FreePostsAdapter(
                new FirestoreRecyclerOptions.Builder<Post>()
                        .setQuery(mPresenter.getPostsQuery(), Post.class)
                        .build());
        mFreePostsAdapter.setAdapterCallback(this);
        mRecyclerView.setAdapter(mFreePostsAdapter);
        mPresenter.onViewPrepared();
    }

    @Override
    public void onDestroyView() {
        mPresenter.onDetach();
        super.onDestroyView();
    }

    @Override
    public void onPostItemViewClick(Post post) {
        mPresenter.updatePost(post);
        startActivity(PostDetailActivity.getStartIntent(this.getContext(), post.getPostId()));
    }

    @Override
    public void onStarIconClick(Post post) {
        mPresenter.updatePost(post);
    }

    @Override
    public void onFilterPostsByStarsClick() {
        mFreePostsAdapter.stopListening();
        mFreePostsAdapter = new FreePostsAdapter(
                new FirestoreRecyclerOptions.Builder<Post>()
                        .setQuery(mPresenter.getPostsQueryOrderedByStars(), Post.class)
                        .build());
        mFreePostsAdapter.setAdapterCallback(this);
        mRecyclerView.setAdapter(mFreePostsAdapter);
        mFreePostsAdapter.startListening();
    }

    @Override
    public void onFilterPostsByViewsClick() {
        mFreePostsAdapter.stopListening();
        mFreePostsAdapter = new FreePostsAdapter(
                new FirestoreRecyclerOptions.Builder<Post>()
                        .setQuery(mPresenter.getPostsQueryOrderedByViews(), Post.class)
                        .build());
        mFreePostsAdapter.setAdapterCallback(this);
        mRecyclerView.setAdapter(mFreePostsAdapter);
        mFreePostsAdapter.startListening();
    }

    @Override
    public void onFilterPostsByDateClick() {
        mFreePostsAdapter = new FreePostsAdapter(
                new FirestoreRecyclerOptions.Builder<Post>()
                        .setQuery(mPresenter.getPostsQueryOrderedByDate(), Post.class)
                        .build());
        mFreePostsAdapter.setAdapterCallback(this);
        mRecyclerView.setAdapter(mFreePostsAdapter);
    }
}