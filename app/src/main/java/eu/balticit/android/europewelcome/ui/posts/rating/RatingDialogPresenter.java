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

package eu.balticit.android.europewelcome.ui.posts.rating;

import android.support.annotation.NonNull;

import eu.balticit.android.europewelcome.R;
import eu.balticit.android.europewelcome.data.DataManager;
import eu.balticit.android.europewelcome.data.firebase.model.Rating;
import eu.balticit.android.europewelcome.ui.base.BasePresenter;
import eu.balticit.android.europewelcome.utils.CommonUtils;

import javax.inject.Inject;

import eu.balticit.android.europewelcome.data.DataManager;
import eu.balticit.android.europewelcome.data.firebase.model.Rating;

/**
 * Rating dialog presenter
 */

public class RatingDialogPresenter<V extends RatingDialogMvpView> extends BasePresenter<V>
        implements RatingDialogMvpPresenter<V> {

    public static final String TAG = "RatingDialogPresenter";

    private boolean isRatingSecondaryActionShown = false;

    @Inject
    RatingDialogPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void onRatingSubmitted(final float rating, String message) {

        if (rating == 0) {
            getMvpView().onError(R.string.rating_not_provided_error);
            return;
        }

        if (!isRatingSecondaryActionShown) {
            if (rating == 5) {
                getMvpView().showPlayStoreRatingView();
                getMvpView().hideSubmitButton();
                getMvpView().disableRatingStars();
                if (checkUserSigned()) {
                    sendRatingDataToServerInBackground(5, "5 stars");
                }
            } else {
                getMvpView().showRatingMessageView();
            }
            isRatingSecondaryActionShown = true;
            return;
        }

        getMvpView().showLoading();
        if (checkUserSigned()) {
            sendRatingDataToServerInBackground(rating, message);
        }

        getMvpView().hideLoading();
        getMvpView().hideKeyboard();
        getMvpView().showMessage(R.string.rating_thanks);
        getMvpView().dismissDialog();

    }

    @Override
    public void onCancelClicked() {
        getMvpView().dismissDialog();
    }

    @Override
    public void onLaterClicked() {
        getMvpView().dismissDialog();
    }

    @Override
    public void onPlayStoreRatingClicked() {
        getMvpView().openPlayStoreForRating();
        getMvpView().dismissDialog();
    }

    private void sendRatingDataToServerInBackground(float rating, String message) {
        Rating userRating = newRating(rating, message);
        getDataManager().saveRating(userRating);
    }

    private boolean checkUserSigned() {
        return getDataManager().getCurrentUserId() != null && !getDataManager().getCurrentUserId().isEmpty();
    }

    @NonNull
    private Rating newRating(float rating, String message){
        return new Rating(getDataManager().getCurrentUserId(),
                getDataManager().getCurrentUserName(),
                getDataManager().getCurrentUserEmail(),
                rating,
                message,
                CommonUtils.getCurrentDate(),
                CommonUtils.getTimeStampInt());
    }
}
