package tv.hooq.sampleapp.networks.responses.discover.feed;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {
    @SerializedName("row_id")
    private String rowId;

    @SerializedName("row_name")
    private String rowName;

    @SerializedName("region")
    private String region;

    @SerializedName("order")
    private int order;

    @SerializedName("type")
    private String type;

    @SerializedName("obj_id")
    private String objId;

    @SerializedName("meta")
    private ImageOrientationMeta meta;

    @SerializedName("data")
    private List<Content> data;

    public String getRowId() {
        return rowId;
    }

    public String getRowName() {
        return rowName;
    }

    public String getRegion() {
        return region;
    }

    public int getOrder() {
        return order;
    }

    public String getType() {
        return type;
    }

    public String getObjId() {
        return objId;
    }

    public ImageOrientationMeta getMeta() {
        return meta;
    }

    public List<Content> getData() {
        return data;
    }
}
