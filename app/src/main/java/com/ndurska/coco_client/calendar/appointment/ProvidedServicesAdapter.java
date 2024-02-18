package com.ndurska.coco_client.calendar.appointment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ndurska.coco_client.R;
import com.ndurska.coco_client.calendar.appointment.dto.ProvidedService;

import java.util.List;

public class ProvidedServicesAdapter extends RecyclerView.Adapter<ProvidedServiceVH> {

    private final Context context;
    private final List<ProvidedService> services;


    public ProvidedServicesAdapter(Context context, List<ProvidedService> services) {
        this.context = context;
        this.services = services;

    }

    @NonNull
    @Override
    public ProvidedServiceVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_item, parent, false);

        return new ProvidedServiceVH(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull ProvidedServiceVH holder, int position) {

        holder.setIsRecyclable(false);

        ProvidedService service = services.get(position);

        holder.tvName.setText(service.getName());
        holder.tvMinPrice.setText(String.valueOf(service.getPriceMin()));
        holder.tvMaxPrice.setText(String.valueOf(service.getPriceMax()));
        holder.tvNotes.setText(service.getAdditionalInfo());

    }

    @Override
    public int getItemCount() {
        return services.size();
    }


}

class ProvidedServiceVH extends RecyclerView.ViewHolder {

    TextView tvName;
    TextView tvMinPrice;
    TextView tvMaxPrice;
    TextView tvNotes;

    private ProvidedServicesAdapter adapter;

    public ProvidedServiceVH(@NonNull View itemView) {
        super(itemView);
        tvName = itemView.findViewById(R.id.tvServiceName);
        tvMinPrice = itemView.findViewById(R.id.tvServiceMinPrice);
        tvMaxPrice = itemView.findViewById(R.id.tvServiceMaxPrice);
        tvNotes = itemView.findViewById(R.id.tvServiceNotes);
    }

    public ProvidedServiceVH linkAdapter(ProvidedServicesAdapter adapter) {
        this.adapter = adapter;
        return this;
    }

}