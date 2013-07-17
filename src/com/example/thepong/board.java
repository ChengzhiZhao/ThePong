package com.example.thepong;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.animation.Animation;

public class board extends SurfaceView implements SurfaceHolder.Callback, Runnable{
	private SurfaceHolder sfh;    
    private Thread th;    
    private Canvas canvas;    
    private Paint paint; 
    private Paint words;
    private int ScreenW, ScreenH;    
    private int paddleH = 40;
    private int paddleW = 200;
    private boolean DOWN=false, LEFT=false, RIGHT=false; 
    private int paddleSpeed = 0;
    private final int lineHeight = 100;
    private int paddle1X = 0;
    private int paddle1Y = 0;
    private int paddle1Center;
    private int paddle2X = 0;
    private int paddle2Y = 0;
    private int paddle2Center;
    private boolean paddle2Active = false;
    private boolean paddle1Active = false;
    private int lastActive = 0;
    private Ball ball;   
    private int Player1_Score = 0;
    private int Player2_Score = 0;
    
    
    public board(Context context) {    
        super(context);    
        th = new Thread(this);    
        sfh = this.getHolder(); 
        getHolder().addCallback(this);
        paint = new Paint();    
        paint.setAntiAlias(true);    
        paint.setColor(Color.WHITE);   
        paint.setStrokeWidth(8);
        paint.setTextSize(24);
        words = new Paint();
        words.setTextSize(18);
        this.setKeepScreenOn(true);// 保持屏幕常亮    
        setFocusable(true);
        ball = new Ball();
        
    }    
    @Override    
    public void startAnimation(Animation animation) {    
        super.startAnimation(animation);    
    }    
    public void surfaceCreated(SurfaceHolder holder) {    
        ScreenW = this.getWidth();// 备注2    
        ScreenH = this.getHeight();    
        paddle1Center = ScreenW/2;
        paddle2Center = ScreenW/2;
        th.start();    
        ball.Inital(ScreenW/2,ScreenH/2);
    }    
    private void draw() {    
        try {    
            canvas = sfh.lockCanvas(); // 得到一个canvas实例    
            canvas.drawColor(Color.BLACK);// 刷屏    
            canvas.drawLine(0, lineHeight, ScreenW, lineHeight, paint);
            canvas.drawLine(0, ScreenH-lineHeight, ScreenW, ScreenH-lineHeight, paint);
            canvas.drawLine(0, ScreenH/2, ScreenW, ScreenH/2, paint);
            canvas.drawText("Player Two: "+Player2_Score, ScreenW-200, ScreenH/2-50, paint);
            canvas.drawText("Player One: "+Player1_Score, ScreenW-200, ScreenH/2+50, paint);
            canvas.drawCircle(ball.getX(),ball.getY(), ball.getRadius(), paint);
            canvas.drawRect(paddle2Center-paddleW/2, lineHeight, paddle2Center+paddleW/2, lineHeight+paddleH, paint);
            canvas.drawRect(paddle1Center-paddleW/2,ScreenH-paddleH-lineHeight,paddle1Center+paddleW/2,ScreenH-lineHeight, paint);
        } catch (Exception ex) {    
        } finally { // 备注3    
            if (canvas != null)    
                sfh.unlockCanvasAndPost(canvas);  // 将画好的画布提交    
        }    
    }     
    public void run() {
        while (true) { 
        	ball.update();
        	draw();    
            try {    
                Thread.sleep(25);    
            } catch (InterruptedException e) {    
                // TODO Auto-generated catch block    
                e.printStackTrace();    
            }    
        }    
    }
    
    public class Ball{
    	private int [] ball_position = {0,0};
        private int ball_radius = 24;
        private String direction ="";
        private int [] speed = {0,0};
        private int [] vel = {5,5};
        private int [] ace = {1,1};
        
