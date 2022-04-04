package com.example.administrator.beta;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.mattcarroll.hover.HoverMenu;
import io.mattcarroll.hover.HoverView;
import io.mattcarroll.hover.window.HoverMenuService;

public class MultipleSectionsHoverMenuService extends HoverMenuService {

    private static final String TAG = "MultipleSectionsHoverMenuService";
    private Context m1Context = null;
    private static Activity activity = null;
    public  MultipleSectionsHoverMenuService(Context context){
        this.m1Context = context;
    }
    @Override
    protected void onHoverMenuLaunched(@NonNull Intent intent, @NonNull HoverView hoverView) {
        hoverView.setMenu(createHoverMenu(m1Context));
        hoverView.bringToFront();
        hoverView.collapse();
        //hoverView.expand();
    }

    @NonNull
    private HoverMenu createHoverMenu(Context mContext ) {
        return new MultiSectionHoverMenu(mContext);
    }

    private static class MultiSectionHoverMenu extends HoverMenu {

        private final Context mContext;
        private final List<Section> mSections;

        public MultiSectionHoverMenu(@NonNull Context context) {
            mContext = context.getApplicationContext();

            mSections = Arrays.asList(
                    new Section(
                            new SectionId("1"),
                            createTabView(),
                            new HoverMenuScreen(mContext, "主界面",1)
                    ),
                    new Section(
                            new SectionId("2"),
                            createTabView(),
                            new HoverMenuScreen(mContext, "辅助测界面",1)
                    ),
                    new Section(
                            new SectionId("3"),
                            createTabView(),
                            new HoverMenuScreen(mContext, "搜索",1)
                    )
            );
        }

        private View createTabView() {
            ImageView imageView = new ImageView(mContext);
            imageView.setImageResource(R.drawable.side_nav_bar);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            return imageView;
        }

        @Override
        public String getId() {
            return "multisectionmenu";
        }

        @Override
        public int getSectionCount() {
            return mSections.size();
        }

        @Nullable
        @Override
        public Section getSection(int index) {
            return mSections.get(index);
        }

        @Nullable
        @Override
        public Section getSection(@NonNull SectionId sectionId) {
            for (HoverMenu.Section section : mSections) {
                if (section.getId().equals(sectionId)) {
                    return section;
                }
            }
            return null;
        }

        @NonNull
        @Override
        public List<Section> getSections() {
            return new ArrayList<>(mSections);
        }
    }

}
