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
 * Created by Konstantins on 1/2/2018.
 */

public class PostComment {
    private String mPostCommentId;
    private String mPostCommentPostId;
    private String mPostCommentUserId;
    private String mPostCommentUserImageUrl;
    private String mPostCommentUserName;
    private String mPostCommentCreationDate;
    private String mPostCommentTimestamp;
    private String mPostCommentText;
    private String mPostCommentStars;

    public PostComment() {
    }

    public PostComment(String postCommentPostId,
                       String postCommentUserId,
                       String postCommentUserImageUrl,
                       String postCommentUserName,
                       String postCommentCreationDate,
                       String postCommentTimestamp,
                       String postCommentText,
                       String postCommentStars) {
        mPostCommentId = UUID.randomUUID().toString();
        mPostCommentPostId = postCommentPostId;
        mPostCommentUserId = postCommentUserId;
        mPostCommentUserImageUrl = postCommentUserImageUrl;
        mPostCommentUserName = postCommentUserName;
        mPostCommentCreationDate = postCommentCreationDate;
        mPostCommentTimestamp = postCommentTimestamp;
        mPostCommentText = postCommentText;
        mPostCommentStars = postCommentStars;
    }

    public String getPostCommentId() {
        return mPostCommentId;
    }

    public void setPostCommentId(String postCommentId) {
        mPostCommentId = postCommentId;
    }

    public String getPostCommentPostId() {
        return mPostCommentPostId;
    }

    public void setPostCommentPostId(String postCommentPostId) {
        mPostCommentPostId = postCommentPostId;
    }

    public String getPostCommentUserId() {
        return mPostCommentUserId;
    }

    public void setPostCommentUserId(String postCommentUserId) {
        mPostCommentUserId = postCommentUserId;
    }

    public String getPostCommentUserImageUrl() {
        return mPostCommentUserImageUrl;
    }

    public void setPostCommentUserImageUrl(String postCommentUserImageUrl) {
        mPostCommentUserImageUrl = postCommentUserImageUrl;
    }

    public String getPostCommentUserName() {
        return mPostCommentUserName;
    }

    public void setPostCommentUserName(String postCommentUserName) {
        mPostCommentUserName = postCommentUserName;
    }

    public String getPostCommentCreationDate() {
        return mPostCommentCreationDate;
    }

    public void setPostCommentCreationDate(String postCommentCreationDate) {
        mPostCommentCreationDate = postCommentCreationDate;
    }

    public String getPostCommentTimestamp() {
        return mPostCommentTimestamp;
    }

    public void setPostCommentTimestamp(String postCommentTimestamp) {
        mPostCommentTimestamp = postCommentTimestamp;
    }

    public String getPostCommentText() {
        return mPostCommentText;
    }

    public void setPostCommentText(String postCommentText) {
        mPostCommentText = postCommentText;
    }

    public String getPostCommentStars() {
        return mPostCommentStars;
    }

    public void setPostCommentStars(String postCommentStars) {
        mPostCommentStars = postCommentStars;
    }
}