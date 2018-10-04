package com.saiyi.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import java.util.Locale;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.JPushMessage;

/**
 * @author lingfeng 2017/12/21 16:58
 */

public class JPushManager {
    private static final String TAG = "JPUSH-SetAliasAndTags";
    public static int sequence = 1;
    /**
     * 增加
     */
    public static final int ACTION_ADD = 1;
    /**
     * 覆盖
     */
    public static final int ACTION_SET = 2;
    /**
     * 删除部分
     */
    public static final int ACTION_DELETE = 3;
    /**
     * 删除所有
     */
    public static final int ACTION_CLEAN = 4;
    /**
     * 查询
     */
    public static final int ACTION_GET = 5;

    public static final int ACTION_CHECK = 6;

    public static final int DELAY_SEND_ACTION = 1;
    private Context context;
    private TagAliasBean tagAliasBean = new TagAliasBean();

    private static JPushManager mInstance;

    private JPushManager() {
    }

    public static JPushManager getInstance() {
        if (mInstance == null) {
            synchronized (JPushManager.class) {
                if (mInstance == null) {
                    mInstance = new JPushManager();
                }
            }
        }
        return mInstance;
    }

    private void init(Context context) {
        if (context != null) {
            this.context = context.getApplicationContext();
        }
    }

    private SparseArray<TagAliasBean> tagAliasActionCache = new SparseArray<TagAliasBean>();


    /**
     * 初始化极光，一般可以放到程序的启动Activity或是Application的onCreate方法中调用
     *
     * @param b 设置是否开启日志,发布时请关闭日志RedDotImageView.java
     */
    public void initJPush(Context context, boolean b) {
        JPushInterface.setDebugMode(b); // 设置开启日志,发布时请关闭日志
        JPushInterface.init(context); // 初始化 JPush
    }

    /**
     * 退出极光，一般是程序退出登录时候，具体还是需要看项目的实际需求
     */
    public void stopJPush() {
        JPushInterface.stopPush(context);//调用了本方法后，JPush 推送服务完全被停止.所有的其他 API 调用都无效,不能通过 JPushInterface.init 恢复，需要调用resumePush恢复。
//        setAliasAndTags("", "");//通过清空别名来停止极光
    }

    public void resumePush() {
        JPushInterface.resumePush(context);
    }

    /**
     * 设置极光推送app别名
     * 用于给某特定用户推送消息。别名，可以近似地被认为，是用户帐号里的昵称 使用标签
     * 覆盖逻辑，而不是增量逻辑。即新的调用会覆盖之前的设置。
     *
     * @param alias
     */
    public void setAlias(Context conn, String alias) {

//        tagAliasBean.action = ACTION_SET;
//        tagAliasBean.isAliasAction = true;
//        tagAliasBean.alias =alias;
        setTagAliasBean(ACTION_SET, alias, null, true);
        setAliasAndTags(conn, tagAliasBean);
    }

    /**
     * 删除极光推送app别名
     */
    public void deleteAlias(Context conn) {

//        tagAliasBean.action = ACTION_DELETE;
//        tagAliasBean.isAliasAction = true;
        setTagAliasBean(ACTION_DELETE, null, null, true);
        setAliasAndTags(conn, tagAliasBean);
    }

    /**
     * 获取极光推送app别名
     */
    public void getAlias(Context conn) {
        setTagAliasBean(ACTION_GET, null, null, true);
        setAliasAndTags(conn, tagAliasBean);
    }

    /**
     * 设置标签
     * 用于给某一群人推送消息。标签类似于博客里为文章打上 tag ，即为某资源分类。
     */
    public void setTags(Context conn, Set<String> Tags) {
        setTagAliasBean(ACTION_SET, null, Tags, false);
        setAliasAndTags(conn, tagAliasBean);
    }

    /**
     * 添加标签
     */
    public void addTags(Context conn, Set<String> Tags) {
        setTagAliasBean(ACTION_ADD, null, Tags, false);
        setAliasAndTags(conn, tagAliasBean);
    }

    /**
     * 删除标签
     */
    public void deleteTags(Context conn, Set<String> Tags) {
        setTagAliasBean(ACTION_DELETE, null, Tags, false);
        setAliasAndTags(conn, tagAliasBean);
    }

    /**
     * 删除所有标签
     */
    public void cleanTags(Context conn) {
        setTagAliasBean(ACTION_CLEAN, null, null, false);
        setAliasAndTags(conn, tagAliasBean);
    }

    /**
     * 获取所有标签
     */
    public void getAllTags(Context conn) {
        setTagAliasBean(ACTION_GET, null, null, false);
        setAliasAndTags(conn, tagAliasBean);
    }

    /**
     * 查询标签状态
     */
    public void checkTags(Context conn, Set<String> Tags) {
        setTagAliasBean(ACTION_CHECK, null, Tags, false);
        setAliasAndTags(conn, tagAliasBean);
    }

