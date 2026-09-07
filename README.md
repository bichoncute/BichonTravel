#  [比熊旅遊] Bichon Travel

> **On-line Travel Booking System**
> 線上旅遊購票平台網站

比熊旅遊（Bichon Travel）是一個以 **Spring Boot + Thymeleaf + MySQL** 開發的線上旅遊購票平台，提供訪客、會員與管理者三種不同角色，分別對應前台瀏覽、會員購物與後台管理等功能。

本專案以「比熊犬」帶來的純粹快樂與療癒感為品牌概念，希望將輕鬆、愉快的體驗融入旅遊服務，打造簡單且完整的線上旅遊購票流程。

本專案同時支援**行程搜尋、商品瀏覽、購物車、訂單、旅客資料、付款倒數、會員管理、商品管理及後台訂單管理**等功能。

---

## 🌐 線上展示

### 🚀 Render Demo

👉 **https://bichontravel-2.onrender.com/web/frontproducts**

可直接進入前台商品頁面瀏覽旅遊行程。

---

# 📌 專案特色

* 👤 訪客、會員、管理者三種角色權限
* 🔎 旅遊行程瀏覽與搜尋
* 🛒 會員購物車
* 🧾 訂單建立與訂單明細
* 👨‍👩‍👧 旅客資料管理
* 💳 模擬付款流程
* ⏱️ 30 秒付款倒數機制
* ✈️ 機位鎖定與釋放機制
* 📚 會員瀏覽紀錄
* 👤 會員資料管理
* 🛠️ 管理者商品 CRUD
* 👥 管理者會員管理
* 📋 管理者訂單管理
* 🧳 管理者旅客資料管理
* 💰 管理者款項管理
* 🗑️ 部分資料採用軟刪除機制

---

# 🛠️ 技術棧

| 分類           | 技術                          |      版本 | 用途                       |
| ------------ | --------------------------- | ------: | ------------------------ |
| 程式語言         | Java                        |      21 | 後端程式開發                   |
| 前端           | Thymeleaf                   |   3.1.5 | Server-side Template     |
| 前端           | HTML5                       |       - | 網頁結構                     |
| 前端           | CSS3                        |       - | 網頁樣式                     |
| 前端           | JavaScript                  |       - | 前端互動與倒數計時                |
| 後端           | Spring Boot                 |   4.1.0 | Java Web Framework       |
| ORM          | Spring Data JPA / Hibernate |       - | 資料庫操作與 ORM               |
| Web Server   | Tomcat                      |    10.1 | 內建 Web Server            |
| Java Library | Lombok                      |   3.1.0 | 減少 Java Boilerplate Code |
| Database     | MySQL                       |  8.0.47 | 關聯式資料庫                   |
| API          | RESTful API                 |       - | API 開發                   |
| API 文件       | Swagger                     |   3.1.0 | API 文件與測試                |
| API 測試       | Postman                     | 12.22.6 | API 測試                   |
| Build Tool   | Maven                       |       - | 專案建置與依賴管理                |
| IDE          | Eclipse                     | 2026-06 | 開發工具                     |
| Container    | Docker / Docker Compose     |       - | 本地環境部署                   |

以上技術棧與版本依專案簡報內容整理。

---

# 👥 使用者角色

本系統主要分為三種角色：

## 👑 Admin 管理者

管理者登入後可以進入管理者後台。

主要功能：

* 會員管理
* 商品／行程管理
* 訂單管理
* 訂單明細管理
* 旅客管理
* 款項管理

管理者同時具備一般會員的相關功能。

---

## 👤 Member 會員

會員登入後可以使用完整的購物流程：

* 瀏覽旅遊行程
* 搜尋商品
* 加入購物車
* 立即購買
* 建立訂單
* 填寫旅客資料
* 前往付款
* 查看我的訂單
* 查看訂單明細
* 查看瀏覽紀錄
* 修改個人資料
* 登出

---

## 👀 Visitor 訪客

訪客不需要註冊或登入即可：

* 瀏覽旅遊行程
* 搜尋商品
* 查看行程詳細資訊

若要進行購物、建立訂單及付款，則需要登入會員。

---

# 🛒 會員購物流程

系統主要購物流程如下：

