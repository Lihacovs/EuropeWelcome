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

package com.bmd.android.europewelcome.ui.posts;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bmd.android.europewelcome.BuildConfig;
import com.bmd.android.europewelcome.R;
import com.bmd.android.europewelcome.di.module.GlideApp;
import com.bmd.android.europewelcome.ui.about.AboutFragment;
import com.bmd.android.europewelcome.ui.auth.LoginActivity;
import com.bmd.android.europewelcome.ui.base.BaseActivity;
import com.bmd.android.europewelcome.ui.newpost.NewPostActivity;
import com.bmd.android.europewelcome.ui.posts.rating.RateUsDialog;
import com.bmd.android.europewelcome.ui.profile.ProfileActivity;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Posts Activity
 */

public class PostsActivity extends BaseActivity implements PostsMvpView{

    @Inject
    PostsMvpPresenter<PostsMvpView> mPresenter;

    @Inject
    PostsPagerAdapter mPagerAdapter;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.feed_view_pager)
    ViewPager mViewPager;

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;

    @BindView(R.id.drawer_view)
    DrawerLayout mDrawer;

    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;

    @BindView(R.id.tv_app_version)
    TextView mAppVersionTextView;

    private Callback mCallback;

    private TextView mNameTextView;

    private TextView mEmailTextView;

    private ImageView mUserImageIv;

    private ActionBarDrawerToggle mDrawerToggle;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, PostsActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);

        getActivityComponent().inject(this);

        setUnBinder(ButterKnife.bind(this));

        mPresenter.onAttach(this);

        setUp();
    }

    public void setActivityCallback(Callback callback) {
        mCallback = callback;
    }

    public interface Callback {
        void onFilterPostsByStarsClick();

        void onFilterPostsByViewsClick();

        void onFilterPostsByDateClick();
    }

    @OnClick(R.id.fab)
    void onFabClick(View v) {
        mPresenter.onFabClick();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(AboutFragment.TAG);
        if (fragment == null) {
            super.onBackPressed();
        } else {
            onFragmentDetached(AboutFragment.TAG);
        }
    }

    @Override
    public void updateAppVersion() {
        String version = getString(R.string.version) + " " + BuildConfig.VERSION_NAME;
        mAppVersionTextView.setText(version);
    }

    @Override
    public void updateUserName(String currentUserName) {
        mNameTextView.setText(currentUserName);
    }

    @Override
    public void updateUserEmail(String currentUserEmail) {
        mEmailTextView.setText(currentUserEmail);
    }

    @Override
    public void updateUserProfilePic(String currentUserProfilePicUrl) {
        if (currentUserProfilePicUrl != null) {
            GlideApp.with(this)
                    .load(currentUserProfilePicUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(mUserImageIv);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mDrawer != null) {
            mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
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
            unlockDrawer();
        }
    }

    @Override
    public void showAboutFragment() {
        lockDrawer();
        getSupportFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .setCustomAnimations(R.anim.slide_left, R.anim.slide_right)
                .add(R.id.cl_root_view, AboutFragment.newInstance(), AboutFragment.TAG)
                .commit();
    }

    @Override
    public void lockDrawer() {
        if (mDrawer != null)
            mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    @Override
    public void unlockDrawer() {
        if (mDrawer != null)
            mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.posts_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Drawable drawable = item.getIcon();
        if (drawable instanceof Animatable) {
            ((Animatable) drawable).start();
        }
        switch (item.getItemId()) {
            case R.id.filter_posts:
                showFilterPopup(this.findViewById(R.id.filter_posts));
                return true;
            case R.id.new_post:
                return true;
            case R.id.my_profile:
                return true;
            case R.id.about:
                return true;
            case R.id.logout_user:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Popup menu for post's list filter options
     * @param v popup view anchor
     */
    private void showFilterPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.filter_allposts_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    //TODO: cant sort strings correctly, need to use int
                    case R.id.filter_posts_by_stars:
                        if (mCallback != null)
                            mCallback.onFilterPostsByStarsClick();
                        return true;
                    case R.id.filter_posts_by_views:
                        if (mCallback != null)
                            mCallback.onFilterPostsByViewsClick();
                        return true;
                    case R.id.filter_posts_by_date:
                        return true;
                    default:
                        return false;
                }
            }
        });

        popup.show();
    }

    @Override
    protected void setUp() {
        setSupportActionBar(mToolbar);

        //Navigation drawer
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawer,
                mToolbar,
                R.string.open_drawer,
                R.string.close_drawer) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                hideKeyboard();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawer.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        setupNavMenu();
        mPresenter.onNavMenuCreated();

        //Tabs
        mPagerAdapter.setCount(2);

        mViewPager.setAdapter(mPagerAdapter);

        mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.blog)));
        mTabLayout.addTab(mTabLayout.newTab().setText("Premium"));
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

        mPresenter.onViewInitialized();
    }

    void setupNavMenu() {
        View headerLayout = mNavigationView.getHeaderView(0);
        mUserImageIv = headerLayout.findViewById(R.id.iv_drawer_header_user_photo);
        mNameTextView = headerLayout.findViewById(R.id.tv_drawer_header_user_name);
        mEmailTextView = headerLayout.findViewById(R.id.tv_drawer_header_user_email);

        headerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onDrawerOptionProfileClick();
            }
        });

        mNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        mDrawer.closeDrawer(GravityCompat.START);
                        switch (item.getItemId()) {
                            case R.id.nav_item_profile:
                                mPresenter.onDrawerOptionProfileClick();
                                return true;
                            case R.id.nav_item_bookmarks:
                                mPresenter.onDrawerOptionBookmarksClick();
                                return true;
                            case R.id.nav_item_drafts:
                                mPresenter.onDrawerOptionDraftsClick();
                                return true;
                            case R.id.nav_item_premium:
                                mPresenter.onDrawerOptionPremiumClick();
                                return true;
                            case R.id.nav_item_about:
                                mPresenter.onDrawerOptionAboutClick();
                                return true;
                            case R.id.nav_item_rate_us:
                                mPresenter.onDrawerRateUsClick();
                                return true;
                            case R.id.nav_item_logout:
                                mPresenter.onDrawerOptionLogoutClick();
                                return true;
                            default:
                                return false;
                        }
                    }
                });
    }

    @Override
    public void openLoginActivity() {
        startActivity(LoginActivity.getStartIntent(this));
    }

    @Override
    public void showRateUsDialog() {
        RateUsDialog.newInstance().show(getSupportFragmentManager());
    }

    @Override
    public void openAddPostActivity() {
        startActivity(NewPostActivity.getStartIntent(this, null));
    }

    @Override
    public void openNewPostActivity() {
        startActivity(NewPostActivity.getStartIntent(this, null));
    }

    @Override
    public void openProfileActivity() {
        startActivity(ProfileActivity.getStartIntent(getBaseContext(), mPresenter.getCurrentUserId()));
    }

    @Override
    public void closeNavigationDrawer() {
        if (mDrawer != null) {
            mDrawer.closeDrawer(Gravity.START);
        }
    }
}