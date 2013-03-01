package com.abrantix.advancedwidgets.examples;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ReordableListViewFragment extends ListFragment {

	ArrayList<Integer> mDataSet = new ArrayList<Integer>();
	HashMap<Integer, Integer> mSavedState = new HashMap<Integer, Integer>();
	
	private void saveState() {
		mSavedState.clear();
		ListView lv = getListView();
		int first = lv.getFirstVisiblePosition();
		int last = lv.getLastVisiblePosition();
		for(int i=0; i<mDataSet.size(); i++) {
			if( i >= first && i <= last) {
				View v = lv.getChildAt(i-first);
				int top = v.getTop();
				int dataIdx = i;
				int dataId = mDataSet.get(dataIdx);
				mSavedState.put(dataId, top);
			} else if( i < first ) {
				int top = lv.getTop() - lv.getHeight()/2;
				int dataId = mDataSet.get(i);
				mSavedState.put(dataId, top);
			} else if( i > last ) {
				int top = lv.getBottom() + lv.getHeight()/2;
				int dataId = mDataSet.get(i);
				mSavedState.put(dataId, top);
			}
		}
		for(int i=0; i < lv.getChildCount(); i++) {
			View v = lv.getChildAt(i);
			int top = v.getTop();
			int dataIdx = first + i;
			int dataId = mDataSet.get(dataIdx);
			mSavedState.put(dataId, top);
		}
	}
	
	Interpolator mInterpolator = new DecelerateInterpolator();
	
	private void animateNewState() {
		ListView lv = getListView();
		int first = lv.getFirstVisiblePosition();
		int last = lv.getLastVisiblePosition();
		for(int i=0; i < lv.getChildCount(); i++) {
			int dataIdx = first + i;
			int dataId = mDataSet.get(dataIdx);
			if( mSavedState.containsKey(dataId) ) {
				View v = lv.getChildAt(i);
				int top = v.getTop();
				int oldTop = mSavedState.get(dataId);
				int hDiff = top - oldTop;
				TranslateAnimation anim = new TranslateAnimation(0, 0, -hDiff, 0);
				anim.setInterpolator(mInterpolator);
				anim.setDuration(333);
				v.startAnimation(anim);
			}
		}
	}

	
	private void generateDataSet() {
		mDataSet.clear();
		for(int i=0; i<8; i++) {
			mDataSet.add(i);
		}
	}
	
	private void reorderDataSet() {
		Collections.shuffle(mDataSet);
	}
	
	ItemAdapter mAdapter;
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, 
			Bundle savedInstanceState) {
		generateDataSet();
		
		final View v = inflater.inflate(R.layout.reordable_list_view, null);
		ListView lv = (ListView) v.findViewById(android.R.id.list);
		if( mAdapter == null ) mAdapter = new ItemAdapter();
		lv.setAdapter( mAdapter );
		
		v.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				saveState();
				reorderDataSet();
				mAdapter.notifyDataSetChanged();
				animateNewState();
				v.postDelayed(this, 2999);
			}
		}, 2999);
		
		return v;
	}
	
	private class ItemAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mDataSet.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if( v == null ) {
				LayoutInflater inflater = LayoutInflater.from(getActivity());
				v = inflater.inflate(R.layout.reordable_list_item, null);
			}
			TextView tv = (TextView) v.findViewById(R.id.text);
			tv.setText("Item "+mDataSet.get(position));
			tv.setTextColor(Color.MAGENTA);
			tv.setBackgroundColor(Color.LTGRAY);
			//tv.setTextColor(Color.argb(0xff, (getCount()-position)*2, (getCount()-position)*2, position*2));
			//tv.setBackgroundColor(Color.argb(0xff, position*1, position*2, position/2));
			return v;
		}
		
	}
	
}
