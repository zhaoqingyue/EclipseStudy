package com.zhaoqy.app.demo.notebook.milletnotes.gtask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import com.zhaoqy.app.demo.notebook.milletnotes.activity.NotesSettingsActivity;
import com.zhaoqy.app.demo.notebook.milletnotes.util.GTaskUtil;

@SuppressLint("DefaultLocale")
public class GTaskClient 
{
    private static final String GTASK_URL      = "https://mail.google.com/tasks/";
    private static final String GTASK_GET_URL  = "https://mail.google.com/tasks/ig";
    private static final String GTASK_POST_URL = "https://mail.google.com/tasks/r/ig";

    private static GTaskClient mInstance = null;
    private DefaultHttpClient  mHttpClient;
    private Account            mAccount;
    private JSONArray          mUpdateArray;
    private String             mGetUrl;
    private String             mPostUrl;
    private boolean            mLoggedin;
    private long               mClientVersion;
    private long               mLastLoginTime;
    private int                mActionId;

    private GTaskClient() 
    {
        mHttpClient = null;
        mGetUrl = GTASK_GET_URL;
        mPostUrl = GTASK_POST_URL;
        mClientVersion = -1;
        mLoggedin = false;
        mLastLoginTime = 0;
        mActionId = 1;
        mAccount = null;
        mUpdateArray = null;
    }

    public static synchronized GTaskClient getInstance() 
    {
        if (mInstance == null) 
        {
            mInstance = new GTaskClient();
        }
        return mInstance;
    }

    @SuppressLint("DefaultLocale")
	public boolean login(Activity activity) 
    {
        final long interval = 1000 * 60 * 5;
        if (mLastLoginTime + interval < System.currentTimeMillis()) 
        {
            mLoggedin = false;
        }

        if (mLoggedin && !TextUtils.equals(getSyncAccount().name, NotesSettingsActivity.getSyncAccountName(activity))) 
        {
            mLoggedin = false;
        }

        if (mLoggedin) 
        {
            return true;
        }

        mLastLoginTime = System.currentTimeMillis();
        String authToken = loginGoogleAccount(activity, false);
        if (authToken == null) 
        {
            return false;
        }

        if (!(mAccount.name.toLowerCase().endsWith("gmail.com") || mAccount.name.toLowerCase().endsWith("googlemail.com"))) 
        {
            StringBuilder url = new StringBuilder(GTASK_URL).append("a/");
            int index = mAccount.name.indexOf('@') + 1;
            String suffix = mAccount.name.substring(index);
            url.append(suffix + "/");
            mGetUrl = url.toString() + "ig";
            mPostUrl = url.toString() + "r/ig";

            if (tryToLoginGtask(activity, authToken)) 
            {
                mLoggedin = true;
            }
        }

        if (!mLoggedin) 
        {
            mGetUrl = GTASK_GET_URL;
            mPostUrl = GTASK_POST_URL;
            if (!tryToLoginGtask(activity, authToken)) 
            {
                return false;
            }
        }

        mLoggedin = true;
        return true;
    }

    private String loginGoogleAccount(Activity activity, boolean invalidateToken) 
    {
        String authToken;
        AccountManager accountManager = AccountManager.get(activity);
        Account[] accounts = accountManager.getAccountsByType("com.google");

        if (accounts.length == 0) 
        {
            return null;
        }

        String accountName = NotesSettingsActivity.getSyncAccountName(activity);
        Account account = null;
        for (Account a : accounts) 
        {
            if (a.name.equals(accountName)) 
            {
                account = a;
                break;
            }
        }
        if (account != null) 
        {
            mAccount = account;
        } 
        else 
        {
            return null;
        }

        AccountManagerFuture<Bundle> accountManagerFuture = accountManager.getAuthToken(account, "goanna_mobile", null, 
        		activity, null, null);
        try 
        {
            Bundle authTokenBundle = accountManagerFuture.getResult();
            authToken = authTokenBundle.getString(AccountManager.KEY_AUTHTOKEN);
            if (invalidateToken) 
            {
                accountManager.invalidateAuthToken("com.google", authToken);
                loginGoogleAccount(activity, false);
            }
        } 
        catch (Exception e) 
        {
            authToken = null;
        }

        return authToken;
    }

    private boolean tryToLoginGtask(Activity activity, String authToken) 
    {
        if (!loginGtask(authToken)) 
        {
            authToken = loginGoogleAccount(activity, true);
            if (authToken == null) 
            {
                return false;
            }

            if (!loginGtask(authToken)) 
            {
                return false;
            }
        }
        return true;
    }

