package com.ndurska.coco_client.calendar.waiting_list;

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
import com.ndurska.coco_client.shared.RequestDispatcher;

import java.util.List;
import java.util.Objects;

public class WaitingListFragment extends DialogFragment implements AddWaitingListRecordFragment.NewWaitingListRecordListener {
    private Button btnAddNew;
    private ShowWaitingListAdapter adapter;
    private RecyclerView rvWaitingList;
    private List<WaitingListRecord> waitingList;
    private RequestDispatcher requestDispatcher;
    private CalendarActivity context;


    public static WaitingListFragment newInstance() {
        return new WaitingListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_waiting_list, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestDispatcher = new RequestDispatcher(getActivity());
//todo thread
//        waitingList = requestDispatcher.getWaitingList();
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
        //todo thread
//        waitingList = requestDispatcher.getWaitingList();
        adapter = new ShowWaitingListAdapter(getContext(), waitingList);
        rvWaitingList.setAdapter(adapter);

    }

    @Override
    public void onNewRecordAdded() {
        setAdapter();

    }
}