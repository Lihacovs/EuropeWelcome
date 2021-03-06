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

package eu.balticit.android.europewelcome.ui.bookmarks;


import eu.balticit.android.europewelcome.R;
import eu.balticit.android.europewelcome.data.DataManager;
import eu.balticit.android.europewelcome.data.firebase.model.Post;
import eu.balticit.android.europewelcome.ui.base.BasePresenter;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import javax.inject.Inject;

/**
 * Bookmarks Presenter
 */
public class BookmarksPresenter<V extends BookmarksMvpView> extends BasePresenter<V> implements
        BookmarksMvpPresenter<V> {

    @SuppressWarnings("unused")
    private static final String TAG = "DraftsPresenter";

    @Inject
    BookmarksPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public Query getBookmarkedPostsQuery() {
        return getDataManager().getBookmarkedPostsQuery(getDataManager().getCurrentUserId());
    }

    @Override
    public void deleteBookmark(Post post) {
        getDataManager().deleteBookmark(getDataManager().getCurrentUserId(), post)
                .addOnSuccessListener(aVoid -> {
                    getMvpView().hideLoading();
                    getMvpView().onError(R.string.bookmarks_removed);
                })
                .addOnFailureListener(e -> {
                    getMvpView().hideLoading();
                    getMvpView().onError(R.string.bookmarks_some_error);
                });
    }

    @Override
    public void updatePost(Post post) {
        getDataManager().updatePost(post)
                .addOnFailureListener(e -> getMvpView().onError(R.string.bookmarks_some_error));
    }

    @Override
    public void deleteAllBookmarks() {
        getDataManager().getUserBookmarks(getDataManager().getCurrentUserId())
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                        Post post = doc.toObject(Post.class);
                        getDataManager().deleteBookmark(getDataManager().getCurrentUserId(), post);
                    }
                    getMvpView().hideLoading();
                })
                .addOnFailureListener(e -> {
                    getMvpView().hideLoading();
                    getMvpView().onError(R.string.bookmarks_some_error);
                });
    }
}
