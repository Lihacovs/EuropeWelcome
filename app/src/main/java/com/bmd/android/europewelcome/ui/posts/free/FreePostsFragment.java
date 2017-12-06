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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bmd.android.europewelcome.R;
import com.bmd.android.europewelcome.data.firebase.model.Post;
import com.bmd.android.europewelcome.di.component.ActivityComponent;
import com.bmd.android.europewelcome.ui.base.BaseFragment;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Konstantins on 12/6/2017.
 */

public class FreePostsFragment extends BaseFragment implements
        FreePostsMvpView, FreePostsAdapter.Callback {

    private static final String TAG = "FreePostsFragment";

    @Inject
    FreePostsMvpPresenter<FreePostsMvpView> mPresenter;

    @Inject
    LinearLayoutManager mLayoutManager;

    @BindView(R.id.add_post)
    Button addPostButton;

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

        addPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Post post = new Post(null
                        , "Jonathan Doherty"
                        , "New Awesome Post"
                        , " Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed consectetur dolor venenatis, sodales lacus id, pellentesque lorem. Nunc tempus viverra nibh, ut semper metus lacinia non. Sed mollis scelerisque odio. Suspendisse vitae aliquet diam. Nulla facilisi. Morbi faucibus consequat metus fringilla hendrerit. Integer sed eleifend quam.\n" +
                        "\n" +
                        "Praesent at turpis egestas, fermentum lacus vel, posuere magna. Aenean laoreet interdum neque, eu vehicula risus commodo quis. Sed ultricies felis id imperdiet convallis. Phasellus condimentum, ligula varius pulvinar bibendum, elit erat condimentum turpis, ac sollicitudin tellus mauris a nibh. Integer ullamcorper enim vitae turpis fringilla, vel faucibus erat tempus. Praesent luctus eu ex non vehicula. Pellentesque non nisi non arcu venenatis condimentum. Sed non pellentesque sem. Aliquam varius mauris massa, ut tincidunt sem condimentum in. Vestibulum id ipsum id neque auctor sollicitudin. Vestibulum nec egestas sapien. Vestibulum placerat felis eu orci porttitor, in placerat est hendrerit. Sed id tincidunt lorem. "
                        , "18"
                        , "182"
                        , null
                        , "18 Oct 2017");
                mPresenter.savePost(post);
            }
        });

        return view;
    }

    @Override
    protected void setUp(View view) {
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mFreePostsAdapter = new FreePostsAdapter(
                new FirestoreRecyclerOptions.Builder<Post>()
                        .setQuery(mPresenter.getPostsQuery(), Post.class)
                        .setLifecycleOwner(this)
                        .build());
        mFreePostsAdapter.setCallback(this);
        mRecyclerView.setAdapter(mFreePostsAdapter);

        mPresenter.onViewPrepared();
    }

    @Override
    public void onDestroyView() {
        mPresenter.onDetach();
        super.onDestroyView();
    }

    @Override
    public void onBlogEmptyViewRetryClick() {

    }
}
