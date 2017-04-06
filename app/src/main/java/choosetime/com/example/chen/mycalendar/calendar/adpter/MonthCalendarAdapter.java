package choosetime.com.example.chen.mycalendar.calendar.adpter;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Context;
import android.graphics.Color;
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
 * Created by Administrator on 2016/5/19 0019.
 */
public class MonthCalendarAdapter extends CalendarBaseAdapter {
    private List<View> views;
    private Context context;

    private Handler os = null;
    private int last_msg_tv_color;
    private Drawable yuanOfRed;
    private Drawable white;
    private int text_black;
    private int text_white;
    private final Drawable yuanOfBlack;

    private String strToDay = "";
    private ArrayList<String> list = new ArrayList<>();


    private Map<String,String> mNumMap;//患者人数


    public MonthCalendarAdapter(List<View> views, Context context, ArrayList<String> list, Map<String,String> map) {
        this.views = views;
        this.context = context;
        this.list = list;
        //选中今天
        Calendar today = new GregorianCalendar();
        today.setTimeInMillis(System.currentTimeMillis());

        strToDay = DateUtils.getTagTimeStr(today);

        selectTime = DateUtils.getTagTimeStr(today);
        text_black = context.getResources().getColor(R.color.white_deep);
        last_msg_tv_color = context.getResources().getColor(R.color.last_msg_tv_color);
        text_white = context.getResources().getColor(R.color.white);
        yuanOfRed = context.getResources().getDrawable(R.drawable.bg_calendar_yuan);//这是当前天的圆形
        yuanOfBlack = context.getResources().getDrawable(R.drawable.icon_calendar_background);//当天圆环的样式，本来是红色环形
        white = context.getResources().getDrawable(R.drawable.bg_canendar_last_item_white);//这是被选中后前一个选中的背景图形



        mNumMap = map;

    }

    public void setHandler(Handler os) {
        this.os = os;
    }

    @Override
    public int getCount() {
        return 2400;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
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
        refresh(view, position, list);
        return view;
    }

    public void getTimeList(ArrayList<String> list) {
        this.list = list;
    }

    /**
     * 提供对外的刷新接口
     */
    public void refresh(ViewGroup view, int position, ArrayList<String> list) {
        //给view 填充内容

        //设置开始时间为本周日
        Calendar today = new GregorianCalendar();
        today.setTimeInMillis(System.currentTimeMillis());
        //距离当前时间的月数
        int month = 1200 - position;
        today.add(Calendar.MONTH, -month);
        view.setTag(today.get(Calendar.MONTH) + "");
        //找到这个月的第一天所在星期的周日
        today.add(Calendar.DAY_OF_MONTH, -(today.get(Calendar.DAY_OF_MONTH) - 1));


        int day_of_week = today.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0) {
            day_of_week = 7;
        }
        today.add(Calendar.DATE, -day_of_week);

