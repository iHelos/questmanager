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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;

import duality.questmanager.R;
import duality.questmanager.Task;
import duality.questmanager.content.QuestDatabase;
import duality.questmanager.content.QuestDatabaseHelper;
import duality.questmanager.intent.CreateTaskServiceHelper;
import duality.questmanager.rest.ResultListener;

import static duality.questmanager.AuthToken.getToken;

/**
 * Created by anna on 07.05.16.
 */
public class CreateTaskFragment extends Fragment implements TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener, ResultListener, View.OnClickListener {

    private EditText title;

    private EditText details;

    private EditText cost;
    private AutoCompleteTextView createTaskWorker;
    private TextView countDetailsTextView;
    private DatePickerDialog dpd;
    private EditText dateEditText;
    private ProgressBar createTaskProgress;
    private Button createTaskButton;
    private View rootView;

    private int mRequest;

    ArrayList<String> users;

    public CreateTaskFragment(){}

    public static CreateTaskFragment newInstance() {
        CreateTaskFragment myFragment = new CreateTaskFragment();

        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.create_task_fragment, container, false);
        title = (EditText) rootView.findViewById(R.id.createTask);

        cost = (EditText) rootView.findViewById(R.id.createTaskCost);
        details = (EditText) rootView.findViewById(R.id.createTaskDetails);

        createTaskWorker = (AutoCompleteTextView) rootView.findViewById(R.id.createTaskWorker);

        dateEditText = (EditText) rootView.findViewById(R.id.dateEdit);
        createTaskProgress = (ProgressBar) rootView.findViewById(R.id.createTaskProgressBar);
        createTaskButton = (Button) rootView.findViewById(R.id.createTaskButton);

        QuestDatabaseHelper DB  = new QuestDatabaseHelper(getContext());

        users = DB.getEmails();
        final String[] usersarray = users.toArray(new String[0]);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, usersarray);
        createTaskWorker.setAdapter(adapter);

        createTaskProgress.setVisibility(View.GONE);


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String token = getToken(sharedPreferences);


        createTaskButton.setOnClickListener(this);


        Calendar now = Calendar.getInstance();
        dpd = DatePickerDialog.newInstance(
                CreateTaskFragment.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setMinDate(now);

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
        String date = dayOfMonth+"."+(monthOfYear+1)+"."+year;

        dateEditText.setText(date);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        String time = "You picked the following time: "+hourOfDay+"h"+minute;
    }

    public void onAddQuestClick(View view) {
//        db.addTask(title, title, 22, true);
//        task.add(new Task(title, 22));
        CreateTaskServiceHelper.start(getContext(), this, "Title", "Text", "100", "ihelos.ermakov@gmail.com", "2016", "07", "11");
        //taskListDoneFragment.refresh(db.getAllTasks());
    }


    @Override
    public void onSuccess(String result) {
        Log.d("createTaskSuccess", result);
        stopProgress();
    }

    @Override
    public void onFail(String result) {
        Log.d("createTaskError", result);
        stopProgress();
    }

    @Override
    public void onClick(View v) {
        String taskTitle = title.getText().toString();
        String taskDetail = details.getText().toString();
        String price = cost.getText().toString();
        String reciever = createTaskWorker.getText().toString();

        String date = dateEditText.getText().toString();
        String[] dateParts = date.split("\\.");
        String year = "";
        String month = "";
        String day = "";
        try {
            year = dateParts[2];
            month = dateParts[1];
            day = dateParts[0];
        } catch (Exception ignored)
        {
            ignored.printStackTrace();
        }
        Log.d("Year", year);
        Log.d("Month", month);
        Log.d("day", day);
        mRequest = CreateTaskServiceHelper.start(getContext(), this, taskTitle, taskDetail, price, reciever, year, month, day);
        startProgress();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        CreateTaskServiceHelper.removeListener(mRequest);
    }

    private void startProgress()
    {
        createTaskProgress.setVisibility(View.VISIBLE);
        createTaskButton.setVisibility(View.GONE);
    }

    private void stopProgress()
    {
        createTaskProgress.setVisibility(View.GONE);
        createTaskButton.setVisibility(View.VISIBLE);
    }
}
