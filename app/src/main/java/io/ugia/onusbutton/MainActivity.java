package io.ugia.onusbutton;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private OnusButton basicButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void buttonClicked(View view) {

        switch (view.getId()) {

        case R.id.am_basicButton:
            startFaceLoadingForButton((OnusButton) view);
        }

    }

    private void startFaceLoadingForButton(final OnusButton button) {

        button.setLoading(true);
        button.postDelayed(new Runnable() {
            @Override
            public void run() {
                button.setLoading(false);
            }
        }, 3000);
    }
}
