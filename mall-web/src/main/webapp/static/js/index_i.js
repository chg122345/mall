//JavaScript代码区域
layui.define(['layer', 'form', 'element', 'upload','table','flow','carousel','util'], function(exports){
    var $ = layui.jquery
        , layer = layui.layer
        , form = layui.form
        , table = layui.table
        , element = layui.element
        , upload = layui.upload
        , device = layui.device()
        , flow = layui.flow
        , util = layui.util
        ,carousel = layui.carousel;
    addCart();
    showCart();

    carousel.render({
        elem: '#adv'
        , width: '100%' //设置容器宽度
        , arrow: 'hover' //显示箭头
        , height: '345px'
    });

    flow.load({
        elem: '#product' //流加载容器
        ,scrollElem: '#product' //滚动条所在元素，一般不用填，此处只是演示需要。
        ,isAuto: false
        ,isLazyimg: true
        ,done: function(page, next){ //加载下一页
            //模拟插入
            setTimeout(function(){
                var lis = [];
                var limit = 4;
                $.get('/a/product?page='+page + '&limit=' + limit, function(res){
                    //假设你的列表返回在data集合中
                    layui.each(res.data, function(index, item){
                        var da = '<div class="layui-col-md3 layui-col-sm6"><div class="box"><div class="box-figure">' +
                            ' <a href="/a/product/'+ item.id +'"><img lay-src="'+ item.imgs +'" alt="图片未加载"></a></div>' +
                            ' <div class="box-body"><h2 class="box-rtitle"><a href="/a/product/'+ item.id +'">'+ item.name +'</a></h2>' +
                            ' <div class="box-desc "><p class="text-center">库存 :  '+ (item.stock > 10 ?"存货充足" : item.stock) +'</p></div>' +
                            ' <div class="box-price"><span class="text-center">￥'+ item.price +'<del class="box-dprice">￥199</del></span>' +
                            '<span data-id="'+item.id+'" style="float: right"><a href="javascript:;" class="layui-btn layui-btn-xs layui-btn-danger add_cart"><i class="layui-icon layui-icon-cart"></i>加入购物车</a></span></div></div></div></div>';
                        lis.push(da);
                    });
                    next(lis.join(''), page < (res.count/limit));
                });
            }, 500);
        }
    });

    function addCart() {
        $(document).on('click', '.add_cart', function() {
            var that = $(this);
           // console.log(that.parent().data('id'));
            jl.req('/cart/add',{id :that.parent().data('id')},function (res) {
                layer.msg(res.msg)
            });
            return false;
        });
    }
    function showCart() {
        $('#show_cart').on('click',function () {
            jl.req('/cart/get','',function (res) {
                if (res.code === 200){
                    window.location.href = "/user/cart";
                } else{
                    layer.msg(res.msg);
                }
            });
            return false;
        })
    }
    /**
     * ajax请求封装
     * @param url
     * @param data
     * @param success
     * @param options
     * @returns {*|{readyState, getResponseHeader, getAllResponseHeaders, setRequestHeader, overrideMimeType, statusCode, abort}}
     */
    var jl ={
        req:function (url, data, success, options) {
        var that = this, type = typeof data === 'function';

        if (type) {
            options = success;
            success = data;
            data = {};
        }

        options = options || {};
        return $.ajax({
            type: options.type || 'post',
            dataType: options.dataType || 'json',
            //contentType:options.contentType || 'application/json;charset=UTF-8',
            data: data,
            url: url,
            success: function (res) {
                if (res.code === 200 || res.code === 100 || res.code === 400) {
                    success && success(res);
                } else {
                    layer.msg(res.msg || res.code, {shift: 6});
                    options.error && options.error();
                }
            }, error: function (e) {
                layer.msg('请求异常，请重试', {shift: 6});
                options.error && options.error(e);
            }
        });
    }
    };

    /**
     * 得到数据提交
     */
    form.on('submit(advert_submit)', function (data) {
        jl.req('/admin/advert',JSON.stringify(data.field),function (res) {
            layer.msg(res.msg);
            if (res.code === 200){
                location.reload();
            }
        },{type : 'POST'});
    });

/*******************category*********************/
    $('#open_add_category').click(function () {
        layer.open({
            type: 1
            ,title:"添加板块"
            ,content:$("#add_category")
        });
    });
    var uploadInst = upload.render({
        elem: '#category_icon_upload'
        ,url: '/admin/upload'
        ,before: function(obj){
            //预读本地文件示例，不支持ie8
            obj.preview(function(index, file, result){
                $('#category_icon').attr('src', result).css({'width':'80px','height':'80px'}); //图片链接（base64）
            });
        }
        ,done: function(res){
            //如果上传失败
            if(res.code === 400){
                return layer.msg('上传失败');
            }
            //上传成功
            $('#category_icon_uri').val(res.data.uri);
        }
        ,error: function(){
            var demoText = $('#re_update');
            demoText.html('<span style="color: #FF5722;">上传失败</span> <a class="layui-btn layui-btn-mini demo-reload">重试</a>');
            demoText.find('#.demo-reload').on('click', function(){
                uploadInst.upload();
            });
        }
    });

    //加载特定模块
    if (layui.cache.page && layui.cache.page !== 'index_i') {
        var extend = {};
        extend[layui.cache.page] = layui.cache.page;
        layui.extend(extend);
        layui.use(layui.cache.page);
    }

    //固定Bar
    util.fixbar({
         top: '&#xe642;'
        , bgcolor: '#1f9bc2'
        , click: function (type) {
        }
    });

    exports('jl',jl);
});