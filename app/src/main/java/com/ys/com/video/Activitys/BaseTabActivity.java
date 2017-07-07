package com.ys.com.video.Activitys;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
/**
 * ��������Ŀ����ܣ�
 * ��ViewPager+�ײ��л�(ImageView+TextView)
 *
 */
public abstract class BaseTabActivity extends FragmentActivity implements OnClickListener, OnPageChangeListener{
	private ViewPager viewPager;
	private List<View> tabs;
	private int[][] drawableResIdsColors;
	private int[] textColors;
	private ImageView[] imgs;
	private TextView[] textViews;
	public static int curIndex = -1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	/**
	 * ��ʼ��
	 * @param tabContainer
	 * @param fragments
	 */
	public  void init(ViewPager viewPager, ViewGroup tabContainer, List<Fragment> fragments, int[][] drawableResIdsColors, int[] textColors){
		this.viewPager = viewPager;
		this.drawableResIdsColors = drawableResIdsColors;
		this.textColors = textColors;
		tabs = new ArrayList<View>();
		imgs = new ImageView[tabContainer.getChildCount()];
		textViews = new TextView[tabContainer.getChildCount()];
		for (int i = 0; i < tabContainer.getChildCount(); i++) {
			ViewGroup viewGroup = (ViewGroup) tabContainer.getChildAt(i);
			for (int j = 0; j < viewGroup.getChildCount(); j++) {
				if(viewGroup.getChildAt(j) instanceof ImageView){
					imgs[i] = (ImageView) viewGroup.getChildAt(j);
					imgs[i].setImageResource(drawableResIdsColors[i][0]);
				}
				if(viewGroup.getChildAt(j) instanceof TextView){
					textViews[i] = (TextView) viewGroup.getChildAt(j);
					textViews[i].setTextColor(textColors[0]);
				}
			}
			tabs.add(viewGroup);
			tabs.get(i).setOnClickListener(this);
		}
		viewPager.setOnPageChangeListener(this);
		viewPager.setAdapter(new MainPagerFragmentAdapter(getSupportFragmentManager(), fragments));
	}

	@Override
	public void onClick(View view) {
		for (int i = 0; i < tabs.size(); i++) {
			if(tabs.get(i) == view){
				if(i == curIndex){
					return;
				}
				switchTab(i);
				break;
			}
		}
	}
	/**
	 * �л�tab��
	 * @param index
	 */
	protected void switchTab(int index) {
		if(index == curIndex){
			return;
		}
		imgs[index].setImageResource(drawableResIdsColors[index][1]);
		textViews[index].setTextColor(textColors[1]);
		if(curIndex != -1){
			imgs[curIndex].setImageResource(drawableResIdsColors[curIndex][0]);
			textViews[curIndex].setTextColor(textColors[0]);
		}
		curIndex = index;
		viewPager.setCurrentItem(index,false);
	}
	
	@Override
	public void onPageScrollStateChanged(int arg0) {
		
	}
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}
	@Override
	public void onPageSelected(int arg0) {
		switchTab(arg0);
	}
	public class MainPagerFragmentAdapter extends FragmentPagerAdapter{
		private List<Fragment> fragments;
		public MainPagerFragmentAdapter(FragmentManager fm,List<Fragment> fragments) {
			super(fm);
			this.fragments = fragments;
		}

		@Override
		public Fragment getItem(int arg0) {
			return fragments.get(arg0);
		}

		@Override
		public int getCount() {
			return fragments.size();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
		}
	}
}
