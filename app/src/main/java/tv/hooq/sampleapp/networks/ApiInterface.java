package tv.hooq.sampleapp.networks;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import tv.hooq.sampleapp.networks.responses.discover.DiscoverFeedResponse;
import tv.hooq.sampleapp.networks.responses.discover.DiscoverTitlesResponse;
import tv.hooq.sampleapp.networks.responses.manifest.ManifestResponse;
import tv.hooq.sampleapp.networks.responses.signout.SignoutResponse;
import tv.hooq.sampleapp.networks.responses.signup.SignupResponse;

public interface ApiInterface {

    /* Sign up */
    @Headers({
            "Accept: application/json,text/html",
            "Content-Type: application/json; charset=UTF-8",
            "Cache-Control: no-cache"
    })
    @POST("2.0/user")
    Call<SignupResponse> signup(
            @Body RequestBody body
    );

    /* Sign out */
    @Headers({
            "Cache-Control: no-cache",
            "Content-Type: application/json; charset=UTF-8"
    })
    @POST("2.0/user/signout")
    Call<SignoutResponse> signout();

    /* Discover Feed */
    @GET("v1.0/discover/feed")
    Call<DiscoverFeedResponse> discoverFeed(
            @Query("region") String region,
            @Query("page") int page,
            @Query("perPage") int perPage
    );

    /* Discover Title */
    @GET("v1.0/discover/titles/{uuid}")
    Call<DiscoverTitlesResponse> discoverTitle(
            @Path("uuid") String uuid
    );

    /* Title Manifest */
    @Headers({
            "content-type: application/json; charset=UTF-8"
    })
    @GET("2.0/play/{uuid}")
    Call<ManifestResponse> getTitleManifest(
            @Path("uuid") String uuid,
            @Header("x-device-id") String deviceId,
            @Header("x-device-model") String deviceModel,
            @Header("x-device-os") String deviceOS,
            @Header("x-device-os-version") String deviceOSVersion,
            @Header("x-device-type") String deviceType,
            @Header("x-dhs-drm") String dhsDrm
    );
}
