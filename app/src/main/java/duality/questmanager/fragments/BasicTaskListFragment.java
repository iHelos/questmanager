package duality.questmanager.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import duality.questmanager.R;
import duality.questmanager.RVAdapter;
import duality.questmanager.Task;
import duality.questmanager.FragmentsActivity.*;


public class BasicTaskListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    protected RecyclerView rv;
    protected List<Task> task;
    private RVAdapter adapter;
    private android.support.design.widget.FloatingActionButton startCreateTaskFragment;
    protected View rootView;
    protected SwipeRefreshLayout mSwipeRefreshLayout;

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

        rootView = inflater.inflate(R.layout.task_list_fragment, container, false);
        startCreateTaskFragment= (android.support.design.widget.FloatingActionButton) rootView.findViewById(R.id.add);
        rv = (RecyclerView) rootView.findViewById(R.id.rv);


        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refreshOutputTasks);
        mSwipeRefreshLayout.setOnRefreshListener(BasicTaskListFragment.this);

        setLayoutManagerAndAdapter();

        // делаем повеселее
//        mSwipeRefreshLayout.setColorScheme(R.color.blue, R.color.green, R.color.yellow, R.color.red);

        return rootView;
    }

    protected void setLayoutManagerAndAdapter() {
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);
        initializeAdapter();
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

    protected void initializeAdapter(){
        adapter = new RVAdapter(task);
        rv.setAdapter(adapter);
    }

    public void refresh(List<Task> newTasks)
    {
        this.task = newTasks;
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {

        Toast.makeText(getActivity(), R.string.refresh_started, Toast.LENGTH_SHORT).show();
        mSwipeRefreshLayout.setRefreshing(true);
        refreshAction();
        mSwipeRefreshLayout.setRefreshing(false);
        Toast.makeText(getActivity(), R.string.refresh_finished, Toast.LENGTH_SHORT).show();


//        // ждем 3 секунды и прячем прогресс
//        mSwipeRefreshLayout.post(new Runnable() {
//            @Override
//            public void run() {
//                refreshAction();
//                mSwipeRefreshLayout.setRefreshing(false);
//                // говорим о том, что собираемся закончить
//                Toast.makeText(getActivity(), R.string.refresh_finished, Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    public void refreshAction() {
        int i = 5;
        while (i > 0) {
            Toast.makeText(getActivity(), i+"", Toast.LENGTH_SHORT).show();
            i--;
        }

    }


}
