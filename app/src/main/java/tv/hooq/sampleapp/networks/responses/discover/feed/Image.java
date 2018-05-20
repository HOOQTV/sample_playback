package tv.hooq.sampleapp.networks.responses.discover.feed;

import com.google.gson.annotations.SerializedName;

public class Image {
    @SerializedName("id")
    private String id;

    @SerializedName("url")
    private String url;

    @SerializedName("type")
    private String type;

    @SerializedName("owner")
    private String owner;

    @SerializedName("storage")
    private String storage;

    @SerializedName("owner_id")
    private String ownerId;

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getType() {
        return type;
    }

    public String getOwner() {
        return owner;
    }

    public String getStorage() {
        return storage;
    }

    public String getOwnerId() {
        return ownerId;
    }
}
