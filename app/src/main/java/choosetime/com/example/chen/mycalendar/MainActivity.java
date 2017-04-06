package choosetime.com.example.chen.mycalendar;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import choosetime.com.example.chen.mycalendar.calendar.fragment.MyCalendarFragment;
import choosetime.com.example.chen.mycalendar.calendar.fragment.widget.HandMoveLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private MyCalendarFragment mMyCalenderFragment;
    private TextView mToolbar;
    private HandMoveLayout mHandMoveLayout;
    private int mCount ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        mNumMap = new HashMap<>();
        mToolbar = (TextView) findViewById(R.id.toolbar);
        mToolbar.setOnClickListener(this);
        loadData();


        mMyCalenderFragment = new MyCalendarFragment(new Handler(){
            @Override
            public void handleMessage(Message msg) {

                super.handleMessage(msg);
            }
        });
        //re_cotent
        getSupportFragmentManager().beginTransaction().add(R.id.re_cotent, mMyCalenderFragment)
                .show(mMyCalenderFragment).commit();
    }

    private void loadData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar:
                mCount++;
                mHandMoveLayout = (HandMoveLayout) mMyCalenderFragment.getView().findViewById(R.id.handmovelayout);
                if (mCount % 2 == 1) {
                    mHandMoveLayout.setWeekSelected(false);
                }else{
                    mHandMoveLayout.collapse();
                }
//                mHandMoveLayout.collapse();
                break;
        }
    }
}
