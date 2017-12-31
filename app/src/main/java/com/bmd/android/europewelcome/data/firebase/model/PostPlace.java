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
 * Created by Konstantins on 12/30/2017.
 */

public class PostPlace {
    private String mPostPlaceId;
    private String mPostPlaceAddress;
    private String mPostPlaceName;
    private double mPostPlaceLat;
    private double mPostPlaceLng;
    private String mPostPlaceCreationDate;
    private int mLayoutOrderNum;

    public PostPlace() {
    }

    public PostPlace(String postPlaceAddress, String postPlaceName, double postPlaceLat, double postPlaceLng, String postPlaceCreationDate, int layoutOrderNum) {
        mPostPlaceId = UUID.randomUUID().toString();
        mPostPlaceAddress = postPlaceAddress;
        mPostPlaceName = postPlaceName;
        mPostPlaceLat = postPlaceLat;
        mPostPlaceLng = postPlaceLng;
        mPostPlaceCreationDate = postPlaceCreationDate;
        mLayoutOrderNum = layoutOrderNum;
    }

    public String getPostPlaceId() {
        return mPostPlaceId;
    }

    public void setPostPlaceId(String postPlaceId) {
        mPostPlaceId = postPlaceId;
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

    public String getPostPlaceCreationDate() {
        return mPostPlaceCreationDate;
    }

    public void setPostPlaceCreationDate(String postPlaceCreationDate) {
        mPostPlaceCreationDate = postPlaceCreationDate;
    }

    public int getLayoutOrderNum() {
        return mLayoutOrderNum;
    }

    public void setLayoutOrderNum(int layoutOrderNum) {
        mLayoutOrderNum = layoutOrderNum;
    }
}
