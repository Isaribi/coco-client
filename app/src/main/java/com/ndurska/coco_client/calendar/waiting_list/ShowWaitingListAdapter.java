package com.ndurska.coco_client.calendar.waiting_list;

import static com.ndurska.coco_client.calendar.CalendarActivity.executorService;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ndurska.coco_client.R;
import com.ndurska.coco_client.calendar.CalendarActivity;
import com.ndurska.coco_client.calendar.CalendarUtils;
import com.ndurska.coco_client.database.dto.DogDto;

import java.util.List;

public class ShowWaitingListAdapter extends RecyclerView.Adapter<WaitingListRecordVH> {

    List<WaitingListRecordDto> waitingList;
    CalendarActivity context;
    WaitingListRequestDispatcher waitingListRequestDispatcher;

    public ShowWaitingListAdapter(Context context, List<WaitingListRecordDto> waitingList) {
        this.waitingList = waitingList;
        this.context = (CalendarActivity) context;
        waitingListRequestDispatcher = new WaitingListRequestDispatcher();
    }

    @NonNull
    @Override
    public WaitingListRecordVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.waiting_list_item, parent, false);

        return new WaitingListRecordVH(view).linkAdapter(this);
    }

    @Override
    public int getItemCount() {
        return waitingList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull WaitingListRecordVH holder, int position) {
        try {
            WaitingListRecordDto waitingListRecordDto = waitingList.get(position);
            //todo fix this with entities
            //todo thread
            DogDto client = waitingListRecordDto.getDogDto();
            holder.tvClientName.setText(client.clientFullName());
            holder.tvDateStart.setText(CalendarUtils.dayMonthFromDate(waitingListRecordDto.getDateStart()));
            holder.tvDateEnd.setText(CalendarUtils.dayMonthFromDate(waitingListRecordDto.getDateEnd()));
            holder.tvNotes.setText(waitingListRecordDto.getNotes());
            holder.btnDelete.setOnClickListener(view ->
                    executorService.execute(() -> {
                        waitingListRequestDispatcher.deleteWaitingListRecord(waitingListRecordDto.getID());
                        context.runOnUiThread(() -> {
                            removeAt(position);
                            context.refreshWeekView();
                        });
                    }));
        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void removeAt(int position) {
        waitingList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, waitingList.size());
    }
}

class WaitingListRecordVH extends RecyclerView.ViewHolder {
    TextView tvClientName;
    TextView tvDateStart;
    TextView tvDateEnd;
    TextView tvNotes;
    Button btnDelete;

    public WaitingListRecordVH(@NonNull View itemView) {
        super(itemView);
        tvClientName = itemView.findViewById(R.id.tvWaitingListClientName);
        tvDateStart = itemView.findViewById(R.id.tvWaitingDateStart);
        tvDateEnd = itemView.findViewById(R.id.tvWaitingDateEnd);
        tvNotes = itemView.findViewById(R.id.tvWaitingRecordNotes);
        btnDelete = itemView.findViewById(R.id.btnDeleteWaitingListRecord);
    }

    public WaitingListRecordVH linkAdapter(ShowWaitingListAdapter adapter) {
        return this;
    }
}
