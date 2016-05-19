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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

import duality.questmanager.R;

import static duality.questmanager.AuthToken.getToken;

public class ReportTaskFragment extends Fragment {

    private TextView title;
    private TextView dateSend;
    private EditText details;
    private TextView countDetailsTextView;
    private ImageView coinImg;


    public ReportTaskFragment(){}

    public static ReportTaskFragment newInstance() {
        ReportTaskFragment myFragment = new ReportTaskFragment();

        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.task_report_fragment_linear, container, false);
        title = (TextView) rootView.findViewById(R.id.reportTitle);
        dateSend = (TextView) rootView.findViewById(R.id.reportDateSend);
        details = (EditText) rootView.findViewById(R.id.reportDetails);
        coinImg = (ImageView) rootView.findViewById(R.id.reportCoin);

//        countDetailsTextView = (TextView) rootView.findViewById(R.id.countReportDetailsTextView);
//        countDetailsTextView.setText("120/120");


        title.setText("Это заголовок");
        dateSend.setText("Вы потратили:5 дней");
        coinImg.setImageResource(R.drawable.ic_coin_big);



//        details.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int aft) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                countDetailsTextView.setText(120 - s.toString().length() + "/120");
//            }
//        });


        return rootView;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }





}
