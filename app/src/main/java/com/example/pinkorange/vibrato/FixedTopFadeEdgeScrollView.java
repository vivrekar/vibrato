package com.example.pinkorange.vibrato;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class FixedTopFadeEdgeScrollView extends ScrollView {

        public FixedTopFadeEdgeScrollView(Context context) {
            super(context);
        }

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
}
