layui.use(['form','layer','laydate','table','laytpl'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laydate = layui.laydate,
        laytpl = layui.laytpl,
        table = layui.table;

    //新闻列表
    var tableIns = table.render({
        elem: '#newsList',
        url : '/a/product',
        cellMinWidth : 95,
        page : true,
        height : "full-200",
        limit : 20,
        limits : [10,15,20,25],
        id : "newsListTable",
        cols : [[
            {type: "checkbox", fixed:"left", width:50},
            {field: 'id', title: 'ID', width:300, align:"center"},
            {field: 'name', title: '名称', width:100,edit:'text'},
            {field: 'price', title: '单价', align:'center',sort:true,edit: 'text'},
            {field: 'stock', title: '库存',  align:'center',edit:'text'},
            {field: 'mlCategoryId', title: '类别',  align:'center',edit:'text',templet:function(d){
               var is =d.mlCategoryId;
                if (is == 1){
                    return "零食";
                }else if (is == 2){
                    return "水果";
                }else {
                    return is;
                }
                }},
            {field: 'details', title: '参数详情', align:'center',edit:'text'},
            {field: 'imgs', title: '插图', align:'center', templet:function(d){
                return '<img src=\"'+d.imgs+'\" height="100px;">'
            }},
            {field: 'created', title: '上架时间', align:'center', minWidth:120,sort: true},
            {title: '操作', width:170, templet:'#newsListBar',fixed:"right",align:"center"}
        ]]
    });

    //搜索【此功能需要后台配合，所以暂时没有动态效果演示】
    $(".search_btn").on("click",function(){
        if($(".searchVal").val() != ''){
            table.reload("newsListTable",{
                page: {
                    curr: 1 //重新从第 1 页开始
                },
                where: {
                    key: $(".searchVal").val()  //搜索的关键字
                }
            })
        }else{
            layer.msg("请输入搜索的内容");
        }
    });

    //添加文章
    function addNews(edit){
        var index = layui.layer.open({
            title : "添加产品",
            type : 2,
            content : "/a/productAdd",
            success : function(layero, index){
                var body = layui.layer.getChildFrame('body', index);
                if(edit){
                    body.find(".newsName").val(edit.newsName);
                    body.find(".abstract").val(edit.abstract);
                    body.find(".thumbImg").attr("src",edit.newsImg);
                    body.find("#news_content").val(edit.content);
                    body.find(".newsStatus select").val(edit.newsStatus);
                    body.find(".openness input[name='openness'][title='"+edit.newsLook+"']").prop("checked","checked");
                    body.find(".newsTop input[name='newsTop']").prop("checked",edit.newsTop);
                    form.render();
                }
                setTimeout(function(){
                    layui.layer.tips('点击此处返回产品列表', '.layui-layer-setwin .layui-layer-close', {
                        tips: 3
                    });
                },500)
            }
        });
        layui.layer.full(index);
        //改变窗口大小时，重置弹窗的宽高，防止超出可视区域（如F12调出debug的操作）
        $(window).on("resize",function(){
            layui.layer.full(index);
        })
    }
    $(".addNews_btn").click(function(){
        addNews();
    });

    function updateProduct(data){
        $.ajax({
            url :'/a/product',
            data :{id : data.id,
                mlCategoryId: data.mlCategoryId,
                name: data.name,
                details: data.details,
                price: data.price,
                stock: data.stock,
                imgs: data.imgs,
            },
            type : 'POST',
            dataType: 'json',
          //  contentType: 'application/json;charset=UTF-8',
            success:function (res) {
                if (res.code === 200){
                    setTimeout(function(){
                        layer.msg("更新成功！");
                        window.location.reload();
                    },2000);
                } else {
                    setTimeout(function(){
                        layer.msg(res.msg);
                    },2000);
                }
            }
        });
        return false;
    }
    //批量删除
    $(".delAll_btn").click(function(){
        var checkStatus = table.checkStatus('newsListTable'),
            data = checkStatus.data,
            newsId = '';
        if(data.length > 0) {
            for (var i in data) {
                newsId += data[i].id + "-";
            }
            layer.confirm('确定将选中的产品下架？', {icon: 3, title: '提示信息'}, function (index) {
                $.ajax({
                    url : "/a/product",
                    data: newsId,
                    type: "DELETE",
                    dataType: 'json',
                    contentType: 'application/json;charset=UTF-8',
                    success : function (res) {
                        layer.msg(res.msg);
                        if (res.code === 200 ){
                            tableIns.reload();
                        }
                    }
                });
                layer.close(index);
                return false;
            })
        }else{
            layer.msg("请选择需要删除的文章");
        }
    });

    //列表操作
    table.on('tool(newsList)', function(obj){
        var layEvent = obj.event,
            data = obj.data;

        if(layEvent === 'edit'){ //编辑
            updateProduct(data);
        } else if(layEvent === 'del'){ //删除
            layer.confirm('确定将' + data.id + '产品下架？',{icon:3, title:'提示信息'},function(index){
                $.ajax({
                    url : "/a/product",
                    data: data.id,
                    type: "DELETE",
                    dataType: 'json',
                    contentType: 'application/json;charset=UTF-8',
                    success : function (res) {
                        layer.msg(res.msg);
                        if (res.code === 200 ){
                            tableIns.reload();
                        }
                    }
                });
                layer.close(index);
                return false;
            });
        } else if(layEvent === 'look'){ //预览
            layer.alert("此功能需要前台展示，暂且搁置。")
        }
    });

})