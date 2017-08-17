package com.muicsenior.warehouse.libraries;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.muicsenior.warehouse.libraries.callbacker.AbstractCallback;
import com.muicsenior.warehouse.libraries.callbacker.Callbackable;
import com.muicsenior.warehouse.libraries.callbacker.CallbackerJSON;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by ta on 27/3/2015.
 */
public class HttpConnector {

    static final int TIMEOUT = 60;
    static final int UITIMEOUT=15;
    public enum RequestMethod{
        METHOD_GET,
        METHOD_POST,
        METHOD_PUT,
        METHOD_DELETE
    }

    private Context mContext;
    private OkHttpClient client = new OkHttpClient();
    public Call call;
    private ArrayList<RequestRecorder> queue;
    private ArrayList<CallbackerJSON> queue_response;
    private int queue_counter;
    private CallbackerJSON beforeAction, afterAction;
    private static boolean disableConnection = false;
    private boolean immortalFlag = false;
    private static String activityName;
    private HashMap<String, ArrayList<Integer>> taskIDNamespacer = new HashMap<>();

    private static HttpConnector instance;

    public static HttpConnector newInstance(){
        return new HttpConnector();
    }

    public static HttpConnector getInstance(){
        return getInstance(false);
    }

    public static HttpConnector getInstance(boolean createNew){
        if(createNew) {
            return newInstance();
        }
        if(instance == null) {
            instance = newInstance();
        }
        return instance;
    }

    public static void setActivityName (Activity activity){
        activityName = activity.getClass().getSimpleName();
    }

    public static void setActivityName (String name){
        activityName = name;
    }

    public static HttpConnector connect(){
        return getInstance();
    }

    private String url = "";
    private Callbackable success, fail;

    public HttpConnector(){
        mContext = Contextor.getInstance().getContext();
        success = fail = new Callbackable(){
            @Override
            public void success(String res){

            }

            @Override
            public void fail(int code, String res){

            }

            @Override
            public void noResponse(){

            }
        };

        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        client.setCookieHandler(cookieManager);

        beforeAction = afterAction = new CallbackerJSON(){
            @Override
            public void success(JSON o){

            }
        };
        //test();
    }

    public void setBeforeAction(CallbackerJSON c){
        beforeAction = c;
    }

    public void setAfterAction(CallbackerJSON c){
        afterAction = c;
    }

    /* TASK Manager */
    private HashMap<Integer, Task> _tasks = new HashMap<>();

    public void cancelTask(int i){
        if(_tasks.containsKey(i)) {
            _tasks.get(i).task.cancel(true);
            _tasks.remove(i);
        }
    }

    public void cancelAllTasks(){
        taskIDNamespacer.clear();
        Set<Integer> integers = _tasks.keySet();

        for(Integer k : integers) {
            Log.e("aaa", "HttpConnector clear task no.=" + k + (_tasks.get(k).immortal ? " - cannot cancel: เป็นอมตะ ไม่มีอำนาจใดมาลบล้างได้" : ""));
            if(_tasks.get(k) != null && !_tasks.get(k).immortal)
                _tasks.get(k).task.cancel(true);
        }
        _tasks.clear();
    }


    public int cancelAllTask(){
        if (!taskIDNamespacer.containsKey(activityName)) return -1;
        Log.i("aaa", "canceling task: "+ activityName);
        ArrayList<Integer> idList = taskIDNamespacer.get(activityName);

        int total = idList.size();
        Integer[] key = new Integer[total];
        key = idList.toArray(key);
        for(Integer k : key) {
            if(_tasks.get(k) != null)
                Log.e("aaa", "HttpConnector clear task no.=" + k + (_tasks.get(k).immortal ? " - cannot cancel: เป็นอมตะ ไม่มีอำนาจใดมาลบล้างได้" : ""));
            if(_tasks.get(k) != null && !_tasks.get(k).immortal)
                _tasks.get(k).task.cancel(true);
        }
        _tasks.clear();
        return total;
    }

    public int cancelAllTaskName(String taskNamespace){
        if (!taskIDNamespacer.containsKey(taskNamespace)) return -1;
        ArrayList<Integer> idList = taskIDNamespacer.get(taskNamespace);
        Log.i("aaa", "canceling task: "+ taskNamespace + " " + idList.toString());

        int total = idList.size();
        Integer[] key = new Integer[total];
        key = idList.toArray(key);
        for(Integer k : key) {
            if(_tasks.get(k) != null && !_tasks.get(k).immortal)
                _tasks.get(k).task.cancel(true);
        }
        _tasks.clear();
        taskIDNamespacer.remove(taskNamespace);
        return total;
    }

