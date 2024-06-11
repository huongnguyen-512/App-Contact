package com.example.appcontact.fragment.detail;

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
import com.example.appcontact.fragment.DirectoryEdit;
import com.example.appcontact.fragment.EmployeeEdit;
import com.example.appcontact.models.DatabaseHelper;

import java.io.ByteArrayOutputStream;

public class DirectoryDetail extends Fragment {

    private TextView txtTenDv, txtEmail, txtweb, txtAddr, txtSdt;
    private ImageView logoDV;
    private DatabaseHelper databaseHelper;
    private Button btnsua, btnxoa;

    public static DirectoryEdit newInstance(String tenDonvi, String email, String web, String diaChi, String sdt, Bitmap avatarBitmap) {
        DirectoryEdit fragment = new DirectoryEdit();
        Bundle args = new Bundle();
        args.putString("tenDonVi", tenDonvi);
        args.putString("diaChi", diaChi); // Update key name
        args.putString("email", email);
        args.putString("sdt", sdt);
        args.putString("website", web); // Update key name
        args.putByteArray("logo", convertBitmapToByteArray(avatarBitmap));
        fragment.setArguments(args);
        return fragment;
    }

    static byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
    ImageView imageViewAvatar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_directory_detail, container, false);
        txtTenDv = view.findViewById(R.id.txtDv);
        txtEmail = view.findViewById(R.id.txtMailDV);
        txtweb = view.findViewById(R.id.txtWeb);
        txtAddr = view.findViewById(R.id.txtaddrDV);
        txtSdt = view.findViewById(R.id.txtsdtDV);
        imageViewAvatar = view.findViewById(R.id.logoDV);
        btnsua = view.findViewById(R.id.btnsuadv);
        btnxoa = view.findViewById(R.id.btnxoadv);

        btnsua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = getArguments();
                if (bundle != null) {
                    String tenDonvi = bundle.getString("tenDonVi", "");
                    String web = bundle.getString("website", "");
                    String email = bundle.getString("email", "");
                    String sdt = bundle.getString("sdt", "");
                    String diaChi = bundle.getString("diaChi", "");
                    byte[] avatarByteArray = bundle.getByteArray("logo");

                    Bitmap avatarBitmap = BitmapFactory.decodeByteArray(avatarByteArray, 0, avatarByteArray.length);

                    DirectoryEdit fragment = DirectoryEdit.newInstance(tenDonvi, email, web, diaChi, sdt, avatarBitmap);

                    FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame_layout, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });

        btnxoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteEmployee();
            }
        });

        Bundle bundle = getArguments();
        if (bundle != null) {
            String tenDonVi = bundle.getString("tenDonVi", "");
            String email = bundle.getString("email", "");
            String website = bundle.getString("website", "");
            String sdt = bundle.getString("sdt", "");
            String diaChi = bundle.getString("diaChi", "");
            byte[] avatarByteArray = bundle.getByteArray("logo");

            txtTenDv.setText(tenDonVi);
            txtEmail.setText(email);
            txtweb.setText(website);
            txtAddr.setText(sdt);
            txtSdt.setText(diaChi);

            if (avatarByteArray != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(avatarByteArray, 0, avatarByteArray.length);
                imageViewAvatar.setImageBitmap(bitmap);
            }
        }
        return view;
    }

    private void deleteEmployee() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            String tenDonvi = bundle.getString("tenDonVi", "");

            databaseHelper = new DatabaseHelper(getActivity());
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            int deletedRows = db.delete(DatabaseHelper.TABLE_DONVI, DatabaseHelper.COLUMN_TEN_DONVI + "=?", new String[]{tenDonvi});
            db.close();

            if (deletedRows > 0) {
                Toast.makeText(getActivity(), "Employee deleted successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Failed to delete employee", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
