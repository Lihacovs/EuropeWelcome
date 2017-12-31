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

package com.bmd.android.europewelcome.ui.addpost;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.bmd.android.europewelcome.R;
import com.bmd.android.europewelcome.data.firebase.model.PostImage;
import com.bmd.android.europewelcome.data.firebase.model.PostPlace;
import com.bmd.android.europewelcome.data.firebase.model.PostText;
import com.bmd.android.europewelcome.di.module.GlideApp;
import com.bmd.android.europewelcome.ui.base.BaseActivity;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by Konstantins on 12/7/2017.
 */

public class AddPostActivity extends BaseActivity implements AddPostMvpView,
        EasyPermissions.PermissionCallbacks {

    private static final int RC_CHOOSE_PHOTO = 101;
    private static final int RC_IMAGE_PERMS = 102;
    private static final int PLACE_PICKER_REQUEST = 1;
    private static final String PERMS = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final String TAG = "AddPostActivity";

    @Inject
    AddPostMvpPresenter<AddPostMvpView> mPresenter;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.et_addpost_posttitle)
    EditText mPostTitleEt;

    @BindView(R.id.iv_addpost_texticon)
    ImageView mTextImageIv;

    @BindView(R.id.iv_addpost_imageicon)
    ImageView mImageIconIv;

    @BindView(R.id.ll_addpost_postcontent)
    ViewGroup mPostContentLl;

    @BindView(R.id.sv_addpost_postcontent)
    ScrollView mScrollView;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, AddPostActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

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

        try {
            MapsInitializer.initialize(this.getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.iv_addpost_texticon)
    void onTextIconClick(){
        attachPostTextLayout(mPresenter.newPostText());
        mScrollView.post(new Runnable() {
            @Override
            public void run() {
                mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    @OnClick(R.id.iv_addpost_imageicon)
    @AfterPermissionGranted(RC_IMAGE_PERMS)
    void onImageIconClick(){
        if (!EasyPermissions.hasPermissions(this, PERMS)) {
            EasyPermissions.requestPermissions(this, getString(R.string.rational_image_perm),
                    RC_IMAGE_PERMS, PERMS);
            return;
        }

        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RC_CHOOSE_PHOTO);
    }

    @OnClick(R.id.iv_addpost_locationicon)
    void onLocationIconClick(){
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

    @OnClick(R.id.iv_addpost_videoicon)
    void onVideoIconClick(){
        attachPostVideoLayout();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                //attach map layout with selected place
                PostPlace postPlace = mPresenter.newPostPlace();
                postPlace.setPostPlaceAddress(place.getAddress().toString());
                postPlace.setPostPlaceName(place.getName().toString());
                postPlace.setPostPlaceLat(place.getLatLng().latitude);
                postPlace.setPostPlaceLng(place.getLatLng().longitude);
                attachPostMapLayout(postPlace);

                String toastMsg = String.format("Place: %s", place.getName() + " " + place.getLatLng().toString());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();

            }
        }

        if (requestCode == RC_CHOOSE_PHOTO) {
            if (resultCode == RESULT_OK) {
                Uri selectedImage = data.getData();
                mPresenter.uploadFileToStorage(selectedImage);

                mScrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });
            } else {
                Toast.makeText(this, "No image chosen", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE
                && EasyPermissions.hasPermissions(this, PERMS)) {
            onImageIconClick();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.addpost_menu, menu);
        return true;
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
            case R.id.save_post:
                //removing focus will trigger PostText update in Presenter list
                mPostContentLl.requestFocus();

                mPresenter.setPostTitle(mPostTitleEt.getText().toString());
                mPresenter.savePost();
                this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }

    /**
     * Handles actions in new added PostImage block layout
     * @param postImage PostImage to attach
     */
    @Override
    public void attachPostImageLayout(final PostImage postImage){
        final ImageView imageIv;
        //Adds PostImage to list and to layout
        mPresenter.addPostImageToList(postImage);

        View imageView = LayoutInflater.from(this)
                .inflate(R.layout.item_addpost_image, mPostContentLl, false);
        mPostContentLl.addView(imageView);

        imageIv = imageView.findViewById(R.id.iv_addpostitem_image);
        GlideApp.with(this)
                .load(postImage.getPostImageUrl())
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageIv);

        //Removes PostImage from list and from layout
        ImageView deleteImageIv = imageView.findViewById(R.id.iv_addpostitem_deleteimage);
        deleteImageIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.removePostImageFromList(postImage);
                View parentView = (View) view.getParent();
                mPostContentLl.removeView(parentView);
            }
        });
    }

    /**
     * Handles actions in added PostText block layout
     * @param postText PostText entity to attach
     */
    @Override
    public void attachPostTextLayout(final PostText postText){
        final EditText textEt;
        //Adds PostText to list and to layout
        mPresenter.addPostTextToList(postText);

        final View textLayout = LayoutInflater.from(this)
                .inflate(R.layout.item_addpost_text, mPostContentLl, false);
        mPostContentLl.addView(textLayout);

        //Removes PostText from list and from layout
        ImageView deleteTextIv = textLayout.findViewById(R.id.iv_addpostitem_deletetext);
        deleteTextIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View parentView = (View) view.getParent();
                mPostContentLl.removeView(parentView);
                mPresenter.removePostTextFromList(postText);
            }
        });

        textEt = textLayout.findViewById(R.id.et_addpostitem_text);
        textEt.setTypeface(Typeface.DEFAULT);
        //postText.setPostText(textEt.getText().toString());

        //TODO: save text to Entity on focus change
        textEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    //removing focus from textPost block will trigger post update
                    postText.setPostText(textEt.getText().toString());
                    mPresenter.updatePostTextInList(postText);
                }
            }
        });

        //TODO: add comment
        final ImageView formatBoldIv = textLayout.findViewById(R.id.iv_addpostitem_formatbold);
        formatBoldIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textEt.getTypeface().getStyle() == Typeface.BOLD) {
                    textEt.setTypeface(Typeface.DEFAULT);
                    formatBoldIv.setImageDrawable(ResourcesCompat.getDrawable(
                            getResources(), R.drawable.ic_format_bold_black_24dp, null));
                    postText.setPostTextBold(false);
                } else if (textEt.getTypeface().getStyle() == Typeface.ITALIC) {
                    textEt.setTypeface(textEt.getTypeface(), Typeface.BOLD_ITALIC);
                    formatBoldIv.setImageDrawable(ResourcesCompat.getDrawable(
                            getResources(), R.drawable.ic_format_bold_off_black_24dp, null));
                    postText.setPostTextBold(true);
                } else if (textEt.getTypeface().getStyle() == Typeface.BOLD_ITALIC) {
                    textEt.setTypeface(textEt.getTypeface(), Typeface.ITALIC);
                    formatBoldIv.setImageDrawable(ResourcesCompat.getDrawable(
                            getResources(), R.drawable.ic_format_bold_black_24dp, null));
                    postText.setPostTextBold(false);
                } else {
                    textEt.setTypeface(textEt.getTypeface(), Typeface.BOLD);
                    formatBoldIv.setImageDrawable(ResourcesCompat.getDrawable(
                            getResources(), R.drawable.ic_format_bold_off_black_24dp, null));
                    postText.setPostTextBold(true);
                }
                mPresenter.updatePostTextInList(postText);
            }

        });

        //TODO: add comment
        final ImageView formatItalicIv = textLayout.findViewById(R.id.iv_addpostitem_formatitalic);
        formatItalicIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textEt.getTypeface().getStyle() == Typeface.ITALIC) {
                    textEt.setTypeface(Typeface.DEFAULT);
                    formatItalicIv.setImageDrawable(ResourcesCompat.getDrawable(
                            getResources(), R.drawable.ic_format_italic_black_24dp, null));
                    postText.setPostTextItalic(false);
                } else if (textEt.getTypeface().getStyle() == Typeface.BOLD) {
                    textEt.setTypeface(textEt.getTypeface(), Typeface.BOLD_ITALIC);
                    formatItalicIv.setImageDrawable(ResourcesCompat.getDrawable(
                            getResources(), R.drawable.ic_format_italic_off_black_24dp, null));
                    postText.setPostTextItalic(true);
                } else if (textEt.getTypeface().getStyle() == Typeface.BOLD_ITALIC) {
                    textEt.setTypeface(textEt.getTypeface(), Typeface.ITALIC);
                    formatItalicIv.setImageDrawable(ResourcesCompat.getDrawable(
                            getResources(), R.drawable.ic_format_italic_black_24dp, null));
                    postText.setPostTextItalic(false);
                } else {
                    textEt.setTypeface(textEt.getTypeface(), Typeface.ITALIC);
                    formatItalicIv.setImageDrawable(ResourcesCompat.getDrawable(
                            getResources(), R.drawable.ic_format_italic_off_black_24dp, null));
                    postText.setPostTextItalic(true);
                }
                mPresenter.updatePostTextInList(postText);
            }
        });

        ImageView formatSizeIv = textLayout.findViewById(R.id.iv_addpostitem_formatsize);
        formatSizeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float textSizePx = textEt.getTextSize();
                float density = getResources().getDisplayMetrics().scaledDensity;
                float textSizeSp = textSizePx / density;
                showMessage(Float.toString(textSizeSp));
                if(textSizeSp <24) {
                    textEt.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSizeSp + 2);
                }else{
                    //TODO add constant to dimen file
                    textEt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                }
                postText.setPostTextSize(textEt.getTextSize()/density);
                mPresenter.updatePostTextInList(postText);
            }
        });
    }

    @Override
    public void attachPostMapLayout(PostPlace postPlace) {

        mPresenter.addPostPlaceToList(postPlace);
        MapView mapView;

        final View mapLayout = LayoutInflater.from(this)
                .inflate(R.layout.item_addpost_map, mPostContentLl, false);
        mPostContentLl.addView(mapLayout);

        //Removes PostText from list and from layout
        ImageView deleteTextIv = mapLayout.findViewById(R.id.iv_addpostitem_deletemap);
        deleteTextIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View parentView = (View) view.getParent();
                mPostContentLl.removeView(parentView);
                mPresenter.removePostPlaceFromList(postPlace);
            }
        });

        mapView = mapLayout.findViewById(R.id.mv_addpostitem_map);
        mapView.onCreate(null);
        mapView.onResume();

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                // Add a marker in Sydney, Australia,
                // and move the map's camera to the same location.
                LatLng place = new LatLng(postPlace.getPostPlaceLat(), postPlace.getPostPlaceLng());
                googleMap.addMarker(new MarkerOptions()
                        .position(place)
                        .title(postPlace.getPostPlaceName()));

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(place).zoom(15).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });

    }

    @Override
    public void attachPostVideoLayout() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        // See #choosePhoto with @AfterPermissionGranted
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this,
                Collections.singletonList(PERMS))) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

}