package duality.questmanager.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.LocalBroadcastManager;
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
import duality.questmanager.gcm.MessageGCMListener;
import duality.questmanager.intent.CreateTaskService;
import duality.questmanager.intent.GetTasksServiceHelper;

/**
 * Created by anna on 20.05.16.
 */
public class TaskListFragmentDone extends BasicTaskListFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MessageInput = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String idStr = intent.getStringExtra(MessageGCMListener.RECIEVE_TASK_ID);
                String title = intent.getStringExtra(MessageGCMListener.RECIEVE_TASK_TITLE);
                String priceStr = intent.getStringExtra(MessageGCMListener.RECIEVE_TASK_PRICE);
                String date = intent.getStringExtra(MessageGCMListener.RECIEVE_TASK_DATE);

                int id = Integer.parseInt(idStr);
                int price = Integer.parseInt(priceStr);

                Task temp = new Task(id, title, price, date, 0);
                task.add(0, temp);
                refresh();
            }
        };
        isCompleted = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
//                String idStr = intent.getStringExtra(MessageGCMListener.GET_TASKRESULT_ID);
//                String isCompletedStr = intent.getStringExtra(MessageGCMListener.GET_TASKRESULT_RESULT);
//
//                int id = Integer.parseInt(idStr);
//                int isCompleted = Integer.parseInt(isCompletedStr);

                QuestDatabaseHelper db = new QuestDatabaseHelper(getContext());
                task = db.getAllTasks(false);
                initializeAdapter();
                refresh();
                //refresh();
            }
        };


//        LocalBroadcastManager.getInstance(getContext()).registerReceiver(MessageInput,
//                new IntentFilter(MessageGCMListener.RECIEVE_TASK_SUCCESS));

    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        LocalBroadcastManager.getInstance(getContext()).registerReceiver(MessageInput,
//                new IntentFilter(MessageGCMListener.RECIEVE_TASK_SUCCESS));
//
//        LocalBroadcastManager.getInstance(getContext()).registerReceiver(isCompleted,
//                new IntentFilter(MessageGCMListener.GET_TASKRESULT_SUCCESS));
//
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(MessageInput);
//        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(isCompleted);
//    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.task_list_done_fragment, container, false);
        rv = (RecyclerView) rootView.findViewById(R.id.rv2);

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refreshInputTasks);
        mSwipeRefreshLayout.setOnRefreshListener(TaskListFragmentDone.this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorAccent);


        setLayoutManagerAndAdapter();
        CoordinatorLayout mainLayout = (CoordinatorLayout) rootView.findViewById(R.id.coordinatorLayoutTLFD);
        ifListEmpty(mainLayout);

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

    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(MessageInput);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(isCompleted);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(MessageInput,
                new IntentFilter(MessageGCMListener.RECIEVE_TASK_SUCCESS));

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(isCompleted,
                new IntentFilter(MessageGCMListener.GET_TASKRESULT_SUCCESS));

    }
}