        public int getX(){
        	return ball_position[0];        	
        }
    	public int getY(){
    		return ball_position[1];
    	}
    	public int getRadius(){
    		return ball_radius;
    	}
    	public void getDirection(){
    		double i = Math.random();
    		if(i<=0.5){
    			direction = "Up";
    		}
    		else{
    			direction = "Down";
    		}
    	}
    	public void vel_rest(){
    		vel [0] = 5;
    		vel [1] = 3;
    	}
    	public void Inital(int w, int h){
    		ball_position[0]=w;
    		ball_position[1]=h;
    		getDirection();
    		vel_rest();
    		if(direction == "Up"){
    			speed[1]=-10;
    			if(Math.random()<0.5){
    				speed[0]=-10;
    			}
    			else{
    				speed[0]=10;
    			}
    		}
    		else{
    			speed[1]=10;
    			if(Math.random()<0.5){
    				speed[0]=-10;
    			}
    			else{
    				speed[0]=10;
    			}
    		}    		
    	}
    	public void update(){
    		
    		ball_position[0] += speed[0];
			ball_position[1] += speed[1];
			
    		if(ball_position[0]<=ball_radius+2 || ball_position[0]>=ScreenW-ball_radius-2){
    			speed[0]=-speed[0];    			
    		}
    		else if(ball_position[1]<=ball_radius+lineHeight+paddleH && ball_position[0]<=paddle2Center+paddleW/2+ball_radius/2 && ball_position[0]>=paddle2Center-paddleW/2-ball_radius/2)
    		{
    			speed[1]-=vel[1];
    			speed[1]=-speed[1];    			   			
    		}
    		else if(ball_position[1]>=ScreenH-ball_radius-lineHeight-paddleH && ball_position[0]<=paddle1Center+paddleW/2+ball_radius/2 && ball_position[0]>=paddle1Center-paddleW/2-ball_radius/2)
    		{
    			speed[1]+=vel[1];
    			speed[1]=-speed[1];     			
    		}
    		else if(ball_position[1]<=ball_radius+lineHeight+paddleH){
    			Player1_Score +=1;
    			Inital(ScreenW/2,ScreenH/2);    			
    		}
    		else if(ball_position[1]>=ScreenH-ball_radius-lineHeight-paddleH){
    			Player2_Score +=1;
    			Inital(ScreenW/2,ScreenH/2);    	
    		}
    	}
    }
    
    
    
    public void surfaceChanged(SurfaceHolder holder, int format, int width,    
            int height) {    
    }    
    public void surfaceDestroyed(SurfaceHolder holder) {    
        // TODO Auto-generated method stub    
    }    
    
    @Override    
    public boolean onTouchEvent(MotionEvent event) {
    	
    	int action = event.getAction();
    	int pointerIndex = (action & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
        int pointerId = event.getPointerId(pointerIndex);
        
        switch (action & MotionEvent.ACTION_MASK) {
        case MotionEvent.ACTION_DOWN: {
        	if (event.getY(0)>=ScreenH/2){
	        	paddle1X=(int)event.getX(0);
	        	paddle1Center = paddle1X; 
	        	paddle1Active = true;
	        	lastActive = 1;
	            break;
            }
        	if(event.getY(0)<ScreenH/2){
        		paddle2X=(int)event.getX(0);
	        	paddle2Center = paddle2X;  
	        	paddle2Active = true;
	        	lastActive = 2;
	            break;        		
        	}
        }
        case MotionEvent.ACTION_UP: {
        	if (event.getY(0)>=ScreenH/2){
            	paddle1Active = false;
            	lastActive=2;
            	break;
            }
        	if (event.getY(0)<ScreenH/2){
	        	paddle2Active = false;
	        	lastActive=1;
	        	break;
        	}
        	
        } 
        case MotionEvent.ACTION_MOVE: {
        	if(paddle1Active && lastActive == 2){
    			paddle2Center = (int) event.getX(1);
    			paddle1Center = (int) event.getX(0);
    			break;
    		}      
        	else if (paddle2Active && lastActive == 1){
        		paddle2Center = (int) event.getX(0);
    			paddle1Center = (int) event.getX(1);  
    			break;
        	}
        	else if (!paddle1Active && lastActive == 2){
        		if(event.getY(0)<ScreenH/2)
        			paddle2Center = (int) event.getX(0);
        		else
        			paddle1Center = (int) event.getX(0);
        		break;
        	}
        	else if(!paddle2Active && lastActive == 1){
        		if(event.getY(0)>=ScreenH/2)
        			paddle1Center = (int) event.getX(0);  
        		else
        			paddle2Center = (int) event.getX(0);
        		break;
        	}
        }
        case MotionEvent.ACTION_POINTER_DOWN:{
        	if (event.getY(1)>=ScreenH/2){
            	paddle1X = (int) event.getX(1);
            	paddle1Center = paddle1X;
            	paddle1Active = true;
            	lastActive = 1;
            	break;
            	}      
        	if (event.getY(1)<ScreenH/2){
	        	paddle2X = (int) event.getX(1);
	        	paddle2Center = paddle2X;
	        	paddle2Active = true;
	        	lastActive = 2;
	        	break;
        	}        	
        }
        case MotionEvent.ACTION_POINTER_UP:{
        	if (event.getY(0)<ScreenH/2){
            	paddle1Active = false;
            	lastActive=2;
            	break;
            }
        	if (event.getY(0)>=ScreenH/2){
	        	paddle2Active = false;
	        	lastActive=1;
	        	break;
        	}
        }
        
        
        
        }
        
        
          
    	
    	
    	
    	
        return true;
    }

}
