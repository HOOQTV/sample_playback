package tv.hooq.sampleapp.networks.responses.discover.feed;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Tvod implements Parcelable{
    @SerializedName("id")
    private String id;

    @SerializedName("ticket")
    private int ticket;

    @SerializedName("price")
    private String price;

    @SerializedName("currency")
    private String currency;

    @SerializedName("expires_in")
    private int expiresIn;

    @SerializedName("display_name")
    private String displayName;

    @SerializedName("platform")
    private List<String> platform;

    public String getId() {
        return id;
    }

    public int getTicket() {
        return ticket;
    }

    public String getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public String getDisplayName() {
        return displayName;
    }

    public List<String> getPlatform() {
        return platform;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
