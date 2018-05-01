package com.example.iulian.newsportal.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.iulian.newsportal.Common.Common;
import com.example.iulian.newsportal.Interface.FaviconService;
import com.example.iulian.newsportal.Interface.ItemClickListener;
import com.example.iulian.newsportal.ListNews;
import com.example.iulian.newsportal.Model.Favicon;
import com.example.iulian.newsportal.Model.WebSite;
import com.example.iulian.newsportal.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Iulian on 4/24/2018
 */

class ListSourceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private ItemClickListener itemClickListener;
    protected TextView source_title;
    protected CircleImageView source_image;

    ListSourceViewHolder(View itemView) {
        super(itemView);

        source_image =  itemView.findViewById(R.id.source_image);
        source_title =  itemView.findViewById(R.id.source_name);

        itemView.setOnClickListener(this);
    }

    public ItemClickListener getItemClickListener() {
        return itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}

public class ListSourceAdapter extends  RecyclerView.Adapter<ListSourceViewHolder>{
    private Context context;
    private WebSite webSite;
    private FaviconService mService;

    public ListSourceAdapter(Context context, WebSite webSite) {
        this.context = context;
        this.webSite = webSite;

        mService = Common.getIconService();
    }

    @NonNull
    @Override
    public ListSourceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.source_layout, parent, false);
        return new ListSourceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListSourceViewHolder holder, int position) {
        StringBuilder iconsApi = new StringBuilder("https://besticon-demo.herokuapp.com/allicons.json?url=");
        iconsApi.append(webSite.getSources().get(position).getUrl());

        mService.getIconUrl(iconsApi.toString()).enqueue(new Callback<Favicon>() {
            @Override
            public void onResponse(Call<Favicon> call, Response<Favicon> response) {
                if(response.body() != null && response.body().getIcons().size() > 0)
                {
                    Picasso.get()
                            .load(response.body().getIcons().get(0).getUrl())
                            .into(holder.source_image);
                }

            }

            @Override
            public void onFailure(Call<Favicon> call, Throwable t) {

            }
        });

        holder.source_title.setText(webSite.getSources().get(position).getName());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Intent intent = new Intent(context, ListNews.class);
                intent.putExtra("source", webSite.getSources().get(position).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return  webSite != null ? webSite.getSources().size() : 0;
    }
}

