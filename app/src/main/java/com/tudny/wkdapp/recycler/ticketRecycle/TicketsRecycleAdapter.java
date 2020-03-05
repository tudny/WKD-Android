package com.tudny.wkdapp.recycler.ticketRecycle;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tudny.wkdapp.R;

import java.util.List;
import java.util.Objects;

public class TicketsRecycleAdapter extends RecyclerView.Adapter<TicketsRecycleAdapter.ViewHolder> {

	private List<RowModel> rowData;
	private final LayoutInflater inflater;
	private ItemClickListener itemClickListener;
	private RecyclerView thisRecyclerView;

	public TicketsRecycleAdapter(Context context,List<RowModel> data){
		this.inflater = LayoutInflater.from(context);
		this.rowData = data;
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i){
		View view = inflater.inflate(R.layout.ticket_row_layout, viewGroup, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i){
		RowModel choose = rowData.get(i);
		viewHolder.reliefText.setText(Html.fromHtml(choose.relief, Html.FROM_HTML_MODE_LEGACY));
		viewHolder.zone1Text.setText(Html.fromHtml(choose.zone1, Html.FROM_HTML_MODE_LEGACY));
		viewHolder.zone2Text.setText(Html.fromHtml(choose.zone2, Html.FROM_HTML_MODE_LEGACY));
		viewHolder.zone3Text.setText(Html.fromHtml(choose.zone3, Html.FROM_HTML_MODE_LEGACY));
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

	public void updateItems(List<RowModel> listValues) {
		rowData = listValues;

		runLayoutAnimation();
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

		final TextView reliefText;
		final TextView zone1Text;
		final TextView zone2Text;
		final TextView zone3Text;

		ViewHolder(View itemView){
			super(itemView);
			reliefText = itemView.findViewById(R.id.reliefTextView);
			zone1Text = itemView.findViewById(R.id.firstZoneTextView);
			zone2Text = itemView.findViewById(R.id.secondZoneTextView);
			zone3Text = itemView.findViewById(R.id.thirdZoneTextView);
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

	public void setItemClickListener(ItemClickListener itemClickListener){
		this.itemClickListener = itemClickListener;
	}

	public interface ItemClickListener {
		void onItemClick(View view, int position);
	}
}
