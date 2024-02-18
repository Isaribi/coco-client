package com.ndurska.coco_client.calendar.waiting_list;

import static com.ndurska.coco_client.calendar.CalendarActivity.executorService;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ndurska.coco_client.R;
import com.ndurska.coco_client.calendar.CalendarActivity;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class WaitingListFragment extends DialogFragment implements AddWaitingListRecordFragment.NewWaitingListRecordListener {
    private Button btnAddNew;
    private ShowWaitingListAdapter adapter;
    private RecyclerView rvWaitingList;
    private List<WaitingListRecordDto> waitingList;
    private WaitingListRequestDispatcher waitingListRequestDispatcher;
    private CalendarActivity context;


    public static WaitingListFragment newInstance(List<WaitingListRecordDto> waitingList) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("waitingList", (Serializable) waitingList);
        return new WaitingListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_waiting_list, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = (CalendarActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        waitingListRequestDispatcher = new WaitingListRequestDispatcher(getContext());
        if (getArguments() != null) {
            waitingList = (List<WaitingListRecordDto>) getArguments().getSerializable("waitingList");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvWaitingList = view.findViewById(R.id.rvCompleteWaitingList);
        btnAddNew = view.findViewById(R.id.btnNewWaitingListRecord);
        rvWaitingList.setLayoutManager(new LinearLayoutManager(getActivity()));

        setAdapter();

        btnAddNew.setOnClickListener(view1 -> {
            FragmentManager fm = requireActivity().getSupportFragmentManager();
            AddWaitingListRecordFragment addWaitingListRecordFragment = AddWaitingListRecordFragment.newInstance();
            addWaitingListRecordFragment.show(fm, "fragment_add_waiting_list_record");
            fm.executePendingTransactions();
            Objects.requireNonNull(addWaitingListRecordFragment.getDialog())
                    .setOnDismissListener(dialogInterface -> setAdapter());
        });
    }

    private void setAdapter() {
        executorService.execute(() -> {
            waitingList = waitingListRequestDispatcher.getWaitingList();
            adapter = new ShowWaitingListAdapter(getContext(), waitingList);
            getActivity().runOnUiThread(() -> rvWaitingList.setAdapter(adapter));
        });
    }

    @Override
    public void onNewRecordAdded() {
        setAdapter();

    }
}