package tv.hooq.sampleapp.networks.responses.manifest;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TextTracks {
    @SerializedName("webvtt")
    private List<TextTrackItem> webvtt;

    @SerializedName("dfxp")
    private List<TextTrackItem> dfxp;

    public List<TextTrackItem> getWebvtt() {
        return webvtt;
    }

    public List<TextTrackItem> getDfxp() {
        return dfxp;
    }
}
