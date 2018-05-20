package tv.hooq.sampleapp.networks;

import android.text.TextUtils;

import java.util.HashMap;

public class BaseUrlSelector {
    private static BaseUrlSelector instance;

    public static BaseUrlSelector getInstance() {
        if (instance == null) {
            instance = new BaseUrlSelector();
        }
        return instance;
    }

    private int mEnvSelected = ENVIRONMENT_STAGGING;

    private static boolean USE_HTTPS = true;

    public final static int ENVIRONMENT_PROD = 0;
    public final static int ENVIRONMENT_STAGGING = 1;

    private HashMap<String,String> sha256s;

    public BaseUrlSelector() {
        sha256s = new HashMap<>();
        sha256s.put("api-sandbox.hooq.tv", "fZLR0JanpIxFSkXKll2JUYGNb17e3LgANdc2r8RGfp8=");
        sha256s.put("cdn-discover-nightly.hooq.tv", "BNuRyzgv5Gr31DQR6HiVJro5PLelDYdwJFOIuXF/FRg=");
    }

    public void setEnvironment(int envSelected) {
        mEnvSelected = envSelected;
    }

    public int getEnvironment() {
        return mEnvSelected;
    }

    public String getPartnerBasePath() {
        String result = null;
        switch (mEnvSelected) {
            case ENVIRONMENT_STAGGING:
                result = "api-sandbox.hooq.tv/";
                break;
        }
        return httpModificator(result);
    }

    public String getCDNDiscoverBasePath() {
        return httpModificator("cdn-discover-nightly.hooq.tv");
    }

    private String httpModificator(String s) {
        if (!TextUtils.isEmpty(s)) {
            if(USE_HTTPS) {
                return "https://" + s;
            } else {
                return "http://" + s;
            }
        }
        return s;
    }

    public String getSha256(String hostname) {
        String sha256 = sha256s.get(hostname);
        return sha256;
    }
}
