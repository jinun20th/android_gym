package com.example.thanh.activity;

import static com.example.thanh.retrofit.RetrofitClient.getRetrofitInstance;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.thanh.R;
import com.example.thanh.adapter.PostItemAdapter;
import com.example.thanh.model.LikeRequest;
import com.example.thanh.model.Post;
import com.example.thanh.retrofit.ApiService;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class post_user_gets extends NavActivity implements PostItemAdapter.OnCommentClickListener, PostItemAdapter.OnLikeClickListener {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.post;
    }

    @Override
    protected int getCheckedNavigationItemId() {
        return R.id.nav_home;
    }

    private ListView feedsListView;
//    private ArrayAdapter<String> feedsAdapter;
    private PostItemAdapter adapter;
    private List<Post> PostList;
    private ApiService apiService;
    private EditText postEditText;
    private Button btnPost;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        feedsListView = findViewById(R.id.feedsListView);
        postEditText = findViewById(R.id.postEditText);
        btnPost = findViewById(R.id.btnPost);

        // Khởi tạo Retrofit
        Retrofit retrofit = getRetrofitInstance();

        // Khởi tạo ApiService
        apiService = retrofit.create(ApiService.class);

        getAllPost(apiService);
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post(apiService, postEditText.getText().toString());
            }
        });

//        PostList = new ArrayList<>();
////        PostItem postItem = new PostItem("nghia", "nghi", "232");
//        PostList.add(new Post("bong hoa"));
//// Thêm các PostItem vào danh sách postItems
//
//        adapter = new PostItemAdapter(this, PostList);
//        feedsListView.setAdapter(adapter);
    }
    private void getAllPost(ApiService apiService) {
        Call<List<Post>> call = apiService.getPostList();
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful()) {
                    PostList = response.body();
                    Log.d("bug", String.valueOf(new Gson().toJson(PostList)));
//                    // Tạo adapter và gán cho ListView
                    adapter = new PostItemAdapter(post_user_gets.this, PostList);
                    adapter.setOnCommentClickListener(post_user_gets.this); // Đăng ký listener
                    adapter.setOnLikeClickListener(post_user_gets.this); // Đăng ký listener
                    feedsListView.setAdapter(adapter);
                } else {
                    Toast.makeText(post_user_gets.this, "Failed to get post", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Toast.makeText(post_user_gets.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    private void post(ApiService apiService, String postt) {

        // Tạo object Message

        Post post = new Post();
        post.setOwnerId(1);
        post.setCaption(postt);
        post.setCreatedDate(1);

        // Gửi yêu cầu POST
        Call<Post> call = apiService.postPost(post);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.isSuccessful()) {
                    // Xử lý thành công
                    Post post = response.body();
//                    Log.d("bug", String.valueOf(new Gson().toJson(postedFoodUser)));
                    getAllPost(apiService);
                    Toast.makeText(post_user_gets.this, "Post successfully", Toast.LENGTH_SHORT).show();
//                    }
                } else {
                    // Xử lý lỗi khi gửi yêu cầu POST
                    Toast.makeText(post_user_gets.this, "Failed to post", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                // Xử lý lỗi kết nối hoặc lỗi trong quá trình gửi yêu cầu POST
                Toast.makeText(post_user_gets.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onCommentClick(int postId, int userId) {
        // Xử lý sự kiện khi người dùng nhấp vào một cuộc trò chuyện
        Intent intent = new Intent(post_user_gets.this, CommentActivity.class);
//         Khởi chạy Intent
        intent.putExtra("post_id", postId);
        intent.putExtra("user_id", userId);
        startActivity(intent);
    }

    @Override
    public void onLikeClick(int postId, int userId) {
        LikeRequest likeRequest = new LikeRequest(postId, userId);

        // Gửi POST request
        Call<Void> call = apiService.likePost(likeRequest);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Xử lý khi request thành công
//                    adapter.setNotifyOnChange(true);
                    getAllPost(apiService);
                } else {
                    // Xử lý khi request thất bại
                    Toast.makeText(post_user_gets.this, "Failed to like post", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Xử lý khi request gặp lỗi
                Toast.makeText(post_user_gets.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }
}
