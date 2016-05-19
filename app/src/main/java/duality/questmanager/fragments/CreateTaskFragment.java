package duality.questmanager.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

import duality.questmanager.R;
import duality.questmanager.Task;

import static duality.questmanager.AuthToken.getToken;

/**
 * Created by anna on 07.05.16.
 */
public class CreateTaskFragment extends Fragment implements TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener {

    private EditText title;

    private EditText details;

    private DatePickerDialog dpd;
    private EditText dateEditText;

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

        details = (EditText) rootView.findViewById(R.id.createTaskDetails);

        dateEditText = (EditText) rootView.findViewById(R.id.dateEdit);


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String token = getToken(sharedPreferences);
        Log.d("YOYO", "Auth-token: " + token);




        Calendar now = Calendar.getInstance();
        dpd = DatePickerDialog.newInstance(
                CreateTaskFragment.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setMinDate(now);
        dpd.setTitle("Крайний срок");
        dateEditText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
            }
        });
        return rootView;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
        dateEditText.setText(date);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        String time = "You picked the following time: "+hourOfDay+"h"+minute;
    }


}
