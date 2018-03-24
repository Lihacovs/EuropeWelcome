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

package com.bmd.android.europewelcome.ui.newpost;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.bmd.android.europewelcome.R;
import com.bmd.android.europewelcome.data.firebase.model.PostSection;
import com.bmd.android.europewelcome.ui.base.BaseActivity;
import com.bmd.android.europewelcome.utils.ScreenUtils;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.MapsInitializer;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * New Post Activity
 */

public class NewPostActivity extends BaseActivity implements NewPostMvpView, NewPostAdapter.Callback {

    private static final String TAG = "AddPostActivity";
    private static final int RC_CHOOSE_PHOTO = 100;
    private static final int RC_IMAGE_PERMS = 101;
    private static final int PLACE_PICKER_REQUEST = 102;
    private static final String PERMS = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final String EXTRA_POST_ID =
            "com.bmd.android.europewelcome.newpost.post_id";

    @Inject
    NewPostMvpPresenter<NewPostMvpView> mPresenter;

    @Inject
    LinearLayoutManager mLayoutManager;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.rv_new_post_container)
    RecyclerView mNewPostRv;

    NewPostAdapter mNewPostAdapter;

    private Parcelable state;

    //TODO: drafts
    //param: postId - null if new post, nonnull if post came for edit/draft
    public static Intent getStartIntent(Context context, @Nullable String postId) {
        Intent intent = new Intent(context, NewPostActivity.class);
        intent.putExtra(EXTRA_POST_ID, postId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        getActivityComponent().inject(this);

        setUnBinder(ButterKnife.bind(this));

        //TODO: Initialize maps once in App?
        try {
            MapsInitializer.initialize(this.getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mPresenter.onAttach(this);

        mPresenter.setPost((String) getIntent().getSerializableExtra(EXTRA_POST_ID));

        setUp();

    }


    @Override
    protected void setUp() {

        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mNewPostRv.setLayoutManager(mLayoutManager);
        mNewPostRv.setItemAnimator(new DefaultItemAnimator());
        mNewPostAdapter = new NewPostAdapter(this,
                new FirestoreRecyclerOptions.Builder<PostSection>()
                        .setQuery(mPresenter.getPostSectionQuery(), PostSection.class)
                        .build());
        mNewPostAdapter.setAdapterCallback(this);
        mNewPostAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                mNewPostRv.smoothScrollToPosition(mNewPostAdapter.getItemCount());
            }
        });
        mNewPostRv.setAdapter(mNewPostAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        mNewPostAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mNewPostAdapter.stopListening();
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }

    @OnClick(R.id.iv_new_post_text_icon)
    void onTextIconClick() {
        mPresenter.newTextPostSection();
    }

    @OnClick(R.id.iv_new_post_image_icon)
    @AfterPermissionGranted(RC_IMAGE_PERMS)
    void onImageIconClick() {
        if (!EasyPermissions.hasPermissions(this, PERMS)) {
            EasyPermissions.requestPermissions(this, getString(R.string.rational_image_perm),
                    RC_IMAGE_PERMS, PERMS);
            return;
        }

        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RC_CHOOSE_PHOTO);
    }

    @OnClick(R.id.iv_new_post_location_icon)
    void onLocationIconClick() {
        //Instantiate Google PlacePicker API
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.iv_new_post_video_icon)
    void onVideoIconClick() {
        //TODO: make dialog to pick video code
        mPresenter.newVideoPostSection("aMimeO279YE");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_CHOOSE_PHOTO) {
            if (resultCode == RESULT_OK) {
                Uri selectedImage = data.getData();
                mPresenter.uploadImageToStorage(selectedImage, this);
            }
        } else if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE
                && EasyPermissions.hasPermissions(this, PERMS)) {
            onImageIconClick();
        }

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                mPresenter.newMapPostSection(place);
            }
        }
    }

    /**
     * Callback methods from {@link NewPostAdapter}
     *
     * @param postSection - entity to process
     */

    @Override
    public void deletePostSection(PostSection postSection) {
        mPresenter.deletePostSection(postSection);
    }

    @Override
    public void updatePostSection(PostSection postSection) {
        mPresenter.updatePostSection(postSection);
    }

    @Override
    public void finishActivity() {
        this.finish();
    }

    @Override
    public void scrollViewToBottom() {

    }

    @OnClick(R.id.tv_new_post_publish)
    void onPublishTvClick() {
        clearFocusForTextSave();
        mPresenter.publishPost();
    }

    @Override
    public void onBackPressed() {
        showSavePostToDraftsDialog();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                showSavePostToDraftsDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showSavePostToDraftsDialog() {
        clearFocusForTextSave();
        showSimpleDialog(getString(R.string.newpost_save_to_draft),
                getString(R.string.newpost_save),
                getString(R.string.newpost_delete),
                (dialog, which) -> {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            mPresenter.savePostToDraft();
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            mPresenter.deletePost();
                            break;
                    }
                });
    }

    /**
     * Triggers EditText fields in {@link NewPostAdapter} to save text data to DB
     */
    private void clearFocusForTextSave(){
        View current = getCurrentFocus();
        if (current != null) current.clearFocus();
    }
}
