package com.ndurska.coco_client.database;

import static com.ndurska.coco_client.calendar.CalendarActivity.executorService;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.ndurska.coco_client.R;
import com.ndurska.coco_client.database.dto.DogDto;
import com.ndurska.coco_client.database.web.DogsRequestDispatcher;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DogCardEdit#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DogCardEdit extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ID = "ID";
    private static final String ARG_NAME = "name";
    private static final String ARG_PSEUDONYM = "pseudonym";
    private static final String ARG_BREED = "breed";
    private static final String ARG_PHONE_NUMBER1 = "phoneNumber1";
    private static final String ARG_PHONE_NUMBER2 = "phoneNumber2";
    private static final String ARG_PHONE_NUMBER_lABEL_1 = "phoneNumberLabel1";
    private static final String ARG_PHONE_NUMBER_LABEL_2 = "phoneNumberLabel2";
    private static final String ARG_LAST_PAYMENT = "lastPayment";
    private static final String ARG_EXPECTED_VISIT_DURATION = "expectedVisitDuration";
    private static final String ARG_NOTES = "notes";
    private static final String ARG_PHOTO_PATH = "photoPath";

    private DogDto dog;
    private int ID;
    private String name;
    private String pseudonym;
    private String breed;
    private String phoneNumber1;
    private String phoneNumber2;
    private String phoneNumberLabel1;
    private String phoneNumberLabel2;
    private int expectedVisitDuration;
    private String notes;
    private byte[] photoUUID;
    DogsRequestDispatcher dogsRequestDispatcher;
    DatabaseActivity activity;

    TextView etDogName;
    TextView etDogPseudonym;
    TextView etDogBreed;
    TextView etPhoneNumber1;
    TextView etPhoneNumber2;
    TextView etPhoneNumberLabel1;
    TextView etPhoneNumberLabel2;
    TextView tvDogID;
    TextView etExpectedVisitDuration;
    TextView etNotes;
    ImageView ivPhoto;
    Button btnSaveChanges;
    Button btnDeleteDog;
    Button btnDeletePicture;

    private DogCardEditListener listener;

    ActivityResultLauncher<Intent> imagePickerActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        if (imageUri != null && !imageUri.toString().contains("2131165280")) {
                            // Convert the image to a byte array
                            byte[] imageByteArray = getImageByteArray(imageUri);

                            // Use the byte array as needed
                            if (imageByteArray != null) {
                                photoUUID = imageByteArray;
                                Glide.with(requireActivity())
                                        .load(imageUri)
                                        .into(ivPhoto);
                                btnDeletePicture.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            }
    );

    private byte[] getImageByteArray(Uri imageUri) {
        try {
            ContentResolver contentResolver = getActivity().getContentResolver();
            InputStream inputStream = contentResolver.openInputStream(imageUri);

            if (inputStream != null) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                }
                return byteArrayOutputStream.toByteArray();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public interface DogCardEditListener {
        void onBtnSaveClicked(boolean isNewDog);

        void onBtnDeleteClicked(boolean isNewDog);
    }

    public DogCardEdit() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DogCardEdit.
     */

    public static DogCardEdit newInstance(DogDto dog) { //display edit card for existing dog
        return createDogCardFromDog(dog);
    }

    @NonNull
    private static DogCardEdit createDogCardFromDog(DogDto dog) {
        DogCardEdit fragment = new DogCardEdit();
        Bundle args = new Bundle();
        args.putSerializable("dog", dog);
        args.putInt(ARG_ID, dog.getId());
        args.putString(ARG_NAME, dog.getName());
        args.putString(ARG_PSEUDONYM, dog.getPseudonym());
        args.putString(ARG_BREED, dog.getBreed());
        args.putString(ARG_PHONE_NUMBER1, dog.getPhoneNumber1());
        args.putString(ARG_PHONE_NUMBER2, dog.getPhoneNumber2());
        args.putString(ARG_PHONE_NUMBER_lABEL_1, dog.getPhoneNumberLabel1());
        args.putString(ARG_PHONE_NUMBER_LABEL_2, dog.getPhoneNumberLabel2());
        args.putInt(ARG_LAST_PAYMENT, dog.getLastPaidAmount());
        args.putInt(ARG_EXPECTED_VISIT_DURATION, dog.getExpectedAppointmentDuration());
        args.putString(ARG_NOTES, dog.getAdditionalInfo());
        args.putByteArray(ARG_PHOTO_PATH, dog.getPhoto());
        fragment.setArguments(args);
        return fragment;
    }

    public static DogCardEdit newInstance() { //display an empty card for new dog creation
        DogDto dog = new DogDto();
        return createDogCardFromDog(dog);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof DogCardEditListener) {
            listener = (DogCardEditListener) context;
        } else
            throw new RuntimeException(context + "" + R.string.must_implement + " DogCardEditListener");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            ID = getArguments().getInt(ARG_ID);
            name = getArguments().getString(ARG_NAME);
            pseudonym = getArguments().getString(ARG_PSEUDONYM);
            breed = getArguments().getString(ARG_BREED);
            phoneNumber1 = getArguments().getString(ARG_PHONE_NUMBER1);
            phoneNumber2 = getArguments().getString(ARG_PHONE_NUMBER2);
            phoneNumberLabel1 = getArguments().getString(ARG_PHONE_NUMBER_lABEL_1);
            phoneNumberLabel2 = getArguments().getString(ARG_PHONE_NUMBER_LABEL_2);
            expectedVisitDuration = getArguments().getInt(ARG_EXPECTED_VISIT_DURATION);
            photoUUID = getArguments().getByteArray(ARG_PHOTO_PATH);
            notes = getArguments().getString(ARG_NOTES);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dog_card_edit, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = (DatabaseActivity) getActivity();
        dogsRequestDispatcher = new DogsRequestDispatcher();
        findViews(view);
        displayDogInfo();
        setListeners();

    }

    private void displayDogInfo() {
        try {
            dog = (DogDto) requireArguments().getSerializable("dog");
            tvDogID.setText(String.valueOf(ID));
            etDogName.setText(name);
            etDogPseudonym.setText(pseudonym);
            etDogBreed.setText(breed);
            etPhoneNumber1.setText(phoneNumber1);
            etPhoneNumber2.setText(phoneNumber2);
            etPhoneNumberLabel1.setText(phoneNumberLabel1);
            etPhoneNumberLabel2.setText(phoneNumberLabel2);
            etNotes.setText(notes);
            etExpectedVisitDuration.setText(String.valueOf(expectedVisitDuration));
            if (photoUUID == null) {
                ivPhoto.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.default_picture));
                btnDeletePicture.setVisibility(View.GONE);
            } else {
                Glide.with(requireActivity()).load(photoUUID).into(ivPhoto);
            }
        } catch (IllegalStateException e) {
            Toast.makeText(getActivity(), R.string.no_dog_exception + "", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getActivity(), R.string.error + "DogCardEdit.onViewCreated", Toast.LENGTH_SHORT).show();
        }
    }

    private void findViews(@NonNull View view) {
        etDogName = view.findViewById(R.id.etDogName);
        etDogPseudonym = view.findViewById(R.id.etDogPseudonym);
        etDogBreed = view.findViewById(R.id.etDogBreed);
        etPhoneNumber1 = view.findViewById(R.id.etPhoneNumber1);
        etPhoneNumber2 = view.findViewById(R.id.etPhoneNumber2);
        etPhoneNumberLabel1 = view.findViewById(R.id.etPhoneNumberLabel1);
        etPhoneNumberLabel2 = view.findViewById(R.id.etPhoneNumberLabel2);
        tvDogID = view.findViewById(R.id.tvDogID);
        etExpectedVisitDuration = view.findViewById(R.id.tvVisitDuration);
        etNotes = view.findViewById(R.id.etNotes);
        btnSaveChanges = view.findViewById(R.id.btnSaveChanges);
        btnDeleteDog = view.findViewById(R.id.btnDeleteDog);
        ivPhoto = view.findViewById(R.id.ivProfilePicture);
        btnDeletePicture = view.findViewById(R.id.btnDeletePicture);
    }

    private boolean dogInfoIsValid() {
        if (noDogInformationProvided()) {
            etDogName.setError(getString(R.string.enter_name_error));
            etDogPseudonym.setError(getString(R.string.enter_adjective_error));
            etDogBreed.setError(getString(R.string.enter_breed_error));
            return false;
        }
        String fullName = etDogName.getText().toString().trim() + etDogPseudonym.getText().toString().trim() + etDogBreed.getText().toString().trim();
        fullName = fullName.toLowerCase();
        if (dogsRequestDispatcher.nameTakenByDifferentDog(fullName)) {
            AtomicBoolean choice = getSameDogNameConfirmation();
            return choice.get();
        }
        return true;
    }

    @NonNull
    private AtomicBoolean getSameDogNameConfirmation() {
        AtomicBoolean choice = new AtomicBoolean(false);
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.dog_with_this_name_already_exists)
                .setMessage(R.string.do_you_still_want_to_save)
                .setPositiveButton(R.string.yes_save, (dialog, which)
                        -> choice.set(true))
                .setNegativeButton(R.string.no_return, (dialog, which)
                        -> choice.set(false))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
        return choice;
    }

    private void setListeners() {
        btnSaveChanges.setOnClickListener(view13 -> saveDog());

        btnDeleteDog.setOnClickListener(view1 -> deleteDog());

        ivPhoto.setOnClickListener(view1 -> choosePhoto());

        btnDeletePicture.setOnClickListener(view12 -> new AlertDialog.Builder(getContext())
                .setTitle(R.string.delete_picture)
                .setMessage(R.string.delete_picture_question)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    ivPhoto.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.default_picture));
                    photoUUID = null;
                    btnDeletePicture.setVisibility(View.GONE);
                })
                .setNegativeButton(R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void saveDog() {
        executorService.execute(() -> {
            if (dogInfoIsValid()) {
                if (isDogInDatabase()) {
                    saveDogEdits();
                    listener.onBtnSaveClicked(false);
                } else {
                    getDogInfoFromViews();
                    saveDogToDatabase();
                    listener.onBtnSaveClicked(true);
                }
            }
        });
    }

    private void deleteDog() {
        executorService.execute(() -> {
            if (isDogInDatabase()) {
                try {
                    dogsRequestDispatcher.deleteDog(dog.getId());
                } catch (Exception e) {
                    Log.println(Log.ERROR, "del", e.toString());
                    Toast.makeText(activity, R.string.delete_dog_exception + "", Toast.LENGTH_SHORT).show();
                }
                listener.onBtnDeleteClicked(false);
            } else {
                listener.onBtnDeleteClicked(true);
            }
        });
    }

    private void choosePhoto() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
        imagePickerActivityResult.launch(galleryIntent);
    }

    private boolean isDogInDatabase() {
        return dog.getId() != 0;
    }

    private boolean noDogInformationProvided() {
        return etDogName.getText().toString().trim().equals("") &&
                etDogPseudonym.getText().toString().trim().equals("") &&
                etDogBreed.getText().toString().trim().equals("");
    }

    private void saveDogEdits() {
        try {
            getDogInfoFromViews();
            dog.setPhoto(photoUUID);
            DogDto editedDog = dogsRequestDispatcher.editDog(dog);
            activity.setActiveDog(editedDog);
            activity.runOnUiThread(() -> Toast.makeText(getActivity(), R.string.dog_changes_saved, Toast.LENGTH_SHORT).show());
            activity.runOnUiThread(() -> Toast.makeText(getActivity(), R.string.dog_changes_not_saved, Toast.LENGTH_SHORT).show());
        } catch (Exception e) {
            activity.runOnUiThread(() -> Toast.makeText(getActivity(), R.string.dog_changes_save_exception + "", Toast.LENGTH_SHORT).show());
        }
    }

    private void getDogInfoFromViews() {
        dog.setName(etDogName.getText().toString());
        dog.setPseudonym(etDogPseudonym.getText().toString());
        dog.setBreed(etDogBreed.getText().toString());
        dog.setPhoneNumber1(etPhoneNumber1.getText().toString());
        dog.setPhoneNumber2(etPhoneNumber2.getText().toString());
        dog.setPhoneNumberLabel1(etPhoneNumberLabel1.getText().toString());
        dog.setPhoneNumberLabel2(etPhoneNumberLabel2.getText().toString());
        dog.setPhoto(photoUUID);
        getActivity().runOnUiThread(() -> ivPhoto.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.default_picture)));
        if (etExpectedVisitDuration.getText().length() == 0)
            dog.setExpectedAppointmentDuration(90);
        else
            dog.setExpectedAppointmentDuration(Integer.parseInt(etExpectedVisitDuration.getText().toString()));
        dog.setAdditionalInfo(etNotes.getText().toString());
    }

    private void saveDogToDatabase() {
        DogDto savedDog = dogsRequestDispatcher.addDog(dog);
        int dogId = savedDog.getId();
        dog.setId(dogId);
        activity.setActiveDog(dog);
        String toastText;
        if (dogId != -1)
            toastText = R.string.dog_saved + "";
        else
            toastText = R.string.dog_not_saved + "";
        activity.runOnUiThread(() -> Toast.makeText(getActivity(), toastText, Toast.LENGTH_SHORT).show());
    }
}

