package tv.hooq.sampleapp.views.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.drm.FrameworkMediaDrm;
import com.google.android.exoplayer2.drm.HttpMediaDrmCallback;
import com.google.android.exoplayer2.drm.UnsupportedDrmException;
import com.google.android.exoplayer2.mediacodec.MediaCodecRenderer;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.source.MergingMediaSource;
import com.google.android.exoplayer2.source.SingleSampleMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.DebugTextViewHelper;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tv.hooq.sampleapp.R;
import tv.hooq.sampleapp.commons.Constants;
import tv.hooq.sampleapp.networks.NetworkAgent;
import tv.hooq.sampleapp.networks.responses.discover.titles.Data;
import tv.hooq.sampleapp.networks.responses.manifest.ManifestResponse;
import tv.hooq.sampleapp.networks.responses.manifest.TextTrackItem;
import tv.hooq.sampleapp.players.exoplayer.EventLogger;
import tv.hooq.sampleapp.players.exoplayer.ExoUtil;
import tv.hooq.sampleapp.players.exoplayer.TrackSelectionHelper;
import tv.hooq.sampleapp.trackings.ConvivaTrackingManager;
import tv.hooq.sampleapp.trackings.PlayManifest;

public class PlayerActivity extends BaseActivity {
    public static final String EXTRA_MOVIE = "EXTRA_MOVIE";

    public final String PREFERRED_DRM = Constants.DRM_DASH_WIDEVINE;

    private List<TextTrackItem> mSubtitles;
    private Data mMovieData;

    @BindView(R.id.progress_bar)
    ProgressBar mLoading;

    @BindView(R.id.player_view)
    SimpleExoPlayerView mSimpleExoPlayerView;

    @BindView(R.id.retry_button)
    Button mRetryBtn;

    @BindView(R.id.controls_root)
    LinearLayout mDebugView;

