package tv.hooq.sampleapp.views.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import tv.hooq.sampleapp.R;
import tv.hooq.sampleapp.commons.SharedPreferencesManager;

public class SplashActivity extends BaseActivity {

    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        goToNextPage();
    }

    private void goToNextPage() {
        Intent intent = null;
        if(SharedPreferencesManager.getInstance().isLogged()) {
            intent = new Intent(mContext, MainActivity.class);
        } else {
            intent = new Intent(mContext, LoginActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
