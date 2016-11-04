package one.noatech.onusbutton;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import one.noatech.onusbutton.dispatcher.SlideLoadingDispatcher;

public class MainActivity extends AppCompatActivity {

    private OnusButton slideButton;
    private OnusButton slideLeftButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        bindViews();
    }

    private void bindViews() {

        slideButton = (OnusButton) findViewById(R.id.am_slideButton);
        slideButton.setLoadingDispatcher(new SlideLoadingDispatcher());

        slideLeftButton = (OnusButton) findViewById(R.id.am_slideLeftButton);
        slideLeftButton.setLoadingDispatcher(
                new SlideLoadingDispatcher(SlideLoadingDispatcher.LOADING_LOCATION_END));
    }

    public void buttonClicked(View view) {

        switch (view.getId()) {

        case R.id.am_basicButton:
        case R.id.am_slideButton:
        case R.id.am_slideLeftButton:
            startFakeLoadingForButton((OnusButton) view);
        }

    }

    private void startFakeLoadingForButton(final OnusButton button) {

        button.setLoading(true);
        button.postDelayed(new Runnable() {
            @Override
            public void run() {
                button.setLoading(false);
            }
        }, 3000);
    }
}
