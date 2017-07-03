package com.ricochet.penetest;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String inputWebsite = "";
    TextView inforText1, inforText2, inforText3, vulnerText;
    ProgressBar progressBar1, progressBar2, progressBar3, progressBar0;
    Button vulnerRequest;
    EditText vulnerPort;
    WebView startWeb;
    MyReceiver br1, br2, br3, br4, br5,br6,br7;
    IntentFilter filter1, filter2, filter3, filter4, filter5, filter6, filter7;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private DrawerLayout drawer;
    private MyFragmentPagerAdapter adapter;
    static View v10,v20,v30,v11,v21,v31;
    static TextView v12,v22,v32;
    static EditText v33;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        br1 = new MyReceiver();
        br2 = new MyReceiver();
        br3 = new MyReceiver();
        br4 = new MyReceiver();
        br5 = new MyReceiver();
        br6 = new MyReceiver();
        br7 = new MyReceiver();
        filter1 = new IntentFilter("com.android.penetest.INFORMATION_BASIC"); // 和广播中Intent的action对应
        filter2 = new IntentFilter("com.android.penetest.INFORMATION_WHOIS");
        filter3 = new IntentFilter("com.android.penetest.INFORMATION_PORT");
        filter4 = new IntentFilter("com.android.penetest.VULNERABILITY");
        filter5 = new IntentFilter("com.android.penetest.ATTACK_PASSWORD");
        filter6 = new IntentFilter("com.android.penetest.ATTACK_XSS");
        filter7 = new IntentFilter("com.android.penetest.ATTACK_SQL");
        registerReceiver(br1, filter1);
        registerReceiver(br2, filter2);
        registerReceiver(br3, filter3);
        registerReceiver(br4, filter4);
        registerReceiver(br5, filter5);
        registerReceiver(br6, filter6);
        registerReceiver(br7, filter7);


        inforText1 = (TextView) findViewById(R.id.basic_infor);
        inforText2 = (TextView) findViewById(R.id.whois_infor);
        inforText3 = (TextView) findViewById(R.id.port_infor);
        progressBar0 = (ProgressBar) findViewById(R.id.start_process);
        progressBar1 = (ProgressBar) findViewById(R.id.basic_process);
        progressBar2 = (ProgressBar) findViewById(R.id.whois_process);
        progressBar3 = (ProgressBar) findViewById(R.id.port_process);
        startWeb = (WebView) findViewById(R.id.start_web);
        vulnerRequest = (Button) findViewById(R.id.vulner_request);
        vulnerPort = (EditText) findViewById(R.id.vulner_port);
        vulnerText = (TextView) findViewById(R.id.vulner_text);
        vulnerRequest.setHeight((int) (vulnerPort.getHeight() * 0.7));
        startWeb.getSettings().setUserAgentString("Mozilla/5.0 (Linux; U; Android 4.1.1; en-us; MI 2S Build/JRO03L) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30");
        vulnerText.setMovementMethod(ScrollingMovementMethod.getInstance());

        adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), this);
        mViewPager = (ViewPager) findViewById(R.id.container);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        inforText3.setMovementMethod(ScrollingMovementMethod.getInstance());



        //右下角浮动按钮
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("请输入");
                final EditText inputURL = new EditText(MainActivity.this);
                inputURL.setHint("例如：baidu.com");
                inputURL.setSingleLine(true);
                inputURL.setSelection(inputURL.getText().length());
                inputURL.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_URI);
                builder.setView(inputURL);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        View include_main = findViewById(R.id.include_main);
                        View include_start = findViewById(R.id.include_start);
                        if (new URLValidate(inputURL.getText().toString()).getResult()) {
                            include_main.setVisibility(View.GONE);
                            include_start.setVisibility(View.VISIBLE);
                            inputWebsite = inputURL.getText().toString();
                            MainActivity.this.getWindow().setTitle(inputWebsite);
                            toolbar.setTitle(inputWebsite);
                            progressBar0.setVisibility(View.VISIBLE);
                            startWeb.setVisibility(View.GONE);
                            startWeb.setWebViewClient(new WebViewClient() {
                                @Override
                                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                    //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                                    view.loadUrl(url);
                                    return true;
                                }

                                @Override
                                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                                    super.onPageStarted(view, url, favicon);
                                    progressBar0.setVisibility(View.VISIBLE);
                                    startWeb.setVisibility(View.GONE);
                                }

                                @Override
                                public void onPageFinished(WebView view, String url) {
                                    progressBar0.setVisibility(View.GONE);
                                    super.onPageFinished(view, url);
                                    startWeb.setVisibility(View.VISIBLE);
                                }
                            });
                            startWeb.loadUrl("http://" + inputWebsite);
                        } else {
                            Toast.makeText(MainActivity.this, "请输入正确域名", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                builder.setNegativeButton("取消", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        //左侧抽屉菜单
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // 发送请求
        vulnerRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("URI", inputWebsite);
                int port = 80;
                if (!vulnerPort.getText().toString().trim().equals("")) {
                    port = Integer.parseInt(vulnerPort.getText().toString());
                }
                intent.putExtra("PORT", port);
                intent.setClass(MainActivity.this, VulnerabilityRequest.class);
                MainActivity.this.startService(intent);
                Intent intentV2 = new Intent();
                intentV2.putExtra("URI", inputWebsite);
                intentV2.setClass(MainActivity.this, VulnerabilityRead.class);
                MainActivity.this.startService(intentV2);
            }
        });
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    //菜单切换功能
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        View content_main = findViewById(R.id.include_main);
        View content_start = findViewById(R.id.include_start);
        View content_information = findViewById(R.id.include_information);
        View content_vulnerability = findViewById(R.id.include_vulnerability);
        View content_attack = findViewById(R.id.include_attack);
        View content_declaration = findViewById(R.id.include_declaration);

        if (id == R.id.nav_start) {
            if (inputWebsite.trim().equals("")) {
                fab.setVisibility(View.VISIBLE);
                content_main.setVisibility(View.VISIBLE);
                content_start.setVisibility(View.GONE);
                content_information.setVisibility(View.GONE);
                content_vulnerability.setVisibility(View.GONE);
                content_attack.setVisibility(View.GONE);
                content_declaration.setVisibility(View.GONE);
            } else {
                fab.setVisibility(View.VISIBLE);
                content_main.setVisibility(View.GONE);
                content_start.setVisibility(View.VISIBLE);
                content_information.setVisibility(View.GONE);
                content_vulnerability.setVisibility(View.GONE);
                content_attack.setVisibility(View.GONE);
                content_declaration.setVisibility(View.GONE);
            }
        } else if (id == R.id.nav_information) {
            if (inputWebsite.trim().equals("")) {
                Toast.makeText(this, "请输入后再进行", Toast.LENGTH_SHORT).show();
            } else {
                fab.setVisibility(View.GONE);
                content_main.setVisibility(View.GONE);
                content_start.setVisibility(View.GONE);
                content_information.setVisibility(View.VISIBLE);
                content_vulnerability.setVisibility(View.GONE);
                content_attack.setVisibility(View.GONE);
                content_declaration.setVisibility(View.GONE);

                inforText1.setVisibility(View.GONE);
                progressBar1.setVisibility(View.VISIBLE);
                inforText2.setVisibility(View.GONE);
                progressBar2.setVisibility(View.VISIBLE);
                inforText3.setVisibility(View.GONE);
                progressBar3.setVisibility(View.VISIBLE);

                Intent intent1 = new Intent();
                intent1.putExtra("URI", inputWebsite);
                intent1.setClass(this, InformationBasic.class);
                this.startService(intent1);
                Intent intent2 = new Intent();
                intent2.putExtra("URI", inputWebsite);
                intent2.setClass(this, InformationWhois.class);
                this.startService(intent2);
                Intent intent3 = new Intent();
                intent3.putExtra("URI", inputWebsite);
                intent3.setClass(this, InformationPort.class);
                this.startService(intent3);
            }
        } else if (id == R.id.nav_vulnerability) {
            if (inputWebsite.trim().equals("")) {
                Toast.makeText(this, "请输入后再进行", Toast.LENGTH_SHORT).show();
            } else {
                fab.setVisibility(View.GONE);
                content_main.setVisibility(View.GONE);
                content_start.setVisibility(View.GONE);
                content_information.setVisibility(View.GONE);
                content_vulnerability.setVisibility(View.VISIBLE);
                content_attack.setVisibility(View.GONE);
                content_declaration.setVisibility(View.GONE);

            }
        } else if (id == R.id.nav_attack) {
            if (inputWebsite.trim().equals("")) {
                Toast.makeText(this, "请输入后再进行", Toast.LENGTH_SHORT).show();
            } else {
                fab.setVisibility(View.GONE);
                content_main.setVisibility(View.GONE);
                content_start.setVisibility(View.GONE);
                content_information.setVisibility(View.GONE);
                content_vulnerability.setVisibility(View.GONE);
                content_attack.setVisibility(View.VISIBLE);
                content_declaration.setVisibility(View.GONE);
            }
        } else if (id == R.id.nav_declaration) {
            fab.setVisibility(View.GONE);
            content_main.setVisibility(View.GONE);
            content_start.setVisibility(View.GONE);
            content_information.setVisibility(View.GONE);
            content_vulnerability.setVisibility(View.GONE);
            content_attack.setVisibility(View.GONE);
            content_declaration.setVisibility(View.VISIBLE);
        }else if (id == R.id.nav_quit) {
            MainActivity.this.finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    //广播处理
    class MyReceiver extends BroadcastReceiver {
        String init="";
        public void onReceive(Context context, Intent intent) {
            switch (intent.getIntExtra("id", 0)) {
                case 1:
                    inforText1.setText(intent.getStringExtra("URI"));
                    progressBar1.setVisibility(View.GONE);
                    inforText1.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    inforText2.setText(intent.getStringExtra("URI"));
                    progressBar2.setVisibility(View.GONE);
                    inforText2.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    inforText3.setText(intent.getStringExtra("URI"));
                    progressBar3.setVisibility(View.GONE);
                    inforText3.setVisibility(View.VISIBLE);
                    break;
                case 4:
                    vulnerText.setText(intent.getStringExtra("re"));
                    break;
                case 5:
                    v12.setText(intent.getStringExtra("re"));
                    break;
                case 6:
                    String link = intent.getStringExtra("re");
                    int a = intent.getIntExtra("status",404);
                    String status  = (a==200?"[成功]":"[失败]");
                    String text = init+"测试"+link+"  "+status+"\n";
                    v22.setText(text);
                    break;
                case 60:
                    int linkNum = intent.getIntExtra("num0",0);
                    String text0 = v22.getText().toString()+"共获得"+linkNum+"个超链接"+"\n"+"正在抓取表单..."+"\n";
                    v22.setText(text0);
                    break;
                case 61:
                    int formNum = intent.getIntExtra("num1",0);
                    String text1 = v22.getText().toString()+"共获得"+formNum+"个攻击点"+"\n"+"正在测试..."+"\n";
                    v22.setText(text1);
                    init =  text1;
                    break;
                case 62:
                    String text2 = init+"测试结束\n"+intent.getIntExtra("success",0)+"次请求发送成功\n"+intent.getIntExtra("failure",0)+"次请求发送失败\n请检查网页有无异常";
                    v22.setText(text2);
                    break;
                case 7:
                    //v33
                    v32.setText(intent.getStringExtra("re"));
                    break;
            }

        }
    }


    //退出时取消广播
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br1);
        unregisterReceiver(br2);
        unregisterReceiver(br3);
        unregisterReceiver(br4);
        unregisterReceiver(br5);
        unregisterReceiver(br6);
        unregisterReceiver(br7);
    }


    public static class PageFragment extends Fragment {
        private int mPage;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mPage = getArguments().getInt("page");
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = null;
            switch (mPage) {
                case 1:
                    view = inflater.inflate(R.layout.fragment_dos, container, false);
                    v10= view.findViewById(R.id.pass_start);
                    v11= view.findViewById(R.id.pass_run);
                    v12=(TextView) view.findViewById(R.id.pass_output);
                    break;
                case 2:
                    view = inflater.inflate(R.layout.fragment_xss, container, false);
                    v20= view.findViewById(R.id.xss_start);
                    v21= view.findViewById(R.id.xss_run);
                    v22=(TextView) view.findViewById(R.id.xss_output);
                    v22.setMovementMethod(ScrollingMovementMethod.getInstance());
                    break;
                case 3:
                    view = inflater.inflate(R.layout.fragment_sqlmap, container, false);
                    v30= view.findViewById(R.id.sql_start);
                    v31= view.findViewById(R.id.sql_run);
                    v32=(TextView) view.findViewById(R.id.sql_output);
                    v33=(EditText)view.findViewById(R.id.sql_link);
                    break;
            }
            return view;
        }

        public static PageFragment newInstance(int page) {
            Bundle bundle = new Bundle();
            bundle.putInt("page", page);
            PageFragment fragment = new PageFragment();
            fragment.setArguments(bundle);
            return fragment;
        }
    }

    class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        private String[] titles = new String[]{"DoS攻击", "XSS攻击", "sql注入"};

        public MyFragmentPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PageFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
    public void attack(View v){
        Intent intent1,intent2,intent3;
        switch (v.getId()) {
            case R.id.attack_pass:
                v10.setVisibility(View.GONE);
                v11.setVisibility(View.VISIBLE);
                intent1 = new Intent();
                intent1.putExtra("URI", inputWebsite);
                intent1.setClass(MainActivity.this, DOS.class);
                MainActivity.this.startService(intent1);
                break;
            case R.id.attack_xss:
                v20.setVisibility(View.GONE);
                v21.setVisibility(View.VISIBLE);
                intent2 = new Intent();
                intent2.putExtra("URI", inputWebsite);
                intent2.setClass(MainActivity.this, XSS.class);
                MainActivity.this.startService(intent2);
                v22.setText("开始测试\n正在抓取链接\n");
                break;
            case R.id.attack_sql:
                v30.setVisibility(View.GONE);
                v31.setVisibility(View.VISIBLE);
                intent3 = new Intent();
                intent3.putExtra("URI", v33.getText().toString());
                intent3.setClass(MainActivity.this, SQL.class);
                MainActivity.this.startService(intent3);
                break;
            case R.id.return_pass:
                v11.setVisibility(View.GONE);
                v10.setVisibility(View.VISIBLE);
                intent1=new Intent();
                intent1.setClass(MainActivity.this, DOS.class);
                MainActivity.this.stopService(intent1);
                break;
            case R.id.return_xss:
                v21.setVisibility(View.GONE);
                v20.setVisibility(View.VISIBLE);
                intent2 = new Intent();
                intent2.setClass(MainActivity.this, XSS.class);
                MainActivity.this.stopService(intent2);
                break;
            case R.id.return_sql:
                v31.setVisibility(View.GONE);
                v30.setVisibility(View.VISIBLE);
                intent3 = new Intent();
                intent3.setClass(MainActivity.this, SQL.class);
                MainActivity.this.stopService(intent3);
                break;
        }
    }


}
