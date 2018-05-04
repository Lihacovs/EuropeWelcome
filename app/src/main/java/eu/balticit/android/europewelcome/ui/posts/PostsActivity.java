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

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import eu.balticit.android.europewelcome.BuildConfig;
import eu.balticit.android.europewelcome.R;
import eu.balticit.android.europewelcome.di.module.GlideApp;
import eu.balticit.android.europewelcome.ui.about.AboutFragment;
import eu.balticit.android.europewelcome.ui.auth.LoginActivity;
import eu.balticit.android.europewelcome.ui.base.BaseActivity;
import eu.balticit.android.europewelcome.ui.bookmarks.BookmarksActivity;
import eu.balticit.android.europewelcome.ui.custom.CustomViewPager;
import eu.balticit.android.europewelcome.ui.drafts.DraftsActivity;
import eu.balticit.android.europewelcome.ui.newpost.NewPostActivity;
import eu.balticit.android.europewelcome.ui.posts.buypremium.BuyPremiumDialog;
import eu.balticit.android.europewelcome.ui.posts.freeposts.FreePostsFragment;
import eu.balticit.android.europewelcome.ui.posts.rating.RateUsDialog;
import eu.balticit.android.europewelcome.ui.premium.PremiumActivity;
import eu.balticit.android.europewelcome.ui.profile.ProfileActivity;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eu.balticit.android.europewelcome.utils.billing.IabHelper;
import eu.balticit.android.europewelcome.utils.billing.IabResult;
import eu.balticit.android.europewelcome.utils.billing.Inventory;
import eu.balticit.android.europewelcome.utils.billing.Purchase;

/**
 * Posts Activity
 */

public class PostsActivity extends BaseActivity implements PostsMvpView, BuyPremiumDialog.Callback {

    private static final String TAG = "PostsActivityTAG";

    @Inject
    PostsMvpPresenter<PostsMvpView> mPresenter;

