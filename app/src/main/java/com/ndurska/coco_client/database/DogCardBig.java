package com.ndurska.coco_client.database;

import static com.ndurska.coco_client.calendar.CalendarActivity.executorService;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.ndurska.coco_client.R;
import com.ndurska.coco_client.database.dto.DogDto;
import com.ndurska.coco_client.database.web.DogsRequestDispatcher;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DogCardBig#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DogCardBig extends Fragment {

    private DogDto dog;
    private int ID;
    private String name;
    private String adjective;
    private String breed;
    private String phoneNumber1;
    private String phoneNumber2;
    private String phoneNumberLabel1;
    private String phoneNumberLabel2;
    private int expectedVisitDuration;
    private String notes;
    private String photoPath;
    private DogCardBigListener listener;
    private DogsRequestDispatcher dbHelper;

    private TextView tvName;
    private TextView tvAdjective;
    private TextView tvBreed;
    private TextView tvID;
    private TextView tvPhoneNumber1;
    private TextView tvPhoneNumber2;
    private TextView tvPhoneNumberLabel1;
    private TextView tvPhoneNumberLabel2;
    private TextView tvLastPayment;
    private TextView tvExpectedVisitDuration;
    private TextView tvNotes;
    private ImageView ivPhoto;
    private Button btnEdit, btnShowAppointments;
    private LinearLayout sameOwnerDogs;

    private DatabaseActivity activity;

    public interface DogCardBigListener {
        void onBtnEditClicked(DogDto dog);

        void onSameOwnerDogClicked(DogDto dog);

        void onBtnShowAppointmentsClicked(DogDto dog);
    }

    public DogCardBig() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DogCardBig.
     */

    public static DogCardBig newInstance(DogDto dog) {
        DogCardBig fragment = new DogCardBig();
        Bundle args = new Bundle();

        args.putSerializable("dog", dog);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof DogCardBigListener) {
            listener = (DogCardBigListener) context;
        } else
            throw new RuntimeException(context + " " + R.string.must_implement + "DogCardBigListener");
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            dog = (DogDto) getArguments().getSerializable("dog");
            ID = dog.getId();
            name = dog.getName();
            adjective = dog.getPseudonym();
            breed = dog.getBreed();
            phoneNumber1 = dog.getPhoneNumber1();
            phoneNumber2 = dog.getPhoneNumber2();
            phoneNumberLabel1 = dog.getPhoneNumberLabel1();
            phoneNumberLabel2 = dog.getPhoneNumberLabel2();
            expectedVisitDuration = dog.getExpectedAppointmentDuration();
            notes = dog.getAdditionalInfo();
            photoPath = dog.getPhotoUUID();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_dog_card_big, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = (DatabaseActivity) getActivity();
        dbHelper = new DogsRequestDispatcher();
        initViews(view);
        displayData();
        setListeners();
    }

    private void initViews(@NonNull View view) {
        tvName = view.findViewById(R.id.tvDogName);
        tvAdjective = view.findViewById(R.id.tvDogPseudonym);
        tvBreed = view.findViewById(R.id.tvDogBreed);
        tvPhoneNumber1 = view.findViewById(R.id.tvPhoneNumber1);
        tvPhoneNumber2 = view.findViewById(R.id.tvPhoneNumber2);
        tvPhoneNumberLabel1 = view.findViewById(R.id.tvPhoneNumberLabel1);
        tvPhoneNumberLabel2 = view.findViewById(R.id.tvPhoneNumberLabel2);
        tvLastPayment = view.findViewById(R.id.tvLastPayment);
        tvExpectedVisitDuration = view.findViewById(R.id.tvVisitDuration);
        tvNotes = view.findViewById(R.id.tvNotes);
        tvID = view.findViewById(R.id.tvDogID);
        btnEdit = view.findViewById(R.id.btnDogEdit);
        sameOwnerDogs = view.findViewById(R.id.llSameOwnerDogs);
        ivPhoto = view.findViewById(R.id.ivProfilePicture);
        btnShowAppointments = view.findViewById(R.id.btnShowAppointments);
    }

    private void displayData() {
        try {
            tvID.setText(String.valueOf(ID));
            tvName.setText(name);
            tvAdjective.setText(adjective);
            tvBreed.setText(breed);
            tvPhoneNumber1.setText(phoneNumber1);
            tvPhoneNumber2.setText(phoneNumber2);
            tvPhoneNumberLabel1.setText(phoneNumberLabel1);
            tvPhoneNumberLabel2.setText(phoneNumberLabel2);
            tvNotes.setText(notes);
            tvExpectedVisitDuration.setText(String.valueOf(expectedVisitDuration));
            setPhoto();
            setLastPaidAmount();
            setSameOwnerDogs();

        } catch (NullPointerException e) {
            Toast.makeText(getActivity(), R.string.null_dog_exception, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void setListeners() {
        btnEdit.setOnClickListener(view12 -> listener.onBtnEditClicked(dog));
        btnShowAppointments.setOnClickListener(view1 -> listener.onBtnShowAppointmentsClicked(dog));
    }

    private void setPhoto() {
        if (photoPath == null)
            ivPhoto.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.default_picture));
        else {
            Glide.with(requireActivity()).load(Uri.parse(photoPath)).into(ivPhoto);
        }
    }

    private void setLastPaidAmount() {
        int amountPaid = dbHelper.getLastPaidAmount(ID);
        tvLastPayment.setText(String.valueOf(amountPaid));
    }

    private void setSameOwnerDogs() {
        executorService.execute(() -> {
            List<DogDto> sameOwnerDogList = dbHelper.getSameOwnerDogs(ID);
            if (sameOwnerDogList.size() > 0) {
                TextView tv = new TextView(getActivity());
                tv.setText(R.string.from);
                activity.runOnUiThread(() -> sameOwnerDogs.addView(tv));
            }
            for (DogDto dog : sameOwnerDogList) {
                TextView dogName = new TextView(getActivity());
                dogName.setText(dog.getName());
                dogName.setTextSize(20);
                dogName.setPadding(5, 5, 5, 5);
                dogName.setOnClickListener(view1 -> listener.onSameOwnerDogClicked(dog));
                activity.runOnUiThread(() -> sameOwnerDogs.addView(dogName));
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

}