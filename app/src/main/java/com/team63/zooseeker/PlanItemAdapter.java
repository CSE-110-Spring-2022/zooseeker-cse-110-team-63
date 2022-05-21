package com.team63.zooseeker;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class PlanItemAdapter extends RecyclerView.Adapter<PlanItemAdapter.ViewHolder> {
    private static final String ITEM_TEMPLATE = "%s (%d ft)";
    private List<Direction> planItems = new ArrayList<>();

    public void setPlanItems(List<Direction> newPlanItems) {
        this.planItems.clear();
        double sum = 0;
        for (int i = 0; i < newPlanItems.size(); i++) {
            newPlanItems.get(i).directionInfo.cumDistance =
                    Direction.roundDistance(sum)
                    + Direction.roundDistance(newPlanItems.get(i).directionInfo.distance);
            sum += newPlanItems.get(i).directionInfo.distance;
        }
        this.planItems.addAll(newPlanItems);
        this.planItems.remove(newPlanItems.size() - 1); // remove the gate item
        notifyDataSetChanged();
    }

    public void clearPlan()
    {
        this.planItems.clear();
        planItems = new ArrayList<>();
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

        holder.setPlanItem(planItems.get(position));
    }

    @Override
    public int getItemCount() {
        return planItems.size();
    }

    @Override
    public long getItemId(int position) { return planItems.get(position).directionInfo.id; }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private Direction planItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.plan_item_text);
        }
        public Direction getPlanItem() { return planItem; }

        public void setPlanItem(Direction planItem) {
            this.planItem = planItem;
            this.textView.setText(String.format(Locale.US, ITEM_TEMPLATE,
                    planItem.directionInfo.name,
                    planItem.directionInfo.cumDistance
            ));
        }
    }
}
