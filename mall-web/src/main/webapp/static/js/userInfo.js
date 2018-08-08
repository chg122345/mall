layui.define(['form','layer','laydate',"jl"],function(exports){
    var  form = layui.form,
        $ = layui.jquery,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        laydate = layui.laydate,
        jl = layui.jl;

    jl.jlupload('/api/upload','.userFaceBtn',"",function (res) {
        layer.msg(res.msg);
        console.log(res);
    });

    //添加验证规则
    form.verify({
        oldPwd : function(value, item){
            if(value.length < 5){
                return "密码长度不能小于5位";
            }
        },
        newPwd : function(value, item){
            if(value.length < 5){
                return "密码长度不能小于5位";
            }
        },
        confirmPwd : function(value, item){
            if($("#oldPwd").val()!= value){
                return "两次输入密码不一致，请重新输入！";
            }
        },
        tel : function(value){
            var myreg=/^[1][3,4,5,7,8][0-9]{9}$/;
            if (!myreg.test(value)){
                return "手机号码不正确";
            }
        },
        userBirthday : function(value){
            var myreg = /^(\d{4})[\u4e00-\u9fa5]|[-\/](\d{1}|0\d{1}|1[0-2])([\u4e00-\u9fa5]|[-\/](\d{1}|0\d{1}|[1-2][0-9]|3[0-1]))*$/;
            if(!myreg.test(value)){
                return "出生日期格式不正确！";
            }
        }
    });
    //选择出生日期
    laydate.render({
        elem: '.userBirthday',
        format: 'yyyy年MM月dd日',
        trigger: 'click',
        max : 0,
        mark : {"0-12-23":"生日"},
        done: function(value, date){
            if(date.month === 12 && date.date === 23){
                layer.msg('今天是Chen_9g的生日，快来送上祝福吧！');
            }
        }
    });

    //提交个人资料
    form.on("submit(changeUser)",function(data){
        var index = layer.msg('提交中，请稍候',{icon: 16,time:false,shade:0.8});
        //将填写的用户信息存到session以便下次调取
        var key,userInfoHtml = '';
        userInfoHtml = {
            'realName' : $(".realName").val(),
            'sex' : data.field.sex,
            'userPhone' : $(".userPhone").val(),
            'userBirthday' : $(".userBirthday").val(),
            'province' : data.field.province,
            'city' : data.field.city,
            'area' : data.field.area,
            'userEmail' : $(".userEmail").val(),
            'myself' : $(".myself").val()
        };
        for(key in data.field){
            if(key.indexOf("like") != -1){
                userInfoHtml[key] = "on";
            }
        }
        window.sessionStorage.setItem("user",JSON.stringify(userInfoHtml));
        setTimeout(function(){
            layer.close(index);
            layer.msg("提交成功！");
        },2000);
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });

    //修改密码
    form.on("submit(changePwd)",function(data){
        var index = layer.msg('提交中，请稍候',{icon: 16,time:false,shade:0.8});
        setTimeout(function(){
            jl.req('/user/change',{nickname:data.field.nickname,oldPassword:data.field.old,newPassword:data.field.new},function (res) {
                layer.msg(res.msg);
                if (res.code === 200){
                    window.location.href= '/login';
                }
            });
            layer.close(index);
            $(".pwd").val('');
        },2000);
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });

    getUserInfo();

    function getUserInfo(){
        $("#userFace").attr("src","/static/images/userface3.jpg");
        jl.req('/user/getUserInfo','',function (res) {
            if (res.code === 200){
                var user =res.data.user;
                $(".nickname").val(user.nickname); //用户名
            }
        },{type : 'GET'})
    }
    exports('userInfo',null);
});