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

package com.bmd.android.europewelcome.ui.postdetail;

import com.bmd.android.europewelcome.data.firebase.model.PostComment;
import com.bmd.android.europewelcome.ui.base.MvpPresenter;
import com.bmd.android.europewelcome.ui.base.MvpView;
import com.google.firebase.firestore.Query;

/**
 * Created by Konstantins on 12/7/2017.
 */

public interface PostDetailMvpPresenter<V extends MvpView> extends MvpPresenter<V> {

    void setPostId(String postId);

    void getPost(String postId);

    void getPostTextList(String postId);

    void getPostImageList(String postId);

    void getPostPlaceList(String postId);

    void getPostCommentList(String postId);

    Query getPostCommentsQuery();

    Query getPostSectionQuery();

    void attachContentToLayout();

    void saveComment(String postId, PostComment postComment);
}
