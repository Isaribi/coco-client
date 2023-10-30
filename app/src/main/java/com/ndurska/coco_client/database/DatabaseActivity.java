package com.ndurska.coco_client.database;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ndurska.coco_client.R;
import com.ndurska.coco_client.shared.ChooseDogAdapter;
import com.ndurska.coco_client.shared.DogFilter;
import com.ndurska.coco_client.shared.RequestDispatcher;
import com.ndurska.coco_client.shared.TextWatcherAdapter;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DatabaseActivity extends AppCompatActivity implements DogCardEdit.DogCardEditListener, DogCardBig.DogCardBigListener, ChooseDogAdapter.ChooseDogAdapterListener {

    private EditText etDogSearch;
    private RecyclerView rvDogList;
    private LinearLayout bottomBar;
    ChooseDogAdapter adapter;
    List<DogDto> dogs;
    private DogDto activeDog;
    private int activeDogPosition;
    RequestDispatcher requestDispatcher;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private Button btnDogAdd;
    private Toolbar toolbar;

    public static ExecutorService executorService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        executorService = Executors.newFixedThreadPool(4);
        setContentView(R.layout.database_activity);
        requestDispatcher = new RequestDispatcher(getApplicationContext());
        executorService.execute(
                () -> {
                    dogs = requestDispatcher.getDogs();
                    runOnUiThread(this::createDogListAdapter);
                }
        );
        displayDogIfRequested();
        checkForSMSPermission();
        findViews();
        setSupportActionBar(toolbar);
        setListeners();
    }

    private void displayDogIfRequested() {
        Bundle b = getIntent().getExtras();
        DogDto requestedDog = new DogDto();
        if (b != null)
            requestedDog = (DogDto) b.getSerializable("dog");

        if (requestedDog.getId() != null && requestedDog.getId() != 0) {
            setActiveDog(requestedDog);
            DogCardBig dogCardBig = DogCardBig.newInstance(activeDog);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.placeholder, dogCardBig).commit();
        }
    }

    private void createDogListAdapter() {
        rvDogList = findViewById(R.id.rvDogList);
        rvDogList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChooseDogAdapter(dogs, this);
        rvDogList.setAdapter(adapter);
    }

    private void setListeners() {
        etDogSearch.addTextChangedListener(new TextWatcherAdapter() {

            @Override
            public void afterTextChanged(Editable editable) {
                DogFilter filter = new DogFilter(dogs);

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Fragment fr = DatabaseActivity.this.getSupportFragmentManager().findFragmentById(R.id.placeholder);
                if (fr != null)
                    ft.remove(fr).commit();

                List<DogDto> searchResults = filter.dogsContaining(String.valueOf(editable));
                adapter = new ChooseDogAdapter(searchResults, DatabaseActivity.this);
                rvDogList.setAdapter(adapter);

            }
        });

        btnDogAdd.setOnClickListener(view -> {
            etDogSearch.setText(null);
            activeDogPosition = adapter.clickedPosition;
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            DogCardEdit dogCardEdit = DogCardEdit.newInstance();
            ft.replace(R.id.placeholder, dogCardEdit).commit();
            bottomBar.setVisibility(View.GONE);
        });

        if (getCallingActivity() != null) {
            btnDogAdd.callOnClick();
        }
    }

    private void findViews() {
        btnDogAdd = findViewById(R.id.btnDogAdd);
        etDogSearch = findViewById(R.id.etDogSearch);
        bottomBar = findViewById(R.id.linearLayout);
        toolbar = findViewById(R.id.toolbar);
    }

    private void checkForSMSPermission() {
        if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_DENIED) {
            Log.d("permission", "permission denied to SEND_SMS - requesting it");
            String[] permissions = {Manifest.permission.SEND_SMS};
            requestPermissions(permissions, PERMISSION_REQUEST_CODE);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        menu.findItem(R.id.summary).setVisible(false);
        menu.findItem(R.id.dog_database).setVisible(false);
        menu.findItem(R.id.send_text_reminders).setVisible(false);
        menu.findItem(R.id.waiting_list).setVisible(false);
        menu.findItem(R.id.month_summary).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.calendar) {
            finish();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }


    public void setActiveDogPosition(int activeDogPosition) {
        this.activeDogPosition = activeDogPosition;
    }

    public void setActiveDog(DogDto activeDog) {
        this.activeDog = activeDog;
    }


    @Override
    public void onBtnSaveClicked(boolean isNewDog) {

//        executorService.execute(
//                () -> {
        dogs = requestDispatcher.getDogs();
        runOnUiThread(this::createDogListAdapter);

        //            }
        //     );
        DogCardBig dogCardBig = DogCardBig.newInstance(activeDog);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.placeholder, dogCardBig).commit();
        //adapter = new ChooseDogAdapter(dogs, DatabaseActivity.this);
        //rvDogList.setAdapter(adapter);
        runOnUiThread(() -> bottomBar.setVisibility(View.VISIBLE));

        if (getCallingActivity() != null) {
            Intent intent = getIntent();
            intent.putExtra("dog", activeDog);
            setResult(RESULT_OK, intent);
            finish();
        }

    }

    @Override
    public void onBtnDeleteClicked(boolean isNewDog) {
        try {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.remove(Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.placeholder))).commit();

            if (!isNewDog) {
                runOnUiThread(() -> {
                    adapter.items.remove(activeDogPosition);
                    adapter.notifyItemRemoved(activeDogPosition);
                });
            }
        } catch (Exception e) {
            runOnUiThread(() -> Toast.makeText(DatabaseActivity.this, R.string.delete_dog_exception + "" + e, Toast.LENGTH_SHORT).show());
        }
        runOnUiThread(() -> bottomBar.setVisibility(View.VISIBLE));
    }

    @Override
    public void onBtnEditClicked(DogDto dog) {
        DogCardEdit dogCardEdit = DogCardEdit.newInstance(dog);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.placeholder, dogCardEdit).commit();
        bottomBar.setVisibility(View.GONE);
    }

    @Override
    public void onSameOwnerDogClicked(DogDto dog) {
        DogCardBig dogCardBig = DogCardBig.newInstance(dog);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.placeholder, dogCardBig).commit();
        for (DogDto c : dogs)
            if (Objects.equals(c.getId(), dog.getId())) {
                setActiveDogPosition(dogs.indexOf(c));
                adapter.setClickedDog(c);
            }
    }

    @Override
    public void onBtnShowAppointmentsClicked(DogDto dog) {
        FragmentManager fm = getSupportFragmentManager();
//        ShowAppointments showAppointmentsFragment = ShowAppointments.newInstance(dog);
//        showAppointmentsFragment.show(fm, "show_appointments_fragment");
    }

    @Override
    public void onRecyclerItemClicked(DogDto dog) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.placeholder, DogCardBig.newInstance(dog)).commit();
        bottomBar.setVisibility(View.VISIBLE);
        this.activeDogPosition = adapter.clickedPosition;
    }


}