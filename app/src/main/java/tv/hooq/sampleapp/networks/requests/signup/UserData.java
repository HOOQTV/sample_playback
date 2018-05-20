package tv.hooq.sampleapp.networks.requests.signup;

import com.google.gson.annotations.SerializedName;

import tv.hooq.sampleapp.commons.Utils;
import tv.hooq.sampleapp.models.enums.UserIdType;

public class UserData {
    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;

    @SerializedName("email")
    private String email;

    @SerializedName("phoneNumber")
    private String phoneNumber;

    @SerializedName("cpCustomerID")
    private String cpCustomerID;

    @SerializedName("country")
    private String country;

    @SerializedName("ipAddress")
    private String ipAddress;

    public UserData(String userId, String password, UserIdType userIdType) {
        switch (userIdType) {
            case PHONE_NUMBER: {
                phoneNumber = userId;
            }
            break;
            case CP_CUSTOMER_ID: {
                cpCustomerID = userId;
            }
            break;
            default: {
                email = userId;
            }
            break;
        }
        this.username = userId;
        this.password = password;

        this.country = Utils.getCountryCode();
        this.ipAddress = Utils.getIpAddress();
    }
}
