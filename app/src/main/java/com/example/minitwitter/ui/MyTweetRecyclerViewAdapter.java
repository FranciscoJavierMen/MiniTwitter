package com.example.minitwitter.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.minitwitter.R;
import com.example.minitwitter.common.Constantes;
import com.example.minitwitter.common.SharedPreferencesManager;
import com.example.minitwitter.data.TweetViewModel;
import com.example.minitwitter.retrofit.response.Like;
import com.example.minitwitter.retrofit.response.Tweet;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyTweetRecyclerViewAdapter extends RecyclerView.Adapter<MyTweetRecyclerViewAdapter.ViewHolder> {

    private List<Tweet> tweetList;
    private Context context;
    private String userName;
    private TweetViewModel tweetViewModel;

    public MyTweetRecyclerViewAdapter(Context context, List<Tweet> tweetList) {
        this.tweetList = tweetList;
        this.context = context;
        userName = SharedPreferencesManager.getSomeStringValue(Constantes.PREF_USERNAME);
        tweetViewModel = ViewModelProviders.of((FragmentActivity)context).get(TweetViewModel.class);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tweet, parent, false);
        return new ViewHolder(view);
    }

    public void setData(List<Tweet> lista){
        this.tweetList = lista;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (tweetList != null) {
            holder.mItem = tweetList.get(position);

            holder.txtUsername.setText("@"+holder.mItem.getUser().getUsername());
            holder.txtMessage.setText(holder.mItem.getMensaje());
            holder.txtLikesCount.setText(String.valueOf(holder.mItem.getLikes().size()));

            String photo = holder.mItem.getUser().getPhotoUrl();
            if (!holder.mItem.getUser().getPhotoUrl().equals("")) {
                Glide.with(context)
                        .load("https://www.minitwitter.com/apiv1/uploads/photos/" + photo)
                        .into(holder.imgAvatar);
            }

            Glide.with(context)
                    .load(R.drawable.ic_like)
                    .into(holder.imgLike);

            holder.txtLikesCount.setTextColor(context.getResources().getColor(R.color.blueColorDark));
            holder.txtLikesCount.setTypeface(null, Typeface.NORMAL);
            holder.imgLike.setOnClickListener(v -> tweetViewModel.likeTweet(holder.mItem.getId()));
            holder.imgOptions.setOnClickListener(view -> tweetViewModel.openDialogMenu(context, holder.mItem.getId()));
            holder.imgOptions.setVisibility(View.GONE);

            if (holder.mItem.getUser().getUsername().equals(userName)){
                holder.imgOptions.setVisibility(View.VISIBLE);
            }
            for (Like like : holder.mItem.getLikes()) {
                if (like.getUsername().equals(userName)) {
                    Glide.with(context)
                            .load(R.drawable.ic_like_pink)
                            .into(holder.imgLike);

                    holder.txtLikesCount.setTextColor(context.getResources().getColor(R.color.colorPink));
                    holder.txtLikesCount.setTypeface(null, Typeface.BOLD);
                    break;
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        if (tweetList != null){
            return tweetList.size();
        }
        return 0;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private final CircleImageView imgAvatar;
        private final ImageView imgLike;
        private final ImageView imgOptions;
        private final TextView txtUsername;
        private final TextView txtMessage;
        private final TextView txtLikesCount;
        Tweet mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            imgAvatar = view.findViewById(R.id.imgAvatar);
            imgLike = view.findViewById(R.id.imgLike);
            imgOptions = view.findViewById(R.id.imgOptions);
            txtUsername = view.findViewById(R.id.txtUsername);
            txtMessage = view.findViewById(R.id.txtMessage);
            txtLikesCount = view.findViewById(R.id.txtLikesCount);
        }
    }
}
