package com.example.appcontact.fragment;

import static android.app.Activity.RESULT_OK;

import android.content.ContentValues;
import android.content.Intent;
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

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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
import com.example.appcontact.fragment.detail.DirectoryDetail;
import com.example.appcontact.models.DatabaseHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class DirectoryEdit extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DirectoryEdit() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DirectoryEdit.
     */
    // TODO: Rename and change types and number of parameters
    public static DirectoryEdit newInstance(String param1, String param2) {
        DirectoryEdit fragment = new DirectoryEdit();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    private EditText edtTen, edtdiachi, edtweb, edtEmail, edtSdt, edtidcha;
    private Button btnUpdate;
    private DatabaseHelper databaseHelper;
    ImageView img;
    private void updateDirectory() {
        // Get data from fields
        String tenDonvi = edtTen.getText().toString();
        String web = edtweb.getText().toString();
        String email = edtEmail.getText().toString();
        String sdt = edtSdt.getText().toString();
        String diachi = edtdiachi.getText().toString();
        String idcha = edtidcha.getText().toString();
        Bitmap avatarBitmap = ((BitmapDrawable) img.getDrawable()).getBitmap();

        // Update directory data in the database
        boolean success = updateDirectoryData(tenDonvi, email, web, diachi, sdt, idcha, avatarBitmap);

        if (success) {
            // Show success message
            Toast.makeText(getActivity(), "Đã cập nhật thông tin nhân viên", Toast.LENGTH_SHORT).show();

            // Check if activity is not null and fragment manager is available
            if (getActivity() != null && getActivity().getSupportFragmentManager() != null) {
                Fragment directoryDetailFragment = DirectoryDetail.newInstance(tenDonvi, email, web, diachi, sdt, idcha,avatarBitmap);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, directoryDetailFragment);
                transaction.addToBackStack(null);  // To allow back navigation to the previous fragment
                transaction.commitAllowingStateLoss();  // Commit the transaction allowing state loss
            }
        } else {
            // Show error message
            Toast.makeText(getActivity(), "Có lỗi xảy ra khi cập nhật thông tin nhân viên", Toast.LENGTH_SHORT).show();
        }
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
        View view = inflater.inflate(R.layout.fragment_directory_edit, container, false);
        edtTen = view.findViewById(R.id.editTextTenDonViup);
        edtdiachi = view.findViewById(R.id.editTextDiaChiDonViup);
        edtEmail = view.findViewById(R.id.editTextEmailDonViup);
        edtSdt = view.findViewById(R.id.editTextSdtDonViup);
        edtweb = view.findViewById(R.id.editTextWebsiteDonViup);
        btnUpdate = view.findViewById(R.id.buttonSaveDonviup);
        edtidcha =view.findViewById(R.id.edtMadvcha);
        img = view.findViewById(R.id.imageViewLogoDonViup);
        databaseHelper = new DatabaseHelper(getActivity());
        // Lấy dữ liệu từ Bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            String tenDonVi = bundle.getString("tenDonVi", "");
            String diaChi = bundle.getString("diaChi", "");
            String email = bundle.getString("email", "");
            String sdt = bundle.getString("sdt", "");
            String website = bundle.getString("website", "");
            String idCha = bundle.getString("idCha", ""); // Correct key name is "idCha"

            byte[] avatarByteArray = bundle.getByteArray("logo"); // Đổi "avatar" thành "logo"

            // Đặt dữ liệu lên các EditText
            edtTen.setText(tenDonVi);
            edtdiachi.setText(diaChi);
            edtEmail.setText(email);
            edtSdt.setText(sdt);
            edtweb.setText(website);
            edtidcha.setText(idCha);
            // Nếu có dữ liệu avatar, chuyển đổi thành Bitmap và hiển thị lên ImageView
            if (avatarByteArray != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(avatarByteArray, 0, avatarByteArray.length);
                img.setImageBitmap(bitmap);
            }
        }

        // Xử lý sự kiện nút cập nhật
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDirectory();
            }
        });

        return view;
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

                img.setImageBitmap(circleBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static DirectoryEdit newInstance(String tenDonvi, String email, String web, String diaChi, String sdt, String idCha, Bitmap avatarBitmap) {
        DirectoryEdit fragment = new DirectoryEdit();
        Bundle args = new Bundle();
        args.putString("tenDonVi", tenDonvi);
        args.putString("diaChi", diaChi); // Update key name
        args.putString("email", email);
        args.putString("sdt", sdt);
        args.putString("website", web); // Update key name
        args.putByteArray("logo", bitmapToByteArray(avatarBitmap));
        args.putString("idCha", idCha);
        fragment.setArguments(args);
        return fragment;
    }

    private static byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
    private boolean updateDirectoryData(String tenDonVi, String email, String website, String diaChi,String sdt, String idCha,  Bitmap logo) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TEN_DONVI,tenDonVi );
        values.put(DatabaseHelper.COLUMN_EMAIL_DONVI, email);
        values.put(DatabaseHelper.COLUMN_WEBSITE_DONVI, website);
        values.put(DatabaseHelper.COLUMN_SDT_DONVI, sdt);
        values.put(DatabaseHelper.COLUMN_DIACHI_DONVI, diaChi);
        values.put(DatabaseHelper.COLUMN_MA_DONVI_CHA, idCha);
        // Convert Bitmap to byte array
        byte[] byteArray = bitmapToByteArray(logo);
        values.put(DatabaseHelper.COLUMN_LOGO_DONVI, byteArray);

        // Update row
        int rowsAffected = db.update(DatabaseHelper.TABLE_DONVI, values, DatabaseHelper.COLUMN_TEN_DONVI + " = ?", new String[]{tenDonVi});
        db.close();

        // Check if the update was successful
        return rowsAffected > 0;
    }
}