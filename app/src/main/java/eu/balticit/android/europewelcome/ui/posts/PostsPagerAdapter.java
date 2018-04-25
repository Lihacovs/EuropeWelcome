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

package eu.balticit.android.europewelcome.ui.posts;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import eu.balticit.android.europewelcome.ui.posts.freeposts.FreePostsFragment;
import eu.balticit.android.europewelcome.ui.posts.premiumposts.PremiumPostsFragment;

/**
 * Created by BIT on 12/6/2017.
 */
public class PostsPagerAdapter extends FragmentStatePagerAdapter {

    private int mTabCount;

    public PostsPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        this.mTabCount = 0;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return FreePostsFragment.newInstance();
            case 1:
                return PremiumPostsFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mTabCount;
    }

    public void setCount(int count) {
        mTabCount = count;
    }
}
