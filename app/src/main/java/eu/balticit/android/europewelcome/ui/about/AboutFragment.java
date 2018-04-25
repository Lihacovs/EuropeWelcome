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

package eu.balticit.android.europewelcome.ui.about;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import eu.balticit.android.europewelcome.BuildConfig;
import eu.balticit.android.europewelcome.R;
import eu.balticit.android.europewelcome.di.component.ActivityComponent;
import eu.balticit.android.europewelcome.ui.base.BaseFragment;
import eu.balticit.android.europewelcome.ui.intro.IntroActivity;
import eu.balticit.android.europewelcome.utils.AppUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eu.balticit.android.europewelcome.utils.AppUtils;

/**
 * About Fragment
 */

public class AboutFragment extends BaseFragment implements AboutMvpView {

    public static final String TAG = "AboutFragment";

    @Inject
    AboutMvpPresenter<AboutMvpView> mPresenter;

    @BindView(R.id.tc_about_app_version)
    TextView mAppVersionTv;

    public static AboutFragment newInstance() {
        Bundle args = new Bundle();
        AboutFragment fragment = new AboutFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        ActivityComponent component = getActivityComponent();
        if (component != null) {
            component.inject(this);
            setUnBinder(ButterKnife.bind(this, view));
            mPresenter.onAttach(this);
        }

        return view;
    }

    //Opens app introduction screens
    @OnClick(R.id.iv_about_link_functions)
    void onAboutAppClick(){
        mPresenter.resetIntroWatch();
        Intent intent = new Intent(getBaseActivity(), IntroActivity.class);
        startActivity(intent);
    }

    //Opens email to send to developer
    @OnClick(R.id.iv_about_send_email)
    void onSendEmailClick(){
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", getString(R.string.app_email), null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject));
        emailIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.email_body));
        startActivity(Intent.createChooser(emailIntent,
                getString(R.string.email_chooser_title)));
    }

    //Opens GooglePlay to rate app
    @OnClick(R.id.iv_about_send_rate)
    void onRateAppClick(){
        AppUtils.openPlayStoreForApp(getBaseActivity());
    }

    @Override
    protected void setUp(View view) {
        String appVersion = "( Version " + BuildConfig.VERSION_NAME + " )";
        mAppVersionTv.setText(appVersion);
    }

    @OnClick(R.id.nav_back_btn)
    void onNavBackClick() {
        getBaseActivity().onFragmentDetached(TAG);
    }

    @Override
    public void onDestroyView() {
        mPresenter.onDetach();
        super.onDestroyView();
    }
}
