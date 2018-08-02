layui.define('jl', function(exports){
    var $ = layui.jquery;
    var layer = layui.layer;
    var form = layui.form;
    var jl = layui.jl;

    form.on("submit(register)",function(data){
        if ($('#password').val() != $('#password2').val()){
            layer.msg("两次密码输入不一致",{shift: 6});
            return false;
        }
        if ($('#code').val().length != 4){
            layer.msg("验证码长度为4",{shift: 6});
            return false;
        }
        jl.req('/register', data.field, function (res) {
            layer.msg(res.msg, {shift: 6});
            if (res.code === 200) {
                window.location.href = "/login";
            }
        });
        return false;
    });

    //表单输入效果
    $(".loginBody .input-item").click(function(e){
        e.stopPropagation();
        $(this).addClass("layui-input-focus").find(".layui-input").focus();
    });
    $(".loginBody .layui-form-item .layui-input").focus(function(){
        $(this).parent().addClass("layui-input-focus");
    });
    $(".loginBody .layui-form-item .layui-input").blur(function(){
        $(this).parent().removeClass("layui-input-focus");
        if($(this).val() != ''){
            $(this).parent().addClass("layui-input-active");
        }else{
            $(this).parent().removeClass("layui-input-active");
        }
    });

    exports('register',null);
});