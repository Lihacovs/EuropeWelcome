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

import com.bmd.android.europewelcome.data.DataManager;
import com.bmd.android.europewelcome.ui.base.BasePresenter;
import com.bmd.android.europewelcome.ui.base.MvpView;

import javax.inject.Inject;

/**
 * Created by Konstantins on 12/7/2017.
 */

public class AddPostPresenter<V extends MvpView> extends BasePresenter<V> implements
        AddPostMvpPresenter<V> {

    private static final String TAG = "AddPostPresenter";

    @Inject
    public AddPostPresenter(DataManager dataManager) {
        super(dataManager);
    }
}

