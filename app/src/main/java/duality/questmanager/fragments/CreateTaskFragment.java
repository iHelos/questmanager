package duality.questmanager.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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


/**
 * Created by anna on 07.05.16.
 */
public class CreateTaskFragment extends Fragment {

    private EditText title;
    private TextView countTitleTextView;
    private EditText details;
    private TextView countDetailsTextView;

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
        return rootView;

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

}
