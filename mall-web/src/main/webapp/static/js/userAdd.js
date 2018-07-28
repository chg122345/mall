layui.use(['form','layer'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;

    form.on("submit(addUser)",function(data){
        //弹出loading
        var index = top.layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
        // 实际使用时的提交信息
        $.post("/user/user",data.field,function(res){
            if (res.code === 200){
                setTimeout(function(){
                    top.layer.close(index);
                    top.layer.msg("用户添加成功！");
                    layer.closeAll("iframe");
                    parent.location.reload();
                },2000);
            } else {
                setTimeout(function(){
                    top.layer.close(index);
                    top.layer.msg(res.msg);
                    layer.closeAll("iframe");
                },2000);
            }
        });
        return false;
    });

    //格式化时间
    function filterTime(val){
        if(val < 10){
            return "0" + val;
        }else{
            return val;
        }
    }
    //定时发布
    var time = new Date();
    var submitTime = time.getFullYear()+'-'+filterTime(time.getMonth()+1)+'-'+filterTime(time.getDate())+' '+filterTime(time.getHours())+':'+filterTime(time.getMinutes())+':'+filterTime(time.getSeconds());

});