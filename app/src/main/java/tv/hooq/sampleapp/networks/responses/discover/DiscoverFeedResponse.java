package tv.hooq.sampleapp.networks.responses.discover;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import tv.hooq.sampleapp.networks.responses.discover.feed.Data;
import tv.hooq.sampleapp.networks.responses.discover.feed.Meta;
import tv.hooq.sampleapp.networks.responses.discover.feed.Pagination;

public class DiscoverFeedResponse {
    @SerializedName("data")
    public List<Data> data;

    @SerializedName("pagination")
    public Pagination pagination;

    @SerializedName("meta")
    public Meta meta;

    public List<Data> getData() {
        return data;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public Meta getMeta() {
        return meta;
    }
}
