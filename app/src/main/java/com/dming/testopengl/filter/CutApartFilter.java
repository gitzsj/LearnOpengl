package com.dming.testopengl.filter;

import android.content.Context;
import android.opengl.Matrix;
import android.view.animation.DecelerateInterpolator;

import com.dming.testopengl.R;
import com.dming.testopengl.utils.GLInterpolator;
import com.dming.testopengl.utils.ShaderHelper;

import java.nio.FloatBuffer;

public class CutApartFilter extends BaseFilter {

    private GLInterpolator mGLInterpolator;
    private DecelerateInterpolator mDecelerateInterpolator;
    private FloatBuffer mTexFB1;
    private FloatBuffer mTexFB2;
    private FloatBuffer mTexFB3;
    private float mTranslateY;
    private float mTranslateYY;

    public CutApartFilter(Context context) {
        super(context, R.raw.multiple_frg);
        mDecelerateInterpolator = new DecelerateInterpolator();
        mGLInterpolator = new GLInterpolator(4000, 1f, 0.0f);
        mTexFB1 = ShaderHelper.arrayToFloatBuffer(new float[]{
                0, 1,
                0, 0,
                0.333f, 0,
                0.333f, 1,
//                0, 0.333f,
//                0, 0,
//                1, 0,
//                1, 0.333f,
        });
        mTexFB2 = ShaderHelper.arrayToFloatBuffer(new float[]{
                0.333f, 1,
                0.333f, 0,
                0.666f, 0,
                0.666f, 1,
//                0, 0.666f,
//                0, 0.334f,
//                1, 0.334f,
//                1, 0.666f,
        });
        mTexFB3 = ShaderHelper.arrayToFloatBuffer(new float[]{
                0.666f, 1,
                0.666f, 0,
                1, 0,
                1, 1,
//                0, 1,
//                0, 0.667f,
//                1, 0.667f,
//                1, 1,
        });
    }

    @Override
    public void onDraw(int textureId, float[] texMatrix, int x, int y, int width, int height) {
        float value = mGLInterpolator.getValue();
        if (value <= 0.2) {
            mTranslateY = mDecelerateInterpolator.getInterpolation(1 - value / 0.2f) * 2;
            mTranslateYY = -2 + mTranslateY;
        } else if (value >= 0.8) {
            mTranslateY = -mDecelerateInterpolator.getInterpolation((value - 0.8f) / 0.2f) * 2;
            mTranslateYY = 2 + mTranslateY;
        } else {
            mTranslateY = 0;
            mTranslateYY = 0;
        }

        mTexFB = mTexFB1;
        int w = width / 3;
        Matrix.setIdentityM(mMvpMatrix, 0);
        super.onDraw(textureId, mMvpMatrix, texMatrix, x, y, w, height);
        //
        mTexFB = mTexFB2;
        if (mTranslateY != 0) {
            Matrix.translateM(mMvpMatrix, 0, 0, mTranslateYY, 0);
            super.onDraw(textureId, mMvpMatrix, texMatrix, x + w, y, w, height);
        }
        Matrix.setIdentityM(mMvpMatrix, 0);
        Matrix.translateM(mMvpMatrix, 0, 0, mTranslateY, 0);
        super.onDraw(textureId, mMvpMatrix, texMatrix, x + w, y, w, height);
        //
        mTexFB = mTexFB3;
        Matrix.setIdentityM(mMvpMatrix, 0);
        super.onDraw(textureId, mMvpMatrix, texMatrix, x + w + w, y, w, height);
//        mTexFB = mTexFB1;
//        int h = height / 3;
//        Matrix.setIdentityM(mMvpMatrix, 0);
//        super.onDraw(textureId, mMvpMatrix, texMatrix, x, y, width, h);
//        //
//        mTexFB = mTexFB2;
////        Matrix.translateM(mMvpMatrix, 0, mGLInterpolator.getValue() * 2, 0, 0);
//        super.onDraw(textureId, mMvpMatrix, texMatrix, x, y + h, width, height - h - h);
//        //
//        mTexFB = mTexFB3;
//        Matrix.setIdentityM(mMvpMatrix, 0);
//        super.onDraw(textureId, mMvpMatrix, texMatrix, x, y + height - h, width, h);
    }
}
