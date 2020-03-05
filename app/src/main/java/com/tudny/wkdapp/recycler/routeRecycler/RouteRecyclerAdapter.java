package com.tudny.wkdapp.recycler.routeRecycler;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tudny.wkdapp.R;

import java.util.List;

public class RouteRecyclerAdapter extends RecyclerView.Adapter<RouteRecyclerAdapter.ViewHolder> {

	private final List<RowModel> rowData;
	private final LayoutInflater inflater;
	private ItemClickListener itemClickListener;
	private RecyclerView recyclerView;

	public RouteRecyclerAdapter(Context context, List<RowModel> data){
		this.inflater = LayoutInflater.from(context);
		this.rowData = data;
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i){
		View view = inflater.inflate(R.layout.route_row_layout, viewGroup, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i){
		RowModel choose = rowData.get(i);

		int arrTypeface = -1;
		int depTypeface = -1;
		int stationTypeface = -1;

		switch(choose.boldType){
			case 1:
				arrTypeface = Typeface.NORMAL;
				depTypeface = Typeface.BOLD;
				break;
			case 2:
				arrTypeface = Typeface.BOLD;
				depTypeface = Typeface.BOLD;
				break;
			case 3:
				arrTypeface = Typeface.BOLD;
				depTypeface = Typeface.NORMAL;
				break;
			default:
				arrTypeface = Typeface.NORMAL;
				depTypeface = Typeface.NORMAL;
		}

		switch (choose.boldType){
			case 1: case 2: case 3:
				stationTypeface = Typeface.BOLD;
				break;
			default:
				stationTypeface = Typeface.NORMAL;
		}

		viewHolder.arrTime.setTypeface(null, arrTypeface);
		viewHolder.depTime.setTypeface(null, depTypeface);
		viewHolder.stationName.setTypeface(null, stationTypeface);

		viewHolder.stationName.setText(choose.stationName);
		viewHolder.arrTime.setText(choose.arrivalTime);
		viewHolder.depTime.setText(choose.departureTime);

		int image1 = 0;
		int image2 = 0;
		int dotImage = 0;

		switch (choose.boldType){
			case 1:
				image1 = R.drawable.route_line_part_inactive;
				image2 = R.drawable.route_line_part;
				break;
			case 2:
				image1 = R.drawable.route_line_part;
				image2 = R.drawable.route_line_part;
				break;
			case 3:
				image1 = R.drawable.route_line_part;
				image2 = R.drawable.route_line_part_inactive;
				break;

			default:
				image1 = R.drawable.route_line_part_inactive;
				image2 = R.drawable.route_line_part_inactive;
				break;
		}

		switch (choose.boldType){
			case 1: case 2: case 3:
				dotImage = R.drawable.active_station_icon;
				break;
			default:
				dotImage = R.drawable.inactive_station_icon;
				break;
		}

		viewHolder.lineOfDistanceImage1.setImageResource(image1);
		viewHolder.lineOfDistanceImage2.setImageResource(image2);
		viewHolder.dotOfStationImage.setImageResource(dotImage);

		int transparent = android.R.color.transparent;
		if(i == 0) viewHolder.lineOfDistanceImage1.setImageResource(transparent);
		if(i == getItemCount() - 1) viewHolder.lineOfDistanceImage2.setImageResource(transparent);
	}

	@Override
	public int getItemCount(){
		return rowData.size();
	}

	@Override
	public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
		super.onAttachedToRecyclerView(recyclerView);
		this.recyclerView = recyclerView;
	}

	public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

		final TextView stationName;
		final TextView arrTime;
		final TextView depTime;
		final ImageView lineOfDistanceImage1;
		final ImageView lineOfDistanceImage2;
		final ImageView dotOfStationImage;

		ViewHolder(View itemView){
			super(itemView);
			stationName = itemView.findViewById(R.id.station_name_text_view);
			arrTime = itemView.findViewById(R.id.arrival_text_view);
			depTime = itemView.findViewById(R.id.departure_text_view);
			lineOfDistanceImage1 = itemView.findViewById(R.id.line_of_distance_image_1);
			lineOfDistanceImage2 = itemView.findViewById(R.id.line_of_distance_image_2);
			dotOfStationImage = itemView.findViewById(R.id.line_of_distance_dot);
			itemView.setOnClickListener(this);
		}

		@Override
		public void onClick(View view){
			if(itemClickListener != null){
				itemClickListener.onItemClick(view, getAdapterPosition());
			}
		}
	}

	public RowModel getItem(int id){
		return rowData.get(id);
	}

	public void setClickListener(ItemClickListener itemClickListener){
		this.itemClickListener = itemClickListener;
	}

	public interface ItemClickListener {
		void onItemClick(View view, int position);
	}
}
