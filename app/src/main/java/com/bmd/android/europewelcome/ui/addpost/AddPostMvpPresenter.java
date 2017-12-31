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

package com.bmd.android.europewelcome.ui.addpost;

import android.net.Uri;

import com.bmd.android.europewelcome.data.firebase.model.Post;
import com.bmd.android.europewelcome.data.firebase.model.PostImage;
import com.bmd.android.europewelcome.data.firebase.model.PostPlace;
import com.bmd.android.europewelcome.data.firebase.model.PostText;
import com.bmd.android.europewelcome.ui.base.MvpPresenter;

/**
 * Created by Konstantins on 12/7/2017.
 */

public interface AddPostMvpPresenter<V extends AddPostMvpView> extends MvpPresenter<V> {

    void addPostTextToList(PostText postText);

    void removePostTextFromList(PostText postText);

    void updatePostTextInList(PostText postText);

    void addPostImageToList(PostImage postImage);

    void removePostImageFromList(PostImage postImage);

    void updatePostImageInList(PostImage postImage);

    void addPostPlaceToList(PostPlace postPlace);

    void removePostPlaceFromList(PostPlace postPlace);

    void setPostTitle(String postTitle);

    void savePost();

    void savePostText(PostText postText, String postId);

    void savePostImage(PostImage postImage, String postId);

    void savePostPlace(PostPlace postPlace, String postId);

    void uploadFileToStorage(Uri uri);

    Post newPost();

    PostText newPostText();

    PostImage newPostImage();

    PostPlace newPostPlace();
}
