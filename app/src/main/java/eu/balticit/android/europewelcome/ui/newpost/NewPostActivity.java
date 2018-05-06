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

package eu.balticit.android.europewelcome.ui.newpost;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import eu.balticit.android.europewelcome.R;
import eu.balticit.android.europewelcome.data.firebase.model.Post;
import eu.balticit.android.europewelcome.data.firebase.model.PostSection;
import eu.balticit.android.europewelcome.ui.base.BaseActivity;
import eu.balticit.android.europewelcome.utils.CommonUtils;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * NewPost Activity. Every {@link Post} can have {@link PostSection} stored as subcollection in DB.
 * {@link PostSection} are used for post content as text, image, map, video type;
 */
public class NewPostActivity extends BaseActivity implements NewPostMvpView, NewPostAdapter.Callback {

    @SuppressWarnings("unused")
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

    @BindView(R.id.iv_new_post_text_icon)
    ImageView mNewPostTextIv;

    @BindView(R.id.iv_new_post_image_icon)
    ImageView mNewPostImageIv;

    @BindView(R.id.iv_new_post_location_icon)
    ImageView mNewPostLocationIv;

    @BindView(R.id.iv_new_post_video_icon)
    ImageView mNewPostVideoIv;

    NewPostAdapter mNewPostAdapter;

    //param: postId - null if new post, nonnull if post came from drafts
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

        mPresenter.onAttach(this);

        setUp();

    }


    @Override
    protected void setUp() {

        showLoading();
        disableIcons();

        mPresenter.setPost((String) getIntent().getSerializableExtra(EXTRA_POST_ID));

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mNewPostRv.setLayoutManager(mLayoutManager);
        mNewPostRv.setItemAnimator(new DefaultItemAnimator());
        mNewPostAdapter = new NewPostAdapter(new FirestoreRecyclerOptions.Builder<PostSection>()
                .setQuery(mPresenter.getPostSectionQuery(), PostSection.class)
                .build(), this);
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

        mPresenter.checkVideoPostSection();
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
    public void hideLoadingSpinner() {
        hideLoading();
    }

    @Override
    public void enableIcons() {
        mNewPostTextIv.setEnabled(true);
        mNewPostImageIv.setEnabled(true);
        mNewPostLocationIv.setEnabled(true);
        mNewPostVideoIv.setEnabled(true);
    }

    @Override
    public void disableIcons() {
        mNewPostTextIv.setEnabled(false);
        mNewPostImageIv.setEnabled(false);
        mNewPostLocationIv.setEnabled(false);
        mNewPostVideoIv.setEnabled(false);
    }

    @Override
    public void finishActivity() {
        this.finish();
    }

    @Override
    public void showYouTubeUrlDialog() {
        //TODO: make custom dialog with post from clipboard, share from youtube APP
        //show dialog to post youtube video URL
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText edittext = new EditText(this);
        alert.setMessage("Add YouTube video URL from clipboard")
                .setView(edittext)
                .setPositiveButton("Okay", (dialog, whichButton) -> {
                    String YouEditTextValue = edittext.getText().toString();
                    mPresenter.newVideoPostSection(CommonUtils.extractYTId(YouEditTextValue));
                }).setNegativeButton("Cancel", (dialog, whichButton) -> {
        }).show();
    }

    @OnClick(R.id.tv_new_post_publish)
    void onPublishTvClick() {
        clearFocusForTextSave();
        mPresenter.publishPost();
    }

    @Override
    public void onBackPressed() {
        clearFocusForTextSave();
        mPresenter.savePostToDraft();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                clearFocusForTextSave();
                mPresenter.savePostToDraft();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Triggers EditText fields in {@link NewPostAdapter} to save text data to DB
     */
    private void clearFocusForTextSave() {
        View current = getCurrentFocus();
        if (current != null) current.clearFocus();
    }
}
