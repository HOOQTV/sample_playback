package tv.hooq.sampleapp.networks.requests.signup;

import com.google.gson.annotations.SerializedName;

import tv.hooq.sampleapp.models.enums.UserIdType;

public class SignupRequest {
    @SerializedName("data")
    private UserData data;

    @SerializedName("meta")
    private SignupMeta meta;

    public SignupRequest(String userId, String password, UserIdType userIdType) {
        data = new UserData(userId, password, userIdType);
        meta = new SignupMeta(userId);
    }

    public UserData getData() {
        return data;
    }

    public void setData(UserData data) {
        this.data = data;
    }

    public SignupMeta getMeta() {
        return meta;
    }

    public void setMeta(SignupMeta meta) {
        this.meta = meta;
    }
}