```text
商品首頁
   ↓
瀏覽／搜尋旅遊行程
   ↓
查看行程詳情
   ↓
加入購物車／立即購買
   ↓
填寫購買人數
   ↓
建立訂單
   ↓
確認訂單明細
   ↓
填寫旅客資料
   ↓
前往付款
   ↓
30 秒付款倒數
   ↓
付款成功
   ↓
訂單成立
```

---

# ✈️ 行程購買

會員購買行程時，需要填寫：

* 成人雙人房人數
* 成人單人房人數
* 嬰兒人數

其中每一種人數都會佔用一個席位。

系統會檢查：

* 報名總人數必須大於 0
* 輸入數量不可小於 0
* 報名人數不可超過目前可售票數

若輸入不符合規則，系統會顯示提示訊息。

---

# 🧾 訂單與訂單明細

建立訂單後，系統會產生對應的訂單明細。

訂單明細包含：

* 成人雙人房人數
* 成人單人房人數
* 嬰兒人數
* 成人雙人房單價
* 成人單人房單價
* 嬰兒單價
* 訂單總金額

其中商品購買當下的票價會被記錄為**價格快照（Price Snapshot）**，避免商品價格後續修改而影響已建立的訂單金額。

---

# 🧳 旅客資料

會員建立訂單後，需要填寫每位旅客的基本資料。

包含：

* 護照號碼
* 國內聯絡手機
* 國外聯絡 Line 帳號
* 特殊需求

會員可以：

* 新增旅客
* 查看旅客資料
* 編輯旅客資料
* 刪除旅客資料

完成旅客資料後即可進入付款流程。

---

# 💳 付款機制

本專案採用**模擬付款**，不串接實際金流。

會員可以選擇：

* CASH
* CREDIT_CARD
* ATM
* MOBILE_PAY

付款頁面設定 **30 秒付款期限**。

```text
進入付款頁面
      ↓
開始 30 秒倒數
      ↓
 ┌────┴────┐
 ↓         ↓
完成付款    超過時間
 ↓         ↓
付款成功    付款逾期
 ↓         ↓
訂單成立    訂單失敗／釋放
```

付款倒數不會因為重新整理網頁而重新計時。

---

# 🛍️ 會員專區

會員登入後可以進入會員專區。

主要功能：

### 我的訂單

查看會員過去建立的訂單及相關訂單資訊。

### 我的購物車

管理自己加入購物車的旅遊行程。

### 瀏覽紀錄

系統會記錄會員在商品首頁查看過的旅遊行程。

如果會員重複查看相同行程，系統只保留該商品最新的瀏覽紀錄，不會重複建立資料。

會員可以從瀏覽紀錄直接：

* 立即購買
* 加入購物車

### 基本資料

會員可以修改自己的基本資料。

### 安全登出

會員可以登出目前帳號。

---

# 🛠️ 管理者後台

管理者登入成功後，可以進入後台管理平台。

後台主要包含：

```text
管理者後台
├── 商品管理
├── 會員管理
├── 訂單管理
├── 訂單明細管理
├── 旅客管理
├── 款項管理
```

---

# 📦 商品／行程管理

管理者可以：

* 查看所有行程
* 建立新行程
* 編輯行程
* 查看行程詳細資料
* 管理行程票價
* 管理可售票數
* 管理目的地
* 管理行程描述

建立或修改行程時，票價及席位數量若小於或等於 0，系統會進行輸入驗證。

---

# 👥 會員管理

管理者可以查看會員資料，包括：

* 姓名
* Email
* 角色
* 電話
* 帳號狀態

並提供：

* 建立會員
* 查看會員
* 編輯會員
* 刪除會員

會員刪除採用**軟刪除（Soft Delete）**。

會員被刪除後，不會直接從資料庫移除，而是將帳號狀態變更為：

```text
disabled
```

被停用的會員若要重新使用會員功能，需要重新註冊新的 Email。

---

# 📋 訂單管理

管理者可以：

* 查看所有訂單
* 建立訂單
* 查看訂單詳情
* 查看訂單明細
* 查看訂單旅客
* 刪除訂單

訂單刪除採用軟刪除方式。

訂單詳情會包含：

* 訂單總金額
* 訂單狀態
* 預約人數
* 付款期限
* 建立時間
* 更新時間

---

# 🧳 後台旅客管理

