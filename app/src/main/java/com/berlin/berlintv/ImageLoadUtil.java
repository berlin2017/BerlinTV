package com.berlin.berlintv;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;

public class ImageLoadUtil {
	private static final int CACHE_SIZE = 10 * 1024 * 1024;
	private static final int MEMORY_CACHE_SIZE = 8 * 1024 * 1024;
	private Context mContext;
	// private static FinalBitmap finalBitmap;
	private static ImageLoader mLoader;
	private DisplayImageOptions mOptions;

	public ImageLoadUtil(Context context) {
		this(context, R.drawable.ic_default);
	}

	public ImageLoadUtil(Context context, int loadingImageRes) {
		mContext = context;
		if (loadingImageRes <= 0) {
			loadingImageRes = R.drawable.ic_default;
		}
		mOptions = new DisplayImageOptions.Builder().showImageOnLoading(loadingImageRes)
				.showImageOnFail(loadingImageRes).showImageForEmptyUri(loadingImageRes).cacheOnDisk(true)
				.cacheInMemory(true).displayer(new FadeInBitmapDisplayer(300)).build();
		if (mLoader == null) {
			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mContext)
					.defaultDisplayImageOptions(mOptions).threadPoolSize(3)
					.memoryCacheSize(MEMORY_CACHE_SIZE).diskCacheSize(CACHE_SIZE).build();
			mLoader = ImageLoader.getInstance();
			mLoader.init(config);
		}
	}

	public void display(ImageView imageView, String uri) {
		// if (finalBitmap != null) {
		// finalBitmap.display(imageView, uri);
		// }
		if (TextUtils.isEmpty(uri)) {
			return;
		}
		if (mLoader != null) {
			ImageAware imageAware = new ImageViewAware(imageView, false);
			mLoader.displayImage(uri, imageAware, mOptions);
		}
	}

	public void displayCircle(ImageView imageView, String uri) {
		if (mLoader != null) {
			DisplayImageOptions options = new DisplayImageOptions.Builder().cloneFrom(mOptions)
					.showImageOnFail(R.drawable.ic_default).showImageForEmptyUri(R.drawable.ic_default)
					.build();
			mLoader.displayImage(uri, imageView, options, null);
		}
	}

	public void display(ImageView imageView, String uri, ImageLoadingListener listener) {
		if (mLoader != null) {
			DisplayImageOptions options = new DisplayImageOptions.Builder().cloneFrom(mOptions).showImageOnFail(null)
					.showImageOnLoading(null).showImageForEmptyUri(null).displayer(new FadeInBitmapDisplayer(1500))
					.build();
			mLoader.displayImage(uri, imageView, options, listener);
		}
	}

	public Bitmap getMemoryBitmap(String key) {
		Bitmap bitmap = null;
		// if (finalBitmap != null) {
		// bitmap = finalBitmap.getBitmapFromCache(key);
		// }
		// return bitmap;
		if (mLoader != null) {
			bitmap = mLoader.getMemoryCache().get(key);
		}
		return bitmap;
	}

	public String getCachePath(String key) {
		String path = null;
		if (mLoader != null) {
			File file = mLoader.getDiskCache().get(key);
			if (file != null) {
				path = mLoader.getDiskCache().get(key).getAbsolutePath();
			}
		}
		return path;
	}

	public void clearCache(String uri) {
		// if (finalBitmap != null) {
		// finalBitmap.clearDiskCache(uri);
		// finalBitmap.clearMemoryCache(uri);
		// }
		if (mLoader != null) {
			mLoader.getDiskCache().remove(uri);
			mLoader.clearMemoryCache();
		}
	}
}
