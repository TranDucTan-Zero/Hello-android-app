package com.zero.hello_android_app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.zero.hello_android_app.Adapter.ProductAdapter;
import com.zero.hello_android_app.LogicBTN.ChildActivity;
import com.zero.hello_android_app.LogicBTN.EditActivity;
import com.zero.hello_android_app.enity.Product;
import com.zero.hello_android_app.services.ProcductService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_EDIT_PRODUCT = 1;
    private static final int REQUEST_CODE_ADD_PRODUCT = 1;
    private ListView productListView;
    private Button btn_add;
    private Button btn_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        productListView = findViewById(R.id.productlist);

        btn_add = findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mở màn hình thêm sản phẩm khi nhấn nút "Thêm"
                Intent myIntent = new Intent(MainActivity.this, ChildActivity.class);
                startActivityForResult(myIntent, REQUEST_CODE_ADD_PRODUCT);
            }
        });


        btn_edit = findViewById(R.id.btn_edit);
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang trang sửa sản phẩm khi nhấn nút "Sửa"
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                startActivityForResult(intent, REQUEST_CODE_EDIT_PRODUCT);
            }
        });

        fetchProductList();
    }


    // Trong MainActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_EDIT_PRODUCT && resultCode == Activity.RESULT_OK) {
            // Nếu kết quả trả về từ EditActivity là thành công,
            // cập nhật lại danh sách sản phẩm
            Log.d("MainActivity", "Fetching product list...");
            fetchProductList(); // Cập nhật danh sách sản phẩm sau khi sửa sản phẩm
        }
        if (requestCode == REQUEST_CODE_ADD_PRODUCT && resultCode == Activity.RESULT_OK) {
            // Nếu kết quả trả về từ ChildActivity là thành công,
            // cập nhật lại danh sách sản phẩm
            Log.d("MainActivity", "Fetching product list...");
            fetchProductList(); // Cập nhật danh sách sản phẩm sau khi thêm sản phẩm
        }
    }



    private void fetchProductList() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/HelloWebApp/rest/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ProcductService productService = retrofit.create(ProcductService.class);
        Call<List<Product>> call = productService.getProductFromRestAPI();
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    List<Product> productList = response.body();
                    showProductList(productList);
                } else {
                    // Xử lý khi không thành công
                    Toast.makeText(MainActivity.this, "Không thể tải danh sách sản phẩm", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                // Xử lý khi gặp lỗi
                Toast.makeText(MainActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void showProductList(List<Product> productList) {
        ProductAdapter adapter = new ProductAdapter(MainActivity.this, R.layout.list_item_product, productList);
        productListView.setAdapter(adapter);

        // Xóa sản phẩm khi nhấn nút Xóa
        adapter.setOnDeleteButtonClickListener(new ProductAdapter.OnDeleteButtonClickListener() {
            @Override
            public void onDeleteButtonClick(int position) {
                deleteProduct(position);
            }
        });
    }

    private void deleteProduct(int position) {
        Product productToDelete = getProductAtPosition(position);
        if (productToDelete != null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:8080/HelloWebApp/rest/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ProcductService productService = retrofit.create(ProcductService.class);
            Call<Void> call = productService.deleteProduct(productToDelete.getId());
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Đã xóa sản phẩm", Toast.LENGTH_SHORT).show();
                        fetchProductList(); // Cập nhật lại danh sách sản phẩm sau khi xóa
                    } else {
                        Toast.makeText(MainActivity.this, "Không thể xóa sản phẩm", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private Product getProductAtPosition(int position) {
        ProductAdapter adapter = (ProductAdapter) productListView.getAdapter();
        if (adapter != null && adapter.getCount() > position) {
            return adapter.getItem(position);
        }
        return null;
    }
}
