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

package eu.balticit.android.europewelcome.ui.profile.changeprofile;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import eu.balticit.android.europewelcome.R;
import eu.balticit.android.europewelcome.di.component.ActivityComponent;
import eu.balticit.android.europewelcome.di.module.GlideApp;
import eu.balticit.android.europewelcome.ui.base.BaseFragment;
import eu.balticit.android.europewelcome.ui.profile.ProfileActivity;
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

public class ChangeProfileFragment extends BaseFragment implements ChangeProfileMvpView {

    public static final String TAG = "ChangeProfileFragment";

    private static final int RC_CHOOSE_PHOTO = 8999;
    private static final int RC_IMAGE_PERMS = 8998;
    private static final String PERMS = Manifest.permission.READ_EXTERNAL_STORAGE;

    @Inject
    ChangeProfileMvpPresenter<ChangeProfileMvpView> mPresenter;

    @BindView(R.id.iv_change_profile_add_photo)
    ImageView mPhotoIv;

    @BindView(R.id.tv_change_profile_name)
    TextView mNameTv;

    @BindView(R.id.tv_change_profile_email)
    TextView mEmailTv;

    @BindView(R.id.et_change_profile_birth_date)
    EditText mBirthDateEt;

    @BindView(R.id.rb_change_profile_male)
    RadioButton mGenderMaleRb;

    @BindView(R.id.rb_change_profile_female)
    RadioButton mGenderFemaleRb;

    @BindView(R.id.rb_change_profile_other)
    RadioButton mGenderOtherRb;

    private String mGender;

    Calendar myCalendar;

    DatePickerDialog.OnDateSetListener date;

    private Callback mCallback;

    public static ChangeProfileFragment newInstance() {
        Bundle args = new Bundle();
        ChangeProfileFragment fragment = new ChangeProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_profile, container, false);

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

        mPresenter.loadCurrentUserData();

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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (Callback) context;
        } catch (ClassCastException e) {
            Log.d(TAG, "onAttach: " + e.getMessage());
        }
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

    @Override
    public void loadUserName(String name) {
        mNameTv.setText(name);
    }

    @Override
    public void loadUserEmail(String email) {
        mEmailTv.setText(email);
    }

    @Override
    public void loadUserBirthDate(String date) {
        mBirthDateEt.setText(date);
    }

    @OnClick(R.id.iv_change_profile_add_photo)
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

    @Override
    public void loadUserGender(String gender) {
        switch (gender) {
            case "Male":
                mGenderMaleRb.setChecked(true);
                mGender = "Male";
                break;
            case "Female":
                mGenderFemaleRb.setChecked(true);
                mGender = "Female";
                break;
            default:
                mGenderOtherRb.setChecked(true);
                mGender = "Not specified";
                break;
        }

    }

    @Override
    public void detachFragment() {
        getBaseActivity().onFragmentDetached(TAG);
        if(mCallback != null){
            mCallback.updateUserData();
        }
    }

    @OnClick({R.id.rb_change_profile_male, R.id.rb_change_profile_female, R.id.rb_change_profile_other})
    public void onRadioButtonClicked(RadioButton radioButton) {
        boolean checked = radioButton.isChecked();
        switch (radioButton.getId()) {
            case R.id.rb_change_profile_male:
                if (checked) {
                    mGender = "Male";
                }
                break;
            case R.id.rb_change_profile_female:
                if (checked) {
                    mGender = "Female";
                }
                break;
            case R.id.rb_change_profile_other:
                if (checked) {
                    mGender = "Not specified";
                }
                break;
        }
    }

    @OnClick(R.id.et_change_profile_birth_date)
    void onBirthDateEtClick() {
        new DatePickerDialog(getBaseActivity(), date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateLabel() {
        String myFormat = "d MMM yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mBirthDateEt.setText(sdf.format(myCalendar.getTime()));
    }

    @OnClick(R.id.btn_change_profile_register)
    void onSaveChangesButtonClick() {
        mPresenter.onSaveChangesButtonClick(
                mPresenter.getUserPhotoUrl(),
                mGender,
                mBirthDateEt.getText().toString());
    }


    /**
     * Callback for {@link ProfileActivity}
     */
    public interface Callback {

        void updateUserData();

    }

}
