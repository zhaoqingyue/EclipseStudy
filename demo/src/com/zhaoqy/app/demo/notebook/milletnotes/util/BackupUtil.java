package com.zhaoqy.app.demo.notebook.milletnotes.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.text.TextUtils;
import android.text.format.DateFormat;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.notebook.milletnotes.db.Notes;
import com.zhaoqy.app.demo.notebook.milletnotes.db.Notes.DataColumns;
import com.zhaoqy.app.demo.notebook.milletnotes.db.Notes.DataConstants;
import com.zhaoqy.app.demo.notebook.milletnotes.db.Notes.NoteColumns;

public class BackupUtil 
{
    private static BackupUtil sInstance;
    private TextExport        mTextExport;

    public static synchronized BackupUtil getInstance(Context context) 
    {
        if (sInstance == null) 
        {
            sInstance = new BackupUtil(context);
        }
        return sInstance;
    }

    public static final int STATE_SD_CARD_UNMOUONTED    = 0;
    public static final int STATE_BACKUP_FILE_NOT_EXIST = 1;
    public static final int STATE_DATA_DESTROIED        = 2;
    public static final int STATE_SYSTEM_ERROR          = 3;
    public static final int STATE_SUCCESS               = 4;

    private BackupUtil(Context context) 
    {
        mTextExport = new TextExport(context);
    }

