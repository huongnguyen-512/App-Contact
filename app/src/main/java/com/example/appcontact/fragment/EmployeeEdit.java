package com.example.appcontact.fragment;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.appcontact.R;
import com.example.appcontact.fragment.detail.EmployeeDetail;
import com.example.appcontact.models.DatabaseHelper;

import java.io.ByteArrayOutputStream;

public class EmployeeEdit extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

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

    public static EmployeeEdit newInstance(String hoTen, String chucVu, String email, String sdt, Bitmap avatarBitmap, String tendvi) {
        EmployeeEdit fragment = new EmployeeEdit();
        Bundle args = new Bundle();
        args.putString("hoTen", hoTen);
        args.putString("chucvu", chucVu);
        args.putString("email", email);
        args.putString("sdt", sdt);
        args.putByteArray("avatar", bitmapToByteArray(avatarBitmap));
        args.putString("tenDonVi", tendvi);
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

    private static byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    private ImageView imageViewAnhDaiDienNV;
    private EditText edtten, edtcv, spintendv, edtmail, edtsdt;

    private Button btnup, btnout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employee_edit, container, false);

        // Initialize databaseHelper
        databaseHelper = new DatabaseHelper(getActivity());

        // Find and assign controls
        edtten = view.findViewById(R.id.editTextHoTenNVup);
        edtcv = view.findViewById(R.id.editTextChucVuNVup);
        edtmail = view.findViewById(R.id.editTextEmailNVup);
        imageViewAnhDaiDienNV = view.findViewById(R.id.imageViewAnhDaiDienNVup);
        edtsdt = view.findViewById(R.id.editTextSdtNVup);
        spintendv = view.findViewById(R.id.spinTendviup);
        btnup = view.findViewById(R.id.buttonSaveNhanVienup);
        Bundle bundle = getArguments();
        if (bundle != null) {
            String hoTen = bundle.getString("hoTen", "");
            String chucVu = bundle.getString("chucvu", "");
            String email = bundle.getString("email", "");
            String sdt = bundle.getString("sdt", "");
            byte[] avatarByteArray = bundle.getByteArray("avatar");
            String tenDv = bundle.getString("tenDonVi", "");

            edtten.setText(hoTen);
            edtcv.setText(chucVu);
            edtmail.setText(email);
            edtsdt.setText(sdt);
            spintendv.setText(tenDv);

            if (avatarByteArray != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(avatarByteArray, 0, avatarByteArray.length);
                imageViewAnhDaiDienNV.setImageBitmap(bitmap);
            }
        }
        btnup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy dữ liệu từ các trường nhập
                String hoTen = edtten.getText().toString();
                String chucVu = edtcv.getText().toString();
                String email = edtmail.getText().toString();
                String sdt = edtsdt.getText().toString();
                String tenDv = spintendv.getText().toString();
                Bitmap avatarBitmap = ((BitmapDrawable)imageViewAnhDaiDienNV.getDrawable()).getBitmap();

                // Kiểm tra email và số điện thoại không giống nhau
                if (!email.equals(sdt)) {
                    // Thực hiện cập nhật dữ liệu vào cơ sở dữ liệu
                    boolean success = updateEmployeeData(hoTen, chucVu, email, sdt, avatarBitmap, tenDv);

                    if (success) {
                        // Hiển thị thông báo cập nhật thành công (nếu cần)
                        Toast.makeText(getActivity(), "Đã cập nhật thông tin nhân viên", Toast.LENGTH_SHORT).show();

                        // Hiển thị dữ liệu đã cập nhật vào fragment EmployeeDetail
                        Fragment employeeDetailFragment = EmployeeDetail.newInstance(hoTen, chucVu, email, sdt, avatarBitmap, tenDv);
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_layout, employeeDetailFragment);
                        transaction.addToBackStack(null);  // Để có thể quay lại fragment trước đó
                        transaction.commit();
                    } else {
                        // Hiển thị thông báo lỗi cập nhật (nếu cần)
                        Toast.makeText(getActivity(), "Có lỗi xảy ra khi cập nhật thông tin nhân viên", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Hiển thị thông báo lỗi khi email và số điện thoại giống nhau
                    Toast.makeText(getActivity(), "Email và số điện thoại không được giống nhau", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;

    }
 DatabaseHelper databaseHelper;
    private boolean updateEmployeeData(String hoTen, String chucVu, String email, String sdt, Bitmap avatarBitmap, String tenDv) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_HOTEN_NV, hoTen);
        values.put(DatabaseHelper.COLUMN_CHUCVU_NV, chucVu);
        values.put(DatabaseHelper.COLUMN_EMAIL_NV, email);
        values.put(DatabaseHelper.COLUMN_SDT_NV, sdt);
        values.put(DatabaseHelper.COLUMN_TEN_DONVI_NV, tenDv);

        // Convert Bitmap to byte array
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        avatarBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        values.put(DatabaseHelper.COLUMN_ANHDAIDIEN_NV, byteArray);

        // Update row
        int rowsAffected = db.update(DatabaseHelper.TABLE_NHANVIEN, values, DatabaseHelper.COLUMN_HOTEN_NV + " = ?", new String[]{hoTen});
        db.close();

        // Check if the update was successful
        return rowsAffected > 0;
    }


}
