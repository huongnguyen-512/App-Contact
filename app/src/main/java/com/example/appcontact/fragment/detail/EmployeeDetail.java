package com.example.appcontact.fragment.detail;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appcontact.R;
import com.example.appcontact.fragment.EmployeeEdit;
import com.example.appcontact.fragment.Employee_show;
import com.example.appcontact.models.DatabaseHelper;
import com.example.appcontact.models.Employee;

import java.io.ByteArrayOutputStream;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EmployeeDetail#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmployeeDetail extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EmployeeDetail() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EmployeeDetail.
     */
    // TODO: Rename and change types and number of parameters
    public static EmployeeDetail newInstance(String param1, String param2) {
        EmployeeDetail fragment = new EmployeeDetail();
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

    private TextView textViewHoTenNV, textViewChucVuNV, textViewEmailNV, textViewSdtNV, txtTendv;
    private ImageView imageViewAnhDaiDienNV;
    private DatabaseHelper databaseHelper;
    private Button btnxoa, btnsua;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employee_detail, container, false);
        btnsua=view.findViewById(R.id.btnsuanv);
        btnsua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve data from arguments
                Bundle bundle = getArguments();
                if (bundle != null) {
                    String hoTen = bundle.getString("hoTen", "");
                    String chucVu = bundle.getString("chucvu", "");
                    String email = bundle.getString("email", "");
                    String sdt = bundle.getString("sdt", "");
                    String tendvi = bundle.getString("tenDonvi","");
                    byte[] avatarByteArray = bundle.getByteArray("avatar");

                    // Convert byte array to Bitmap
                    Bitmap avatarBitmap = BitmapFactory.decodeByteArray(avatarByteArray, 0, avatarByteArray.length);

                    // Create a new instance of EmployeeEdit fragment
                    EmployeeEdit fragment = EmployeeEdit.newInstance(hoTen, chucVu, email, sdt, avatarBitmap, tendvi);

                    // Start fragment transaction
                    FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame_layout, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });

        btnxoa=view.findViewById(R.id.btnxoanv);
        btnxoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call a method to delete the employee
                deleteEmployee();
            }
        });
        textViewHoTenNV = view.findViewById(R.id.textViewHoTenNV);
        textViewChucVuNV = view.findViewById(R.id.textViewChucVuNV);
        textViewEmailNV = view.findViewById(R.id.textViewEmailNV);
        textViewSdtNV = view.findViewById(R.id.textViewSdtNV);
        imageViewAnhDaiDienNV = view.findViewById(R.id.imgAnhDaiDienNV);
        txtTendv = view.findViewById(R.id.txtTendv);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String hoTen = bundle.getString("hoTen", "");
            String chucVu = bundle.getString("chucvu", "");
            String email = bundle.getString("email", "");
            String sdt = bundle.getString("sdt", "");
            byte[] avatarByteArray = bundle.getByteArray("avatar");
            String tenDv = bundle.getString("tenDonVi", ""); // Retrieve unit name

            textViewHoTenNV.setText(hoTen);
            textViewSdtNV.setText(sdt);
            textViewChucVuNV.setText(chucVu);
            textViewEmailNV.setText(email);
            txtTendv.setText(tenDv); // Set unit name

            if (avatarByteArray != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(avatarByteArray, 0, avatarByteArray.length);
                imageViewAnhDaiDienNV.setImageBitmap(bitmap);
            }
        }

        return view;
    }
    public static EmployeeEdit newInstance(String hoTen, String chucVu, String email, String sdt, Bitmap avatar, String tendvi) {
        EmployeeEdit fragment = new EmployeeEdit();
        Bundle args = new Bundle();
        args.putString("hoTen", hoTen);
        args.putString("chucvu", chucVu);
        args.putString("email", email);
        args.putString("sdt", sdt);
        args.putByteArray("avatar", convertBitmapToByteArray(avatar));
        args.putString("tenDonvi", tendvi);
        fragment.setArguments(args);
        return fragment;
    }


    static byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }


    private void deleteEmployee() {
        // Get employee information from arguments
        Bundle bundle = getArguments();
        if (bundle != null) {
            String hoTen = bundle.getString("hoTen", "");

            // Delete the employee from the database
            databaseHelper = new DatabaseHelper(getActivity());
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            int deletedRows = db.delete(DatabaseHelper.TABLE_NHANVIEN, DatabaseHelper.COLUMN_HOTEN_NV + "=?", new String[]{hoTen});
            db.close();

            if (deletedRows > 0) {
                Toast.makeText(getActivity(), "Employee deleted successfully", Toast.LENGTH_SHORT).show();
                // Optionally, you can navigate back to the previous screen or perform any other action here
            } else {
                Toast.makeText(getActivity(), "Failed to delete employee", Toast.LENGTH_SHORT).show();
            }
        }
    }

}