    @Inject
    PostsPagerAdapter mPagerAdapter;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.feed_view_pager)
    CustomViewPager mViewPager;

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

    private IabHelper mHelper;
    private static final String mPremiumProductSKU = "europe_welcome_premium_1";
    // Does the user have the premium upgrade?
    boolean mIsPremium = false;
    // (arbitrary) request code for the purchase flow
    static final int RC_REQUEST = 10001;


    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, PostsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);

        getActivityComponent().inject(this);

        setUnBinder(ButterKnife.bind(this));

        mPresenter.onAttach(this);

        //--//--// Google In App Billing Setup //--//--//
        //--//--// S T A R T //--//--//
        /* base64EncodedPublicKey should be YOUR APPLICATION'S PUBLIC KEY
         * (that you got from the Google Play developer console). This is not your
         * developer public key, it's the *app-specific* public key.
         *
         * Instead of just storing the entire literal string here embedded in the
         * program,  construct the key at runtime from pieces or
         * use bit manipulation (for example, XOR with some other string) to hide
         * the actual key.  The key itself is not secret information, but we don't
         * want to make it easy for an attacker to replace the public key with one
         * of their own and then fake messages from the server.
         */
        mHelper = new IabHelper(this, getString(R.string.APP_ENCODED_PUBLIC_KEY));
        mHelper.enableDebugLogging(false);
        mHelper.startSetup(result -> {
            if (!result.isSuccess()) {
                // Oh no, there was a problem.
                onError("Some error occurred. Please try again");
                Log.d(TAG, "Problem setting up In-app Billing: " + result);
                return;
            }
            // Have we been disposed of in the meantime? If so, quit.
            if (mHelper == null) return;

            Log.d(TAG, "In-app Billing ready: " + result);

            // IAB is fully set up. Now, let's get an inventory of stuff we own.
            Log.d(TAG, "Setup successful. Querying inventory.");
            try {
                mHelper.queryInventoryAsync(mGotInventoryListener);
            } catch (IabHelper.IabAsyncInProgressException e) {
                Log.d(TAG, "onCreate: Error querying inventory. Another async operation in progress.");
            }
        });
        //--//--// Google In App Billing Setup //--//--//
        //--//--// E N D //--//--//

        setUp();
    }

    //--//--// Google In App Billing Methods //--//--//
    //--//--// S T A R T //--//--//

    // Listener that's called when we finish querying the items and subscriptions we own
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d(TAG, "Query inventory finished.");

            // Have we been disposed of in the meantime? If so, quit.
            if (mHelper == null) return;

            // Is it a failure?
            if (result.isFailure()) {
                Log.d(TAG, "onQueryInventoryFinished: Failed to query inventory: " + result);
                return;
            }

            Log.d(TAG, "Query inventory was successful.");

            /*
             * Check for items we own. Notice that for each purchase, we check
             * the developer payload to see if it's correct! See
             * verifyDeveloperPayload().
             */

            // Do we have the premium upgrade?
            Purchase premiumPurchase = inventory.getPurchase(mPremiumProductSKU);
            if(premiumPurchase != null && verifyDeveloperPayload(premiumPurchase)){
                try {
                    mHelper.consumeAsync(premiumPurchase, mConsumeFinishedListener);
                } catch (IabHelper.IabAsyncInProgressException e) {
                    e.printStackTrace();
                }
            }

            mIsPremium = (premiumPurchase != null && verifyDeveloperPayload(premiumPurchase));
            Log.d(TAG, "User is " + (mIsPremium ? "PREMIUM" : "NOT PREMIUM"));

            Log.d(TAG, "Initial inventory query finished; enabling main UI.");
        }
    };

    // User clicked the "Upgrade to Premium" button.
    public void onUpgradeAppButtonClicked(View arg0) {
        Log.d(TAG, "Upgrade button clicked; launching purchase flow for upgrade.");

        //For security, generate your payload here for verification.
        String payload = mPresenter.getCurrentUserId();

        try {
            mHelper.launchPurchaseFlow(this, mPremiumProductSKU, RC_REQUEST,
                    mPurchaseFinishedListener, payload);
        } catch (IabHelper.IabAsyncInProgressException e) {
            Log.d(TAG, "onUpgradeAppButtonClicked: Error launching purchase flow. Another async operation in progress.");
        }
    }

    // Callback for when a purchase is finished
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            if (result.isFailure()) {
                Log.d(TAG, "onIabPurchaseFinished: Error purchasing: " + result);
                return;
            }
            if (!verifyDeveloperPayload(purchase)) {
                onError("Some error occurred. Please try again");
                Log.d(TAG, "onIabPurchaseFinished: Error purchasing. Authenticity verification failed.");
                return;
            }

            Log.d(TAG, "Purchase successful.");

            if (purchase.getSku().equals(mPremiumProductSKU)) {
                // bought the premium upgrade!
                Log.d(TAG, "Purchase is premium upgrade. Congratulating user.");
                mPresenter.makeUserPremium();

                //showMessage("Thank you for upgrading to premium!");

                try {
                    mHelper.consumeAsync(purchase, mConsumeFinishedListener);
                } catch (IabHelper.IabAsyncInProgressException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    // Called when consumption is complete
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            Log.d(TAG, "Consumption finished. Purchase: " + purchase + ", result: " + result);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            // We know this is the "gas" sku because it's the only one we consume,
            // so we don't check which sku was consumed. If you have more than one
            // sku, you probably should check...
            if (result.isSuccess()) {
                // successfully consumed, so we apply the effects of the item in our
                // game world's logic, which in our case means filling the gas tank a bit
                Log.d(TAG, "Consumption successful. Provisioning.");
            } else {
                Log.d(TAG, "onConsumeFinished: Error while consuming: " + result);
            }
            //updateUi();
            //setWaitScreen(false);
            Log.d(TAG, "End consumption flow.");
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);
        if (mHelper == null) return;

        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            super.onActivityResult(requestCode, resultCode, data);
        } else {
            Log.d(TAG, "onActivityResult handled by IABUtil.");
        }
    }

    /**
     * Verifies the developer payload of a purchase.
     */
    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();
        /*
         * verify that the developer payload of the purchase is correct. It will be
         * the same one that you sent when initiating the purchase.
         *
         * WARNING: Locally generating a random string when starting a purchase and
         * verifying it here might seem like a good approach, but this will fail in the
         * case where the user purchases an item on one device and then uses your app on
         * a different device, because on the other device you will not have access to the
         * random string you originally generated.
         *
         * So a good developer payload has these characteristics:
         *
         * 1. If two different users purchase an item, the payload is different between them,
         *    so that one user's purchase can't be replayed to another user.
         *
         * 2. The payload must be such that you can verify it even when the app wasn't the
         *    one who initiated the purchase flow (so that items purchased by the user on
         *    one device work on other devices owned by the user).
         *
         * Using your own server to store and verify developer payloads across app
         * installations is recommended.
         */
        return mPresenter.getCurrentUserId().equals(payload);
    }
    //--//--// Google In App Billing Methods //--//--//
    //--//--// E N D //--//--//

    public void setActivityCallback(Callback callback) {
        mCallback = callback;
    }

    @Override
    public void purchasePremiumClick() {
        onUpgradeAppButtonClicked(null);
    }

    public interface Callback {
        void onFilterPostsByStarsClick();

        void onFilterPostsByViewsClick();

        void onFilterPostsByDateClick();

        void onFilterPostsByCommentsClick();

        void onTabLongClick();
    }

    @OnClick(R.id.fab)
    void onFabClick() {
        mPresenter.onNewPostClick();
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
    public void chooseLoginAction() {
        Menu navMenu = mNavigationView.getMenu();
        if (mPresenter.checkUserSigned()) {
            navMenu.findItem(R.id.nav_item_sign_in).setVisible(false);
            navMenu.findItem(R.id.nav_item_logout).setVisible(true);
        } else {
            navMenu.findItem(R.id.nav_item_logout).setVisible(false);
            navMenu.findItem(R.id.nav_item_sign_in).setVisible(true);
        }
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
    public void setDefaultUserImage() {
        mUserImageIv.setImageResource(R.drawable.ic_europe_stars_24px);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //every time activity goes on top in task's stack - update nav drawer
        //reselectTab(0);
        mPresenter.onNavMenuCreated();
        if (mDrawer != null) {
            mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();

        if (mHelper != null) {
            try {
                mHelper.dispose();
            } catch (IabHelper.IabAsyncInProgressException e) {
                e.printStackTrace();
            }
        }
        mHelper = null;
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
                mPresenter.onNewPostClick();
                return true;
            case R.id.my_profile:
                mPresenter.onDrawerOptionProfileClick();
                return true;
            case R.id.about:
                mPresenter.onDrawerOptionAboutClick();
                return true;
            case R.id.logout_user:
                mPresenter.onDrawerOptionLogoutClick();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Popup menu for post's list filter options
     *
     * @param v popup view anchor
     */
    private void showFilterPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.filter_allposts_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.filter_posts_by_stars:
                    if (mCallback != null)
                        mCallback.onFilterPostsByStarsClick();
                    return true;
                case R.id.filter_posts_by_comments:
                    if (mCallback != null)
                        mCallback.onFilterPostsByCommentsClick();
                    return true;
                case R.id.filter_posts_by_date:
                    if (mCallback != null)
                        mCallback.onFilterPostsByDateClick();
                    return true;
                default:
                    return false;
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
        //mPresenter.onNavMenuCreated();

        //Tabs
        mPagerAdapter.setCount(2);

        mViewPager.setAdapter(mPagerAdapter);

        mTabLayout.addTab(mTabLayout.newTab().setText("Free"));
        mTabLayout.addTab(mTabLayout.newTab().setText("Premium"));
        mViewPager.setOffscreenPageLimit(mTabLayout.getTabCount());

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        mViewPager.disableScroll(true);

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        mViewPager.setCurrentItem(tab.getPosition());
                        break;
                    case 1:
                        mPresenter.checkUserHasPremium(tab.getPosition());
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //Long click for Admin to see not accepted posts
        LinearLayout tabStrip = (LinearLayout) mTabLayout.getChildAt(0);
        tabStrip.getChildAt(0).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mCallback != null)
                    mCallback.onTabLongClick();
                return false;
            }
        });


        mPresenter.onViewInitialized();
    }

    @Override
    public void showPremiumTab(int tabPosition) {
        mViewPager.setCurrentItem(tabPosition);
    }

    @Override
    public void reselectTab(int tabPosition) {
        Objects.requireNonNull(mTabLayout.getTabAt(tabPosition)).select();
    }

    @Override
    public void disablePagerScroll() {
        mViewPager.disableScroll(true);
    }

    @Override
    public void enablePagerScroll() {
        mViewPager.disableScroll(false);
    }

    void setupNavMenu() {
        View headerLayout = mNavigationView.getHeaderView(0);
        mUserImageIv = headerLayout.findViewById(R.id.iv_drawer_header_user_photo);
        mNameTextView = headerLayout.findViewById(R.id.tv_drawer_header_user_name);
        mEmailTextView = headerLayout.findViewById(R.id.tv_drawer_header_user_email);

        headerLayout.setOnClickListener(view -> mPresenter.onDrawerOptionProfileClick());

        mNavigationView.setNavigationItemSelectedListener(
                item -> {
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
                        case R.id.nav_item_sign_in:
                            mPresenter.onDrawerOptionLogoutClick();
                            return true;
                        default:
                            return false;
                    }
                });
    }

    @Override
    public void openLoginActivity() {
        startActivity(LoginActivity.getStartIntent(this));
    }

    @Override
    public void showBuyPremiumDialog() {
        BuyPremiumDialog.newInstance().show(getSupportFragmentManager(), this);
    }

    @Override
    public void openPremiumActivity() {
        startActivity(PremiumActivity.getStartIntent(this));
    }

    @Override
    public void showRateUsDialog() {
        RateUsDialog.newInstance().show(getSupportFragmentManager());
    }

    @Override
    public void openBookmarksActivity() {
        startActivity(BookmarksActivity.getStartIntent(this));
    }

    @Override
    public void openDraftsActivity() {
        startActivity(DraftsActivity.getStartIntent(this));
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