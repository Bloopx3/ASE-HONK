package bloop.honk.View;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import bloop.honk.Model.CarPark;
import bloop.honk.R;

public class CarParkAdapter extends RecyclerView.Adapter<CarParkAdapter.MyViewHolder> {
    private Context context;
    public List<CarPark> data = Collections.emptyList();
    private LayoutInflater inflater;
    private CarParkAdapter.ItemClickListener mClickListener;

    public CarParkAdapter(Context context, List<CarPark> data) {
        inflater = LayoutInflater.from(context);
        //data.clear();
        this.data = data;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.carparks_info, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CarParkAdapter.MyViewHolder holder, int position) {
        final CarPark current = data.get(position);
        holder.Development.setText(current.getDevelopment());
        holder.AvailableLots.setText(" "+current.getAvailableLots());
        holder.Type.setText("Lots type: "+current.getLotType());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView Development;
        TextView AvailableLots;
        TextView Type;

        public MyViewHolder(View itemView) {
            super(itemView);
            Development = itemView.findViewById(R.id.carpark_development);
            AvailableLots = itemView.findViewById(R.id.carpark_availableLots);
            Type = itemView.findViewById(R.id.carpark_type);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public CarPark getItem(int id) {
        return data.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(CarParkAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
