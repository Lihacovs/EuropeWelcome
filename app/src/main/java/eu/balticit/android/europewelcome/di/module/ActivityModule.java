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

package eu.balticit.android.europewelcome.di.module;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import eu.balticit.android.europewelcome.data.AppDataManager;
import eu.balticit.android.europewelcome.data.DataManager;
import eu.balticit.android.europewelcome.di.ActivityContext;
import eu.balticit.android.europewelcome.di.PerActivity;
import eu.balticit.android.europewelcome.ui.about.AboutMvpPresenter;
import eu.balticit.android.europewelcome.ui.about.AboutMvpView;
import eu.balticit.android.europewelcome.ui.about.AboutPresenter;
import eu.balticit.android.europewelcome.ui.auth.LoginMvpPresenter;
import eu.balticit.android.europewelcome.ui.auth.LoginMvpView;
import eu.balticit.android.europewelcome.ui.auth.LoginPresenter;
import eu.balticit.android.europewelcome.ui.auth.register.RegisterMvpPresenter;
import eu.balticit.android.europewelcome.ui.auth.register.RegisterMvpView;
import eu.balticit.android.europewelcome.ui.auth.register.RegisterPresenter;
import eu.balticit.android.europewelcome.ui.bookmarks.BookmarksMvpPresenter;
import eu.balticit.android.europewelcome.ui.bookmarks.BookmarksMvpView;
import eu.balticit.android.europewelcome.ui.bookmarks.BookmarksPresenter;
import eu.balticit.android.europewelcome.ui.drafts.DraftsMvpPresenter;
import eu.balticit.android.europewelcome.ui.drafts.DraftsMvpView;
import eu.balticit.android.europewelcome.ui.drafts.DraftsPresenter;
import eu.balticit.android.europewelcome.ui.newpost.NewPostMvpPresenter;
import eu.balticit.android.europewelcome.ui.newpost.NewPostMvpView;
import eu.balticit.android.europewelcome.ui.newpost.NewPostPresenter;
import eu.balticit.android.europewelcome.ui.postdetail.PostDetailMvpPresenter;
import eu.balticit.android.europewelcome.ui.postdetail.PostDetailMvpView;
import eu.balticit.android.europewelcome.ui.postdetail.PostDetailPresenter;
import eu.balticit.android.europewelcome.ui.posts.PostsMvpPresenter;
import eu.balticit.android.europewelcome.ui.posts.PostsMvpView;
import eu.balticit.android.europewelcome.ui.posts.PostsPagerAdapter;
import eu.balticit.android.europewelcome.ui.posts.PostsPresenter;
import eu.balticit.android.europewelcome.ui.posts.buypremium.BuyPremiumDialogMvpPresenter;
import eu.balticit.android.europewelcome.ui.posts.buypremium.BuyPremiumDialogMvpView;
import eu.balticit.android.europewelcome.ui.posts.buypremium.BuyPremiumDialogPresenter;
import eu.balticit.android.europewelcome.ui.posts.freeposts.FreePostsMvpPresenter;
import eu.balticit.android.europewelcome.ui.posts.freeposts.FreePostsMvpView;
import eu.balticit.android.europewelcome.ui.posts.freeposts.FreePostsPresenter;
import eu.balticit.android.europewelcome.ui.posts.premiumposts.PremiumPostsMvpPresenter;
import eu.balticit.android.europewelcome.ui.posts.premiumposts.PremiumPostsMvpView;
import eu.balticit.android.europewelcome.ui.posts.premiumposts.PremiumPostsPresenter;
import eu.balticit.android.europewelcome.ui.posts.rating.RatingDialogMvpPresenter;
import eu.balticit.android.europewelcome.ui.posts.rating.RatingDialogMvpView;
import eu.balticit.android.europewelcome.ui.posts.rating.RatingDialogPresenter;
import eu.balticit.android.europewelcome.ui.premium.PremiumMvpPresenter;
import eu.balticit.android.europewelcome.ui.premium.PremiumMvpView;
import eu.balticit.android.europewelcome.ui.premium.PremiumPresenter;
import eu.balticit.android.europewelcome.ui.profile.ProfileMvpPresenter;
import eu.balticit.android.europewelcome.ui.profile.ProfileMvpView;
import eu.balticit.android.europewelcome.ui.profile.ProfilePagerAdapter;
import eu.balticit.android.europewelcome.ui.profile.ProfilePresenter;
import eu.balticit.android.europewelcome.ui.profile.changeprofile.ChangeProfileMvpPresenter;
import eu.balticit.android.europewelcome.ui.profile.changeprofile.ChangeProfileMvpView;
import eu.balticit.android.europewelcome.ui.profile.changeprofile.ChangeProfilePresenter;
import eu.balticit.android.europewelcome.ui.profile.usercomments.UserCommentsMvpPresenter;
import eu.balticit.android.europewelcome.ui.profile.usercomments.UserCommentsMvpView;
import eu.balticit.android.europewelcome.ui.profile.usercomments.UserCommentsPresenter;
import eu.balticit.android.europewelcome.ui.profile.userposts.UserPostsMvpPresenter;
import eu.balticit.android.europewelcome.ui.profile.userposts.UserPostsMvpView;
import eu.balticit.android.europewelcome.ui.profile.userposts.UserPostsPresenter;

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
    ChangeProfileMvpPresenter<ChangeProfileMvpView> provideChangeProfilePresenter(
            ChangeProfilePresenter<ChangeProfileMvpView> presenter) {
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

    @Provides
    BuyPremiumDialogMvpPresenter<BuyPremiumDialogMvpView> provideBuyPremiumPresenter(
            BuyPremiumDialogPresenter<BuyPremiumDialogMvpView> presenter) {
        return presenter;
    }

    /*@Provides
    FeedMvpPresenter<FeedMvpView> provideFeedPresenter(
            FeedPresenter<FeedMvpView> presenter) {
        return presenter;
    }*/

    @Provides
    UserPostsMvpPresenter<UserPostsMvpView> provideUserPostsPresenter(
            UserPostsPresenter<UserPostsMvpView> presenter) {
        return presenter;
    }

    @Provides
    UserCommentsMvpPresenter<UserCommentsMvpView> provideUserCommentsPresenter(
            UserCommentsPresenter<UserCommentsMvpView> presenter) {
        return presenter;
    }

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
    PremiumMvpPresenter<PremiumMvpView> providePremiumPresenter(
            PremiumPresenter<PremiumMvpView> presenter) {
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

    @Provides
    ProfilePagerAdapter provideProfilePagerAdapter(AppCompatActivity activity) {
        return new ProfilePagerAdapter(activity.getSupportFragmentManager());
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
