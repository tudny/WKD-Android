package com.tudny.wkdapp.recycler.stationsRecycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tudny.wkdapp.R;

import java.util.List;

public class StationRecyclerAdapter extends RecyclerView.Adapter<StationRecyclerAdapter.ViewHolder> {

	private List<RowModel> rowData;
	private final LayoutInflater inflater;
	private ItemClickListener itemClickListener;
	private RecyclerView thisRecyclerView;

	public StationRecyclerAdapter(Context context, List<RowModel> data){
		this.inflater = LayoutInflater.from(context);
		this.rowData = data;
	}

	@NonNull
	@Override
	public StationRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		View view = inflater.inflate(R.layout.station_row_layout, viewGroup, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull StationRecyclerAdapter.ViewHolder viewHolder, int i) {
		RowModel choose = rowData.get(i);
		viewHolder.stationName.setText(choose.stationName);
	}

	@Override
	public void onAttachedToRecyclerView(RecyclerView recyclerView){
		super.onAttachedToRecyclerView(recyclerView);
		thisRecyclerView = recyclerView;
	}

	@Override
	public int getItemCount() {
		return rowData.size();
	}

	public void updateItems(List<RowModel> listValues){
		rowData = listValues;
		notifyDataSetChanged();
	}

	public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		final TextView stationName;

		ViewHolder(View itemView){
			super(itemView);
			stationName = itemView.findViewById(R.id.station_name);
			itemView.setOnClickListener(this);
		}

		@Override
		public void onClick(View view) {
			if(itemClickListener != null){
				itemClickListener.onItemClick(view,getAdapterPosition());
			}
		}
	}

	public RowModel getItem(int id){
		return rowData.get(id);
	}

	public void setItemClickListener(ItemClickListener itemClickListener){
		this.itemClickListener = itemClickListener;
	}

	public interface ItemClickListener {
		void onItemClick(View view, int position);
	}
}
