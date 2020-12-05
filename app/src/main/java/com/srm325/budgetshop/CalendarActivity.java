package com.srm325.budgetshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;
import com.srm325.budgetshop.model_classes.Entry;

public class CalendarActivity extends AppCompatActivity {
    CalendarView calendar;
    RecyclerView recyclerView;
    EntriesAdapter adapter;
    FirestoreRepository firestoreRepository;
    String categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        firestoreRepository = new FirestoreRepository();
        //initializes the calendarview
        initializeCalendar();
    }


    public void initializeCalendar() {
        calendar = (CalendarView) findViewById(R.id.calendar);

        // sets whether to show the week number.
        calendar.setShowWeekNumber(false);

        // sets the first day of week according to Calendar.
        // here we set Monday as the first day of the Calendar
        calendar.setFirstDayOfWeek(2);

        //The background color for the selected week.
        calendar.setSelectedWeekBackgroundColor(getResources().getColor(R.color.light_blue));

        //sets the color for the dates of an unfocused month.
        calendar.setUnfocusedMonthDateColor(getResources().getColor(R.color.transparent));

        //sets the color for the separator line between weeks.
        calendar.setWeekSeparatorLineColor(getResources().getColor(R.color.transparent));

        //sets the color for the vertical bar shown at the beginning and at the end of the selected date.
        calendar.setSelectedDateVerticalBar(R.color.dark_blue);

        //sets the listener to be notified upon selected date change.
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            //show the selected date as a toast
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
                int months = month + 1;
                Toast.makeText(getApplicationContext(), day + "/" + months + "/" + year, Toast.LENGTH_LONG).show();
                Intent i = new Intent(CalendarActivity.this, CalendarPopUpActivity.class);
                //Create the bundle
                Bundle bundle = new Bundle();
                String dateString = day + "/" + months + "/" + year;
                //Add your data to bundle
                bundle.putString("stuff", dateString);
                bundle.putInt("day", day);
                bundle.putInt("month", months);
                bundle.putInt("day", year);
                //Add the bundle to the intent
                i.putExtras(bundle);
                //Fire that second activity
                startActivity(i);

            }
        });
    }
}