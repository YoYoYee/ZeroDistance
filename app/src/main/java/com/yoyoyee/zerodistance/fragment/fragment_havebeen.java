package com.yoyoyee.zerodistance.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.yoyoyee.zerodistance.R;
import com.yoyoyee.zerodistance.activity.NewMissionActivity;
import com.yoyoyee.zerodistance.app.AppController;
import com.yoyoyee.zerodistance.client.ClientFunctions;
import com.yoyoyee.zerodistance.client.ClientResponse;
import com.yoyoyee.zerodistance.helper.CardViewAdapter;
import com.yoyoyee.zerodistance.helper.QueryFunctions;
import com.yoyoyee.zerodistance.helper.SQLiteHandler;
import com.yoyoyee.zerodistance.helper.SessionFunctions;
import com.yoyoyee.zerodistance.helper.datatype.Group;
import com.yoyoyee.zerodistance.helper.datatype.Mission;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by 楊霖村 on 2016/4/4.
 */
public class fragment_havebeen extends Fragment {//
    int[] id;
    String[] title;
    Date[] expAt;
    int[] needNum;
    int[] currentNum;
    boolean[] missiondangerous;
    boolean becontext, isfirst=true;
    String[] detial;
    ArrayList<Mission> missions;
    Mission[] mission , allmission;
    Group[] group;
    ArrayList<Group> Group;
    //
    CardViewAdapter CardViewAdapter;
    int upDataCount=0;
    private SwipeRefreshLayout mSwipeRefreshLayout;//RecyclerView外圍框
    RecyclerView mList;
    LinearLayoutManager layoutManager;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_havebeen, container, false);
        makecard();
        // 頂端向下滑更新
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                CardViewAdapter.setItemCount(0);
                mList.scrollToPosition(0);
                updataphoneDB();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        // 頂端向下滑更新

        try {
            mList = (RecyclerView) v.findViewById(R.id.listView);
            LinearLayoutManager layoutManager;
            layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mList.setLayoutManager(layoutManager);
            mList.setAdapter(CardViewAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return v;

        //listView 典籍

        //listView 典籍
    }
    public void  onResume(){
        super.onResume();
        //  Toast.makeText(getContext(), "onResume{mission}", Toast.LENGTH_SHORT).show();
        CardViewAdapter.setItemCount(0);
        mList.scrollToPosition(0);
        makecard();
        mList.setAdapter(CardViewAdapter);
    }

    //設置字體大小
    private void setFontSize(){
        TextView textViewTemp;
        SessionFunctions SF= new SessionFunctions();

    }
    public void makecard() {
        missions  = QueryFunctions.getMissions();
        Group  = QueryFunctions.getGroups();
        mission = new Mission[missions.size()];
        group = new Group[Group.size()];
        allmission = new Mission[missions.size()+Group.size()];
        for(int i = 0;i <missions.size();i++) {
            if ((becontext)) {
                mission[i] = new Mission(missions.get(missions.size() - i - 1).id, missions.get(missions.size() - i - 1).title
                        , missions.get(missions.size() - i - 1).content, missions.get(missions.size() - i - 1).expAt
                        , missions.get(missions.size() - i - 1).needNum, missions.get(missions.size() - i - 1).currentNum
                        , false, true);

            } else {//獎勵
                mission[i] = new Mission(missions.get(missions.size() - i - 1).id, missions.get(missions.size() - i - 1).title
                        , missions.get(missions.size() - i - 1).reward, missions.get(missions.size() - i - 1).expAt
                        , missions.get(missions.size() - i - 1).needNum, missions.get(missions.size() - i - 1).currentNum
                        , false, true);
            }
        }
        for(int i=missions.size();i<missions.size()+Group.size();i++){
            group[i-missions.size()] = new Group(Group.get(missions.size()+Group.size()-i-1).id, Group.get(missions.size()+Group.size()-i-1).title
                    , Group.get(missions.size()+Group.size()-i-1).content, Group.get(missions.size()+Group.size()-i-1).expAt
                    , Group.get(missions.size()+Group.size()-i-1).needNum, Group.get(missions.size()+Group.size()-i-1).currentNum, false);
        }

        if(!isfirst) {
            mList.setAdapter(CardViewAdapter);
        }
        Arrays.sort(mission);//時間排序
        Arrays.sort(group);
        for(int i = 0;i <missions.size();i++){
            allmission[i] = mission[i];
        }for(int i=0;i<Group.size();i++){
            allmission[missions.size()+i]=group[i];
        }

        CardViewAdapter = new CardViewAdapter(allmission,R.layout.fragment_fragment_havebeen);
    }
    private void updataphoneDB(){//更新手機資料
        totalvuledel();
        updataMissionDB();
    }
    private void updataMissionDB(){  //成功會更新Group
        ClientFunctions.updateGroups(new ClientResponse() {
            @Override
            public void onResponse(String response) {
              //  Toast.makeText(getContext(), "更新成功(任務)", Toast.LENGTH_SHORT).show();
                upDataCount=0;
                updataGroupDB();//更新揪團
            }

            @Override
            public void onErrorResponse(String response) {
                if(upDataCount>=5){
                    Toast.makeText(getContext(), "更新失敗(任務)", Toast.LENGTH_SHORT).show();
                }else{
                    upDataCount+=1;
                    updataMissionDB();
                }
            }
        });
    }

    private void updataGroupDB(){
        ClientFunctions.updateGroups(new ClientResponse() {
            @Override
            public void onResponse(String response) {
//                CardViewAdapter.notifyDataSetChanged();
              //  Toast.makeText(getContext(), "更新成功(揪團)", Toast.LENGTH_SHORT).show()
                upDataCount=0;
            }

            @Override
            public void onErrorResponse(String response) {
                if(upDataCount>=5){
                    Toast.makeText(getContext(), "更新失敗(揪團)", Toast.LENGTH_SHORT).show();
                }else{
                    upDataCount+=1;
                    updataGroupDB();
                }
            }
        });
    }
    public void totalvuledel(){
        id = null;
        title = null;
        detial = null;
        expAt = null;
        needNum = null;
        currentNum = null;
        missiondangerous = null;
    }
}