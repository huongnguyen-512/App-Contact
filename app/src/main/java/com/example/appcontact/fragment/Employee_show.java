package com.example.appcontact.fragment;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class Employee_show extends Fragment {
    private ImageButton btnAdd;
    private DatabaseHelper dbhelper;
    private ListView listViewEmployees;
    private ArrayList<Employee> employlist;
    private EmployeeAdapter adapter;



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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employee_show2, container, false);

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

        ImageButton ic_menu = view.findViewById(R.id.ic_menu);
        ic_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });

        listViewEmployees = view.findViewById(R.id.list_view_employee);
        Context context = getActivity();
        dbhelper = new DatabaseHelper(context);

        employlist = new ArrayList<>();
        loadEmployees();

        adapter = new EmployeeAdapter(getActivity(), employlist);
        listViewEmployees.setAdapter(adapter);
        listViewEmployees.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // Inside onItemClick method
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Retrieve the selected employee from the list
                Employee selectedEmployee = employlist.get(position);

                // Pass the selected employee data to the detail fragment
                Bundle bundle = new Bundle();
                bundle.putString("hoTen", selectedEmployee.getHoTen());
                bundle.putString("chucvu", selectedEmployee.getChucVu());
                bundle.putString("email", selectedEmployee.getEmail());
                bundle.putString("sdt", selectedEmployee.getSdt());

                // Convert the avatar Bitmap to a byte array
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                selectedEmployee.getAvatar().compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                bundle.putByteArray("avatar", byteArray);

                // Navigate to the detail fragment
                EmployeeDetail detailFragment = new EmployeeDetail();
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
        return view;
    }


    private void loadEmployees() {
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_NHANVIEN, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                byte[] imgByteArray = cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ANHDAIDIEN_NV));
                String hoTen = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_HOTEN_NV));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EMAIL_NV));
                String chucvu = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CHUCVU_NV));
                String sdt = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SDT_NV));
                int employeeId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MA_NV));

                Bitmap bitmap = BitmapFactory.decodeByteArray(imgByteArray, 0, imgByteArray.length);
                // Giảm kích thước của bitmap
                bitmap = Bitmap.createScaledBitmap(bitmap, 40, 40, false);
                Employee employee = new Employee(employeeId, hoTen, chucvu, email, sdt, bitmap); // Pass employeeId to constructor
                employlist.add(employee);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
    }


    private void showPopupMenu(View view) {
        PopupMenu popup = new PopupMenu(requireContext(), view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.popup_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.ic_chon1) {
                    Toast.makeText(requireContext(), "Chọn", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (id == R.id.chontat) {
                    Toast.makeText(requireContext(), "Chọn tất cả", Toast.LENGTH_SHORT).show();
                    return true;
                } else {
                    return false;
                }
            }
        });
        popup.show();
    }

}