    private boolean loginGtask(String authToken) 
    {
        int timeoutConnection = 10000;
        int timeoutSocket = 15000;
        HttpParams httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
        mHttpClient = new DefaultHttpClient(httpParameters);
        BasicCookieStore localBasicCookieStore = new BasicCookieStore();
        mHttpClient.setCookieStore(localBasicCookieStore);
        HttpProtocolParams.setUseExpectContinue(mHttpClient.getParams(), false);

        try 
        {
            String loginUrl = mGetUrl + "?auth=" + authToken;
            HttpGet httpGet = new HttpGet(loginUrl);
            HttpResponse response = null;
            response = mHttpClient.execute(httpGet);

            List<Cookie> cookies = mHttpClient.getCookieStore().getCookies();
            boolean hasAuthCookie = false;
            for (Cookie cookie : cookies) 
            {
                if (cookie.getName().contains("GTL")) 
                {
                    hasAuthCookie = true;
                }
            }
            if (!hasAuthCookie) 
            {
            }

            String resString = getResponseContent(response.getEntity());
            String jsBegin = "_setup(";
            String jsEnd = ")}</script>";
            int begin = resString.indexOf(jsBegin);
            int end = resString.lastIndexOf(jsEnd);
            String jsString = null;
            if (begin != -1 && end != -1 && begin < end) 
            {
                jsString = resString.substring(begin + jsBegin.length(), end);
            }
            JSONObject js = new JSONObject(jsString);
            mClientVersion = js.getLong("v");
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            return false;
        } 
        return true;
    }

    private int getActionId() 
    {
        return mActionId++;
    }

    private HttpPost createHttpPost() 
    {
        HttpPost httpPost = new HttpPost(mPostUrl);
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        httpPost.setHeader("AT", "1");
        return httpPost;
    }

    @SuppressWarnings("resource")
	private String getResponseContent(HttpEntity entity) throws IOException 
    {
        String contentEncoding = null;
        if (entity.getContentEncoding() != null) 
        {
            contentEncoding = entity.getContentEncoding().getValue();
        }

        InputStream input = entity.getContent();
        if (contentEncoding != null && contentEncoding.equalsIgnoreCase("gzip")) 
        {
            input = new GZIPInputStream(entity.getContent());
        } 
        else if (contentEncoding != null && contentEncoding.equalsIgnoreCase("deflate")) 
        {
            Inflater inflater = new Inflater(true);
            input = new InflaterInputStream(entity.getContent(), inflater);
        }

        try 
        {
            InputStreamReader isr = new InputStreamReader(input);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();

            while (true) 
            {
                String buff = br.readLine();
                if (buff == null) 
                {
                    return sb.toString();
                }
                sb = sb.append(buff);
            }
        } 
        finally 
        {
            input.close();
        }
    }

