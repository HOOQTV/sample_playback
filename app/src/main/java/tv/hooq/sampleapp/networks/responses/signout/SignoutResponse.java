package tv.hooq.sampleapp.networks.responses.signout;

import com.google.gson.annotations.SerializedName;

public class SignoutResponse {
    @SerializedName("data")
    private Object data;

    @SerializedName("now")
    private Meta meta;

    public Object getData() {
        return data;
    }

    public Meta getMeta() {
        return meta;
    }
}
