package com.itheima.googleplay.fragment;

import com.itheima.googleplay.view.LoadingPage.LoadResult;

import android.view.View;


public class TopFragment extends BaseFragment {

	@Override
	public View createSuccessView() {
		return null;
	}

	@Override
	protected LoadResult load() {
		return LoadResult.error;
	}
}
