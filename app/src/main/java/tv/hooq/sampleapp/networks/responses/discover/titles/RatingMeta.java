package tv.hooq.sampleapp.networks.responses.discover.titles;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class RatingMeta implements Parcelable{
    @SerializedName("ageRating")
    private String ageRating;

    @SerializedName("releaseYear")
    private int releaseYear;

    @SerializedName("contentRating")
    private String contentRating;

    protected RatingMeta(Parcel in) {
        ageRating = in.readString();
        releaseYear = in.readInt();
        contentRating = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ageRating);
        dest.writeInt(releaseYear);
        dest.writeString(contentRating);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RatingMeta> CREATOR = new Creator<RatingMeta>() {
        @Override
        public RatingMeta createFromParcel(Parcel in) {
            return new RatingMeta(in);
        }

        @Override
        public RatingMeta[] newArray(int size) {
            return new RatingMeta[size];
        }
    };

    public String getAgeRating() {
        return ageRating;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public String getContentRating() {
        return contentRating;
    }
}
