package tv.hooq.sampleapp.commons;

import java.util.HashMap;

public class Config {
    public final static String API_KEY = "{your_api_key}";

    public final static HashMap<String,String> getSharedKeysHmac() {
        HashMap<String,String> sharedKeys = new HashMap<>();
        sharedKeys.put("{your_key_0}", "{your_value_0}");
        sharedKeys.put("{your_key_1}", "{your_value_1}");
        return sharedKeys;
    }
}
