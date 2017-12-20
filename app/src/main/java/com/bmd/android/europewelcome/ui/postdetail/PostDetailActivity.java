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
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bmd.android.europewelcome.R;
import com.bmd.android.europewelcome.data.firebase.model.PostImage;
import com.bmd.android.europewelcome.data.firebase.model.PostText;
import com.bmd.android.europewelcome.di.module.GlideApp;
import com.bmd.android.europewelcome.ui.base.BaseActivity;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Konstantins on 12/7/2017.
 */

public class PostDetailActivity extends BaseActivity implements PostDetailMvpView {

    private static final String EXTRA_POST_ID =
            "com.bmd.android.europewelcome.postdetail.post_id";

    @Inject
    PostDetailMvpPresenter<PostDetailMvpView> mPresenter;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.ll_post_postcontent)
    LinearLayout mPostContentLl;

    @BindView(R.id.tv_post_posttitle)
    TextView mPostTitle;

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
        mPresenter.getPost(mPostId);
        mPresenter.getPostTextList(mPostId);
        mPresenter.getPostImageList(mPostId);

        //mPresenter.attachContentToLayout();
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

    /**
     * Attaches PostImage block layout to parent layout
     * @param postImage PostImage to attach
     */
    @Override
    public void attachPostImageLayout(final PostImage postImage){
        final ImageView imageIv;

        View imageView = LayoutInflater.from(this)
                .inflate(R.layout.item_addpost_image, mPostContentLl, false);
        mPostContentLl.addView(imageView);

        imageIv = imageView.findViewById(R.id.iv_addpostitem_image);
        GlideApp.with(this)
                .load(postImage.getPostImageUrl())
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageIv);
    }

    /**
     * Handles actions in added PostText block layout
     * @param postText PostText entity to attach
     */
    @Override
    public void attachPostTextLayout(final PostText postText){

        final View textLayout = LayoutInflater.from(this)
                .inflate(R.layout.item_addpost_text, mPostContentLl, false);
        mPostContentLl.addView(textLayout);

        EditText textEt = textLayout.findViewById(R.id.et_addpostitem_text);
        textEt.setText(postText.getPostText());
    }
}
