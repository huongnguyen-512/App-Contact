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
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Pattern;

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

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public Directory_show() {
        // Required empty public constructor
    }

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
                transaction.replace(R.id.frame_layout, fragment);
                transaction.addToBackStack(null);
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
            Toast.makeText(requireContext(), "No directories found", Toast.LENGTH_SHORT).show();
        } else {
            adapter = new DirectoryAdapter(getActivity(), directlist);
            listViewDirectories.setAdapter(adapter);
        }

        listViewDirectories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Directory selectedDirectory = directlist.get(position);

                Bundle bundle = new Bundle();
                bundle.putString("tenDonVi", selectedDirectory.getTenDonVi());
                bundle.putString("email", selectedDirectory.getEmail());
                bundle.putString("website", selectedDirectory.getWebsite());
                bundle.putString("sdt", selectedDirectory.getSdt());
                bundle.putString("diaChi", selectedDirectory.getDiaChi());
                bundle.putString("idCha", selectedDirectory.getMaDonViCha());

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                selectedDirectory.getLogo().compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                bundle.putByteArray("logo", byteArray);

                DirectoryDetail detailFragment = new DirectoryDetail();
                detailFragment.setArguments(bundle);

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
        return view;
    }

    private String normalizeString(String input) {
        if (input == null) {
            return "";
        }
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        normalized = pattern.matcher(normalized).replaceAll("");
        return normalized.replaceAll("\\s+", " ").trim();
    }

    private boolean loadDirects() {
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_DONVI, null, null, null, null, null, null);
        if (cursor.getCount() == 0) {
            cursor.close();
            db.close();
            return false;
        }

        directlist.clear();
        if (cursor.moveToFirst()) {
            do {
                byte[] imgByteArray = cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LOGO_DONVI));
                String idCha = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MA_DONVI_CHA));
                String tenDonVi = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TEN_DONVI));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EMAIL_DONVI));
                String website = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_WEBSITE_DONVI));
                String diaChi = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DIACHI_DONVI));
                String sdt = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SDT_DONVI));
                int direcid = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MA_DONVI));

                Bitmap bitmap = BitmapFactory.decodeByteArray(imgByteArray, 0, imgByteArray.length);
                bitmap = Bitmap.createScaledBitmap(bitmap, 40, 40, false);

                Directory directory = new Directory(direcid, tenDonVi, email, website, diaChi, sdt, bitmap, idCha, false);
                directlist.add(directory);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        // Sort the list alphabetically by the normalized unit name
        Collections.sort(directlist, new Comparator<Directory>() {
            @Override
            public int compare(Directory d1, Directory d2) {
                String name1 = normalizeString(d1.getTenDonVi());
                String name2 = normalizeString(d2.getTenDonVi());
                return name1.compareTo(name2);
            }
        });

        // Add headers after sorting
        String currentHeader = null;
        ArrayList<Directory> sortedListWithHeaders = new ArrayList<>();
        for (Directory directory : directlist) {
            String firstLetter = normalizeString(directory.getTenDonVi()).substring(0, 1).toUpperCase();
            if (currentHeader == null || !currentHeader.equals(firstLetter)) {
                currentHeader = firstLetter;
                sortedListWithHeaders.add(new Directory(currentHeader));
            }
            sortedListWithHeaders.add(directory);
        }

        directlist.clear();
        directlist.addAll(sortedListWithHeaders);

        return true;
    }

    private void filterDirect(String searchText) {
        ArrayList<Directory> filteredList = new ArrayList<>();
        String currentHeader = null;
        String normalizedSearchText = normalizeString(searchText).toLowerCase();

        for (Directory directory : directlist) {
            if (directory.isHeader()) {
                currentHeader = directory.getTenDonVi();
                if (filteredList.size() == 0 || !currentHeader.equals(filteredList.get(filteredList.size() - 1).getTenDonVi())) {
                    filteredList.add(directory);
                }
            } else if (normalizeString(directory.getTenDonVi()).toLowerCase().contains(normalizedSearchText)) {
                String firstLetter = normalizeString(directory.getTenDonVi()).substring(0, 1).toUpperCase();
                if (currentHeader == null || !currentHeader.equals(firstLetter)) {
                    currentHeader = firstLetter;
                    filteredList.add(new Directory(currentHeader));
                }
                filteredList.add(directory);
            }
        }

        adapter = new DirectoryAdapter(getActivity(), filteredList);
        listViewDirectories.setAdapter(adapter);
    }
}
