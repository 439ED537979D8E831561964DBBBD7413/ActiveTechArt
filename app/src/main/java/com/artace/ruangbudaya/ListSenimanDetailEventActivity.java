package com.artace.ruangbudaya;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.artace.ruangbudaya.adapter.ListSenimanDetailEventAdapter;
import com.artace.ruangbudaya.connection.DatabaseConnection;
import com.artace.ruangbudaya.controller.AppController;
import com.artace.ruangbudaya.pojo.PojoSeniman;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListSenimanDetailEventActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ListSenimanDetailEventAdapter adapter;
    List<PojoSeniman> senimanList = new ArrayList<PojoSeniman>();
    ProgressBar mLoadingAnim;
    SwipeRefreshLayout mSwipeRefreshLayout;
    RequestQueue queue;
    String urlRead = DatabaseConnection.getReadListSenimanDetailEvent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_seniman_detail_event);

        Toolbar toolbar = (Toolbar) findViewById(R.id.list_seniman_detail_event_toolbar);
        this.setSupportActionBar(toolbar);
        ActionBar ab = this.getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("List Seniman");

        recyclerView = (RecyclerView) findViewById(R.id.list_seniman_detail_event_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this.getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2,dpToPx(10),true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new ListSenimanDetailEventAdapter(this, senimanList);
        recyclerView.setAdapter(adapter);

        getData();

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.list_seniman_detail_event_swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
    }


    public void getData(){
        //Set loading anim
        mLoadingAnim = (ProgressBar) findViewById(R.id.list_seniman_detail_event_progressbar);
        mLoadingAnim.setVisibility(View.VISIBLE);

        //Getting Instance of Volley Request Queue
        queue = AppController.getInstance().getRequestQueue();

        //empty eventList
        senimanList.clear();

        Bundle dataExtra = getIntent().getExtras();
        final int id_acara = dataExtra.getInt("id_acara");

        urlRead += "?id_acara="+String.valueOf(id_acara);

        JsonArrayRequest newsReq = new JsonArrayRequest(urlRead, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try{
                    JSONArray jr = response.getJSONArray(0);
                    for (int i = 0; i < jr.length(); i++) {
                        try {

                            JSONObject obj = (JSONObject) jr.get(i);

                            PojoSeniman seniman = new PojoSeniman(obj.getInt("id_kelompok_seniman"),obj.getInt("id_acara"),obj.getString("nama"),obj.getString("portfolio"),obj.getString("no_hp"), DatabaseConnection.getBaseUrl() + obj.getString("foto"), obj.getString("status"));

                            // adding event to events array
                            senimanList.add(seniman);
                            Log.d("OSFragment","Iterasi pertama");
                            Log.e("data"," "+obj.toString());
                        } catch (Exception e) {
                            System.out.println("LOG gamao! = " + e.getMessage());
                        } finally {
                            //Notify adapter about data changes
                            adapter.notifyItemChanged(i);
                        }
                    }
                }catch (Exception e){
                    Log.e("OSFragment","LOG gamao diluar! = " + e.getMessage());
                }
                finally {
                    if (senimanList.size() == 0){
                        Context context = getApplicationContext();
                        Toast toast = Toast.makeText(context,"Data Kosong", Toast.LENGTH_LONG);
                        toast.show();
                    }

                    mLoadingAnim.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("LOG_OrganizerEventsFragment : "+error.getMessage());

            }
        });
        //Adding JsonArrayRequest to Request Queue
        queue.add(newsReq);
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
