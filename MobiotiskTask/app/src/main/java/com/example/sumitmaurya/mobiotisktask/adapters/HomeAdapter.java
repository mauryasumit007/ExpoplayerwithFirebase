package com.example.sumitmaurya.mobiotisktask.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sumitmaurya.mobiotisktask.R;
import com.example.sumitmaurya.mobiotisktask.models.Output;
import com.example.sumitmaurya.mobiotisktask.others.ItemClickListener;

import java.util.List;

public class HomeAdapter extends  RecyclerView.Adapter<HomeAdapter.HomeDataHolder> {

    Context context;
    ItemClickListener itemClickListener;

    List<Output> wikiList;

    public HomeAdapter(List<Output> wikiList, Context context) {
        this.wikiList = wikiList;
        this.context = context;
    }



    @NonNull
    @Override
    public HomeDataHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_homelayout,viewGroup,false);
        HomeDataHolder mh = new HomeDataHolder(view);
        return mh;
    }

    @Override
    public void onBindViewHolder(@NonNull final HomeDataHolder homeDataHolder, int i) {

homeDataHolder.tvTitle.setText(wikiList.get(i).getTitle());
homeDataHolder.tvotherinfo.setText(wikiList.get(i).getDescription());
        if(wikiList.get(i).getThumb()!=null){
            Glide.with(context).load(wikiList.get(i).getThumb()).into(homeDataHolder.ithumbimage);

        }
        homeDataHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClickListener != null)
                    itemClickListener.onClick(view,homeDataHolder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return wikiList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void setClickListener(ItemClickListener itemClickListener) {    //  Method for setting clicklistner interface
        this.itemClickListener = itemClickListener;

    }

    public class HomeDataHolder extends RecyclerView.ViewHolder {

        TextView tvTitle,tvotherinfo;
        ImageView ithumbimage;

        public HomeDataHolder(View v) {
            super(v);
            tvTitle = (TextView) v.findViewById(R.id.title);
            tvotherinfo = (TextView) v.findViewById(R.id.desc);
            ithumbimage = (ImageView) v.findViewById(R.id.imageView);
        }


    }
}
