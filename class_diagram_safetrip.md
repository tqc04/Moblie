# Class Diagram - Đặt vé và thanh toán trực tiếp SAFETRIP

## Biểu đồ lớp

```
┌─────────────────────────────┐
│         Customer            │
├─────────────────────────────┤
│ - customerId: String        │
│ - fullName: String          │
│ - gender: String            │
│ - birthYear: int            │
│ - idNumber: String          │
│ - email: String             │
│ - phone: String             │
│ - permanentAddress: String  │
├─────────────────────────────┤
│ + register(): void          │
│ + updateInfo(): void        │
│ + viewBookingHistory(): List│
└─────────────────────────────┘
                ▲
                │ extends
┌─────────────────────────────┐
│    RegisteredCustomer       │
├─────────────────────────────┤
│ - username: String          │
│ - password: String          │
│ - isActivated: boolean      │
│ - activationLink: String    │
├─────────────────────────────┤
│ + login(): boolean          │
│ + logout(): void            │
│ + changePassword(): void    │
│ + activateAccount(): void   │
└─────────────────────────────┘

┌─────────────────────────────┐         ┌─────────────────────────────┐
│           Trip              │         │         Schedule            │
├─────────────────────────────┤         ├─────────────────────────────┤
│ - tripId: String            │         │ - scheduleId: String        │
│ - origin: String            │◆────────│ - tripId: String            │
│ - destination: String       │   1  *  │ - departureTime: DateTime   │
│ - distance: double          │         │ - arrivalTime: DateTime     │
├─────────────────────────────┤         │ - price: double             │
│ + getAvailableSchedules():  │         │ - availableSeats: int       │
│   List<Schedule>            │         ├─────────────────────────────┤
│ + calculateDistance(): double│         │ + checkAvailability(): boolean│
└─────────────────────────────┘         │ + updateSeats(): void       │
                                       └─────────────────────────────┘

┌─────────────────────────────┐
│          Booking            │
├─────────────────────────────┤
│ - bookingId: String         │
│ - customerId: String        │
│ - scheduleId: String        │  
│ - bookingDate: DateTime     │
│ - ticketType: TicketType    │
│ - departureDate: Date       │
│ - returnDate: Date          │
│ - quantity: int             │
│ - totalAmount: double       │
│ - status: BookingStatus     │
├─────────────────────────────┤
│ + createBooking(): void     │
│ + calculateTotal(): double  │
│ + confirmBooking(): void    │
│ + cancelBooking(): void     │
└─────────────────────────────┘
         │
         │ 1
         ▼ 1
┌─────────────────────────────┐         ┌─────────────────────────────┐
│        Transaction          │         │         Branch              │
├─────────────────────────────┤         ├─────────────────────────────┤
│ - transactionId: String     │         │ - branchId: String          │
│ - bookingId: String         │         │ - branchName: String        │
│ - paymentMethod: PaymentMethod│       │ - address: String           │
│ - transactionDate: DateTime │         │ - phone: String             │
│ - confirmationLink: String   │         ├─────────────────────────────┤
│ - isConfirmed: boolean       │         │ + calculateDistance(address:│
├─────────────────────────────┤         │   String): double           │
│ + generateTransactionId(): String│     └─────────────────────────────┘
│ + sendConfirmationEmail(): void│                    ▲
│ + confirmTransaction(): void │                      │
└─────────────────────────────┘                      │
         │                                           │
         │ 1                                         │
         ▼ 0..1                                      │
┌─────────────────────────────┐                      │
│     DeliveryInfo            │                      │
├─────────────────────────────┤                      │
│ - deliveryId: String        │                      │
│ - transactionId: String     │◇─────────────────────┘
│ - deliveryType: DeliveryType│
│ - deliveryAddress: String   │
│ - deliveryFee: double       │
│ - deliveryStatus: DeliveryStatus│
│ - deliveryDate: DateTime    │
├─────────────────────────────┤
│ + calculateDeliveryFee(): double│
│ + updateDeliveryStatus(): void│
└─────────────────────────────┘

┌─────────────────────────────┐         ┌─────────────────────────────┐
│      EmailService           │         │        StaffMember          │
├─────────────────────────────┤         ├─────────────────────────────┤
│ - smtpServer: String        │         │ - staffId: String           │
│ - port: int                 │         │ - name: String              │
├─────────────────────────────┤         │ - role: String              │
│ + sendConfirmationEmail(    │         ├─────────────────────────────┤
│   email: String,            │         │ + deliverTicket(delivery:   │
│   transaction: Transaction):│         │   DeliveryInfo): void       │
│   void                      │         │ + collectPayment(amount:    │
│ + sendActivationEmail(      │         │   double): void             │
│   customer: Customer): void │         │ + updateOrderStatus(booking:│
└─────────────────────────────┘         │   Booking, status: String): │
                                       │   void                      │
                                       └─────────────────────────────┘

<<enumeration>>                 <<enumeration>>              <<enumeration>>
┌───────────────┐              ┌─────────────────┐          ┌──────────────────┐
│  TicketType   │              │  BookingStatus  │          │  PaymentMethod   │
├───────────────┤              ├─────────────────┤          ├──────────────────┤
│ ONE_WAY       │              │ PENDING         │          │ CASH_ON_DELIVERY │
│ ROUND_TRIP    │              │ CONFIRMED       │          │ ONLINE_PAYMENT   │
└───────────────┘              │ PAID            │          └──────────────────┘
                              │ CANCELLED       │
                              └─────────────────┘

<<enumeration>>                 <<enumeration>>
┌─────────────────┐            ┌──────────────────┐
│  DeliveryType   │            │ DeliveryStatus   │
├─────────────────┤            ├──────────────────┤
│ BRANCH_PICKUP   │            │ PENDING          │
│ HOME_DELIVERY   │            │ IN_PROGRESS      │
└─────────────────┘            │ DELIVERED        │
                              └──────────────────┘
```

