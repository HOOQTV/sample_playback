package tv.hooq.sampleapp.networks.responses.discover;

import com.google.gson.annotations.SerializedName;

import tv.hooq.sampleapp.networks.responses.discover.titles.Data;
import tv.hooq.sampleapp.networks.responses.discover.titles.Meta;

public class DiscoverTitlesResponse {
    @SerializedName("data")
    private Data data;

    @SerializedName("meta")
    private Meta meta;

    public Data getData() {
        return data;
    }

    public Meta getMeta() {
        return meta;
    }
}