    private static boolean externalStorageAvailable() 
    {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    public int exportToText() 
    {
        return mTextExport.exportToText();
    }

    public String getExportedTextFileName() 
    {
        return mTextExport.mFileName;
    }

    public String getExportedTextFileDir() 
    {
        return mTextExport.mFileDirectory;
    }

    private static class TextExport 
    {
        private static final String[] NOTE_PROJECTION = 
        {
            NoteColumns.ID,
            NoteColumns.MODIFIED_DATE,
            NoteColumns.SNIPPET,
            NoteColumns.TYPE
        };

        private static final int NOTE_COLUMN_ID            = 0;
        private static final int NOTE_COLUMN_MODIFIED_DATE = 1;
        private static final int NOTE_COLUMN_SNIPPET       = 2;

        private static final String[] DATA_PROJECTION = 
        {
            DataColumns.CONTENT,
            DataColumns.MIME_TYPE,
            DataColumns.DATA1,
            DataColumns.DATA2,
            DataColumns.DATA3,
            DataColumns.DATA4,
        };

        private static final int DATA_COLUMN_CONTENT      = 0;
        private static final int DATA_COLUMN_MIME_TYPE    = 1;
        private static final int DATA_COLUMN_CALL_DATE    = 2;
        private static final int DATA_COLUMN_PHONE_NUMBER = 4;

        private final String [] TEXT_FORMAT;
        private static final int FORMAT_FOLDER_NAME  = 0;
        private static final int FORMAT_NOTE_DATE    = 1;
        private static final int FORMAT_NOTE_CONTENT = 2;

        private Context mContext;
        private String  mFileName;
        private String  mFileDirectory;

        public TextExport(Context context) 
        {
            TEXT_FORMAT = context.getResources().getStringArray(R.array.format_for_exported_note);
            mContext = context;
            mFileName = "";
            mFileDirectory = "";
        }

        private String getFormat(int id) 
        {
            return TEXT_FORMAT[id];
        }

        private void exportFolderToText(String folderId, PrintStream ps) 
        {
            Cursor notesCursor = mContext.getContentResolver().query(Notes.CONTENT_NOTE_URI,
            NOTE_PROJECTION, NoteColumns.PARENT_ID + "=?", new String[] { folderId }, null);
            if (notesCursor != null) 
            {
                if (notesCursor.moveToFirst()) 
                {
                    do {
                        ps.println(String.format(getFormat(FORMAT_NOTE_DATE), DateFormat.format(
                                mContext.getString(R.string.milletnote_format_datetime_mdhm),
                                notesCursor.getLong(NOTE_COLUMN_MODIFIED_DATE))));
                        String noteId = notesCursor.getString(NOTE_COLUMN_ID);
                        exportNoteToText(noteId, ps);
                    } while (notesCursor.moveToNext());
                }
                notesCursor.close();
            }
        }
        
        private void exportNoteToText(String noteId, PrintStream ps) 
        {
            Cursor dataCursor = mContext.getContentResolver().query(Notes.CONTENT_DATA_URI,
            DATA_PROJECTION, DataColumns.NOTE_ID + "=?", new String[] { noteId }, null);

            if (dataCursor != null) 
            {
                if (dataCursor.moveToFirst()) 
                {
                    do 
                    {
                        String mimeType = dataCursor.getString(DATA_COLUMN_MIME_TYPE);
                        if (DataConstants.CALL_NOTE.equals(mimeType)) 
                        {
                            String phoneNumber = dataCursor.getString(DATA_COLUMN_PHONE_NUMBER);
                            long callDate = dataCursor.getLong(DATA_COLUMN_CALL_DATE);
                            String location = dataCursor.getString(DATA_COLUMN_CONTENT);

                            if (!TextUtils.isEmpty(phoneNumber)) 
                            {
                                ps.println(String.format(getFormat(FORMAT_NOTE_CONTENT), phoneNumber));
                            }
                            ps.println(String.format(getFormat(FORMAT_NOTE_CONTENT), 
                            DateFormat.format(mContext.getString(R.string.milletnote_format_datetime_mdhm), callDate)));
                            if (!TextUtils.isEmpty(location)) 
                            {
                                ps.println(String.format(getFormat(FORMAT_NOTE_CONTENT), location));
                            }
                        } 
                        else if (DataConstants.NOTE.equals(mimeType)) 
                        {
                            String content = dataCursor.getString(DATA_COLUMN_CONTENT);
                            if (!TextUtils.isEmpty(content)) 
                            {
                                ps.println(String.format(getFormat(FORMAT_NOTE_CONTENT), content));
                            }
                        }
                    } while (dataCursor.moveToNext());
                }
                dataCursor.close();
            }
    
            try 
            {
                ps.write(new byte[] { Character.LINE_SEPARATOR, Character.LETTER_NUMBER });
            } 
            catch (IOException e) 
            {
            }
        }

        public int exportToText() 
        {
            if (!externalStorageAvailable()) 
            {
                return STATE_SD_CARD_UNMOUONTED;
            }

            PrintStream ps = getExportToTextPrintStream();
            if (ps == null) 
            {
                return STATE_SYSTEM_ERROR;
            }
       
            Cursor folderCursor = mContext.getContentResolver().query(Notes.CONTENT_NOTE_URI,  NOTE_PROJECTION,
                    "(" + NoteColumns.TYPE + "=" + Notes.TYPE_FOLDER + " AND "
                            + NoteColumns.PARENT_ID + "<>" + Notes.ID_TRASH_FOLER + ") OR "
                            + NoteColumns.ID + "=" + Notes.ID_CALL_RECORD_FOLDER, null, null);

            if (folderCursor != null) 
            {
                if (folderCursor.moveToFirst()) 
                {
                    do 
                    {
                        String folderName = "";
                        if(folderCursor.getLong(NOTE_COLUMN_ID) == Notes.ID_CALL_RECORD_FOLDER) 
                        {
                            folderName = mContext.getString(R.string.milletnote_call_record_folder_name);
                        } 
                        else 
                        {
                            folderName = folderCursor.getString(NOTE_COLUMN_SNIPPET);
                        }
                        if (!TextUtils.isEmpty(folderName)) 
                        {
                            ps.println(String.format(getFormat(FORMAT_FOLDER_NAME), folderName));
                        }
                        String folderId = folderCursor.getString(NOTE_COLUMN_ID);
                        exportFolderToText(folderId, ps);
                    } while (folderCursor.moveToNext());
                }
                folderCursor.close();
            }

            Cursor noteCursor = mContext.getContentResolver().query(Notes.CONTENT_NOTE_URI, NOTE_PROJECTION,
                    NoteColumns.TYPE + "=" + +Notes.TYPE_NOTE + " AND " + NoteColumns.PARENT_ID + "=0", null, null);

            if (noteCursor != null) 
            {
                if (noteCursor.moveToFirst()) 
                {
                    do 
                    {
                        ps.println(String.format(getFormat(FORMAT_NOTE_DATE), DateFormat.format(
                                mContext.getString(R.string.milletnote_format_datetime_mdhm),
                                noteCursor.getLong(NOTE_COLUMN_MODIFIED_DATE))));
                        String noteId = noteCursor.getString(NOTE_COLUMN_ID);
                        exportNoteToText(noteId, ps);
                    } while (noteCursor.moveToNext());
                }
                noteCursor.close();
            }
            ps.close();
            return STATE_SUCCESS;
        }
        
        private PrintStream getExportToTextPrintStream() 
        {
            File file = generateFileMountedOnSDcard(mContext, R.string.milletnote_file_path, R.string.milletnote_file_name_txt_format);
            if (file == null) 
            {
                return null;
            }
            mFileName = file.getName();
            mFileDirectory = mContext.getString(R.string.milletnote_file_path);
            PrintStream ps = null;
            try 
            {
                FileOutputStream fos = new FileOutputStream(file);
                ps = new PrintStream(fos);
            } 
            catch (Exception e) 
            {
                e.printStackTrace();
                return null;
            } 
            return ps;
        }
    }

    private static File generateFileMountedOnSDcard(Context context, int filePathResId, int fileNameFormatResId) 
    {
        StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStorageDirectory());
        sb.append(context.getString(filePathResId));
        File filedir = new File(sb.toString());
        sb.append(context.getString(fileNameFormatResId, DateFormat.format(context.getString(R.string.milletnote_format_date_ymd),
                        System.currentTimeMillis())));
        File file = new File(sb.toString());

        try 
        {
            if (!filedir.exists()) 
            {
                filedir.mkdir();
            }
            if (!file.exists()) 
            {
                file.createNewFile();
            }
            return file;
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        } 
        return null;
    }
}
