package one.noa.onusbutton;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.content.res.AppCompatResources;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import one.noa.library.R;
import one.noa.onusbutton.dispatcher.BasicLoadingDispatcher;
import one.noa.onusbutton.dispatcher.StateDispatcher;

/**
 * Created by joseluisugia on 01/04/15.
 */
public class OnusButton extends FrameLayout {

    private static final int INVALID_VALUE = -1;

    private TextView labelPlaceholder;
    private ProgressBar progressBar;

    private boolean isLoading;
    private StateDispatcher loadingDispatcher = new BasicLoadingDispatcher();

    public OnusButton(Context context) {
        this(context, null);
    }

    public OnusButton(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.buttonStyle);
        initAttrs(context, attrs);
    }

    public OnusButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeViews(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    private void initializeViews(Context context, AttributeSet attrs, int defStyleAttr) {

        // Avoid padding on container, it happens on label
        setClipToPadding(false);
        setPadding(0, 0, 0, 0);

        initializePlaceholderButton(context, attrs, defStyleAttr);
        initializeProgressBar(context, attrs, defStyleAttr);
    }

    private void initializeProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {

        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams
                .WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;

        int defaultStyleAttr = defStyleAttr == INVALID_VALUE ? android.R.attr.progressBarStyleSmall : defStyleAttr;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.OnusButton, defaultStyleAttr, 0);

        Drawable progressBarDrawable = typedArray.getDrawable(R.styleable.OnusButton_loadingDrawable);

        int defaultProgressBarColor = labelPlaceholder.getCurrentTextColor();
        int progressBarColor = typedArray.getColor(R.styleable.OnusButton_loadingDrawableColor, defaultProgressBarColor);

        progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleSmallInverse);

        if (progressBarDrawable != null) {
            progressBarDrawable.setColorFilter(progressBarColor, PorterDuff.Mode.SRC_IN);
            progressBar.setIndeterminateDrawable(progressBarDrawable);

        } else {
            int defaultColor = getResources().getColor(android.R.color.white);
            int color = progressBarColor != INVALID_VALUE ? progressBarColor : defaultColor;
            progressBar.getIndeterminateDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        }

        progressBar.setIndeterminate(true);
        addView(progressBar, layoutParams);
    }

    private void adjustProgressBarOnSizeChanged(int w, int h, int oldw, int oldh) {

        int progressBarWidth = progressBar.getMeasuredWidth();
        int derivedMargin = (h - progressBar.getMeasuredWidth()) / 2;
        int defaultMargin = Math.max(progressBarWidth, Math.min(progressBarWidth * 2, derivedMargin));

        LayoutParams layoutParams = (LayoutParams) progressBar.getLayoutParams();
        layoutParams.leftMargin = layoutParams.rightMargin = defaultMargin;
        progressBar.setLayoutParams(layoutParams);
    }

    private void initializePlaceholderButton(Context context, AttributeSet attrs, int defStyleAttr) {

        labelPlaceholder = new TextView(context, attrs, defStyleAttr);

        // Ignore global properties (boundaries, background)
        labelPlaceholder.setBackground(null);
        labelPlaceholder.setClickable(false);

        // Null margins on placeholder to avoid inheriting from parent. Margins are applied to container
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(0, 0, 0, 0);

        addView(labelPlaceholder, layoutParams);
    }

    void initAttrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray attributeArray = context.obtainStyledAttributes(attrs, R.styleable.OnusButton);

            Drawable drawableLeft = null;
            Drawable drawableRight = null;
            Drawable drawableBottom = null;
            Drawable drawableTop = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                drawableLeft = attributeArray.getDrawable(R.styleable.OnusButton_drawableLeftCompat);
                drawableRight = attributeArray.getDrawable(R.styleable.OnusButton_drawableRightCompat);
                drawableBottom = attributeArray.getDrawable(R.styleable.OnusButton_drawableBottomCompat);
                drawableTop = attributeArray.getDrawable(R.styleable.OnusButton_drawableTopCompat);
            } else {
                final int drawableLeftId = attributeArray.getResourceId(R.styleable.OnusButton_drawableLeftCompat, -1);
                final int drawableRightId = attributeArray.getResourceId(R.styleable.OnusButton_drawableRightCompat, -1);
                final int drawableBottomId = attributeArray.getResourceId(R.styleable.OnusButton_drawableBottomCompat, -1);
                final int drawableTopId = attributeArray.getResourceId(R.styleable.OnusButton_drawableTopCompat, -1);

                if (drawableLeftId != -1)
                    drawableLeft = AppCompatResources.getDrawable(context, drawableLeftId);
                if (drawableRightId != -1)
                    drawableRight = AppCompatResources.getDrawable(context, drawableRightId);
                if (drawableBottomId != -1)
                    drawableBottom = AppCompatResources.getDrawable(context, drawableBottomId);
                if (drawableTopId != -1)
                    drawableTop = AppCompatResources.getDrawable(context, drawableTopId);
            }
            setCompoundDrawablesWithIntrinsicBounds(drawableLeft, drawableTop, drawableRight, drawableBottom);
            attributeArray.recycle();
        }
    }

    public void setLoadingDispatcher(StateDispatcher loadingDispatcher) {
        this.loadingDispatcher = loadingDispatcher;
    }

    public void setLoading(boolean loading) {

        isLoading = loading;
        setClickable(!isLoading);

        if (loading) {
            startAnimation();
        } else {
            stopAnimation();
        }
    }

    public boolean isLoading() {
        return isLoading;
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);

        if (visibility == VISIBLE) {
            if (isLoading) {
                startAnimation();
            } else {
                stopAnimation();
            }
        }
    }

    private void startAnimation() {

        if (getVisibility() != VISIBLE) {
            return;
        }

        loadingDispatcher.dispatchLoadingStart(this, labelPlaceholder, progressBar);
    }

    private void stopAnimation() {

        if (getVisibility() != VISIBLE) {
            return;
        }

        loadingDispatcher.dispatchLoadingStop(this, labelPlaceholder, progressBar);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        adjustProgressBarOnSizeChanged(w, h, oldw, oldh);

        loadingDispatcher.onSizeChanged(this, labelPlaceholder, progressBar);
    }

    //--
    // TextView Inherited Methods
    //------------------------------
    public TextView getTextLabel() {
        return labelPlaceholder;
    }

    public void setText(CharSequence text) {
        labelPlaceholder.setText(text);
    }

    public CharSequence getText() {
        return labelPlaceholder.getText();
    }

    public int length() {
        return labelPlaceholder.length();
    }

    public void setTypeface(Typeface tf, int style) {
        labelPlaceholder.setTypeface(tf, style);
    }

    public void setCompoundDrawablesWithIntrinsicBounds(int left, int top, int right, int bottom) {
        labelPlaceholder.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
    }

    public void setCompoundDrawablesWithIntrinsicBounds(Drawable left, Drawable top, Drawable right, Drawable
            bottom) {
        labelPlaceholder.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
    }

    //--
    // ProgressBar Inherited Methods
    //------------------------------
    public ProgressBar getProgressBar() {
        return progressBar;
    }
}
