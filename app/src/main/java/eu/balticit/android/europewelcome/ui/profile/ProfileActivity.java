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

package eu.balticit.android.europewelcome.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import eu.balticit.android.europewelcome.R;
import eu.balticit.android.europewelcome.di.module.GlideApp;
import eu.balticit.android.europewelcome.ui.about.AboutFragment;
import eu.balticit.android.europewelcome.ui.base.BaseActivity;
import eu.balticit.android.europewelcome.ui.profile.changeprofile.ChangeProfileFragment;
import eu.balticit.android.europewelcome.ui.profile.usercomments.UserCommentsFragment;
import eu.balticit.android.europewelcome.ui.profile.userposts.UserPostsFragment;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Profile Activity.
 */
public class ProfileActivity extends BaseActivity
        implements ProfileMvpView, ProfilePagerAdapter.Callback, ChangeProfileFragment.Callback {

    private static final String EXTRA_USER_ID =
            "com.bmd.android.europewelcome.postdetail.user_id";

    @Inject
    ProfileMvpPresenter<ProfileMvpView> mPresenter;

    @Inject
    ProfilePagerAdapter mPagerAdapter;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.feed_view_pager)
    ViewPager mViewPager;

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;

    @BindView(R.id.iv_profile_user_photo)
    ImageView mUserImageIv;

    @BindView(R.id.tv_profile_user_name)
    TextView mUserNameIv;

    @BindView(R.id.tv_profile_user_birth_date)
    TextView mUserBirthDateIv;

    @BindView(R.id.tv_profile_change_profile)
    TextView mChangeProfileTv;

    private String mUserId;

    public static Intent getStartIntent(Context context, String userId) {
        Intent intent = new Intent(context, ProfileActivity.class);
        intent.putExtra(EXTRA_USER_ID, userId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getActivityComponent().inject(this);

        setUnBinder(ButterKnife.bind(this));

        mUserId = (String) getIntent().getSerializableExtra(EXTRA_USER_ID);

        mPresenter.setUserId(mUserId);

        mPresenter.onAttach(this);

        setUp();
    }

    @Override
    protected void setUp() {
        showLoading();
        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        //Tabs
        mPagerAdapter.setCount(2);
        mPagerAdapter.setProfilePagerAdapterCallback(this);

        mViewPager.setAdapter(mPagerAdapter);

        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.profile_user_posts));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.profile_user_comments));
        mViewPager.setOffscreenPageLimit(mTabLayout.getTabCount());

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mPresenter.loadUserProfile();
    }

    @OnClick(R.id.tv_profile_change_profile)
    void onChangeProfileTvClick() {
        showChangeProfileFragment();
    }

    @Override
    public void onFragmentAttached() {

    }

    @Override
    public void onFragmentDetached(String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment != null) {
            fragmentManager
                    .beginTransaction()
                    .disallowAddToBackStack()
                    .setCustomAnimations(R.anim.slide_left, R.anim.slide_right)
                    .remove(fragment)
                    .commitNow();
        }
    }

    @Override
    public void showAboutFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .setCustomAnimations(R.anim.slide_left, R.anim.slide_right)
                .add(R.id.cl_root_view, AboutFragment.newInstance(), AboutFragment.TAG)
                .commit();
    }

    /**
     * Attaches {@link ChangeProfileFragment} to this activity
     */
    private void showChangeProfileFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .setCustomAnimations(R.anim.slide_left, R.anim.slide_right)
                .add(R.id.cl_root_view, ChangeProfileFragment.newInstance(), ChangeProfileFragment.TAG)
                .commit();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(AboutFragment.TAG);
        Fragment fragment2 = fragmentManager.findFragmentByTag(ChangeProfileFragment.TAG);
        if (fragment == null & fragment2 == null) {
            super.onBackPressed();
        } else {
            onFragmentDetached(AboutFragment.TAG);
            onFragmentDetached(ChangeProfileFragment.TAG);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
            case R.id.about:
                showAboutFragment();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }

    @Override
    public void loadUserImageUrl(String userImageUrl) {
        if (userImageUrl != null) {
            GlideApp.with(this)
                    .load(userImageUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(mUserImageIv);
        }
    }

    @Override
    public void loadUserName(String userName) {
        mUserNameIv.setText(userName);
    }

    @Override
    public void loadUserBirthDate(String userBirthDate) {
        mUserBirthDateIv.setText(userBirthDate);
    }

    @Override
    public void showChangeProfile() {
        mChangeProfileTv.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideChangeProfile() {
        mChangeProfileTv.setVisibility(View.GONE);
    }

    @Override
    public Fragment getUserPostsFragment() {
        return UserPostsFragment.newInstance(mUserId);
    }

    @Override
    public Fragment getUserCommentsFragment() {
        return UserCommentsFragment.newInstance(mUserId);
    }

    @Override
    public void updateUserData() {
        mPresenter.loadUserProfile();
    }
}
