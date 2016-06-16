package vn.appsmobi.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import vn.appsmobi.R;
import vn.appsmobi.utils.Constants;

public class SeeMoreActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView tv_full_text;
    private String text_content, text_name_card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_more);

        getData();
        initView();
        initAction();
    }

    private void getData() {
        Bundle extras = getIntent().getExtras();
        text_content = extras.getString(Constants.Intent.CARD_CONTENT);
        text_name_card = extras.getString(Constants.Intent.CARD_NAME);
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbarSeeMoreActivity);
        toolbar.setTitle(text_name_card);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        tv_full_text = (TextView) findViewById(R.id.tv_full_text);
        tv_full_text.setText(text_content);
    }

    private void initAction() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}
