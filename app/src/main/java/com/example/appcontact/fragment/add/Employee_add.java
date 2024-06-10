package com.example.appcontact.fragment.add;

import static android.app.Activity.RESULT_OK;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.appcontact.R;
import com.example.appcontact.fragment.Employee_show;
import com.example.appcontact.models.DatabaseHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class Employee_add extends Fragment {
    private EditText editTextHoTenNV, editTextChucVuNV, editTextEmailNV, editTextSdtNV;
    private ImageView imageViewAnhDaiDienNV;
    private Button buttonSaveNhanVien, btnout;
    private DatabaseHelper databaseHelper;
    private Bitmap anhDaiDienBitmap;
    private Spinner spinTendv;

    // Required empty public constructor

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employee_add, container, false);

        editTextHoTenNV = view.findViewById(R.id.editTextHoTenNV);
        editTextChucVuNV = view.findViewById(R.id.editTextChucVuNV);
        editTextEmailNV = view.findViewById(R.id.editTextEmailNV);
        editTextSdtNV = view.findViewById(R.id.editTextSdtNV);
        imageViewAnhDaiDienNV = view.findViewById(R.id.imageViewAnhDaiDienNV);
        buttonSaveNhanVien = view.findViewById(R.id.buttonSaveNhanVien);
        btnout = view.findViewById(R.id.btnbackNV);
        spinTendv = view.findViewById(R.id.spinTendvi);
        btnout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the Employee_show fragment
                Fragment fragment = new Employee_show();
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        databaseHelper = new DatabaseHelper(getContext());
        loadUnitNamesIntoSpinner();
        imageViewAnhDaiDienNV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open image picker
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });

        buttonSaveNhanVien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Save the employee
                saveNhanVien();
            }
        });

        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            // Handle image selection
            Uri selectedImage = data.getData();
            try {
                anhDaiDienBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);

                // Calculate the desired size for the circular image
                int sizeInDp = 120;
                int sizeInPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, sizeInDp, getResources().getDisplayMetrics());

                // Create a square bitmap with the desired size
                Bitmap squareBitmap = Bitmap.createScaledBitmap(anhDaiDienBitmap, sizeInPx, sizeInPx, true);

                // Create circular image
                Bitmap circleBitmap = Bitmap.createBitmap(sizeInPx, sizeInPx, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(circleBitmap);
                Paint paint = new Paint();
                paint.setAntiAlias(true);
                canvas.drawCircle(sizeInPx / 2f, sizeInPx / 2f, sizeInPx / 2f, paint);
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                canvas.drawBitmap(squareBitmap, 0, 0, paint);

                imageViewAnhDaiDienNV.setImageBitmap(circleBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    private void saveNhanVien() {
        String hoTenNV = editTextHoTenNV.getText().toString();
        String chucVuNV = editTextChucVuNV.getText().toString();
        String emailNV = editTextEmailNV.getText().toString();
        String sdtNV = editTextSdtNV.getText().toString();
        String tenDv = spinTendv.getSelectedItem().toString(); // Retrieve selected unit name

        if (hoTenNV.isEmpty() || chucVuNV.isEmpty() || emailNV.isEmpty() || sdtNV.isEmpty() || anhDaiDienBitmap == null) {
            Toast.makeText(getContext(), "Please fill all fields and select an image.", Toast.LENGTH_SHORT).show();
            return;
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        anhDaiDienBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_HOTEN_NV, hoTenNV);
        values.put(DatabaseHelper.COLUMN_CHUCVU_NV, chucVuNV);
        values.put(DatabaseHelper.COLUMN_EMAIL_NV, emailNV);
        values.put(DatabaseHelper.COLUMN_SDT_NV, sdtNV);
        values.put(DatabaseHelper.COLUMN_ANHDAIDIEN_NV, byteArray);
        values.put(DatabaseHelper.COLUMN_TEN_DONVI_NV, tenDv); // Save unit name
        long newRowId = db.insert(DatabaseHelper.TABLE_NHANVIEN, null, values);
        db.close();

        if (newRowId != -1) {
            Toast.makeText(getContext(), "Employee saved successfully.", Toast.LENGTH_SHORT).show();
            clearFields();
        } else {
            Toast.makeText(getContext(), "Failed to save employee.", Toast.LENGTH_SHORT).show();
        }
    }


    private void loadUnitNamesIntoSpinner() {
        List<String> unitNames = databaseHelper.getAllUnitNames();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, unitNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinTendv.setAdapter(adapter);
    }
    private void clearFields() {
        // Clear all input fields and image view
        editTextHoTenNV.setText("");
        editTextChucVuNV.setText("");
        editTextEmailNV.setText("");
        editTextSdtNV.setText("");
        imageViewAnhDaiDienNV.setImageDrawable(null);
    }
}
