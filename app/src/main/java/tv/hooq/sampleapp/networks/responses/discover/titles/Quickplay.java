package tv.hooq.sampleapp.networks.responses.discover.titles;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Quickplay implements Parcelable{
    @SerializedName("id")
    private String id;

    @SerializedName("assetId")
    private String assetId;

    protected Quickplay(Parcel in) {
        id = in.readString();
        assetId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(assetId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Quickplay> CREATOR = new Creator<Quickplay>() {
        @Override
        public Quickplay createFromParcel(Parcel in) {
            return new Quickplay(in);
        }

        @Override
        public Quickplay[] newArray(int size) {
            return new Quickplay[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getAssetId() {
        return assetId;
    }
}
