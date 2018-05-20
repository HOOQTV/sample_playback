package tv.hooq.sampleapp.networks.requests.signup;

import com.google.gson.annotations.SerializedName;

import tv.hooq.sampleapp.commons.Config;
import tv.hooq.utils.HooqHmacGenerator;

public class WithSignIn {
    @SerializedName("enabled")
    private boolean enabled;

    @SerializedName("hmac")
    private String hmac;

    @SerializedName("device")
    private DeviceInformation device;

    public WithSignIn(String user) {
        enabled = true;
        device = new DeviceInformation();
        
        HooqHmacGenerator hooqHmacGenerator = new HooqHmacGenerator(Config.getSharedKeysHmac());
        hmac = hooqHmacGenerator.encrypt(user);
    }
}