    @BindView(R.id.debug_text_view)
    TextView mDebugTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        ButterKnife.bind(this);
        initView(savedInstanceState);
    }

    private void initView(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mMovieData = savedInstanceState.getParcelable(EXTRA_MOVIE);
        } else {
            mMovieData = getIntent().getParcelableExtra(EXTRA_MOVIE);
        }
        setTitle(mMovieData.getTitle());
        getTitleManifest();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(EXTRA_MOVIE, mMovieData);
        super.onSaveInstanceState(outState);
    }

    private void getTitleManifest() {
        setLoading(true);
        NetworkAgent.getInstance().getTitleManifest(mMovieData.getId(), Constants.DRM_DASH_WIDEVINE).enqueue(new Callback<ManifestResponse>() {
            @Override
            public void onResponse(Call<ManifestResponse> call, Response<ManifestResponse> response) {
                if (response.isSuccessful()) {
                    initPlayer(response.body());
                }
                setLoading(false);
            }

            @Override
            public void onFailure(Call<ManifestResponse> call, Throwable throwable) {
                setLoading(false);
            }
        });
    }

    private void setLoading(boolean isShow) {
        mLoading.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    /*
    Player
     */

    private SimpleExoPlayer mPlayer;
    private TrackSelectionHelper mTrackSelectionHelper;
    private DebugTextViewHelper mDebugTextViewHelper;
    private DefaultTrackSelector mDefaultTrackSelector;
    private Context mContext = this;
    private EventLogger mEventLogger;
    private Handler mMainHandler;
    private TrackGroupArray mLastTrackGroupArray;
    private Boolean mShouldAutoPlay = true;
    private int mResumeWindow;
    private long mResumePosition;
    private boolean inErrorState;
    private int mLastState = 0;
    private DataSource.Factory mMediaDataSourceFactory;
    private Activity mActivity = this;

    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    private static final CookieManager DEFAULT_COOKIE_MANAGER;

    static {
        DEFAULT_COOKIE_MANAGER = new CookieManager();
        DEFAULT_COOKIE_MANAGER.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER);
    }

    private void clearResumePosition() {
        mResumeWindow = C.INDEX_UNSET;
        mResumePosition = C.TIME_UNSET;
    }

    private void initPlayer(ManifestResponse manifestResponse) {
        mShouldAutoPlay = true;
        clearResumePosition();
        mMediaDataSourceFactory = buildDataSourceFactory(true);
        mMainHandler = new Handler();
        if (CookieHandler.getDefault() != DEFAULT_COOKIE_MANAGER) {
            CookieHandler.setDefault(DEFAULT_COOKIE_MANAGER);
        }

        mSimpleExoPlayerView.setControllerVisibilityListener(new PlaybackControlView.VisibilityListener() {
            @Override
            public void onVisibilityChange(int visibility) {
                mDebugView.setVisibility(visibility);
            }
        });
        mSimpleExoPlayerView.requestFocus();

        boolean needNewPlayer = mPlayer == null;
        String licenseUrl = manifestResponse.getData().getLicense();
        String contentUrl = manifestResponse.getData().getContent();

        if (TextUtils.isEmpty(contentUrl)) {
            setErrorListener("Manifest URL is empty");
            return;
        }
        if (needNewPlayer) {
            TrackSelection.Factory adaptiveTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);
            mDefaultTrackSelector = new DefaultTrackSelector(adaptiveTrackSelectionFactory);
            mTrackSelectionHelper = new TrackSelectionHelper(mDefaultTrackSelector, adaptiveTrackSelectionFactory);
            mLastTrackGroupArray = null;
            mEventLogger = new EventLogger(mDefaultTrackSelector);

            DrmSessionManager<FrameworkMediaCrypto> drmSessionManager = null;
            boolean preferExtensionDecoders = false;

            if (!TextUtils.isEmpty(licenseUrl)) {
                String drmScheme = ExoUtil.DRM_SCHEME_WIDEVINE;
                String[] keyRequestProperties = null;

                boolean multiSession = false;

                int errorStringId = R.string.error_drm_unknown;
                if (Util.SDK_INT < 18) {
                    errorStringId = R.string.error_drm_not_supported;
                } else {
                    try {
                        UUID drmSchemeUuid = ExoUtil.getInstance().getDrmUuid(drmScheme);
                        drmSessionManager = buildDrmSessionManagerV18(drmSchemeUuid, licenseUrl,
                                keyRequestProperties, multiSession);
                    } catch (UnsupportedDrmException e) {
                        errorStringId = e.reason == UnsupportedDrmException.REASON_UNSUPPORTED_SCHEME
                                ? R.string.error_drm_unsupported_scheme : R.string.error_drm_unknown;
                    }
                }
                if (drmSessionManager == null) {
                    setErrorListener(mContext.getResources().getString(errorStringId));
                    return;
                }
            }

            @DefaultRenderersFactory.ExtensionRendererMode int extensionRendererMode =
                    ExoUtil.getInstance().useExtensionRenderers()
                            ? (preferExtensionDecoders ? DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER
                            : DefaultRenderersFactory.EXTENSION_RENDERER_MODE_ON)
                            : DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF;
            DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(mContext,
                    drmSessionManager, extensionRendererMode);

            mPlayer = ExoPlayerFactory.newSimpleInstance(renderersFactory, mDefaultTrackSelector);
            mPlayer.addListener(mEventListener);
            mPlayer.addListener(mEventLogger);
            mPlayer.addMetadataOutput(mEventLogger);
            mPlayer.addAudioDebugListener(mEventLogger);
            mPlayer.addVideoDebugListener(mEventLogger);


            mSimpleExoPlayerView.setPlayer(mPlayer);
            mPlayer.setPlayWhenReady(mShouldAutoPlay);
            mDebugTextViewHelper = new DebugTextViewHelper(mPlayer, mDebugTextView);
            mDebugTextViewHelper.start();
        }

        mSubtitles = new ArrayList<>();
        if (manifestResponse.getData().getTextTracks() != null) {
            if (manifestResponse.getData().getTextTracks().getWebvtt() != null) {
                mSubtitles = manifestResponse.getData().getTextTracks().getWebvtt();
            }
        }
        ArrayList<MediaSource> listSources = new ArrayList<>();

        /* MediaSource for content */
        MediaSource mediaSourceContent = buildMediaSource(Uri.parse(contentUrl), getExtension(contentUrl), mMainHandler, mEventLogger);
        listSources.add(mediaSourceContent);

        /* MediaSource for subtitle */
        for (int i = 0; i < mSubtitles.size(); i++) {
            TextTrackItem subtitle = mSubtitles.get(i);
            Format subtitleFormat = Format.createTextSampleFormat(
                    null,
                    MimeTypes.TEXT_VTT,
                    0,
                    null);

            MediaSource textMediaSource = new SingleSampleMediaSource(
                    Uri.parse(subtitle.getUri()), //testing purpose
                    mMediaDataSourceFactory,
                    subtitleFormat,
                    C.TIME_UNSET);
        }

        /* Merging all of sources */
        MediaSource mediaSourceFinal;
        MediaSource[] mediaSources = new MediaSource[listSources.size()];
        for (int i = 0; i < listSources.size(); i++) {
            mediaSources[i] = listSources.get(i);
        }
        mediaSourceFinal = new MergingMediaSource(mediaSources);

        boolean haveResumePosition = mResumeWindow != C.INDEX_UNSET;
        if (haveResumePosition) {
            mPlayer.seekTo(mResumeWindow, mResumeWindow);
        }
        ConvivaTrackingManager.getInstance().startTracking(new PlayManifest(
                        mMovieData.getTitle(),
                        mMovieData.getRunningTime(),
                        "user-id",
                        "sample-user-sku-paid-hotstar20k",
                        manifestResponse.getData().getContent()),
                mPlayer);


        mPlayer.prepare(mediaSourceFinal, !haveResumePosition, false);
        inErrorState = false;
        updateButtonVisibilities();
    }

    private String getExtension(String url) {
        if (TextUtils.isEmpty(url)) {
            return url;
        }
        String[] aUrl = url.split("\\.");
        return aUrl[aUrl.length - 1];
    }

    private int getContentType(String drmScheme) {
        switch (drmScheme) {
            case Constants.DRM_DASH_WIDEVINE: {
                return C.TYPE_DASH;
            }
            default:
                return -1;
        }
    }

    private MediaSource buildMediaSource(
            Uri uri,
            String overrideExtension,
            @Nullable Handler handler,
            @Nullable MediaSourceEventListener listener) {
        @C.ContentType int type = getContentType(PREFERRED_DRM);
        switch (type) {
            case C.TYPE_DASH:
                return new DashMediaSource.Factory(
                        new DefaultDashChunkSource.Factory(mMediaDataSourceFactory),
                        buildDataSourceFactory(false))
                        .createMediaSource(uri, handler, listener);
            case C.TYPE_SS:
                return new SsMediaSource.Factory(
                        new DefaultSsChunkSource.Factory(mMediaDataSourceFactory),
                        buildDataSourceFactory(false))
                        .createMediaSource(uri, handler, listener);
            case C.TYPE_HLS:
                return new HlsMediaSource.Factory(mMediaDataSourceFactory)
                        .createMediaSource(uri, handler, listener);
            case C.TYPE_OTHER:
                return new ExtractorMediaSource.Factory(mMediaDataSourceFactory)
                        .createMediaSource(uri, handler, listener);
            default: {
                throw new IllegalStateException("Unsupported type: " + type);
            }
        }
    }

    private void updateButtonVisibilities() {
        mDebugView.removeAllViews();

        mRetryBtn.setVisibility(inErrorState ? View.VISIBLE : View.GONE);
        mDebugView.addView(mRetryBtn);

        if (mPlayer == null) {
            return;
        }

        MappingTrackSelector.MappedTrackInfo mappedTrackInfo = mDefaultTrackSelector.getCurrentMappedTrackInfo();
        if (mappedTrackInfo == null) {
            return;
        }

        for (int i = 0; i < mappedTrackInfo.length; i++) {
            TrackGroupArray trackGroups = mappedTrackInfo.getTrackGroups(i);
            if (trackGroups.length != 0) {
                Button button = new Button(mContext);
                int label;
                switch (mPlayer.getRendererType(i)) {
                    case C.TRACK_TYPE_AUDIO:
                        label = R.string.audio;
                        break;
                    case C.TRACK_TYPE_VIDEO:
                        label = R.string.video;
                        break;
                    case C.TRACK_TYPE_TEXT:
                        label = R.string.text;
                        break;
                    default:
                        continue;
                }

                button.setText(label);
                button.setTag(i);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mActivity != null) {
                            MappingTrackSelector.MappedTrackInfo mappedTrackInfo = mDefaultTrackSelector.getCurrentMappedTrackInfo();
                            if (mappedTrackInfo != null) {
                                int rendererIndex = (int) v.getTag();
                                ArrayList<String> labels = new ArrayList<>();
                                if (rendererIndex == 2) {
                                    for (int j = 0; j < mSubtitles.size(); j++) {
                                        labels.add(mSubtitles.get(j).getName());
                                    }
                                }
                                mTrackSelectionHelper.showSelectionDialog(
                                        mActivity, ((Button) v).getText(), mappedTrackInfo, rendererIndex, labels);
                            }
                        }
                    }
                });
                mDebugView.addView(button, mDebugView.getChildCount() - 1);
            }
        }
    }

    private HttpDataSource.Factory buildHttpDataSourceFactory(boolean useBandwidthMeter) {
        return ExoUtil.getInstance().buildHttpDataSourceFactory(useBandwidthMeter ? BANDWIDTH_METER : null);
    }

    private DataSource.Factory buildDataSourceFactory(boolean useBandwidthMeter) {
        return ExoUtil.getInstance().buildDataSourceFactory(useBandwidthMeter ? BANDWIDTH_METER : null);
    }

    private DrmSessionManager<FrameworkMediaCrypto> buildDrmSessionManagerV18(UUID uuid,
                                                                              String licenseUrl, String[] keyRequestPropertiesArray, boolean multiSession)
            throws UnsupportedDrmException {
        HttpMediaDrmCallback drmCallback = new HttpMediaDrmCallback(licenseUrl,
                buildHttpDataSourceFactory(false));
        if (keyRequestPropertiesArray != null) {
            for (int i = 0; i < keyRequestPropertiesArray.length - 1; i += 2) {
                drmCallback.setKeyRequestProperty(keyRequestPropertiesArray[i],
                        keyRequestPropertiesArray[i + 1]);
            }
        }
        return new DefaultDrmSessionManager<>(uuid, FrameworkMediaDrm.newInstance(uuid), drmCallback,
                null, mMainHandler, mEventLogger, multiSession);
    }

    private ExoPlayer.EventListener mEventListener = new ExoPlayer.EventListener() {
        @Override
        public void onTimelineChanged(Timeline timeline, Object o) {

        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroupArray, TrackSelectionArray trackSelectionArray) {
            updateButtonVisibilities();
            if (trackGroupArray != mLastTrackGroupArray) {
                MappingTrackSelector.MappedTrackInfo mappedTrackInfo = mDefaultTrackSelector.getCurrentMappedTrackInfo();
                if (mappedTrackInfo != null) {
                    if (mappedTrackInfo.getTrackTypeRendererSupport(C.TRACK_TYPE_VIDEO)
                            == MappingTrackSelector.MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS) {
                        setErrorListener(getString(R.string.error_unsupported_video));
                    }
                    if (mappedTrackInfo.getTrackTypeRendererSupport(C.TRACK_TYPE_AUDIO)
                            == MappingTrackSelector.MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS) {
                        setErrorListener(getString(R.string.error_unsupported_audio));
                    }
                }
                mLastTrackGroupArray = trackGroupArray;
            }
        }

        @Override
        public void onLoadingChanged(boolean b) {

        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            if (playbackState == Player.STATE_ENDED) {
                showControls();
            }
            mLastState = playbackState;
            updateButtonVisibilities();
        }

        @Override
        public void onRepeatModeChanged(int i) {

        }

        @Override
        public void onShuffleModeEnabledChanged(boolean b) {

        }

        @SuppressLint("StringFormatInvalid")
        @Override
        public void onPlayerError(ExoPlaybackException e) {
            String errorString = null;
            if (e.type == ExoPlaybackException.TYPE_RENDERER) {
                Exception cause = e.getRendererException();
                if (cause instanceof MediaCodecRenderer.DecoderInitializationException) {
                    // Special case for decoder initialization failures.
                    MediaCodecRenderer.DecoderInitializationException decoderInitializationException =
                            (MediaCodecRenderer.DecoderInitializationException) cause;
                    if (decoderInitializationException.decoderName == null) {
                        if (decoderInitializationException.getCause() instanceof MediaCodecUtil.DecoderQueryException) {
                            errorString = getString(R.string.error_querying_decoders);
                        } else if (decoderInitializationException.secureDecoderRequired) {
                            errorString = getString(R.string.error_no_secure_decoder,
                                    decoderInitializationException.mimeType);
                        } else {
                            errorString = getString(R.string.error_no_decoder,
                                    decoderInitializationException.mimeType);
                        }
                    } else {
                        errorString = getString(R.string.error_instantiating_decoder,
                                decoderInitializationException.decoderName);
                    }
                }
            }
            if (errorString != null) {
                setErrorListener(errorString);
            }
            inErrorState = true;
            updateResumePosition();
            updateButtonVisibilities();
            showControls();
        }

        @Override
        public void onPositionDiscontinuity(int i) {
            if (inErrorState) {
                // This will only occur if the user has performed a seek whilst in the error state. Update
                // the resume position so that if the user then retries, playback will resume from the
                // position to which they seeked.
                updateResumePosition();
            }
        }

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

        }

        @Override
        public void onSeekProcessed() {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();
        ConvivaTrackingManager.getInstance().stopTracking();
    }

    private void releasePlayer() {
        if (mPlayer != null) {
            mDebugTextViewHelper.stop();
            mDebugTextViewHelper = null;
            mShouldAutoPlay = mPlayer.getPlayWhenReady();
            updateResumePosition();
            mPlayer.release();
            mPlayer = null;
            mDefaultTrackSelector = null;
            mTrackSelectionHelper = null;
            mEventLogger = null;
        }
    }

    private void showControls() {
        mDebugView.setVisibility(View.GONE);
    }

    private void updateResumePosition() {
        mResumeWindow = mPlayer.getCurrentWindowIndex();
        mResumePosition = Math.max(0, mPlayer.getContentPosition());
    }

    private void setErrorListener(String msg) {
        showToast(msg);
    }
}
