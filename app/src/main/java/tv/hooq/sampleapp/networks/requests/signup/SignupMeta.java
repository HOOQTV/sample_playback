package tv.hooq.sampleapp.networks.requests.signup;

import com.google.gson.annotations.SerializedName;

public class SignupMeta {
    @SerializedName("withSignIn")
    private WithSignIn withSignIn;

    public SignupMeta(String userId) {
        withSignIn = new WithSignIn(userId);
    }
}
