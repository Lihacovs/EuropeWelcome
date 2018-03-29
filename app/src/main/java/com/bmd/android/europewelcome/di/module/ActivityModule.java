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

package com.bmd.android.europewelcome.di.module;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.bmd.android.europewelcome.di.ActivityContext;
import com.bmd.android.europewelcome.di.PerActivity;
import com.bmd.android.europewelcome.ui.about.AboutMvpPresenter;
import com.bmd.android.europewelcome.ui.about.AboutMvpView;
import com.bmd.android.europewelcome.ui.about.AboutPresenter;
import com.bmd.android.europewelcome.ui.auth.LoginMvpPresenter;
import com.bmd.android.europewelcome.ui.auth.LoginMvpView;
import com.bmd.android.europewelcome.ui.auth.LoginPresenter;
import com.bmd.android.europewelcome.ui.auth.register.RegisterMvpPresenter;
import com.bmd.android.europewelcome.ui.auth.register.RegisterMvpView;
import com.bmd.android.europewelcome.ui.auth.register.RegisterPresenter;
import com.bmd.android.europewelcome.ui.bookmarks.BookmarksMvpPresenter;
import com.bmd.android.europewelcome.ui.bookmarks.BookmarksMvpView;
import com.bmd.android.europewelcome.ui.bookmarks.BookmarksPresenter;
import com.bmd.android.europewelcome.ui.drafts.DraftsMvpPresenter;
import com.bmd.android.europewelcome.ui.drafts.DraftsMvpView;
import com.bmd.android.europewelcome.ui.drafts.DraftsPresenter;
import com.bmd.android.europewelcome.ui.newpost.NewPostMvpPresenter;
import com.bmd.android.europewelcome.ui.newpost.NewPostMvpView;
import com.bmd.android.europewelcome.ui.newpost.NewPostPresenter;
import com.bmd.android.europewelcome.ui.postdetail.PostDetailMvpPresenter;
import com.bmd.android.europewelcome.ui.postdetail.PostDetailMvpView;
import com.bmd.android.europewelcome.ui.postdetail.PostDetailPresenter;
import com.bmd.android.europewelcome.ui.posts.PostsMvpPresenter;
import com.bmd.android.europewelcome.ui.posts.PostsMvpView;
import com.bmd.android.europewelcome.ui.posts.PostsPagerAdapter;
import com.bmd.android.europewelcome.ui.posts.PostsPresenter;
import com.bmd.android.europewelcome.ui.posts.free.FreePostsMvpPresenter;
import com.bmd.android.europewelcome.ui.posts.free.FreePostsMvpView;
import com.bmd.android.europewelcome.ui.posts.free.FreePostsPresenter;
import com.bmd.android.europewelcome.ui.posts.premium.PremiumPostsMvpPresenter;
import com.bmd.android.europewelcome.ui.posts.premium.PremiumPostsMvpView;
import com.bmd.android.europewelcome.ui.posts.premium.PremiumPostsPresenter;
import com.bmd.android.europewelcome.ui.posts.rating.RatingDialogMvpPresenter;
import com.bmd.android.europewelcome.ui.posts.rating.RatingDialogMvpView;
import com.bmd.android.europewelcome.ui.posts.rating.RatingDialogPresenter;
import com.bmd.android.europewelcome.ui.profile.ProfileMvpPresenter;
import com.bmd.android.europewelcome.ui.profile.ProfileMvpView;
import com.bmd.android.europewelcome.ui.profile.ProfilePresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Provides dependencies for Activities
 */

@Module
public class ActivityModule {

    private AppCompatActivity mActivity;

    public ActivityModule(AppCompatActivity activity) {
        this.mActivity = activity;
    }

    @Provides
    @ActivityContext
    Context provideContext() {
        return mActivity;
    }

    @Provides
    AppCompatActivity provideActivity() {
        return mActivity;
    }

    /*@Provides
    @PerActivity
    SplashMvpPresenter<SplashMvpView> provideSplashPresenter(
            SplashPresenter<SplashMvpView> presenter) {
        return presenter;
    }*/

    @Provides
    AboutMvpPresenter<AboutMvpView> provideAboutPresenter(
            AboutPresenter<AboutMvpView> presenter) {
        return presenter;
    }

    @Provides
    RegisterMvpPresenter<RegisterMvpView> provideRegisterPresenter(
            RegisterPresenter<RegisterMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    LoginMvpPresenter<LoginMvpView> provideLoginPresenter(
            LoginPresenter<LoginMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    PostsMvpPresenter<PostsMvpView> providePostsPresenter(
            PostsPresenter<PostsMvpView> presenter) {
        return presenter;
    }

    @Provides
    RatingDialogMvpPresenter<RatingDialogMvpView> provideRateUsPresenter(
            RatingDialogPresenter<RatingDialogMvpView> presenter) {
        return presenter;
    }

    /*@Provides
    FeedMvpPresenter<FeedMvpView> provideFeedPresenter(
            FeedPresenter<FeedMvpView> presenter) {
        return presenter;
    }*/

    @Provides
    PremiumPostsMvpPresenter<PremiumPostsMvpView> providePremiumPostsPresenter(
            PremiumPostsPresenter<PremiumPostsMvpView> presenter) {
        return presenter;
    }

    @Provides
    FreePostsMvpPresenter<FreePostsMvpView> provideFreePostsPresenter(
            FreePostsPresenter<FreePostsMvpView> presenter) {
        return presenter;
    }

    @Provides
    PostDetailMvpPresenter<PostDetailMvpView> providePostDetailPresenter(
            PostDetailPresenter<PostDetailMvpView> presenter) {
        return presenter;
    }

    @Provides
    DraftsMvpPresenter<DraftsMvpView> provideDraftsPresenter(
             DraftsPresenter<DraftsMvpView> presenter) {
        return presenter;
    }

    @Provides
    BookmarksMvpPresenter<BookmarksMvpView> provideBookmarksPresenter(
            BookmarksPresenter<BookmarksMvpView> presenter) {
        return presenter;
    }

    @Provides
    NewPostMvpPresenter<NewPostMvpView> provideNewPostPresenter(
            NewPostPresenter<NewPostMvpView> presenter) {
        return presenter;
    }

    @Provides
    ProfileMvpPresenter<ProfileMvpView> provideProfilePresenter(
            ProfilePresenter<ProfileMvpView> presenter) {
        return presenter;
    }

    @Provides
    PostsPagerAdapter providePostsPagerAdapter(AppCompatActivity activity) {
        return new PostsPagerAdapter(activity.getSupportFragmentManager());
    }

    /*@Provides
    OpenSourceAdapter provideOpenSourceAdapter() {
        return new OpenSourceAdapter(new ArrayList<OpenSourceResponse.Repo>());
    }*/

    /*@Provides
    FreePostsAdapter provideFreePostsAdapter(FirestoreRecyclerOptions<Post> options) {
        return new FreePostsAdapter(options);
    }*/

    @Provides
    LinearLayoutManager provideLinearLayoutManager(AppCompatActivity activity) {
        return new LinearLayoutManager(activity);
    }
}
