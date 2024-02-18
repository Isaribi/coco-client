package com.ndurska.coco_client.calendar.appointment;

import static com.ndurska.coco_client.calendar.CalendarActivity.executorService;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.ndurska.coco_client.R;
import com.ndurska.coco_client.calendar.appointment.dto.ProvidedService;
import com.ndurska.coco_client.calendar.appointment.web.ProvidedServicesRequestDispatcher;

import java.util.List;

public class AddServiceFragment extends DialogFragment {

    private RecyclerView rvServices;
    private EditText etName;
    private EditText etMinPrice;
    private EditText etMaxPrice;
    private EditText etNotes;
    private Button btnAddService;

    private ProvidedServicesRequestDispatcher providedServicesRequestDispatcher;

    private List<ProvidedService> providedServices;


    public AddServiceFragment() {
        // Required empty public constructor
    }

    public static AddServiceFragment newInstance() {
        AddServiceFragment fragment = new AddServiceFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        providedServicesRequestDispatcher = new ProvidedServicesRequestDispatcher(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_service, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initViews(view);
        addListeners();
        executorService.execute(
                () -> {
                    providedServices = (List<ProvidedService>) providedServicesRequestDispatcher.getProvidedServices();
                    ProvidedServicesAdapter adapter = new ProvidedServicesAdapter(getActivity(), providedServices);
                    getActivity().runOnUiThread(() -> {
                        rvServices.setAdapter(adapter);
                    });
                });
    }

    private void initViews(@NonNull View view) {
        rvServices = view.findViewById(R.id.rvServices);
        etName = view.findViewById(R.id.etName);
        etMinPrice = view.findViewById(R.id.etMinPrice);
        etMaxPrice = view.findViewById(R.id.etMaxPrice);
        etNotes = view.findViewById(R.id.etServiceNotes);
        btnAddService = view.findViewById(R.id.btnAddService);
    }

    private void addListeners() {
        btnAddService.setOnClickListener(view -> {
            ProvidedService providedService = new ProvidedService(null,
                    etName.getText().toString(),
                    Integer.parseInt(etMinPrice.getText().toString()),
                    Integer.parseInt(etMaxPrice.getText().toString()),
                    etNotes.getText().toString());
            executorService.execute(() -> providedServicesRequestDispatcher.addProvidedService(providedService));
        });
    }
}