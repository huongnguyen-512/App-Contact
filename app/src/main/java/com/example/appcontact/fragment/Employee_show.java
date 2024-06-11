package com.example.appcontact.fragment;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.appcontact.R;
import com.example.appcontact.databinding.FragmentEmployeeShow2Binding;
import com.example.appcontact.fragment.add.Employee_add;
import com.example.appcontact.fragment.detail.EmployeeDetail;
import com.example.appcontact.models.DatabaseHelper;
import com.example.appcontact.models.Employee;
import com.example.appcontact.models.EmployeeAdapter;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Employee_show extends Fragment {

    private ImageButton btnAdd;
    private DatabaseHelper dbhelper;
    private ListView listViewEmployees;
    private ArrayList<Employee> employlist;
    private EmployeeAdapter adapter;
    private EditText edtTk;

    public static Employee_show newInstance(String param1, String param2) {
        Employee_show fragment = new Employee_show();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employee_show2, container, false);
        edtTk = view.findViewById(R.id.edtTKNV);

        btnAdd = view.findViewById(R.id.btnadd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new Employee_add();
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        listViewEmployees = view.findViewById(R.id.list_view_employee);

        Context context = getActivity();
        dbhelper = new DatabaseHelper(context);

        employlist = new ArrayList<>();

        // Load employees only if there are some
        if (!loadEmployees()) {
            Toast.makeText(requireContext(), "No employees found", Toast.LENGTH_SHORT).show();
        } else {
            adapter = new EmployeeAdapter(getActivity(), employlist);
            listViewEmployees.setAdapter(adapter);
        }

        listViewEmployees.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Employee selectedEmployee = employlist.get(position);

                if (!selectedEmployee.isHeader()) {
                    Bundle bundle = new Bundle();
                    bundle.putString("hoTen", selectedEmployee.getHoTen());
                    bundle.putString("chucvu", selectedEmployee.getChucVu());
                    bundle.putString("email", selectedEmployee.getEmail());
                    bundle.putString("sdt", selectedEmployee.getSdt());
                    bundle.putString("tenDonVi", selectedEmployee.getTenDonVi());

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    selectedEmployee.getAvatar().compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    bundle.putByteArray("avatar", byteArray);

                    EmployeeDetail detailFragment = new EmployeeDetail();
                    detailFragment.setArguments(bundle);

                    View container = requireActivity().findViewById(R.id.frame_layout);
                    if (container != null) {
                        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_layout, detailFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    } else {
                        Toast.makeText(requireContext(), "Container view not found", Toast.LENGTH_SHORT).show();
                    }
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
                filterEmployees(s.toString());
            }
        });

        return view;
    }

    private boolean loadEmployees() {
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_NHANVIEN, null, null, null, null, null, DatabaseHelper.COLUMN_HOTEN_NV + " ASC");

        if (cursor.getCount() == 0) {
            cursor.close();
            db.close();
            return false; // No employees found
        }

        employlist.clear();
        String currentHeader = null;

        if (cursor.moveToFirst()) {
            do {
                byte[] imgByteArray = cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ANHDAIDIEN_NV));
                String hoTen = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_HOTEN_NV));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EMAIL_NV));
                String chucvu = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CHUCVU_NV));
                String sdt = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SDT_NV));
                int employeeId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MA_NV));
                String tenDvi = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TEN_DONVI_NV));
                Bitmap bitmap = BitmapFactory.decodeByteArray(imgByteArray, 0, imgByteArray.length);
                bitmap = Bitmap.createScaledBitmap(bitmap, 40, 40, false);

                // Get the first letter of the employee's name
                String firstLetter = hoTen.substring(0, 1).toUpperCase();

                // If the header doesn't exist or is different, create a new header
                if (currentHeader == null || !currentHeader.equals(firstLetter)) {
                    currentHeader = firstLetter;
                    employlist.add(new Employee(currentHeader));
                }

                Employee employee = new Employee(employeeId, hoTen, chucvu, email, sdt, bitmap, false, tenDvi);
                employlist.add(employee);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return true; // Employees loaded successfully
    }

    private void filterEmployees(String searchText) {
        ArrayList<Employee> filteredList = new ArrayList<>();
        String currentHeader = null;

        for (Employee employee : employlist) {
            if (employee.isHeader()) {
                currentHeader = employee.getHoTen();
                if (filteredList.size() == 0 || !currentHeader.equals(filteredList.get(filteredList.size() - 1).getHoTen())) {
                    filteredList.add(employee);
                }
            } else if (employee.getHoTen().toLowerCase().contains(searchText.toLowerCase())) {
                if (filteredList.size() == 0 || !currentHeader.equals(filteredList.get(filteredList.size() - 1).getHoTen())) {
                    filteredList.add(new Employee(employee.getHoTen().substring(0, 1).toUpperCase()));
                }
                filteredList.add(employee);
            }
        }

        adapter = new EmployeeAdapter(getActivity(), filteredList);
        listViewEmployees.setAdapter(adapter);
    }
}
