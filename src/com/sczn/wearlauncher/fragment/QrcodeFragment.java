package com.sczn.wearlauncher.fragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Hashtable;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.receivers.AbsBroadcastReceiver;
import com.sczn.wearlauncher.util.MxyLog;
import com.sczn.wearlauncher.util.SharedPreferencesUtils;
import com.sczn.wearlauncher.util.SysServices;


public class QrcodeFragment extends absDialogFragment{
	private static final String TAG = QrcodeFragment.class.getSimpleName();
	
	private static QrcodeFragment instance;
	public static QrcodeFragment getInstance(){
		if(instance == null){
			synchronized (QrcodeFragment.class) {
				instance = new QrcodeFragment();
			}
		}
		return instance;
	}
	private QrcodeFragment(){
		super();
		mQrcodeRceiver = new QrcodeRceiver();
		mQrcodeTask = new QrcodeTask();
	}
	
	public static final String QRCODE_IMAGE_NAME = "QrcodeImg";
	public static final String PREFERENCE_KEY_INDEX = "qrcode_index";
	
	public static final int QRCODE_STATE_PREPARING= 0;
	public static final int QRCODE_STATE_VALUE = 1;
	public static final int QRCODE_STATE_ERROR= 2;
	public static final int QRCODE_STATE_UNVALUE = 3;
	
