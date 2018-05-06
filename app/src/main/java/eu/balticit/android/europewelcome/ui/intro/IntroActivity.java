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

package eu.balticit.android.europewelcome.ui.intro;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro2;

import eu.balticit.android.europewelcome.MyApp;
import eu.balticit.android.europewelcome.R;
import eu.balticit.android.europewelcome.data.DataManager;
import eu.balticit.android.europewelcome.ui.posts.PostsActivity;

public class IntroActivity extends AppIntro2 {

    DataManager mDataManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        mDataManager = ((MyApp) getApplication()).getDataManager();

        if (mDataManager.isAppIntroWatched()) {
            Intent intent = new Intent(this, PostsActivity.class);
            startActivity(intent);
            finish();
        }

        if (getSupportActionBar() != null) getSupportActionBar().hide();

        // Note here that we DO NOT use setContentView();

        // Add your slide fragments here.
        // AppIntro will automatically generate the dots indicator and buttons.
        addSlide(SlideFragment.newInstance(R.layout.intro_layout_one));
        addSlide(SlideFragment.newInstance(R.layout.intro_layout_two));
        addSlide(SlideFragment.newInstance(R.layout.intro_layout_three));
        addSlide(SlideFragment.newInstance(R.layout.intro_layout_four));
        addSlide(SlideFragment.newInstance(R.layout.intro_layout_five));


        // OPTIONAL METHODS
        // Override bar/separator color.
        //setBarColor(Color.parseColor("#3F51B5"));
        //setSeparatorColor(Color.parseColor("#2196F3"));

        // Hide Skip/Done button.
        showStatusBar(false);
        showSkipButton(false);
        setProgressButtonEnabled(true);
        setZoomAnimation();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Do something when users tap on Done button.

        Intent intent = PostsActivity.getStartIntent(this);
        startActivity(intent);
        mDataManager.watchAppIntro(true);
        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }
}