layui.use(['form','layer','jquery'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;

    $(".loginBody .seraph").click(function(){
        layer.msg("这只是做个样式，至于功能，你见过哪个后台能这样登录的？还是老老实实的找管理员去注册吧",{
            time:5000
        });
    });

    //登录按钮
    form.on("submit(login)",function(data){
        if ($('#code').val().length != 4){
            layer.msg("验证码长度为4",{time:3000});
            return false;
        }
      //  $(this).text("登录中...").attr("disabled","disabled").addClass("layui-disabled");
        $.post('/login', data.field, function (res) {
            layer.msg(res.msg,{time:3000});
            if (res.code === 200){
                window.location.href = "/index";
            }
        });
        return false;
    });

    //表单输入效果
    $(".loginBody .input-item").click(function(e){
        e.stopPropagation();
        $(this).addClass("layui-input-focus").find(".layui-input").focus();
    })
    $(".loginBody .layui-form-item .layui-input").focus(function(){
        $(this).parent().addClass("layui-input-focus");
    })
    $(".loginBody .layui-form-item .layui-input").blur(function(){
        $(this).parent().removeClass("layui-input-focus");
        if($(this).val() != ''){
            $(this).parent().addClass("layui-input-active");
        }else{
            $(this).parent().removeClass("layui-input-active");
        }
    })
});
