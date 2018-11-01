package com.zhaoqy.app.demo.notebook.milletnotes.util;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.notebook.milletnotes.activity.NotesSettingsActivity;

import android.content.Context;
import android.preference.PreferenceManager;

public class ResourceParser 
{
	public static final int YELLOW           = 0;
    public static final int BLUE             = 1;
    public static final int WHITE            = 2;
    public static final int GREEN            = 3;
    public static final int RED              = 4;
    public static final int TEXT_SMALL       = 0;
    public static final int TEXT_MEDIUM      = 1;
    public static final int TEXT_LARGE       = 2;
    public static final int TEXT_SUPER       = 3;
    public static final int BG_DEFAULT_COLOR = YELLOW;
    public static final int BG_DEFAULT_FONT_SIZE = TEXT_MEDIUM;

    public static class NoteBgResources 
    {
        private final static int [] BG_EDIT_RESOURCES = new int [] 
        {
            R.drawable.milletnotes_edit_yellow,
            R.drawable.milletnotes_edit_blue,
            R.drawable.milletnotes_edit_white,
            R.drawable.milletnotes_edit_green,
            R.drawable.milletnotes_edit_red
        };

        private final static int [] BG_EDIT_TITLE_RESOURCES = new int [] 
        {
            R.drawable.milletnotes_edit_title_yellow,
            R.drawable.milletnotes_edit_title_blue,
            R.drawable.milletnotes_edit_title_white,
            R.drawable.milletnotes_edit_title_green,
            R.drawable.milletnotes_edit_title_red
        };

        public static int getNoteBgResource(int id) 
        {
            return BG_EDIT_RESOURCES[id];
        }

        public static int getNoteTitleBgResource(int id) 
        {
            return BG_EDIT_TITLE_RESOURCES[id];
        }
    }

    public static int getDefaultBgId(Context context) 
    {
        if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean(NotesSettingsActivity.PREFERENCE_SET_BG_COLOR_KEY, false)) 
        {
            return (int) (Math.random() * NoteBgResources.BG_EDIT_RESOURCES.length);
        }
        else 
        {
            return BG_DEFAULT_COLOR;
        }
    }

    public static class NoteItemBgResources 
    {
        private final static int [] BG_FIRST_RESOURCES = new int [] 
        {
            R.drawable.milletnotes_list_yellow_up,
            R.drawable.milletnotes_list_blue_up,
            R.drawable.milletnotes_list_white_up,
            R.drawable.milletnotes_list_green_up,
            R.drawable.milletnotes_list_red_up
        };

        private final static int [] BG_NORMAL_RESOURCES = new int [] 
        {
            R.drawable.milletnotes_list_yellow_middle,
            R.drawable.milletnotes_list_blue_middle,
            R.drawable.milletnotes_list_white_middle,
            R.drawable.milletnotes_list_green_middle,
            R.drawable.milletnotes_list_red_middle
        };

        private final static int [] BG_LAST_RESOURCES = new int [] 
        {
            R.drawable.milletnotes_list_yellow_down,
            R.drawable.milletnotes_list_blue_down,
            R.drawable.milletnotes_list_white_down,
            R.drawable.milletnotes_list_green_down,
            R.drawable.milletnotes_list_red_down,
        };

        private final static int [] BG_SINGLE_RESOURCES = new int [] 
        {
            R.drawable.milletnotes_list_yellow_single,
            R.drawable.milletnotes_list_blue_single,
            R.drawable.milletnotes_list_white_single,
            R.drawable.milletnotes_list_green_single,
            R.drawable.milletnotes_list_red_single
        };

        public static int getNoteBgFirstRes(int id) 
        {
            return BG_FIRST_RESOURCES[id];
        }

        public static int getNoteBgLastRes(int id) 
        {
            return BG_LAST_RESOURCES[id];
        }

        public static int getNoteBgSingleRes(int id) 
        {
            return BG_SINGLE_RESOURCES[id];
        }

        public static int getNoteBgNormalRes(int id) 
        {
            return BG_NORMAL_RESOURCES[id];
        }

        public static int getFolderBgRes() 
        {
            return R.drawable.milletnotes_list_folder;
        }
    }

    public static class WidgetBgResources 
    {
        private final static int [] BG_2X_RESOURCES = new int [] 
       {
            R.drawable.milletnotes_widget_2x_yellow,
            R.drawable.milletnotes_widget_2x_blue,
            R.drawable.milletnotes_widget_2x_white,
            R.drawable.milletnotes_widget_2x_green,
            R.drawable.milletnotes_widget_2x_red,
        };

        public static int getWidget2xBgResource(int id) 
        {
            return BG_2X_RESOURCES[id];
        }

        private final static int [] BG_4X_RESOURCES = new int [] 
        {
            R.drawable.milletnotes_widget_4x_yellow,
            R.drawable.milletnotes_widget_4x_blue,
            R.drawable.milletnotes_widget_4x_white,
            R.drawable.milletnotes_widget_4x_green,
            R.drawable.milletnotes_widget_4x_red
        };

        public static int getWidget4xBgResource(int id) 
        {
            return BG_4X_RESOURCES[id];
        }
    }

    public static class TextAppearanceResources 
    {
        private final static int [] TEXTAPPEARANCE_RESOURCES = new int [] 
        {
            R.style.TextAppearanceNormal,
            R.style.TextAppearanceMedium,
            R.style.TextAppearanceLarge,
            R.style.TextAppearanceSuper
        };

        public static int getTexAppearanceResource(int id) 
        {
            if (id >= TEXTAPPEARANCE_RESOURCES.length) 
            {
                return BG_DEFAULT_FONT_SIZE;
            }
            return TEXTAPPEARANCE_RESOURCES[id];
        }

        public static int getResourcesSize() 
        {
            return TEXTAPPEARANCE_RESOURCES.length;
        }
    }
}
