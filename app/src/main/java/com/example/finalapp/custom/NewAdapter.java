package com.example.finalapp.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.finalapp.R;
import com.example.finalapp.model.BaiDang;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewAdapter extends BaseAdapter {
    private List<BaiDang> list;
    private Context context;
    private LayoutInflater layoutInflater; // Để nén các view từ XML.

    public NewAdapter(List<BaiDang> list, Context context) {
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
        ViewHolder holder; // Khai báo biến để giữ các thành phần giao diện.
        // Kiểm tra nếu view chưa được tạo.
        if (convertView == null) {
            // Sử dụng LayoutInflater để nén layout từ tệp XML item_baiviet thành một đối tượng view.
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_baiviet, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        BaiDang baiDang = list.get(position);
        holder.mtieude.setText("Tiêu đề: "+baiDang.getTieude());
        holder.mmota.setText(baiDang.getMota());
        holder.mdiachi.setText("Địa chỉ: "+ baiDang.getAddress()+", "+baiDang.getHuyen()+", "+baiDang.getTinh());
        holder.mhinhthuc.setText("Hình thức: "+baiDang.getTitle());
        holder.mgia.setText(baiDang.getPrice());
        holder.mtinh.setText(baiDang.getTinh());
        holder.mhuyen.setText(baiDang.getHuyen());
        holder.mphone.setText(baiDang.getPhone());
        holder.mdientich.setText("Diện tích: "+baiDang.getDientich()+" m²");

        // lấy ảnh
        String imgString = "data:image/png;base64," + baiDang.getImg();
        Bitmap bitmap = decodeBase64ToBitmap(imgString);
        if (bitmap != null) {
            holder.image.setImageBitmap(bitmap);
        } else {
            holder.image.setImageResource(R.drawable.placeholder_image); // Ảnh mặc định nếu không giải mã được
        }

        return convertView;
    }
    private Bitmap decodeBase64ToBitmap(String base64Image) {
        if (base64Image.startsWith("data:image/png;base64,")) {
            String base64 = base64Image.split(",")[1]; // Tách phần mã hóa
            byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        } else {
            return null; // Không phải chuỗi Base64 hợp lệ
        }
    }

    public class ViewHolder {
        TextView mtieude, mmota, mdiachi, mhinhthuc, mtinh, mhuyen, mphone, mdientich, mgia;
        ImageView image;
        // Constructor để gán các view từ layout.
        public ViewHolder( View itemView) {
            image = itemView.findViewById(R.id.image);
            mtieude = itemView.findViewById(R.id.text_title);
            mmota = itemView.findViewById(R.id.text_content);
            mdiachi = itemView.findViewById(R.id.diachi);
            mhinhthuc = itemView.findViewById(R.id.hinhthuc);
            mtinh = itemView.findViewById(R.id.tinh);
            mhuyen = itemView.findViewById(R.id.huyen);
            mphone = itemView.findViewById(R.id.phone);
            mdientich = itemView.findViewById(R.id.txt_dientich);
            mgia = itemView.findViewById(R.id.gia);
        }
    }
}
