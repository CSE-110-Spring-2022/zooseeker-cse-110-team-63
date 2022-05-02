package com.team63.zooseeker;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// https://www.youtube.com/watch?v=sJ-Z9G0SDhc
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private List<NodeInfo> searchItems = Collections.emptyList();
    private List<NodeInfo> searchResults = new ArrayList<NodeInfo>();
    private Consumer<NodeInfo> onItemClicked;
    public void setSearchItems(List<NodeInfo> newSearchItems) {
        this.searchItems.clear();
        this.searchItems = newSearchItems;
        this.searchResults = newSearchItems;
        notifyDataSetChanged();
    }

    public void setOnItemClickedHandler(Consumer<NodeInfo> onItemClicked) {
        this.onItemClicked = onItemClicked;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.search_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setPrePlanItem(searchResults.get(position));
    }

    @Override
    public int getItemCount() {
        return searchResults.size();
    }

    public Filter getFilter() {
        // credit to https://www.youtube.com/watch?v=sJ-Z9G0SDhc
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                List<NodeInfo> filteredExhibits = new ArrayList<NodeInfo>();
                if (constraint == null || constraint.length() == 0) {
                    filteredExhibits.addAll(searchItems);
                }
                else {
                    for (NodeInfo exhibit : searchItems) {
                        Pattern pattern = Pattern.compile(constraint.toString(), Pattern.CASE_INSENSITIVE);
                        // https://www.w3schools.com/java/java_regex.asp
                        Matcher matcher = pattern.matcher(exhibit.name);
                        if (matcher.find()) {
                            filteredExhibits.add(exhibit);
                        }
                    }
                }
                results.values = filteredExhibits;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                searchResults.clear();
                searchResults.addAll((List) results.values);
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final CheckBox checkBox;
        private final ConstraintLayout area;
        private NodeInfo searchItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.area = itemView.findViewById(R.id.search_item_area);
            this.checkBox = itemView.findViewById(R.id.search_item_checkbox);
            this.textView = itemView.findViewById(R.id.search_item_text);
            this.area.setOnClickListener(view -> {
                if (onItemClicked == null) return;
                onItemClicked.accept(searchItem);
            });
        }

        public NodeInfo getPlanItem() { return searchItem; }

        public void setPrePlanItem(NodeInfo searchItem) {
            this.searchItem = searchItem;
            this.checkBox.setChecked(searchItem.selected);
            this.textView.setText(searchItem.name);
        }
    }
}