管理者可以查看所有訂單中的旅客資料。

包含：

* 護照號碼
* 手機號碼
* Line 帳號
* 特殊需求

管理者可以：

* 查看旅客詳情
* 編輯旅客資料
* 刪除旅客資料

---

# 💰 款項管理

管理者可以管理系統中的付款資料。

主要功能：

* 查看款項列表
* 查看款項詳細資料
* 編輯款項
* 修改付款方式
* 修改付款狀態
* 修改付款金額

款項詳細資料包含：

* 款項總金額
* 款項狀態
* 付款倒數起算時間
* 使用者付款時間

---

# 🗄️ 資料庫

本專案使用：

```text
MySQL 8.0.47
```

Database Schema：

```text
bichontravel_db
```

主要資料內容包含：

```text
Users
Browse_history
Cart_items
Products
Image
Orders
Order_item
Order_travelers
Payments
```

資料庫負責管理：

* 使用者
* 旅遊商品
* 商品圖片
* 購物車
* 瀏覽紀錄
* 訂單
* 訂單明細
* 旅客
* 付款資料

---

# 🐳 Docker 本地執行

本專案提供 Docker Compose 方式建立本地執行環境。

## 1. 建立 Docker Image

在專案根目錄執行：

```bash
docker compose build --no-cache
```

---

## 2. 啟動服務

```bash
docker compose up
```

啟動完成後，可以透過瀏覽器進入：

```text
http://localhost:8080/web/frontproducts
```

---

## 3. 關閉服務

停止目前 Docker Compose 服務：

```bash
docker compose down
```

---

## 🔄 完整 Docker 操作流程

如果需要重新建立 Image，可以依序執行：

```bash
docker compose down

docker compose build --no-cache

docker compose up
```

---

# 🌐 線上版本

本專案已部署至 Render：

**Bichon Travel**

https://bichontravel-2.onrender.com/web/frontproducts

---

# 📁 專案架構

```text
BichonTravel
│
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com.example.demo
│   │   │       ├── config
│   │   │       ├── controller
│   │   │       ├── model
│   │   │       ├── repository
│   │   │       └── service
│   │   │
│   │   └── resources
│   │       ├── static
│   │       │   ├── css
│   │       │   ├── js
│   │       │   └── images
│   │       │
│   │       ├── templates
│   │       │   ├── member
│   │       │   ├── admin
│   │       │   ├── payments
│   │       │   └── ...
│   │       │
│   │       └── application.properties
│   │
│   └── test
│
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── README.md
```

---

# 🔐 權限概念

| 功能      | Visitor | Member | Admin |
| ------- | :-----: | :----: | :---: |
| 瀏覽商品    |    ✅    |    ✅   |   ✅   |
| 搜尋商品    |    ✅    |    ✅   |   ✅   |
| 查看商品詳情  |    ✅    |    ✅   |   ✅   |
| 加入購物車   |    ❌    |    ✅   |   ✅   |
| 建立訂單    |    ❌    |    ✅   |   ✅   |
| 查看自己的訂單 |    ❌    |    ✅   |   ✅   |
| 管理自己的資料 |    ❌    |    ✅   |   ✅   |
| 瀏覽紀錄    |    ❌    |    ✅   |   ✅   |
| 商品管理    |    ❌    |    ❌   |   ✅   |
| 會員管理    |    ❌    |    ❌   |   ✅   |
| 訂單管理    |    ❌    |    ❌   |   ✅   |
| 訂單明細管理  |    ❌    |    ❌   |   ✅   |
| 旅客管理    |    ❌    |    ❌   |   ✅   |
| 款項管理    |    ❌    |    ❌   |   ✅   |
| 營收數據    |    ❌    |    ❌   |   ✅   |

---

# 📌 專案資訊

**Project:** Bichon Travel
**Type:** Online Travel Booking System
**Language:** Java 21
**Framework:** Spring Boot 4.1.0
**Frontend:** Thymeleaf / HTML5 / CSS3 / JavaScript
**Database:** MySQL 8.0.47
**Build Tool:** Maven
**Container:** Docker / Docker Compose
**Deployment:** Render

---

# 👨‍💻 Developer

**Liu Yi-Hsuan**

2026.09

---

## 🎯 Project Goal

透過本專案實作一個完整的線上旅遊購票平台，將：

