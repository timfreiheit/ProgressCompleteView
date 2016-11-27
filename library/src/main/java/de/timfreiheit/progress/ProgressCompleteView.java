package de.timfreiheit.progress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.Map;


import static android.view.View.MeasureSpec.AT_MOST;
import static android.view.View.MeasureSpec.EXACTLY;

public class ProgressCompleteView extends FrameLayout {

    public enum Status {
        LOADING,
        SUCCESS,
        ERROR
    }

    private CircularProgressView progressView;
    private ImageView completeImage;

    private Status status = Status.LOADING;

    private Map<Status, Integer> colors = new HashMap<>();

    public ProgressCompleteView(Context context) {
        super(context);
        init(context, null);
    }

    public ProgressCompleteView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ProgressCompleteView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        progressView = new CircularProgressView(context);

        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        progressView.setLayoutParams(params);
        progressView.startAnimation();
        addView(progressView);

        completeImage = new ImageView(context);
        params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        int margin = (int) Utils.dpToPx(context, 10);
        params.setMargins(margin, margin, margin, margin);
        completeImage.setLayoutParams(params);
        completeImage.setVisibility(View.GONE);
        addView(completeImage);

        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.ProgressCompleteView,
                    0, 0);

            try {

                int loadingColor = a.getColor(R.styleable.ProgressCompleteView_pcv_color_loading, Utils.getThemeAccentColor(getContext()));
                setColor(Status.LOADING, loadingColor);

                int successColor = a.getColor(R.styleable.ProgressCompleteView_pcv_color_success, Color.GREEN);
                setColor(Status.SUCCESS, successColor);

                int errorColor = a.getColor(R.styleable.ProgressCompleteView_pcv_color_error, Color.RED);
                setColor(Status.ERROR, errorColor);

            } finally {
                a.recycle();
            }
        } else {
            setColor(Status.LOADING, Utils.getThemeAccentColor(getContext()));
            setColor(Status.ERROR, Color.RED);
            setColor(Status.SUCCESS, Color.GREEN);
        }

        progressView.setColor(colors.get(Status.LOADING));

    }

    public void setColor(Status status, @ColorInt int color) {
        colors.put(status, color);
        if (status == this.status) {
            progressView.setColor(color);
        }
    }

    public void setStatus(Status status) {
        if (this.status == status) {
            return;
        }
        this.status = status;
        int color = colors.get(status);
        switch (status) {
            case LOADING:
                progressView.setColor(color);
                progressView.startAnimation();
                completeImage.setVisibility(View.GONE);
                break;
            case ERROR: {
                progressView.completeAnimation(color);
                completeImage.setVisibility(View.VISIBLE);
                AnimatedVectorDrawableCompat drawable = AnimatedVectorDrawableCompat.create(getContext(), R.drawable.ic_error_animated);
                drawable.setTint(color);
                animateDrawable(drawable);
                break;
            }
            case SUCCESS: {
                progressView.completeAnimation(color);
                AnimatedVectorDrawableCompat drawable = AnimatedVectorDrawableCompat.create(getContext(), R.drawable.ic_check_animated);
                drawable.setTint(color);
                animateDrawable(drawable);
                break;
            }
        }
    }

    private void animateDrawable(final AnimatedVectorDrawableCompat drawable) {
        completeImage.setImageDrawable(null);
        completeImage.setVisibility(View.VISIBLE);
        progressView.addListener(new CircularProgressView.CircularProgressViewListener() {
            @Override
            public void onAnimationStart() {

            }

            @Override
            public void onAnimationEnd() {
                completeImage.setVisibility(View.VISIBLE);
                completeImage.setImageDrawable(drawable);
                drawable.start();
                progressView.removeListener(this);
            }
        });
    }

    public Status getStatus() {
        return status;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == EXACTLY || heightMode == EXACTLY) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }

        // make the view 100dp * 100 dp when not restricted to a specific size
        int wrapContentDimension = (int) Utils.dpToPx(getContext(), 100);

        widthSize = widthMode == AT_MOST
                ? Math.min(widthSize, wrapContentDimension)
                : wrapContentDimension;

        heightSize = widthMode == AT_MOST
                ? Math.min(heightSize, wrapContentDimension)
                : wrapContentDimension;

        super.onMeasure(MeasureSpec.makeMeasureSpec(widthSize, widthMode), MeasureSpec.makeMeasureSpec(heightSize, heightMode));

    }
}
