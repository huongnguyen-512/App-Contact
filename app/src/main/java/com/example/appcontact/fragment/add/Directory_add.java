package com.example.appcontact.fragment.add;

import static android.app.Activity.RESULT_OK;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.appcontact.R;
import com.example.appcontact.fragment.Directory_show;
import com.example.appcontact.models.DatabaseHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Directory_add#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Directory_add extends Fragment {
    private EditText edtmaDV, edtTendv, edtMailDv, edtWeb, edtdcDv, edtsdtDV, edtMadvCha;
    private ImageView logoDV;
    private Button buttonSaveDv, btnout;
    private DatabaseHelper databaseHelper;
    private Bitmap anhDaiDienBitmap;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Directory_add() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Directory_add.
     */
    // TODO: Rename and change types and number of parameters
    public static Directory_add newInstance(String param1, String param2) {
        Directory_add fragment = new Directory_add();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_directory_add, container, false);
//        edtmaDV = view.findViewById(R.id.editTextMaDonVi);
        edtTendv = view.findViewById(R.id.editTextTenDonVi);
        edtMailDv = view.findViewById(R.id.editTextEmailDonVi);
        edtWeb = view.findViewById(R.id.editTextWebsiteDonVi);
        edtdcDv = view.findViewById(R.id.editTextDiaChiDonVi);
        edtsdtDV = view.findViewById(R.id.editTextSdtDonVi);
        edtMadvCha = view.findViewById(R.id.edtMadvcha);
        logoDV =view.findViewById(R.id.imageViewLogoDonVi);
        buttonSaveDv = view.findViewById(R.id.buttonSaveDonvi);
        btnout = view.findViewById(R.id.btnbackDvi);
        edtMadvCha= view.findViewById(R.id.edtMadvcha);
        btnout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new Directory_show();
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        databaseHelper = new DatabaseHelper(getContext());

        logoDV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open image picker
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });

        buttonSaveDv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDonVi();
            }
        });

        return view;
    }
    private void saveDonVi() {
        String tenDV = edtTendv.getText().toString();
        String email = edtMailDv.getText().toString();
        String website = edtWeb.getText().toString();
        String sdt = edtsdtDV.getText().toString();
        String diaChi =edtdcDv.getText().toString();
        String idcha = edtMadvCha.getText().toString();
        // Ensure all fields are filled and image is selected
        if ( tenDV.isEmpty() || email.isEmpty() || website.isEmpty() ||sdt.isEmpty() ||diaChi.isEmpty() || anhDaiDienBitmap == null) {
            Toast.makeText(getContext(), "Please fill all fields and select an image.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert Bitmap to byte array
//        String stringfilePath = Environment.getExternalStorageDirectory().getPath();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        anhDaiDienBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TEN_DONVI, tenDV);
        values.put(DatabaseHelper.COLUMN_EMAIL_DONVI, email);
        values.put(DatabaseHelper.COLUMN_WEBSITE_DONVI, website);
        values.put(DatabaseHelper.COLUMN_DIACHI_DONVI, diaChi);
        values.put(DatabaseHelper.COLUMN_SDT_DONVI, sdt);
        values.put(DatabaseHelper.COLUMN_MA_DONVI_CHA,idcha);
        values.put(DatabaseHelper.COLUMN_LOGO_DONVI, byteArray);

        long newRowId = db.insert(DatabaseHelper.TABLE_DONVI , null, values);
        db.close();

        // Show toast message based on insertion result
        if (newRowId != -1) {
            Toast.makeText(getContext(), "Đơn vị thêm thành công.", Toast.LENGTH_SHORT).show();
//            clearFields();
        } else {
            Toast.makeText(getContext(), "Thêm đơn vị thất bại.", Toast.LENGTH_SHORT).show();
        }
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

                logoDV.setImageBitmap(circleBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//    private void clearFields() {
//        // Clear all input fields and image view
//        e.setText("");
//        editTextChucVuNV.setText("");
//        editTextEmailNV.setText("");
//        editTextSdtNV.setText("");
//        imageViewAnhDaiDienNV.setImageDrawable(null);
//    }
    }
}