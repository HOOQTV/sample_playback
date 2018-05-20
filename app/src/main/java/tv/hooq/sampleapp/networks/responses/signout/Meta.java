package tv.hooq.sampleapp.networks.responses.signout;

import com.google.gson.annotations.SerializedName;

public class Meta {
    @SerializedName("now")
    private long now;

    @SerializedName("requestId")
    private String requestId;

    public long getNow() {
        return now;
    }

    public String getRequestId() {
        return requestId;
    }
}
