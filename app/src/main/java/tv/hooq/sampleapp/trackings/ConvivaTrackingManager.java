package tv.hooq.sampleapp.trackings;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.conviva.api.AndroidSystemInterfaceFactory;
import com.conviva.api.Client;
import com.conviva.api.ClientSettings;
import com.conviva.api.ContentMetadata;
import com.conviva.api.ConvivaException;
import com.conviva.api.SystemFactory;
import com.conviva.api.SystemSettings;
import com.conviva.api.player.PlayerStateManager;
import com.conviva.api.system.SystemInterface;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerLibraryInfo;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;

import java.util.HashMap;

import tv.hooq.sampleapp.BuildConfig;
import tv.hooq.sampleapp.SampleApplication;
import tv.hooq.sampleapp.commons.Utils;

/**
 * Tracking Video statistic using Conviva
 * Created by endiirawan on 5/15/18.
 */
public class ConvivaTrackingManager {
    private static ConvivaTrackingManager INSTANCE;
    private Client mClient;
    private int mSessionId = -1;
    private PlayerStateManager mStateManager = null;

    public static ConvivaTrackingManager getInstance() {
        if (INSTANCE == null)
            INSTANCE = new ConvivaTrackingManager();
        return INSTANCE;
    }

    private ConvivaTrackingManager() {
        // Setup Conviva
        Context context = SampleApplication.getInstance().getApplicationContext();
        SystemInterface androidSystemInterface = AndroidSystemInterfaceFactory.buildSecure(context);
        if (androidSystemInterface.isInitialized()) {
            SystemSettings systemSettings = new SystemSettings();
            systemSettings.allowUncaughtExceptions = false;

            // Assign a Key & Gateway Url
            final String customerKey, gatewayUrl;
            if (BuildConfig.DEBUG) {
                customerKey = Utils.getConvivaDebugKey();
                gatewayUrl = Utils.getConvivaDebugGatewayUrl();
                systemSettings.logLevel = SystemSettings.LogLevel.DEBUG;
            } else {
                customerKey = ""; // Release key here
                gatewayUrl = ""; // Release gateway url here
                systemSettings.logLevel = SystemSettings.LogLevel.INFO;
            }

            SystemFactory androidSystemFactory = new SystemFactory(androidSystemInterface, systemSettings);

            ClientSettings clientSettings = new ClientSettings(customerKey);
            clientSettings.heartbeatInterval = 5;
            clientSettings.gatewayUrl = gatewayUrl;
            mClient = new Client(clientSettings, androidSystemFactory);
        }
    }

    private ContentMetadata createMetaData(Context context, PlayManifest manifest) {
        ContentMetadata convivaMetaData = new ContentMetadata();

        // Conviva Default Metadata
        convivaMetaData.applicationName = "Sample App from HOOQ";
        convivaMetaData.streamType = ContentMetadata.StreamType.VOD;
        convivaMetaData.viewerId = manifest.getUserId();
        convivaMetaData.streamUrl = manifest.getStreamUrl();
        convivaMetaData.duration = manifest.getDuration() / 1000; // in seconds
        convivaMetaData.assetName = manifest.getTitle();

        // Custom Metadata
        convivaMetaData.custom = new HashMap<>();
        convivaMetaData.custom.put("streamProtocol", "DASH");
        convivaMetaData.custom.put("connectionType", Utils.getConnectionTypeString(context));
        convivaMetaData.custom.put("playerVendor", "ExoPlayer");
        convivaMetaData.custom.put("playerVersion", "1.0");
        convivaMetaData.custom.put("accessType", manifest.getAccessType());
        convivaMetaData.custom.put("show", manifest.getTitle());

        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (manager != null) {
            String carrierName = manager.getNetworkOperatorName();
            if (!TextUtils.isEmpty(carrierName)) {
                convivaMetaData.custom.put("carrier", carrierName);
            }
        }

        return convivaMetaData;
    }

