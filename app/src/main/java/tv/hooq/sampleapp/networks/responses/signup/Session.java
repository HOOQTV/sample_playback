package tv.hooq.sampleapp.networks.responses.signup;

import com.google.gson.annotations.SerializedName;

public class Session {
    @SerializedName("accessToken")
    private String accessToken;

    @SerializedName("refreshToken")
    private String refreshToken;

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
