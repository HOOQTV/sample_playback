package tv.hooq.sampleapp.networks.responses.manifest;

import com.google.gson.annotations.SerializedName;

public class Meta {
    @SerializedName("now")
    private long now;

    public long getNow() {
        return now;
    }
}
