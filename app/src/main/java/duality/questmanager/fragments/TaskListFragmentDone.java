package duality.questmanager.fragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import duality.questmanager.R;
import duality.questmanager.Task;
import duality.questmanager.content.QuestDatabaseHelper;
import duality.questmanager.intent.GetTasksServiceHelper;

/**
 * Created by anna on 20.05.16.
 */
public class TaskListFragmentDone extends BasicTaskListFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.task_list_done_fragment, container, false);
        rv = (RecyclerView) rootView.findViewById(R.id.rv2);

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refreshInputTasks);
        mSwipeRefreshLayout.setOnRefreshListener(TaskListFragmentDone.this);

        setLayoutManagerAndAdapter();

        return rootView;
    }


    public static TaskListFragmentDone newInstance(ArrayList<Task> task) {
        TaskListFragmentDone myFragment = new TaskListFragmentDone();

        Bundle args = new Bundle();
        args.putParcelableArrayList("Tasks", task);
        myFragment.setArguments(args);

        return myFragment;
    }

    @Override
    public void refreshAction() {
        int mRequest = GetTasksServiceHelper.start(getContext(), this, false);
    }

    @Override
    public void onSuccess(String result) {
        QuestDatabaseHelper db = new QuestDatabaseHelper(getContext());
        task = db.getAllTasks(false);
        initializeAdapter();
        refresh();
    }

    @Override
    public void onFail(String result) {

    }



}