	private int qrcodeState = QRCODE_STATE_PREPARING;
	private ImageView mQrcodeImage;
	private TextView mQrcodeText;
	private QrcodeRceiver mQrcodeRceiver;
	private QrcodeTask mQrcodeTask;
	
	
	@Override
	protected int getLayoutResouceId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_status_qrcode;
	}
	@Override
	protected void creatView() {
		// TODO Auto-generated method stub
		mQrcodeImage = findViewById(R.id.qrcode_image);
		mQrcodeText = findViewById(R.id.qrcode_text);
		
		mRootView.setOnClickListener(onRootClick);
		initQrcode();
		mQrcodeRceiver.register(getActivity());
	}
	@Override
	protected void destorytView() {
		// TODO Auto-generated method stub
		
		mQrcodeRceiver.unRegister(getActivity());
	}
	
	public void initQrcode(){
		final String index = getBtTndex();
		//MxyLog.i(TAG, "initQrcode" + "--getBtTndex=" + getBtTndex());
		if(index == null){
			setQrcodeState(QRCODE_STATE_UNVALUE);
			return;
		}
		if(!index.equals(readQrcodeIndex())){
			setQrcodeState(QRCODE_STATE_PREPARING);
			createQrcode(index);
			return;
		}
		final Bitmap image = getQrcodeImage();
		if(image == null){
			setQrcodeState(QRCODE_STATE_PREPARING);
			createQrcode(index);
			return;
		}
		mQrcodeImage.setImageBitmap(image);
		setQrcodeState(QRCODE_STATE_VALUE);
	}
	
	private void writeQrcodeIndex(String index){
		SharedPreferencesUtils.setParam(getActivity(), PREFERENCE_KEY_INDEX, index);
	}
	
	private String readQrcodeIndex(){
		return (String) SharedPreferencesUtils.getParam(getActivity(), PREFERENCE_KEY_INDEX,"");
	}
	
	private void createQrcode(String index){
		if(mQrcodeTask != null){
			final QrcodeTask oldTask = mQrcodeTask;
			oldTask.cancel(true);
			mQrcodeTask = null;
		}
		mQrcodeTask = new QrcodeTask();
		mQrcodeTask.execute(index);

	}

	private void setQrcodeState(int state){
		switch (state) {
			case QRCODE_STATE_VALUE:
				mQrcodeImage.setVisibility(View.VISIBLE);
				mQrcodeText.setVisibility(View.INVISIBLE);
				break;
			case QRCODE_STATE_ERROR:
				mQrcodeText.setText(R.string.status_qrcode_error);
				mQrcodeImage.setVisibility(View.INVISIBLE);
				mQrcodeText.setVisibility(View.VISIBLE);
				break;
			case QRCODE_STATE_PREPARING:
				mQrcodeText.setText(R.string.status_qrcode_preparing);
				mQrcodeImage.setVisibility(View.INVISIBLE);
				mQrcodeText.setVisibility(View.VISIBLE);
				break;
			default:
				mQrcodeText.setText(R.string.status_qrcode_empty);
				mQrcodeImage.setVisibility(View.INVISIBLE);
				mQrcodeText.setVisibility(View.VISIBLE);
				break;
		}
	}
	private Bitmap getQrcodeImage(){
		File QRFile = new File(SysServices.getStoragePath(),QRCODE_IMAGE_NAME);
        try {
            FileInputStream inputStream = new FileInputStream(QRFile);
            return BitmapFactory.decodeStream(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
	}

	private String getBtTndex(){
		final BluetoothAdapter adapter = SysServices.getBtAdapter(getActivity());
		if(adapter == null || !adapter.isEnabled()){
			return null;
		}
		return adapter.getAddress() + "#" + adapter.getName();
	}

	private OnClickListener onRootClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			QrcodeFragment.this.dismiss();
		}
	};
	
	private class QrcodeRceiver extends AbsBroadcastReceiver{
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if(BluetoothAdapter.ACTION_STATE_CHANGED.equals(intent.getAction())){
				switch (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.STATE_OFF)) {
					case BluetoothAdapter.STATE_OFF:
						setQrcodeState(QRCODE_STATE_UNVALUE);
						break;
					case BluetoothAdapter.STATE_ON:
						initQrcode();
						break;
					default:
						break;
				}
				return;
			}
			if(BluetoothAdapter.ACTION_LOCAL_NAME_CHANGED.equals(intent.getAction())){
				setQrcodeState(QRCODE_STATE_PREPARING);
				createQrcode(getBtTndex());
				return;
			}
		}
		@Override
		public IntentFilter getIntentFilter() {
			// TODO Auto-generated method stub
			IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
			filter.addAction(BluetoothAdapter.ACTION_LOCAL_NAME_CHANGED);
			return filter;
		}
	}
	
	private class QrcodeTask extends AsyncTask<String, Void, String>{

		private static final int QR_WIDTH = 400;
		private static final int QR_HEIGHT = 400;
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
            try {
            	//MxyLog.i(TAG, "Qrcodcreat" + "--start");
            	Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
                hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
				BitMatrix bitMatrix = new QRCodeWriter().encode(params[0], BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
				int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
				for (int y = 0; y < QR_HEIGHT; y++)
	            {
	                for (int x = 0; x < QR_WIDTH; x++)
	                {
	                    if (bitMatrix.get(x, y))
	                    {
	                        pixels[y * QR_WIDTH + x] = 0xff000000;
	                    }
	                    else
	                    {
	                        pixels[y * QR_WIDTH + x] = 0xffffffff;
	                    }
	                }
	            }
				//MxyLog.i(TAG, "Qrcodcreat" + "--success");
				
				Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ARGB_8888);
	            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
	            
	            File QRPic = new File(SysServices.getStoragePath(),QRCODE_IMAGE_NAME);
	            if(QRPic.exists()){
	                QRPic.delete();
	            }
	            FileOutputStream outputStream = new FileOutputStream(QRPic);
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream);
                outputStream.flush();
                outputStream.close();
                return params[0];
			} catch (WriterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				MxyLog.e(TAG, "Qrcodcreat--failure--" + e.toString());
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				MxyLog.e(TAG, "Qrcodcreat--failure--" + e.toString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				MxyLog.e(TAG, "Qrcodcreat--failure--" + e.toString());
			}
			return null;
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if(getActivity() == null){
				return;
			}
			if(result == null){
				setQrcodeState(QRCODE_STATE_ERROR);
			}else{
				writeQrcodeIndex(result);
				initQrcode();
			}
			super.onPostExecute(result);
		}
		@Override
		protected void onCancelled(String result) {
			// TODO Auto-generated method stub
			super.onCancelled(result);
		}
		
		
	}
}
