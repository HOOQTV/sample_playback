package tv.hooq.sampleapp.networks.responses.discover.titles;

import com.google.gson.annotations.SerializedName;

public class GenreMeta {
    @SerializedName("genre")
    private boolean genre;

    public boolean isGenre() {
        return genre;
    }
}
