package tv.hooq.sampleapp.networks.responses.manifest;

import com.google.gson.annotations.SerializedName;

public class TextTrackItem {
    @SerializedName("name")
    private String name;

    @SerializedName("lang")
    private String lang;

    @SerializedName("uri")
    private String uri;

    public String getName() {
        return name;
    }

    public String getLang() {
        return lang;
    }

    public String getUri() {
        return uri;
    }
}
