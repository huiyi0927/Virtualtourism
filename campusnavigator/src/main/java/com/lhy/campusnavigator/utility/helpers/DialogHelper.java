package com.lhy.campusnavigator.utility.helpers;

import android.content.Context;

import com.lhy.campusnavigator.controller.PrivacyConfirmDialog;
import com.lhy.campusnavigator.controller.SpotSearchDialog;
import com.lhy.campusnavigator.model.Mode;
import com.lhy.campusnavigator.model.SpotProvider;
import com.lhy.campusnavigator.utility.interfaces.SingleSelectListener;
import com.lxj.xpopup.XPopup;

/**
 * @Description 对话框帮助类
 * @Author John
 * @email
 * @Date 2022/9/5 14:37
 * @Version 1
 */
public class DialogHelper {

    private DialogHelper() {
    }

    public static SpotSearchDialog buildSpotSearchDialog(Context context, Mode mode,
                                             SpotProvider provider, SingleSelectListener listener) {

        return (SpotSearchDialog) new XPopup.Builder(context)
                .asCustom(new SpotSearchDialog(
                        context, mode,
                        provider, listener));
    }

    public static void showPrivacyConfirmDialog(Context context) {
        new XPopup.Builder(context)
                .isDestroyOnDismiss(true)
                .asCustom(new PrivacyConfirmDialog(context))
                .show();
    }
}
