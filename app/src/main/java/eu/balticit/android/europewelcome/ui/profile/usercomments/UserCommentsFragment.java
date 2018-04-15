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

package eu.balticit.android.europewelcome.ui.profile.usercomments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import eu.balticit.android.europewelcome.R;
import eu.balticit.android.europewelcome.data.firebase.model.PostComment;
import eu.balticit.android.europewelcome.di.component.ActivityComponent;
import eu.balticit.android.europewelcome.ui.base.BaseFragment;
import eu.balticit.android.europewelcome.ui.postdetail.PostDetailActivity;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.balticit.android.europewelcome.data.firebase.model.PostComment;
import eu.balticit.android.europewelcome.ui.postdetail.PostDetailActivity;

public class UserCommentsFragment extends BaseFragment implements
        UserCommentsMvpView, UserCommentsAdapter.Callback{

    private static final String TAG = "UserPostsFragment";
    private static final String EXTRA_USER_ID =
            "com.bmd.android.europewelcome.profile.usercomments.user_id";

    @Inject
    UserCommentsMvpPresenter<UserCommentsMvpView> mPresenter;

    @Inject
    LinearLayoutManager mLayoutManager;

    @BindView(R.id.user_comments_recycler_view)
    RecyclerView mRecyclerView;

    UserCommentsAdapter mUserCommentsAdapter;

    private String mUserId;

    public static UserCommentsFragment newInstance(String userId) {
        Bundle args = new Bundle();
        args.putString(EXTRA_USER_ID, userId);
        UserCommentsFragment fragment = new UserCommentsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_comments, container, false);

        ActivityComponent component = getActivityComponent();
        if (component != null) {
            component.inject(this);
            setUnBinder(ButterKnife.bind(this, view));
            mPresenter.onAttach(this);
            //mOpenSourceAdapter.setCallback(this);
        }

        Bundle args = getArguments();
        if (args != null) {
            mUserId = args.getString(EXTRA_USER_ID);
        }

        return view;
    }

    @Override
    protected void setUp(View view) {
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mUserCommentsAdapter = new UserCommentsAdapter(
                new FirestoreRecyclerOptions.Builder<PostComment>()
                        .setQuery(mPresenter.getUserCommentsQuery(mUserId), PostComment.class)
                        .build());
        mUserCommentsAdapter.setAdapterCallback(this);
        mRecyclerView.setAdapter(mUserCommentsAdapter);

        //mPresenter.onViewPrepared();
    }

    @Override
    public void onStart() {
        super.onStart();
        mUserCommentsAdapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        mUserCommentsAdapter.stopListening();
    }

    @Override
    public void onDestroyView() {
        mPresenter.onDetach();
        super.onDestroyView();
    }

    @Override
    public void onPostItemViewClick(String postId) {
        startActivity(PostDetailActivity.getStartIntent(getBaseActivity(), postId));
    }
}
