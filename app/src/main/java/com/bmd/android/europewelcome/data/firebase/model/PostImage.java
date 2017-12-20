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
 * POJO for Post's images
 */

public class PostImage {
    private String mPostImageId;
    private String mPostImageUrl;
    private String mPostImageCaption;
    private String mPostImageCreationDate;
    private int mLayoutOrderNum;

    public PostImage() {
    }

    public PostImage(String postImageUrl, String postImageCaption, String postImageCreationDate, int layoutOrderNum) {
        mPostImageId = UUID.randomUUID().toString();
        mPostImageUrl = postImageUrl;
        mPostImageCaption = postImageCaption;
        mPostImageCreationDate = postImageCreationDate;
        mLayoutOrderNum = layoutOrderNum;
    }

    public String getPostImageId() {
        return mPostImageId;
    }

    public void setPostImageId(String postImageId) {
        mPostImageId = postImageId;
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

    public String getPostImageCreationDate() {
        return mPostImageCreationDate;
    }

    public void setPostImageCreationDate(String postImageCreationDate) {
        mPostImageCreationDate = postImageCreationDate;
    }

    public int getLayoutOrderNum() {
        return mLayoutOrderNum;
    }

    public void setLayoutOrderNum(int layoutOrderNum) {
        mLayoutOrderNum = layoutOrderNum;
    }
}
