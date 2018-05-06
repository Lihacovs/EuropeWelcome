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

/**
 * POJO for User
 */
@SuppressWarnings("ALL")
public class User {
    private String mUserId;
    private String mUserEmail;
    private String mUserPassword;
    private String mUserName;
    private String mUserPhotoUrl;
    private String mUserGender;
    private String mUserBirthDate;
    private String mUserCreationDate;
    private boolean mUserPremium;
    private boolean mUserAdmin;

    public User() {
    }

    public User(String userId,
                String userEmail,
                String userPassword,
                String userName,
                String userPhotoUrl,
                String userGender,
                String userBirthDate,
                String userCreationDate,
                boolean userPremium,
                boolean userAdmin) {
        mUserId = userId;
        mUserEmail = userEmail;
        mUserPassword = userPassword;
        mUserName = userName;
        mUserPhotoUrl = userPhotoUrl;
        mUserGender = userGender;
        mUserBirthDate = userBirthDate;
        mUserCreationDate = userCreationDate;
        mUserPremium = userPremium;
        mUserAdmin = userAdmin;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

    public String getUserEmail() {
        return mUserEmail;
    }

    public void setUserEmail(String userEmail) {
        mUserEmail = userEmail;
    }

    public String getUserPassword() {
        return mUserPassword;
    }

    public void setUserPassword(String userPassword) {
        mUserPassword = userPassword;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public String getUserPhotoUrl() {
        return mUserPhotoUrl;
    }

    public void setUserPhotoUrl(String userPhotoUrl) {
        mUserPhotoUrl = userPhotoUrl;
    }

    public String getUserGender() {
        return mUserGender;
    }

    public void setUserGender(String userGender) {
        mUserGender = userGender;
    }

    public String getUserBirthDate() {
        return mUserBirthDate;
    }

    public void setUserBirthDate(String userBirthDate) {
        mUserBirthDate = userBirthDate;
    }

    public String getUserCreationDate() {
        return mUserCreationDate;
    }

    public void setUserCreationDate(String userCreationDate) {
        mUserCreationDate = userCreationDate;
    }

    public boolean isUserPremium() {
        return mUserPremium;
    }

    public void setUserPremium(boolean userPremium) {
        mUserPremium = userPremium;
    }

    public boolean isUserAdmin() {
        return mUserAdmin;
    }

    public void setUserAdmin(boolean userAdmin) {
        mUserAdmin = userAdmin;
    }
}
