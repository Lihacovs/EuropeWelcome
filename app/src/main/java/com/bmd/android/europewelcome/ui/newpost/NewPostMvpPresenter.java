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

package com.bmd.android.europewelcome.ui.newpost;

import android.content.Context;
import android.net.Uri;

import com.bmd.android.europewelcome.data.firebase.model.Post;
import com.bmd.android.europewelcome.data.firebase.model.PostSection;
import com.bmd.android.europewelcome.ui.base.MvpPresenter;
import com.google.android.gms.location.places.Place;
import com.google.firebase.firestore.Query;

/**
 * Presenter interface for {@link NewPostPresenter}
 */

public interface NewPostMvpPresenter<V extends NewPostMvpView> extends MvpPresenter<V> {

    void setPost(String postId);

    Query getPostSectionQuery();

    void newTextPostSection();

    void newMapPostSection(Place place);

    void newVideoPostSection(String videoCode);

    void deletePostSection(PostSection postSection);

    void updatePostSection(PostSection postSection);

    void uploadImageToStorage(Uri uri, Context context);

    void savePostToDraft();

    void deletePost();

    void publishPost();

}
