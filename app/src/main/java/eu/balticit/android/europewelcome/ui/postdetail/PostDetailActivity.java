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

package eu.balticit.android.europewelcome.ui.postdetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import eu.balticit.android.europewelcome.R;
import eu.balticit.android.europewelcome.data.firebase.model.PostComment;
import eu.balticit.android.europewelcome.data.firebase.model.PostSection;
import eu.balticit.android.europewelcome.di.module.GlideApp;
import eu.balticit.android.europewelcome.ui.about.AboutFragment;
import eu.balticit.android.europewelcome.ui.base.BaseActivity;
import eu.balticit.android.europewelcome.ui.profile.ProfileActivity;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Post Detail Activity
 */
public class PostDetailActivity extends BaseActivity implements PostDetailMvpView,
        PostSectionAdapter.Callback, PostCommentsAdapter.Callback {

    private static final String EXTRA_POST_ID =
            "com.bmd.android.europewelcome.postdetail.post_id";

    @Inject
    PostDetailMvpPresenter<PostDetailMvpView> mPresenter;

    @Inject
    LinearLayoutManager mLayoutManager;

    @Inject
    LinearLayoutManager mLayoutManager2;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.iv_post_detail_user_image)
    ImageView mPostUserImageIv;

    @BindView(R.id.tv_post_detail_user_name)
    TextView mPostUserNameTv;

    @BindView(R.id.tv_post_detail_creation_date)
    TextView mPostDateTv;

    @BindView(R.id.iv_post_detail_current_user_image)
    ImageView mPostNewCommentUserImageIv;

    @BindView(R.id.et_post_detail_new_comment_text)
    EditText mPostNewCommentTextEt;

    @BindView(R.id.rv_post_detail_post_section)
    RecyclerView mPostSectionRv;

    @BindView(R.id.rv_post_detail_comments)
    RecyclerView mPostCommentsRv;

    @BindView(R.id.iv_post_detail_star_image)
    ImageView mStarIv;

    @BindView(R.id.iv_post_detail_bookmark_image)
    ImageView mBookmarkIv;

    @BindView(R.id.tv_post_detail_stars_count)
    TextView mPostStarsTv;

    @BindView(R.id.tv_post_detail_comment_count)
    TextView mPostCommentsTv;

    @BindView(R.id.cl_post_detail_star_container)
    ConstraintLayout mStarContainerCl;

    PostSectionAdapter mPostSectionAdapter;

    PostCommentsAdapter mPostCommentsAdapter;

    private String mPostId;

    private Callback mCallback;

    public static Intent getStartIntent(Context context, String postId) {
        Intent intent = new Intent(context, PostDetailActivity.class);
        intent.putExtra(EXTRA_POST_ID, postId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        mPostId = (String) getIntent().getSerializableExtra(EXTRA_POST_ID);

        getActivityComponent().inject(this);

        setUnBinder(ButterKnife.bind(this));

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
        mPresenter.setPostId(mPostId);


        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mPostSectionRv.setLayoutManager(mLayoutManager);
        mPostSectionRv.setItemAnimator(new DefaultItemAnimator());
        mPostSectionAdapter = new PostSectionAdapter(
                new FirestoreRecyclerOptions.Builder<PostSection>()
                        .setQuery(mPresenter.getPostSectionQuery(), PostSection.class)
                        .build(), this);
        mPostSectionAdapter.setAdapterCallback(this);
        mPostSectionRv.setAdapter(mPostSectionAdapter);


        mLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        mPostCommentsRv.setLayoutManager(mLayoutManager2);
        mPostCommentsRv.setItemAnimator(new DefaultItemAnimator());
        mPostCommentsAdapter = new PostCommentsAdapter(
                new FirestoreRecyclerOptions.Builder<PostComment>()
                        .setQuery(mPresenter.getPostCommentsQuery(), PostComment.class)
                        .build(), this);
        mPostCommentsAdapter.setAdapterCallback(this);
        mPostCommentsRv.setAdapter(mPostCommentsAdapter);

        mPresenter.getPost(mPostId);
        mPostSectionAdapter.startListening();
        mPostCommentsAdapter.startListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.post_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.about:
                showAboutFragment();
                return true;
            case R.id.bookmark:
                onBookmarkIconClick();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
    protected void onDestroy() {
        mPresenter.onDetach();
        mPostSectionAdapter.stopListening();
        mPostCommentsAdapter.stopListening();
        super.onDestroy();
    }

    @OnClick(R.id.cl_post_detail_user_container)
    void onUserImageIvClick() {
        startActivity(ProfileActivity.getStartIntent(getBaseContext(), mPresenter.getPostAuthorId()));
    }


    @OnClick(R.id.iv_post_detail_send_icon)
    void onCommentSend() {
        if (!mPostNewCommentTextEt.getText().toString().equals("") &&
                !mPostNewCommentTextEt.getText().toString().isEmpty()) {
            mPresenter.createNewComment(mPostNewCommentTextEt.getText().toString());
        }
    }

    @Override
    public void clearCommentInput() {
        hideKeyboard();
        mPostNewCommentTextEt.setText(null);
        mPostNewCommentTextEt.clearFocus();
    }

    @Override
    public void setPostUserImage(String userImageUrl) {
        if (userImageUrl != null) {
            GlideApp.with(this)
                    .load(userImageUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(mPostUserImageIv);
        }
    }

    @Override
    public void setPostUserName(String userName) {
        if (userName != null) {
            mPostUserNameTv.setText(userName);
        }
    }

    @Override
    public void setPostCreationDate(String creationDate) {
        if (creationDate != null) {
            mPostDateTv.setText(creationDate);
        }
    }

    @Override
    public void setPostNewCommentUserImage(String userImageUrl) {
        if (userImageUrl != null)
            GlideApp.with(this)
                    .load(userImageUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(mPostNewCommentUserImageIv);
    }

    @OnClick(R.id.cl_post_detail_star_container)
    void onStarIconClick() {
        //disable view to protect from double click
        mStarContainerCl.setEnabled(false);
        mPresenter.addOrRemoveStar();
    }

    @OnClick(R.id.iv_post_detail_bookmark_image)
    void onBookmarkIconClick() {
        //disable view to protect from double click
        mBookmarkIv.setEnabled(false);
        mPresenter.saveOrDeleteBookmark();
    }

    @Override
    public void setBookmarkedIcon() {
        mBookmarkIv.setImageResource(R.drawable.ic_fill_bookmark_blue_24px);
        mBookmarkIv.setEnabled(true);
    }

    @Override
    public void setNotBookmarkedIcon() {
        mBookmarkIv.setImageResource(R.drawable.ic_border_bookmark_gray_24px);
        mBookmarkIv.setEnabled(true);
    }

    @Override
    public void setStarRatedIcon() {
        mStarIv.setImageResource(R.drawable.ic_fill_star_gold_24px);
        mPostStarsTv.setTextColor(getResources().getColor(R.color.orange));
        mStarContainerCl.setEnabled(true);
    }

    @Override
    public void setNotStarRatedIcon() {
        mStarIv.setImageResource(R.drawable.ic_border_star_gray_24px);
        mPostStarsTv.setTextColor(getResources().getColor(R.color.gray_500));
        mStarContainerCl.setEnabled(true);
    }

    @Override
    public void setPostStars(String starsCount) {
        mPostStarsTv.setText(starsCount);
    }

    @Override
    public void setPostComments(String commentsCount) {
        mPostCommentsTv.setText(commentsCount);
    }

    @Override
    public void setCommentLikeIcon(PostCommentsAdapter.ViewHolder holder) {
        if (mCallback != null) {
            mCallback.setCommentLikeIcon(holder);
        }
    }

    @Override
    public void setNotCommentLikeIcon(PostCommentsAdapter.ViewHolder holder) {
        if (mCallback != null) {
            mCallback.removeCommentLikeIcon(holder);
        }
    }


    @Override
    public void onImageClick(PostSection postSection) {
        showMessage("Image has been clicked");
    }

    @Override
    public void hideLoadingSpinner() {
        hideLoading();
    }

    @Override
    public void onLikeCommentClick(PostComment postComment, PostCommentsAdapter.ViewHolder holder) {
        mPresenter.addOrRemoveCommentLike(postComment, holder);
    }

    @Override
    public void checkCommentLikedByUser(PostComment postComment, PostCommentsAdapter.ViewHolder holder) {
        mPresenter.checkCommentLikedByUser(postComment, holder);
    }

    @Override
    public void onCommentUserImageClick(String userId) {
        startActivity(ProfileActivity.getStartIntent(getBaseContext(), userId));
    }

    /**
     * Callback for {@link PostDetailActivity}
     */
    public void setPostDetailActivityCallback(Callback callback) {
        mCallback = callback;
    }

    public interface Callback {

        void setCommentLikeIcon(PostCommentsAdapter.ViewHolder holder);

        void removeCommentLikeIcon(PostCommentsAdapter.ViewHolder holder);
    }
}