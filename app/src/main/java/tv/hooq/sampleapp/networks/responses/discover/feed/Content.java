package tv.hooq.sampleapp.networks.responses.discover.feed;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Content {
    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("as")
    private String as;

    @SerializedName("region")
    private String region;

    @SerializedName("images")
    private List<Image> images;

    @SerializedName("running_time")
    private int runningTime;

    @SerializedName("availability")
    private String availability;

    @SerializedName("meta")
    private ReleaseYearMeta meta;

    @SerializedName("running_time_friendly")
    private String runningTimeFriendly;

    @SerializedName("numberOfSeasons")
    private int numberOfSeasons;

    @SerializedName("tvod")
    private Tvod tvod;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAs() {
        return as;
    }

    public String getRegion() {
        return region;
    }

    public List<Image> getImages() {
        return images;
    }

    public int getRunningTime() {
        return runningTime;
    }

    public String getAvailability() {
        return availability;
    }

    public ReleaseYearMeta getMeta() {
        return meta;
    }

    public String getRunningTimeFriendly() {
        return runningTimeFriendly;
    }

    public int getNumberOfSeasons() {
        return numberOfSeasons;
    }

    public Tvod getTvod() {
        return tvod;
    }
}
