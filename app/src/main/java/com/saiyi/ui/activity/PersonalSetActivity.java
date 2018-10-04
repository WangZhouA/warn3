package com.saiyi.ui.activity;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.saiyi.R;
import com.saiyi.application.MyApplication;
import com.saiyi.http.ConstantInterface;
import com.saiyi.ui.fragment.FragmentLoginActivity;
import com.saiyi.ui.view.ForPlayDialog;
import com.saiyi.utils.UserInfos;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static java.lang.String.valueOf;

/**
 * Created by Administrator on 2017/3/24.
 */

public class PersonalSetActivity extends BasActivity implements View.OnClickListener {

    private ImageButton header_left;
    private ImageButton header_right;
    private ImageView personal_set_head;
    private ImageButton personal_set_input;
    private TextView header_text;
    private LinearLayout lname;
    private TextView personal_set_text_name;

    UserInfos us= new UserInfos(PersonalSetActivity.this);



    private static final int PHOTO_REQUEST_CAREMA = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_CLIP = 3;// 结果
    /* 头像名称 */

    ForPlayDialog forPlayDialog;
    private Dialog dialog;

    private View inflate;

    private TextView choosePhoto;
    private TextView takePhoto;

    private String  user_number;

    File file;

    String ed_name;

    String strurlforPicture;

    private String path;

    private String  jsonName;

    private String  jsonPictureName;

    private int flag;




    Uri photoUri;
    String filePath;  //图片路径


    public PersonalSetActivity() {
    }


    private Handler handler=new Handler(){

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){

                case 1:
                    if (flag==0) {

                        personal_set_text_name.setText(jsonName);

                    }else if (flag==1) {


                        String strurl = ConstantInterface. HTTP_SERVICE_HOST+ jsonPictureName;
                        Log.e("-------->图片",strurl);
//                        Glide.with(PersonalSetActivity.this).load(strurl).into(personal_set_head);
                       ConstantInterface.showImage(PersonalSetActivity.this,strurl,R.drawable.personal_head_portrait,R.drawable.personal_head_portrait,personal_set_head);

                    }

                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_set);

        MyApplication.getInstance().addActivity(this);
        header_left = (ImageButton) findViewById(R.id.header_left);
        header_right = (ImageButton) findViewById(R.id.header_right);
        personal_set_head = (ImageView) findViewById(R.id.personal_set_head);
        personal_set_input = (ImageButton) findViewById(R.id.personal_set_input);
        header_text = (TextView) findViewById(R.id.header_text);
        personal_set_text_name= (TextView) findViewById(R.id.personal_set_text_name);
        header_left.setOnClickListener(this);
        personal_set_input.setOnClickListener(this);
        header_left.setImageResource(R.drawable.ic_back);
        header_right.setVisibility(View.GONE);
        personal_set_head.setOnClickListener(this);
        header_text.setText(R.string.Personal_Settings);
        lname= (LinearLayout) findViewById(R.id.reName);
        lname.setOnClickListener(this);


        //拿到登录的账号用来去获取useId
        if (FragmentLoginActivity.getUser_number()!=null){
            user_number=FragmentLoginActivity.getUser_number();
            Log.i("--->user_number",""+user_number);
        }
        strurlforPicture = ConstantInterface.HTTP_SERVICE_ADDRESS_REPICTURE;


        //在这里判断名字
        if ( us.getStringInfo("ReName")==null ){
            flag=0;
            okHttpQueryUserMessage();

        }else {
            personal_set_text_name.setText(us.getStringInfo("ReName"));
        }


//        if (us.getStringInfo("paths")==null){
        flag=1;
        okHttpQueryUserMessage();
//
//        }else {
//
//            Uri uris =Uri.parse(us.getStringInfo("paths").toString());
//            personal_set_head.setImageURI(uris);
//
//        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left:
                finish();
                break;

            case R.id.personal_set_head:
                dialog = new Dialog(this, R.style.ActionSheetDialogStyle);
                //填充对话框的布局
                inflate = LayoutInflater.from(this).inflate(R.layout.bottom_dialog, null);
                //初始化控件
                choosePhoto = (TextView) inflate.findViewById(R.id.choosePhoto);
                takePhoto = (TextView) inflate.findViewById(R.id.takePhoto);
                choosePhoto.setOnClickListener(this);
                takePhoto.setOnClickListener(this);
                //将布局设置给Dialog
                dialog.setContentView(inflate);
                //获取当前Activity所在的窗体
                Window dialogWindow = dialog.getWindow();
                //设置Dialog从窗体底部弹出
                dialogWindow.setGravity(Gravity.BOTTOM);
                //获得窗体的属性
                WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                lp.y = 20;//设置Dialog距离底部的距离
                //       将属性设置给窗体
                dialogWindow.setAttributes(lp);
                dialog.show();//显示对话框
                break;


