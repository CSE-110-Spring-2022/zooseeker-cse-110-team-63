package com.team63.zooseeker;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    private List<NodeInfo> prePlanItems = Collections.emptyList();
    public void setPrePlanItems(List<NodeInfo> newPrePlanItems) {
        this.prePlanItems.clear();
        this.prePlanItems = newPrePlanItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.plan_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setPrePlanItem(prePlanItems.get(position));
    }

    @Override
    public int getItemCount() {
        return prePlanItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private NodeInfo prePlanItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.plan_item_text);
        }

        public NodeInfo getPlanItem() { return prePlanItem; }

        public void setPrePlanItem(NodeInfo prePlanItem) {
            this.prePlanItem = prePlanItem;
            this.textView.setText(prePlanItem.name);
        }
    }
}
