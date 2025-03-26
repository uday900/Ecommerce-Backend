# DarlaMart - E-commerce Platform

## Deployment Link
[Live Demo](https://darlamart.netlify.app/)

## Overview
DarlaMart is a full-stack e-commerce platform built with React, Tailwind CSS, Java Spring Boot, and PostgreSQL. The platform provides both admin and user functionalities, including product management, order handling, authentication, and authorization.

## Tech Stack

**Frontend:** React, Tailwind CSS, Axios, React Hook Form, Context API, Toaster, React Router DOM  
**Backend:** Java, Spring Boot, Hibernate, REST API, Spring Security (JWT), Lombok  
**Database:** PostgreSQL  
**Tools:** Visual Studio Code, Spring Tool Suite (STS), Postman  

## Features
### **Admin Side**
- Create, update, and delete products
- CRUD operations on categories
- Add and remove carousel images
- Update user order statuses
- Check user logs

### **User Side**
- Registration and login
- Browse and filter products
- Add products to cart
- Place an order
- Checkout process
- Review products

### **Authentication & Authorization**
- Provided admin and user roles
- JWT token expiration set to 24 hours from login
- Restricted user access to certain endpoints

## API Endpoints

### **Authentication Endpoints**
- `POST /auth/register` - Register a new user
- `POST /auth/login` - Authenticate a user
- `POST /auth/forgot-password?email=${email}` - Initiate password reset process
- `POST /auth/verify-otp?otp=${otp}&email=${email}` - Verify OTP for password reset
- `POST /auth/reset-password?newPassword=${new_password}&token=${token}&email=${email}` - Reset password with token
- `POST /auth/verify-account?token=${token}` - Verify account with token

### **Category Endpoints**
- `GET /category/fetch` - Fetch all categories
- `GET /category/fetch/${category_id}` – Fetch category by ID
- `POST /category/add` - Add a category
- `PUT /category/update/${category_id}` - Update a category by ID
- `DELETE /category/delete/${category_id}` - Delete a category by ID

### **Product Endpoints**
- `GET /products/fetch` - Fetch all products
- `GET /products/fetch/${product_id}` - Fetch a product by ID
- `PUT /products/update/${product_id}` - Update a product by ID
- `DELETE /products/delete/${product_id}` - Delete a product by ID
- `POST /products/add` - Add a product
- `GET /products/fetch/category?categoryName=${category_name}` - Fetch products by category name
- `GET /products/fetch/search?searchValue=${search_value}` - Fetch products by search value

### **Carousel Endpoints**
- `GET /carousels/fetch` - Fetch all carousel images
- `POST /carousels/add` - Add a carousel image
- `DELETE /carousels/delete/${image_id}` - Delete a carousel image

### **User Endpoints**
- `GET /users/fetch` - Fetch all users
- `GET /users/fetch/${user_id}` - Fetch a user by ID

### **Order Endpoints**
- `GET /orders/fetch` - Fetch all orders
- `PUT /orders/update-status/${order_id}?status=${new_status}` - Update order status
- `GET /orders/fetch/user/${user_id}` - Fetch orders by user ID
- `POST /orders/place-order/${user_id}?productId=${product_id}&quantity=${quantity}` - Place an order
- `POST /orders/check-out/${user_id}` - Check out a cart for a user

### **Cart Endpoints**
- `GET /users/cart/fetch/${user_id}` - Fetch user's cart
- `POST /users/cart/add/${user_id}?productId=${product_id}` - Add product to cart
- `DELETE /users/cart/remove/${user_id}/${cart_item_id}` - Remove item from cart
- `GET /users/cart/count/${user_id}` - Fetch cart item count
- `POST /users/cart/increment/${user_id}?cartItemId=${cart_item_id}` - Increment cart item quantity
- `POST /users/cart/decrement/${user_id}?cartItemId=${cart_item_id}` - Decrement cart item quantity

### **Review Endpoints**
- `POST /product/reviews` - Add a review for a product
- `GET /product/reviews/${product_id}` - Fetch all reviews for a product
- `DELETE /product/reviews/${id}/${user_id}` - Delete a review by ID

## Backend Repository
[Backend GitHub Repository](https://github.com/uday900/Ecommerce-Backend.git)

## Frontend Repository
[Frontend GitHub Repository](https://github.com/uday900/Ecommerce-Frontend.git)

## Running the Project

### **Backend (Spring Boot)**
1. Clone the repository:
   ```bash
   git clone https://github.com/uday900/Ecommerce-Backend.git
   ```
2. Navigate to the project directory:
   ```bash
   cd Ecommerce-Backend
   ```
3. Set up environment variables (replace with actual values in your `.env` or `application.properties` file):
   ```properties
   database_url=${database_url}
   database_user=${database_user}
   database_password=${database_password}
   frontend_url=${frontend_url}
   admin_mail=${admin_mail}
   admin_mail_password=${admin_mail_password}
   ```
4. Build and run the Spring Boot application:
   ```bash
   ./mvnw spring-boot:run
   ```

### **Frontend (React)**
1. Clone the repository:
   ```bash
   git clone https://github.com/uday900/Ecommerce-Frontend.git
   ```
2. Navigate to the frontend directory:
   ```bash
   cd Ecommerce-Frontend
   ```
3. Install dependencies:
   ```bash
   npm install
   ```
4. Run the project:
   ```bash
   npm run dev
   ```

### **Frontend URL**
[Frontend Deployment](https://darla-mart.netlify.app/)

## Challenges Faced
- Implementing security features, where JWT generation and verification
- Configuring CORS in the backend to allow frontend requests
- Handling relational mappings efficiently in the database
- Image handling, storing in PostgreSQL, initially faced an error: "unable to access LOB stream"; resolved by maintaining transactions

