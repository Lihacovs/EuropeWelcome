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

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.bmd.android.europewelcome.R;
import com.bmd.android.europewelcome.data.firebase.model.PostSection;
import com.bmd.android.europewelcome.di.module.GlideApp;
import com.bmd.android.europewelcome.ui.base.BaseViewHolder;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Adapter for {@link NewPostActivity}
 */

public class NewPostAdapter extends FirestoreRecyclerAdapter<PostSection, BaseViewHolder> {

    private static final int VIEW_TYPE_TEXT = 0;
    private static final int VIEW_TYPE_IMAGE = 1;
    private static final int VIEW_TYPE_MAP = 2;
    private static final int VIEW_TYPE_VIDEO = 3;

    private static final String TAG = "NewPostAdapter";

    private Callback mCallback;

    private Context mContext;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See
     * {@link FirestoreRecyclerOptions} for configuration options.
     *
     * @param options Query in builder
     */
    NewPostAdapter(Context context, FirestoreRecyclerOptions<PostSection> options) {
        super(options);
        mContext = context;
    }

    void setAdapterCallback(Callback callback) {
        mCallback = callback;
    }

    @Override
    protected void onBindViewHolder(BaseViewHolder holder, int position, PostSection model) {
        holder.onBind(position, model);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_TEXT:
                return new NewPostAdapter.TextViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_new_post_text, parent, false));
            case VIEW_TYPE_IMAGE:
                return new NewPostAdapter.ImageViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_new_post_image, parent, false));
            case VIEW_TYPE_MAP:
                return new NewPostAdapter.MapViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_new_post_map, parent, false));
            case VIEW_TYPE_VIDEO:
                return new NewPostAdapter.VideoViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_new_post_video, parent, false));
            default:
                return new NewPostAdapter.TextViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_new_post_text, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position).getPostSectionViewType().equals("Text")) {
            return VIEW_TYPE_TEXT;
        } else if (getItem(position).getPostSectionViewType().equals("Image")) {
            return VIEW_TYPE_IMAGE;
        } else if (getItem(position).getPostSectionViewType().equals("Map")) {
            return VIEW_TYPE_MAP;
        } else if (getItem(position).getPostSectionViewType().equals("Video")) {
            return VIEW_TYPE_VIDEO;
        } else {
            return -1;
        }
    }

    @Override
    public void onDataChanged() {
        // Called each time there is a new query snapshot. You may want to use this method
        // to hide a loading spinner or check for the "no documents" state and update your UI.
        // ...
    }

    @Override
    public void onError(FirebaseFirestoreException e) {
        super.onError(e);
        Log.d(TAG, "onError: " + e);
    }

    public interface Callback {
        //void onZoomIconClick(PostSection postSection);
        void deletePostSection(PostSection postSection);

        void updatePostSection(PostSection postSection);
    }

    public class TextViewHolder extends BaseViewHolder {

        @BindView(R.id.et_new_post_item_text)
        EditText mPostTextEt;

        @BindView(R.id.iv_new_post_item_format_bold)
        ImageView mFormatBoldIv;

        @BindView(R.id.iv_new_post_item_format_italic)
        ImageView mFormatItalicIv;

        PostSection mPostSection;

        TextViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {
            mPostTextEt.setText("");
        }

        public void onBind(int position, PostSection model) {
            super.onBind(position, model);

            mPostSection = model;

            if (model.getPostText() != null) {
                mPostTextEt.setText(model.getPostText());
                mPostTextEt.setSelection(mPostTextEt.getText().length());
            }

            mPostTextEt.setTextSize(TypedValue.COMPLEX_UNIT_SP, model.getPostTextSize());

            if (!model.isPostTextBold() & !model.isPostTextItalic()) {
                mPostTextEt.setTypeface(Typeface.DEFAULT);
                mFormatBoldIv.setImageResource(R.drawable.ic_format_bold_black_24dp);
                mFormatItalicIv.setImageResource(R.drawable.ic_format_italic_black_24dp);
            }
            if (model.isPostTextBold() & !model.isPostTextItalic()) {
                mPostTextEt.setTypeface(null, Typeface.BOLD);
                mFormatBoldIv.setImageResource(R.drawable.ic_format_bold_off_black_24dp);
                mFormatItalicIv.setImageResource(R.drawable.ic_format_italic_black_24dp);
            }
            if (!model.isPostTextBold() & model.isPostTextItalic()) {
                mPostTextEt.setTypeface(null, Typeface.ITALIC);
                mFormatBoldIv.setImageResource(R.drawable.ic_format_bold_black_24dp);
                mFormatItalicIv.setImageResource(R.drawable.ic_format_italic_off_black_24dp);
            }
            if (model.isPostTextBold() & model.isPostTextItalic()) {
                mPostTextEt.setTypeface(null, Typeface.BOLD_ITALIC);
                mFormatBoldIv.setImageResource(R.drawable.ic_format_bold_off_black_24dp);
                mFormatItalicIv.setImageResource(R.drawable.ic_format_italic_off_black_24dp);
            }

            mPostTextEt.addTextChangedListener(
                    new TextWatcher() {
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                        }

                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        private Timer timer = new Timer();
                        private final long DELAY = 1000; // milliseconds

                        @Override
                        public void afterTextChanged(final Editable s) {
                            timer.cancel();
                            timer = new Timer();
                            timer.schedule(
                                    new TimerTask() {
                                        @Override
                                        public void run() {
                                            // TODO: Optimize code below
                                            // you will probably need to use runOnUiThread(Runnable action) for some specific actions
                                            if (mPostSection.getPostText() == null) {
                                                mPostSection.setPostText(mPostTextEt.getText().toString());
                                                if (mCallback != null)
                                                    mCallback.updatePostSection(mPostSection);
                                            } else if (!mPostSection.getPostText().equals(mPostTextEt.getText().toString())) {
                                                mPostSection.setPostText(mPostTextEt.getText().toString());
                                                if (mCallback != null)
                                                    mCallback.updatePostSection(mPostSection);
                                            }
                                        }
                                    },
                                    DELAY
                            );
                        }
                    }
            );
        }

        @OnClick(R.id.iv_new_post_item_delete_text)
        void sectionDeleteClick() {
            if (mCallback != null)
                mCallback.deletePostSection(mPostSection);
        }

        @OnClick(R.id.iv_new_post_item_format_bold)
        void formatBoldIconClick(ImageView v) {
            if (mPostTextEt.getTypeface().getStyle() == Typeface.BOLD |
                    mPostTextEt.getTypeface().getStyle() == Typeface.BOLD_ITALIC) {
                mPostSection.setPostTextBold(false);
            } else {
                mPostSection.setPostTextBold(true);
            }
            if (mCallback != null)
                mCallback.updatePostSection(mPostSection);
        }

        @OnClick(R.id.iv_new_post_item_format_italic)
        void formatItalicIconClick(ImageView v) {
            if (mPostTextEt.getTypeface().getStyle() == Typeface.ITALIC |
                    mPostTextEt.getTypeface().getStyle() == Typeface.BOLD_ITALIC) {
                mPostSection.setPostTextItalic(false);
            } else {
                mPostSection.setPostTextItalic(true);
            }
            if (mCallback != null)
                mCallback.updatePostSection(mPostSection);
        }

        @OnClick(R.id.iv_new_post_item_format_size)
        void formatSizeIconClick(ImageView v) {
            if (mPostSection.getPostTextSize() < 24) {
                mPostSection.setPostTextSize(mPostSection.getPostTextSize() + 2);
            } else {
                mPostSection.setPostTextSize(18);
            }
            if (mCallback != null)
                mCallback.updatePostSection(mPostSection);
        }
    }

    public class ImageViewHolder extends BaseViewHolder {

        @BindView(R.id.iv_new_post_item_image)
        ImageView mPostImageIv;

        PostSection mPostSection;

        ImageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {
            mPostImageIv.setImageDrawable(null);
        }

        public void onBind(int position, PostSection model) {
            super.onBind(position, model);

            mPostSection = model;

            if (model.getPostImageUrl() != null) {
                GlideApp.with(itemView.getContext())
                        .load(model.getPostImageUrl())
                        .centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(mPostImageIv);
            }
        }

        @OnClick(R.id.iv_new_post_item_delete_image)
        void onDeleteImageClick() {
            if (mCallback != null)
                mCallback.deletePostSection(mPostSection);
        }
    }

    public class MapViewHolder extends BaseViewHolder {

        @BindView(R.id.mv_new_post_item_map)
        com.google.android.gms.maps.MapView mMapView;

        PostSection mPostSection;

        MapViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {
        }

        public void onBind(int position, PostSection model) {
            super.onBind(position, model);

            mPostSection = model;

            mMapView.onCreate(null);
            mMapView.onResume();

            mMapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    LatLng place = new LatLng(model.getPostPlaceLat(), model.getPostPlaceLng());

                    googleMap.addMarker(new MarkerOptions()
                            .position(place)
                            .title(model.getPostPlaceName()));

                    // For zooming automatically to the location of the marker
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(place)
                            .zoom(15)
                            .build();
                    googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
            });
        }

        @OnClick(R.id.iv_new_post_item_delete_map)
        void onDeleteMapClick() {
            if (mCallback != null)
                mCallback.deletePostSection(mPostSection);
        }
    }

    public class VideoViewHolder extends BaseViewHolder {

        YouTubePlayerFragment mYouTubePlayerFragment =
                (YouTubePlayerFragment) ((Activity) mContext).getFragmentManager().findFragmentById(R.id.fragment_new_post_item_youtube_video);
        YouTubePlayer.OnInitializedListener onInitializedListener;

        PostSection mPostSection;

        VideoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {
        }

        public void onBind(int position, PostSection model) {
            super.onBind(position, model);

            mPostSection = model;

            mYouTubePlayerFragment.initialize("AIzaSyByraF4EaclX12v43bAKldVHZfMjazz9y8", new YouTubePlayer.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean b) {

                    youTubePlayer.cueVideo("PEGccV-NOm8");
                    youTubePlayer.setShowFullscreenButton(false);
                }

                @Override
                public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                }
            });

        }

        @OnClick(R.id.btn_addpostitem_playvideo)
        void onPlayButtonClick() {
            //mYouTubePlayerFragment.initialize("AIzaSyByraF4EaclX12v43bAKldVHZfMjazz9y8", onInitializedListener);
        }

        @OnClick(R.id.iv_new_post_item_delete_video)
        void onDeleteMapClick() {
            if (mCallback != null)
                mCallback.deletePostSection(mPostSection);
        }
    }
}
