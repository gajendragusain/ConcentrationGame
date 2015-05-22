package com.example.gajendra.memorygameforkids;

/**
 * Created by GajendraS on 07-05-2015.
 */
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by GajendraS on 18-04-2015.
 */
public class BoxView extends ImageView{
    private boolean discovered = false;

    public BoxView(final Context context) {
        super(context);
    }

    public BoxView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public BoxView(final Context context, final AttributeSet att_set, final int style) {
        super(context, att_set, style);
    }

    public boolean isDiscovered() {
        return discovered;
    }

    public void setDiscovered(boolean discovered) {
        this.discovered = discovered;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth()); //Snap to width
    }
}