## Mô tả các quan hệ:

### Quan hệ kế thừa:
- `RegisteredCustomer` kế thừa từ `Customer`: Khách hàng đã đăng ký có thêm thông tin tài khoản

### Quan hệ hợp thành (Composition):
- `Trip` ◆──── `Schedule` (1 to *): Một chuyến đi có nhiều lịch trình

### Quan hệ kết hợp (Association):
- `Booking` ──── `Customer`: Mỗi booking thuộc về một khách hàng
- `Booking` ──── `Schedule`: Mỗi booking liên kết với một lịch trình
- `Transaction` ──── `Booking`: Mỗi giao dịch liên kết với một booking
- `DeliveryInfo` ──── `Transaction`: Thông tin giao vé liên kết với giao dịch

### Quan hệ tổng hợp (Aggregation):
- `DeliveryInfo` ◇──── `Branch`: Giao vé có thể liên quan đến chi nhánh

## Mô tả các lớp chính:

1. **Customer/RegisteredCustomer**: Quản lý thông tin khách hàng
2. **Trip**: Quản lý thông tin tuyến đường
3. **Schedule**: Quản lý lịch trình và giá vé
4. **Booking**: Quản lý đặt vé
5. **Transaction**: Quản lý giao dịch thanh toán
6. **DeliveryInfo**: Quản lý thông tin giao vé
7. **Branch**: Quản lý chi nhánh
8. **EmailService**: Dịch vụ gửi email
9. **StaffMember**: Nhân viên giao vé và thu tiền

## Các enum:
- **TicketType**: Loại vé (một chiều/khứ hồi)
- **BookingStatus**: Trạng thái đặt vé
- **PaymentMethod**: Phương thức thanh toán
- **DeliveryType**: Loại hình giao vé
- **DeliveryStatus**: Trạng thái giao vé 