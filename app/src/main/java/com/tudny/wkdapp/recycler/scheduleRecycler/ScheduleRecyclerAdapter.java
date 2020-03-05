package com.tudny.wkdapp.recycler.scheduleRecycler;


import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tudny.wkdapp.R;
import com.tudny.wkdapp.core.data.Route;
import com.tudny.wkdapp.core.data.Schedule;

import java.util.List;
import java.util.Objects;

public class ScheduleRecyclerAdapter extends RecyclerView.Adapter<ScheduleRecyclerAdapter.ViewHolder> {

	private List<RowModel> rowData;
	private final LayoutInflater inflater;
	private ItemClickListener itemClickListener;
	private RecyclerView thisRecyclerView;
	private Schedule schedule;

	public ScheduleRecyclerAdapter(Context context, List<RowModel> data){
		this.inflater = LayoutInflater.from(context);
		this.rowData = data;
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		View view = inflater.inflate(R.layout.schedule_row_layout, viewGroup, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
		RowModel choose = rowData.get(i);
		viewHolder.depTime.setText(choose.departureTime);
		viewHolder.timeToDep.setText(choose.timeToDeparture);
		if(i == 0){
			viewHolder.depTime.setTypeface(null, Typeface.BOLD);
			viewHolder.timeToDep.setTypeface(null, Typeface.BOLD);
		}
	}

	@Override
	public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
		super.onAttachedToRecyclerView(recyclerView);
		thisRecyclerView = recyclerView;
	}

	@Override
	public int getItemCount(){
		return rowData.size();
	}

	public void updateItems(List<RowModel> listValues, Schedule schedule, Boolean playAnimation){
		rowData = listValues;
		this.schedule = schedule;
		if(playAnimation == Boolean.TRUE) runLayoutAnimation();
		notifyDataSetChanged();
	}

	private void runLayoutAnimation() {
		final RecyclerView recyclerView = thisRecyclerView;

		final Context context = recyclerView.getContext();
		final LayoutAnimationController controller =
				AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down);

		recyclerView.setLayoutAnimation(controller);
		Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
		recyclerView.scheduleLayoutAnimation();
	}

	public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

		final TextView depTime;
		final TextView timeToDep;

		ViewHolder(@NonNull View itemView) {
			super(itemView);
			depTime = itemView.findViewById(R.id.depTime);
			timeToDep = itemView.findViewById(R.id.timeToDep);
			itemView.setOnClickListener(this);
		}

		@Override
		public void onClick(View view) {
			if(itemClickListener != null){
				itemClickListener.onItemClick(view, getAdapterPosition());
			}
		}
	}

	public RowModel getItem(int id){
		return rowData.get(id);
	}

	public Route getRoute(int id){
		return schedule.getRoute().get(id);
	}

	@SuppressWarnings("unused")
	public Schedule getSchedule(){
		return schedule;
	}

	public void setClickListener(ItemClickListener itemClickListener){
		this.itemClickListener = itemClickListener;
	}

	public interface ItemClickListener {
		void onItemClick(View view, int position);
	}
}
