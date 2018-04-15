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

package eu.balticit.android.europewelcome.ui.profile.userposts;

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
import eu.balticit.android.europewelcome.data.firebase.model.Post;
import eu.balticit.android.europewelcome.di.component.ActivityComponent;
import eu.balticit.android.europewelcome.ui.base.BaseFragment;
import eu.balticit.android.europewelcome.ui.postdetail.PostDetailActivity;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.balticit.android.europewelcome.data.firebase.model.Post;
import eu.balticit.android.europewelcome.ui.postdetail.PostDetailActivity;

public class UserPostsFragment extends BaseFragment implements
        UserPostsMvpView, UserPostsAdapter.Callback {

    private static final String TAG = "UserPostsFragment";
    private static final String EXTRA_USER_ID =
            "com.bmd.android.europewelcome.profile.userposts.user_id";

    @Inject
    UserPostsMvpPresenter<UserPostsMvpView> mPresenter;

    @Inject
    LinearLayoutManager mLayoutManager;

    @BindView(R.id.user_posts_recycler_view)
    RecyclerView mRecyclerView;

    UserPostsAdapter mUserPostsAdapter;

    private String mUserId;

    public static UserPostsFragment newInstance(String userId) {
        Bundle args = new Bundle();
        args.putString(EXTRA_USER_ID, userId);
        UserPostsFragment fragment = new UserPostsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_posts, container, false);

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
        mUserPostsAdapter = new UserPostsAdapter(
                new FirestoreRecyclerOptions.Builder<Post>()
                        .setQuery(mPresenter.getUserPostsQuery(mUserId), Post.class)
                        .build());
        mUserPostsAdapter.setAdapterCallback(this);
        mRecyclerView.setAdapter(mUserPostsAdapter);

        //mPresenter.onViewPrepared();
    }

    @Override
    public void onStart() {
        super.onStart();
        mUserPostsAdapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        mUserPostsAdapter.stopListening();
    }

    @Override
    public void onDestroyView() {
        mPresenter.onDetach();
        super.onDestroyView();
    }

    @Override
    public void onPostItemViewClick(Post post) {
        startActivity(PostDetailActivity.getStartIntent(getBaseActivity(), post.getPostId()));
    }
}
