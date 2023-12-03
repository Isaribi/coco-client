package com.ndurska.coco_client.calendar;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ndurska.coco_client.R;
import com.ndurska.coco_client.database.dto.DogDto;
import com.ndurska.coco_client.database.web.DogsRequestDispatcher;

import java.util.ArrayList;
import java.util.List;

public class ChooseMultipleClientsAdapter extends RecyclerView.Adapter<ChooseDogVH> {
    List<DogDto> items;
    Context context;
    DogsRequestDispatcher dogsRequestDispatcher;
    DogDto activeDog;
    List<DogDto> chosenDogs;
    int clickedPosition = -1;


    public ChooseMultipleClientsAdapter(List<DogDto> items, Context context) {
        this.items = items;
        this.context = context;
        this.chosenDogs = new ArrayList<>();

        dogsRequestDispatcher = new DogsRequestDispatcher();

    }

    @NonNull
    @Override
    public ChooseDogVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.choose_client_card_item, parent, false);

        return new ChooseDogVH(view).linkAdapter(this);

    }

    @Override
    public void onBindViewHolder(@NonNull ChooseDogVH holder, int position) {

        DogDto client = items.get(position);
        holder.tvDogID.setText(String.valueOf(client.getId()));
        String clientName = client.getName() + " " + client.getPseudonym() + " " + client.getBreed();
        holder.tvDogName.setText(clientName);
        holder.tvDogName.setAutoSizeTextTypeUniformWithConfiguration(1, 17, 1, TypedValue.COMPLEX_UNIT_DIP);

        holder.itemView.setOnClickListener(view -> {
            try {
                clickedPosition = holder.getAdapterPosition();
                activeDog = items.get(clickedPosition);
                if (!chosenDogs.contains(activeDog))
                    chosenDogs.add(activeDog);
                else
                    chosenDogs.remove(activeDog);
                notifyItemChanged(clickedPosition);

            } catch (Exception e) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            }
        });

        //set color of selected clients
        if (chosenDogs.contains(items.get(position)))
            setDrawableColor(holder.itemView.getContext(), holder.itemView.getBackground(), R.color.color_coco_2);
        else
            setDrawableColor(holder.itemView.getContext(), holder.itemView.getBackground(), R.color.color_coco_4);
    }

    /**
     * Changes background color of drawable without losing the shape
     */
    public static void setDrawableColor(Context context, Drawable drawable, int color) {
        Drawable drawableWrap = DrawableCompat.wrap(drawable).mutate();
        DrawableCompat.setTint(drawableWrap, ContextCompat.getColor(context, color));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public List<DogDto> getChosenDogs() {
        return chosenDogs;
    }
}

class ChooseDogVH extends RecyclerView.ViewHolder {

    TextView tvDogID;
    TextView tvDogName;

    public ChooseDogVH(@NonNull View itemView) {
        super(itemView);
        tvDogName = itemView.findViewById(R.id.tvClientCardItemName);
        tvDogID = itemView.findViewById(R.id.tvClientCardItemID);
    }

    public ChooseDogVH linkAdapter(ChooseMultipleClientsAdapter adapter) {
        return this;

    }
}
