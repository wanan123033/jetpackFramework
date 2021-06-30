package com.jetpackframework.view.titlebar;

import android.graphics.Color;
import android.view.View;

import com.jetpackframework.R;
import com.jetpackframework.base.JetpackApplicationDelegate;

import static com.blankj.utilcode.util.ColorUtils.getColor;
import static com.blankj.utilcode.util.SizeUtils.sp2px;

public class TitleBarBuilder {
    private String leftText;
    private int leftImageResource;
    private int rightImageResource;
    private String rightText;
    private CustomNavigatorBar.OnCustomClickListener onCustomClickListener;
    private float leftTextSize;
    private int leftTextColor;
    private float rightTextSize;
    private int rightTextColor;
    private String title;
    private float titleTextSize;
    private int titleTextColor;
    private int leftTextVisibility = View.VISIBLE;
    private int leftImageVisibility = View.GONE;
    private int rightImageVisibility = View.GONE;
    private int rightTextVisibility = View.VISIBLE;
    private int titleVisibility;
    private int backgroundColor;
    private View.OnClickListener leftImageOnClickListener;
    private View.OnClickListener leftTextOnClickListener;
    private View.OnClickListener rightTextOnClickListener;
    private View.OnClickListener rightImageOnClickListener;

    public String getLeftText() {
        return leftText;
    }

    public TitleBarBuilder setLeftText(String leftText) {
        this.leftText = leftText;
        return this;
    }

    public int getLeftImageResource() {
        return leftImageResource;
    }

    public TitleBarBuilder setLeftImageResource(int leftImageResource) {
        this.leftImageResource = leftImageResource;
        return this;
    }

    public int getRightImageResource() {
        return rightImageResource;
    }

    public TitleBarBuilder setRightImageResource(int rightImageResource) {
        this.rightImageResource = rightImageResource;
        return this;
    }

    public String getRightText() {
        return rightText;
    }

    public TitleBarBuilder setRightText(String rightText) {
        this.rightText = rightText;
        return this;
    }

    public CustomNavigatorBar.OnCustomClickListener getOnCustomClickListener() {
        return onCustomClickListener;
    }

    public TitleBarBuilder setOnCustomClickListener(CustomNavigatorBar.OnCustomClickListener onCustomClickListener) {
        this.onCustomClickListener = onCustomClickListener;
        return this;
    }

    public float getLeftTextSize() {
        return leftTextSize == 0.0f ? sp2px(16.0f) : leftTextSize;
    }

    public TitleBarBuilder setLeftTextSize(float leftTextSize) {
        this.leftTextSize = leftTextSize;
        return this;
    }

    public int getLeftTextColor() {
        return leftTextColor == 0 ? Color.WHITE : leftTextColor;
    }

    public TitleBarBuilder setLeftTextColor(int leftTextColor) {
        this.leftTextColor = leftTextColor;
        return this;
    }

    public float getRightTextSize() {
        return rightTextSize == 0 ? sp2px(16) : rightTextSize;
    }

    public TitleBarBuilder setRightTextSize(float rightTextSize) {
        this.rightTextSize = rightTextSize;
        return this;
    }

    public int getRightTextColor() {
        return rightTextColor == 0 ? Color.WHITE : rightTextColor;
    }

    public TitleBarBuilder setRightTextColor(int rightTextColor) {
        this.rightTextColor = rightTextColor;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public TitleBarBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public float getTitleTextSize() {
        return titleTextSize == 0 ? sp2px(18) : titleTextSize;
    }

    public TitleBarBuilder setTitleTextSize(float titleTextSize) {
        this.titleTextSize = titleTextSize;
        return this;
    }

    public int getTitleTextColor() {
        return titleTextColor == 0 ? Color.WHITE : titleTextColor;
    }

    public TitleBarBuilder setTitleTextColor(int titleTextColor) {
        this.titleTextColor = titleTextColor;
        return this;
    }

    public int getLeftTextVisibility() {
        return leftTextVisibility == 0 ? View.VISIBLE : leftTextVisibility;
    }

    public TitleBarBuilder setLeftTextVisibility(int leftTextVisibility) {
        this.leftTextVisibility = leftTextVisibility;
        return this;
    }

    public int getLeftImageVisibility() {
        return leftImageVisibility == 0 ? View.VISIBLE : leftImageVisibility;
    }

    public TitleBarBuilder setLeftImageVisibility(int leftImageVisibility) {
        this.leftImageVisibility = leftImageVisibility;
        return this;
    }

    public int getRightImageVisibility() {
        return rightImageVisibility == 0 ? View.VISIBLE : rightImageVisibility;
    }

    public TitleBarBuilder setRightImageVisibility(int rightImageVisibility) {
        this.rightImageVisibility = rightImageVisibility;
        return this;
    }

    public int getRightTextVisibility() {
        return rightTextVisibility == 0 ? View.VISIBLE : rightTextVisibility;
    }

    public TitleBarBuilder setRightTextVisibility(int rightTextVisibility) {
        this.rightTextVisibility = rightTextVisibility;
        return this;
    }

    public int getTitleVisibility() {
        return titleVisibility == 0 ? View.VISIBLE : titleVisibility;
    }

    public TitleBarBuilder setTitleVisibility(int titleVisibility) {
        this.titleVisibility = titleVisibility;
        return this;
    }

    public int getBackgroundColor() {
        return backgroundColor == 0 ? getColor(R.color.blue_btn_bg_color) : backgroundColor;
    }

    public TitleBarBuilder setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public View.OnClickListener getLeftImageOnClickListener() {
        return leftImageOnClickListener;
    }

    public View.OnClickListener getLeftTextOnClickListener() {
        return leftTextOnClickListener;
    }

    public View.OnClickListener getRightTextOnClickListener() {
        return rightTextOnClickListener;
    }

    public View.OnClickListener getRightImageOnClickListener() {
        return rightImageOnClickListener;
    }

    public TitleBarBuilder setLeftImageOnClickListener(View.OnClickListener leftImageOnClickListener) {
        this.leftImageOnClickListener = leftImageOnClickListener;
        return this;
    }

    public TitleBarBuilder setLeftTextOnClickListener(View.OnClickListener leftTextOnClickListener) {
        this.leftTextOnClickListener = leftTextOnClickListener;
        return this;
    }

    public TitleBarBuilder setRightTextOnClickListener(View.OnClickListener rightTextOnClickListener) {
        this.rightTextOnClickListener = rightTextOnClickListener;
        return this;
    }

    public TitleBarBuilder setRightImageOnClickListener(View.OnClickListener rightImageOnClickListener) {
        this.rightImageOnClickListener = rightImageOnClickListener;
        return this;
    }
}
