package one.noatech.onusbutton.dispatcher;

import android.view.View;

/**
 * Created by joseluisugia on 16/03/15.
 */
public abstract class StateDispatcher {

    /**
     * This method is executed when the layer finish measurement and detected a change on its size. Use this method
     * to set member values so that they do not need to be calculated on every iteration of the animation.
     * Especially those depending on the dimensions of your components. You can also use this method to set the
     * initial state of the text label and loading indicator prior to executing any animation.
     *
     * @param loadingButton    The button itself.
     * @param labelPlaceholder A reference to the placeholder text.
     * @param loadingIndicator The loading indicator.
     */
    public abstract void onSizeChanged(View loadingButton, View labelPlaceholder, View loadingIndicator);

    /**
     * This method is called when the loading mode is triggered. At this point you can modify and/or animate the
     * exposed views accordingly in order to reflect some loading state to the user.
     *
     * @param loadingButton    The button itself.
     * @param labelPlaceholder A reference to the placeholder text.
     * @param loadingIndicator The loading indicator.
     */
    public abstract void dispatchLoadingStart(View loadingButton, View labelPlaceholder, View loadingIndicator);

    /**
     * This method is called when the loading mode is finished and the button is supposed to go back to its
     * initial state. Similarly, you can use this method to reestablish the original appearance of your views to
     * reflect the default state.
     *
     * @param loadingButton    The button itself.
     * @param labelPlaceholder A reference to the placeholder text.
     * @param loadingIndicator The loading indicator.
     */
    public abstract void dispatchLoadingStop(View loadingButton, View labelPlaceholder, View loadingIndicator);
}
