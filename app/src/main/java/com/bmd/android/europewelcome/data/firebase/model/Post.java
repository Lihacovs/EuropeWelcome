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
 * POJO for Post
 */

public class Post {

    private String mPostId;
    private String mPostAuthorId;
    private String mPostAuthorName;
    private String mPostAuthorImageUrl;
    private String mPostTitle;
    private String mPostText;
    private String mPostStars;
    private String mPostWatches;
    private String mPostImageUrl;
    private String mPostCreationDate;
    private int mChildLayoutNum;

    public Post() {
    }

    public Post(String postAuthorId,
                String postAuthorName,
                String postAuthorImageUrl,
                String postTitle,
                String postText,
                String postStars,
                String postWatches,
                String postImageUrl,
                String postCreationDate,
                int childLayoutNum) {
        mPostId = UUID.randomUUID().toString();
        mPostAuthorId = postAuthorId;
        mPostAuthorName = postAuthorName;
        mPostAuthorImageUrl = postAuthorImageUrl;
        mPostTitle = postTitle;
        mPostText = postText;
        mPostStars = postStars;
        mPostWatches = postWatches;
        mPostImageUrl = postImageUrl;
        mPostCreationDate = postCreationDate;
        mChildLayoutNum = childLayoutNum;
    }

    public String getPostId() {
        return mPostId;
    }

    public void setPostId(String postId) {
        mPostId = postId;
    }

    public String getPostAuthorId() {
        return mPostAuthorId;
    }

    public void setPostAuthorId(String postAuthorId) {
        mPostAuthorId = postAuthorId;
    }

    public String getPostAuthorName() {
        return mPostAuthorName;
    }

    public void setPostAuthorName(String postAuthorName) {
        mPostAuthorName = postAuthorName;
    }

    public String getPostAuthorImageUrl() {
        return mPostAuthorImageUrl;
    }

    public void setPostAuthorImageUrl(String postAuthorImageUrl) {
        mPostAuthorImageUrl = postAuthorImageUrl;
    }

    public String getPostTitle() {
        return mPostTitle;
    }

    public void setPostTitle(String postTitle) {
        mPostTitle = postTitle;
    }

    public String getPostText() {
        return mPostText;
    }

    public void setPostText(String postText) {
        mPostText = postText;
    }

    public String getPostStars() {
        return mPostStars;
    }

    public void setPostStars(String postStars) {
        mPostStars = postStars;
    }

    public String getPostWatches() {
        return mPostWatches;
    }

    public void setPostWatches(String postWatches) {
        mPostWatches = postWatches;
    }

    public String getPostImageUrl() {
        return mPostImageUrl;
    }

    public void setPostImageUrl(String postImageUrl) {
        mPostImageUrl = postImageUrl;
    }

    public String getPostCreationDate() {
        return mPostCreationDate;
    }

    public void setPostCreationDate(String postCreationDate) {
        mPostCreationDate = postCreationDate;
    }

    public int getChildLayoutNum() {
        return mChildLayoutNum;
    }

    public void setChildLayoutNum(int childLayoutNum) {
        mChildLayoutNum = childLayoutNum;
    }
}


