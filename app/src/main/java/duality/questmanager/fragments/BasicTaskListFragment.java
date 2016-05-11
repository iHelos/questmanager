package duality.questmanager.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import duality.questmanager.R;
import duality.questmanager.RVAdapter;
import duality.questmanager.Task;


public class BasicTaskListFragment extends Fragment {
    String[] tasks_text = new String[]{"one", "two", "three", "four",
            "five", "six", "seven", "eight", "nine", "ten", "eleven",
            "twelve", "thirteen", "fourteen", "fifteen"};


    protected RecyclerView rv;
    protected List<Task> task;
    private RVAdapter adapter;

    public BasicTaskListFragment(){}

    public static BasicTaskListFragment newInstance(ArrayList<Task> task) {
        BasicTaskListFragment myFragment = new BasicTaskListFragment();

        Bundle args = new Bundle();
        args.putParcelableArrayList("Tasks", task);
        myFragment.setArguments(args);

        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.task_list_fragment, container, false);


        rv = (RecyclerView) rootView.findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);


        initializeAdapter();


//        Button startFragmentActivity = (Button) rootView.findViewById(R.id.add);
//        startFragmentActivity.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Fragment fragment = CreateTaskFragment.newInstance();
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
//            }
//        });


        return rootView;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle  = getArguments();
        System.out.println("On Create Basic" + bundle);

        if (bundle != null) {
            task = bundle.getParcelableArrayList("Tasks");
        }

    }

    private void initializeAdapter(){
        adapter = new RVAdapter(task);
        rv.setAdapter(adapter);
    }

    public void refresh(List<Task> newTasks)
    {
        this.task = newTasks;
        adapter.notifyDataSetChanged();
    }



}
