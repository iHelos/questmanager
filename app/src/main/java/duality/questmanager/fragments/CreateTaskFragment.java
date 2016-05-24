package duality.questmanager.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import duality.questmanager.LoginActivity;
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
    private TextInputLayout titleLabel;

    private EditText details;
    private TextInputLayout detailsLabel;

    private EditText cost;
    private TextInputLayout costLabel;

    private AutoCompleteTextView createTaskWorker;
    private TextInputLayout workerLabel;

    private DatePickerDialog dpd;
    private EditText dateEditText;
    private TextInputLayout dateLabel;

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
        titleLabel = (TextInputLayout) rootView.findViewById(R.id.input_layout_name);


        details = (EditText) rootView.findViewById(R.id.createTaskDetails);
        detailsLabel = (TextInputLayout) rootView.findViewById(R.id.input_layout_name_details);

        cost = (EditText) rootView.findViewById(R.id.createTaskCost);
        costLabel = (TextInputLayout) rootView.findViewById(R.id.input_layout_name_cost);

        createTaskWorker = (AutoCompleteTextView) rootView.findViewById(R.id.createTaskWorker);
        workerLabel = (TextInputLayout) rootView.findViewById(R.id.input_layout_name_worker);

        dateEditText = (EditText) rootView.findViewById(R.id.dateEdit);
        dateLabel = (TextInputLayout) rootView.findViewById(R.id.input_layout_name_date);

        createTaskProgress = (ProgressBar) rootView.findViewById(R.id.createTaskProgressBar);
        createTaskButton = (Button) rootView.findViewById(R.id.createTaskButton);


        QuestDatabaseHelper DB  = new QuestDatabaseHelper(getContext());

        users = DB.getEmails();
        final String[] usersarray = users.toArray(new String[0]);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.list_item_autocomplite, usersarray);

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

//    private void setupFloatingLabelError() {
//        final TextInputLayout floatingUsernameLabel = (TextInputLayout) rootView.findViewById(R.id.input_layout_name);
//        floatingUsernameLabel.getEditText().addTextChangedListener(new TextWatcher() {
//            // ...
//            @Override
//            public void onTextChanged(CharSequence text, int start, int count, int after) {
//                if (text.length() < 1 ) {
//                    floatingUsernameLabel.setError(getString(R.string.field_required));
//                    floatingUsernameLabel.setErrorEnabled(true);
//                } else {
//                    floatingUsernameLabel.setErrorEnabled(false);
//                }
//            }
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count,
//                                          int after) {
//                // TODO Auto-generated method stub
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//    }


    public void onAddQuestClick(View view) {
//        db.addTask(title, title, 22, true);
//        task.add(new Task(title, 22));
        CreateTaskServiceHelper.start(getContext(), this, "Title", "Text", "100", "ihelos.ermakov@gmail.com", "2016", "07", "11");
        //taskListDoneFragment.refresh(db.getAllTasks());
    }


    @Override
    public void onSuccess(String result) {
        Log.d("createTaskSuccess", result);

        int id = Integer.parseInt(result);

        Fragment fragment = InfoTaskFragment.newInstance(id, false);

        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.commit();
        getActivity().setTitle(R.string.header_report_for_task);
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

        //Валидация
        Boolean isEmptyTitle = true;
        Boolean isEmptyDetails = true;
        Boolean isEmptyPrice = true;
        Boolean isEmptyReciever = false;
        Boolean isEmptyDate = false;

        if (taskTitle.length() == 0) {
            titleLabel.setError(getString(R.string.field_required));
            titleLabel.setErrorEnabled(true);
                    isEmptyTitle = true;
        }
        else {
            titleLabel.setError(null);
            titleLabel.setErrorEnabled(false);
            isEmptyTitle = false;
        }
        if (taskDetail.length() == 0) {
            detailsLabel.setError(getString(R.string.field_required));
            detailsLabel.setErrorEnabled(true);
            isEmptyDetails = true;
        }
        else {
            detailsLabel.setError(null);
            detailsLabel.setErrorEnabled(false);
            isEmptyDetails = false;
        }
        if (price.length() == 0) {
            costLabel.setError(getString(R.string.field_required));
            costLabel.setErrorEnabled(true);
            isEmptyPrice = true;
        }
        else {
            costLabel.setError(null);
            costLabel.setErrorEnabled(false);
            isEmptyPrice = false;
        }
//        if (reciever.length() == 0) {
//            workerLabel.setError(getString(R.string.field_required));
//            isEmptyReciever = true;
//        }
//        else {
//            if (!LoginActivity.isValidEmail(reciever)){
//                workerLabel.setError(getString(R.string.invalid_email));
//            } else {
//                isEmptyReciever = false;
//                workerLabel.setError(null);
//            }
//
//        }
//        if (date.length() == 0) {
//            dateLabel.setError(getString(R.string.field_required));
//            isEmptyDate = true;
//        }
//        else {
//            dateLabel.setError(null);
//            isEmptyDate = false;
//        }

        if (!isEmptyTitle & !isEmptyDetails & !isEmptyPrice & !isEmptyReciever & !isEmptyDate) {

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
    }

//    private boolean isValidEmail(String email) {
//        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
//                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
//
//        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
//        Matcher matcher = pattern.matcher(email);
//        return matcher.matches();
//    }


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
