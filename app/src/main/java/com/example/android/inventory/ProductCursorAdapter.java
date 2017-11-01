package com.example.android.inventory;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static android.R.attr.id;
import static com.example.android.inventory.data.ProductContract.*;
import static java.security.AccessController.getContext;

/**
 * Created by narbeh on 10/24/17.
 */

public class ProductCursorAdapter extends CursorAdapter {

    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        TextView nameTextView = (TextView) view.findViewById(R.id.product_name);
        TextView quantityTextView = (TextView) view.findViewById(R.id.quantity);
        TextView priceTextView = (TextView) view.findViewById(R.id.price);
        ImageView productImageView = (ImageView) view.findViewById(R.id.product_image);


        Button sellButton = (Button) view.findViewById(R.id.sell);

        final int position = cursor.getPosition();

        sellButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                cursor.moveToPosition(position);

                int itemIdColumnIndex = cursor.getColumnIndex(ProductEntry._ID);
                final long itemId = cursor.getLong(itemIdColumnIndex);
                Uri currentProductUri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI, itemId);

                int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_QUANTITY);
                String phoneQuantity = cursor.getString(quantityColumnIndex);
                int updateQuantity = Integer.parseInt(phoneQuantity);

                if (updateQuantity > 0) {
                    updateQuantity--;
                    ContentValues updateValues = new ContentValues();
                    updateValues.put(ProductEntry.COLUMN_QUANTITY, updateQuantity);
                    int rowsUpdate = context.getContentResolver().update(currentProductUri, updateValues, null, null);
                } else {
                Toast.makeText(context, "Quantity is 0 and can't be reduced.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
        int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_QUANTITY);
        int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRICE);
        int imageColumnIndex =cursor.getColumnIndex(ProductEntry.COLUMN_IMAGE);

        String productName = cursor.getString(nameColumnIndex);
        int quantity = cursor.getInt(quantityColumnIndex);
        int price = cursor.getInt(priceColumnIndex);
        byte[] image = cursor.getBlob(imageColumnIndex);
        if (image != null) {
            Bitmap bmp = BitmapFactory.decodeByteArray(image, 0, image.length);
            productImageView.setImageBitmap(bmp);
        } else {
            productImageView.setImageResource(R.drawable.product_placeholder);
        }
        nameTextView.setText(productName);
        quantityTextView.setText(Integer.toString(quantity));
        priceTextView.setText(Integer.toString(price));
    }
}
