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

package eu.balticit.android.europewelcome.ui.posts.buypremium;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import eu.balticit.android.europewelcome.R;
import eu.balticit.android.europewelcome.di.component.ActivityComponent;
import eu.balticit.android.europewelcome.ui.auth.LoginActivity;
import eu.balticit.android.europewelcome.ui.base.BaseDialog;

public class BuyPremiumDialog extends BaseDialog implements BuyPremiumDialogMvpView {

    public static final String TAG = "BuyPremiumDialog";

    private Callback mCallback;

    @Inject
    BuyPremiumDialogMvpPresenter<BuyPremiumDialogMvpView> mPresenter;

    public static BuyPremiumDialog newInstance() {
        BuyPremiumDialog fragment = new BuyPremiumDialog();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_buy_premium, container, false);

        ActivityComponent component = getActivityComponent();
        if (component != null) {

            component.inject(this);

            setUnBinder(ButterKnife.bind(this, view));

            mPresenter.onAttach(this);
        }

        return view;
    }

    public void show(FragmentManager fragmentManager, Callback callback) {
        mCallback = callback;
        super.show(fragmentManager, TAG);
    }

    @Override
    protected void setUp(View view) {

    }

    @OnClick(R.id.btn_buy_premium_get_premium)
    void onGetPremiumBtnClick(){
        mPresenter.onGetPremiumClick();
    }

    @OnClick(R.id.tv_buy_premium_terms)
    void onTermsTvClick(){
        dismissDialog();
        getBaseActivity().showAboutFragment();
    }

    @Override
    public void openLoginActivity() {
        startActivity(LoginActivity.getStartIntent(getBaseActivity()));
    }

    @Override
    public void purchasePremium() {
        if(mCallback != null) mCallback.purchasePremiumClick();
    }

    @Override
    public void dismissDialog() {
        super.dismissDialog(TAG);
    }

    @Override
    public void onDestroyView() {
        mPresenter.onDetach();
        super.onDestroyView();
    }

    public interface Callback {
        void purchasePremiumClick();
    }
}
