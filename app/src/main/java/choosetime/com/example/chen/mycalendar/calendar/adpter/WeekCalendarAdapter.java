package choosetime.com.example.chen.mycalendar.calendar.adpter;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import choosetime.com.example.chen.mycalendar.R;
import choosetime.com.example.chen.mycalendar.calendar.CalendarUtil;
import choosetime.com.example.chen.mycalendar.calendar.fragment.MyCalendarFragment;
import choosetime.com.example.chen.mycalendar.calendar.utils.DateUtils;

/**
 * Created by Administrator on 2016/1/16 0016.
 */
public class WeekCalendarAdapter extends CalendarBaseAdapter {
    private List<View> views;
    private Context context;

    private Handler os = null;

    private Drawable yuanOfRed;
    private Drawable white;
    private int text_black;
    private int last_msg_tv_color;
    private int text_white;
    private final Drawable yuanOfBlack;
    private ArrayList<String> list = new ArrayList<>();
    private String strToday = "";

    private Map<String,String> mNumMap;//患者人数


    public WeekCalendarAdapter(List<View> views, Context context, ArrayList<String> list, Map<String,String> map) {
        this.list = list;
        this.views = views;
        this.context = context;
        //选中今天
        Calendar today = new GregorianCalendar();
        today.setTimeInMillis(System.currentTimeMillis());

        strToday = DateUtils.getTagTimeStr(today);

        selectTime = DateUtils.getTagTimeStr(today);
        last_msg_tv_color = context.getResources().getColor(R.color.last_msg_tv_color);
        text_black = context.getResources().getColor(R.color.white_deep);
        text_white = context.getResources().getColor(R.color.white);
        yuanOfRed = context.getResources().getDrawable(R.drawable.bg_calendar_yuan);
        yuanOfBlack = context.getResources().getDrawable(R.drawable.icon_calendar_background);
        white = context.getResources().getDrawable(R.drawable.bg_canendar_last_item_white);//这是被选中后前一个选中的背景图形


        mNumMap = map;
    }

    public void setHandler(Handler os) {
        this.os = os;
    }

    @Override
    public int getCount() {
        return 9600;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        container.removeView(views.get(position % views.size()));
    }

    public void getTimeList(ArrayList<String> list) {
        this.list = list;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        ViewGroup view = (ViewGroup) views.get(position % views.size());
        int index = container.indexOfChild(view);
        if (index != -1) {
            container.removeView(view);
        }
        try {
            container.addView(view);
        } catch (Exception e) {

        }

        //给view 填充内容

        //设置开始时间为本周日
        Calendar today = new GregorianCalendar();
        today.setTimeInMillis(System.currentTimeMillis());
        int day_of_week = today.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0) {
            day_of_week = 7;
        }
        today.add(Calendar.DATE, -day_of_week);
        //距离当前时间的周数
        int week = getCount() / 2 - position;
        today.add(Calendar.DATE, -week * 7);

