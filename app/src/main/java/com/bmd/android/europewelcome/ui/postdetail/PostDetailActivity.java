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
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bmd.android.europewelcome.R;
import com.bmd.android.europewelcome.data.firebase.model.PostComment;
import com.bmd.android.europewelcome.data.firebase.model.PostImage;
import com.bmd.android.europewelcome.data.firebase.model.PostPlace;
import com.bmd.android.europewelcome.data.firebase.model.PostSection;
import com.bmd.android.europewelcome.data.firebase.model.PostText;
import com.bmd.android.europewelcome.di.module.GlideApp;
import com.bmd.android.europewelcome.ui.base.BaseActivity;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Konstantins on 12/7/2017.
 */

public class PostDetailActivity extends BaseActivity implements PostDetailMvpView {

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

    @BindView(R.id.ll_postdetail_postcontent)
    LinearLayout mPostContentLl;

    @BindView(R.id.ll_postdetail_comments)
    LinearLayout mPostCommentsLl;

    @BindView(R.id.tv_postdetail_posttitle)
    TextView mPostTitle;

    @BindView(R.id.iv_postdetail_userimage)
    ImageView mPostUserImageIv;

    @BindView(R.id.tv_postdetail_username)
    TextView mPostUserNameTv;

    @BindView(R.id.tv_postdetail_postcreationdate)
    TextView mPostDateTv;

    @BindView(R.id.rv_postdetail_postsection)
    RecyclerView mPostSectionRv;

    @BindView(R.id.rv_postdetail_comments)
    RecyclerView mRecyclerView;

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
        mPostSectionRv.setAdapter(mPostSectionAdapter);


        mLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager2);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mPostCommentsAdapter = new PostCommentsAdapter(
                new FirestoreRecyclerOptions.Builder<PostComment>()
                        .setQuery(mPresenter.getPostCommentsQuery(), PostComment.class)
                        .build());
        //mPostCommentsAdapter.setAdapterCallback(this);
        mRecyclerView.setAdapter(mPostCommentsAdapter);


        mPresenter.getPost(mPostId);



    }

    @Override
    public void onStart() {
        super.onStart();
        mPostCommentsAdapter.startListening();
        mPostSectionAdapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        mPostCommentsAdapter.stopListening();
        mPostSectionAdapter.stopListening();
    }


    @OnClick(R.id.iv_postdetail_newcommentsend)
    void onCommentSend(){
        PostComment postComment = new PostComment(
                mPostId,
                null,
                null,
                null,
                null,
                "Some Awesome Post PostComment",
                "8"
                );

        mPresenter.saveComment(mPostId, postComment);
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

    /**
     * Attaches PostImage block layout to parent layout
     * @param postImage PostImage to attach
     */
    @Override
    public void attachPostImageLayout(final PostImage postImage){
        final ImageView imageIv;

        View imageView = LayoutInflater.from(this)
                .inflate(R.layout.item_postdetail_image, mPostContentLl, false);
        mPostContentLl.addView(imageView);

        imageIv = imageView.findViewById(R.id.iv_postdetailitem_image);
        GlideApp.with(this)
                .load(postImage.getPostImageUrl())
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageIv);
    }

    /**
     * Attaches PostText block layout to parent layout
     * @param postText PostText entity to attach
     */
    @Override
    public void attachPostTextLayout(final PostText postText){

        final View textLayout = LayoutInflater.from(this)
                .inflate(R.layout.item_postdetail_text, mPostContentLl, false);
        mPostContentLl.addView(textLayout);

        TextView textTv = textLayout.findViewById(R.id.tv_postdetailitem_posttext);
        textTv.setText(postText.getPostText());
        textTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, postText.getPostTextSize());
        if(postText.isPostTextBold() & !postText.isPostTextItalic()){
            textTv.setTypeface(null, Typeface.BOLD);
        }
        if(!postText.isPostTextBold() & postText.isPostTextItalic()){
            textTv.setTypeface(null, Typeface.ITALIC);
        }
        if(postText.isPostTextBold() & postText.isPostTextItalic()){
            textTv.setTypeface(null, Typeface.BOLD_ITALIC);
        }
    }

    @Override
    public void attachPostPlaceLayout(PostPlace postPlace) {
        final View mapLayout = LayoutInflater.from(this)
                .inflate(R.layout.item_postdetail_map, mPostContentLl, false);
        mPostContentLl.addView(mapLayout);

        MapView mapView = mapLayout.findViewById(R.id.mv_postdetailitem_map);
        mapView.onCreate(null);
        mapView.onResume();

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                LatLng place = new LatLng(postPlace.getPostPlaceLat(), postPlace.getPostPlaceLng());

                googleMap.addMarker(new MarkerOptions()
                        .position(place)
                        .title(postPlace.getPostPlaceName()));

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(place)
                        .zoom(15)
                        .build();
                googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });
    }

    @Override
    public void attachCommentLayout(PostComment postComment) {
        final View commentLayout = LayoutInflater.from(this)
                .inflate(R.layout.item_postdetail_comment, mPostCommentsLl, false);
        mPostContentLl.addView(commentLayout);

        TextView commentTv = commentLayout.findViewById(R.id.tv_postdetailitem_comment);
        commentTv.setText(postComment.getPostCommentText());
    }


}