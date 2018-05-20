package tv.hooq.sampleapp.networks.responses.signup;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

public class SignupResponse {
    @SerializedName("data")
    private SignupData data;

    @SerializedName("meta")
    private Object meta;

    public SignupData getData() {
        return data;
    }

    public Object getMeta() {
        return meta;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static SignupResponse parseFromString(String json) {
        Gson gson = new Gson();
        SignupResponse signupResponse = gson.fromJson(json, new TypeToken<SignupResponse>() {
        }.getType());
        return signupResponse;
    }
}
