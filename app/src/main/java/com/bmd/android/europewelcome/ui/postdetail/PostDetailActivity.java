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

package com.bmd.android.europewelcome.ui.postdetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bmd.android.europewelcome.R;
import com.bmd.android.europewelcome.data.firebase.model.PostComment;
import com.bmd.android.europewelcome.data.firebase.model.PostSection;
import com.bmd.android.europewelcome.di.module.GlideApp;
import com.bmd.android.europewelcome.ui.base.BaseActivity;
import com.bmd.android.europewelcome.ui.profile.ProfileActivity;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Post Detail Activity
 */

public class PostDetailActivity extends BaseActivity implements PostDetailMvpView,
        PostSectionAdapter.Callback, PostCommentsAdapter.Callback {

    private static final String EXTRA_POST_ID =
            "com.bmd.android.europewelcome.postdetail.post_id";

    @Inject
    PostDetailMvpPresenter<PostDetailMvpView> mPresenter;

    @Inject
    LinearLayoutManager mLayoutManager;

    @Inject
    LinearLayoutManager mLayoutManager2;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.tv_postdetail_posttitle)
    TextView mPostTitle;

    @BindView(R.id.iv_postdetail_userimage)
    ImageView mPostUserImageIv;

    @BindView(R.id.tv_postdetail_username)
    TextView mPostUserNameTv;

    @BindView(R.id.tv_postdetail_postcreationdate)
    TextView mPostDateTv;

    @BindView(R.id.iv_postdetail_newcommentuserimage)
    ImageView mPostNewCommentUserImageIv;

    @BindView(R.id.et_postdetail_newcommenttext)
    EditText mPostNewCommentTextEt;

    @BindView(R.id.rv_postdetail_postsection)
    RecyclerView mPostSectionRv;

    @BindView(R.id.rv_postdetail_comments)
    RecyclerView mPostCommentsRv;

    PostSectionAdapter mPostSectionAdapter;

    PostCommentsAdapter mPostCommentsAdapter;

    private String mPostId;

    public static Intent getStartIntent(Context context, String postId) {
        Intent intent = new Intent(context, PostDetailActivity.class);
        intent.putExtra(EXTRA_POST_ID, postId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        mPostId = (String) getIntent().getSerializableExtra(EXTRA_POST_ID);

        getActivityComponent().inject(this);

        setUnBinder(ButterKnife.bind(this));

        mPresenter.onAttach(this);

        setUp();
    }

    @Override
    protected void setUp() {

        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        mPresenter.setPostId(mPostId);


        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mPostSectionRv.setLayoutManager(mLayoutManager);
        mPostSectionRv.setItemAnimator(new DefaultItemAnimator());
        mPostSectionAdapter = new PostSectionAdapter(
                new FirestoreRecyclerOptions.Builder<PostSection>()
                        .setQuery(mPresenter.getPostSectionQuery(), PostSection.class)
                        .build());
        mPostSectionAdapter.setAdapterCallback(this);
        mPostSectionRv.setAdapter(mPostSectionAdapter);


        mLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        mPostCommentsRv.setLayoutManager(mLayoutManager2);
        mPostCommentsRv.setItemAnimator(new DefaultItemAnimator());
        mPostCommentsAdapter = new PostCommentsAdapter(
                new FirestoreRecyclerOptions.Builder<PostComment>()
                        .setQuery(mPresenter.getPostCommentsQuery(), PostComment.class)
                        .build());
        mPostCommentsAdapter.setAdapterCallback(this);
        mPostCommentsRv.setAdapter(mPostCommentsAdapter);


        mPresenter.getPost(mPostId);

    }

    @Override
    public void onStart() {
        super.onStart();
        mPostSectionAdapter.startListening();
        mPostCommentsAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPostSectionAdapter.stopListening();
        mPostCommentsAdapter.stopListening();
    }


    @OnClick(R.id.iv_postdetail_newcommentsend)
    void onCommentSend(){
        if(!mPostNewCommentTextEt.getText().toString().equals("") &&
                !mPostNewCommentTextEt.getText().toString().isEmpty()) {
            PostComment postComment = mPresenter.newPostComment();
            postComment.setPostCommentText(mPostNewCommentTextEt.getText().toString());
            mPresenter.saveComment(mPostId, postComment);
        }
        mPostNewCommentTextEt.setText(null);
    }

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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }

    @Override
    public void setPostTitle(String postTitle) {
        if (postTitle != null) {
            mPostTitle.setText(postTitle);
        }
    }

    @OnClick(R.id.iv_postdetail_userimage)
    void onUserImageIvClick(){
        startActivity(ProfileActivity.getStartIntent(getBaseContext(), mPresenter.getPostAuthorId()));
    }

    @Override
    public void setPostUserImage(String userImageUrl) {
        if(userImageUrl != null){
            GlideApp.with(this)
                    .load(userImageUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(mPostUserImageIv);
        }
    }

    @Override
    public void setPostUserName(String userName) {
        if(userName != null){
            mPostUserNameTv.setText(userName);
        }
    }

    @Override
    public void setPostCreationDate(String creationDate) {
        if(creationDate != null){
            mPostDateTv.setText(creationDate);
        }
    }

    @Override
    public void setPostNewCommentUserImage(String userImageUrl) {
        if(userImageUrl != null)
            GlideApp.with(this)
                    .load(userImageUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(mPostNewCommentUserImageIv);
    }


    @Override
    public void onZoomIconClick(PostSection postSection) {
        showMessage("Zoom Icon Click");
    }

    @Override
    public void onStarIconClick(PostComment postComment) {
        showMessage("Star Icon Click");
    }
}