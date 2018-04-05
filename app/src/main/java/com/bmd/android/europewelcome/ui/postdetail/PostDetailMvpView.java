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

package com.bmd.android.europewelcome.ui.postdetail;

import com.bmd.android.europewelcome.ui.base.MvpView;

/**
 * View Interface for {@link PostDetailActivity}
 */
public interface PostDetailMvpView extends MvpView {

    void clearCommentInput();

    void setPostUserImage(String userImageUrl);

    void setPostUserName(String userName);

    void setPostCreationDate(String creationDate);

    void setPostNewCommentUserImage(String userImageUrl);

    void setBookmarkedIcon();

    void setNotBookmarkedIcon();

    void setStarRatedIcon();

    void setNotStarRatedIcon();

    void setPostStars(String starsCount);

    void setPostComments(String commentsCount);
}
