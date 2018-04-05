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
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.bmd.android.europewelcome.R;
import com.bmd.android.europewelcome.data.firebase.model.Post;
import com.bmd.android.europewelcome.ui.base.BaseActivity;
import com.bmd.android.europewelcome.ui.postdetail.PostDetailActivity;
import com.bmd.android.europewelcome.ui.posts.free.FreePostsAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Bookmarks Activity. Data queried and populated to {users}->{userId}->{bookmarks}->{postId} from
 * {@link FreePostsAdapter.ViewHolder#onBookmarkIconClick()}.
 * If Firestore team release "collection group query" data should be stored and queried from
 * {posts}->{postId}->{bookmarks}->{userId} to avoid Post entity duplication in DB.
 */
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

    @NonNull
    public static Intent getStartIntent(Context context) {
        return new Intent(context, BookmarksActivity.class);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.bookmarks_menu, menu);
        return true;
    }

    //TODO: implement menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                upIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    // This activity is NOT part of this app's task, so create a new task
                    // when navigating up, with a synthesized back stack.
                    TaskStackBuilder.create(this)
                            // Add all of this activity's parents to the back stack
                            .addNextIntentWithParentStack(upIntent)
                            // Navigate up to the closest parent
                            .startActivities();
                } else {
                    // This activity is part of this app's task, so simply
                    // navigate up to the logical parent activity.
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
            case R.id.delete_all_bookmarks:
                showMessage("Delete All Bookmarks");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void hideLoadingSpinner() {
        hideLoading();
    }

    //TODO: implement menu

    @Override
    public void onBookmarkIconClick(Post post) {
        showYesNoDialog(getString(R.string.bookmarks_remove_bookmark),
                getString(R.string.bookmarks_remove),
                getString(R.string.bookmarks_cancel),
                (dialog, which) -> {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            mPresenter.deleteBookmark(post);
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            break;
                    }
                });
    }

    @Override
    public void onViewHolderClick(Post post) {
        mPresenter.updatePost(post);
        startActivity(PostDetailActivity.getStartIntent(this, post.getPostId()));
    }
}
