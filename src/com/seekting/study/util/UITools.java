package com.seekting.study.util;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.widget.AbsListView;
import android.widget.HorizontalScrollView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

public class UITools { 
    
    /** 
     * ���ø�Ԫ�ص�Padding��ScrollView��ӵ��� 
     * @param scrollView 
     * @param padding 
     */
    public static void elasticPadding(final ScrollView scrollView, final int padding){ 
        View child = scrollView.getChildAt(0); 
        //��¼��ǰ��padding 
        final int oldpt = child.getPaddingTop(); 
        final int oldpb = child.getPaddingBottom(); 
        //�����µ�padding 
        child.setPadding(child.getPaddingLeft(), padding+oldpt, child.getPaddingRight(), padding+oldpb); 
  
        //�����ͼ��������¼����� 
        scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() { 
            private boolean inTouch = false; //��ָ�Ƿ���״̬ 
  
            @SuppressLint("NewApi") 
            private void disableOverScroll(){ 
                scrollView.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER); 
            } 
  
            /**  ���������� */
            private void scrollToTop(){ 
                scrollView.smoothScrollTo(scrollView.getScrollX(), padding-oldpt); 
            } 
  
            /** �������ײ� */
            private void scrollToBottom(){ 
                scrollView.smoothScrollTo(scrollView.getScrollX(), scrollView.getChildAt(0).getBottom()-scrollView.getMeasuredHeight()-padding+oldpb); 
            } 
  
            /** ���scrollView�����Ժ�,��ԭλ�� */
            private final Runnable checkStopped = new Runnable() { 
                @Override
                public void run() { 
                    int y = scrollView.getScrollY(); 
                    int bottom = scrollView.getChildAt(0).getBottom()-y-scrollView.getMeasuredHeight(); 
                    if(y <= padding && !inTouch){ 
                        scrollToTop(); 
                    }else if(bottom<=padding && !inTouch){ 
                        scrollToBottom(); 
                    } 
                } 
            }; 
  
            @SuppressWarnings("deprecation") 
            @Override
            public void onGlobalLayout() { 
                //�Ƴ������� 
                scrollView.getViewTreeObserver().removeGlobalOnLayoutListener(this); 
                //������С�߶� 
                //scrollView.getChildAt(0).setMinimumHeight(scrollView.getMeasuredHeight()); 
                //ȡ��overScrollЧ�� 
                if(Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD){ 
                    disableOverScroll(); 
                } 
  
                scrollView.setOnTouchListener(new OnTouchListener() { 
                    @Override
                    public boolean onTouch(View v, MotionEvent event) { 
                        if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_POINTER_DOWN){ 
                            inTouch = true; 
                        }else if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL){ 
                            inTouch = false; 
                            //��ָ�����Ժ���һ���Ƿ���Ҫ��ԭλ�� 
                            scrollView.post(checkStopped); 
                        } 
                        return false; 
                    } 
                }); 
  
                scrollView.getViewTreeObserver().addOnScrollChangedListener(new OnScrollChangedListener() { 
                    @Override
                    public void onScrollChanged() { 
                        scrollView.getHandler().removeCallbacks(checkStopped); 
                        //�����������,�Ƴ�checkStopped,ֹͣ�����Ժ�ִֻ��һ�μ������ 
                        if(!inTouch && scrollView!=null && scrollView.getHandler()!=null){ 
                            scrollView.postDelayed(checkStopped, 100); 
                        } 
                    } 
                }); 
  
