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

package eu.balticit.android.europewelcome.ui.posts.freeposts;

import eu.balticit.android.europewelcome.data.firebase.model.Post;
import eu.balticit.android.europewelcome.ui.base.MvpPresenter;
import com.google.firebase.firestore.Query;

/**
 * Presenter interface for {@link FreePostsPresenter}
 */
public interface FreePostsMvpPresenter<V extends FreePostsMvpView>
        extends MvpPresenter<V> {

    void onViewPrepared();

    Query getPostsQuery();

    void checkUser();

    Query getPostsQueryOrderedByStars();

    Query getPostsQueryOrderedByViews();

    Query getPostsQueryOrderedByDate();

    Query getPostsQueryOrderedByComments();

    Query getNotAcceptedPostsQuery();

    void updatePost(Post post);

    void addOrRemoveStar(Post post, FreePostsAdapter.ViewHolder holder);

    void saveOrDeleteBookmark(Post post, FreePostsAdapter.ViewHolder holder);

    void checkPostBookmarkedByUser(Post post, FreePostsAdapter.ViewHolder holder);

    void checkPostStarRatedByUser(Post post, FreePostsAdapter.ViewHolder holder);

    void acceptPost(Post post);

    void deletePost(Post post);
}
