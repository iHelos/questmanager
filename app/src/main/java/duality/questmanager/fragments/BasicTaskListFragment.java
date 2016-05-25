package duality.questmanager.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import duality.questmanager.R;
import duality.questmanager.RVAdapter;
import duality.questmanager.Task;
import duality.questmanager.content.QuestDatabaseHelper;
import duality.questmanager.gcm.MessageGCMListener;
import duality.questmanager.intent.GetTasksServiceHelper;
import duality.questmanager.rest.ResultListener;


public class BasicTaskListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, ResultListener {

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

    protected BroadcastReceiver MessageInput = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String idStr = intent.getStringExtra(MessageGCMListener.SEND_OUT_TASK_ID);
            String title = intent.getStringExtra(MessageGCMListener.SEND_OUT_TASK_TITLE);
            String priceStr = intent.getStringExtra(MessageGCMListener.SEND_OUT_TASK_PRICE);
            String date = intent.getStringExtra(MessageGCMListener.SEND_OUT_TASK_DATE);

            int id = Integer.parseInt(idStr);
            int price = Integer.parseInt(priceStr);

            Task temp = new Task(id, title, price, date, 0);
            task.add(0, temp);
            refresh();
        }
    };

    protected BroadcastReceiver isCompleted = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            String idStr = intent.getStringExtra(MessageGCMListener.OUT_TASKRESULT_ID);
//            String isCompletedStr = intent.getStringExtra(MessageGCMListener.OUT_TASKRESULT_RESULT);
//
//            int id = Integer.parseInt(idStr);
//            int isCompleted = Integer.parseInt(isCompletedStr);

            QuestDatabaseHelper db = new QuestDatabaseHelper(getContext());
            task = db.getAllTasks(false);
            initializeAdapter();
            refresh();
            //refresh();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.task_list_fragment, container, false);
        startCreateTaskFragment= (android.support.design.widget.FloatingActionButton) rootView.findViewById(R.id.add);
        startCreateTaskFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = CreateTaskFragment.newInstance();

                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, fragment);
                ft.addToBackStack(null);
                ft.commit();
                getActivity().setTitle(R.string.menu_create_task);
            }
        });
        rv = (RecyclerView) rootView.findViewById(R.id.rv);


        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refreshOutputTasks);
        mSwipeRefreshLayout.setOnRefreshListener(BasicTaskListFragment.this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorAccent);
//        mSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.cardBackgroundColorInverse);

        setLayoutManagerAndAdapter();
        
        return rootView;
    }

    protected void setLayoutManagerAndAdapter() {
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);
        initializeAdapter();
    }

    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(MessageInput,
                new IntentFilter(MessageGCMListener.SEND_OUT_TASK_SUCCESS));

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(isCompleted,
                new IntentFilter(MessageGCMListener.OUT_TASKRESULT_SUCCESS));

    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(MessageInput);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(isCompleted);
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

    public void refresh()
    {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {

        refreshAction();
        mSwipeRefreshLayout.setRefreshing(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
                refreshAction();

            }
        }, 1);

    }

    public void refreshAction() {
        int mRequest = GetTasksServiceHelper.start(getContext(), this, true);
    }


    @Override
    public void onSuccess(String result) {
        QuestDatabaseHelper db = new QuestDatabaseHelper(getContext());
        task = db.getAllTasks(true);
        initializeAdapter();
        refresh();
    }

    @Override
    public void onFail(String result) {

    }
}
