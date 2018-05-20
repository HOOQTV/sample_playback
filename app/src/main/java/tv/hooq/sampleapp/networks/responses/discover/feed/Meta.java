package tv.hooq.sampleapp.networks.responses.discover.feed;

import com.google.gson.annotations.SerializedName;

public class Meta {
    @SerializedName("requestId")
    private String requestId;

    @SerializedName("timestamp")
    private long timestamp;

    public String getRequestId() {
        return requestId;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
