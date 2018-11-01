package com.zhaoqy.app.demo.camera.qrcode.camera;

import com.google.zxing.ResultPoint;
import com.google.zxing.ResultPointCallback;
import com.zhaoqy.app.demo.camera.qrcode.view.QRCodeScanView;

public final class ViewfinderResultPointCallback implements ResultPointCallback 
{
	private final QRCodeScanView viewfinderView;

	public ViewfinderResultPointCallback(QRCodeScanView viewfinderView) 
	{
		this.viewfinderView = viewfinderView;
	}

	public void foundPossibleResultPoint(ResultPoint point) 
	{
		viewfinderView.addPossibleResultPoint(point);
	}
}
