/*
 * Copyright (C) 2017. Baltic Mobile Development
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

package com.bmd.android.europewelcome.ui.addpost;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.bmd.android.europewelcome.R;
import com.bmd.android.europewelcome.data.firebase.model.PostImage;
import com.bmd.android.europewelcome.data.firebase.model.PostText;
import com.bmd.android.europewelcome.ui.base.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Konstantins on 12/7/2017.
 */

public class AddPostActivity extends BaseActivity implements AddPostMvpView {

    @Inject
    AddPostMvpPresenter<AddPostMvpView> mPresenter;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.et_addpost_posttitle)
    EditText mPostTitleEt;

    @BindView(R.id.iv_addpost_texticon)
    ImageView mTextImageIv;

    @BindView(R.id.iv_addpost_imageicon)
    ImageView mImageIconIv;

    @BindView(R.id.ll_addpost_postcontent)
    ViewGroup mPostContentLl;

    @BindView(R.id.ll_addpost_focuscontainer)
    LinearLayout mFocusContainer;

    @BindView(R.id.sv_addpost_postcontent)
    ScrollView mScrollView;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, AddPostActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        getActivityComponent().inject(this);

        setUnBinder(ButterKnife.bind(this));

        mPresenter.onAttach(this);

        setUp();
    }

    @Override
    protected void setUp() {

        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @OnClick(R.id.iv_addpost_texticon)
    void onTextIconClick(){
        attachPostTextLayout(mPostContentLl,newPostText());
        mScrollView.post(new Runnable() {
            @Override
            public void run() {
                mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    @OnClick(R.id.iv_addpost_imageicon)
    void onImageIconClick(){
        attachPostImageLayout(mPostContentLl,newPostImage());
        mScrollView.post(new Runnable() {
            @Override
            public void run() {
                mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    @OnClick(R.id.iv_addpost_locationicon)
    void onLocationIconClick(){
        showMessage("Location Image Click");
    }

    @OnClick(R.id.iv_addpost_videoicon)
    void onVideoIconClick(){
        showMessage("Video Image Click");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.addpost_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                upIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    // This activity is NOT part of this app's task, so create a new task
                    // when navigating up, with a synthesized back stack.
                    TaskStackBuilder.create(this)
                            // Add all of this activity's parents to the back stack
                            .addNextIntentWithParentStack(upIntent)
                            // Navigate up to the closest parent
                            .startActivities();
                } else {
                    // This activity is part of this app's task, so simply
                    // navigate up to the logical parent activity.
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
            case R.id.save_post:
                //removing focus will trigger PostText update in Presenter list
                mFocusContainer.requestFocus();

                mPresenter.savePost();
                this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }

    /**
     * Handles actions in new added PostImage block layout
     * @param parentLayout root parent layout
     * @param postImage PostImage to attach
     */
    private void attachPostImageLayout(final ViewGroup parentLayout, final PostImage postImage){
        //Adds PostImage to list and to layout
        mPresenter.addPostImageToList(postImage);

        View imageView = LayoutInflater.from(this)
                .inflate(R.layout.item_addpost_image, parentLayout, false);
        parentLayout.addView(imageView);

        //Removes PostImage from list and from layout
        ImageView deleteImageIv = imageView.findViewById(R.id.iv_addpostitem_deleteimage);
        deleteImageIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.removePostImageFromList(postImage);
                View parentView = (View) view.getParent();
                parentLayout.removeView(parentView);
            }
        });
    }

    /**
     * Handles actions in added PostText block layout
     * @param parentLayout root parent layout
     * @param postText PostText entity to attach
     */
    private void attachPostTextLayout(final ViewGroup parentLayout, final PostText postText){
        final EditText textEt;
        //Adds PostText to list and to layout
        mPresenter.addPostTextToList(postText);

        final View textLayout = LayoutInflater.from(this)
                .inflate(R.layout.item_addpost_text, parentLayout, false);
        parentLayout.addView(textLayout);

        //Removes PostText from list and from layout
        ImageView deleteTextIv = textLayout.findViewById(R.id.iv_addpostitem_deletetext);
        deleteTextIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.removePostTextFromList(postText);
                View parentView = (View) view.getParent();
                parentLayout.removeView(parentView);
            }
        });

        textEt = textLayout.findViewById(R.id.et_addpostitem_text);
        textEt.requestFocus();
        textEt.setTypeface(Typeface.DEFAULT);
        textEt.setText(postText.getPostText());
        //postText.setPostText(textEt.getText().toString());

        //TODO: save text to Entity on focus change
        textEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    //removing focus from textPost block will trigger post update
                    postText.setPostText(textEt.getText().toString());
                    mPresenter.updatePostTextInList(postText);
                }
            }
        });

        //TODO: add comment
        final ImageView formatBoldIv = textLayout.findViewById(R.id.iv_addpostitem_formatbold);
        formatBoldIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textEt.getTypeface().getStyle() == Typeface.BOLD) {
                    textEt.setTypeface(Typeface.DEFAULT);
                    formatBoldIv.setImageDrawable(ResourcesCompat.getDrawable(
                            getResources(), R.drawable.ic_format_bold_black_24dp, null));
                    postText.setPostTextBold(false);
                } else if (textEt.getTypeface().getStyle() == Typeface.ITALIC) {
                    textEt.setTypeface(textEt.getTypeface(), Typeface.BOLD_ITALIC);
                    formatBoldIv.setImageDrawable(ResourcesCompat.getDrawable(
                            getResources(), R.drawable.ic_format_bold_off_black_24dp, null));
                    postText.setPostTextBold(true);
                } else if (textEt.getTypeface().getStyle() == Typeface.BOLD_ITALIC) {
                    textEt.setTypeface(textEt.getTypeface(), Typeface.ITALIC);
                    formatBoldIv.setImageDrawable(ResourcesCompat.getDrawable(
                            getResources(), R.drawable.ic_format_bold_black_24dp, null));
                    postText.setPostTextBold(false);
                } else {
                    textEt.setTypeface(textEt.getTypeface(), Typeface.BOLD);
                    formatBoldIv.setImageDrawable(ResourcesCompat.getDrawable(
                            getResources(), R.drawable.ic_format_bold_off_black_24dp, null));
                    postText.setPostTextBold(true);
                }
                mPresenter.updatePostTextInList(postText);
            }

        });

        //TODO: add comment
        final ImageView formatItalicIv = textLayout.findViewById(R.id.iv_addpostitem_formatitalic);
        formatItalicIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textEt.getTypeface().getStyle() == Typeface.ITALIC) {
                    textEt.setTypeface(Typeface.DEFAULT);
                    formatItalicIv.setImageDrawable(ResourcesCompat.getDrawable(
                            getResources(), R.drawable.ic_format_italic_black_24dp, null));
                    postText.setPostTextItalic(false);
                } else if (textEt.getTypeface().getStyle() == Typeface.BOLD) {
                    textEt.setTypeface(textEt.getTypeface(), Typeface.BOLD_ITALIC);
                    formatItalicIv.setImageDrawable(ResourcesCompat.getDrawable(
                            getResources(), R.drawable.ic_format_italic_off_black_24dp, null));
                    postText.setPostTextItalic(true);
                } else if (textEt.getTypeface().getStyle() == Typeface.BOLD_ITALIC) {
                    textEt.setTypeface(textEt.getTypeface(), Typeface.ITALIC);
                    formatItalicIv.setImageDrawable(ResourcesCompat.getDrawable(
                            getResources(), R.drawable.ic_format_italic_black_24dp, null));
                    postText.setPostTextItalic(false);
                } else {
                    textEt.setTypeface(textEt.getTypeface(), Typeface.ITALIC);
                    formatItalicIv.setImageDrawable(ResourcesCompat.getDrawable(
                            getResources(), R.drawable.ic_format_italic_off_black_24dp, null));
                    postText.setPostTextItalic(true);
                }
                mPresenter.updatePostTextInList(postText);
            }
        });

        ImageView formatSizeIv = textLayout.findViewById(R.id.iv_addpostitem_formatsize);
        formatSizeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float textSizePx = textEt.getTextSize();
                float density = getResources().getDisplayMetrics().scaledDensity;
                float textSizeSp = textSizePx / density;
                showMessage(Float.toString(textSizeSp));
                if(textSizeSp <24) {
                    textEt.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSizeSp + 2);
                }else{
                    //TODO add constant to dimen file
                    textEt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                }
                postText.setPostTextSize(textEt.getTextSize()/density);
                mPresenter.updatePostTextInList(postText);
            }
        });
    }

    private PostImage newPostImage(){
        return new PostImage("www.random.com/image1"
                ,"Some image caption"
                ,"24 Oct 2017"
        );
    }

    private PostText newPostText(){
        return new PostText("This mixes stuff"
                ,14
                ,false
                ,false
                ,"24 Oct 2017"
        );
    }
}