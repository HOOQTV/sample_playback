package tv.hooq.sampleapp.views.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tv.hooq.sampleapp.R;
import tv.hooq.sampleapp.commons.SharedPreferencesManager;
import tv.hooq.sampleapp.networks.NetworkAgent;
import tv.hooq.sampleapp.networks.responses.signup.SignupResponse;

public class LoginActivity extends BaseActivity {
    @BindView(R.id.layout_loading)
    LinearLayout mLoadingLayout;

    @BindView(R.id.button_signin)
    Button mSigninButton;

    @BindView(R.id.edittext_email)
    EditText mEmailEditText;

    @OnClick(R.id.button_signin)
    void signinButtonOnClick() {
        if (isValidEmail()) {
            doSignin();
        } else {
            setLoading(false);
            showToast("Email is invalid");
        }
    }

    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        SharedPreferencesManager.getInstance().setSignupResponse(null);
        setLoading(false);
        setTitle("Sign in");
    }

    private boolean isValidEmail() {
        if (getEmail().length() == 0) return false;
        if (!getEmail().contains("@")) return false;
        return true;
    }

    private String getEmail() {
        String email = mEmailEditText.getText().toString();
        return email;
    }

    private void setLoading(boolean isShow) {
        mSigninButton.setVisibility(isShow ? View.GONE : View.VISIBLE);
        mLoadingLayout.setVisibility(isShow ? View.VISIBLE : View.GONE);
        mEmailEditText.setEnabled(!isShow);
    }

    private void doSignin() {
        setLoading(true);
        NetworkAgent.getInstance().signup(getEmail()).enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                if(response.isSuccessful()) {
                    SignupResponse signupResponse = response.body();
                    SharedPreferencesManager.getInstance().setSignupResponse(signupResponse);
                    gotoMainPage();
                }
                setLoading(false);
            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable throwable) {
                setLoading(false);
            }
        });
    }

    private void gotoMainPage() {
        Intent intent = new Intent(mContext, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
