//存放主要交互逻辑的代码
var seckill={
    //封装秒杀相关ajax的url
    URL:{
        now:function () {
            return '/seckill/time/now';
        },
        exposer:function (seckillId) {
            return '/seckill/'+ seckillId +'/exposer';
        },
        execution:function (seckillId,md5) {
            return '/seckill/'+seckillId+'/'+md5+'/execution';
        }
    },
    //处理秒杀逻辑
    handlerSeckill:function (seckillId,node) {
        //获取秒杀地址，控制显示逻辑，执行秒杀
        node.hide().
            html('<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>');//按钮
        $.post(seckill.URL.exposer(seckillId),{},function (result) {
            //在回调函数中执行交互流程
            if(result && result['success']){
                var exposer = result['data'];
                if(exposer['exposed']){//是否开启秒杀
                    //获取秒杀地址
                    var md5 = exposer['md5'];
                    var killUrl = seckill.URL.execution(seckillId,md5);
                    console.log("killUrl:"+killUrl);
                    //绑定一次点击事件，防止用户多次点击
                    $('#killBtn').one('click',function () {
                        //执行秒杀请求,在哪里运行，this就表示这个对象，即this = #killBtn
                        //禁用按钮
                        $(this).addClass('disabled');
                        //发送秒杀请求
                        $.post(killUrl,{},function (result) {
                            if(result && result['success']){
                                var killResult = result['data'];
                                var state = killResult['state'];
                                var stateInfo = killResult['stateInfo'];
                                //显示秒杀结果
                                node.html('<span class="label label-success">'+ stateInfo +'</span>')
                            }
                        });
                    });
                    node.show();
                }else{//未开启秒杀
                    var now = exposer['now'];
                    var start = exposer['start'];
                    var end = exposer['end'];
                    //重新计算计时逻辑
                    seckill.countdown(seckillId,now,start,end);
                }
            }else{
                console.log('result:'+result);
            }
        });
    },
    //验证手机号
    validatePhone:function (phone) {
      if(phone && phone.length == 11 && !isNaN(phone)){
          return true;
      }else {
          return false;
      }
    },
    //判断时间
    countdown:function (seckillId,nowTime,startTime,endTime) {
        var seckillBox = $('#seckill-box');
        //时间判断
        if(nowTime > endTime){//秒杀结束
            seckillBox.html('秒杀结束！');
        }else if(nowTime < startTime){//秒杀未开始
            //计时事件绑定
            var killTime = new Date(startTime + 1000);
            seckillBox.countdown(killTime,function (event) {
                //时间格式
                var format = event.strftime('秒杀倒计时：%D天 %H时 %M分 %S秒');
                seckillBox.html(format);
                //时间完成后回调事件
            }).on('finish.countdown',function () {
                //获取秒杀地址，控制显示逻辑，执行秒杀
                seckill.handlerSeckill(seckillId,seckillBox);
            });
        }else{//秒杀开始
            seckill.handlerSeckill(seckillId,seckillBox);
        }
    },
    //详情页秒杀逻辑
    detail:{
        //详情页初始化
        init:function (params) {
            //用户手机验证和登录，计时交互
            //在cokkie中查找手机号
            var killPhone = $.cookie('killPhone');
            if(!seckill.validatePhone(killPhone)){//手机号格式不正确
                //绑定弹出层
                var killPhoneModal = $('#killPhoneModal');
                //显示弹出层
                killPhoneModal.modal({
                    show:true,//显示弹出层
                    backdrop:'static',//禁止位置关闭，点击页面中其他位置，不能关闭弹出层
                    keyboard:false//关闭键盘事件
                });
                $('#killPhoneBtn').click(function () {//绑定按钮点击事件
                    //获取输入的手机号
                   var inputPhone = $('#killPhoneKey').val();
                   if(seckill.validatePhone(inputPhone)){//手机号格式正确
                       //将电话写入cookie
                       $.cookie('killPhone',inputPhone,{expires:7,path:'/seckill'});
                      //刷新页面
                      window.location.reload();
                   }else{
                       $('#killPhoneMessage').hide().html('<label class="label label-danger">手机号错误！</label>').show(300);
                   }
                });
            }
            //已经登录
            //计时交互
            var startTime = params['startTime'];
            var endTime = params['endTime'];
            var seckillId = params['seckillId'];
            $.get(seckill.URL.now(),{},function (result) {
                if(result && result['success']){
                    var nowTime = result['data'];
                    //时间判断
                    seckill.countdown(seckillId,nowTime,startTime,endTime);
                }else{
                    console.log('result:'+result);
                }
            });
        }
    }
}