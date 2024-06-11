package com.example.appcontact.fragment;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.appcontact.R;
import com.example.appcontact.fragment.add.Directory_add;
import com.example.appcontact.fragment.detail.DirectoryDetail;
import com.example.appcontact.models.DatabaseHelper;
import com.example.appcontact.models.Directory;
import com.example.appcontact.models.DirectoryAdapter;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Directory_show#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Directory_show extends Fragment {
    private ImageButton btnAdd;
    private DatabaseHelper dbhelper;
    private ListView listViewDirectories;
    private ArrayList<Directory> directlist;
    private DirectoryAdapter adapter;
    private EditText edtTk;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Directory_show() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Directory_show.
     */
    // TODO: Rename and change types and number of parameters
    public static Directory_show newInstance(String param1, String param2) {
        Directory_show fragment = new Directory_show();
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
        View view = inflater.inflate(R.layout.fragment_directory_show, container, false);
        edtTk = view.findViewById(R.id.searchdirec);
        btnAdd = view.findViewById(R.id.btnaddDV);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new Directory_add();
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, fragment); // R.id.fragment_container is the ID of the container where you want to replace the fragment
                transaction.addToBackStack(null); // Add transaction to back stack to allow back navigation
                transaction.commit();
            }
        });
        listViewDirectories = view.findViewById(R.id.lsvDirectory);
        Context context = getActivity();
        dbhelper = new DatabaseHelper(context);

        directlist = new ArrayList<>();

        adapter = new DirectoryAdapter(getActivity(), directlist);
        listViewDirectories.setAdapter(adapter);
        if (!loadDirects()) {
            Toast.makeText(requireContext(), "No directs found", Toast.LENGTH_SHORT).show();
        } else {
            adapter = new DirectoryAdapter(getActivity(), directlist);
            listViewDirectories.setAdapter(adapter);
        }

        listViewDirectories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // Inside onItemClick method
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Retrieve the selected employee from the list
                Directory selectedDirectory = directlist.get(position);

                // Pass the selected employee data to the detail fragment
                Bundle bundle = new Bundle();
                bundle.putString("tenDonVi", selectedDirectory.getTenDonVi());
                bundle.putString("email", selectedDirectory.getEmail());
                bundle.putString("website", selectedDirectory.getWebsite());
                bundle.putString("sdt", selectedDirectory.getSdt());
                bundle.putString("diaChi", selectedDirectory.getDiaChi());
                // Convert the avatar Bitmap to a byte array
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                selectedDirectory.getLogo().compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                bundle.putByteArray("logo", byteArray);

                // Navigate to the detail fragment
                DirectoryDetail detailFragment = new DirectoryDetail();
                detailFragment.setArguments(bundle);

                // Check if the container view exists in the activity layout
                View container = requireActivity().findViewById(R.id.frame_layout);
                if(container != null) {
                    FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame_layout, detailFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else {
                    Toast.makeText(requireContext(), "Container view not found", Toast.LENGTH_SHORT).show();
                }
            }


        });
        edtTk.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                filterDirect(s.toString());
            }
        });
        return  view;
    }
    private boolean loadDirects() {
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_DONVI, null, null, null, null, null, null);
        if (cursor.getCount() == 0) {
            cursor.close();
            db.close();
            return false; // No employees found
        }

        directlist.clear();
        String currentHeader = null;
        if (cursor.moveToFirst()) {
            do {
                byte[] imgByteArray = cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LOGO_DONVI));
                String maDonVi = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MA_DONVI));
                String tenDonVi = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TEN_DONVI));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EMAIL_DONVI));
                String website = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_WEBSITE_DONVI));
                String diaChi = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DIACHI_DONVI));
                String sdt = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SDT_DONVI));
               int direcid = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MA_DONVI));

                Bitmap bitmap = BitmapFactory.decodeByteArray(imgByteArray, 0, imgByteArray.length);
                // Giảm kích thước của bitmap
                bitmap = Bitmap.createScaledBitmap(bitmap, 40, 40, false);

                String firstLetter = tenDonVi.substring(0, 1).toUpperCase();
                if (currentHeader == null || !currentHeader.equals(firstLetter)) {
                    currentHeader = firstLetter;
                    directlist.add(new Directory(currentHeader));
                }
                Directory directory = new Directory(direcid, tenDonVi, email, website, diaChi, sdt, bitmap,false); // Pass employeeId to constructor
                directlist.add(directory);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return true;
    }

    private void filterDirect (String searchText) {
        ArrayList<Directory> filteredList = new ArrayList<>();
        String currentHeader = null;

        for (Directory directory : directlist) {
            if (directory.isHeader()) {
                currentHeader = directory.getTenDonVi();
                if (filteredList.size() == 0 || !currentHeader.equals(filteredList.get(filteredList.size() - 1).getTenDonVi())) {
                    filteredList.add(directory);
                }
            } else if (directory.getTenDonVi().toLowerCase().contains(searchText.toLowerCase())) {
                if (filteredList.size() == 0 || !currentHeader.equals(filteredList.get(filteredList.size() - 1).getTenDonVi())) {
                    filteredList.add(new Directory(directory.getTenDonVi().substring(0, 1).toUpperCase()));
                }
                filteredList.add(directory);
            }
        }

        adapter = new DirectoryAdapter(getActivity(), filteredList);
        listViewDirectories.setAdapter(adapter);
    }

}