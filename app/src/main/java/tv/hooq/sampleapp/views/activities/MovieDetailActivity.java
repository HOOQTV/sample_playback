package tv.hooq.sampleapp.views.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tv.hooq.sampleapp.R;
import tv.hooq.sampleapp.networks.NetworkAgent;
import tv.hooq.sampleapp.networks.responses.discover.DiscoverTitlesResponse;
import tv.hooq.sampleapp.networks.responses.discover.titles.Data;

public class MovieDetailActivity extends BaseActivity {
    public static final String EXTRA_MOVIE_ID = "EXTRA_MOVIE_ID";
    public static final String EXTRA_MOVIE_TITLE = "EXTRA_MOVIE_TITLE";

    @BindView(R.id.tv_title)
    TextView mTitleView;

    @BindView(R.id.tv_duration)
    TextView mDurationView;

    @BindView(R.id.tv_release_year)
    TextView mReleaseYearView;

    @BindView(R.id.tv_languages)
    TextView mLanguageView;

    @BindView(R.id.tv_audios)
    TextView mAudiosView;

    @BindView(R.id.tv_tags)
    TextView mTagsView;

    @BindView(R.id.tv_short_description)
    TextView mShortDescriptionView;

    @BindView(R.id.main_layout)
    RelativeLayout mMainLayout;

    @BindView(R.id.layout_loading)
    LinearLayout mLoadingView;

    @BindView(R.id.btn_play)
    Button mPlayBtn;

    @BindView(R.id.btn_episode)
    Button mEpisodeBtn;

    private Data mMovieData;

    @OnClick(R.id.btn_play)
    void playBtnOnClick() {
        if (mMovieData != null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(PlayerActivity.EXTRA_MOVIE, mMovieData);

            Intent intent = new Intent(mContext, PlayerActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else {
            showToast("UUID is empty");
        }
    }

    @OnClick(R.id.btn_episode)
    void episodeBtnOnClick() {
        showToast("This feature is currently unavailable");
    }

    private Context mContext = this;
    private String mUUID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        setLoading(true);
        Intent intent = getIntent();
        setTitle(intent.getStringExtra(EXTRA_MOVIE_TITLE));

        mUUID = intent.getStringExtra(EXTRA_MOVIE_ID);
        getDetail(mUUID);
    }

    private void setLoading(boolean isShow) {
        mLoadingView.setVisibility(isShow ? View.VISIBLE : View.GONE);
        mMainLayout.setVisibility(isShow ? View.GONE : View.VISIBLE);
    }

    private void getDetail(String uuid) {

        NetworkAgent.getInstance().discoverTitle(uuid).enqueue(new Callback<DiscoverTitlesResponse>() {
            @Override
            public void onResponse(Call<DiscoverTitlesResponse> call, Response<DiscoverTitlesResponse> response) {
                if(response.isSuccessful()) {
                    DiscoverTitlesResponse discoverTitlesResponse = response.body();
                    mMovieData = discoverTitlesResponse.getData();
                    mTitleView.setText(mMovieData.getTitle());
                    mShortDescriptionView.setText(Html.fromHtml("<b>Short Description:</b><br />" + mMovieData.getShortDescription()));
                        mDurationView.setText(Html.fromHtml("<b>Duration:</b><br />" + mMovieData.getRunningTimeFriendly()));
                        mReleaseYearView.setText(Html.fromHtml("<b>Release year:</b><br />" + Integer.toString(mMovieData.getMeta().getReleaseYear())));

                        String language = "-";
                        if (mMovieData.getLanguages() != null) {
                            language = "";
                            for (int i = 0; i < mMovieData.getLanguages().size(); i++) {
                                if (TextUtils.isEmpty(language)) {
                                    language = mMovieData.getLanguages().get(i).toString();
                                } else {
                                    language = language + ", " + mMovieData.getLanguages().get(i).toString();
                                }
                            }
                        }
                        mLanguageView.setText(Html.fromHtml("<b>Language:</b><br />" + language));

                        String audio = "-";
                        if (mMovieData.getAudios() != null) {
                            audio = "";
                            for (int i = 0; i < mMovieData.getAudios().size(); i++) {
                                if (TextUtils.isEmpty(audio)) {
                                    audio = mMovieData.getAudios().get(i);
                                } else {
                                    audio = audio + ", " + mMovieData.getAudios().get(i);
                                }
                            }
                        }
                        mAudiosView.setText(Html.fromHtml("<b>Audio:</b><br />" + audio));

                        String tags = "-";
                        if (mMovieData.getTags() != null) {
                            tags = "";
                            for (int i = 0; i < mMovieData.getTags().size(); i++) {
                                if (TextUtils.isEmpty(tags)) {
                                    tags = mMovieData.getTags().get(i).getLabel();
                                } else {
                                    tags = tags + ", " + mMovieData.getTags().get(i).getLabel();
                                }
                            }
                        }
                        mTagsView.setText(Html.fromHtml("<b>Genre:</b><br />" + tags));

                        if (mMovieData.getAs().equalsIgnoreCase("MOVIE")) {
                            mEpisodeBtn.setVisibility(View.GONE);
                        } else {
                            mEpisodeBtn.setVisibility(View.VISIBLE);
                        }
                }
                setLoading(false);
            }

            @Override
            public void onFailure(Call<DiscoverTitlesResponse> call, Throwable throwable) {
                setLoading(false);
            }
        });
    }
}
