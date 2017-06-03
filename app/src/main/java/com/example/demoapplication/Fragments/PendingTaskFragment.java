package com.example.demoapplication.Fragments;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demoapplication.Adapter.MyTaskAdapter;
import com.example.demoapplication.ClassFiles.MainActivity;
import com.example.demoapplication.Database.DatabaseHandler;
import com.example.demoapplication.ModelClass.MyTaskModel;
import com.example.demoapplication.R;
import com.example.demoapplication.UtilClass.AppPreferences;
import com.example.demoapplication.UtilClass.AppUtils;
import com.example.demoapplication.UtilClass.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anshultyagi on 02/06/17.
 */

public class PendingTaskFragment extends Fragment {
    View rootView;
    TextView empty_text;
    DatabaseHandler databaseHandler;
    MyTaskModel myTaskModel;
    private List<MyTaskModel> myTaskModelList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private MyTaskAdapter mAdapter;

    private LinearLayoutManager mLinearLayoutManager;
    private GridLayoutManager mGridLayoutManager;
    private Paint p = new Paint();
    private AlertDialog.Builder alertDialog;
    private View view;

    public PendingTaskFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_tasks_list, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_Category);
        empty_text = (TextView) rootView.findViewById(R.id.empty_text);
        empty_text.setText("There is no Pending task.");
        mAdapter = new MyTaskAdapter(myTaskModelList, getActivity());
        prepareMovieData();
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mGridLayoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        int spanCount = 2; // 3 columns
        int spacing = 10; // 50px
        boolean includeEdge = false;
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        initSwipe();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Home");
    }

    public void prepareMovieData() {
        String email;
        databaseHandler = new DatabaseHandler(getActivity());
        if (myTaskModelList == null) {
            myTaskModelList = new ArrayList<>();
        } else {
            myTaskModelList.clear();
        }
        Cursor cursor = databaseHandler.getPendingTaskData("SELECT * FROM " + "pending_tasks_table");
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                email = cursor.getString(2);
                if (email.equals(AppPreferences.getUserEmail(getActivity(), AppUtils.EMAIL))) {
                    if (!myTaskModelList.contains(cursor.getString(0))) {
                        myTaskModel = new MyTaskModel();
                        myTaskModel.setTitleName(cursor.getString(3));
                        myTaskModel.setsDescription(cursor.getString(1));
                        myTaskModel.setsId(cursor.getString(0));
                        myTaskModelList.add(myTaskModel);
                    }

                }
            }
            mAdapter.notifyDataSetChanged();
            empty_text.setVisibility(View.GONE);
        } else {
            empty_text.setVisibility(View.VISIBLE);
        }

    }

    public void changeLayoutManager() {
        if (MainActivity.getInstance().imageView_changeToGrid.getVisibility() == View.VISIBLE) {
            if (mRecyclerView.getLayoutManager().equals(mLinearLayoutManager)) {
                mRecyclerView.setLayoutManager(mGridLayoutManager);
            }
            MainActivity.getInstance().imageView_changeToGrid.setVisibility(View.GONE);
            MainActivity.getInstance().imageView_changeToList.setVisibility(View.VISIBLE);
        } else if (MainActivity.getInstance().imageView_changeToList.getVisibility() == View.VISIBLE) {
            if (mRecyclerView.getLayoutManager().equals(mGridLayoutManager)) {
                mRecyclerView.setLayoutManager(mLinearLayoutManager);
            }
            MainActivity.getInstance().imageView_changeToGrid.setVisibility(View.VISIBLE);
            MainActivity.getInstance().imageView_changeToList.setVisibility(View.GONE);
        }
    }
    public static final float ALPHA_FULL = 1.0f;

    private void initSwipe() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                String emails, ids = myTaskModelList.get(position).getsId();
                if (direction == ItemTouchHelper.LEFT) {
                    mAdapter.removeItem(position);
                    Cursor cursor = databaseHandler.getPendingTaskData("SELECT * FROM " + "pending_tasks_table");
                    if (cursor != null && cursor.getCount() > 0) {
                        while (cursor.moveToNext()) {
                            emails = cursor.getString(2);
                            if (emails.equals(AppPreferences.getUserEmail(getActivity(), AppUtils.EMAIL))) {
                                if (ids.equals(cursor.getString(0))) {
                                    databaseHandler.deletePendingTask(emails, ids);
                                }
                                mAdapter.notifyDataSetChanged();
                                empty_text.setVisibility(View.GONE);
                            }
                        }
                    }

                } else {
                    //  removeView();
                    Toast.makeText(getActivity(), "Completed", Toast.LENGTH_LONG).show();
                    mAdapter.removeItem(position);
                    Cursor cursor = databaseHandler.getPendingTaskData("SELECT * FROM " + "pending_tasks_table");
                    if (cursor != null && cursor.getCount() > 0) {
                        while (cursor.moveToNext()) {
                            emails = cursor.getString(2);
                            if (emails.equals(AppPreferences.getUserEmail(getActivity(), AppUtils.EMAIL))) {
                                if (ids.equals(cursor.getString(0))) {
                                    databaseHandler.deletePendingTask(emails, ids);
                                    databaseHandler.insertCompletedTask(cursor.getString(2),
                                            cursor.getString(3), cursor.getString(1));
                                }
                                mAdapter.notifyDataSetChanged();
                                empty_text.setVisibility(View.GONE);
                            }
                        }
                    }
                    // edit_position = position;
                    //alertDialog.setTitle("Edit Country");
                    //   et_country.setText(countries.get(position).toString());
                    //alertDialog.show();
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if (dX > 0) {
                        p.setColor(getActivity().getResources().getColor(R.color.Green));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.checked);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);

                    } else {
                        p.setColor(getActivity().getResources().getColor(R.color.Red));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.basket);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);

                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    private void removeView() {
        if (view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }
}

