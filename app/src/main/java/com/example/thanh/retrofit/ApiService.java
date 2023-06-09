package com.example.thanh.retrofit;

import com.example.thanh.model.Conversation;
import com.example.thanh.model.Course;
import com.example.thanh.model.CourseDetail;
import com.example.thanh.model.CourseRegister;
import com.example.thanh.model.CourseScheduleDetail;
import com.example.thanh.model.CourseUser;
import com.example.thanh.model.FoodItem;
import com.example.thanh.model.FoodUser;
import com.example.thanh.model.LikeRequest;
import com.example.thanh.model.Message;
import com.example.thanh.model.Post;
import com.example.thanh.model.PostReaction;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("v1/courses")
    Call<List<Course>> getAllCourses();

    @GET("v1/courses/dtl/{id}")
    Call<CourseDetail> getCourseById(@Path("id") int id);

    @GET("v1/courseUser/byuser/{id}")
    Call<List<CourseUser>> getAllCoursesUser(@Path("id") int id);

    @POST("v1/courseUser")
    Call<CourseRegister> createCourseUser(@Body CourseRegister courseUser);

    @GET("v1/courseSchedule")
    Call<CourseScheduleDetail> getCourseSchedule(@Query("courseId") int id);

    @GET("v1/conversation/{id}")
    Call<List<Conversation>> getConversation(@Path("id") int id);

    @POST("v1/conversation")
    Call<Conversation> postConversation(@Body Conversation conversation);

    @GET("v1/message/{id}")
    Call<List<Message>> getNewApi(@Path("id") int conversationId);

    @POST("v1/message")
    Call<Message> postMessage(@Body Message message);

    @GET("v1/conversation/{id}")
    Call<List<Conversation>> searchConversation(@Path("id") int id, @Query("name") String name);

    @GET("v1/foodsUser/{id}")
    Call<List<FoodUser>> getFoodUserBySession(@Path("id") int id, @Query("session") String session);

    @GET("v1/foodsUser/{id}")
    Call<List<FoodUser>> getFoodUser(@Path("id") int id);

    @POST("v1/foodsUser")
    Call<FoodUser> postFoodUser(@Body FoodUser foodUser);

    @GET("v1/food")
    Call<List<FoodItem>> getFoodList();

    @POST("v1/food")
    Call<FoodItem> addNewFood(@Body FoodItem foodItem);

    @GET("v1/post")
    Call<List<Post>> getPostList();

    @POST("v1/post")
    Call<Post> postPost(@Body Post post);

    @GET("v1/postReact")
    Call<List<PostReaction>> getPostReaction(@Query("userId") int userId, @Query("postId") int postId);

    @POST("v1/postReact")
    Call<PostReaction> postPostReaction(@Body PostReaction postReaction);

    @POST("v1/postReact/like")
    Call<Void> likePost(@Body LikeRequest likeRequest);
}