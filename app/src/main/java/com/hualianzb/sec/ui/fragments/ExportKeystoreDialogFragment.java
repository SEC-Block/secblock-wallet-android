package com.hualianzb.sec.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.hualianzb.sec.R;
import com.hualianzb.sec.commons.constants.Constant;
import com.hualianzb.sec.models.RememberSEC;
import com.hualianzb.sec.ui.activitys.MakeMoneyActicity;
import com.hualianzb.sec.ui.basic.BaseDialogFragment;
import com.hysd.android.platform_huanuo.base.config.PlatformConfig;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Date:2019/1/5
 * auther:wangtianyun
 * 右边DialogFragment
 */
public class ExportKeystoreDialogFragment extends BaseDialogFragment {
    String address;
    @BindView(R.id.tv_keystore)
    TextView tv_keystore;
    Unbinder unbinder;
    private ExportLinster listterner;
    private RememberSEC bean;

    // 定义activity必须实现的接口方法
    public interface ExportLinster {
        void copyKeyStore(String sssd);
    }

    // 当FRagmen被加载到activity的时候会被回调
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof QrCodeDialogFragment.InfacnCopy) {
            listterner = (ExportLinster) activity; // 2.2 获取到宿主activity并赋值
        } else {
            throw new IllegalArgumentException("activity must implements ExportLinster");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        address = getArguments().getString("address");
        Map<String, RememberSEC> map = PlatformConfig.getMap(Constant.SpConstant.WALLET);
        for (RememberSEC rememberEth : map.values()) {
            if (address.equals(rememberEth.getAddress())) {
                bean = rememberEth;
                break;
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mWindow.setGravity(Gravity.TOP | Gravity.END);
        mWindow.setWindowAnimations(R.style.RightDialog);
        mWindow.setLayout(mWidth * 5 / 7, mHeight -
                ImmersionBar.getNavigationBarHeight(getActivity())
                - ImmersionBar.getStatusBarHeight(getActivity()));
    }

    @Override
    protected int setLayoutId() {
        return R.layout.layout_dialog_export_keystore;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        ImmersionBar.with(this)
                .statusBarColor(R.color.white).
                statusBarDarkFont(true).init();
    }

    @Override
    protected void initData() {
        super.initData();
        tv_keystore.setText(bean.getWalletFile());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((MakeMoneyActicity) getActivity()).getActivityImmersionBar().keyboardEnable(true).init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnClick({R.id.tv_close, R.id.tv_copy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_close:
                getDialog().dismiss();
                break;
            case R.id.tv_copy:
                listterner.copyKeyStore("1");
                getDialog().dismiss();
                break;
        }
    }

    //把传递进来的activity对象释放掉
    @Override
    public void onDetach() {
        super.onDetach();
        listterner = null;
    }
}