            case R.id.takePhoto:

                flag=1;
                if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                    photoUri = patrUri("file2");
                    startCamera(PHOTO_REQUEST_CAREMA,photoUri);
                }else{
                    Toast.makeText(PersonalSetActivity.this, getResources().getString(R.string.Do_not_use), Toast.LENGTH_SHORT).show();
                }

                dialog.dismiss();
                break;

            case R.id.choosePhoto:


                flag=2;
                Intent intent1 = new Intent(Intent.ACTION_PICK);
                intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent1, PHOTO_REQUEST_GALLERY);
                dialog.dismiss();
                break;

            case R.id.reName:
                okHttpReName();
                break;

        }
    }

    //相机
    private void startCamera(int requestCode, Uri photoUri) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
        startActivityForResult(intent, requestCode);

    }
    // 压缩图片尺寸
    private Bitmap compressBySize(String pathName, int targetWidth, int targetHeight) {


        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;// 不去真的解析图片，只是获取图片的头部信息，包含宽高等；
        Bitmap bitmap = BitmapFactory.decodeFile(pathName, opts);
        // 得到图片的宽度、高度；
        float imgWidth = opts.outWidth;
        float imgHeight = opts.outHeight;
        // 分别计算图片宽度、高度与目标宽度、高度的比例；取大于等于该比例的最小整数；
        int widthRatio = (int) Math.ceil(imgWidth / (float) targetWidth);
        int heightRatio = (int) Math.ceil(imgHeight / (float) targetHeight);
        opts.inSampleSize = 1;
        if (widthRatio > 1 || widthRatio > 1) {
            if (widthRatio > heightRatio) {
                opts.inSampleSize = widthRatio;
            } else {
                opts.inSampleSize = heightRatio;
            }
        }
        // 设置好缩放比例后，加载图片进内容；
        opts.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(pathName, opts);

        return  bitmap;
    }

    private Uri patrUri(String fileName) {
        // TODO Auto-generated method stub
        String strPhotoName = fileName+System.currentTimeMillis()+".jpg";
        String savePath = Environment.getExternalStorageDirectory().getPath()
                + "/MyPhoto/";
        File dir = new File(savePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        filePath = savePath + strPhotoName;
        return Uri.fromFile(new File(dir, strPhotoName));
    }

    private void saveFile(Bitmap bitmap, String filePath2) throws IOException  {

        File testFile = new File(filePath2);
        if (testFile.exists()) {
            testFile.delete();
        }
        File myCaptureFile = new File(filePath2);
        System.out.println("------filePath2==" + filePath2);
        BufferedOutputStream bos = new BufferedOutputStream( new FileOutputStream(myCaptureFile));
        // 100表示不进行压缩，70表示压缩率为30%
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bos);
        bos.flush();
        bos.close();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case PHOTO_REQUEST_CAREMA: //调用系统相机返回
                if (resultCode == RESULT_OK) {
                    Bitmap bitmap = compressBySize(filePath, 480, 800);  //设置压缩后图片的高度和宽度


                    try {
                        saveFile(bitmap, filePath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    personal_set_head.setImageBitmap(bitmap);

                    File file = new File(filePath);
                    Map<String, Object> map = new HashMap<>();
                    map.put("phone", user_number);
                    okHttpForPicture(map, file);
                    Log.i("--->拍照", "上传照片结束");
//                    String paths =uri.toString();
//                    Log.i("--->需要保存在本地的path",paths);
//                    us.setUserInfo("paths",paths);

                }
                break;
            case PHOTO_REQUEST_GALLERY:  //调用系统相册返回
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    Log.i("--->相册" +
                            "uri",""+uri);
                    //=============================================================
                    ContentResolver resolver = getContentResolver();
                    Bitmap bm=null;

                    try {
                        bm = MediaStore.Images.Media.getBitmap(resolver, uri);        //显得到bitmap图片
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    String[] proj = {MediaStore.Images.Media.DATA};

                    //好像是android多媒体数据库的封装接口，具体的看Android文档

                    Cursor cursor = managedQuery(uri, proj, null, null, null);

                    //按我个人理解 这个是获得用户选择的图片的索引值

                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                    //将光标移至开头 ，这个很重要，不小心很容易引起越界

                    cursor.moveToFirst();

                    //最后根据索引值获取图片路径

                    String path = cursor.getString(column_index);

                    Log.i("--->path--->",path);

                    File file=new File(path);

                    Uri uri1=Uri.fromFile(file);

                    personal_set_head.setImageURI(uri1);

                    Map<String, Object> map=new HashMap<>();
                    map.put("phone",user_number);
                    okHttpForPicture(map,file);


//                    String paths =uri1.toString();
//                    Log.i("--->需要保存在本地的paths",paths);
//                    us.setUserInfo("paths",paths);

                }
                break;

            case PHOTO_CLIP:

                break;

            default:
                break;
        }


        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onResume() {

        super.onResume();


    }

    private void okHttpQueryUserMessage() {

        final  String strurl=ConstantInterface.HTTP_SERVICE_ADDRESS_QUERY_USER_MESSAGE+user_number;

        //创建网络处理的对象
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
        Request request = new Request.Builder().url(strurl).build();
        //call就是我们可以执行的请求类
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //失败
                Log.e(ConstantInterface.TAG,"get--->"+Thread.currentThread().getName() + "结果  " + e.toString());

            }
            @Override
            public void onResponse(Call call, Response response   ) throws IOException {

                String  result =response.body().string();

                try {
                    JSONObject jsonObject =new JSONObject(result);
                    JSONObject json=jsonObject.optJSONObject("data");

                    jsonName= json.getString("name");
                    Log.i("--->jsonName",jsonName);
                    us.setUserInfo("ReName",jsonName);

                    jsonPictureName=json.getString("headimg");
                    Log.i("--->jsonPictureName",jsonPictureName);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                Message mesg =new  Message();
                mesg.what=1;
                handler.sendMessage(mesg);

                Log.i("--->",result);

            }
        });

    }

    private  void okHttpReName(){

        forPlayDialog = new ForPlayDialog(PersonalSetActivity.this);
        forPlayDialog.setTitle(getResources().getString(R.string.Fill_in_the_username));
        forPlayDialog.setYesOnclickListener(getResources().getString(R.string.sure), new ForPlayDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                ed_name=forPlayDialog.setMessage();
                //已经拿到用户输入的名字，现在需要联网请求去改变名字了
                if (ed_name!=null) {
                    okHttpRName();
                    personal_set_text_name.setText(ed_name);
                    us.setUserInfo("ReName",ed_name);

                }
                forPlayDialog.dismiss();
            }
        });
        forPlayDialog.setNoOnclickListener( getResources().getString(R.string.link_cancle), new ForPlayDialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {

                forPlayDialog.dismiss();
            }
        });
        forPlayDialog.show();
    }

    private void okHttpRName() {
        final String strurl = ConstantInterface.HTTP_SERVICE_ADDRESS_USER_RENAME;

        OkHttpClient client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
        JSONObject jsonObject = new JSONObject();
        try {

//            比如这这user
            jsonObject.put("phone",user_number);
            jsonObject.put("name", ed_name);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

        final Request request = new Request.Builder()
                .url(strurl)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("--->", "" + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.i("--->", "正确的修改用户信息" + response.body().string());

                } else {
                    Log.i("--->", "错误的修改信息" + response.body().string());
                }
            }
        });
    }



    private void okHttpForPicture( final Map<String, Object> map, File file) {
        final String strurl= ConstantInterface.HTTP_SERVICE_ADDRESS_REPICTURE;

        OkHttpClient client = new OkHttpClient();
        // form 表单形式上传
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if(file != null){
            // MediaType.parse() 里面是上传的文件类型。
            RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
            String filename = file.getName();
            // 参数分别为， 请求key ，文件名称 ， RequestBody
            requestBody.addFormDataPart("file", file.getName(), body);
        }
        if (map != null) {
            // map 里面是请求中所需要的 key 和 value
            for (Map.Entry entry : map.entrySet()) {
                requestBody.addFormDataPart(valueOf(entry.getKey()), valueOf(entry.getValue()));
            }
        }
        Request request = new Request.Builder().url(strurl).post(requestBody.build()).tag(PersonalSetActivity.this).build();
        // readTimeout("请求超时时间" , 时间单位);
        client.newBuilder().readTimeout(5000, TimeUnit.MILLISECONDS).build().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("lfq" ,"onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String str = response.body().string();
                    Log.i("--->成功上传图片", response.message() + " , body " + str);
                    okHttpQueryUserMessage();
                } else {
                    Log.i("--->上传图片失败" ,response.message() + " error : body " + response.body().string());
                }
            }
        });
    }

    private void photoClip(Uri uri) { // 调用系统中自带的图片剪裁
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTO_CLIP);

    }

}



