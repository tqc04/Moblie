package com.example.finalapp.custom;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalapp.Detail;
import com.example.finalapp.R;
import com.example.finalapp.model.BaiDang;
import com.example.finalapp.ui.ggMap.MapsActivity3;

import java.util.List;

public class SearchAdapter extends BaseAdapter {
    private List<BaiDang> list;
    private Context context;

    public SearchAdapter(List<BaiDang> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_view_motel, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        BaiDang baiDang = list.get(position);

        holder.mtieude.setText(baiDang.getTieude());
        holder.mmota.setText(baiDang.getMota());
        holder.mdiachi.setText("Địa chỉ: " + baiDang.getAddress() + ", " + baiDang.getHuyen() + ", " + baiDang.getTinh());
        holder.mhinhthuc.setText("Loại tin: " + baiDang.getTitle());
        holder.mgia.setText("Giá: " + baiDang.getPrice() + " VND");
        holder.mdientich.setText("Diện tích: " + baiDang.getDientich() + " m²");
        holder.mphone.setText("SĐT: " + baiDang.getPhone());

        // Xử lý ảnh Base64
        String imgString = baiDang.getImg();
        if (!imgString.startsWith("data:image/png;base64,")) {
            imgString = "data:image/png;base64," + imgString;
        }

        Bitmap bitmap = decodeBase64ToBitmap(imgString);
        if (bitmap != null) {
            holder.image.setImageBitmap(bitmap);
        } else {
            holder.image.setImageResource(R.drawable.placeholder_image); // ảnh mặc định nếu lỗi
        }

        holder.image.setOnClickListener(v -> {

            if (baiDang.getLatitude() != null && baiDang.getLongitude() != null &&
                    !baiDang.getLatitude().isEmpty() && !baiDang.getLongitude().isEmpty()) {

                Intent intent = new Intent(context, MapsActivity3.class);
                intent.putExtra("lat", baiDang.getLatitude());
                intent.putExtra("lng", baiDang.getLongitude());
                intent.putExtra("address", baiDang.getAddress());
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "Phòng trọ chưa có tọa độ!", Toast.LENGTH_SHORT).show();
            }
        });
        convertView.setOnClickListener(v -> {
            if (baiDang.getKey() != null) {
                Intent intent = new Intent(context, Detail.class);
                intent.putExtra("key", baiDang.getKey());
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "Không tìm thấy key bài đăng!", Toast.LENGTH_SHORT).show();
            }
        });




        return convertView;
    }

    private Bitmap decodeBase64ToBitmap(String base64Image) {
        try {
            String base64 = base64Image.split(",")[1];
            byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        } catch (Exception e) {
            return null;
        }
    }

    public static class ViewHolder {
        TextView mtieude, mmota, mdiachi, mhinhthuc, mtinh, mhuyen, mphone, mdientich, mgia;
        ImageView image;

        public ViewHolder(View itemView) {
            image = itemView.findViewById(R.id.image);
            mtieude = itemView.findViewById(R.id.text_title);
            mmota = itemView.findViewById(R.id.text_content);
            mdiachi = itemView.findViewById(R.id.diachi);
            mhinhthuc = itemView.findViewById(R.id.hinhthuc);
//            mtinh = itemView.findViewById(R.id.tinh);
//            mhuyen = itemView.findViewById(R.id.huyen);
            mphone = itemView.findViewById(R.id.phone);
            mdientich = itemView.findViewById(R.id.txt_dientich);
            mgia = itemView.findViewById(R.id.gia);
        }
    }
}
