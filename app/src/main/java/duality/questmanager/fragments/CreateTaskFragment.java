package duality.questmanager.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import duality.questmanager.R;
import duality.questmanager.Task;
import duality.questmanager.rest.CreateTask;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import java.util.Calendar;

/**
 * Created by anna on 07.05.16.
 */
public class CreateTaskFragment extends Fragment implements TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener{

    private EditText title;
    private TextView countTitleTextView;
    private EditText details;
    private TextView countDetailsTextView;
    private TextView timeTextView;
    private TextView dateTextView;

    public CreateTaskFragment(){}

    public static CreateTaskFragment newInstance() {
        CreateTaskFragment myFragment = new CreateTaskFragment();

        return myFragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.create_task_fragment, container, false);
        title = (EditText) rootView.findViewById(R.id.createTask);
        countTitleTextView = (TextView) rootView.findViewById(R.id.countTextView);
        countTitleTextView.setText("23/23");
        details = (EditText) rootView.findViewById(R.id.createTaskDetails);
        countDetailsTextView = (TextView) rootView.findViewById(R.id.countDetailsTextView);
        countDetailsTextView.setText("120/120");

        timeTextView = (TextView)rootView.findViewById(R.id.time_textview);
        dateTextView = (TextView)rootView.findViewById(R.id.date_textview);

        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int aft) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                countTitleTextView.setText(23 - s.toString().length() + "/23");
            }


        });
        details.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int aft) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                countDetailsTextView.setText(120 - s.toString().length() + "/120");
            }
        });


        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                CreateTaskFragment.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
        return rootView;

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = "You picked the following date: "+ dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
        dateTextView.setText(date);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        String time = "You picked the following time: "+hourOfDay+"h"+minute;
        timeTextView.setText(time);
    }
}
