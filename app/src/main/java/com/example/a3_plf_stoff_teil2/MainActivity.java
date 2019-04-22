package com.example.a3_plf_stoff_teil2;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.FirebaseApp;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {

    //TODO Some Bugs with Date and Firebase Timestamp - fixxing


    public static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.YYYY");

    private FloatingActionButton fab;
    private EditText name, age, school, datePicker;
    private Button OK, numberPicker;
    private CheckBox checkBox;


    private CustomAdapter adapter;
    private List<Student> students;
    private ListView listView;

    private FirebaseFirestore db;
    private String collectionName = "Students";
    private int RC_SIGN_IN = 1;
    private FirebaseUser DB_User;
    private Student currentStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
    }

    private void initUI() {
        initListView();
        initFAB();
        initFirebase();
    }

    private void initListView() {
        students = new ArrayList<>();
        adapter = new CustomAdapter(this, R.layout.custom_adapter, students);
        listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            currentStudent = students.get(position);
            createDialog(true);
        });
    }

    private void initFAB() {
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(listener -> {
            createDialog(false);
        });
    }


    private void createDialog(boolean update) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.student_dialog, null);
        dialogBuilder.setView(dialogView);

        name = dialogView.findViewById(R.id.editTextName);
        age = dialogView.findViewById(R.id.editTextAge);
        school = dialogView.findViewById(R.id.editTextSchool);

        numberPicker = dialogView.findViewById(R.id.numberPicker);
        checkBox = dialogView.findViewById(R.id.checkBoxGraduated);
        datePicker = dialogView.findViewById(R.id.datePicker);
        OK = dialogView.findViewById(R.id.buttonOK);

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();


        final Calendar calendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            datePicker.setText(simpleDateFormat.format(calendar.getTime()));
        };
        datePicker.setOnClickListener(v -> new DatePickerDialog(MainActivity.this, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show());

        numberPicker.setOnClickListener(l ->{
            NumberPicker myNumberPicker = new NumberPicker(this);
            myNumberPicker.setMaxValue(10);
            myNumberPicker.setMinValue(0);
            myNumberPicker.setOnValueChangedListener((picker, oldVal, newVal) -> numberPicker.setText(String.valueOf(newVal)));
            AlertDialog.Builder builder = new AlertDialog.Builder(this).setView(myNumberPicker);
            builder.setTitle("Select Number Of Subjects").setPositiveButton(android.R.string.ok, (dialog, which) -> {});
            builder.show();
        });


        AtomicBoolean grad = new AtomicBoolean(false);
        checkBox.setOnClickListener(v -> {
            if (((CheckBox) v).isChecked()) {
                grad.set(true);
            }
        });


        if (update) {
            name.setText(currentStudent.getName());
            age.setText(String.valueOf(currentStudent.getAge()));
            school.setText(currentStudent.getSchoolName());
            numberPicker.setText(String.valueOf(currentStudent.getNumberOfSubjects()));
            checkBox.setChecked(currentStudent.isGraduated());
            datePicker.setText(simpleDateFormat.format(currentStudent.getJoinedSchool()));

            OK.setOnClickListener(l -> {
                Map<String, Object> data = new HashMap<>();

                String id = currentStudent.getId();
                String name = this.name.getText().toString();
                String age = this.age.getText().toString();
                String schoolName = school.getText().toString();
                String number = numberPicker.getText().toString();
                String datePicker = this.datePicker.getText().toString();
                boolean graduated = grad.get();
                Date dx = null;
                try {
                    dx = simpleDateFormat.parse(datePicker);
                } catch (ParseException e) {
                }

                if (!name.equals(currentStudent.getName())) {
                    data.put("name", name);
                    currentStudent.setName(name);
                }
                if (!age.equals(currentStudent.getAge())) {
                    data.put("age", Integer.valueOf(age));
                    currentStudent.setAge(Integer.valueOf(age));
                }
                if (!schoolName.equals(currentStudent.getSchoolName())) {
                    data.put("schoolName", schoolName);
                    currentStudent.setSchoolName(schoolName);
                }
                if (!number.equals(currentStudent.getNumberOfSubjects())) {
                    data.put("numberOfSubjects", Integer.valueOf(number));
                    currentStudent.setNumberOfSubjects(Integer.valueOf(number));
                }
                if (!dx.equals(currentStudent.getJoinedSchool())) {
                    data.put("joinedSchool", dx);
                    currentStudent.setJoinedSchool(dx);
                }
                if (graduated != currentStudent.isGraduated()) {
                    data.put("graduated", graduated);
                    currentStudent.setGraduated(graduated);
                }

                adapter.notifyDataSetChanged();
                updateInFirebase(currentStudent, data);

                alertDialog.dismiss();
            });
        } else {
            OK.setOnClickListener(l -> {
                String name = this.name.getText().toString();
                String age = this.age.getText().toString();
                String schoolName = school.getText().toString();
                String number = numberPicker.getText().toString();
                String datePicker = this.datePicker.getText().toString();
                boolean graduated = grad.get();
                Date dx = null;
                try {
                    dx = simpleDateFormat.parse(datePicker);
                } catch (ParseException e) {
                }

                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(this, "Name must not be empty", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                } else if (!TextUtils.isDigitsOnly(age) || TextUtils.isEmpty(age)) {
                    Toast.makeText(this, "Error with age", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                } else if (TextUtils.isEmpty(schoolName)) {
                    Toast.makeText(this, "School Name must not be empty", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                } else if (number.toLowerCase().equals("subjects")) {
                    Toast.makeText(this, "Select a number of Subjects the Student is attending", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                } else if (TextUtils.isEmpty(datePicker)) {
                    Toast.makeText(this, "Select a date the Student joined said School", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }

                Student s = new Student.Builder()
                        .generateId()
                        .setName(name)
                        .setAge(Integer.valueOf(age))
                        .setSchoolName(schoolName)
                        .setGraduated(graduated)
                        .setNumberOfSubjects(Integer.valueOf(number))
                        .setJoinedSchool(dx)
                        .create();
                students.add(s);
                adapter.notifyDataSetChanged();
                addToFirebase(s);
                alertDialog.dismiss();
            });
        }

    }

    private void initFirebase() {
        db = FirebaseFirestore.getInstance();
        signIn();
    }

    private void signIn() {
        List providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build());
// new AuthUI.IdpConfig.GoogleBuilder().build(),
// new AuthUI.IdpConfig.FacebookBuilder().build(),
// new AuthUI.IdpConfig.TwitterBuilder().build());


        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                DB_User = FirebaseAuth.getInstance().getCurrentUser();
                Log.d("LOGIN", "Successfully signed in");
            } else {
                Log.d("LOGIN", "failed to sign in");
            }
        }

        readFirebase();
    }

    public void readFirebase() {
        db.collection(collectionName)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Student s = new Student.Builder()
                                    .setId(document.getId())
                                    .setName((String) document.get("name"))
                                    .setAge(Integer.valueOf(document.get("age").toString()))
                                    .setJoinedSchool(((Timestamp) document.get("joinedSchool")).toDate())
                                    .setGraduated((boolean) document.get("graduated"))
                                    .setNumberOfSubjects(Integer.valueOf(document.get("numberOfSubjects").toString()))
                                    .setSchoolName((String) document.get("schoolName"))
                                    .create();
                            students.add(s);
                            adapter.notifyDataSetChanged();
                            Log.d("firestoreDemo.get", s.toString());
                        }
                    } else {
                        Log.w("firestoreDemo.get", "Error getting documents.", task.getException());
                    }
                });
        adapter.notifyDataSetChanged();
    }

    public void addToFirebase(Student student) {
        db.collection(collectionName)
                .document(student.getId())
                .set(student)
                .addOnSuccessListener(aVoid -> Log.d("3.2 ADD", "DocumentSnapshot successfully written!"))
                .addOnFailureListener(e -> Log.w("3.2 ADD", "Error writing document", e));
    }

    public void updateInFirebase(Student student, Map<String, Object> data) {
        db.collection(collectionName)
                .document(student.getId())
                .update(data)
                .addOnSuccessListener(s -> Log.d("firestoreDemo.update", "DocumentSnapshot successfully updated!"))
                .addOnFailureListener(e -> Log.w("firestoreDemo.update", "Error updating document", e));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