                //��һ�μ�����ͼ,��ԭλ�� 
                scrollView.postDelayed(checkStopped, 300); 
            } 
        }); 
    } 
  
    /** 
     * ���ø�Ԫ�ص�Padding��HorizontalScrollView��ӵ��� 
     * @param scrollView 
     * @param padding 
     */
    public static void elasticPadding(final HorizontalScrollView scrollView, final int padding){ 
        View child = scrollView.getChildAt(0); 
  
        //��¼��ǰ��padding 
        final int oldpt = child.getPaddingTop(); 
        final int oldpb = child.getPaddingBottom(); 
        //�����µ�padding 
        child.setPadding(padding+oldpt, child.getPaddingTop(), padding+oldpb, child.getPaddingBottom()); 
  
        //�����ͼ��������¼����� 
        scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() { 
            private boolean inTouch = false; //��ָ�Ƿ���״̬ 
  
            @SuppressLint("NewApi") 
            private void disableOverScroll(){ 
                scrollView.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER); 
            } 
  
            /**  ��������� */
            private void scrollToLeft(){ 
                scrollView.smoothScrollTo(padding-oldpt, scrollView.getScrollY()); 
            } 
  
            /** �������ײ� */
            private void scrollToRight(){ 
                scrollView.smoothScrollTo(scrollView.getChildAt(0).getRight()-scrollView.getMeasuredWidth()-padding+oldpb, scrollView.getScrollY()); 
            } 
  
            /** ���scrollView�����Ժ�,��ԭλ�� */
            private final Runnable checkStopped = new Runnable() { 
                @Override
                public void run() { 
                    int x = scrollView.getScrollX(); 
                    int bottom = scrollView.getChildAt(0).getRight()-x-scrollView.getMeasuredWidth(); 
                    if(x <= padding && !inTouch){ 
                        scrollToLeft(); 
                    }else if(bottom<=padding && !inTouch){ 
                        scrollToRight(); 
                    } 
                } 
            }; 
  
            @SuppressWarnings("deprecation") 
            @Override
            public void onGlobalLayout() { 
                //�Ƴ������� 
                scrollView.getViewTreeObserver().removeGlobalOnLayoutListener(this); 
  
                //ȡ��overScrollЧ�� 
                if(Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD){ 
                    disableOverScroll(); 
                } 
  
                scrollView.setOnTouchListener(new OnTouchListener() { 
                    @Override
                    public boolean onTouch(View v, MotionEvent event) { 
                        if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_POINTER_DOWN){ 
                            inTouch = true; 
                        }else if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL){ 
                            inTouch = false; 
                            //��ָ�����Ժ���һ���Ƿ���Ҫ��ԭλ�� 
                            scrollView.post(checkStopped); 
                        } 
                        return false; 
                    } 
                }); 
  
                scrollView.getViewTreeObserver().addOnScrollChangedListener(new OnScrollChangedListener() { 
                    @Override
                    public void onScrollChanged() { 
                        //�����������,�Ƴ�checkStopped,ֹͣ�����Ժ�ִֻ��һ�μ������ 
                        scrollView.getHandler().removeCallbacks(checkStopped); 
                        if(!inTouch && scrollView!=null && scrollView.getHandler()!=null){ 
                            scrollView.postDelayed(checkStopped, 100); 
                        } 
                    } 
                }); 
  
                //��һ�μ�����ͼ,��ԭλ�� 
                scrollView.postDelayed(checkStopped, 300); 
            } 
        }); 
    } 
  
    public static void elasticListView(final ListView list, int padding){ 
        final TextView header = new TextView(list.getContext()); 
        final TextView footer = new TextView(list.getContext()); 
        header.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.FILL_PARENT, padding)); 
        footer.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.FILL_PARENT, padding)); 
        ListAdapter adapter = list.getAdapter(); 
        list.setAdapter(null); 
        list.addHeaderView(header); 
        list.addFooterView(footer); 
        list.setAdapter(adapter); 
  
        list.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() { 
            private boolean inTouch = false; //��ָ�Ƿ���״̬ 
  
            /** ���ListView�����Ժ�,��ԭλ�� */
            private final Runnable checkStopped = new Runnable() { 
                @Override
                public void run() { 
                    View child = list.getChildAt(0); 
                    if(child == header){ 
                        int by = child.getMeasuredHeight()+child.getTop(); 
                        list.smoothScrollBy(by, 500); 
                    }else{ 
                        child = list.getChildAt(list.getChildCount()-1); 
                        if(child == footer){ 
                            int by = child.getTop() - list.getMeasuredHeight() ; 
                            list.smoothScrollBy(by, 500); 
                        } 
                    } 
                } 
            }; 
  
            @Override
            public void onGlobalLayout() { 
                list.getViewTreeObserver().removeGlobalOnLayoutListener(this); 
                list.setOnTouchListener(new OnTouchListener() { 
                    @Override
                    public boolean onTouch(View v, MotionEvent event) { 
                        if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_POINTER_DOWN){ 
                            inTouch = true; 
                        }else if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL){ 
                            inTouch = false; 
                            //��ָ�����Ժ���һ���Ƿ���Ҫ��ԭλ�� 
                            list.postDelayed(checkStopped, 100); 
                        } 
                        return false; 
                    } 
                }); 
  
                list.getViewTreeObserver().addOnScrollChangedListener(new OnScrollChangedListener() { 
  
                    @Override
                    public void onScrollChanged() { 
                        list.getHandler().removeCallbacks(checkStopped); 
                        if(!inTouch){ 
                            list.postDelayed(checkStopped, 100); 
                        } 
                    } 
                }); 
            } 
        }); 
    } 
}