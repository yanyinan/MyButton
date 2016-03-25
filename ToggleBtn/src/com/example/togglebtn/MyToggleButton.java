package com.example.togglebtn;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;


public class MyToggleButton extends View implements OnClickListener {
	//�����Ϳɻ�����ť
	private Bitmap backGroud;
	private Bitmap slideBtn;
	private Paint paint;
	//�ɻ�����ť����View��˾���
	private float slidBtnleft;
	//��ʱ�Ŀ���״̬
	private boolean currState;
	//downʱ�̵�x����
	private float firstX;
	//moveʱ��x����
	private float lastX;
	private float disX;
	private int maxLeft;
	//�����ı�־λ
	private boolean isMove;
	private int backgroundId;
	private int buttonId;
	/**
	 * �ڴ����д���ִ�еĹ��췽��
	 * @param context
	 */
	public MyToggleButton(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	
	/**
	 * �����ļ��д���ִ�еĹ��췽��
	 * @param context
	 * @param attrs
	 */
	public MyToggleButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		int count = attrs.getAttributeCount();
		for (int i = 0; i < count; i++) {
			//attr�洢ԭʼֵ�����ַ�������Դid
			String nameString = attrs.getAttributeName(i);
			String string = attrs.getAttributeValue(i);
			System.out.println("name:" + nameString +",value:" + string);
			
		}
		//attrs�൱��ԭ���ϣ�R.styleable.MyToggleButton�൱��ͼֽ��TypedArray��ԭ�ϸ���ͼֽ�ӹ�Ϊ��Ӧ�Ķ���
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MyToggleButton);
		int taCount = ta.getIndexCount();
		for (int id = 0; id < taCount; id++) {
			//R�ļ���styleable����ÿ���Զ������Զ�Ӧ��id
			int value = ta.getIndex(id);
			switch (id) {
			case R.styleable.MyToggleButton_back_img:
			//	Drawable backGroud =  ta.getDrawable(id);
				backgroundId = ta.getResourceId(id, -1);
				if(backgroundId == -1){
					throw new RuntimeException("�����ñ���ͼƬ");
				}
				break;
			case R.styleable.MyToggleButton_button_img:
			//	Drawable button_img =  ta.getDrawable(id);
				buttonId = ta.getResourceId(id, -1);
				if(buttonId == -1){
					throw new RuntimeException("�����ð�ťͼƬ");
				}
				break;
			case R.styleable.MyToggleButton_state:
				currState = ta.getBoolean(id, true);
				break;
			default:
				break;
			}

		}
		initView();
	}
	
	
	
	private void initView() {
	//	 backGroud = BitmapFactory.decodeResource(getResources(), R.drawable.switch_background);
	//	 slideBtn = BitmapFactory.decodeResource(getResources(), R.drawable.slide_button);
		 backGroud = BitmapFactory.decodeResource(getResources(), backgroundId);
		 slideBtn = BitmapFactory.decodeResource(getResources(), buttonId);
		 maxLeft = backGroud.getWidth() - slideBtn.getWidth(); 
		 paint = new Paint();
		 paint.setAntiAlias(true);
		 setOnClickListener(this);
		 flushState();
	}

	/**
	 * ���û����д�÷����������Ĭ�ϵ�View�ķ���������ʹ��wrap_content�൱��match_parent
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		/**
		 * ����View�Ŀ��
		 */
		setMeasuredDimension(backGroud.getWidth(), backGroud.getHeight());
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		/**
		 * paintΪ֮ǰ�õ��Ļ��ʶ���
		 */
		canvas.drawBitmap(backGroud, 0, 0, paint);
		canvas.drawBitmap(slideBtn, slidBtnleft, 0, paint);

	}

	
	@Override
	public void onClick(View v) {
		//��ֹ��move�¼���ͻ
		if(!isMove){
			currState = !currState;
			flushState();
		}
		
	}

	/**
	 * ˢ�¿���״̬
	 */
	private void flushState() {
		if(currState){
			slidBtnleft = backGroud.getWidth() - slideBtn.getWidth();
		}else {
			slidBtnleft = 0;
		}
		
		flushView();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event); 
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			firstX = lastX = event.getX();
			isMove = false;
			break;
		case MotionEvent.ACTION_MOVE:
			disX = event.getX() - lastX;
			lastX = event.getX();
			slidBtnleft += disX;
			//������״̬
			if(Math.abs(event.getX()-firstX)>5){
				isMove = true;
			}
			break;	
		case MotionEvent.ACTION_UP:
			if(isMove){
				if(slidBtnleft>maxLeft/2){
					currState = true;
				}else {
					currState = false;
				}
				flushState();
			}
			
			break;		
		default:
			break;
		}
		flushView();
		return true;
	}

	/**
	 * ˢ����ͼ��ˢ��ǰ���ð�ťλ�õ����ƣ�
	 */
	private void flushView() {
		//��֤�ƶ��󻬶���ť�ڱ���ͼƬ֮��
		slidBtnleft = slidBtnleft<0?0:slidBtnleft;
		slidBtnleft = slidBtnleft>maxLeft?maxLeft:slidBtnleft;
		invalidate();
	}
	
}
