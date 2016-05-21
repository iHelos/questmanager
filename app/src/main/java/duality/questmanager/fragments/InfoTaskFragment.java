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
import android.widget.LinearLayout;
import android.widget.TextView;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import duality.questmanager.R;
import duality.questmanager.Task;
import duality.questmanager.rest.CreateTask;

import static duality.questmanager.AuthToken.getToken;

public class InfoTaskFragment extends Fragment implements TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener {

    protected Integer taskId;
    private EditText title;
    private String titleContent;
    private EditText details;
    private String detailsContent;
    private EditText worker;
    private String workerContent;
    private EditText cost;
    private String costContent;
    private DatePickerDialog dpd;
    private EditText date;
    private GregorianCalendar dateContent;
    private Boolean isTasksForMe;

    public InfoTaskFragment(){}

    public static InfoTaskFragment newInstance(Integer ID, Boolean isForMe) {
        InfoTaskFragment myFragment = new InfoTaskFragment();

        Bundle args = new Bundle();
        args.putInt("ID", ID);
        args.putBoolean("isForMe", isForMe);
        myFragment.setArguments(args);


        return myFragment;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.task_full_info_fragment, container, false);
        title = (EditText) rootView.findViewById(R.id.infoTaskTitle);
        title.setText(titleContent);

        details = (EditText) rootView.findViewById(R.id.infoTaskDetails);
        details.setText(detailsContent);
        worker = (EditText) rootView.findViewById(R.id.infoTaskWorker);
        worker.setText(workerContent);
        cost = (EditText) rootView.findViewById(R.id.infoTaskCost);
        cost.setText(costContent);
        date = (EditText) rootView.findViewById(R.id.infoTaskDate);
        date.setText(dateFormat(dateContent));


        LinearLayout mainll = (LinearLayout) rootView.findViewById(R.id.mainLinearLayout);

        if (isTasksForMe) {
            Button done_btn = (Button) inflater.inflate(R.layout.task_done_btn, null, false);
            Button cancel_btn = (Button) inflater.inflate(R.layout.task_cancel_btn, null, false);

            mainll.addView(done_btn);
            mainll.addView(cancel_btn);

        } else {
            Button button = (Button) inflater.inflate(R.layout.task_report_btn, null, false);
            mainll.addView(button);
        }





//        Calendar now = dateContent;
//        dpd = DatePickerDialog.newInstance(
//                InfoTaskFragment.this,
//                now.get(Calendar.YEAR),
//                now.get(Calendar.MONTH),
//                now.get(Calendar.DAY_OF_MONTH)
//        );
//        onDateSet(dpd, now.get(Calendar.YEAR), now.get(Calendar.MONTH),
//                now.get(Calendar.DAY_OF_MONTH));
//        dpd.autoDismiss(true);
//        dpd.setMaxDate(now);
//        dpd.setMinDate(now);
//        date.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
//            }
//        });

        return rootView;


    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle  = getArguments();

        if (bundle != null) {
            taskId = bundle.getInt("ID");
            isTasksForMe = bundle.getBoolean("isForMe");
        }

        getTaskById(taskId);
    }
    void getTaskById(Integer ID) {

        Task mock = new Task("Заголовочек", "Описаниеце длинное придлинное мазафака ШИва ван лов","shiva@mur.cat", 99, new GregorianCalendar(2017,
                Calendar.DECEMBER, 31));
        if (ID == 1) {
            titleContent = mock.getTitle();
            detailsContent = mock.getDetails();
            workerContent = mock.getWorker();
            costContent = mock.getCoinCost() + "";
            dateContent = mock.getDate();
        }

    }
    String dateFormat(GregorianCalendar currentDate) {
        String result = "";
        if (currentDate != null) {
        result = currentDate.get(Calendar.DAY_OF_MONTH) + "." + currentDate.get(Calendar.MONTH) + "." + currentDate.get(Calendar.YEAR);
        }
        return result;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
            String dateSet = dayOfMonth+"."+(monthOfYear+1)+"."+year;
            date.setText(dateSet);
        }

        @Override
        public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
            String time = "You picked the following time: "+hourOfDay+"h"+minute;
        }
}
