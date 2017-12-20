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
 * POJO for Post's texts
 */

public class PostText {
    private String mPostTextId;
    private String mPostText;
    private float mPostTextSize;
    private boolean mPostTextBold;
    private boolean mPostTextItalic;
    private String mPostCreationDate;
    private int mLayoutOrderNum;

    public PostText() {
    }

    public PostText(String postText, float postTextSize, boolean postTextBold, boolean postTextItalic, String postCreationDate, int layoutOrderNum) {
        mPostTextId = UUID.randomUUID().toString();
        mPostText = postText;
        mPostTextSize = postTextSize;
        mPostTextBold = postTextBold;
        mPostTextItalic = postTextItalic;
        mPostCreationDate = postCreationDate;
        mLayoutOrderNum = layoutOrderNum;
    }

    public String getPostTextId() {
        return mPostTextId;
    }

    public void setPostTextId(String postTextId) {
        mPostTextId = postTextId;
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

    public String getPostCreationDate() {
        return mPostCreationDate;
    }

    public void setPostCreationDate(String postCreationDate) {
        mPostCreationDate = postCreationDate;
    }

    public int getLayoutOrderNum() {
        return mLayoutOrderNum;
    }

    public void setLayoutOrderNum(int layoutOrderNum) {
        mLayoutOrderNum = layoutOrderNum;
    }
}