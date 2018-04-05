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

package com.bmd.android.europewelcome.utils;

/**
 * Constants used through application
 */

public final class AppConstants {

    public static final String STATUS_CODE_SUCCESS = "success";
    public static final String STATUS_CODE_FAILED = "failed";

    public static final int API_STATUS_CODE_LOCAL_ERROR = 0;

    public static final String DB_NAME = "europe_welcome.db";
    public static final String PREF_NAME = "europe_welcome_pref";

    public static final long NULL_INDEX = -1L;

    public static final String SEED_DATABASE_OPTIONS = "seed/options.json";
    public static final String SEED_DATABASE_QUESTIONS = "seed/questions.json";

    public static final String CLIENT_ID_TOKEN = "300998502025-u88i7rcl7dbf2jlmjj22d00fk48fgp7n.apps.googleusercontent.com";

    public static final String TIMESTAMP_FORMAT = "yyyyMMdd_HHmmss";
    public static final String DATE_FORMAT = "d MMM yyyy";

    public static final String POSTS_COLLECTION = "posts";
    public static final String BOOKMARKS_COLLECTION = "bookmarks";
    public static final String STARS_COLLECTION = "stars";
    public static final String COMMENT_COLLECTION = "postComment";
    public static final String POST_SECTION_COLLECTION = "postSection";
    public static final String USERS_COLLECTION = "users";
    public static final String RATINGS_COLLECTION = "ratings";


    private AppConstants() {
        // This utility class is not publicly instantiable
    }
}
