package com.example.appcontact.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.appcontact.R;

import java.util.ArrayList;

public class DirectoryAdapter extends ArrayAdapter<Directory> {
    public DirectoryAdapter(@NonNull Context context, ArrayList<Directory> directoryArrayListArrayList ) {
        super(context, R.layout.custom_listview, directoryArrayListArrayList);
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Directory directory = getItem(position);
        DirectoryAdapter.ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_listview, parent, false);
            viewHolder = new DirectoryAdapter.ViewHolder();
            viewHolder.listimg = convertView.findViewById(R.id.logo);
            viewHolder.txtname = convertView.findViewById(R.id.txtName);
            viewHolder.txtsdt = convertView.findViewById(R.id.txtSDT);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (DirectoryAdapter.ViewHolder) convertView.getTag();
        }

        // Displaying employee details
        if (directory != null) {
            // Get the Bitmap directly from the Employee object
            Bitmap img = directory.logo;

            // Create a circular bitmap with a desired size (e.g., 200x200 pixels)
            Bitmap circularBitmap = getRoundedBitmap(img, 200);

            viewHolder.listimg.setImageBitmap(circularBitmap);
            viewHolder.txtname.setText(directory.tenDonVi);
            viewHolder.txtsdt.setText(directory.sdt);
        }

        return convertView;
    }
    static class ViewHolder {
        ImageView listimg;
        TextView txtname, txtsdt;
    }

    // Utility method to create a rounded bitmap with the desired size
    private Bitmap getRoundedBitmap(Bitmap bitmap, int targetSize) {
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, targetSize, targetSize, true);
        Bitmap output = Bitmap.createBitmap(targetSize, targetSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, targetSize, targetSize);
        final RectF rectF = new RectF(rect);
        final float roundPx = targetSize / 2;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(scaledBitmap, rect, rect, paint);

        return output;
    }
}

