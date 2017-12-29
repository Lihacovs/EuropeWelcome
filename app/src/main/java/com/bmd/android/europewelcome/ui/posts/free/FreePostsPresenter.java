/*
 * Copyright (C) 2017. Baltic Mobile Development
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

package com.bmd.android.europewelcome.ui.posts.free;

import android.support.annotation.NonNull;

import com.bmd.android.europewelcome.data.DataManager;
import com.bmd.android.europewelcome.data.firebase.model.Post;
import com.bmd.android.europewelcome.ui.base.BasePresenter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;

import javax.inject.Inject;

/**
 * Created by Konstantins on 12/6/2017.
 */

public class FreePostsPresenter<V extends FreePostsMvpView> extends BasePresenter<V>
        implements FreePostsMvpPresenter<V> {

    @Inject
    public FreePostsPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void onViewPrepared() {

    }

    @Override
    public Query getPostsQuery() {
        return  getDataManager().getPostsColRef();
    }

    @Override
    public Query getPostsQueryOrderedByStars() {
        return getDataManager().getPostsQueryOrderedByStars();
    }

    @Override
    public Query getPostsQueryOrderedByViews() {
        return getDataManager().getPostsQueryOrderedByViews();
    }

    @Override
    public Query getPostsQueryOrderedByDate() {
        return getDataManager().getPostsQueryOrderedByDate();
    }

    @Override
    public void savePost(Post post) {
        getDataManager().savePost(post).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                getMvpView().showMessage("postSaved");
            }
        });
    }

    @Override
    public void updatePost(Post post) {
        getDataManager().updatePost(post).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                getMvpView().onError("Error accured: " + e.getMessage());
            }
        });
    }
}