    public HttpConnector immortal(){
        immortalFlag = true;
        return this;
    }

    /*public void test2(){
        try {
            if(true)
                return;
            String json = "{" +
                    "\"a\":{" +
                    "\"a2\":\"test\", " +
                    "\"a3\":true" +
                    "}, " +
                    "\"b\":[10,20,30,40,50]," +
                    "\"c\": \"2014-11-03T10:20:30+07:00\"" +
                    "}";
            /*JSONObject obj = new JSONObject(json);
            String pageName = (String)obj.getJSONObject("a").get("a2");
            Log.i("aaa", "TEST MY AJAX JSON 0 :: " + pageName);
            //Log.i("aaa", "TEST MY AJAX JSON :: " + obj.getJSONObject("b").getJSONArray("a2").toString());
            //Log.i("aaa", "TEST MY AJAX JSON :: " + obj.get("b").toString());
            String ttt = "{\"aaa\":" + new JSONObject(json).get("b").toString() + "}";
            Log.i("aaa", "TEST MY AJAX JSON 0 :: " + new JSONObject(ttt).getJSONArray("aaa").get(1).toString());* /

            JSON o = new JSON(json);
            Log.i("aaa", "TEST MY AJAX JSON 1 :: " + o);
            Log.i("aaa", "TEST MY AJAX JSON 2 :: " + o.get("a"));
            Log.i("aaa", "TEST MY AJAX JSON 3 :: " + o.get("a").regis("test").get("a2"));
            Log.i("aaa", "TEST MY AJAX JSON 3 :: " + o.get("a").get("a3", boolean.class));
            Log.i("aaa", "TEST MY AJAX JSON 4 :: " + o.get("b"));
            Log.i("aaa", "TEST MY AJAX JSON 5 :: " + o.get("b").get(1));
            Log.i("aaa", "TEST MY AJAX JSON 6 :: " + o.get("b").length());

            JSON o2 = new JSON(o.get("b").toString());

            Log.i("aaa", "TEST MY AJAX JSON 7 :: " + o2.get(1));
            Log.i("aaa", "TEST MY AJAX JSON 8 :: " + JSON.register.get("test"));

            /*JSONArray arr = o.get("b", JSONArray.class);
            arr.put(0, o.get("c"));
            JSON o3 = new JSON(arr);* /

            Log.i("aaa", "TEST MY AJAX JSON 9 :: " + o.get("b").get(2, int.class));

            for(int i : o.get("b", int[].class)) {
                Log.i("aaa", "TEST MY AJAX JSON 10 :: " + i);
            }

            //GET("https://app.dek-d.com/api/v1.0/auth?a=q", Pair.attach("bp",1).with("test", "โมโม").build(), new CallbackerJSON(){
            //    @Override
            //    public void success(JSON o){
            //        String token = o.get("data").get("token", String.class);
            //        Log.i("aaa", "TOKEN :: " + token);
            //    }
            //});

        } catch(Exception e) {
            e.printStackTrace();
        }

    }*/

    /*public void test(){

        String json = " {\"submit_date\":\"2014-04-29T00:00:00+07:00\",\"view_day\":0,\"type\":\"story_long\",\"isban\":false,\"is_end\":false,\"id\":1153215,\"rating_sum\":21,\"username\":\"lovelyniya\",\"title\":\"บำเรอรักเมียสมอ้าง\",\"category_main_title\":\"ฟรีสไตล์\",\"chapter\":41,\"last_update\":\"2015-04-06T20:53:37+07:00\",\"category_sub_title\":\"นิยายซึ้งกินใจ\",\"description\":\"เมื่อผู้หญิงไร้ยางอายได้กลายมาเป็นแม่ของลูก มหาเศรษฐีหนุ่มหล่อจึงต้องเอาตัวเองไปผูกติดกับเธอเอาไว้ ด้วยเหตุผล...เพื่อลูก แต่ทำไปทำมา เขาอยากจะทำลูกคนที่สองกับเธอเสียอย่างนั้น โหดหื่นฮา 18++ ค่า\",\"review_score\":907,\"user_id\":2969736,\"view_all\":790926,\"writer\":\"นิยา เบรานี่\",\"view_month\":100257,\"published\":false,\"tags\":[\"ตบจูบ\",\"ตบ\",\"จูบ\",\"บีบคั้น\",\"หน่วง\",\"โหด\",\"หื่น\",\"ฮา\",\"มาม่า\",\"บำเรอ\",\"เมีย\",\"ปลอมตัว\",\"สลับตัว\",\"น้ำเน่า\",\"เด็ก\",\"เด็กผู้ชาย\",\"น่ารัก\",\"รักเด็ก\"],\"vote\":5,\"comment_all\":3973,\"favorite\":10209,\"islock\":true,\"isbad\":true,\"vote_all\":99,\"category_main\":1,\"allow_comment\":true,\"rating\":4,\"category_sub\":3,\"comment\":3973,\"thumb\":\"http:\\/\\/image.dek-d.com\\/27\\/0296\\/9736\\/\\/118092590\"}";

        JSON o = new JSON(json);
        Log.i("aaa", "TEST MY AJAX JSON 1 :: " + o.get("writer"));
    }*/

