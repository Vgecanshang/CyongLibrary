# CyongLibrary
## 三方通用组件整合库

### 1.动态权限申请工具类
  (1)初始化工具类
  ```Java
    private ApplyPermissionUtil permissionUtil = null;//三方动态申请权限工具类
    permissionUtil = new ApplyPermissionUtil(MainActivity.this, requestPermissionsListener);
  ```
  (2)权限申请结果监听类requestPermissionsListener
  ```Java
      ApplyPermissionUtil.RequestPermissionsListener requestPermissionsListener = new ApplyPermissionUtil.RequestPermissionsListener() {
        @Override
        public void getRequestPermissionResult(boolean b, int i) {
            switch(i){
                case TYPE_EXTERNAL_STORAGE:
                    if(b){
                        Toast.makeText(MainActivity.this , "获取文件读取权限成功..." , Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(MainActivity.this , "获取读写权限失败..." , Toast.LENGTH_LONG).show();
                        finish();
                    }
                    break;
            }
        }
    };
  ```
  (3)在Activity或Fragment的onRequestPermissionsResult方法中让工具监听:
   ```Java
   @Override
   public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
       permissionUtil.listenerRequestPermissionsResult(requestCode , permissions , grantResults);//监听权限请求结果
       super.onRequestPermissionsResult(requestCode, permissions, grantResults);
   }
   ```
  (4)动态申请权限
   ```Java
   permissionUtil.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, TYPE_EXTERNAL_STORAGE);
   ```
   注意点：
    在Fragment中申请动态权限的时候， 要确定依附的Activity的onRequestPermissionsResult中没有拦截该事件，因为Fragment申请权限首先是回调到依附的Activity的
    onRequestPermissionsResult中，如果没有调用super.onRequestPermissionsResult(requestCode, permissions, grantResults);，则无法回调到Fragment永远无法回调onRequestPermissionsResult。<br>
    下面是我遇到的问题：<br>
    允许权限后，始终后无法在该Fragment获取回调 ， 拒绝则有<br>
    原因：由于该DialogFragment依附MainActivity的基类JYActivity，我们在JYActivity中重写了onRequestPermissionsResult,所以要在允许和拒绝后 调用super.onRequestPermissionsResult，这样Fragment的onRequestPermissionsResult才能有回调。<br>
    
    ---

