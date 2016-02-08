package me.lock8.onusbutton.dispatcher;

import android.view.Gravity;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.FrameLayout;

/**
 * Created by joseluisugia on 07/02/16.
 */
public class SlideLoadingDispatcher extends StateDispatcher {

    /**
     * {@link LOADING_LOCATION_LEFT} makes the loading indicator appear from the right side of the button. The rest
     * of the content moves to the left.
     */
    public static final int LOADING_LOCATION_START = -1;

    /**
     * {@link LOADING_LOCATION_TOP} makes the loading indicator appear from the top of the button. The rest of
     * the content moves below.
     */
    public static final int LOADING_LOCATION_TOP = -2;

    /**
     * {@link LOADING_LOCATION_RIGHT} makes the loading indicator appear from the left side of the button. The rest
     * of the content moves to the right.
     */
    public static final int LOADING_LOCATION_END = -3;

    /**
     * {@link LOADING_LOCATION_BOTTOM} makes the loading indicator appear from the top of the button. The rest of
     * the content moves up.
     */
    public static final int LOADING_LOCATION_BOTTOM = -4;

    private final int loadingLocation;
    private int loadingIndicatorGravity;
    private int loadingIndicatorMargin;

    public SlideLoadingDispatcher() {
        this(LOADING_LOCATION_BOTTOM);
    }

    public SlideLoadingDispatcher(int loadingLocation) {
        this.loadingLocation = loadingLocation;
        initializeProgressBarLocation();
    }

    private void initializeProgressBarLocation() {

        switch (loadingLocation) {

        case LOADING_LOCATION_BOTTOM:
        case LOADING_LOCATION_TOP:
            loadingIndicatorGravity = Gravity.CENTER;
            break;

        case LOADING_LOCATION_START:
            loadingIndicatorGravity = Gravity.CENTER_VERTICAL | Gravity.START;
            break;

        case LOADING_LOCATION_END:
            loadingIndicatorGravity = Gravity.CENTER_VERTICAL | Gravity.END;
            break;
        }
    }

    @Override
    public void onSizeChanged(View loadingButton, View labelPlaceholder, View loadingIndicator) {

        int buttonHeight = loadingButton.getMeasuredHeight();
        int loadingIndicatorWidth = loadingIndicator.getMeasuredWidth();

        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) loadingIndicator.getLayoutParams();
        layoutParams.gravity = loadingIndicatorGravity;

        // Calculate the initial resting X position of the progress bar
        loadingIndicatorMargin = (buttonHeight - loadingIndicatorWidth) / 2;

        switch (loadingLocation) {

        case LOADING_LOCATION_BOTTOM:
            loadingIndicator.setTranslationY(buttonHeight);
            break;

        case LOADING_LOCATION_TOP:
            loadingIndicator.setTranslationY(-buttonHeight);
            break;

        case LOADING_LOCATION_START:
            layoutParams.setMarginStart(loadingIndicatorMargin);
            loadingIndicator.setTranslationX(-loadingIndicatorWidth - loadingIndicatorMargin);
            break;

        case LOADING_LOCATION_END:
            layoutParams.setMarginEnd(loadingIndicatorMargin);
            loadingIndicator.setTranslationX(loadingIndicatorWidth + loadingIndicatorMargin);
            break;
        }

        loadingIndicator.setLayoutParams(layoutParams);

    }

    @Override
    public void dispatchLoadingStart(View loadingButton, View labelPlaceholder, View loadingIndicator) {

        int buttonHeight = loadingButton.getMeasuredHeight();
        ViewPropertyAnimator loadingIndicatorAnimator = loadingIndicator.animate();
        ViewPropertyAnimator labelPlaceholderAnimator = labelPlaceholder.animate();

        switch (loadingLocation) {

        case LOADING_LOCATION_BOTTOM:
            loadingIndicatorAnimator.translationY(0);
            labelPlaceholderAnimator.translationY(-buttonHeight);
            break;

        case LOADING_LOCATION_TOP:
            loadingIndicatorAnimator.translationY(0);
            labelPlaceholderAnimator.translationY(buttonHeight);
            break;

        case LOADING_LOCATION_START:
            loadingIndicatorAnimator.translationX(0);
            labelPlaceholderAnimator.translationX(loadingIndicator.getMeasuredWidth() + loadingIndicatorMargin);
            break;

        case LOADING_LOCATION_END:
            loadingIndicatorAnimator.translationX(0);
            labelPlaceholderAnimator.translationX(-loadingIndicator.getMeasuredWidth() - loadingIndicatorMargin);
            break;
        }
    }

    @Override
    public void dispatchLoadingStop(View loadingButton, View labelPlaceholder, View loadingIndicator) {

        int buttonHeight = loadingButton.getMeasuredHeight();
        ViewPropertyAnimator loadingIndicatorAnimator = loadingIndicator.animate();
        ViewPropertyAnimator labelPlaceholderAnimator = labelPlaceholder.animate();

        switch (loadingLocation) {

        case LOADING_LOCATION_BOTTOM:
            loadingIndicatorAnimator.translationY(buttonHeight);
            labelPlaceholderAnimator.translationY(0);
            break;

        case LOADING_LOCATION_TOP:
            loadingIndicatorAnimator.translationY(-buttonHeight);
            labelPlaceholderAnimator.translationY(0);
            break;

        case LOADING_LOCATION_START:
            loadingIndicatorAnimator.translationX(-loadingButton.getMeasuredWidth() - loadingIndicatorMargin);
            labelPlaceholderAnimator.translationX(0);
            break;

        case LOADING_LOCATION_END:
            loadingIndicatorAnimator.translationX(loadingButton.getMeasuredWidth() * loadingIndicatorMargin);
            labelPlaceholderAnimator.translationX(0);
            break;
        }
    }
}
