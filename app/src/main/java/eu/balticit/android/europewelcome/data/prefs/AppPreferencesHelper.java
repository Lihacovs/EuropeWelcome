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

package eu.balticit.android.europewelcome.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

import eu.balticit.android.europewelcome.data.DataManager;
import eu.balticit.android.europewelcome.di.ApplicationContext;
import eu.balticit.android.europewelcome.di.PreferenceInfo;

/**
 * Reads and writes the data from android shared preferences.
 */
@Singleton
public class AppPreferencesHelper implements PreferencesHelper {

    private static final String PREF_KEY_USER_LOGGED_IN_MODE = "PREF_KEY_USER_LOGGED_IN_MODE";
    private static final String PREF_KEY_CURRENT_USER_ID = "PREF_KEY_CURRENT_USER_ID";
    private static final String PREF_KEY_CURRENT_USER_NAME = "PREF_KEY_CURRENT_USER_NAME";
    private static final String PREF_KEY_CURRENT_USER_EMAIL = "PREF_KEY_CURRENT_USER_EMAIL";
    private static final String PREF_KEY_CURRENT_USER_BIRTH_DATE
            = "PREF_KEY_CURRENT_USER_EMAIL_BIRTH_DATE";
    private static final String PREF_KEY_CURRENT_USER_GENDER = "PREF_KEY_CURRENT_USER_EMAIL_GENDER";
    private static final String PREF_KEY_CURRENT_USER_PREMIUM = "PREF_KEY_CURRENT_USER_PREMIUM";
    private static final String PREF_KEY_CURRENT_USER_PROFILE_PIC_URL
            = "PREF_KEY_CURRENT_USER_PROFILE_PIC_URL";
    private static final String PREF_KEY_ACCESS_TOKEN = "PREF_KEY_ACCESS_TOKEN";
    private static final String PREF_KEY_EMAIL_USED_FOR_SERVER = "PREF_EMAIL_USED_FOR_SERVER";
    private static final String PREF_KEY_INTRO_WATCHED = "PREF_KEY_INTRO_WATCHED";

    private final SharedPreferences mPrefs;

    @Inject
    AppPreferencesHelper(@ApplicationContext Context context,
                         @PreferenceInfo String prefFileName) {
        mPrefs = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
    }

    @Override
    public String getCurrentUserId() {
        return mPrefs.getString(PREF_KEY_CURRENT_USER_ID, null);
    }

    @Override
    public void setCurrentUserId(String userId) {
        mPrefs.edit().putString(PREF_KEY_CURRENT_USER_ID, userId).apply();
    }

    @Override
    public String getCurrentUserName() {
        return mPrefs.getString(PREF_KEY_CURRENT_USER_NAME, null);
    }

    @Override
    public void setCurrentUserName(String userName) {
        mPrefs.edit().putString(PREF_KEY_CURRENT_USER_NAME, userName).apply();
    }

    @Override
    public String getCurrentUserEmail() {
        return mPrefs.getString(PREF_KEY_CURRENT_USER_EMAIL, null);
    }

    @Override
    public void setCurrentUserEmail(String email) {
        mPrefs.edit().putString(PREF_KEY_CURRENT_USER_EMAIL, email).apply();
    }

    @Override
    public String getCurrentUserBirthDate() {
        return mPrefs.getString(PREF_KEY_CURRENT_USER_BIRTH_DATE, null);
    }

    @Override
    public void setCurrentUserBirthDate(String birthDate) {
        mPrefs.edit().putString(PREF_KEY_CURRENT_USER_BIRTH_DATE, birthDate).apply();
    }

    @Override
    public String getCurrentUserGender() {
        return mPrefs.getString(PREF_KEY_CURRENT_USER_GENDER, null);
    }

    @Override
    public void setCurrentUserGender(String gender) {
        mPrefs.edit().putString(PREF_KEY_CURRENT_USER_GENDER, gender).apply();
    }

    @Override
    public boolean getCurrentUserPremium() {
        return mPrefs.getBoolean(PREF_KEY_CURRENT_USER_PREMIUM, false);
    }

    @Override
    public void setCurrentUserPremium(boolean hasPremium) {
        mPrefs.edit().putBoolean(PREF_KEY_CURRENT_USER_PREMIUM, hasPremium).apply();
    }

    @Override
    public String getCurrentUserProfilePicUrl() {
        return mPrefs.getString(PREF_KEY_CURRENT_USER_PROFILE_PIC_URL, null);
    }

    @Override
    public void setCurrentUserProfilePicUrl(String profilePicUrl) {
        mPrefs.edit().putString(PREF_KEY_CURRENT_USER_PROFILE_PIC_URL, profilePicUrl).apply();
    }

    @Override
    public int getCurrentUserLoggedInMode() {
        return mPrefs.getInt(PREF_KEY_USER_LOGGED_IN_MODE,
                DataManager.LoggedInMode.LOGGED_IN_MODE_LOGGED_OUT.getType());
    }

    @Override
    public void setCurrentUserLoggedInMode(DataManager.LoggedInMode mode) {
        mPrefs.edit().putInt(PREF_KEY_USER_LOGGED_IN_MODE, mode.getType()).apply();
    }

    @Override
    public String getAccessToken() {
        return mPrefs.getString(PREF_KEY_ACCESS_TOKEN, null);
    }

    @Override
    public void setAccessToken(String accessToken) {
        mPrefs.edit().putString(PREF_KEY_ACCESS_TOKEN, accessToken).apply();
    }

    @Override
    public String getLastUsedEmail() {
        return mPrefs.getString(PREF_KEY_EMAIL_USED_FOR_SERVER, null);
    }

    @Override
    public void setLastUsedEmail(String email) {
        mPrefs.edit().putString(PREF_KEY_EMAIL_USED_FOR_SERVER, email).apply();
    }

    /**
     * Puts true value if AppIntro was watched
     */
    @Override
    public void watchAppIntro(boolean watched) {
        mPrefs.edit().putBoolean(PREF_KEY_INTRO_WATCHED, watched).apply();
    }

    @Override
    public boolean isAppIntroWatched() {
        return mPrefs.getBoolean(PREF_KEY_INTRO_WATCHED, false);
    }
}