        render(view, today);
        return view;
    }

    /**
     * 渲染page中的view：7天
     */
    private void render(final ViewGroup view, final Calendar today) {
        for (int a = 0; a < 7; a++) {
            final int dayOfMonth = today.get(Calendar.DAY_OF_MONTH);
//             int day_of_year=today.get(Calendar.DAY_OF_YEAR);
            final ViewGroup dayOfWeek = (ViewGroup) view.getChildAt(a);
            //((TextView) dayOfWeek.getChildAt(0)).setText(getStr(today.get(Calendar.DAY_OF_WEEK)));
            ((TextView) dayOfWeek.findViewById(R.id.gongli)).setText(dayOfMonth + "");
            String str = "";
            try {
                str = new CalendarUtil().getChineseDay(today.get(Calendar.YEAR),
                        today.get(Calendar.MONTH) + 1, today.get(Calendar.DAY_OF_MONTH));
            } catch (Exception e) {

            }


            if (str.equals("初一")) {//如果是初一，显示月份
                str = new CalendarUtil().getChineseMonth(today.get(Calendar.YEAR),
                        today.get(Calendar.MONTH) + 1, today.get(Calendar.DAY_OF_MONTH));
            }



            dayOfWeek.setTag(DateUtils.getTagTimeStr(today));
            String ThisWeek = dayOfWeek.getTag().toString();//这一周的日子，可以算病人
            String num = mNumMap.get(ThisWeek);//得到病人
            if (! "".equals(num)) {
                //TODO:获取当前农历下的字
                ((TextView) dayOfWeek.findViewById(R.id.nongli)).setText(num);
                Log.i("hehe","dayOfMonth"+ dayOfMonth );
                Log.i("hehe","dayOfMonth"+ ThisWeek+"@@@"+num );
            }
            dayOfWeek.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    is = true;
                    //TODO:发消息，告诉Activity我改变选中的日期了
                    if (WeekCalendarAdapter.this.os != null) {
//                        os.sendEmptyMessage(MyCalendarFragment.change);


                        selectTime = dayOfWeek.getTag().toString();

                        Message msg = Message.obtain();
                        msg.what = MyCalendarFragment.change;
                        Bundle bundle = new Bundle();
                        bundle.putString("time",selectTime);
                        msg.setData(bundle);
                        os.sendMessage(msg);
                    }
//                    selectTime = dayOfWeek.getTag().toString();
//                    Toast.makeText(context, "select"+selectTime, Toast.LENGTH_SHORT).show();


                    today.add(Calendar.DATE, -7);//因为已经渲染过7天，所以today往前推7天， 代表当前page重绘；

                    //界面特效：变为红色，执行动画
                    dayOfWeek.findViewById(R.id.cal_container).setBackgroundDrawable(yuanOfRed);
                    ((TextView) dayOfWeek.findViewById(R.id.gongli)).setTextColor(text_white);
                    ((TextView) dayOfWeek.findViewById(R.id.nongli)).setTextColor(text_white);

                    Animator anim = AnimatorInflater.loadAnimator(context, R.anim.alpha_calendar_select_bg_soufang);

                    //显示的调用invalidate
                    dayOfWeek.invalidate();
                    anim.setTarget(dayOfWeek);
                    anim.start();
                    //添加监听：动画开始时，恢复上个选中的day的状态，结束时执行刷新方法;

                    //将上一个选中的day的状态恢复
                    if (day != null) {
                        day.findViewById(R.id.cal_container).setBackgroundDrawable(yuanOfRed);//这是被选中后前一个选中的背景图形
                        ((TextView) day.findViewById(R.id.gongli)).setTextColor(text_black);
                        ((TextView) day.findViewById(R.id.nongli)).setTextColor(last_msg_tv_color);
                        //特殊情况:上个选中的day今天
                        if (strToday.equals(tag)) {
                            day.findViewById(R.id.cal_container).setBackgroundDrawable(yuanOfBlack);
                            ((TextView) dayOfWeek.findViewById(R.id.gongli)).setTextColor(text_black);
                            ((TextView) day.findViewById(R.id.nongli)).setTextColor(last_msg_tv_color);
                        } else {
                            day.findViewById(R.id.cal_container).setBackgroundDrawable(yuanOfRed);//这是被选中后前一个选中的背景图形
                            ((TextView) day.findViewById(R.id.gongli)).setTextColor(text_black);
                            ((TextView) day.findViewById(R.id.nongli)).setTextColor(last_msg_tv_color);
                        }
                    }

                    anim.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            render(view, today);
                            is = false;
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });

                }
            });
            if (strToday.equals(DateUtils.getTagTimeStr(today))) {
                dayOfWeek.findViewById(R.id.cal_container).setBackgroundDrawable(yuanOfBlack);
                ((TextView) dayOfWeek.findViewById(R.id.gongli)).setTextColor(text_black);
                ((TextView) dayOfWeek.findViewById(R.id.nongli)).setTextColor(last_msg_tv_color);
                if (!selectTime.equals(strToday)) {
                    today.add(Calendar.DATE, 1);
                    continue;
                }
            } else {
                dayOfWeek.findViewById(R.id.cal_container).setBackgroundDrawable(white);
                ((TextView) dayOfWeek.findViewById(R.id.gongli)).setTextColor(text_black);
                ((TextView) dayOfWeek.findViewById(R.id.nongli)).setTextColor(last_msg_tv_color);
            }
            if (selectTime.equals(DateUtils.getTagTimeStr(today))) {

                dayOfWeek.findViewById(R.id.cal_container).setBackgroundDrawable(yuanOfRed);
                ((TextView) dayOfWeek.findViewById(R.id.gongli)).setTextColor(text_white);
                ((TextView) dayOfWeek.findViewById(R.id.nongli)).setTextColor(text_white);


                day = dayOfWeek;
                tag = selectTime;
            } else {
                //TODO:这是本周未选中的颜色
                dayOfWeek.findViewById(R.id.cal_container).setBackgroundColor(context.getResources().getColor(R.color.base_bg_color_blue));
                ((TextView) dayOfWeek.findViewById(R.id.gongli)).setTextColor(text_black);
                ((TextView) dayOfWeek.findViewById(R.id.nongli)).setTextColor(last_msg_tv_color);
            }
            if (list.contains(DateUtils.getTagTimeStr(today))) {
                ((ImageView) dayOfWeek.findViewById(R.id.imv_point)).setVisibility(View.VISIBLE);
                ((ImageView) dayOfWeek.findViewById(R.id.imv_point)).setImageResource(R.drawable.calendar_item_point);
            } else {
                ((ImageView) dayOfWeek.findViewById(R.id.imv_point)).setVisibility(View.INVISIBLE);
            }

            today.add(Calendar.DATE, 1);
        }
    }

    private ViewGroup day = null;
    private String tag = "";

    public String getSelectTime() {
        return selectTime;
    }

    public void setSelectTime(String selectTime) {
        this.selectTime = selectTime;
    }

}
