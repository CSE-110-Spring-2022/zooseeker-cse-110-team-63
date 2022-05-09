package com.team63.zooseeker;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// credit to
// https://www.youtube.com/watch?v=sJ-Z9G0SDhc
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private List<NodeInfo> searchItems = new ArrayList<>();
    private List<NodeInfo> searchResults = searchItems; // make it point to searchItems by default

    private Consumer<NodeInfo> onItemClicked;
    public void setSearchItems(List<NodeInfo> newSearchItems) {
        searchItems.clear();
        searchItems.addAll(newSearchItems);
        updateSearchResults();
        notifyDataSetChanged();
    }

    // don't forget, we have to update the search results too,
    // or else after updating the data we will see all results despite having
    // a search query
    private void updateSearchResults() {
        HashMap<String, NodeInfo> searchItemsMap = new HashMap<>();
        for (NodeInfo item : searchItems) {
            searchItemsMap.put(item.id, item);
        }
        List<NodeInfo> newSearchResults = new ArrayList<>();
        for (NodeInfo oldResult : searchResults) {
            newSearchResults.add(searchItemsMap.get(oldResult.id));
        }
        searchResults = newSearchResults;
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
                List<NodeInfo> filteredExhibits = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredExhibits.addAll(searchItems);
                }
                else {
                    for (NodeInfo exhibit : searchItems) {
                        Pattern pattern = Pattern.compile(constraint.toString(), Pattern.CASE_INSENSITIVE);
                        // https://www.w3schools.com/java/java_regex.asp
                        Matcher matcher1 = pattern.matcher(exhibit.name);
                        Matcher matcher2 = pattern.matcher(exhibit.concatTags);
                        if (matcher1.find() || matcher2.find()) {
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
        private final ConstraintLayout area;
        private NodeInfo searchItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.area = itemView.findViewById(R.id.search_item_area);
            this.textView = itemView.findViewById(R.id.search_item_text);
            this.area.setOnClickListener(view -> {
                if (onItemClicked == null) return;
                onItemClicked.accept(searchItem);
                ((Activity)view.getContext()).finish();
            });
        }

        public NodeInfo getPlanItem() { return searchItem; }

        public void setPrePlanItem(NodeInfo searchItem) {
            this.searchItem = searchItem;
            this.textView.setText(searchItem.name);
        }
    }
}