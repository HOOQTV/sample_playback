package tv.hooq.sampleapp.networks.responses.manifest;

import com.google.gson.annotations.SerializedName;

public class ContentManifest {
    @SerializedName("content")
    private String content;

    @SerializedName("license")
    private String license;

    @SerializedName("certificate")
    private String certificate;

    @SerializedName("textTracks")
    private TextTracks textTracks;

    public String getContent() {
        return content;
    }

    public String getLicense() {
        return license;
    }

    public String getCertificate() {
        return certificate;
    }

    public TextTracks getTextTracks() {
        return textTracks;
    }
}
