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

package com.bmd.android.europewelcome.ui.bookmarks;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.bmd.android.europewelcome.R;
import com.bmd.android.europewelcome.data.firebase.model.Post;
import com.bmd.android.europewelcome.ui.base.BaseActivity;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookmarksActivity extends BaseActivity implements BookmarksMvpView, BookmarksAdapter.Callback {

    private static final String TAG = "BookmarksActivity";

    @Inject
    BookmarksMvpPresenter<BookmarksMvpView> mPresenter;

    @Inject
    LinearLayoutManager mLayoutManager;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.rv_bookmarks_container)
    RecyclerView mBookmarksRv;

    BookmarksAdapter mBookmarksAdapter;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, BookmarksActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);

        getActivityComponent().inject(this);

        setUnBinder(ButterKnife.bind(this));

        mPresenter.onAttach(this);

        setUp();
    }

    @Override
    protected void setUp() {
        showLoading();

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBookmarksRv.setLayoutManager(mLayoutManager);
        mBookmarksRv.setItemAnimator(new DefaultItemAnimator());
        mBookmarksAdapter = new BookmarksAdapter(new FirestoreRecyclerOptions.Builder<Post>()
                .setQuery(mPresenter.getBookmarkedPostsQuery(), Post.class)
                .build());
        mBookmarksAdapter.setAdapterCallback(this);
        mBookmarksRv.setAdapter(mBookmarksAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        mBookmarksAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mBookmarksAdapter.stopListening();
    }

    @Override
    public void hideLoadingSpinner() {
        hideLoading();
    }
}
