layui.use(['form','layer','table','laytpl'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laytpl = layui.laytpl,
        table = layui.table;

    //订单列表
    var tableIns = table.render({
        elem: '#userList',
        url : '/a/order',
        cellMinWidth : 95,
        page : true,
        height : "full-400",
        limits : [10,15,20,25],
        limit : 20,
        id : "userListTable",
        cols : [[
            {type: "checkbox", fixed:"left", width:50},
            {field: 'id', title: 'ID', minWidth:200, align:"center"},
            {field: 'serial', title: '订单编号', minWidth:100, align:"center"},
            {field: 'number', title: '订购数量', minWidth:50, align:'center'},
            {field: 'money', title: '订单金额', minWidth:50, align:'center', edit:'number'},
            {field: 'status', title: '订单状态', minWidth:50, align:'center',edit: 'text',templet:function(d){
                var s = d.status;
                if (s == 1){
                    return "待付款";
                } else if (s == 2){
                    return "待发货";
                } else if (s == 3){
                    return "已发货";
                } else if (s == 4){
                    return "交易成功";
                }
                }},
            {field: 'address', title: '收货信息', minWidth:100, align:'center',templet:function (d) {
                    return "<p> 收货人：" + d.address.name +"</p><p>电话："+ d.address.phone +"</p><p>邮编："
                        + d.address.postcode +"</p><p>地址："+ d.address.place +"</p>";
                }},
            {field: 'orderItem', title: '订单详情', align:'center',templet:function(d){
                var it = d.orderItem;
                var res = "";
               $.each(it, function (index, oi) {
                   res += "<p>产品：" + oi.product.name + "&nbsp;&nbsp;单价："+ oi.product.price +"</p><p><img height='100px;' src='"+oi.product.imgs +"'></p><p>购买数量："
                       + oi.number +"&nbsp;&nbsp;小计：" + oi.money +"</p>";
               });
              return res;
            }},
            {field: 'created', title: '下单时间', align:'center',minWidth:150,sort:true},
            {title: '操作', minWidth:175, templet:'#userListBar',fixed:"right",align:"center"}
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

    //批量删除
    $(".delAll_btn").click(function(){
        var checkStatus = table.checkStatus('userListTable'),
            data = checkStatus.data,
            newsId = '';
        if(data.length > 0) {
            for (var i in data) {
                newsId += (data[i].id) + '-';
            }
            layer.confirm('确定删除选中的订单？', {icon: 3, title: '提示信息'}, function (index) {
                $.ajax({
                    url : "/a/order",
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
            layer.msg("请选择需要删除的订单");
        }
    });

   function updateOrder(data){
       $.ajax({
           url :'/a/order',
           data :JSON.stringify({
               id : data.id,
               number : data.number,
               money : data.money,
               status : data.status }),
           type : 'PUT',
           dataType: 'json',
           contentType: 'application/json;charset=UTF-8',
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

    //列表操作
    table.on('tool(userList)', function(obj){
        var layEvent = obj.event,
            data = obj.data;

        if(layEvent === 'edit'){ //更新
            updateOrder(data);
        }/*else if(layEvent === 'usable'){ //启用禁用
            var _this = $(this),
                usableText = "是否确定禁用此用户？",
                btnText = "已禁用";
            if(_this.text()=="已禁用"){
                usableText = "是否确定启用此用户？",
                btnText = "已启用";
            }
            layer.confirm(usableText,{
                icon: 3,
                title:'系统提示',
                cancel : function(index){
                    layer.close(index);
                }
            },function(index){
                _this.text(btnText);
                layer.close(index);
            },function(index){
                layer.close(index);
            });
        }*/else if(layEvent === 'del'){ //删除
            layer.confirm('确定删除'+ data.id +'用户？',{icon:3, title:'提示信息'},function(index){
                $.ajax({
                    url : "/a/order",
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
        }
    });

});
