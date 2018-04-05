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

package com.bmd.android.europewelcome.ui.postdetail;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bmd.android.europewelcome.R;
import com.bmd.android.europewelcome.data.firebase.model.PostSection;
import com.bmd.android.europewelcome.di.module.GlideApp;
import com.bmd.android.europewelcome.ui.base.BaseViewHolder;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;

import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.firebase.firestore.FirebaseFirestoreException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Post Section Adapter
 */
public class PostSectionAdapter extends FirestoreRecyclerAdapter<PostSection, BaseViewHolder> {

    private static final int VIEW_TYPE_TITLE = 0;
    private static final int VIEW_TYPE_TEXT = 1;
    private static final int VIEW_TYPE_IMAGE = 2;
    private static final int VIEW_TYPE_MAP = 3;
    private static final int VIEW_TYPE_VIDEO = 4;

    private Callback mCallback;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See
     * {@link FirestoreRecyclerOptions} for configuration options.
     *
     * @param options Query in builder
     */
    PostSectionAdapter(FirestoreRecyclerOptions<PostSection> options) {
        super(options);
    }

    void setAdapterCallback(Callback callback) {
        mCallback = callback;
    }

    @Override
    protected void onBindViewHolder(@NonNull BaseViewHolder holder,
                                    int position,
                                    @NonNull PostSection model) {
        holder.onBind(position, model);
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_TITLE:
                return new TitleViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_post_detail_title, parent, false));
            case VIEW_TYPE_TEXT:
                return new TextViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_post_detail_text, parent, false));
            case VIEW_TYPE_IMAGE:
                return new ImageViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_post_detail_image, parent, false));
            case VIEW_TYPE_MAP:
                return new MapViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_post_detail_map, parent, false));
            case VIEW_TYPE_VIDEO:
                return new VideoViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_post_detail_video, parent, false));
            default:
                return new TitleViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_post_detail_title, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (getItem(position).getPostSectionViewType()) {
            case "Title":
                return VIEW_TYPE_TITLE;
            case "Text":
                return VIEW_TYPE_TEXT;
            case "Image":
                return VIEW_TYPE_IMAGE;
            case "Map":
                return VIEW_TYPE_MAP;
            case "Video":
                return VIEW_TYPE_VIDEO;
            default:
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
    public void onError(@NonNull FirebaseFirestoreException e) {
        super.onError(e);
    }


    public interface Callback {

        void onImageClick(PostSection postSection);
    }

    public class TitleViewHolder extends BaseViewHolder {

        @BindView(R.id.tv_post_detail_item_post_title)
        TextView mPostTitleTv;

        TitleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {
            mPostTitleTv.setText("");
        }

        public void onBind(int position, PostSection model) {
            super.onBind(position, model);

            if (model.getPostText() != null) {
                mPostTitleTv.setText(model.getPostTitle());
            }
        }
    }

    public class TextViewHolder extends BaseViewHolder {

        @BindView(R.id.tv_post_detail_item_post_text)
        TextView mPostTextTv;

        TextViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {
            mPostTextTv.setText("");
        }

        public void onBind(int position, PostSection model) {
            super.onBind(position, model);

            if (model.getPostText() != null) {
                mPostTextTv.setText(model.getPostText());
            }

            mPostTextTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, model.getPostTextSize());

            if (model.isPostTextBold() & !model.isPostTextItalic()) {
                mPostTextTv.setTypeface(null, Typeface.BOLD);
            }
            if (!model.isPostTextBold() & model.isPostTextItalic()) {
                mPostTextTv.setTypeface(null, Typeface.ITALIC);
            }
            if (model.isPostTextBold() & model.isPostTextItalic()) {
                mPostTextTv.setTypeface(null, Typeface.BOLD_ITALIC);
            }
        }
    }

    public class ImageViewHolder extends BaseViewHolder {

        @BindView(R.id.iv_post_detail_item_image)
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

        @OnClick(R.id.iv_post_detail_item_image)
        void onImageClick() {
            if (mCallback != null)
                mCallback.onImageClick(mPostSection);
        }
    }

    public class MapViewHolder extends BaseViewHolder {

        @BindView(R.id.mv_post_detail_item_map)
        com.google.android.gms.maps.MapView mMapView;

        MapViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {
        }

        public void onBind(int position, PostSection model) {
            super.onBind(position, model);

            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(googleMap -> {
                LatLng place = new LatLng(model.getPostPlaceLat(), model.getPostPlaceLng());
                googleMap.getUiSettings().setScrollGesturesEnabled(false);
                googleMap.addMarker(new MarkerOptions()
                        .position(place)
                        .title(model.getPostPlaceName()));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(place)
                        .zoom(15)
                        .build();
                googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            });
        }
    }

    public class VideoViewHolder extends BaseViewHolder {

        YouTubePlayerFragment mYouTubePlayerFragment =
                (YouTubePlayerFragment) ((Activity) itemView.getContext())
                .getFragmentManager().findFragmentById(R.id.fragment_post_detail_item_youtube_video);

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

            mYouTubePlayerFragment.initialize(
                    //TODO:Add key to strings
                    "AIzaSyByraF4EaclX12v43bAKldVHZfMjazz9y8",
                    new YouTubePlayer.OnInitializedListener() {
                        @Override
                        public void onInitializationSuccess(
                                YouTubePlayer.Provider provider,
                                final YouTubePlayer youTubePlayer, boolean b) {

                            youTubePlayer.cueVideo(mPostSection.getYouTubeVideoCode());
                            youTubePlayer.setShowFullscreenButton(false);
                        }

                        @Override
                        public void onInitializationFailure(
                                YouTubePlayer.Provider provider,
                                YouTubeInitializationResult youTubeInitializationResult) {
                        }
                    });

        }
    }
}