    public void put(int sequence, TagAliasBean tagAliasBean) {
        tagAliasActionCache.put(sequence, tagAliasBean);
    }

    private Handler delaySendHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DELAY_SEND_ACTION:
                    if (msg.obj != null && msg.obj instanceof TagAliasBean) {
                        Log.i(TAG, "on delay time");
                        sequence++;
                        TagAliasBean tagAliasBean = (TagAliasBean) msg.obj;
                        tagAliasActionCache.put(sequence, tagAliasBean);
                        if (context != null) {
                            //setAliasAndTags 里的sequence会再自增一次，保持和上面的sequence一致这里再自减一次
                            sequence--;
                            setAliasAndTags(context, tagAliasBean);
                        } else {
                            Log.i(TAG, "#unexcepted - context was null");
                        }
                    } else {
                        Log.i(TAG, "#unexcepted - msg obj was incorrect");
                    }
                    break;
            }
        }
    };


    public void setAliasAndTags(Context context, TagAliasBean tagAliasBean) {

        init(context);
        if (tagAliasBean == null) {
            Log.i(TAG, "setAliasAndTags时tagAliasBean为null");
            return;
        }
        sequence++;
        put(sequence, tagAliasBean);
        if (tagAliasBean.isAliasAction) {
            switch (tagAliasBean.action) {
                case ACTION_GET:
                    JPushInterface.getAlias(context, sequence);
                    break;
                case ACTION_DELETE:
                    JPushInterface.deleteAlias(context, sequence);
                    break;
                case ACTION_SET:
                    JPushInterface.setAlias(context, sequence, tagAliasBean.alias);
                    break;
                default:
                    Log.i(TAG, "unsupport alias action type");
                    return;
            }
        } else {
            switch (tagAliasBean.action) {
                case ACTION_ADD:
                    JPushInterface.addTags(context, sequence, tagAliasBean.tags);
                    break;
                case ACTION_SET:
                    JPushInterface.setTags(context, sequence, tagAliasBean.tags);
                    break;
                case ACTION_DELETE:
                    JPushInterface.deleteTags(context, sequence, tagAliasBean.tags);
                    break;
                case ACTION_CHECK:
                    //一次只能check一个tag
                    String tag = (String) tagAliasBean.tags.toArray()[0];
                    JPushInterface.checkTagBindState(context, sequence, tag);
                    break;
                case ACTION_GET:
                    JPushInterface.getAllTags(context, sequence);
                    break;
                case ACTION_CLEAN:
                    JPushInterface.cleanTags(context, sequence);
                    break;
                default:
                    Log.i(TAG, "unsupport tag action type");
                    return;
            }
        }
    }

    public void onTagOperatorResult(Context context, JPushMessage jPushMessage) {
        int sequence = jPushMessage.getSequence();
        Log.i(TAG, "action - onTagOperatorResult, sequence:" + sequence + ",tags:" + jPushMessage.getTags());
        Log.i(TAG, "tags size:" + jPushMessage.getTags().size());
        init(context);
        //根据sequence从之前操作缓存中获取缓存记录
        TagAliasBean tagAliasBean = tagAliasActionCache.get(sequence);
        if (tagAliasBean == null) {


            return;
        }
        if (jPushMessage.getErrorCode() == 0) {
            Log.i(TAG, "action - modify tag Success,sequence:" + sequence);
            tagAliasActionCache.remove(sequence);
            String logs = getActionStr(tagAliasBean.action) + " tags success";
            Log.i(TAG, logs);
            Toast.makeText(context, logs, Toast.LENGTH_SHORT).show();
        } else {
            String logs = "Failed to " + getActionStr(tagAliasBean.action) + " tags";
            if (jPushMessage.getErrorCode() == 6018) {
                //tag数量超过限制,需要先清除一部分再add
                logs += ", tags is exceed limit need to clean";
            }
            logs += ", errorCode:" + jPushMessage.getErrorCode();
            Log.i(TAG, logs);
            if (!RetryActionIfNeeded(jPushMessage.getErrorCode(), tagAliasBean)) {
                Toast.makeText(context, logs, Toast.LENGTH_SHORT).show();

            }
        }
    }

    public void onCheckTagOperatorResult(Context context, JPushMessage jPushMessage) {
        int sequence = jPushMessage.getSequence();
        Log.i(TAG, "action - onCheckTagOperatorResult, sequence:" + sequence + ",checktag:" + jPushMessage.getCheckTag());
        init(context);
        //根据sequence从之前操作缓存中获取缓存记录
        TagAliasBean tagAliasBean = tagAliasActionCache.get(sequence);
        if (tagAliasBean == null) {
            Toast.makeText(context, "获取缓存记录失败", Toast.LENGTH_SHORT).show();

            return;
        }
        if (jPushMessage.getErrorCode() == 0) {
            Log.i(TAG, "tagBean:" + tagAliasBean);
            tagAliasActionCache.remove(sequence);
            String logs = getActionStr(tagAliasBean.action) + " tag " + jPushMessage.getCheckTag() + " bind state success,state:" + jPushMessage.getTagCheckStateResult();
            Log.i(TAG, logs);
            Toast.makeText(context, logs, Toast.LENGTH_SHORT).show();

        } else {
            String logs = "Failed to " + getActionStr(tagAliasBean.action) + " tags, errorCode:" + jPushMessage.getErrorCode();
            Log.i(TAG, logs);
            if (!RetryActionIfNeeded(jPushMessage.getErrorCode(), tagAliasBean)) {
                Toast.makeText(context, logs, Toast.LENGTH_SHORT).show();

            }
        }
    }

    public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
        int sequence = jPushMessage.getSequence();
        Log.i(TAG, "action - onAliasOperatorResult, sequence:" + sequence + ",alias:" + jPushMessage.getAlias());
        setCurrentAlias(jPushMessage.getAlias());
        init(context);
        //根据sequence从之前操作缓存中获取缓存记录
        TagAliasBean tagAliasBean = tagAliasActionCache.get(sequence);
        if (tagAliasBean == null) {
            Toast.makeText(context, "获取缓存记录失败", Toast.LENGTH_SHORT).show();
            return;
        }
        if (jPushMessage.getErrorCode() == 0) {
            Log.i(TAG, "action - modify alias Success,sequence:" + sequence);
            tagAliasActionCache.remove(sequence);
            String logs = getActionStr(tagAliasBean.action) + " alias success";
            Log.i(TAG, logs);
            Toast.makeText(context, logs, Toast.LENGTH_SHORT).show();
        } else {
            String logs = "Failed to " + getActionStr(tagAliasBean.action) + " alias, errorCode:" + jPushMessage.getErrorCode();
            Log.i(TAG, logs);
            if (!RetryActionIfNeeded(jPushMessage.getErrorCode(), tagAliasBean)) {
                Toast.makeText(context, logs, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String onAliasOperatorResultAlias;

    private void setCurrentAlias(String alias) {
        Log.i(TAG, "我后执行");
        this.onAliasOperatorResultAlias = alias;
    }

    public String getOnAliasOperatorResultAlias() {
        Log.i(TAG, "我先执行");
        return onAliasOperatorResultAlias;
    }

    /**
     * 设置 TagAliasBean
     *
     * @param action        目标操作
     * @param alias
     * @param tags
     * @param isAliasAction true 设置alias ；false设置tags
     */
    private void setTagAliasBean(int action, String alias, Set<String> tags, boolean isAliasAction) {

        tagAliasBean.action = action;
        tagAliasBean.alias = alias;
        tagAliasBean.tags = tags;
        tagAliasBean.isAliasAction = isAliasAction;
        Log.e("----> tagAliasBean", tagAliasBean.isAliasAction+"");

    }

    private boolean RetryActionIfNeeded(int errorCode, TagAliasBean tagAliasBean) {
        if (isConnected(context)) {
            Log.i(TAG, "no network");
            return false;
        }
        //返回的错误码为6002 超时,6014 服务器繁忙,都建议延迟重试
        if (errorCode == 6002 || errorCode == 6014) {
            Log.i(TAG, "need retry");
            if (tagAliasBean != null) {
                Message message = new Message();
                message.what = DELAY_SEND_ACTION;
                message.obj = tagAliasBean;
                delaySendHandler.sendMessageDelayed(message, 1000 * 60);
                String logs = getRetryStr(tagAliasBean.isAliasAction, tagAliasBean.action, errorCode);
                Toast.makeText(context, logs, Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
    }

    private String getRetryStr(boolean isAliasAction, int actionType, int errorCode) {
        String str = "Failed to %s %s due to %s. Try again after 60s.";
        str = String.format(Locale.ENGLISH, str, getActionStr(actionType), (isAliasAction ? "alias" : " tags"), (errorCode == 6002 ? "timeout" : "server too busy"));
        return str;
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conn.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }

    private String getActionStr(int actionType) {
        switch (actionType) {
            case ACTION_ADD:
                return "add";
            case ACTION_SET:
                return "set";
            case ACTION_DELETE:
                return "delete";
            case ACTION_GET:
                return "get";
            case ACTION_CLEAN:
                return "clean";
            case ACTION_CHECK:
                return "check";
        }
        return "unkonw operation";
    }

    public static class TagAliasBean {
        int action;
        Set<String> tags;
        String alias;
        boolean isAliasAction;

        @Override
        public String toString() {
            return "TagAliasBean{" +
                    "action=" + action +
                    ", tags=" + tags +
                    ", alias='" + alias + '\'' +
                    ", isAliasAction=" + isAliasAction +
                    '}';
        }
    }
}
