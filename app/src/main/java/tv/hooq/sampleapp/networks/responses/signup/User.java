package tv.hooq.sampleapp.networks.responses.signup;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("channelPartnerID")
    private String channelPartnerID;

    @SerializedName("customerUsername")
    private String customerUsername;

    @SerializedName("customerPassword")
    private String customerPassword;

    @SerializedName("email")
    private String email;

    @SerializedName("firstName")
    private String firstName;

    @SerializedName("lastName")
    private String lastName;

    @SerializedName("country")
    private String country;

    @SerializedName("ipAddress")
    private String ipAddress;

    @SerializedName("Address")
    private Object Address;

    public String getChannelPartnerID() {
        return channelPartnerID;
    }

    public String getCustomerUsername() {
        return customerUsername;
    }

    public String getCustomerPassword() {
        return customerPassword;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCountry() {
        return country;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public Object getAddress() {
        return Address;
    }
}
