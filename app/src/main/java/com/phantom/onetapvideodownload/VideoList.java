package com.phantom.onetapvideodownload;

import android.content.Context;

import com.phantom.onetapvideodownload.Video.Video;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class VideoList {
    private VideoList() {
    }

    public static final String PREFS_NAME = "SavedUrls";
    public static VideoList mUrlList;
    List<Video> mList = new ArrayList<>();
    private static Context mContext;
    private static DatabaseHandler mDatabaseHandler;

    public static VideoList getVideoListSingleton(Context context) {
        if (mUrlList != null) {
            return mUrlList;
        }

        mUrlList = new VideoList();
        mContext = context;
        mDatabaseHandler = DatabaseHandler.getDatabase(context);
        mUrlList.loadSavedVideos();

        return mUrlList;
    }

    public void sortList() {
        Collections.sort(mList, new Comparator<Video>() {
            @Override
            public int compare(Video lhs, Video rhs) {
                return lhs.getDatabaseId() < rhs.getDatabaseId() ? 1 : 0;
            }
        });
    }

    public void addVideo(Video video) {
        long videoId = mDatabaseHandler.addOrUpdateVideo(video);
        video.setDatabaseId(videoId);
        sortList();
    }

    public void removeVideo(Video video) {
        mDatabaseHandler.deleteVideo(video.getDatabaseId());
        mList.remove(video);
    }

    public int size() {
        return mList.size();
    }

    public Video getVideo(int pos) {
        assert pos < mList.size();
        return mList.get(pos);
    }

    public void clearLocalList() {
        mList.clear();
    }

    public void clearSavedVideos() {
        mDatabaseHandler.clearDatabase();
    }

    public void loadSavedVideos() {
        mList = mDatabaseHandler.getAllVideos();
    }

    public ArrayList<Video> getVideoList() {
        return new ArrayList<>(mList);
    }

    public boolean isEmpty() {
        return size() <= 0;
    }

    public void reloadVideos() {
        clearLocalList();
        loadSavedVideos();
    }
}
