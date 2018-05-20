package tv.hooq.sampleapp.networks.responses.signup;

import com.google.gson.annotations.SerializedName;

public class SignupMeta {
    @SerializedName("now")
    private long now;

    @SerializedName("requestId")
    private String requestId;

    public void setNow(long now) {
        this.now = now;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
