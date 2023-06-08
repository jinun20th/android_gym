package com.example.thanh.activity;

import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.thanh.R;
import com.example.thanh.model.CourseSchedule;
import com.example.thanh.retrofit.ApiService;
import com.example.thanh.retrofit.RetrofitClient;
import com.google.gson.Gson;

import java.sql.Date;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class course_user_calendar extends NavActivity {

    private ApiService apiService;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.course_user_calendar;
    }

    @Override
    protected int getCheckedNavigationItemId() {
        return R.id.nav_yourcourses;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int courseId = getIntent().getIntExtra("_id", -1);

        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        Call<CourseSchedule> call = apiService.getCourseSchedule(courseId);
        call.enqueue(new Callback<CourseSchedule>() {
            @Override
            public void onResponse(retrofit2.Call<CourseSchedule> call, Response<CourseSchedule> response) {
                if (response.isSuccessful()) {
                    CourseSchedule course = response.body();
                    String jsonString = new Gson().toJson(course);
                    Log.d("RES Schedule", jsonString);
                    Log.d("API schedule", "Success");
                    displaySchedule(course);
                } else {
                    Log.d("API schedule", "Fail");
                }
            }

            @Override
            public void onFailure(Call<CourseSchedule> call, Throwable t) {
                String errorMessage = t.getMessage();
                Log.d("Error: ", errorMessage);
            }
        });
    }

    private void displaySchedule(CourseSchedule courseSchedule) {
        CalendarView calendarView = findViewById(R.id.calendarView);
        TextView dayTitle = findViewById(R.id.dayTitle);
        TextView dayNote = findViewById(R.id.dayNote);
        TextView dayNot = findViewById(R.id.dayNot);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                dayTitle.setVisibility(View.GONE);
                dayNote.setVisibility(View.GONE);
                dayNot.setVisibility(View.VISIBLE);
                // Lấy giá trị ngày được chọn
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);

                long from = courseSchedule.getFromDateTime();
                long to = courseSchedule.getToDateTime();
                Date dateFrom = new Date(from * 1000);
                Date dateTo = new Date(to * 1000);

                // Tạo định dạng cho đối tượng Date
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

                // Chuyển đổi đối tượng Date thành chuỗi ngày tháng
                String fromDate = dateFormat.format(dateFrom);
                String toDate = dateFormat.format(dateTo);
//                Log.d("from", fromDate);
//                Log.d("to", toDate);

                // Đặt giá trị giờ, phút và giây thành tối đa
                selectedDate.set(Calendar.HOUR_OF_DAY, 23);
                selectedDate.set(Calendar.MINUTE, 59);
                selectedDate.set(Calendar.SECOND, 59);
                selectedDate.set(Calendar.MILLISECOND, 999);

                // Chuyển đổi ngày sang định dạng long
                long selectedDateTime = selectedDate.getTimeInMillis();
                Date dateNow = new Date(selectedDateTime);
                String nowDate = dateFormat.format(dateNow);
//                Log.d("now", nowDate);

                // So sánh với từng CourseSchedule và hiển thị ghi chú tương ứng
                if (selectedDateTime >= courseSchedule.getFromDateTime() * 1000 && selectedDateTime <= courseSchedule.getToDateTime() * 1000) {
                    String note = courseSchedule.getNote();
                    // Hiển thị ghi chú
                    Log.d("Show", "Success");
                    dayNot.setVisibility(View.GONE);
                    dayNote.setVisibility(View.VISIBLE);
                    dayNote.setText(note);
                }
            }
        });
    }
}