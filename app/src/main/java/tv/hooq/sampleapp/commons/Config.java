package tv.hooq.sampleapp.commons;

        import java.util.HashMap;

public class Config {

    //TODO : Update this code to use preferred endpoint
    public final static boolean USE_API_NIGHTLY = true;
    public final static boolean USE_DISCOVER_NIGHTLY = false;

    //TODO : Update this code with HOOQ API Key given
    public final static String API_KEY = "{your_api_key}";

    //TODO : Update this code with the Conviva API KEY
    public final static String CONVIVA_API_KEY = "{your_conviva_api_key}";

    //TODO : Update this code with the Conviva Gateway url given
    public final static String CONVIVA_GATEWAY_URL = "{your_conviva_gate_url}";

    //TODO : Update this country code to your CountryCode. Use ISO 3166-2 (2 char) Country Code
    //Available county code : IN
    public final static String COUNTRY_CODE = "XX";

    //TODO : Get the IP dynamically or  hardcode your public IP
    public final static String IP_ADDRESS = "0.0.0.0";

    //TODO : Change this value to use Certificate Pinning
    public final static boolean USE_CERTIFICATE_PINNING = false;

    //TODO : Change this value if want to activate user directly
    public final static boolean WITH_ACTIVATE = false;

    //TODO : Change this value to available SKU
    public final static String SKU = "{{your_sku}}";

    //TODO : Update this HMAC key, will be used in Sign In Request
    public final static HashMap<String, String> getSharedKeysHmac() {
        HashMap<String, String> sharedKeys = new HashMap<>();
        sharedKeys.put("{your_key_0}", "{your_value_0}");
        sharedKeys.put("{your_key_1}", "{your_value_1}");
        return sharedKeys;
    }
}
