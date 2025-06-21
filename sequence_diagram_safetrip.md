# Sequence Diagram - Đặt vé và thanh toán trực tiếp SAFETRIP

## Mô tả tương tác giữa các đối tượng

```
┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐
│Khách hàng│ │   GUI    │ │Controller│ │ Database │ │EmailService│ │Nhân viên │
└─────┬────┘ └─────┬────┘ └─────┬────┘ └─────┬────┘ └─────┬────┘ └─────┬────┘
      │            │            │            │            │            │
      │ 1.Truy cập │            │            │            │            │
      ├───────────►│            │            │            │            │
      │            │            │            │            │            │
      │ 2.Nhập thông tin chuyến │            │            │            │
      │   (nơi đi, nơi đến,    │            │            │            │
      │    loại vé, ngày, SL)  │            │            │            │
      ├───────────►│            │            │            │            │
      │            │            │            │            │            │
      │            │ 3.Gửi thông tin         │            │            │
      │            ├───────────►│            │            │            │
      │            │            │            │            │            │
      │            │            │ 4.Truy vấn │            │            │
      │            │            │   chuyến đi│            │            │
      │            │            ├───────────►│            │            │
      │            │            │            │            │            │
      │            │            │ 5.Danh sách│            │            │
      │            │            │   giờ & giá│            │            │
      │            │            │◄───────────┤            │            │
      │            │            │            │            │            │
      │            │ 6.Hiển thị │            │            │            │
      │            │   danh sách│            │            │            │
      │            │◄───────────┤            │            │            │
      │            │            │            │            │            │
      │ 7.Chọn giờ │            │            │            │            │
      │   khởi hành│            │            │            │            │
      ├───────────►│            │            │            │            │
      │            │            │            │            │            │
      │            │ 8.Tính tiền│            │            │            │
      │            ├───────────►│            │            │            │
      │            │            │            │            │            │
      │            │ 9.Tổng tiền│            │            │            │
      │            │◄───────────┤            │            │            │
      │            │            │            │            │            │
      │ 10.Xác nhận│            │            │            │            │
      │    đặt vé  │            │            │            │            │
      ├───────────►│            │            │            │            │
      │            │            │            │            │            │
  ┌───┴───┐        │            │            │            │            │
  │alt    │        │            │            │            │            │
  │       │ 11a.Đăng nhập      │            │            │            │
  │[Có TK]├───────►│            │            │            │            │
  │       │        │ 12a.Xác thực            │            │            │
  │       │        ├───────────►├───────────►│            │            │
  │       │        │            │◄───────────┤            │            │
  │       │        │◄───────────┤            │            │
  │       │        │            │            │            │
  │[Không │ 11b.Nhập thông tin │            │            │            │
  │ TK]   │   cá nhân chi tiết │            │            │            │
  │       ├───────►│            │            │            │            │
  └───┬───┘        │            │            │            │            │
      │            │            │            │            │            │
      │ 13.Chọn nơi│            │            │            │            │
      │   giao vé  │            │            │            │            │
      ├───────────►│            │            │            │            │
      │            │            │            │            │            │
  ┌───┴───┐        │            │            │            │            │
  │alt    │        │            │            │            │            │
  │       │ 14a.Chọn chi nhánh │            │            │            │
  │[Chi   ├───────►│            │            │            │            │
  │nhánh] │        │            │            │            │            │
  │       │ 14b.Nhập địa chỉ   │            │            │            │
  │[Địa   │    chi tiết        │            │            │            │
  │chỉ    ├───────►│            │            │            │            │
  │khác]  │        │            │            │            │            │
  └───┬───┘        │            │            │            │            │
      │            │            │            │            │            │
      │            │ 15.Tính phí│            │            │            │
      │            │    giao vé │            │            │            │
      │            ├───────────►│            │            │            │
      │            │            │            │            │            │
      │            │ 16.Tổng   │            │            │            │
      │            │   chi phí  │            │            │            │
      │            │◄───────────┤            │            │            │
      │            │            │            │            │            │
      │ 17.Xác nhận│            │            │            │            │
      │ thanh toán │            │            │            │            │
      │    sau     │            │            │            │            │
      ├───────────►│            │            │            │            │
      │            │            │            │            │            │
      │            │ 18.Tạo đơn│            │            │            │
      │            │    hàng    │            │            │            │
      │            ├───────────►│            │            │            │
      │            │            │            │            │            │
      │            │            │ 19.Lưu đơn │            │            │
      │            │            │    hàng    │            │            │
      │            │            ├───────────►│            │            │
      │            │            │            │            │            │
      │            │            │ 20.Mã GD   │            │            │
      │            │            │◄───────────┤            │            │
      │            │            │            │            │            │
      │            │            │ 21.Gửi email xác nhận    │            │
      │            │            ├───────────────────────►│            │
      │            │            │            │            │            │
      │ 22.Email   │            │            │            │            │
      │ xác nhận   │            │            │            │            │
      │◄───────────┼────────────┼────────────┼───────────┤            │
      │            │            │            │            │            │
      │ 23.Click   │            │            │            │            │
      │   link XN  │            │            │            │            │
      ├───────────►├───────────►│            │            │            │
      │            │            │            │            │            │
      │            │            │ 24.Cập nhật│            │            │
      │            │            │  xác nhận  │            │            │
      │            │            ├───────────►│            │            │
      │            │            │            │            │ 25.Giao vé │
      │            │            │            │            │     & thu  │
      │◄───────────┼────────────┼────────────┼───────────┼───────────┤
      │            │            │            │            │    tiền    │
      │            │            │            │            │            │
      │            │            │ 26.Cập nhật trạng thái  │            │
      │            │            │◄───────────┼───────────┼───────────┤
      │            │            │            │            │            │
      │            │            │ 27.Cập nhật│            │            │
      │            │            │    DB      │            │            │
      │            │            ├───────────►│            │            │
      │            │            │            │            │            │
```

## Mô tả chi tiết các bước:

1. **Khách hàng truy cập hệ thống** qua web/mobile
2. **Nhập thông tin chuyến đi**: nơi đi, nơi đến, loại vé (một chiều/khứ hồi), ngày, số lượng
3. **GUI gửi thông tin** đến Controller
4. **Controller truy vấn** database để lấy danh sách chuyến
5. **Database trả về** danh sách giờ khởi hành và giá
6. **Hiển thị danh sách** cho khách hàng
7. **Khách hàng chọn** giờ khởi hành
8. **Tính tổng tiền** vé
9. **Hiển thị tổng tiền** cho khách hàng
10. **Khách hàng xác nhận** đặt vé

**Phân nhánh tài khoản:**
- 11a-12a: Nếu có tài khoản → đăng nhập và xác thực
- 11b: Nếu không có tài khoản → nhập thông tin cá nhân chi tiết

13. **Chọn nơi giao vé**

**Phân nhánh địa điểm:**
- 14a: Chọn chi nhánh (phí = 0)
- 14b: Nhập địa chỉ khác (tính phí theo km)

15. **Tính phí giao vé** (nếu có)
16. **Hiển thị tổng chi phí** (vé + phí giao)
17. **Xác nhận thanh toán sau**
18-20. **Tạo và lưu đơn hàng**, nhận mã giao dịch
21-22. **Gửi email xác nhận** với link
23-24. **Khách hàng click link** xác nhận
25. **Nhân viên giao vé** và thu tiền
26-27. **Cập nhật trạng thái** đã thanh toán 