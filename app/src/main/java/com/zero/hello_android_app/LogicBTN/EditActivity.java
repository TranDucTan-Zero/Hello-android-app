package com.zero.hello_android_app.LogicBTN;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.zero.hello_android_app.R;
import com.zero.hello_android_app.enity.Product;
import com.zero.hello_android_app.services.ProcductService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditActivity extends AppCompatActivity {
    private Button btnUpdateProduct;
    private EditText editTextProductID, editTextProductName, editTextProductImage, editTextProductPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_edit_product);

        // Ánh xạ các thành phần giao diện
        btnUpdateProduct = findViewById(R.id.buttonUpdateProduct);
        editTextProductID = findViewById(R.id.editTextProductID);
        editTextProductName = findViewById(R.id.editTextProductName);
        editTextProductImage = findViewById(R.id.editTextProductImage);
        editTextProductPrice = findViewById(R.id.editTextProductPrice);

        btnUpdateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Gọi phương thức updateProduct khi nhấn nút cập nhật sản phẩm
                updateProduct();
            }
        });

        Button btnExit = findViewById(R.id.buttonExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Đóng EditActivity khi nhấn nút Thoát
                finish();
            }
        });
    }

    // Trong EditActivity
    private void updateProduct() {
        // Lấy thông tin mới của sản phẩm từ các EditText
        int productId = Integer.parseInt(editTextProductID.getText().toString());
        String productName = editTextProductName.getText().toString();
        double productPrice = Double.parseDouble(editTextProductPrice.getText().toString());
        String productImage = editTextProductImage.getText().toString();

        // Tạo đối tượng Product mới
        Product product = new Product(productId, productName, productPrice, productImage);

        // Gửi yêu cầu cập nhật sản phẩm tới máy chủ
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/HelloWebApp/rest/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ProcductService productService = retrofit.create(ProcductService.class);
        Call<Void> call = productService.updateProduct(productId, product);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Cập nhật thành công
                    Toast.makeText(EditActivity.this, "Cập nhật sản phẩm thành công", Toast.LENGTH_SHORT).show();
                    setResult(Activity.RESULT_OK); // Đặt kết quả là thành công
                    finish(); // Kết thúc EditActivity sau khi cập nhật thành công
                } else {
                    // Xử lý khi cập nhật không thành công
                    Toast.makeText(EditActivity.this, "Không thể cập nhật sản phẩm", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Xử lý khi gặp lỗi kết nối
                Toast.makeText(EditActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
