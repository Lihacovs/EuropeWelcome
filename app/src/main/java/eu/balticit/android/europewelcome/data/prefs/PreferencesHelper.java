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

import eu.balticit.android.europewelcome.data.DataManager;

/**
 * Interface decouples any specific implementation of the {@link AppPreferencesHelper}
 * and hence makes it as plug and play unit
 */
public interface PreferencesHelper {

    int getCurrentUserLoggedInMode();

    void setCurrentUserLoggedInMode(DataManager.LoggedInMode mode);

    String getCurrentUserId();

    void setCurrentUserId(String userId);

    String getCurrentUserName();

    void setCurrentUserName(String userName);

    String getCurrentUserEmail();

    void setCurrentUserEmail(String email);

    String getCurrentUserBirthDate();

    void setCurrentUserBirthDate(String birthDate);

    String getCurrentUserGender();

    void setCurrentUserGender(String gender);

    boolean getCurrentUserPremium();

    void setCurrentUserPremium(boolean hasPremium);

    String getCurrentUserProfilePicUrl();

    void setCurrentUserProfilePicUrl(String profilePicUrl);

    String getAccessToken();

    void setAccessToken(String accessToken);

    String getLastUsedEmail();

    void setLastUsedEmail(String email);

    boolean isAppIntroWatched();

    void watchAppIntro(boolean watched);

}
