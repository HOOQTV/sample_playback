package tv.hooq.sampleapp.networks.responses.manifest;

import com.google.gson.annotations.SerializedName;

public class ManifestResponse {
    @SerializedName("data")
    private ContentManifest data;

    @SerializedName("meta")
    private Meta meta;

    public ContentManifest getData() {
        return data;
    }

    public Meta getMeta() {
        return meta;
    }
}