        render(view, today);
    }

    /**
     * 渲染page中的view：7天
     */
    private void render(final ViewGroup view1, final Calendar today) {
        //一页显示一个月+7天，为42；
        for (int b = 0; b < 6; b++) {
            final ViewGroup view = (ViewGroup) view1.getChildAt(b);
            for (int a = 0; a < 7; a++) {
                final int dayOfMonth = today.get(Calendar.DAY_OF_MONTH);
                // int day_of_year=today.get(Calendar.DAY_OF_YEAR);
                final ViewGroup dayOfWeek = (ViewGroup) view.getChildAt(a);
                //((TextView) dayOfWeek.getChildAt(0)).setText(getStr(today.get(Calendar.DAY_OF_WEEK)));
                ((TextView) dayOfWeek.findViewById(R.id.gongli)).setText(dayOfMonth + "");
                String str = "";
                try {
                    str = new CalendarUtil().getChineseDay(today.get(Calendar.YEAR),
                            today.get(Calendar.MONTH) + 1, today.get(Calendar.DAY_OF_MONTH));
                } catch (Exception e) {

                }
                ((ImageView) dayOfWeek.findViewById(R.id.imv_point)).setVisibility(View.INVISIBLE);
//                String str =



                if (str.equals("初一")) {//如果是初一，显示月份
                    str = new CalendarUtil().getChineseMonth(today.get(Calendar.YEAR),
                            today.get(Calendar.MONTH) + 1, today.get(Calendar.DAY_OF_MONTH));
                }
                //TODO:设置下面人数
//                ((TextView) dayOfWeek.findViewById(R.id.nongli)).setText(str);

                Log.e("hehe2",dayOfMonth+"");


                if (list.contains(DateUtils.getTagTimeStr(today))) {
                    ((ImageView) dayOfWeek.findViewById(R.id.imv_point)).setVisibility(View.VISIBLE);
                    ((ImageView) dayOfWeek.findViewById(R.id.imv_point)).setImageResource(R.drawable.calendar_item_point);
                } else {
                    ((ImageView) dayOfWeek.findViewById(R.id.imv_point)).setVisibility(View.INVISIBLE);
                }
                dayOfWeek.setTag(DateUtils.getTagTimeStr(today));


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
                        if (MonthCalendarAdapter.this.os != null) {
//                            os.sendEmptyMessage(MyCalendarFragment.change);

                            selectTime = dayOfWeek.getTag().toString();

                            Message msg = Message.obtain();
                            msg.what = MyCalendarFragment.change;
                            Bundle bundle = new Bundle();
                            bundle.putString("time",selectTime);
                            msg.setData(bundle);
                            os.sendMessage(msg);
                        }


                      //  Toast.makeText(context, "select"+selectTime, Toast.LENGTH_SHORT).show();
                        today.add(Calendar.DATE, -42);//因为已经渲染过42天，所以today往前推42天， 代表当前page重绘；

                        //TODO：这是画背景的
                        //恢复上个选中的day的状态
                        if (day != null) {
                            day.findViewById(R.id.cal_container).setBackgroundColor(Color.BLUE);
//                            day.findViewById(R.id.cal_container).setBackgroundDrawable(bg_canendar_last_item_white);
                            ((TextView)day.findViewById(R.id.gongli)).setTextColor(text_black);
                            //特殊情况
                            if (strToDay.equals(tag)) {
                                day.findViewById(R.id.cal_container).setBackgroundDrawable(yuanOfBlack);
                                ((TextView) day.findViewById(R.id.gongli)).setTextColor(text_black);
                                ((TextView) day.findViewById(R.id.nongli)).setTextColor(last_msg_tv_color);

                            } else {
                                day.findViewById(R.id.cal_container).setBackgroundDrawable(yuanOfRed);//这是被选中后前一个选中的背景图形
//                                day.findViewById(R.id.cal_container).setBackgroundColor(Color.BLUE);
                                ((TextView) day.findViewById(R.id.gongli)).setTextColor(text_black);
                                ((TextView) day.findViewById(R.id.nongli)).setTextColor(last_msg_tv_color);
                            }
                        }
                        //TODO：当天背景圆形变为红色
//                        dayOfWeek.findViewById(R.id.cal_container).setBackgroundDrawable(yuanOfRed);
                        ((TextView) dayOfWeek.findViewById(R.id.gongli)).setTextColor(text_white);
                        ((TextView) dayOfWeek.findViewById(R.id.nongli)).setTextColor(text_white);

                        Animator anim = AnimatorInflater.loadAnimator(context, R.anim.alpha_calendar_select_bg_soufang);
                        //显示的调用invalidate
                        dayOfWeek.invalidate();
                        anim.setTarget(dayOfWeek);
                        anim.start();
                        //  添加监听：动画结束时执行刷新方法;

                        anim.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                render(view1, today);
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
                if (strToDay.equals(DateUtils.getTagTimeStr(today))) {
//                    dayOfWeek.findViewById(R.id.cal_container).setBackgroundDrawable(yuanOfBlack);//今天画个背景圆圈
                    ((TextView) dayOfWeek.findViewById(R.id.gongli)).setTextColor(text_black);
                    ((TextView) dayOfWeek.findViewById(R.id.nongli)).setTextColor(last_msg_tv_color);
                    if (!selectTime.equals(strToDay)) {
                        today.add(Calendar.DATE, 1);
                        continue;
                    }
                } else {//TODO：这是不在本月的文本绘制

                    dayOfWeek.findViewById(R.id.cal_container).setBackgroundColor(context.getResources().getColor(R.color.base_bg_color_blue));
//                    dayOfWeek.findViewById(R.id.cal_container).setBackgroundDrawable(bg_canendar_last_item_white);
                    ((TextView) dayOfWeek.findViewById(R.id.gongli)).setTextColor(text_black);
//                    ((TextView) dayOfWeek.findViewById(R.id.gongli)).setTextColor(Color.WHITE);//写成白色这样就看不到其他天数的背景
                }
                //不是当前月的显示为灰色
                if ((Integer.parseInt((String) view1.getTag())) != today.get(Calendar.MONTH)) {
//                    ((TextView) dayOfWeek.findViewById(R.id.gongli)).setTextColor(last_msg_tv_color);
                    ((TextView) dayOfWeek.findViewById(R.id.gongli)).setTextColor(context.getResources().getColor(R.color.base_bg_color_blue));
                    ((TextView) dayOfWeek.findViewById(R.id.nongli)).setTextColor(context.getResources().getColor(R.color.base_bg_color_blue));
                    if ((Integer.parseInt((String) view1.getTag())) > today.get(Calendar.MONTH)) {
                        //下个月
                        dayOfWeek.setOnClickListener(lastLister);
                    } else {
                        //上个月
                        dayOfWeek.setOnClickListener(nextLister);
                    }
                    today.add(Calendar.DATE, 1);
                    continue;
                } else {
                    dayOfWeek.setAlpha(1.0f);
                }
                //如果是选中天的话显示为红色
                if (selectTime.equals(DateUtils.getTagTimeStr(today))) {

                    dayOfWeek.findViewById(R.id.cal_container).setBackgroundDrawable(yuanOfRed);//选中日子的背景
                    ((TextView) dayOfWeek.findViewById(R.id.gongli)).setTextColor(text_white);
                    ((TextView) dayOfWeek.findViewById(R.id.nongli)).setTextColor(text_white);

                    if (strToDay.equals(DateUtils.getTagTimeStr(today))) {
                        dayOfWeek.findViewById(R.id.cal_container).setBackgroundDrawable(yuanOfRed);//当前时间的背景圆形
                        ((TextView) dayOfWeek.findViewById(R.id.gongli)).setTextColor(context.getResources().getColor(R.color.red_fe6c6b));//TODO:这是当天时间的文字内容，目前我把它设为粉红色
                        ((TextView) dayOfWeek.findViewById(R.id.nongli)).setTextColor(text_white);
                    }

                    day = dayOfWeek;
                    if (MonthCalendarAdapter.this.os != null) {
                        Message message = os.obtainMessage();
                        message.obj = b;
                        message.what = MyCalendarFragment.change2;
                        os.sendMessage(message);
                    }
                    tag = selectTime;
                } else {
                    //TODO:这是当月其他天的背景颜色
                    dayOfWeek.findViewById(R.id.cal_container).setBackgroundColor(context.getResources().getColor(R.color.base_bg_color_blue));
//                    dayOfWeek.findViewById(R.id.cal_container).setBackgroundDrawable(bg_canendar_last_item_white);
                    ((TextView) dayOfWeek.findViewById(R.id.gongli)).setTextColor(text_black);
                    ((TextView) dayOfWeek.findViewById(R.id.nongli)).setTextColor(last_msg_tv_color);
                }
                today.add(Calendar.DATE, 1);
            }
        }

    }

    View.OnClickListener nextLister = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (MonthCalendarAdapter.this.os != null) {
                Message message = os.obtainMessage();
                message.what = MyCalendarFragment.pagerNext;
                os.sendMessage(message);
            }
        }
    };
    View.OnClickListener lastLister = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (MonthCalendarAdapter.this.os != null) {
                Message message = os.obtainMessage();
                message.what = MyCalendarFragment.pagerLast;
                os.sendMessage(message);
            }
        }
    };

    private ViewGroup day = null;

    public ViewGroup getDay() {
        return day;
    }

    private String tag = "";

    public String getSelectTime() {
        return selectTime;
    }

    public void setSelectTime(String selectTime) {
        this.selectTime = selectTime;
    }

}