    /*public void test3(){
        Log.i("aaa", "bbbb");
        loadUrl(RequestMethod.METHOD_DELETE, "http://wwwback.dek-d.com/api/v1.1/me/favorite/novel?bp=1", Pair.attach("test", "momo").build(), new CallbackerJSON(){
            @Override
            public void success(JSON o){

            }
        });
    }*/

    private CallbackerJSON dequeue(){
        return new CallbackerJSON(){
            @Override
            public void success(JSON o){

            }
        };
    }

    public Task GET(String url){
        Callbackable callback = queue == null ? new CallbackerJSON(){
            @Override
            public void success(JSON o){

            }
        } : dequeue();
        return loadUrl(RequestMethod.METHOD_GET, url, Pair.noop().build(), callback);
    }

    public Task GET(String url, Map<String, String> params){
        Callbackable callback = new CallbackerJSON(){
            @Override
            public void success(JSON o){

            }
        };
        return loadUrl(RequestMethod.METHOD_GET, url, params, callback);
    }

    /*
     * =============================================
     *  GET
     * =============================================
     */

    public Task GET(String url, Callbackable callback){
        return loadUrl(RequestMethod.METHOD_GET, url, Pair.noop().build(), callback);
    }

    public Task GET(String url, Map<String, String> params, Callbackable callback){
        return loadUrl(RequestMethod.METHOD_GET, url, params, callback);
    }

    /*
     * =============================================
     *  POST
     * =============================================
     */

    public Task POST(String url, Callbackable callback){
        return loadUrl(RequestMethod.METHOD_POST, url, Pair.noop().build(), callback);
    }

    public Task POST(String url, Map<String, String> params, Callbackable callback){
        return loadUrl(RequestMethod.METHOD_POST, url, params, callback);
    }

    /*
     * =============================================
     *  PUT
     * =============================================
     */

    public Task PUT(String url, Callbackable callback){
        return loadUrl(RequestMethod.METHOD_PUT, url, Pair.noop().build(), callback);
    }

    public Task PUT(String url, Map<String, String> params, Callbackable callback){
        return loadUrl(RequestMethod.METHOD_PUT, url, params, callback);
    }

    /*
     * =============================================
     *  DELETE
     * =============================================
     */

    public Task DELETE(String url, Callbackable callback){
        return loadUrl(RequestMethod.METHOD_DELETE, url, Pair.noop().build(), callback);
    }

    public Task DELETE(String url, Map<String, String> params, Callbackable callback){
        return loadUrl(RequestMethod.METHOD_DELETE, url, params, callback);
    }

    /*
     * =============================================
     *  load connect
     * =============================================
     */

    public static void setEnableConnection(boolean enable){
        disableConnection = !enable;
    }

