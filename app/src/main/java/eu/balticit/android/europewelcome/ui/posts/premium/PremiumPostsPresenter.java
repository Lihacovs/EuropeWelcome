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

package eu.balticit.android.europewelcome.ui.posts.premium;

import eu.balticit.android.europewelcome.data.DataManager;
import eu.balticit.android.europewelcome.ui.base.BasePresenter;

import javax.inject.Inject;

import eu.balticit.android.europewelcome.data.DataManager;

/**
 * Created by BIT on 12/6/2017.
 */

public class PremiumPostsPresenter <V extends PremiumPostsMvpView> extends BasePresenter<V>
        implements PremiumPostsMvpPresenter<V> {

    @Inject
    public PremiumPostsPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void onViewPrepared() {

    }
}