    private JSONObject postRequest(JSONObject js) throws Exception 
    {
        if (!mLoggedin) 
        {
            throw new Exception("not logged in");
        }

        HttpPost httpPost = createHttpPost();
        try 
        {
            LinkedList<BasicNameValuePair> list = new LinkedList<BasicNameValuePair>();
            list.add(new BasicNameValuePair("r", js.toString()));
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "UTF-8");
            httpPost.setEntity(entity);

            HttpResponse response = mHttpClient.execute(httpPost);
            String jsString = getResponseContent(response.getEntity());
            return new JSONObject(jsString);

        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            throw new Exception("postRequest failed");
        } 
    }

    public void createTask(Task task) throws Exception 
    {
        commitUpdate();
        try 
        {
            JSONObject jsPost = new JSONObject();
            JSONArray actionList = new JSONArray();
            actionList.put(task.getCreateAction(getActionId()));
            jsPost.put(GTaskUtil.GTASK_JSON_ACTION_LIST, actionList);
            jsPost.put(GTaskUtil.GTASK_JSON_CLIENT_VERSION, mClientVersion);
            JSONObject jsResponse = postRequest(jsPost);
            JSONObject jsResult = (JSONObject) jsResponse.getJSONArray( GTaskUtil.GTASK_JSON_RESULTS).get(0);
            task.setGid(jsResult.getString(GTaskUtil.GTASK_JSON_NEW_ID));
        } 
        catch (JSONException e) 
        {
            e.printStackTrace();
        }
    }

    public void createTaskList(TaskList tasklist) throws Exception 
    {
        commitUpdate();
        try 
        {
            JSONObject jsPost = new JSONObject();
            JSONArray actionList = new JSONArray();
            actionList.put(tasklist.getCreateAction(getActionId()));
            jsPost.put(GTaskUtil.GTASK_JSON_ACTION_LIST, actionList);
            jsPost.put(GTaskUtil.GTASK_JSON_CLIENT_VERSION, mClientVersion);
            JSONObject jsResponse = postRequest(jsPost);
            JSONObject jsResult = (JSONObject) jsResponse.getJSONArray(GTaskUtil.GTASK_JSON_RESULTS).get(0);
            tasklist.setGid(jsResult.getString(GTaskUtil.GTASK_JSON_NEW_ID));
        } 
        catch (JSONException e) 
        {
            e.printStackTrace();
        }
    }

    public void commitUpdate() throws Exception 
    {
        if (mUpdateArray != null) 
        {
            try 
            {
                JSONObject jsPost = new JSONObject();
                jsPost.put(GTaskUtil.GTASK_JSON_ACTION_LIST, mUpdateArray);
                jsPost.put(GTaskUtil.GTASK_JSON_CLIENT_VERSION, mClientVersion);
                postRequest(jsPost);
                mUpdateArray = null;
            } 
            catch (Exception e) 
            {
                e.printStackTrace();
            }
        }
    }

    public void addUpdateNode(Node node) throws Exception 
    {
        if (node != null) 
        {
            if (mUpdateArray != null && mUpdateArray.length() > 10) 
            {
                commitUpdate();
            }

            if (mUpdateArray == null)
            {
            	mUpdateArray = new JSONArray();
            }
            mUpdateArray.put(node.getUpdateAction(getActionId()));
        }
    }

    public void moveTask(Task task, TaskList preParent, TaskList curParent) throws Exception 
    {
        commitUpdate();
        try 
        {
            JSONObject jsPost = new JSONObject();
            JSONArray actionList = new JSONArray();
            JSONObject action = new JSONObject();
            action.put(GTaskUtil.GTASK_JSON_ACTION_TYPE, GTaskUtil.GTASK_JSON_ACTION_TYPE_MOVE);
            action.put(GTaskUtil.GTASK_JSON_ACTION_ID, getActionId());
            action.put(GTaskUtil.GTASK_JSON_ID, task.getGid());
            if (preParent == curParent && task.getPriorSibling() != null) 
            {
                action.put(GTaskUtil.GTASK_JSON_PRIOR_SIBLING_ID, task.getPriorSibling());
            }
            action.put(GTaskUtil.GTASK_JSON_SOURCE_LIST, preParent.getGid());
            action.put(GTaskUtil.GTASK_JSON_DEST_PARENT, curParent.getGid());
            if (preParent != curParent) 
            {
                action.put(GTaskUtil.GTASK_JSON_DEST_LIST, curParent.getGid());
            }
            actionList.put(action);
            jsPost.put(GTaskUtil.GTASK_JSON_ACTION_LIST, actionList);
            jsPost.put(GTaskUtil.GTASK_JSON_CLIENT_VERSION, mClientVersion);
            postRequest(jsPost);
        } 
        catch (JSONException e) 
        {
            e.printStackTrace();
        }
    }

    public void deleteNode(Node node) throws Exception 
    {
        commitUpdate();
        try 
        {
            JSONObject jsPost = new JSONObject();
            JSONArray actionList = new JSONArray();
            node.setDeleted(true);
            actionList.put(node.getUpdateAction(getActionId()));
            jsPost.put(GTaskUtil.GTASK_JSON_ACTION_LIST, actionList);
            jsPost.put(GTaskUtil.GTASK_JSON_CLIENT_VERSION, mClientVersion);
            postRequest(jsPost);
            mUpdateArray = null;
        } 
        catch (JSONException e) 
        {
            e.printStackTrace();
        }
    }

    public JSONArray getTaskLists() throws Exception 
    {
        try 
        {
            HttpGet httpGet = new HttpGet(mGetUrl);
            HttpResponse response = null;
            response = mHttpClient.execute(httpGet);
            String resString = getResponseContent(response.getEntity());
            String jsBegin = "_setup(";
            String jsEnd = ")}</script>";
            int begin = resString.indexOf(jsBegin);
            int end = resString.lastIndexOf(jsEnd);
            String jsString = null;
            if (begin != -1 && end != -1 && begin < end) 
            {
                jsString = resString.substring(begin + jsBegin.length(), end);
            }
            JSONObject js = new JSONObject(jsString);
            return js.getJSONObject("t").getJSONArray(GTaskUtil.GTASK_JSON_LISTS);
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        } 
        return null;
    }

    public JSONArray getTaskList(String listGid) throws Exception 
    {
        commitUpdate();
        try 
        {
            JSONObject jsPost = new JSONObject();
            JSONArray actionList = new JSONArray();
            JSONObject action = new JSONObject();
            action.put(GTaskUtil.GTASK_JSON_ACTION_TYPE,  GTaskUtil.GTASK_JSON_ACTION_TYPE_GETALL);
            action.put(GTaskUtil.GTASK_JSON_ACTION_ID, getActionId());
            action.put(GTaskUtil.GTASK_JSON_LIST_ID, listGid);
            action.put(GTaskUtil.GTASK_JSON_GET_DELETED, false);
            actionList.put(action);
            jsPost.put(GTaskUtil.GTASK_JSON_ACTION_LIST, actionList);
            jsPost.put(GTaskUtil.GTASK_JSON_CLIENT_VERSION, mClientVersion);
            JSONObject jsResponse = postRequest(jsPost);
            return jsResponse.getJSONArray(GTaskUtil.GTASK_JSON_TASKS);
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        return null;
    }

    public Account getSyncAccount() 
    {
        return mAccount;
    }

    public void resetUpdateArray() 
    {
        mUpdateArray = null;
    }
}
