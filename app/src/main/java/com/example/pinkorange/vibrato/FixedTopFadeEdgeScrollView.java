package com.example.pinkorange.vibrato;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class FixedTopFadeEdgeScrollView extends ScrollView {

        public FixedTopFadeEdgeScrollView(Context context) {
            super(context);
        }
        private boolean enableScrolling = true;
        public FixedTopFadeEdgeScrollView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public FixedTopFadeEdgeScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @Override
        protected float getTopFadingEdgeStrength() {
            return 0.1f;
        }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (scrollingEnabled()) {
            return super.onTouchEvent(ev);
        } else {
            return false;
        }
    }
    private boolean scrollingEnabled(){
        return enableScrolling;
    }
    public void setScrolling(boolean enableScrolling) {
        this.enableScrolling = enableScrolling;
    }
}

