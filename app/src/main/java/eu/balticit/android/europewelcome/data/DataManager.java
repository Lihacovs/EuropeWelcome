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

package eu.balticit.android.europewelcome.data;

import eu.balticit.android.europewelcome.data.firebase.FirebaseHelper;
import eu.balticit.android.europewelcome.data.network.NetworkHelper;
import eu.balticit.android.europewelcome.data.prefs.PreferencesHelper;

/**
 * Interface that is implemented by the {@link AppDataManager}.
 * Contains methods, exposed for all the data handling operations.
 */
public interface DataManager extends FirebaseHelper, PreferencesHelper, NetworkHelper {

    void setUserAsLoggedOut();

    void updateUserInfo(
            String accessToken,
            String userId,
            LoggedInMode loggedInMode,
            String userName,
            String email,
            String profilePicPath,
            String userBirthDate,
            String userGender);

    enum LoggedInMode {

        LOGGED_IN_MODE_LOGGED_OUT(0),
        LOGGED_IN_MODE_GOOGLE(1),
        LOGGED_IN_MODE_FB(2),
        LOGGED_IN_MODE_SERVER(3);

        private final int mType;

        LoggedInMode(int type) {
            mType = type;
        }

        public int getType() {
            return mType;
        }
    }
}
