package tv.hooq.sampleapp.trackings;

/**
 * Created by endiirawan on 5/15/18.
 */

public class PlayManifest {
    private String mTitle;
    private int mDuration;
    private String mUserId;
    private String mUserType;
    private String mStreamUrl;

    public PlayManifest(String title, int duration, String userId, String userType, String streamUrl) {
        this.mTitle = title;
        this.mDuration = duration;
        this.mUserId = userId;
        this.mUserType = userType;
        this.mStreamUrl = streamUrl;
    }

    public String getTitle() {
        return mTitle;
    }

    public int getDuration() {
        return mDuration;
    }

    public String getUserId() {
        return mUserId;
    }

    public String getAccessType() {
        return mUserType;
    }

    public String getStreamUrl() {
        return mStreamUrl;
    }
}
