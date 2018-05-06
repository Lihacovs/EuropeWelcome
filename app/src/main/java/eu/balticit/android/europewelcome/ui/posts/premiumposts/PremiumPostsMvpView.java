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

package eu.balticit.android.europewelcome.ui.posts.premiumposts;

import eu.balticit.android.europewelcome.ui.base.MvpView;

/**
 * Created by BIT on 12/6/2017.
 */
public interface PremiumPostsMvpView extends MvpView {

    void setBookmarkedIcon(PremiumPostsAdapter.ViewHolder holder);

    void removeBookmarkedIcon(PremiumPostsAdapter.ViewHolder holder);

    void setStarRatedIcon(PremiumPostsAdapter.ViewHolder holder);

    void removeStarRatedIcon(PremiumPostsAdapter.ViewHolder holder);
}