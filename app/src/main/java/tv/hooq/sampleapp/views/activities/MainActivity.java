package tv.hooq.sampleapp.views.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tv.hooq.sampleapp.R;
import tv.hooq.sampleapp.adapters.MovieAdapter;
import tv.hooq.sampleapp.networks.NetworkAgent;
import tv.hooq.sampleapp.networks.responses.discover.feed.Content;
import tv.hooq.sampleapp.networks.responses.discover.feed.Data;
import tv.hooq.sampleapp.networks.responses.discover.DiscoverFeedResponse;
import tv.hooq.sampleapp.networks.responses.signout.SignoutResponse;

public class MainActivity extends BaseActivity {
    @BindView(R.id.listview_movie)
    RecyclerView mListView;

    @BindView(R.id.layout_loading)
    LinearLayout mLoading;

    private Context mContext = this;
    private MovieAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_logout: {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Logout")
                        .setMessage("Are you sure?")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                setLoading(true);
                                NetworkAgent.getInstance().signout().enqueue(new Callback<SignoutResponse>() {
                                    @Override
                                    public void onResponse(Call<SignoutResponse> call, Response<SignoutResponse> response) {
                                        gotoLoginPage();
                                    }

                                    @Override
                                    public void onFailure(Call<SignoutResponse> call, Throwable throwable) {
                                        gotoLoginPage();
                                    }
                                });
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, null)
                        .create()
                        .show();
            }
            break;
        }
        return true;
    }

    private void gotoLoginPage() {
        setLoading(false);
        Intent intent = new Intent(mContext, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void initView() {
        setTitle("Discover Web");
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mListView.setLayoutManager(layoutManager);

        getDiscoveryPage();
    }

    private void getDiscoveryPage() {
        setLoading(true);

        NetworkAgent.getInstance().discoverFeed(1, 10).enqueue(new Callback<DiscoverFeedResponse>() {
            @Override
            public void onResponse(Call<DiscoverFeedResponse> call, Response<DiscoverFeedResponse> response) {
                if(response.isSuccessful()) {
                    updateList(response.body());
                }
                setLoading(false);
            }

            @Override
            public void onFailure(Call<DiscoverFeedResponse> call, Throwable throwable) {
                setLoading(false);
            }
        });
    }

    private void updateList(DiscoverFeedResponse discoverFeedResponse) {
        ArrayList<MovieAdapter.MovieModel> movieModels = new ArrayList<>();
        MovieAdapter.MovieModel movieModel;

        for (int i = 0; i < discoverFeedResponse.getData().size(); i++) {
            Data discoverFeedData = discoverFeedResponse.getData().get(i);
            if (discoverFeedData.getData() != null && discoverFeedData.getData().size() > 0) {
                movieModel = new MovieAdapter.MovieModel(MovieAdapter.MovieModel.TYPE_HEADER, discoverFeedData.getRowName());
                movieModels.add(movieModel);

                for (int j = 0; j < discoverFeedData.getData().size(); j++) {
                    Content content = discoverFeedData.getData().get(j);

                    String imageUrl = null;
                    if(content.getImages() != null && content.getImages().size() > 0) {
                        imageUrl = content.getImages().get(0).getUrl();
                    }
                    movieModel = new MovieAdapter.MovieModel(MovieAdapter.MovieModel.TYPE_ITEM, content.getTitle(), imageUrl, content);
                    movieModels.add(movieModel);
                }
            }
        }

        if (mAdapter == null) {
            mAdapter = new MovieAdapter(movieModels);
            mAdapter.setListener(new MovieAdapter.IListener() {
                @Override
                public void onItemSelected(MovieAdapter.MovieModel movie) {
                    if (movie.getType() == MovieAdapter.MovieModel.TYPE_ITEM) {
                        Content title = (Content) movie.getTag();
                        gotoDetailPage(title.getId(), title.getTitle());
                    }
                }
            });
            mListView.setAdapter(mAdapter);
        } else {
            mAdapter.setMovies(movieModels);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void gotoDetailPage(String id, String title) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(MovieDetailActivity.EXTRA_MOVIE_ID, id);
        intent.putExtra(MovieDetailActivity.EXTRA_MOVIE_TITLE, title);
        startActivity(intent);
    }

    private void setLoading(boolean isShow) {
        mLoading.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }
}