    private void trackOnSeekEnd() {
        try {
            if (null != mStateManager) {
                mStateManager.setPlayerSeekEnd();
            }
        } catch (ConvivaException e) {
            e.printStackTrace();
        }
    }

    /**
     * Start new Conviva session
     *
     * @param title movie/tvshows
     */
    public void startTracking(PlayManifest title, ExoPlayer player) {
        try {
            player.addListener(exoListener);

            Context context = SampleApplication.getInstance().getApplicationContext();
            ContentMetadata convivaMetaData = createMetaData(context, title);

            // Stop previous conviva session
            stopTracking();
            // Create new Conviva Session
            mSessionId = mClient.createSession(convivaMetaData);
            mStateManager = mClient.getPlayerStateManager(); // Create new state manager, call this function only once
            mStateManager.setPlayerVersion(ExoPlayerLibraryInfo.VERSION);
            mStateManager.setPlayerType("ExoPlayer");

            if (!TextUtils.isEmpty(convivaMetaData.streamUrl)) // if streamUrl still null, it means playback not yet started
                mClient.attachPlayer(mSessionId, mStateManager);

        } catch (ConvivaException e) {
            e.printStackTrace();
        }
    }

    /**
     * Stop current Conviva Session
     */
    public void stopTracking() {
        try {
            if (mSessionId != -1) {
                mClient.detachPlayer(mSessionId);
                mClient.cleanupSession(mSessionId);
                mSessionId = -1;
            }
            if (null != mStateManager) {
                mClient.releasePlayerStateManager(mStateManager);
                mStateManager.reset();
            }
        } catch (ConvivaException e) {
            e.printStackTrace();
        }
    }

    /**
     * Track when user seek the progress
     *
     * @param position
     */
    public void trackOnSeekStarted(int position) {
        try {
            if (null != mStateManager) {
                mStateManager.setPlayerSeekStart(position);
            }
        } catch (ConvivaException e) {
            e.printStackTrace();
        }
    }

    /**
     * ExoPlayer Event Listener
     */
    private Player.EventListener exoListener = new Player.EventListener() {

        @Override
        public void onTimelineChanged(Timeline timeline, Object o) {
        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroupArray, TrackSelectionArray trackSelectionArray) {
        }

        @Override
        public void onLoadingChanged(boolean b) {
        }

        @Override
        public void onRepeatModeChanged(int i) {
        }

        @Override
        public void onShuffleModeEnabledChanged(boolean b) {

        }

        @Override
        public void onPlayerError(ExoPlaybackException e) {
            try {
                if (mStateManager != null) {
                    mStateManager.setPlayerState(PlayerStateManager.PlayerState.STOPPED);
                    String errorMessage = e.getMessage();
                    if (errorMessage == null)
                        errorMessage = "unknown";
                    mStateManager.sendError(errorMessage, Client.ErrorSeverity.FATAL);
                }
            } catch (ConvivaException e1) {
                e1.printStackTrace();
            }
        }

        @Override
        public void onPositionDiscontinuity(int i) {

        }

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
        }

        @Override
        public void onSeekProcessed() {
            trackOnSeekEnd();
        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            try {
                if (mStateManager == null) return;

                switch (playbackState) {
                    case Player.STATE_BUFFERING:
                        mStateManager.setPlayerState(PlayerStateManager.PlayerState.BUFFERING);
                        break;
                    case Player.STATE_ENDED:
                        mStateManager.setPlayerState(PlayerStateManager.PlayerState.STOPPED);
                        break;
                    case Player.STATE_IDLE:
                        break;
                    case Player.STATE_READY:
                        if (playWhenReady) {
                            mStateManager.setPlayerState(PlayerStateManager.PlayerState.PLAYING);
                        } else {
                            mStateManager.setPlayerState(PlayerStateManager.PlayerState.PAUSED);
                        }
                        break;
                    default:
                        break;
                }
            } catch (ConvivaException e) {
                e.printStackTrace();
            }
        }

    };
}
