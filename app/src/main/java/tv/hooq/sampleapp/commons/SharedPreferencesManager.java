package tv.hooq.sampleapp.commons;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import tv.hooq.sampleapp.SampleApplication;
import tv.hooq.sampleapp.networks.responses.signup.SignupResponse;

public class SharedPreferencesManager {

    private static SharedPreferencesManager instance;

    public static SharedPreferencesManager getInstance() {
        if (instance == null) instance = new SharedPreferencesManager();
        return instance;
    }

    private Context getContext() {
        return SampleApplication.getInstance();
    }

    private SharedPreferences.Editor getEditor() {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
        return editor;
    }

    private SharedPreferences getPreference() {
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(getContext());
        return preference;
    }

    public void clear() {
        SharedPreferences.Editor editor = getEditor();
        editor.clear();
        editor.commit();
    }

    public String getSanctuaryToken() {
        SignupResponse signupResponse = getSignupResponse();
        if(signupResponse == null) {
            return null;
        }
        return signupResponse.getData().getSession().getAccessToken();
    }

    //-- SIGN IN RESPONSE
    private static final String PREF_ACCOUNT_PARAM = "PREF_ACCOUNT_PARAM";

    public void setSignupResponse(SignupResponse signupResponse) {
        String sSigninResponse = null;
        if(signupResponse != null) {
            sSigninResponse = signupResponse.toString();
        }
        SharedPreferences.Editor editor = getEditor().putString(PREF_ACCOUNT_PARAM, sSigninResponse);
        editor.commit();
    }

    public SignupResponse getSignupResponse() {
        String sAccountParam = getPreference().getString(PREF_ACCOUNT_PARAM, null);
        if(TextUtils.isEmpty(sAccountParam)) {
            return null;
        }
        return SignupResponse.parseFromString(sAccountParam);
    }

    public boolean isLogged() {
        return getSignupResponse() != null;
    }

}
