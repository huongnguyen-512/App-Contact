package com.example.appcontact.fragment;

import static android.app.Activity.RESULT_OK;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
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

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.appcontact.R;
import com.example.appcontact.fragment.detail.EmployeeDetail;
import com.example.appcontact.models.DatabaseHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class EmployeeEdit extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private ImageView imageViewAnhDaiDienNV;
    private EditText edtTen, edtChucVu, edtEmail, edtSdt;
    private Spinner spinTenDv;
    private Button btnUpdate;
    private DatabaseHelper databaseHelper;


    public EmployeeEdit() {
        // Required empty public constructor
    }

    public static EmployeeEdit newInstance(String param1, String param2) {
        EmployeeEdit fragment = new EmployeeEdit();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static EmployeeEdit newInstance(String hoTen, String chucVu, String email, String sdt, Bitmap avatarBitmap, String tenDv) {
        EmployeeEdit fragment = new EmployeeEdit();
        Bundle args = new Bundle();
        args.putString("hoTen", hoTen);
        args.putString("chucvu", chucVu);
        args.putString("email", email);
        args.putString("sdt", sdt);
        args.putByteArray("avatar", bitmapToByteArray(avatarBitmap));
        args.putString("tenDonVi", tenDv);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        databaseHelper = new DatabaseHelper(getActivity());
    }

    private static byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employee_edit, container, false);

        // Find and assign controls
        edtTen = view.findViewById(R.id.editTextHoTenNVup);
        edtChucVu = view.findViewById(R.id.editTextChucVuNVup);
        edtEmail = view.findViewById(R.id.editTextEmailNVup);
        imageViewAnhDaiDienNV = view.findViewById(R.id.imageViewAnhDaiDienNVup);
        edtSdt = view.findViewById(R.id.editTextSdtNVup);
        spinTenDv = view.findViewById(R.id.spinTendviup);
        btnUpdate = view.findViewById(R.id.buttonSaveNhanVienup);
        imageViewAnhDaiDienNV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open image picker
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });
        loadUnitNamesIntoSpinner();

        Bundle bundle = getArguments();
        if (bundle != null) {
            String hoTen = bundle.getString("hoTen", "");
            String chucVu = bundle.getString("chucvu", "");
            String email = bundle.getString("email", "");
            String sdt = bundle.getString("sdt", "");
            byte[] avatarByteArray = bundle.getByteArray("avatar");
            String tenDv = bundle.getString("tenDonVi", "");

            edtTen.setText(hoTen);
            edtChucVu.setText(chucVu);
            edtEmail.setText(email);
            edtSdt.setText(sdt);
            // Set the spinner selection based on the unit name
            spinTenDv.setSelection(((ArrayAdapter<String>) spinTenDv.getAdapter()).getPosition(tenDv));

            if (avatarByteArray != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(avatarByteArray, 0, avatarByteArray.length);
                imageViewAnhDaiDienNV.setImageBitmap(bitmap);
            }
        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateEmployee();
            }
        });

        return view;
    }

    private void updateEmployee() {
        // Get data from fields
        String hoTen = edtTen.getText().toString();
        String chucVu = edtChucVu.getText().toString();
        String email = edtEmail.getText().toString();
        String sdt = edtSdt.getText().toString();
        String tenDv = spinTenDv.getSelectedItem().toString();
        Bitmap avatarBitmap = ((BitmapDrawable) imageViewAnhDaiDienNV.getDrawable()).getBitmap();

        // Check if email and phone number are not the same
        if (!email.equals(sdt)) {
            // Update the data in the database
            boolean success = updateEmployeeData(hoTen, chucVu, email, sdt, avatarBitmap, tenDv);

            if (success) {
                // Show success message
                Toast.makeText(getActivity(), "Đã cập nhật thông tin nhân viên", Toast.LENGTH_SHORT).show();

                // Display the updated data in the EmployeeDetail fragment
                Fragment employeeDetailFragment = EmployeeDetail.newInstance(hoTen, chucVu, email, sdt, avatarBitmap, tenDv);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, employeeDetailFragment);
                transaction.addToBackStack(null);  // To allow back navigation to the previous fragment
                transaction.commit();
            } else {
                // Show error message
                Toast.makeText(getActivity(), "Có lỗi xảy ra khi cập nhật thông tin nhân viên", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean updateEmployeeData(String hoTen, String chucVu, String email, String sdt, Bitmap avatarBitmap, String tenDv) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_HOTEN_NV, hoTen);
        values.put(DatabaseHelper.COLUMN_CHUCVU_NV, chucVu);
        values.put(DatabaseHelper.COLUMN_EMAIL_NV, email);
        values.put(DatabaseHelper.COLUMN_SDT_NV, sdt);
        values.put(DatabaseHelper.COLUMN_TEN_DONVI_NV, tenDv);

        // Convert Bitmap to byte array
        byte[] byteArray = bitmapToByteArray(avatarBitmap);
        values.put(DatabaseHelper.COLUMN_ANHDAIDIEN_NV, byteArray);

        // Update row
        int rowsAffected = db.update(DatabaseHelper.TABLE_NHANVIEN, values, DatabaseHelper.COLUMN_HOTEN_NV + " = ?", new String[]{hoTen});
        db.close();

        // Check if the update was successful
        return rowsAffected > 0;
    }

    private void loadUnitNamesIntoSpinner() {
        List<String> unitNames = databaseHelper.getAllUnitNames();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, unitNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinTenDv.setAdapter(adapter);
    }
    Bitmap anhDaiDienBitmap;
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
}
