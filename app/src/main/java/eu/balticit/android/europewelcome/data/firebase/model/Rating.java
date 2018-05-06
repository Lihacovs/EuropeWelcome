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
 * POJO for Rating
 */
@SuppressWarnings("ALL")
public class Rating {
    private String mRatingId;
    private String mUserId;
    private String mUserName;
    private String mUserEmail;
    private float mRatingStars;
    private String mRatingMessage;
    private String mRatingCreationDate;
    private int mRatingCreationTimestamp;

    public Rating() {
    }

    public Rating(String userId,
                  String userName,
                  String userEmail,
                  float ratingStars,
                  String ratingMessage,
                  String ratingCreationDate,
                  int ratingCreationTimestamp) {
        mRatingId = UUID.randomUUID().toString();
        mUserId = userId;
        mUserName = userName;
        mUserEmail = userEmail;
        mRatingStars = ratingStars;
        mRatingMessage = ratingMessage;
        mRatingCreationDate = ratingCreationDate;
        mRatingCreationTimestamp = ratingCreationTimestamp;
    }

    public String getRatingId() {
        return mRatingId;
    }

    public void setRatingId(String ratingId) {
        mRatingId = ratingId;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public String getUserEmail() {
        return mUserEmail;
    }

    public void setUserEmail(String userEmail) {
        mUserEmail = userEmail;
    }

    public float getRatingStars() {
        return mRatingStars;
    }

    public void setRatingStars(float ratingStars) {
        mRatingStars = ratingStars;
    }

    public String getRatingMessage() {
        return mRatingMessage;
    }

    public void setRatingMessage(String ratingMessage) {
        mRatingMessage = ratingMessage;
    }

    public String getRatingCreationDate() {
        return mRatingCreationDate;
    }

    public void setRatingCreationDate(String ratingCreationDate) {
        mRatingCreationDate = ratingCreationDate;
    }

    public int getRatingCreationTimestamp() {
        return mRatingCreationTimestamp;
    }

    public void setRatingCreationTimestamp(int ratingCreationTimestamp) {
        mRatingCreationTimestamp = ratingCreationTimestamp;
    }
}
