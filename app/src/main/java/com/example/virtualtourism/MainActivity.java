package com.example.virtualtourism;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;


public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();


    private Marker locationMarker;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    //定位成功回调信息，设置相关消息

                    //取出经纬度
                    LatLng latLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());

                    //添加Marker显示定位位置
                    if (locationMarker == null) {
                        //如果是空的添加一个新的,icon方法就是设置定位图标，可以自定义
                        locationMarker = aMap.addMarker(new MarkerOptions()
                                .position(latLng));
                    } else {
                        //已经添加过了，修改位置即可
                        locationMarker.setPosition(latLng);
                    }

                    //然后可以移动到定位点,使用animateCamera就有动画效果
                    aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));

                } else {
                    //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }
            }
        }
    };

    private MapView mMapView;
    private AMap aMap = null;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //对象的实例创建，反射，Class就可以创建一个实例
        mButton = findViewById(R.id.bt_location);


//        RelativeLayout relativeLayout = new RelativeLayout();


        //先进行权限申请情况判断，没有权限申请权限
        if (lacksPermission(permissions)) {
            ActivityCompat.requestPermissions(this, permissions, OPEN_SET_REQUEST_CODE);
        }

//        获取地图控件引用
        mMapView = (MapView) findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mMapView.getMap();
        }

        aMap.setMyLocationEnabled(true);// 可触发定位并显示当前位置

        aMap.showIndoorMap(true);  //true：显示室内地图；false：不显示；
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        //初始化定位蓝点样式类
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。

        Log.d(TAG, "onCreate isMyLocationShowing: " + myLocationStyle.isMyLocationShowing());
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        Log.d(TAG, "onCreate isMyLocationShowing111: " + aMap.getMyLocationStyle().isMyLocationShowing());
        aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));

        try {
            //初始化定位
            mLocationClient = new AMapLocationClient(getApplicationContext());
            mLocationClient.setLocationListener(mLocationListener);
            AMapLocationClientOption option = new AMapLocationClientOption();
            //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
            option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            option.setMockEnable(false);
            mLocationClient.setLocationOption(option);


            mLocationClient.startLocation();


        } catch (Exception e) {
            e.printStackTrace();
        }

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLocationClient.startLocation();

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }


    /**
     * 定位需要的权限
     */
    private String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION
            , Manifest.permission.ACCESS_COARSE_LOCATION
            , Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS};
    private static final int OPEN_SET_REQUEST_CODE = 100;


    //判断是否获取权限
    public boolean lacksPermission(String[] permissions) {
        for (String permission : permissions) {
            //判断是否缺少权限，true=缺少权限
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }
        return false;
    }

}