**商品瀏覽 → 購物 → 訂單 → 旅客資料 → 付款 → 後台管理**

串接成完整的電子商務流程，同時實作不同使用者角色與權限管理，並透過 Docker 與 Render 完成專案部署。

---

# 🐶 Bichon Travel

> **Online Travel Booking System**

Bichon Travel is an online travel booking platform developed with **Java, Spring Boot, Thymeleaf, and MySQL**.

The system provides three user roles — **Visitor, Member, and Administrator** — with different permissions and workflows.

The project implements a complete travel booking process, including travel package browsing, shopping cart management, order creation, traveler information, simulated payment, payment countdown, and administrator management.

The concept of Bichon Travel is inspired by the joy and healing energy of Bichon Frise dogs, combining a friendly travel experience with a simple and intuitive online booking system.

---

## 🌐 Live Demo

### 🚀 Render Deployment

**Bichon Travel Online Demo**

[https://bichontravel-2.onrender.com/web/frontproducts](https://bichontravel-2.onrender.com/web/frontproducts?utm_source=chatgpt.com)

---

# ✨ Features

* 👤 Three user roles: Visitor, Member, and Administrator
* 🔎 Travel package browsing and searching
* 🛒 Shopping cart
* 🛍️ Buy Now function
* 🧾 Order creation and management
* 📋 Order detail management
* 🧳 Traveler information management
* 💳 Simulated payment process
* ⏱️ 30-second payment countdown
* ✈️ Seat / ticket availability management
* 📚 Browsing history
* 👤 Member profile management
* 🛠️ Administrator travel package management
* 👥 Administrator member management
* 📋 Administrator order management
* 🧳 Administrator traveler management
* 💰 Administrator payment management
* 🗑️ Soft-delete mechanism for selected data

---

# 🛠️ Tech Stack

| Category             | Technology                  | Version | Purpose                                 |
| -------------------- | --------------------------- | ------: | --------------------------------------- |
| Programming Language | Java                        |      21 | Backend development                     |
| Frontend             | Thymeleaf                   |   3.1.5 | Server-side HTML rendering              |
| Frontend             | HTML5                       |       - | Web page structure                      |
| Frontend             | CSS3                        |       - | Web page styling                        |
| Frontend             | JavaScript                  |       - | Frontend interactions and countdown     |
| Backend              | Spring Boot                 |   4.1.0 | Java web application framework          |
| ORM                  | Spring Data JPA / Hibernate |       - | Database access and ORM                 |
| Web Server           | Tomcat                      |    10.1 | Embedded web server                     |
| Java Library         | Lombok                      |   3.1.0 | Reduce boilerplate code                 |
| Database             | MySQL                       |  8.0.47 | Relational database                     |
| API                  | RESTful API                 |       - | API development                         |
| API Documentation    | Swagger                     |   3.1.0 | API documentation and testing           |
| API Testing          | Postman                     | 12.22.6 | API testing                             |
| Build Tool           | Maven                       |       - | Project build and dependency management |
| IDE                  | Eclipse                     | 2026-06 | Development environment                 |
| Containerization     | Docker / Docker Compose     |       - | Local containerized environment         |
| Deployment           | Render                      |       - | Cloud deployment                        |

The technology stack and versions are based on the project documentation.

---

# 👥 User Roles

The system is divided into three main roles:

## 👀 Visitor

Visitors can browse the website without registering or logging in.

Available functions include:

* Browse travel packages
* Search travel packages
* View travel package details

Visitors must log in as a member before they can proceed with purchasing and checkout.

---

## 👤 Member

Members can access the complete travel booking workflow.

Main functions:

* Browse travel packages
* Search travel packages
* View travel package details
* Add products to the shopping cart
* Buy Now
* Create orders
* View order details
* Add traveler information
* Proceed to payment
* View personal orders
* Manage shopping cart
* View browsing history
* Edit personal information
* Log out

---

## 👑 Administrator

Administrators can access the backend management system.

Main functions:

* Travel package management
* Member management
* Order management
* Order detail management
* Traveler management
* Payment management

The administrator also has access to general member functions.

---

# 🛒 Member Booking Flow

The main booking workflow is:

```text
Travel Package Homepage
          ↓
Browse / Search Travel Packages
          ↓
View Travel Package Details
          ↓
Add to Cart / Buy Now
          ↓
Enter Number of Travelers
          ↓
Create Order
          ↓
Review Order Details
          ↓
Enter Traveler Information
          ↓
Proceed to Payment
          ↓
30-Second Payment Countdown
          ↓
     ┌────┴────┐
     ↓         ↓
Payment       Timeout
Success        ↓
     ↓       Payment Expired
Order         ↓
Confirmed    Order Failed
```

---

# ✈️ Travel Package Purchase

When purchasing a travel package, members need to enter:

* Number of adults in double rooms
* Number of adults in single rooms
* Number of infants

Each traveler occupies one available seat/ticket.

The system validates the following conditions:

* Total number of travelers must be greater than 0
* Number of travelers cannot be negative
* Total number of travelers cannot exceed the available capacity

Invalid input will trigger a system validation message.

---

# 🧾 Orders & Order Details

After confirming the purchase, the system creates an order and its corresponding order details.

Order details include:

* Number of adults in double rooms
* Number of adults in single rooms
* Number of infants
* Double-room adult price
* Single-room adult price
* Infant price
* Total order amount

The prices at the time of checkout are stored as a **price snapshot** in the order details.

This allows the system to preserve the original purchase price even if the travel package price is changed later.

---

# 🧳 Traveler Information

After creating an order, members need to provide traveler information.

The system supports information such as:

* Passport number
* Domestic contact phone number
* International contact Line account
* Special requirements

Members can:

* Add travelers
* View traveler details
* Edit traveler information
* Delete traveler information

After completing the traveler information, members can proceed to payment.

---

# 💳 Payment System

This project uses a **simulated payment process** and does not connect to a real payment gateway.

Supported payment methods include:

* CASH
* CREDIT_CARD
* ATM
* MOBILE_PAY

The payment page uses a **30-second countdown timer**.

The countdown does not restart when the page is refreshed.

If the member does not complete the payment within the specified time, the payment expires and the booking process is terminated.

---

# 👤 Member Center

After logging in, members can access their personal member center.

## My Orders

Members can view their own orders and related order information.

## My Shopping Cart

Members can manage travel packages they are interested in.

## Browsing History

The system records travel packages viewed by members.

If a member views the same travel package multiple times, the system keeps the latest browsing record instead of creating duplicate records.

Members can directly:

* Buy Now
* Add to Cart

from their browsing history.

## Account Settings

Members can edit their personal information.

## Logout

Members can securely log out of their account.

---

# 🛠️ Administrator Backend

The administrator backend provides management functions for the travel booking system.

```text
Administrator Backend
│
├── Travel Package Management
├── Member Management
├── Order Management
├── Order Detail Management
├── Traveler Management
└── Payment Management
```

---

# 📦 Travel Package Management

Administrators can:

* View all travel packages
* Create new travel packages
* Edit travel package information
* View travel package details
* Manage prices
* Manage available capacity
* Manage destinations
* Manage travel package descriptions

The system also validates price and capacity fields when creating or editing travel packages.

---

# 👥 Member Management

Administrators can manage member accounts and view information such as:

* Name
* Email
* Role
* Phone number
* Account status

Available operations include:

* Create members
* View members
* Edit members
* Delete members

The system uses **soft delete** for member deletion.

Instead of physically removing a member from the database, the account status is changed to:

```text
disabled
```

A disabled account can no longer be used as an active member account.

---

# 📋 Order Management

Administrators can:

* View all orders
* Create orders
* View order details
* View order items
* View travelers associated with an order
* Soft-delete orders

Order details include:

* Total order amount
* Order status
* Number of travelers
* Payment deadline
* Created time
* Updated time

---

# 🧳 Traveler Management

Administrators can view traveler information associated with all orders.

Traveler information includes:

* Passport number
* Phone number
* Line account
* Special requirements

Administrators can:

* View traveler details
* Edit traveler information
* Delete traveler information

---

# 💰 Payment Management

Administrators can manage payment records in the backend.

Available functions include:

* View payment records
* View payment details
* Edit payment records
* Modify payment method
* Modify payment status
* Modify payment amount

Payment details include:

* Total payment amount
* Payment status
* Payment countdown start time
* Payment completion time

---

# 🗄️ Database

The project uses:

```text
MySQL 8.0.47
```

Database schema:

```text
bichontravel_db
```

Main database entities include:

```text
Users
Browse_history
Cart_items
Products
Image
Orders
Order_item
Order_travelers
Payments
```

These tables are used to manage:

* Users
* Travel packages
* Product images
* Shopping cart items
* Browsing history
* Orders
* Order details
* Travelers
* Payments

---

# 🐳 Running Locally with Docker

The project supports running the application using **Docker Compose**.

Make sure Docker Desktop is installed and running before starting the application.

## 1. Build the Docker Image

Open a terminal in the project root directory and run:

```bash
docker compose build --no-cache
```

---

## 2. Start the Application

```bash
docker compose up
```

After the containers have started successfully, open:

```text
http://localhost:8080/web/frontproducts
```

in your browser.

---

## 3. Stop the Application

To stop and remove the Docker Compose containers:

```bash
docker compose down
```

---

## 🔄 Complete Docker Workflow

If you want to rebuild the project from scratch:

```bash
docker compose down

docker compose build --no-cache

docker compose up
```

---

# 📁 Project Structure

```text
BichonTravel
│
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com.example.demo
│   │   │       ├── config
│   │   │       ├── controller
│   │   │       ├── model
│   │   │       ├── repository
│   │   │       └── service
│   │   │
│   │   └── resources
│   │       ├── static
│   │       │   ├── css
│   │       │   ├── js
│   │       │   └── images
│   │       │
│   │       ├── templates
│   │       │   ├── member
│   │       │   ├── admin
│   │       │   ├── payments
│   │       │   └── ...
│   │       │
│   │       └── application.properties
│   │
│   └── test
│
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── README.md
```

---

# 🔐 Permission Overview

| Function                    | Visitor | Member | Admin |
| --------------------------- | :-----: | :----: | :---: |
| Browse Travel Packages      |    ✅    |    ✅   |   ✅   |
| Search Travel Packages      |    ✅    |    ✅   |   ✅   |
| View Package Details        |    ✅    |    ✅   |   ✅   |
| Add to Cart                 |    ❌    |    ✅   |   ✅   |
| Buy Now                     |    ❌    |    ✅   |   ✅   |
| Create Orders               |    ❌    |    ✅   |   ✅   |
| View Personal Orders        |    ❌    |    ✅   |   ✅   |
| Manage Personal Information |    ❌    |    ✅   |   ✅   |
| Browsing History            |    ❌    |    ✅   |   ✅   |
| Travel Package Management   |    ❌    |    ❌   |   ✅   |
| Member Management           |    ❌    |    ❌   |   ✅   |
| Order Management            |    ❌    |    ❌   |   ✅   |
| Order Detail Management     |    ❌    |    ❌   |   ✅   |
| Traveler Management         |    ❌    |    ❌   |   ✅   |
| Payment Management          |    ❌    |    ❌   |   ✅   |

---

# 📌 Project Information

| Item             | Details                               |
| ---------------- | ------------------------------------- |
| Project          | Bichon Travel                         |
| Type             | Online Travel Booking System          |
| Language         | Java 21                               |
| Framework        | Spring Boot 4.1.0                     |
| Frontend         | Thymeleaf / HTML5 / CSS3 / JavaScript |
| Database         | MySQL 8.0.47                          |
| Build Tool       | Maven                                 |
| Containerization | Docker / Docker Compose               |
| Deployment       | Render                                |

---

# 👨‍💻 Developer

**劉懿萱**

2026

---

# 🎯 Project Goal

The goal of Bichon Travel is to develop a complete online travel booking platform and integrate the following processes into a single system:

```text
Travel Package Browsing
        ↓
Shopping
        ↓
Order Creation
        ↓
Traveler Information
        ↓
Payment
        ↓
Order Management
        ↓
Administrator Management
```

Through this project, I implemented a practical web application using **Java and Spring Boot**, while gaining experience in database design, MVC architecture, RESTful APIs, Thymeleaf frontend development, authentication and authorization, Docker containerization, and cloud deployment.

---

# 🐶 Bichon Travel

> **Travel with happiness.**
> **Explore the world with Bichon Travel. 🐾**


# 🐶 Bichon Travel

> **Travel with happiness.
> Explore the world with Bichon Travel. 🐾**
