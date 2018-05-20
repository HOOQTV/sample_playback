package tv.hooq.sampleapp.networks.requests.signup;

import com.google.gson.annotations.SerializedName;

import tv.hooq.sampleapp.commons.Constants;
import tv.hooq.sampleapp.commons.Utils;

public class DeviceInformation {
    @SerializedName("name")
    private String name;

    @SerializedName("type")
    private String type;

    @SerializedName("modelNo")
    private String modelNo;

    @SerializedName("serialNo")
    private String serialNo;

    @SerializedName("brand")
    private String brand;

    @SerializedName("os")
    private String os;

    @SerializedName("osVersion")
    private String osVersion;

    public DeviceInformation() {
        this.name = Utils.getDeviceName();
        this.modelNo = Utils.getDeviceModel();

        this.type = Constants.DEVICE_TYPE_MOBILE;
        this.serialNo = Utils.getDeviceId();
        this.brand = Utils.getDeviceManufacture();
        this.os = Constants.DEVICE_OS;
        this.osVersion = Integer.toString(android.os.Build.VERSION.SDK_INT);
    }
}
