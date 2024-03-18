package com.zero.hello_android_app.LogicBTN;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
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

public class ChildActivity extends AppCompatActivity {
    private EditText editTextProductName, editTextProductImage, editTextProductPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add_product);

        editTextProductName = findViewById(R.id.editTextProductName);
        editTextProductImage = findViewById(R.id.buttonSelectImage);
        editTextProductPrice = findViewById(R.id.editTextProductPrice);

        Button btnSaveProduct = findViewById(R.id.buttonSaveProduct);
        btnSaveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveProduct();
            }
        });

        Button btnExit = findViewById(R.id.buttonExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
    }

    private void saveProduct() {
        String productName = editTextProductName.getText().toString();
        double productPrice = Double.parseDouble(editTextProductPrice.getText().toString());
        String productImage = editTextProductImage.getText().toString();


        // Tạo đối tượng Product mới
        Product newProduct = new Product(0, productName, productPrice, productImage);

        // Gửi yêu cầu cập nhật sản phẩm tới máy chủ
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/HelloWebApp/rest/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ProcductService productService = retrofit.create(ProcductService.class);
        Call<Void> call = productService.addProduct(newProduct);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("ChildActivity", "Response received");
                if (response.isSuccessful()) {
                    // Cập nhật thành công
                    Toast.makeText(ChildActivity.this, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
                    setResult(Activity.RESULT_OK); // Set result as RESULT_OK
                    finish();

                } else {
                    // Xử lý khi cập nhật không thành công
                    Toast.makeText(ChildActivity.this, "Không thể Thêm sản phẩm", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Xử lý khi gặp lỗi kết nối
                Toast.makeText(ChildActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}
