package com.sczn.wearlauncher.view.card;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by mengmeng on 2016/10/12.
 */
public class CompassView  extends ImageView{
	private float mDirection;// ������ת������
	private Drawable compass;// ͼƬ��Դ
	
	//����������
	public CompassView(Context context) {
		super(context);
		mDirection = 0.0f;// Ĭ�ϲ���ת
		compass = null;
	}

	public CompassView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mDirection = 0.0f;
		compass = null;
	}

	public CompassView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mDirection = 0.0f;
		compass = null;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (compass == null) {
			compass = getDrawable();// ��ȡ��ǰview��ͼƬ��Դ
			compass.setBounds(0, 0, getWidth(), getHeight());// ͼƬ��Դ��view��λ�ã��˴��൱�ڳ���view
		}

		canvas.save();
		canvas.rotate(mDirection, getWidth() / 2, getHeight() / 2);// ��ͼƬ���ĵ���ת��
		compass.draw(canvas);// ����ת���ͼƬ����view�ϣ���������ת�������
		canvas.restore();// ����һ��
	}

	/**
	 * �Զ�����·���ķ���
	 * 
	 * @param direction
	 *            ����ķ���
	 */
	public void updateDirection(float direction) {
		mDirection = direction;
		invalidate();// ����ˢ��һ�£����·���
	}
}
