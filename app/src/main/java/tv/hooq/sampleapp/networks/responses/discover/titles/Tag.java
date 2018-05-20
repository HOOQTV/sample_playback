package tv.hooq.sampleapp.networks.responses.discover.titles;

import com.google.gson.annotations.SerializedName;

public class Tag {
    @SerializedName("id")
    private String id;

    @SerializedName("tag")
    private String tag;

    @SerializedName("meta")
    public GenreMeta meta;

    @SerializedName("label")
    private String label;

    public String getId() {
        return id;
    }

    public String getTag() {
        return tag;
    }

    public GenreMeta getMeta() {
        return meta;
    }

    public String getLabel() {
        return label;
    }
}
