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

package eu.balticit.android.europewelcome.utils;

/**
 * Constants used through application
 */

public final class AppConstants {

    public static final String DB_NAME = "europe_welcome.db";
    public static final String PREF_NAME = "europe_welcome_pref";

    public static final String TIMESTAMP_FORMAT = "yyyyMMdd_HHmmss";
    public static final String DATE_FORMAT = "d MMM yyyy";

    /**
     * Constants for data collections in firestore database. used in
     * {@link eu.balticit.android.europewelcome.data.firebase.FirebaseHelper}
     */
    public static final String POSTS_COLLECTION = "posts";
    public static final String BOOKMARKS_COLLECTION = "bookmarks";
    public static final String STARS_COLLECTION = "stars";
    public static final String COMMENT_LIKES_COLLECTION = "commentLikes";
    public static final String POST_COMMENTS_COLLECTION = "postComment";
    public static final String USER_COMMENTS_COLLECTION = "userComment";
    public static final String POST_SECTION_COLLECTION = "postSection";
    public static final String USERS_COLLECTION = "users";
    public static final String RATINGS_COLLECTION = "ratings";


    private AppConstants() {
        // This utility class is not publicly instantiable
    }
}
