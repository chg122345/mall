layui.use(['form','layer','layedit','laydate','upload'],function(){
    var form = layui.form
        layer = parent.layer === undefined ? layui.layer : top.layer,
        laypage = layui.laypage,
        upload = layui.upload,
        layedit = layui.layedit,
        laydate = layui.laydate,
        $ = layui.jquery;

    //用于同步编辑器内容到textarea
    layedit.sync(editIndex);

    //上传缩略图
    upload.render({
        elem: '.thumbBox',
        url: '/a/upload',
        method : "post",  //此处是为了演示之用，实际使用中请将此删除，默认用post方式提交
        done: function(res, index, upload){
            layer.msg(res.msg);
            if (res.code === 200){
                $('.thumbImg').attr('src',res.data.uri);
                $('.thumbBox').css("background","#fff");
                $('#p_img').val(res.data.uri);
            }
        }
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
    laydate.render({
        elem: '#release',
        type: 'datetime',
        trigger : "click",
        done : function(value, date, endDate){
            submitTime = value;
        }
    });
    form.on("radio(release)",function(data){
        if(data.elem.title == "定时发布"){
            $(".releaseDate").removeClass("layui-hide");
            $(".releaseDate #release").attr("lay-verify","required");
        }else{
            $(".releaseDate").addClass("layui-hide");
            $(".releaseDate #release").removeAttr("lay-verify");
            submitTime = time.getFullYear()+'-'+(time.getMonth()+1)+'-'+time.getDate()+' '+time.getHours()+':'+time.getMinutes()+':'+time.getSeconds();
        }
    });

    form.verify({
        newsName : function(val){
            if(val == ''){
                return "名称不能为空";
            }
        },
        /*content : function(val){
            if(val == ''){
                return "文章内容不能为空";
            }
        }*/
    });
    form.on("submit(addNews)",function(data){
        //截取文章内容中的一部分文字放入文章摘要
       // var abstract = layedit.getText(editIndex).substring(0,50);
        //弹出loading
        var index = top.layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
        $.post("/a/product", {
            name : $('#p_name').val(),
            price : $('#p_price').val(),
            stock : $('#p_stock').val(),
            mlCategoryId : data.field.mlCategoryId,
            details : layedit.getContent(editIndex).split('<audio controls="controls" style="display: none;"></audio>')[0],  //文章内容
            imgs : $(".thumbImg").attr("src"),  //缩略图
        },function(res){
            if (res.code === 200){
                setTimeout(function(){
                    top.layer.close(index);
                    top.layer.msg("上架成功！");
                    layer.closeAll("iframe");
                    //刷新父页面
                    parent.location.reload();
                },500);
            } else{
                top.layer.close(index);
                layer.msg(res.msg);
            }
        });
        top.layer.close(index);
        layer.closeAll("iframe");
        return false;
    });

    //创建一个编辑器
    var editIndex = layedit.build('news_content',{
        height : 400,
        uploadImage : {
            url : "/static/json/newsImg.json"
        }
    });

});