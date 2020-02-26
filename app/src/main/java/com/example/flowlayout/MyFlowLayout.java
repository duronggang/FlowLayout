package com.example.flowlayout;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class MyFlowLayout extends ViewGroup {

    private int mHorizontalSpacing = dp2px(16); //每个item横向间距
    private int mVerticalSpacing = dp2px(8); //每个item横向间距

    private List<List<View>> allLines; // 记录所有的行，一行一行的存储
    List<Integer> lineHeights = new ArrayList<>(); // 记录每一行的行高
    OnFLowclicklistener onFLowclicklistener;
    int viewposition = 0;
    int selectposition= -1;
    public MyFlowLayout(Context context) {
        super(context);
    }

    public MyFlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        initMeasureParams();
        //解析viewgroup 自己的宽高
        int selfwidth = MeasureSpec.getSize(widthMeasureSpec);
        int selfHight = MeasureSpec.getSize(heightMeasureSpec);

        //度量子view的大小
        int childviewCount = getChildCount();
        int paddingleft = getPaddingLeft();
        int paddingtop = getPaddingTop();
        int paddingright = getPaddingRight();
        int paddingbottom = getPaddingBottom();
        List<View> lineview = new ArrayList<>();
        int lineviewsize = 0;//记录一行已经使用了多大的宽度
        int lineheigh = 0;//一行的高
        int parentNeededWidth = 0;  // measure过程中，子View要求的父ViewGroup的宽
        int parentNeededHeight = 0; // measure过程中，子View要求的父ViewGroup的高
        for (int i = 0; i < childviewCount; i++) {
            View childview = getChildAt(i);
            LayoutParams childlayoutparams = childview.getLayoutParams();
            //根据父容器的measurespec 和 子view的layoutparams 综合获取子view的measurespec
            int childwithMeasurespec = getChildMeasureSpec(widthMeasureSpec, paddingleft + paddingright, childlayoutparams.width);
            int childheightMeasurespec = getChildMeasureSpec(heightMeasureSpec, paddingtop + paddingbottom, childlayoutparams.height);
            childview.measure(childwithMeasurespec, childheightMeasurespec);
            int chilecwidthmeaure = childview.getMeasuredWidth(); //获取子view测量后的宽高
            int childheightmeasure = childview.getMeasuredHeight();
            //通过宽度来判断是否需要换行，通过换行后的每行的行高来获取整个viewGroup的行高
            //如果需要换行

            if(chilecwidthmeaure+lineviewsize+mHorizontalSpacing >selfwidth){
                allLines.add(lineview);
                lineHeights.add(lineheigh);
                //一旦换行，我们就可以判断当前行需要的宽和高了，所以此时要记录下来
                parentNeededHeight = parentNeededHeight + lineheigh + mVerticalSpacing;
                parentNeededWidth = Math.max(parentNeededWidth, lineviewsize + mHorizontalSpacing);
                lineview = new ArrayList<>();
                lineviewsize = 0;
                lineheigh = 0;
            }
            // view 是分行layout的，所以要记录每一行有哪些view，这样可以方便layout布局
            lineview.add(childview);
            //每行都会有自己的宽和高
            lineviewsize = lineviewsize+chilecwidthmeaure+mHorizontalSpacing;
            lineheigh = Math.max(lineheigh,childheightmeasure);
            //如果当前childView是最后一行的最后一个
            if(i == getChildCount()-1){
                lineHeights.add(lineheigh);
                allLines.add(lineview);
                parentNeededWidth = Math.max(parentNeededWidth, lineviewsize);
                parentNeededHeight += lineheigh;
            }
        }

        //根据子View的度量结果，来重新度量自己ViewGroup
        // 作为一个ViewGroup，它自己也是一个View,它的大小也需要根据它的父亲给它提供的宽高来度量
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int realWidth = (widthMode == MeasureSpec.EXACTLY) ? selfwidth: parentNeededWidth;
        int realHeight = (heightMode == MeasureSpec.EXACTLY) ?selfHight: parentNeededHeight;

        setMeasuredDimension(realWidth,realHeight);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        viewposition = 0;
        int linenum = allLines.size();
        int cur =0;//当前view距离左边距
        int cut = 0;
        for(int m =0;m< linenum;m++){
            List<View> lineviewd = allLines.get(m);
            int lineHeight = lineHeights.get(m);

            for(int k =0;k<lineviewd.size();k++){
                viewposition++;
                View view = lineviewd.get(k);
                int left = cur;
                int top = cut;
                int right = left+view.getMeasuredWidth();
                int bottom = view.getMeasuredHeight()+cut;
                if(selectposition == viewposition){
                    view.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                }else {
                    view.setBackgroundResource(R.drawable.shape_button_circular);
                }
                view.layout(left,top,right,bottom);
                view.setTag(viewposition);
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onFLowclicklistener.onclicklistener((Integer) view.getTag(),view);
                    }
                });
                view.setOnLongClickListener(new OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        onFLowclicklistener.onLoneclicklistener((Integer) view.getTag(),view);
                        return true;
                    }
                });
                cur = right+mHorizontalSpacing;
            }
            cur = 0;
            cut = cut+lineHeight+mVerticalSpacing;
        }
    }

    public static int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }

    private void initMeasureParams() {
        allLines = new ArrayList<>();
        lineHeights = new ArrayList<>();
    }

    public void setLisetenr(OnFLowclicklistener onFLowclicklistener){
        this.onFLowclicklistener = onFLowclicklistener;
    }

    public void setSelectPosition(int selectposition){
        this.selectposition = selectposition;
    }
}
