package tv.hooq.sampleapp.networks.responses.discover.titles;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import tv.hooq.sampleapp.networks.responses.discover.feed.Image;
import tv.hooq.sampleapp.networks.responses.discover.feed.Tvod;

public class Data implements Parcelable {
    @SerializedName("id")
    private String id;

    @SerializedName("parent_id")
    private Object parentId;

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("short_description")
    private String shortDescription;

    @SerializedName("as")
    private String as;

    @SerializedName("region")
    private String region;

    @SerializedName("languages")
    private List<Object> languages;

    @SerializedName("audios")
    private List<String> audios;

    @SerializedName("streamable")
    private boolean streamable;

    @SerializedName("downloadable")
    private boolean downloadable;

    @SerializedName("running_time")
    private int runningTime;

    @SerializedName("images")
    private List<Image> images;

    @SerializedName("tags")
    private List<Tag> tags;

    @SerializedName("people")
    private List<Person> people;

    @SerializedName("seasons")
    private Object seasons;

    @SerializedName("meta")
    private RatingMeta meta;

    @SerializedName("quickplay")
    private Quickplay quickplay;

    @SerializedName("expires_on")
    private String expiresOn;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("availability")
    private String availability;

    @SerializedName("trailers")
    private Object trailers;

    @SerializedName("tvod")
    private Tvod tvod;

    @SerializedName("running_time_player")
    private String runningTimePlayer;

    @SerializedName("running_time_friendly")
    private String runningTimeFriendly;

    protected Data(Parcel in) {
        id = in.readString();
        title = in.readString();
        description = in.readString();
        shortDescription = in.readString();
        as = in.readString();
        region = in.readString();
        audios = in.createStringArrayList();
        streamable = in.readByte() != 0;
        downloadable = in.readByte() != 0;
        runningTime = in.readInt();
        expiresOn = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
        availability = in.readString();
        tvod = in.readParcelable(Tvod.class.getClassLoader());
        runningTimePlayer = in.readString();
        runningTimeFriendly = in.readString();
    }

    public static final Creator<Data> CREATOR = new Creator<Data>() {
        @Override
        public Data createFromParcel(Parcel in) {
            return new Data(in);
        }

        @Override
        public Data[] newArray(int size) {
            return new Data[size];
        }
    };

    public String getId() {
        return id;
    }

    public Object getParentId() {
        return parentId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getAs() {
        return as;
    }

    public String getRegion() {
        return region;
    }

    public List<Object> getLanguages() {
        return languages;
    }

    public List<String> getAudios() {
        return audios;
    }

    public boolean isStreamable() {
        return streamable;
    }

    public boolean isDownloadable() {
        return downloadable;
    }

    public int getRunningTime() {
        return runningTime;
    }

    public List<Image> getImages() {
        return images;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public List<Person> getPeople() {
        return people;
    }

    public Object getSeasons() {
        return seasons;
    }

    public RatingMeta getMeta() {
        return meta;
    }

    public Quickplay getQuickplay() {
        return quickplay;
    }

    public String getExpiresOn() {
        return expiresOn;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getAvailability() {
        return availability;
    }

    public Object getTrailers() {
        return trailers;
    }

    public Tvod getTvod() {
        return tvod;
    }

    public String getRunningTimePlayer() {
        return runningTimePlayer;
    }

    public String getRunningTimeFriendly() {
        return runningTimeFriendly;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(shortDescription);
        dest.writeString(as);
        dest.writeString(region);
        dest.writeStringList(audios);
        dest.writeByte((byte) (streamable ? 1 : 0));
        dest.writeByte((byte) (downloadable ? 1 : 0));
        dest.writeInt(runningTime);
        dest.writeString(expiresOn);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeString(availability);
        dest.writeParcelable(tvod, flags);
        dest.writeString(runningTimePlayer);
        dest.writeString(runningTimeFriendly);
    }
}
