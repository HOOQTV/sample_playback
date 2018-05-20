package tv.hooq.sampleapp.networks.responses.discover.feed;

import com.google.gson.annotations.SerializedName;

public class ImageOrientationMeta {
    @SerializedName("imageOrientation")
    private String imageOrientation;

    public String getImageOrientation() {
        return imageOrientation;
    }
}
