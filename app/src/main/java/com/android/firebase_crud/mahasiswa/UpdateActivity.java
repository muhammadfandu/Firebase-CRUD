package com.android.firebase_crud.mahasiswa;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.firebase_crud.R;
import com.android.firebase_crud.model.Mahasiswa;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtName, edtMail;
    private Button btnUpdate;

    public static final String EXTRA_MAHASISWA = "extra_mahasiswa";
    public final int ALERT_DIALOG_CLOSE = 10;
    public final int ALERT_DIALOG_DELETE = 20;

    private Mahasiswa mahasiswa;
    private String mahasiswaId;

    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        edtName = findViewById(R.id.edtEditName);
        edtMail = findViewById(R.id.edtEditMail);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(this);

        mahasiswa = getIntent().getParcelableExtra(EXTRA_MAHASISWA);

        if (mahasiswa != null) {
            mahasiswaId = mahasiswa.getId();
        } else {
            mahasiswa = new Mahasiswa();
        }

        if (mahasiswa != null) {
            edtName.setText(mahasiswa.getName());
            edtMail.setText(mahasiswa.getMail());
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Edit Data");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnUpdate) {
            updateMahasiswa();
        }
    }

    private void updateMahasiswa() {
        String name = edtName.getText().toString().trim();
        String mail = edtMail.getText().toString().trim();

        boolean isEmptyFields = false;

        if (TextUtils.isEmpty(name)) {
            isEmptyFields = true;
            edtName.setError("Field ini tidak boleh kosong");
        }

        if (TextUtils.isEmpty(mail)) {
            isEmptyFields = true;
            edtMail.setError("Field ini tidak boleh kosong");
        }

        if (! isEmptyFields) {

            Toast.makeText(UpdateActivity.this, "Updating Data...", Toast.LENGTH_SHORT).show();

            mahasiswa.setName(name);
            mahasiswa.setMail(mail);

            DatabaseReference dbMahasiswa = mDatabase.child("mahasiswa");

            //update data
            dbMahasiswa.child(mahasiswaId).setValue(mahasiswa);

            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_form, menu);

        return super.onCreateOptionsMenu(menu);
    }

    //pilih menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                showAlertDialog(ALERT_DIALOG_DELETE);
                break;
            case android.R.id.home:
                showAlertDialog(ALERT_DIALOG_CLOSE);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        showAlertDialog(ALERT_DIALOG_CLOSE);
    }

    private void showAlertDialog(int type) {
        final boolean isDialogClose = type == ALERT_DIALOG_CLOSE;
        String dialogTitle, dialogMessage;

        if (isDialogClose) {
            dialogTitle = "Batal";
            dialogMessage = "Apakah anda ingin membatalkan perubahan pada form?";
        } else {
            dialogTitle = "Hapus Data";
            dialogMessage = "Apakah anda yakin ingin menghapus item ini?";
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle(dialogTitle);
        alertDialogBuilder.setMessage(dialogMessage)
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (isDialogClose) {
                            finish();
                        } else {
                            //kode hapus data masuk disini
                            if (isDialogClose) {
                                finish();
                            } else {
                                //hapus data
                                DatabaseReference dbMahasiswa =
                                        mDatabase.child("mahasiswa").child(mahasiswaId);

                                dbMahasiswa.removeValue();

                                Toast.makeText(UpdateActivity.this, "Deleting data...",
                                        Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    }
                }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}