package com.example.finalapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.finalapp.R;
import com.example.finalapp.model.Motel;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CustomListAdapter extends RecyclerView.Adapter<CustomListAdapter.ViewHolder> {
    private Context context;
    private List<Motel> motelList;

    public CustomListAdapter(Context context, List<Motel> motelList) {
        this.context = context;
        this.motelList = motelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_motel, parent, false);
        return new ViewHolder(view);
    }

@Override
public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    Motel motel = motelList.get(position);

    String imgString = "data:image/png;base64,"+motel.getImg();
    Log.d("Image URL", imgString);

//    if (imgString.startsWith("data:image")) { // Kiểm tra nếu là chuỗi mã hóa Base64
        Bitmap bitmap = decodeBase64ToBitmap(imgString);
        holder.imageView.setImageBitmap(bitmap);
//    } else {
//        // Load image using Glide
//        Glide.with(context)
//                .load(imgString)
//                .placeholder(R.drawable.placeholder_image)
//                .error(R.drawable.error_image)
//                .into(holder.imageView);
//    }

    // Set text fields
    holder.titleTextView.setText("Tiêu đề: " + motel.getTieude());
    holder.descriptionTextView.setText(motel.getMota());
//    holder.addressTextView.setText("Địa chỉ: "+motel.getAddress());
    String priceFormatted = formatPrice(motel.getPrice());
    holder.priceTextView.setText("Giá: " + priceFormatted);
    holder.areaTextView.setText("Diện tích: " + motel.getDientich() +" m²");
    holder.phoneTextView.setText("Số điện thoại liên hệ: "+ motel.getPhone());
    holder.locationTextView.setText("Địa chỉ: "+ motel.getAddress()+", "+motel.getTinh() + ", " + motel.getHuyen());
}
    private String formatPrice(String price) {
        try {
            // Chuyển đổi chuỗi giá tiền thành số
            double priceValue = Double.parseDouble(price);
            // Định dạng số với dấu phẩy
            NumberFormat formatter = NumberFormat.getInstance(Locale.getDefault());
            return formatter.format(priceValue) + " VND"; // Thêm đơn vị tiền tệ
        } catch (NumberFormatException e) {
            return price; // Nếu có lỗi, trả về giá gốc
        }
    }
    @Override
    public int getItemCount() {
        return motelList.size();
    }
    private Bitmap decodeBase64ToBitmap(String base64Image) {
        // Nếu chuỗi không có header, bạn cần thêm nó vào
        if (!base64Image.startsWith("data:image/png;base64,")) {
            base64Image = "data:image/png;base64," + base64Image;
        }

        String base64 = base64Image.split(",")[1]; // Tách phần mã hóa
        byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTextView;
        TextView descriptionTextView;
//        TextView addressTextView;
        TextView priceTextView;
        TextView areaTextView;
        TextView phoneTextView;
        TextView locationTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
//            addressTextView = itemView.findViewById(R.id.addressTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            areaTextView = itemView.findViewById(R.id.areaTextView);
            phoneTextView = itemView.findViewById(R.id.phoneTextView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
        }
    }
} 