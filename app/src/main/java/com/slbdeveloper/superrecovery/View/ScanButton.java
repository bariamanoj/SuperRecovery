package com.slbdeveloper.superrecovery.View;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.slbdeveloper.superrecovery.R;


public class ScanButton extends FrameLayout {

    public final ImageView f4440e;

    public final ImageView f4441f;

    public ObjectAnimator f4442g;

    public ScanButton(Context context) {
        this(context, null, 0, 6, null);
    }

    public ScanButton(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 4, null);
    }

    public ScanButton(Context context, AttributeSet attributeSet, int i, int i1, Object o) {
        this(context, (i1 & 2) != 0 ? null : attributeSet, (i1 & 4) != 0 ? 0 : i);
    }


    private final ObjectAnimator getRotateAnimator() {
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.f4441f, View.ROTATION, 0.0f, 360.0f);
        //g.a((Object) ofFloat, "ObjectAnimator.ofFloat(m… View.ROTATION, 0f, 360f)");
        this.f4442g = ofFloat;
        ofFloat.setRepeatCount(-1);
        this.f4442g.setInterpolator(new LinearInterpolator());
        this.f4442g.setDuration(1000L);
        return this.f4442g;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public ScanButton(Context context, AttributeSet attributeSet, int i2) {
        super(context, attributeSet, i2);
        if (context != null) {
            View inflate = LayoutInflater.from(context).inflate(R.layout.view_scan_button, (ViewGroup) this, true);
            View findViewById = inflate.findViewById(R.id.inner_image_view);
            //g.a((Object) findViewById, "view.findViewById(R.id.inner_image_view)");
            this.f4440e = (ImageView) findViewById;
            View findViewById2 = inflate.findViewById(R.id.outer_image_view);
            //g.a((Object) findViewById2, "view.findViewById(R.id.outer_image_view)");
            this.f4441f = (ImageView) findViewById2;
            this.f4442g = getRotateAnimator();
            return;
        }
       // g.a("context");
        throw null;
    }

}
