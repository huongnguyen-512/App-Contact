package com.example.appcontact.fragment.detail;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appcontact.R;
import com.example.appcontact.models.DatabaseHelper;
import com.example.appcontact.models.Employee;

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

    private TextView textViewHoTenNV, textViewChucVuNV, textViewEmailNV, textViewSdtNV;
    private ImageView imageViewAnhDaiDienNV;
    private DatabaseHelper databaseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employee_detail, container, false);

        textViewHoTenNV = view.findViewById(R.id.textViewHoTenNV);
        textViewChucVuNV = view.findViewById(R.id.textViewChucVuNV);
        textViewEmailNV = view.findViewById(R.id.textViewEmailNV);
        textViewSdtNV = view.findViewById(R.id.textViewSdtNV);
        imageViewAnhDaiDienNV = view.findViewById(R.id.imgAnhDaiDienNV);

        // Load employee data passed from previous fragment
        Bundle bundle = getArguments();
        if (bundle != null) {
            String hoTen = bundle.getString("hoTen", "");
            String chucVu = bundle.getString("chucvu", "");
            String email = bundle.getString("email", "");
            String sdt = bundle.getString("sdt", "");
            byte[] avatarByteArray = bundle.getByteArray("avatar");

            // Set employee details to views
            textViewHoTenNV.setText(hoTen);
            textViewSdtNV.setText(sdt);
            textViewChucVuNV.setText(chucVu);
            textViewEmailNV.setText(email);
            // Set other details similarly

            // Set avatar image if available
            if (avatarByteArray != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(avatarByteArray, 0, avatarByteArray.length);
                ImageView imageViewAvatar = view.findViewById(R.id.imgAnhDaiDienNV);
                imageViewAvatar.setImageBitmap(bitmap);
            }
        }
        return view;}


}