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

package com.bmd.android.europewelcome.ui.drafts;

import android.util.Log;

import com.bmd.android.europewelcome.data.DataManager;
import com.bmd.android.europewelcome.data.firebase.model.Post;
import com.bmd.android.europewelcome.data.firebase.model.PostSection;
import com.bmd.android.europewelcome.ui.base.BasePresenter;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import javax.inject.Inject;

public class DraftsPresenter<V extends DraftsMvpView> extends BasePresenter<V> implements
        DraftsMvpPresenter<V> {

    private static final String TAG = "DraftsPresenter";

    @Inject
    DraftsPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public Query getPostAsDraftQuery() {
        return getDataManager().getPostAsDraftQuery(getCurrentUserId());
    }

    @Override
    public String getCurrentUserId() {
        return getDataManager().getCurrentUserId();
    }

    @Override
    public void deleteDraft(Post post) {
        getMvpView().showLoading();
        //First delete PostSection collection with all documents under that post
        getDataManager().getFirstPostSectionCollection(post.getPostId()).addOnSuccessListener(documentSnapshots -> {
            for (DocumentSnapshot doc : documentSnapshots.getDocuments()) {
                PostSection postSection = doc.toObject(PostSection.class);
                getDataManager().deletePostSection(post.getPostId(), postSection);
            }
            //Then delete Post document itself
            getDataManager().deletePost(post).addOnSuccessListener(aVoid -> {
                getMvpView().hideLoading();
            }).addOnFailureListener(e -> {
                getMvpView().hideLoading();
                Log.d(TAG, "deletePost: " + e.getMessage());
            });
        }).addOnFailureListener(e -> {
            getMvpView().hideLoading();
            Log.d(TAG, "onFailure: " + e.getMessage());
        });
    }
}
