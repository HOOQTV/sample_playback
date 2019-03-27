package tv.hooq.sampleapp.networks.requests.signup;

import com.google.gson.annotations.SerializedName;

import tv.hooq.sampleapp.commons.Config;

public class SignupMeta {
    @SerializedName("withSignIn")
    private WithSignIn withSignIn;

    @SerializedName("withActivate")
    private WithActivate withActivate;

    public SignupMeta(String userId) {
        withSignIn = new WithSignIn(userId);
        withActivate = new WithActivate(Config.WITH_ACTIVATE, Config.SKU);
    }
}
