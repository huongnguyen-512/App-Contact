package com.example.appcontact.fragment.detail;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appcontact.R;
import com.example.appcontact.models.DatabaseHelper;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DirectoryDetail#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DirectoryDetail extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DirectoryDetail() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DirectoryDetail.
     */
    // TODO: Rename and change types and number of parameters
    public static DirectoryDetail newInstance(String param1, String param2) {
        DirectoryDetail fragment = new DirectoryDetail();
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
    private TextView txtTenDv, txtEmail, txtweb, txtAddr, txtSdt;
    private ImageView logoDV;
    private DatabaseHelper databaseHelper;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_directory_detail, container, false);
        txtTenDv = view.findViewById(R.id.txtDv);
        txtEmail = view.findViewById(R.id.txtMailDV);
        txtweb = view.findViewById(R.id.txtWeb);
        txtAddr = view.findViewById(R.id.txtaddrDV);
        txtSdt = view.findViewById(R.id.txtsdtDV);
        Bundle bundle = getArguments();
        if (bundle != null) {
            String tenDonVi = bundle.getString("tenDonVi", "");
            String email = bundle.getString("email", "");
            String website = bundle.getString("website", "");
            String sdt = bundle.getString("sdt", "");
            String diaChi = bundle.getString("diaChi", "");
            byte[] avatarByteArray = bundle.getByteArray("logo");

            // Set employee details to views
            txtTenDv.setText(tenDonVi);
            txtEmail.setText(email);
            txtweb.setText(website);
            txtAddr.setText(sdt);
            txtSdt.setText(diaChi);
            // Set other details similarly

            // Set avatar image if available
            if (avatarByteArray != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(avatarByteArray, 0, avatarByteArray.length);
                ImageView imageViewAvatar = view.findViewById(R.id.logoDV);
                imageViewAvatar.setImageBitmap(bitmap);
            }
        }
        return view;
    }
}