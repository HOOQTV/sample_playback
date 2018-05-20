package tv.hooq.sampleapp.players.exoplayer;

import android.content.Context;
import android.text.TextUtils;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.core.BuildConfig;
import com.google.android.exoplayer2.drm.UnsupportedDrmException;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;

import java.util.Locale;
import java.util.UUID;

import tv.hooq.sampleapp.R;
import tv.hooq.sampleapp.SampleApplication;

/**
 * Created by Finno on 18/01/18.
 */

public class ExoUtil {

    public static final String DRM_SCHEME_WIDEVINE = "widevine";
    public static final String DRM_SCHEME_PLAYREADY = "playready";
    public static final String DRM_SCHEME_CLEARKEY = "clearkey";

    private static ExoUtil instance;
    public static ExoUtil getInstance() {
        if(instance == null) instance = new ExoUtil();
        return instance;
    }

    protected String mUserAgent;
    private Context mContext;

    public ExoUtil() {
        mContext = SampleApplication.getInstance();
        mUserAgent = Util.getUserAgent(mContext, "ExoPlayerDemo");
    }

    public UUID getDrmUuid(String drmScheme) throws UnsupportedDrmException {
        switch (Util.toLowerInvariant(drmScheme)) {
            case DRM_SCHEME_WIDEVINE:
                return C.WIDEVINE_UUID;
            case DRM_SCHEME_PLAYREADY:
                return C.PLAYREADY_UUID;
            case DRM_SCHEME_CLEARKEY:
                return C.CLEARKEY_UUID;
            default:
                try {
                    return UUID.fromString(drmScheme);
                } catch (RuntimeException e) {
                    throw new UnsupportedDrmException(UnsupportedDrmException.REASON_UNSUPPORTED_SCHEME);
                }
        }
    }

    public DataSource.Factory buildDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultDataSourceFactory(mContext, bandwidthMeter,
                buildHttpDataSourceFactory(bandwidthMeter));
    }

    public HttpDataSource.Factory buildHttpDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultHttpDataSourceFactory(mUserAgent, bandwidthMeter);
    }

    public boolean useExtensionRenderers() {
        return BuildConfig.FLAVOR.equals("withExtensions");
    }














    /**
     * Builds a track name for display.
     *
     * @param format {@link Format} of the track.
     * @return a generated name specific to the track.
     */
    public String buildTrackName(Format format) {
        String trackName;
        if (MimeTypes.isVideo(format.sampleMimeType)) {
            trackName = joinWithSeparator(joinWithSeparator(joinWithSeparator(
                    buildResolutionString(format), buildBitrateString(format)), buildTrackIdString(format)),
                    buildSampleMimeTypeString(format));
        } else if (MimeTypes.isAudio(format.sampleMimeType)) {
            trackName = joinWithSeparator(joinWithSeparator(joinWithSeparator(joinWithSeparator(
                    buildLanguageString(format), buildAudioPropertyString(format)),
                    buildBitrateString(format)), buildTrackIdString(format)),
                    buildSampleMimeTypeString(format));
        } else {
            trackName = joinWithSeparator(joinWithSeparator(joinWithSeparator(buildLanguageString(format),
                    buildBitrateString(format)), buildTrackIdString(format)),
                    buildSampleMimeTypeString(format));
        }
        return trackName.length() == 0 ? "unknown" : trackName;
    }

    private String buildResolutionString(Format format) {
        return format.width == Format.NO_VALUE || format.height == Format.NO_VALUE
                ? "" : format.width + "x" + format.height;
    }

    private String buildAudioPropertyString(Format format) {
        return format.channelCount == Format.NO_VALUE || format.sampleRate == Format.NO_VALUE
                ? "" : format.channelCount + "ch, " + format.sampleRate + "Hz";
    }

    private String buildLanguageString(Format format) {
        String lang = TextUtils.isEmpty(format.language) || "und".equals(format.language) ? ""
                : format.language;
        return getFriendlySubtitle(lang);
    }

    private String buildBitrateString(Format format) {
        return format.bitrate == Format.NO_VALUE ? ""
                : String.format(Locale.US, "%.2fMbit", format.bitrate / 1000000f);
    }

    private String joinWithSeparator(String first, String second) {
        return first.length() == 0 ? second : (second.length() == 0 ? first : first + ", " + second);
    }

    private String buildTrackIdString(Format format) {
        return format.id == null ? "" : ("id:" + format.id);
    }

    private String buildSampleMimeTypeString(Format format) {
        return format.sampleMimeType == null ? "" : format.sampleMimeType;
    }

    public static String getFriendlySubtitle(String key) {
        Context context = SampleApplication.getInstance().getApplicationContext();
        if (key != null) {
            // check in 2 char ISO country code
            String[] subtitleKeys = context.getResources().getStringArray(R.array.hawk_language_subtitle_values_2char);
            for (int i = 0; i < subtitleKeys.length; i++) {
                if (subtitleKeys[i].equalsIgnoreCase(key)) {
                    String[] subtitles = context.getResources().getStringArray(R.array.hawk_language_subtitle);
                    return subtitles[i];
                }
            }
        }
        return key;
    }
}
