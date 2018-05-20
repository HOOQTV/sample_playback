package tv.hooq.sampleapp.networks;

import android.net.Uri;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

import okhttp3.CertificatePinner;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tv.hooq.sampleapp.commons.Config;
import tv.hooq.sampleapp.commons.Constants;
import tv.hooq.sampleapp.commons.SharedPreferencesManager;
import tv.hooq.sampleapp.commons.Utils;
import tv.hooq.sampleapp.models.enums.UserIdType;
import tv.hooq.sampleapp.networks.requests.signup.SignupRequest;
import tv.hooq.sampleapp.networks.responses.discover.DiscoverFeedResponse;
import tv.hooq.sampleapp.networks.responses.discover.DiscoverTitlesResponse;
import tv.hooq.sampleapp.networks.responses.manifest.ManifestResponse;
import tv.hooq.sampleapp.networks.responses.signout.SignoutResponse;
import tv.hooq.sampleapp.networks.responses.signup.SignupResponse;

public class NetworkAgent {

    private static NetworkAgent instance;

    public static NetworkAgent getInstance() {
        if(instance == null) {
            instance = new NetworkAgent();
        }
        return instance;
    }

    public NetworkAgent() {

    }

    private Retrofit buildService(String baseUrl) {
        return buildService(baseUrl, getOkHttpClient(baseUrl));
    }

    private Retrofit buildService(String baseUrl, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    private OkHttpClient getOkHttpClient(String baseUrl) {
        return getOkHttpClient(baseUrl, null);
    }

    private OkHttpClient getOkHttpClient(String baseUrl, String authorization) {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Uri uri = Uri.parse(baseUrl);
        String hostname = uri.getHost();
        String sha256 = BaseUrlSelector.getInstance().getSha256(hostname);
        if(!TextUtils.isEmpty(sha256)) {
            CertificatePinner.Builder certificatePinnerBuilder = new CertificatePinner.Builder();
            certificatePinnerBuilder.add(hostname, "sha256/" + sha256);
            httpClient.certificatePinner(certificatePinnerBuilder.build());
        }

        httpClient.addInterceptor(new BasicAuthInterceptor(authorization));
        httpClient.connectTimeout(1500, TimeUnit.MILLISECONDS);
        return httpClient.build();
    }

    public class BasicAuthInterceptor implements Interceptor {

        private String credentials = null;

        public BasicAuthInterceptor(String authorization) {
            this.credentials = authorization;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Request.Builder authReqBuilder = request.newBuilder();

            /* Authorization */
            String authorization = "";
            if(TextUtils.isEmpty(credentials)) {
                String jwtLogin = SharedPreferencesManager.getInstance().getSanctuaryToken();
                if (!TextUtils.isEmpty(jwtLogin)) {
                    authorization = "Bearer " + jwtLogin;
                }
            } else {
                authorization = credentials;
            }
            authReqBuilder.addHeader("Authorization", authorization);

            authReqBuilder.addHeader("apikey", Config.API_KEY);

            Request authenticatedRequest = authReqBuilder.build();
            return chain.proceed(authenticatedRequest);
        }
    }

    private ApiInterface getApiService(String baseUrl) {
        return buildService(baseUrl).create(ApiInterface.class);
    }

    public Call<SignupResponse> signup(String email) {
        try {
            UserIdType userIdType = UserIdType.PHONE_NUMBER;
            if(email.contains("@")) {
                userIdType = UserIdType.EMAIL;
            }
            String password = "123456";
            SignupRequest signupRequest = new SignupRequest(email, password, userIdType);

            Gson gson = new Gson();
            String body = gson.toJson(signupRequest);
            RequestBody in = RequestBody.create(MediaType.parse("application/json"), body.getBytes("UTF-8"));
            return getApiService(BaseUrlSelector.getInstance().getPartnerBasePath()).signup(in);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Call<DiscoverFeedResponse> discoverFeed(int page, int perPage) {
        String region = Utils.getCountryCode();
        return getApiService(BaseUrlSelector.getInstance().getCDNDiscoverBasePath()).discoverFeed(region, page, perPage);
    }

    public Call<DiscoverTitlesResponse> discoverTitle(String uuid) {
        return getApiService(BaseUrlSelector.getInstance().getCDNDiscoverBasePath()).discoverTitle(uuid);
    }

    public Call<ManifestResponse> getTitleManifest(String uuid, String dhsDrm) {
        String deviceId = Utils.getDeviceId();
        String deviceModel = Utils.getDeviceName();
        String deviceOS = Constants.DEVICE_OS;
        String deviceOSVersion = Integer.toString(android.os.Build.VERSION.SDK_INT);
        String deviceType = Constants.DEVICE_TYPE_MOBILE;

        Call<ManifestResponse> titleManifestCall = getApiService(BaseUrlSelector.getInstance().getPartnerBasePath()).getTitleManifest(
                uuid,
                deviceId,
                deviceModel,
                deviceOS,
                deviceOSVersion,
                deviceType,
                dhsDrm
        );
        return titleManifestCall;
    }

    public Call<SignoutResponse> signout() {
        return getApiService(BaseUrlSelector.getInstance().getPartnerBasePath()).signout();
    }
}
