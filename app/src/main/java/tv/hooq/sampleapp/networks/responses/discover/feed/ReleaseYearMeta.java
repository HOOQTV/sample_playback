package tv.hooq.sampleapp.networks.responses.discover.feed;

import com.google.gson.annotations.SerializedName;

public class ReleaseYearMeta {
    @SerializedName("releaseYear")
    private int releaseYear;

    public int getReleaseYear() {
        return releaseYear;
    }
}
