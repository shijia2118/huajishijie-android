package com.test.tworldapplication.inter;

import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.IDCardResult;

/**
 * Created by dasiy on 16/10/14.
 */
public interface BaiduOcrScanResult {
    void onSuccess(IDCardResult result);



    void onError(String errorMsg);
}
