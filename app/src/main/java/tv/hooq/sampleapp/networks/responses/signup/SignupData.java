package tv.hooq.sampleapp.networks.responses.signup;

import com.google.gson.annotations.SerializedName;

public class SignupData {
    @SerializedName("user")
    private User user;

    @SerializedName("session")
    private Session session;

    public User getUser() {
        return user;
    }

    public Session getSession() {
        return session;
    }
}
