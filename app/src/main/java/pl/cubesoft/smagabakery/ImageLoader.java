package pl.cubesoft.smagabakery;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.widget.ImageView;

public interface ImageLoader {

    public enum Transform {
        CIRCLE,
        NONE
    }

    public interface Listener {
        void onImageLoadSuccess(Bitmap bitmap);
        void onImageLoadError();
    }

    void load(Uri uri, Transform transform, Object tag, Listener listener);

    class Size {
        final int width;
        final int height;

        public Size(int width, int height) {
            this.width = width;
            this.height = height;
        }
    }
    void load(Uri uri, Transform transform, Object tag, Size size, Listener listener);

    void load(Uri uri, ImageView imageView, boolean fit, boolean noPlaceholder, Transform transform, Object tag);
    void load(Uri uri, ImageView imageView, boolean fit, boolean noPlaceholder, Transform transform, Object tag, @Nullable Listener listener);

    void load(Uri uri, ImageView imageView, Transform transform, Object imageLoadTag);

    void pauseTag(Object tag);

    void resumeTag(Object tag);

    void cancelTag(Object tag);

    void cancelRequest(ImageView imageView);


}
