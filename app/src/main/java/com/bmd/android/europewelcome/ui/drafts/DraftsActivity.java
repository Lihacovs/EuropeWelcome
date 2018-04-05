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

package com.bmd.android.europewelcome.ui.drafts;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.bmd.android.europewelcome.ui.newpost.NewPostActivity;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;


import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Drafts Activity. Data queried from {posts} where postAsDraft flag is true.
 */
public class DraftsActivity extends BaseActivity implements DraftsMvpView, DraftsAdapter.Callback{

    private static final String TAG = "AddPostActivity";

    @Inject
    DraftsMvpPresenter<DraftsMvpView> mPresenter;

    @Inject
    LinearLayoutManager mLayoutManager;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.rv_drafts_container)
    RecyclerView mDraftsRv;

    DraftsAdapter mDraftsAdapter;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, DraftsActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drafts);

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
        mDraftsRv.setLayoutManager(mLayoutManager);
        mDraftsRv.setItemAnimator(new DefaultItemAnimator());
        mDraftsAdapter = new DraftsAdapter(new FirestoreRecyclerOptions.Builder<Post>()
                        .setQuery(mPresenter.getPostAsDraftQuery(), Post.class)
                        .build());
        mDraftsAdapter.setAdapterCallback(this);
        mDraftsRv.setAdapter(mDraftsAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        mDraftsAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mDraftsAdapter.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.drafts_menu, menu);
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
            case R.id.delete_all_drafts:
                showMessage("Delete All Drafts");
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Callback methods from {@link DraftsAdapter}
     *
     * @param post - entity to process
     */
    @Override
    public void deleteDraft(Post post) {
        showYesNoDialog(getString(R.string.drafts_delete_draft),
                getString(R.string.drafts_delete),
                getString(R.string.drafts_cancel),
                (dialog, which) -> {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            mPresenter.deleteDraft(post);
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            break;
                    }
                });
    }

    @Override
    public void openDraft(Post post) {
        startActivity(NewPostActivity.getStartIntent(this, post.getPostId()));
    }

    @Override
    public void hideLoadingSpinner() {
        hideLoading();
    }
}
