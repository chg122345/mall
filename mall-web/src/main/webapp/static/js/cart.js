var $,tab,dataStr,layer;
layui.config({
	base : "/static/js/"
}).extend({
	jl : "index_i"
});
layui.use(['jl','form','jquery'],function(){
	var form = layui.form,
		jl = layui.jl,
		$ = layui.$;

	$(document).ready(function(){
       jl.req('/cart/get','',function (res) {
           $.each(res.data.cart.items, function (index, item) {
               var nameTd  = $("<td></td>").append(item.product.name);
               var priceTd   = $("<td></td>").append(item.product.price);
               var imgTd = $("<td></td>").append("<img src='"+item.product.imgs +"' style='max-width: 200px; max-height: 200px;'>");
               var numTd   = $("<td></td>").append("<input type='number' style='width:60px;' class='layui-input num' value='"+ item.number +"'>");
               var monerTd   = $("<td></td>").append(item.money);
               var editBtn = $("<button></button>").addClass("layui-btn layui-btn-xs")
                   .append($("<i></i>").addClass("layui-icon layui-icon-edit")).append("更改 ");
               editBtn.attr("edit-id",item.product.id);
               var delBtn  = $("<button></button>").addClass("layui-btn layui-btn-xs layui-btn-danger")
                   .append($("<i></i>").addClass("layui-icon layui-icon-delete")).append("移除 ");
               delBtn.attr("del-id",item.product.id);
               var btnTd   = $("<td></td>").append(editBtn).append(" ").append(delBtn);
               $("<tr></tr>")
                   .append(nameTd)
                   .append(priceTd)
                   .append(imgTd)
                   .append(numTd)
                   .append(monerTd)
                   .append(btnTd)
                   .appendTo("#cart_list tbody");
           });
           var order = $("<button id='to_order'></button>").addClass("layui-btn layui-btn-sm layui-btn-normal").append("生成订单 ");
           var tnum = $("<span style='padding: 10px;'></span>")
               .append("总数："+ res.data.cart.totalNumber).append(" ");
           var tmoney = $("<span style='padding: 10px;'></span>")
               .append("总计金额："+ res.data.cart.totalMoney).append(" ").append(" ");
           var tdtnum = $("<td></td>").append(tnum).append(" ");
           var tdmoney = $("<td></td>").append(tmoney).append(" ");
           var tdorder = $("<td></td>").append(order).append(" ");
           $("<tr></tr>")
               .append("<td></td>")
               .append("<td></td>")
               .append("<td></td>")
               .append(tdtnum)
               .append(tdmoney)
               .append(tdorder)
               .appendTo("#cart_list tbody");
       },{type:'GET'})
    });

	function toOrder(){
	    $(document).on('click','#to_order',function () {
            jl.req('/order','',function (res) {
                layer.msg(res.msg);
                if (res.code === 200){
                    window.location.href = "/user/order"
                }
            },{type: 'GET'});
            return false;
        })
    }
});