    public static boolean isNetworkOnline(){

        if(disableConnection) {
            return false;
        }

        boolean status = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) Contextor.getInstance().getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getNetworkInfo(0);
            if(netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
                status = true;
            }
            else {
                netInfo = cm.getNetworkInfo(1);
                if(netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED)
                    status = true;
            }
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
        return status;
    }


    private static int ajax_task_id = 1;

    protected Task loadUrl(RequestMethod method, String url, Map<String, String> postData, final Callbackable callback){

        final int task_id = ajax_task_id++;

        Log.i("aaa", "start loadURL: [" + task_id + "]");

        if(!isNetworkOnline()) {
            Log.e("aaa", "connect : network offline");
            if(beforeAction != null) {
                beforeAction.noConnection();
                callComplete(beforeAction);
            }
            if(callback != null) {
                callback.noResponse();
                callComplete(callback);
            }
            return null;
        }

        Request request;
        if(method == RequestMethod.METHOD_GET) {
            if(postData.size() > 0) {
                url += url.indexOf("?") == -1 ? "?" : "&";
                url += mapToPostString(postData);
            }
            /*for(Map.Entry<String, String> entry : postData.entrySet()) {
                url += entry.getKey() + (entry.getValue() == null ? "" : "=" + entry.getValue()) + "&";
            }
            url = url.substring(0, url.length() - 1);  */

            request = new Request.Builder().url(url).build();
        }
        else {
            Map<String, String> postParams = new HashMap<String, String>();
            if(postData != null) {
                for(Map.Entry<String, String> entry : postData.entrySet()) {
                    postParams.put(entry.getKey(), entry.getValue());
                }
            }
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
            RequestBody body = RequestBody.create(mediaType, mapToPostString(postParams));

            if(method == RequestMethod.METHOD_POST) {
                request = new Request.Builder().url(url).post(body).build();
            }
            else if(method == RequestMethod.METHOD_PUT) {
                request = new Request.Builder().url(url).put(body).build();
            }
            else if(method == RequestMethod.METHOD_DELETE) {
                request = new Request.Builder().url(url).method("DELETE", body).build();
            }
            else {
                return null;
            }
        }

        Log.i("aaa", "connect [" + task_id + "] : prepare request complete");

        client.setConnectTimeout(_timeout(), TimeUnit.SECONDS);
        client.setReadTimeout(_timeout(), TimeUnit.SECONDS);
        Call call = client.newCall(request);

        final HTTPRequestData data = new HTTPRequestData();
        data.url = url;
        data.method = method;
        data.postData = postData;
        data.call = call;

        try {
            String debug_param = "", n_debug = "\n";
            if(postData.size() > 0) {
                n_debug = "\n";
                debug_param += '\n';
                Map<String, String> postParams = new HashMap<String, String>();
                if(postData != null) {
                    for(Map.Entry<String, String> entry : postData.entrySet()) {
                        postParams.put(entry.getKey(), entry.getValue());
                    }
                }
                for(Map.Entry<String, String> entry : postParams.entrySet()) {
                    try {
                        //content.append(URLEncoder.encode(entry.getKey(), "UTF-8")).append('=').append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                        if(entry.getKey().equals("password") || entry.getKey().equals("client_secret")) {
                            debug_param += "        - " + encodeURI(entry.getKey()) + '=' + (entry.getValue().charAt(0) + "*****อ๊ะ! ตรงนี้เป็นพาสเวิร์ด ห้ามดูนะจ๊ะ*****" + entry.getValue().charAt(entry.getValue().length() - 1)) + '\n';
                        }
                        else {
                            debug_param += "        - " + encodeURI(entry.getKey()) + '=' + encodeURI(entry.getValue()) + '\n';
                        }
                    } catch(Exception e) {
                    }
                }
            }
            Log.i("aaa", "url: (" + method + ") " + n_debug + "[ " + task_id + " ] " + url + debug_param);
        } catch(Exception eeee) {
            eeee.printStackTrace();
        }

        Log.i("aaa", "connect [" + task_id + "] : prepare parems complete");
        AsyncTask curTask;

        HTTPRequestTask httpRequestTask = new HTTPRequestTask(new HTTPRequestListener() {
            @Override
            public void onMessageReceived(int statusCode, String message) {
                Log.i("aaa", "[" + task_id + "] onMessageReceived " + statusCode + ":" + message);
                cancelTask(task_id);
                if (callback != null) {
                    JSON j = new JSON(message);
                    if (beforeAction != null) {
                        beforeAction.success(j);
                        callComplete(beforeAction);
                    }
                    callback.success(message);
                    callComplete(callback);
                    if (afterAction != null) {
                        afterAction.success(j);
                        callComplete(afterAction);
                    }
                }
            }

            @Override
            public void onMessageError(int statusCode, String message) {
                Log.e("aaa", "[" + task_id + "] onMessageError " + statusCode + ":" + message);
                Log.i("debug_login","message error : " + statusCode + ":" + message);
                cancelTask(task_id);
                if (callback != null) {
                    if (beforeAction != null) {
                        beforeAction.fail(statusCode, message);
                        callComplete(beforeAction);
                    }
                    if (statusCode == 0) {
                        callback.noResponse();
                    } else {
                        callback.fail(statusCode, message);
                    }
                    callComplete(callback);
                    if (afterAction != null) {
                        afterAction.fail(statusCode, message);
                        callComplete(afterAction);
                    }
                }
            }
        });

        Log.i("aaa", "connect [" + task_id + "] : prepare task complete");

        try {
            Log.i("aaa", "connect task: [" + task_id + "]");
            curTask = httpRequestTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, data);
        } catch (Exception e) {
            e.printStackTrace();
            ((ThreadPoolExecutor)AsyncTask.THREAD_POOL_EXECUTOR).shutdownNow();
            Log.i("aaa", "connect task: [" + task_id + "]");
            curTask = httpRequestTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, data);
        }

        this.call = call;

        Task t = new Task(task_id, curTask);
        _tasks.put(task_id, t);
        registerTaskID (task_id);

        if(immortalFlag) {
            t.immortal(true);
            immortalFlag = false;
        }

        Log.i("aaa", "connect end: [" + task_id + "]");
        return t;
    }

    private void registerTaskID (int task_id) {
        if (activityName == null || activityName.isEmpty()) return ;
        if (taskIDNamespacer.containsKey(activityName)) {
            ArrayList<Integer> idList = taskIDNamespacer.get(activityName);
            idList.add(task_id);
        } else {
            ArrayList<Integer> idList = new ArrayList<>();
            idList.add(task_id);
            taskIDNamespacer.put(activityName, idList);
        }
    }

    private int _timeout(){
        return TIMEOUT;
        //return Utils.random(1, 5) == 1 ? 1 : TIMEOUT;
    }

    private void callComplete(Callbackable c){
        if(c instanceof AbstractCallback) {
            //Log.i("aaa", "HttpConnector . call complete " + c.getClass());
            ((AbstractCallback) c).complete();
        }
    }

    protected String mapToPostString(Map<String, String> data){
        StringBuilder content = new StringBuilder();
        for(Map.Entry<String, String> entry : data.entrySet()) {
            if(content.length() > 0) {
                content.append('&');
            }
            try {
                //content.append(URLEncoder.encode(entry.getKey(), "UTF-8")).append('=').append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                content.append(encodeURI(entry.getKey())).append('=').append(encodeURI(entry.getValue()));
            } catch(Exception e) {
                //throw new AssertionError(e);
            }
        }
        return content.toString();
    }

    public static String encodeURI(String s){
        try {
            s = URLEncoder.encode(s, "UTF-8");
        } catch(Exception e) {
            s = "";
        }
        return s;
    }

    /**
     *
     */
    public class HTTPRequestData{

        public String url;
        public HttpConnector.RequestMethod method;
        public Map<String, String> postData;
        public Call call;

    }

    /**
     *
     */
    public class HTTPRequestTask extends AsyncTask<HttpConnector.HTTPRequestData, Void, HTTPRequestTask.ContentMessage>{

        HTTPRequestListener mListener;

        public HTTPRequestTask(HTTPRequestListener aListener){
            mListener = aListener;
        }

        @Override
        protected ContentMessage doInBackground(HttpConnector.HTTPRequestData... params){
            HttpConnector.HTTPRequestData data = params[0];
            ContentMessage message = new ContentMessage();

            try {
                Log.i("aaa", "connect execute start: " + data.url);
                Response response = data.call.execute();
                Log.i("aaa", "connect execute complete: " + data.url);
                if(response.isSuccessful()) {
                    message.success = true;
                    message.statusCode = response.code();
                }
                else {
                    message.success = false;
                    message.statusCode = response.code();
                }
                message.body = response.body().string();
            } catch(IOException e) {
                e.printStackTrace();
                message.success = false;
            } catch(Exception e) {
                e.printStackTrace();
                message.success = false;
            }

            return message;
        }

        @Override
        protected void onPostExecute(ContentMessage s){
            super.onPostExecute(s);
            Log.i("aaa", "connect post execute");
            if(mListener != null) {
                if(s.success) {
                    mListener.onMessageReceived(s.statusCode, s.body);
                }
                else {
                    mListener.onMessageError(s.statusCode, s.body);
                }
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        public class ContentMessage{
            boolean success;
            int statusCode;
            String body;
        }
    }

    /**
     *
     */
    public interface HTTPRequestListener{

        public void onMessageReceived(int statusCode, final String message);

        public void onMessageError(int statusCode, final String message);

    }

    private class RequestRecorder{

        String url = null;
        Map<String, String> params = null;
        CallbackerJSON callback = null;

        public RequestRecorder(){

        }

        public RequestRecorder(String url, Map<String, String> params, CallbackerJSON callback){
            this.url = url;
            this.params = params;
            this.callback = callback;
        }
    }



    public static class Task{
        public final int taskId;
        public AsyncTask task;
        public boolean immortal = false;

        public Task(int taskId, AsyncTask task){
            this.taskId = taskId;
            this.task = task;
        }

        public boolean cancel(){
            getInstance().cancelTask(taskId);
            return true;
        }

        public void immortal(boolean flag){
            immortal = flag;
        }
    }

}
