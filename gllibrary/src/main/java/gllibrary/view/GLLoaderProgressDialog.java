package gllibrary.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.gl.gllibrary.R;


/**
 * 加载页面
 */
public class GLLoaderProgressDialog extends Dialog {
	private Context context = null;
	private static GLLoaderProgressDialog customProgressDialog = null;
	
	public GLLoaderProgressDialog(Context context){
		super(context);
		this.context = context;
	}
	
	public GLLoaderProgressDialog(Context context, int theme) {
        super(context, theme);
    }
	
	public static GLLoaderProgressDialog createDialog(Context context){
		customProgressDialog = new GLLoaderProgressDialog(context, R.style.dialog_untran);
		customProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		customProgressDialog.setContentView(R.layout.gl_dialog_load_progress);
		//customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		customProgressDialog.setCancelable(false); //
		return customProgressDialog;
	}
 
    public void onWindowFocusChanged(boolean hasFocus){
    	
    	if (customProgressDialog == null){
    		return;
    	}
    	
        ImageView imageView = (ImageView) customProgressDialog.findViewById(R.id.loadingImageView);
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
        animationDrawable.start();
    }
 
    /**
     * 
     * [Summary]
     *       setTitile 标题
     * @param strTitle
     * @return
     *
     */
    public GLLoaderProgressDialog setTitile(String strTitle){
    	return customProgressDialog;
    }
    
    /**
     * 
     * [Summary]
     *       setMessage 提示内容
     * @param strMessage
     * @return
     *
     */
    public GLLoaderProgressDialog setMessage(String strMessage){
    	TextView tvMsg = (TextView)customProgressDialog.findViewById(R.id.id_tv_loadingmsg);
    	
    	if (tvMsg != null){
    		tvMsg.setText(strMessage);
    	}
    	
    	return customProgressDialog;
    }
}