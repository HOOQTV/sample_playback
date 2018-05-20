package tv.hooq.sampleapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import tv.hooq.sampleapp.R;

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<MovieModel> mMovies;
    private IListener mListener;

    public MovieAdapter(ArrayList<MovieModel> movies) {
        this.mMovies = movies;
    }

    public void setMovies(ArrayList<MovieModel> movies) {
        this.mMovies = movies;
    }

    public void setEmpty() {
        if(this.mMovies == null) {
            this.mMovies = new ArrayList<>();
        }
        this.mMovies.clear();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie, parent, false);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MovieModel content = (MovieModel) v.getTag();
                if (mListener != null) {
                    mListener.onItemSelected(content);
                }
            }
        });
        return new MovieViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MovieViewHolder viewHolder = (MovieViewHolder) holder;
        viewHolder.bind(mMovies.get(position));
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }


    public static class MovieViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.view_line)
        View mLine;

        @BindView(R.id.imageview_icon_movie)
        ImageView mIcon;

        @BindView(R.id.textview_title_movie)
        TextView mTitle;

        public MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(MovieModel movieModel) {
            itemView.setTag(movieModel);

            mTitle.setText(movieModel.getTitle());
            if(movieModel.getType() == MovieModel.TYPE_HEADER) {
                mLine.setVisibility(View.VISIBLE);
                mIcon.setVisibility(View.GONE);
                mTitle.setLines(1);
            } else {
                mLine.setVisibility(View.GONE);
                mIcon.setVisibility(View.VISIBLE);
                mTitle.setLines(2);
            }
        }
    }


    public static class MovieModel {

        public static final int TYPE_HEADER = 0;
        public static final int TYPE_ITEM = 1;

        private String title;
        private int type;
        private String image;
        private Object tag;



        public MovieModel() {

        }

        public MovieModel(int type, String title) {
            this.type = type;
            this.title = title;
        }

        public MovieModel(int type, String title, String image, Object tag) {
            this.type = type;
            this.title = title;
            this.image = image;
            this.tag = tag;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public Object getTag() {
            return tag;
        }

        public void setTag(Object tag) {
            this.tag = tag;
        }
    }

    public void setListener(IListener listener) {
        mListener = listener;
    }

    public interface IListener {
        void onItemSelected(MovieModel movie);
    }

}
