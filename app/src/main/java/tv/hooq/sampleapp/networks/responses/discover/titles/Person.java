package tv.hooq.sampleapp.networks.responses.discover.titles;

import com.google.gson.annotations.SerializedName;

public class Person {
    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("role")
    private String role;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }
}
