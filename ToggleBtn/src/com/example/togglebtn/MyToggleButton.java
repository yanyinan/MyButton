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
	//背景和可滑动按钮
	private Bitmap backGroud;
	private Bitmap slideBtn;
	private Paint paint;
	//可滑动按钮距离View左端距离
	private float slidBtnleft;
	//此时的开关状态
	private boolean currState;
	//down时刻的x坐标
	private float firstX;
	//move时刻x坐标
	private float lastX;
	private float disX;
	private int maxLeft;
	//滑动的标志位
	private boolean isMove;
	private int backgroundId;
	private int buttonId;
	/**
	 * 在代码中创建执行的构造方法
	 * @param context
	 */
	public MyToggleButton(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	
	/**
	 * 布局文件中创建执行的构造方法
	 * @param context
	 * @param attrs
	 */
	public MyToggleButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		int count = attrs.getAttributeCount();
		for (int i = 0; i < count; i++) {
			//attr存储原始值，即字符串和资源id
			String nameString = attrs.getAttributeName(i);
			String string = attrs.getAttributeValue(i);
			System.out.println("name:" + nameString +",value:" + string);
			
		}
		//attrs相当于原材料，R.styleable.MyToggleButton相当于图纸。TypedArray将原料根据图纸加工为对应的对象
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MyToggleButton);
		int taCount = ta.getIndexCount();
		for (int id = 0; id < taCount; id++) {
			//R文件中styleable类中每个自定义属性对应的id
			int value = ta.getIndex(id);
			switch (id) {
			case R.styleable.MyToggleButton_back_img:
			//	Drawable backGroud =  ta.getDrawable(id);
				backgroundId = ta.getResourceId(id, -1);
				if(backgroundId == -1){
					throw new RuntimeException("请设置背景图片");
				}
				break;
			case R.styleable.MyToggleButton_button_img:
			//	Drawable button_img =  ta.getDrawable(id);
				buttonId = ta.getResourceId(id, -1);
				if(buttonId == -1){
					throw new RuntimeException("请设置按钮图片");
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
	 * 如果没有重写该方法，则调用默认的View的方法。属性使用wrap_content相当于match_parent
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		/**
		 * 设置View的宽高
		 */
		setMeasuredDimension(backGroud.getWidth(), backGroud.getHeight());
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		/**
		 * paint为之前得到的画笔对象
		 */
		canvas.drawBitmap(backGroud, 0, 0, paint);
		canvas.drawBitmap(slideBtn, slidBtnleft, 0, paint);

	}

	
	@Override
	public void onClick(View v) {
		//防止和move事件冲突
		if(!isMove){
			currState = !currState;
			flushState();
		}
		
	}

	/**
	 * 刷新开关状态
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
			//滑动的状态
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
	 * 刷新视图（刷新前做好按钮位置的限制）
	 */
	private void flushView() {
		//保证移动后滑动按钮在背景图片之内
		slidBtnleft = slidBtnleft<0?0:slidBtnleft;
		slidBtnleft = slidBtnleft>maxLeft?maxLeft:slidBtnleft;
		invalidate();
	}
	
}
