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

package com.bmd.android.europewelcome.data.firebase.model;

import java.util.UUID;

/**
 * POJO class to contain post's text, pictures, maps, videos
 */

public class PostSection {
    private String mPostSectionId;
    private String mPostSectionViewType;
    private String mPostSectionCreationDate;
    private String mPostSectionTimestamp;
    private int mLayoutOrderNum;

    //Text fields
    private String mPostText;
    private float mPostTextSize;
    private boolean mPostTextBold;
    private boolean mPostTextItalic;

    //Image fields
    private String mPostImageUrl;
    private String mPostImageCaption;

    //Map fields
    private String mPostPlaceAddress;
    private String mPostPlaceName;
    private double mPostPlaceLat;
    private double mPostPlaceLng;

    //Empty constructor required for Firebase queries
    public PostSection() {
    }

    public PostSection(
            String postSectionViewType,
            String postSectionCreationDate,
            String postSectionTimestamp,
            int layoutOrderNum,
            String postText,
            float postTextSize,
            boolean postTextBold,
            boolean postTextItalic,
            String postImageUrl,
            String postImageCaption,
            String postPlaceAddress,
            String postPlaceName,
            double postPlaceLat,
            double postPlaceLng) {
        mPostSectionId = UUID.randomUUID().toString();
        mPostSectionViewType = postSectionViewType;
        mPostSectionCreationDate = postSectionCreationDate;
        mPostSectionTimestamp = postSectionTimestamp;
        mLayoutOrderNum = layoutOrderNum;
        mPostText = postText;
        mPostTextSize = postTextSize;
        mPostTextBold = postTextBold;
        mPostTextItalic = postTextItalic;
        mPostImageUrl = postImageUrl;
        mPostImageCaption = postImageCaption;
        mPostPlaceAddress = postPlaceAddress;
        mPostPlaceName = postPlaceName;
        mPostPlaceLat = postPlaceLat;
        mPostPlaceLng = postPlaceLng;
    }

    public String getPostSectionId() {
        return mPostSectionId;
    }

    public void setPostSectionId(String postSectionId) {
        mPostSectionId = postSectionId;
    }

    public String getPostSectionViewType() {
        return mPostSectionViewType;
    }

    public void setPostSectionViewType(String postSectionViewType) {
        mPostSectionViewType = postSectionViewType;
    }

    public String getPostSectionCreationDate() {
        return mPostSectionCreationDate;
    }

    public void setPostSectionCreationDate(String postSectionCreationDate) {
        mPostSectionCreationDate = postSectionCreationDate;
    }

    public String getPostSectionTimestamp() {
        return mPostSectionTimestamp;
    }

    public void setPostSectionTimestamp(String postSectionTimestamp) {
        mPostSectionTimestamp = postSectionTimestamp;
    }

    public int getLayoutOrderNum() {
        return mLayoutOrderNum;
    }

    public void setLayoutOrderNum(int layoutOrderNum) {
        mLayoutOrderNum = layoutOrderNum;
    }

    public String getPostText() {
        return mPostText;
    }

    public void setPostText(String postText) {
        mPostText = postText;
    }

    public float getPostTextSize() {
        return mPostTextSize;
    }

    public void setPostTextSize(float postTextSize) {
        mPostTextSize = postTextSize;
    }

    public boolean isPostTextBold() {
        return mPostTextBold;
    }

    public void setPostTextBold(boolean postTextBold) {
        mPostTextBold = postTextBold;
    }

    public boolean isPostTextItalic() {
        return mPostTextItalic;
    }

    public void setPostTextItalic(boolean postTextItalic) {
        mPostTextItalic = postTextItalic;
    }

    public String getPostImageUrl() {
        return mPostImageUrl;
    }

    public void setPostImageUrl(String postImageUrl) {
        mPostImageUrl = postImageUrl;
    }

    public String getPostImageCaption() {
        return mPostImageCaption;
    }

    public void setPostImageCaption(String postImageCaption) {
        mPostImageCaption = postImageCaption;
    }

    public String getPostPlaceAddress() {
        return mPostPlaceAddress;
    }

    public void setPostPlaceAddress(String postPlaceAddress) {
        mPostPlaceAddress = postPlaceAddress;
    }

    public String getPostPlaceName() {
        return mPostPlaceName;
    }

    public void setPostPlaceName(String postPlaceName) {
        mPostPlaceName = postPlaceName;
    }

    public double getPostPlaceLat() {
        return mPostPlaceLat;
    }

    public void setPostPlaceLat(double postPlaceLat) {
        mPostPlaceLat = postPlaceLat;
    }

    public double getPostPlaceLng() {
        return mPostPlaceLng;
    }

    public void setPostPlaceLng(double postPlaceLng) {
        mPostPlaceLng = postPlaceLng;
    }
}
