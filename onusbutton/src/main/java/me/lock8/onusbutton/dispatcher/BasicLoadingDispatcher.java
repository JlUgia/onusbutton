package me.lock8.onusbutton.dispatcher;

import android.view.View;

/**
 * Created by joseluisugia on 07/02/16.
 */
public class BasicLoadingDispatcher extends StateDispatcher {

    @Override
    public void onSizeChanged(View loadingButton, View labelPlaceholder, View loadingIndicator) {
        reset(labelPlaceholder, loadingIndicator);
    }

    @Override
    public void dispatchLoadingStart(View loadingButton, View labelPlaceholder, View loadingIndicator) {
        labelPlaceholder.setVisibility(View.INVISIBLE);
        loadingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public void dispatchLoadingStop(View loadingButton, View labelPlaceholder, View loadingIndicator) {
        reset(labelPlaceholder, loadingIndicator);
    }

    private void reset(View labelPlaceholder, View loadingIndicator) {
        labelPlaceholder.setVisibility(View.VISIBLE);
        loadingIndicator.setVisibility(View.INVISIBLE);
    }
}
