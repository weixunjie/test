package cn.cloudwalk.localsdkdemo.util;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;


/**
 * Created by Xuyutong on 2015/8/31.
 */
public abstract class AnimaUtil {

    /**
     *  透明动画
     */
    public final static int ANIMATION_ALPH = 1;

    /**
     *  渐变尺寸伸缩动画效果
     */
    public final static int ANIMATION_SCALE = 2;


    /**
     *   画面转换位置移动动画效果
     */
    public final static int ANIMATION_TRANSLATE = 3;

    /**
     *   画面转移旋转动画效果
     */
    public final static int ANIMATION_ROTATE = 4;

    /**
     *  上下�?
     */
    private Context contxt;

    /**
     * 获取选择类型
     */
    private int Type = 0;

    /**
     *  动画效果的方式，�?�?为消�?And �?�?为出现，默认为�?0”；
     */
    private int method = 0;

    private Animation anim;
    public AnimaUtil(Context contxt,int Type,int method,int Resourse) {
        this.contxt = contxt;
        this.Type = Type;
        this.method = method;
        switch(Type){
            case ANIMATION_ALPH://透明
                alphaUtil(contxt,method,Resourse);
                break;
            case ANIMATION_SCALE://缩放
                break;
            case ANIMATION_TRANSLATE://位移
                TranslateUtil(contxt,method,Resourse);
                break;
            case ANIMATION_ROTATE://旋转
                break;
        }
    }

    /**
     *  透明渐变
     */
    public void alphaUtil(Context contxt,int method,int Resourse){
        if(method == 0) {
            anim = AnimationUtils.loadAnimation(contxt, Resourse);
        }else{
            anim = AnimationUtils.loadAnimation(contxt, Resourse);
        }
    }

    /**
     *  平移渐变
     * @param contxt
     * @param method
     */
    public void TranslateUtil(Context contxt,int method,int Resourse){
        if(method == 0) {
            anim = AnimationUtils.loadAnimation(contxt, Resourse);
        }else{
            anim = AnimationUtils.loadAnimation(contxt, Resourse);
        }
    }

    /**
     *  设置动画监听事件
     */
    private  void setAnimTionListener(){
        if(anim != null) {
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    onAnimationstart(animation);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    onAnimationend(animation);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    onAnimationrepeat(animation);
                }
            });
        }else{
            Toast.makeText(contxt,"动画类型不能为空",Toast.LENGTH_LONG).show();
        }
    }

    /**
     *  获取动画对象
     * @return
     */
    public Animation getAnimaTion(){
        if(anim != null){
            setAnimTionListener();
            return anim;
        }else{
            return null;
        }
    }
    /**
     *  动画的开�?
     * @param animation
     */
    public abstract void onAnimationstart(Animation animation);
    /**
     *  动画的结�?
     * @param animation
     */
    public abstract void onAnimationend(Animation animation);
    /**
     *  动画的重�?
     * @param animation
     */
    public abstract void onAnimationrepeat(Animation animation);
}
