package com.saahil.blogshare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private ArrayList<Posts> post;
    ItemClicked activity;

    public interface ItemClicked{
        void onItemClicked(int index);
    }

    public PostAdapter(Context context, ArrayList<Posts> list){
        post=list;
        activity=(ItemClicked) context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle, tvPublished, tvBody;
        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            tvTitle=itemView.findViewById(R.id.tvTitle);
            tvPublished=itemView.findViewById(R.id.tvPublished);
            tvBody=itemView.findViewById(R.id.tvBody);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.onItemClicked(post.indexOf((Posts) view.getTag()));
                }
            });
        }
    }

    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_items, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.ViewHolder viewHolder, int i) {
        viewHolder.itemView.setTag(post.get(i));
        viewHolder.tvTitle.setText(post.get(i).getTitle());
        viewHolder.tvBody.setText(post.get(i).getBody());
        viewHolder.tvPublished.setText("Published on "+post.get(i).getPublished());
    }

    @Override
    public int getItemCount() {
        return post.size();
    }
}
