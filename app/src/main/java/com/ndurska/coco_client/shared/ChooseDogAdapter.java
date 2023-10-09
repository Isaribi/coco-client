package com.ndurska.coco_client.shared;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ndurska.coco_client.R;
import com.ndurska.coco_client.database.Dog;
import com.ndurska.coco_client.database.DogDto;

import java.util.List;


public class ChooseDogAdapter extends RecyclerView.Adapter<ChooseDogAdapter.DogCardVH> {
    public List<DogDto> items;
    Context context;
    RequestDispatcher requestDispatcher;

    DogDto activeDog;
    public int clickedPosition = -1;
    int prevClickedPosition = -1;

    private ChooseDogAdapterListener listener;

    public interface ChooseDogAdapterListener {
        void onRecyclerItemClicked(DogDto dog);

    }

    public ChooseDogAdapter(List<DogDto> items, Context context) {
        this.items = items;
        this.context = context;

        requestDispatcher = new RequestDispatcher(context);


    }


    @NonNull
    @Override
    public DogCardVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dog_card_item, parent, false);
        if (context instanceof ChooseDogAdapterListener)
            listener = (ChooseDogAdapterListener) context;

        return new DogCardVH(view).linkAdapter(this);

    }

    @Override
    public void onBindViewHolder(@NonNull DogCardVH holder, int position) {
        try {
            DogDto dog = items.get(position);
            holder.tvDogID.setText(String.valueOf(dog.getId()));
            StringBuffer dogName = new StringBuffer(dog.getName() + " " + dog.getPseudonym() + " " + dog.getBreed());
            holder.tvDogName.setText(dogName);

        } catch (Exception e) {
            holder.tvDogID.setText("ERR");
            holder.tvDogName.setText(items.get(position).getName());
        }

        holder.itemView.setOnClickListener(view -> {

            //todo check this change form getAbsoluteAdapterPosition
            activeDog = items.get(holder.getAdapterPosition());

            setClickedDog(activeDog);
            listener.onRecyclerItemClicked(activeDog);


        });

        CardView card = (CardView) holder.itemView;
        if (clickedPosition == position) {
            card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.color_coco_2));
        } else {
            card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.color_coco_4));
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setClickedDog(DogDto client) {

        prevClickedPosition = clickedPosition;
        clickedPosition = items.indexOf(client);
        notifyItemChanged(prevClickedPosition);
        notifyItemChanged(clickedPosition);

    }

    class DogCardVH extends RecyclerView.ViewHolder {

        TextView tvDogID;
        TextView tvDogName;

        private ChooseDogAdapter adapter;

        public DogCardVH(@NonNull View itemView) {
            super(itemView);
            tvDogName = itemView.findViewById(R.id.tvDogCardItemName);
            tvDogID = itemView.findViewById(R.id.tvDogCardItemID);

        }

        public DogCardVH linkAdapter(ChooseDogAdapter adapter) {
            this.adapter = adapter;
            return this;

        }

    }
}

