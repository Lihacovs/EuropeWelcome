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

package com.bmd.android.europewelcome.ui.auth.register;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bmd.android.europewelcome.R;
import com.bmd.android.europewelcome.di.component.ActivityComponent;
import com.bmd.android.europewelcome.di.module.GlideApp;
import com.bmd.android.europewelcome.ui.base.BaseFragment;
import com.bmd.android.europewelcome.ui.posts.PostsActivity;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static android.app.Activity.RESULT_OK;

/**
 * Register fragment. Creates user account in Firebase server.
 */

public class RegisterFragment extends BaseFragment implements RegisterMvpView {

    public static final String TAG = "RegisterFragment";
    private static final int RC_CHOOSE_PHOTO = 9999;
    private static final int RC_IMAGE_PERMS = 9998;
    private static final String PERMS = Manifest.permission.READ_EXTERNAL_STORAGE;

    @Inject
    RegisterMvpPresenter<RegisterMvpView> mPresenter;

    @BindView(R.id.et_register_email)
    EditText mEmailEt;

    @BindView(R.id.et_register_password)
    EditText mPasswordEt;

    @BindView(R.id.iv_register_add_photo)
    ImageView mPhotoIv;

    @BindView(R.id.et_register_name)
    EditText mNameEt;

    @BindView(R.id.et_register_surname)
    EditText mSurnameEt;

    @BindView(R.id.et_register_birth_date)
    EditText mBirthDateEt;

    String mGender = "Not specified";

    Calendar myCalendar;

    DatePickerDialog.OnDateSetListener date;

    public static RegisterFragment newInstance() {
        Bundle args = new Bundle();
        RegisterFragment fragment = new RegisterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        ActivityComponent component = getActivityComponent();
        if (component != null) {
            component.inject(this);
            setUnBinder(ButterKnife.bind(this, view));
            mPresenter.onAttach(this);
        }

        return view;
    }

    @Override
    protected void setUp(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        myCalendar = Calendar.getInstance();

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
    }

    @OnClick(R.id.btn_register_back)
    void onButtonBackClick() {
        getBaseActivity().onFragmentDetached(TAG);
    }

    @OnClick(R.id.tv_register_terms)
    void onTermsTvClick(){
        getBaseActivity().showAboutFragment();
    }

    @OnClick(R.id.iv_register_add_photo)
    @AfterPermissionGranted(RC_IMAGE_PERMS)
    void onAddPhotoIvClick() {
        if (!EasyPermissions.hasPermissions(getBaseActivity(), PERMS)) {
            EasyPermissions.requestPermissions(this, getString(R.string.rational_image_perm),
                    RC_IMAGE_PERMS, PERMS);
            return;
        }

        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RC_CHOOSE_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_CHOOSE_PHOTO) {
            if (resultCode == RESULT_OK) {
                Uri selectedImage = data.getData();
                mPresenter.uploadFileToStorage(selectedImage, getBaseActivity());
            }
        } else if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE
                && EasyPermissions.hasPermissions(getBaseActivity(), PERMS)) {
            onAddPhotoIvClick();
        }
    }

    @OnClick({R.id.rb_register_male, R.id.rb_register_female, R.id.rb_register_other})
    public void onRadioButtonClicked(RadioButton radioButton) {
        boolean checked = radioButton.isChecked();
        switch (radioButton.getId()) {
            case R.id.rb_register_male:
                if (checked) {
                    mGender = "Male";
                }
                break;
            case R.id.rb_register_female:
                if (checked) {
                    mGender = "Female";
                }
                break;
            case R.id.rb_register_other:
                if (checked) {
                    mGender = "Not specified";
                }
                break;
        }
    }

    @OnClick(R.id.et_register_birth_date)
    void onBirthDateEtClick() {
        new DatePickerDialog(getBaseActivity(), date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @OnClick(R.id.btn_register_register)
    void onRegisterButtonClick() {
        mPresenter.onRegisterButtonClick(
                mEmailEt.getText().toString(),
                mPasswordEt.getText().toString(),
                mPresenter.getUserPhotoUrl(),
                mNameEt.getText().toString(),
                mSurnameEt.getText().toString(),
                mGender,
                mBirthDateEt.getText().toString());
    }

    @Override
    public void onDestroyView() {
        mPresenter.onDetach();
        super.onDestroyView();
    }

    private void updateLabel() {
        String myFormat = "d MMM yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mBirthDateEt.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void openMainActivity() {
        Intent intent = PostsActivity.getStartIntent(getBaseActivity());
        startActivity(intent);
        onDestroyView();
        getBaseActivity().finish();
    }

    @Override
    public void loadUserPhoto(String photoUrl) {
        if (mPhotoIv != null) {
            GlideApp.with(this)
                    .load(photoUrl)
                    .centerCrop()
                    .apply(RequestOptions.circleCropTransform())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(mPhotoIv);
        }
    }
}
