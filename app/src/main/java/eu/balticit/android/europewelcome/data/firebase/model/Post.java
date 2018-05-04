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

package eu.balticit.android.europewelcome.data.firebase.model;

import java.util.UUID;

/**
 * POJO for Post
 */

public class Post {

    public String mPostId;
    public String mPostAuthorId;
    public String mPostAuthorName;
    public String mPostAuthorImageUrl;
    public String mPostTitle;
    public String mPostText;
    public int mPostStars;
    public int mPostWatches;
    public int mPostComments;
    public int mPostCreationTimestamp;
    public String mPostImageUrl;
    public String mPostCreationDate;
    public boolean mPostAsDraft;
    public boolean mPostPublished;
    public boolean mPostAccepted;
    public boolean mPostPremium;


    public Post() {
    }

    public Post(String postAuthorId,
                String postAuthorName,
                String postAuthorImageUrl,
                String postTitle,
                String postText,
                int postStars,
                int postWatches,
                int postComments,
                int postCreationTimestamp,
                String postImageUrl,
                String postCreationDate,
                boolean postAsDraft,
                boolean postPublished,
                boolean postAccepted,
                boolean postPremium) {
        mPostId = UUID.randomUUID().toString();
        mPostAuthorId = postAuthorId;
        mPostAuthorName = postAuthorName;
        mPostAuthorImageUrl = postAuthorImageUrl;
        mPostTitle = postTitle;
        mPostText = postText;
        mPostStars = postStars;
        mPostWatches = postWatches;
        mPostComments = postComments;
        mPostCreationTimestamp = postCreationTimestamp;
        mPostImageUrl = postImageUrl;
        mPostCreationDate = postCreationDate;
        mPostAsDraft = postAsDraft;
        mPostPublished = postPublished;
        mPostAccepted = postAccepted;
        mPostPremium = postPremium;
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

    public int getPostStars() {
        return mPostStars;
    }

    public void setPostStars(int postStars) {
        mPostStars = postStars;
    }

    public int getPostWatches() {
        return mPostWatches;
    }

    public void setPostWatches(int postWatches) {
        mPostWatches = postWatches;
    }

    public int getPostCreationTimestamp() {
        return mPostCreationTimestamp;
    }

    public int getPostComments() {
        return mPostComments;
    }

    public void setPostComments(int postComments) {
        mPostComments = postComments;
    }

    public void setPostCreationTimestamp(int postCreationTimestamp) {
        mPostCreationTimestamp = postCreationTimestamp;
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

    public boolean isPostAsDraft() {
        return mPostAsDraft;
    }

    public void setPostAsDraft(boolean postAsDraft) {
        mPostAsDraft = postAsDraft;
    }

    public boolean isPostPublished() {
        return mPostPublished;
    }

    public void setPostPublished(boolean postPublished) {
        mPostPublished = postPublished;
    }

    public boolean isPostAccepted() {
        return mPostAccepted;
    }

    public void setPostAccepted(boolean postAccepted) {
        mPostAccepted = postAccepted;
    }

    public boolean isPostPremium() {
        return mPostPremium;
    }

    public void setPostPremium(boolean postPremium) {
        mPostPremium = postPremium;
    }
}


