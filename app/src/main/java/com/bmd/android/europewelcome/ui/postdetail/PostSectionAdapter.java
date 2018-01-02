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

import android.graphics.Typeface;
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
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.FirebaseFirestoreException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Konstantins on 1/2/2018.
 */

public class PostSectionAdapter extends FirestoreRecyclerAdapter<PostSection, BaseViewHolder> {
    private static final int VIEW_TYPE_TEXT = 0;
    private static final int VIEW_TYPE_IMAGE = 1;
    private static final int VIEW_TYPE_MAP = 2;
    private static final int VIEW_TYPE_VIDEO = 3;

    private Callback mCallback;
    private FirestoreRecyclerOptions<PostSection> mOptions;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See
     * {@link FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public PostSectionAdapter(FirestoreRecyclerOptions<PostSection> options) {
        super(options);
    }

    public void setAdapterCallback(Callback callback) {
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
                return new TextViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_postdetail_text, parent, false));
            case VIEW_TYPE_IMAGE:
                return new ImageViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_postdetail_image, parent, false));
            case VIEW_TYPE_MAP:
                return new MapViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_postdetail_map, parent, false));
            case VIEW_TYPE_VIDEO:
            default:
                return new TextViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_postdetail_text, parent, false));
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
    }


    public interface Callback {

        //void onStarIconClick(PostComment postComment);
    }

    public class TextViewHolder extends BaseViewHolder {

        @BindView(R.id.tv_postdetailitem_posttext)
        TextView mPostTextTv;

        public TextViewHolder(View itemView) {
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

            if(model.isPostTextBold() & !model.isPostTextItalic()){
                mPostTextTv.setTypeface(null, Typeface.BOLD);
            }
            if(!model.isPostTextBold() & model.isPostTextItalic()){
                mPostTextTv.setTypeface(null, Typeface.ITALIC);
            }
            if(model.isPostTextBold() & model.isPostTextItalic()){
                mPostTextTv.setTypeface(null, Typeface.BOLD_ITALIC);
            }
        }
    }

    public class ImageViewHolder extends BaseViewHolder {

        @BindView(R.id.iv_postdetailitem_image)
        ImageView mPostImageIv;

        @BindView(R.id.iv_postdetail_zoomimage)
        ImageView mZoomIconIv;

        public ImageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {
            mPostImageIv.setImageDrawable(null);
            mZoomIconIv.setImageDrawable(null);
        }

        public void onBind(int position, PostSection model) {
            super.onBind(position, model);

            if (model.getPostImageUrl() != null) {
                GlideApp.with(itemView.getContext())
                        .load(model.getPostImageUrl())
                        .centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(mPostImageIv);
            }
        }
    }

    public class MapViewHolder extends BaseViewHolder {

        @BindView(R.id.mv_postdetailitem_map)
        com.google.android.gms.maps.MapView mMapView;

        public MapViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {
        }

        public void onBind(int position, PostSection model) {
            super.onBind(position, model);

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
    }
}
