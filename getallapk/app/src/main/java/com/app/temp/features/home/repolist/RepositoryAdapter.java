package com.app.temp.features.home.repolist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.temp.R;
import com.app.temp.pojo.Repository;

import java.util.Collections;
import java.util.List;

/**
 * Created by nguyen_van_cuong on 05/12/2017.
 */

public class RepositoryAdapter extends RecyclerView.Adapter<RepositoryAdapter.RepositoryViewHolder> {

    List<Repository> repositories;
    Callback callback;

    public RepositoryAdapter(Callback callback) {
        this.callback = callback;
        this.repositories = Collections.emptyList();
    }

    public void setRepositories(List<Repository> repositories) {
        this.repositories = repositories;
    }

    @Override
    public RepositoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_repo, parent,false);
        RepositoryViewHolder viewHolder = new RepositoryViewHolder(itemView);
        viewHolder.lnContent.setOnClickListener(view -> callback.onItemClick(viewHolder.repository));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RepositoryViewHolder holder, int position) {
        holder.setRepository(repositories.get(position));
    }

    @Override
    public int getItemCount() {
        return repositories.size();
    }

    public static class RepositoryViewHolder extends RecyclerView.ViewHolder {

        Repository repository;

        LinearLayout lnContent;
        TextView tvTitle;
        TextView tvWatchers;
        TextView tvStars;
        TextView tvForks;

        public void setRepository(Repository repository) {
            this.repository = repository;

            tvTitle.setText(repository.getName());
            tvWatchers.setText(String.valueOf(repository.getWatchersCount()) + " watchers");
            tvStars.setText(String.valueOf(repository.getStargazersCount()) + " stars");
            tvForks.setText(String.valueOf(repository.getForksCount()) + " forks");
        }

        public RepositoryViewHolder(View itemView) {
            super(itemView);

            lnContent = itemView.findViewById(R.id.ln_content);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvWatchers = itemView.findViewById(R.id.tv_watchers);
            tvStars = itemView.findViewById(R.id.tv_stars);
            tvForks = itemView.findViewById(R.id.tv_forks);
        }
    }

    public interface Callback {
        void onItemClick(Repository repository);
    }
}
