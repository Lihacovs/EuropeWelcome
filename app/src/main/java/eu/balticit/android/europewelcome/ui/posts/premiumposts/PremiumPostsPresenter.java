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

import com.google.firebase.firestore.Query;

import eu.balticit.android.europewelcome.R;
import eu.balticit.android.europewelcome.data.DataManager;
import eu.balticit.android.europewelcome.data.firebase.model.Post;
import eu.balticit.android.europewelcome.ui.base.BasePresenter;

import javax.inject.Inject;

/**
 * Created by BIT on 12/6/2017.
 */

public class PremiumPostsPresenter<V extends PremiumPostsMvpView> extends BasePresenter<V>
        implements PremiumPostsMvpPresenter<V> {

    @Inject
    PremiumPostsPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void onViewPrepared() {

    }

    @Override
    public Query getPostsQueryOrderedByStars() {
        return getDataManager().getPremiumPostsQueryOrderedByStars();
    }

    @Override
    public Query getPostsQueryOrderedByViews() {
        return null;
    }

    @Override
    public Query getPostsQueryOrderedByDate() {
        return getDataManager().getPremiumPostsQueryOrderedByDate();
    }

    @Override
    public Query getPostsQueryOrderedByComments() {
        return getDataManager().getPremiumPostsQueryOrderedByComments();
    }

    @Override
    public void updatePost(Post post) {
        getDataManager().updatePost(post)
                .addOnFailureListener(e -> getMvpView().onError(R.string.free_posts_some_error));
    }

    /**
     * Checks if user rated post with star and updates UI accordingly. Launched from
     * {@link PremiumPostsAdapter.ViewHolder#onStarIconClick()}
     *
     * @param post   - Post to check for star
     * @param holder - ViewHolder to update UI
     */
    @Override
    public void addOrRemoveStar(Post post, PremiumPostsAdapter.ViewHolder holder) {
        String currentUserId = getDataManager().getCurrentUserId();
        if (currentUserId != null) {
            getDataManager().getStar(currentUserId, post.getPostId())
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            int newStarCount = post.getPostStars() - 1;
                            post.setPostStars(newStarCount);
                            updatePost(post);
                            //getMvpView().removeStarRatedIcon(holder);
                            getDataManager().deleteStar(currentUserId, post)
                                    .addOnSuccessListener(aVoid -> {

                                    });
                        } else {
                            int newStarCount = post.getPostStars() + 1;
                            post.setPostStars(newStarCount);
                            updatePost(post);
                            //getMvpView().setStarRatedIcon(holder);
                            getDataManager().saveStar(currentUserId, post)
                                    .addOnSuccessListener(aVoid -> {

                                    });
                        }
                    })
                    .addOnFailureListener(e -> getMvpView().onError(R.string.free_posts_some_error));
        } else {
            getMvpView().onError(R.string.free_posts_login_to_rate);
        }
    }

    /**
     * Checks if user rated post with star and updates UI accordingly. Launched from
     * {@link PremiumPostsAdapter.ViewHolder#onStarIconClick()}
     *
     * @param post   - Post to check for star
     * @param holder - ViewHolder to update UI
     */
    @Override
    public void saveOrDeleteBookmark(Post post, PremiumPostsAdapter.ViewHolder holder) {
        String currentUserId = getDataManager().getCurrentUserId();
        if (currentUserId != null) {
            getDataManager().getBookmark(currentUserId, post.getPostId())
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            getMvpView().removeBookmarkedIcon(holder);
                            getDataManager().deleteBookmark(currentUserId, post)
                                    .addOnSuccessListener(aVoid ->
                                            getMvpView().onError(R.string.free_posts_bookmark_removed));
                        } else {
                            getMvpView().setBookmarkedIcon(holder);
                            getDataManager().saveBookmark(currentUserId, post)
                                    .addOnSuccessListener(aVoid ->
                                            getMvpView().onError(R.string.free_posts_bookmark_saved));
                        }
                    })
                    .addOnFailureListener(e -> getMvpView().onError(R.string.free_posts_some_error));
        } else {
            getMvpView().onError(R.string.free_posts_login_to_bookmark);
        }
    }

    /**
     * Checks if post bookmarked by current user and updates UI accordingly. Launched from
     * {@link PremiumPostsAdapter.ViewHolder#bind(Post)}
     *
     * @param post   Post to check
     * @param holder ViewHolder to update UI
     */
    @Override
    public void checkPostBookmarkedByUser(Post post, PremiumPostsAdapter.ViewHolder holder) {
        String currentUserId = getDataManager().getCurrentUserId();
        if (currentUserId != null) {
            getDataManager().getBookmark(currentUserId, post.getPostId())
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            getMvpView().setBookmarkedIcon(holder);
                        } else {
                            getMvpView().removeBookmarkedIcon(holder);
                        }
                    })
                    .addOnFailureListener(e -> getMvpView().onError(R.string.free_posts_some_error));
        }
    }

    /**
     * Checks if post star rated by current user and updates UI accordingly. Launched from
     * {@link PremiumPostsAdapter.ViewHolder#bind(Post)}
     *
     * @param post   Post to check
     * @param holder ViewHolder to update UI
     */
    @Override
    public void checkPostStarRatedByUser(Post post, PremiumPostsAdapter.ViewHolder holder) {
        String currentUserId = getDataManager().getCurrentUserId();
        if (currentUserId != null) {
            getDataManager().getStar(currentUserId, post.getPostId())
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            getMvpView().setStarRatedIcon(holder);
                        } else {
                            getMvpView().removeStarRatedIcon(holder);
                        }
                    })
                    .addOnFailureListener(e -> getMvpView().onError(R.string.free_posts_some_error));
        }
    }
}
