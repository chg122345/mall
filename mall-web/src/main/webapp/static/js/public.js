//JavaScript代码区域
layui.config({
    base: "/static/js/"
}).extend({
    address: "address"
}).define(['layer', 'form', 'element', 'upload','table','carousel','util','flow','address'], function(exports){
    var $ = layui.jquery
        ,flow = layui.flow
        , layer = layui.layer
        , form = layui.form
        , table = layui.table
        , element = layui.element
        , upload = layui.upload
        , device = layui.device()
        , util = layui.util
        ,carousel = layui.carousel
        ,address = layui.address;

    address.provinces();
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
            }, error: function () {
                layer.msg('请求异常，请重试', {shift: 6});
                options.error && options.error(e);
            }
        });
    }

        ,jlupload: function (url,elem,show,success) {
            var that = this, type = typeof data === 'function';
           return upload.render({
                url : url || '/api/upload'
               ,elem : elem
               ,before : show
               ,done : success
               ,error: function(e){
                  layer.msg("接口数据异常")
               }
           });
        }
        //简易编辑器
        , layEditor: function (options) {
            var html = ['<div class="layui-unselect fly-edit">'
                , '<span type="face" title="插入表情"><i class="iconfont icon-yxj-expression" style="top: 1px;"></i></span>'
                , '<span type="picture" title="插入图片：img[src]"><i class="iconfont icon-tupian"></i></span>'
                , '<span type="href" title="超链接格式：a(href)[text]"><i class="iconfont icon-lianjie"></i></span>'
                , '<span type="code" title="插入代码或引用"><i class="iconfont icon-emwdaima" style="top: 1px;"></i></span>'
                , '<span type="hr" title="插入水平线">hr</span>'
                , '<span type="yulan" title="预览"><i class="iconfont icon-yulan1"></i></span>'
                , '</div>'].join('');

            var log = {}, mod = {
                face: function (editor, self) { //插入表情
                    var str = '', ul, face = fly.faces;
                    for (var key in face) {
                        str += '<li title="' + key + '"><img src="' + face[key] + '"></li>';
                    }
                    str = '<ul id="LAY-editface" class="layui-clear">' + str + '</ul>';
                    layer.tips(str, self, {
                        tips: 3
                        , time: 0
                        , skin: 'layui-edit-face'
                    });
                    $(document).on('click', function () {
                        layer.closeAll('tips');
                    });
                    $('#LAY-editface li').on('click', function () {
                        var title = $(this).attr('title') + ' ';
                        layui.focusInsert(editor[0], 'face' + title);
                    });
                }
                , picture: function (editor) { //插入图片
                    layer.open({
                        type: 1
                        , id: 'fly-jie-upload'
                        , title: '插入图片'
                        , area: 'auto'
                        , shade: false
                        , area: '465px'
                        , fixed: false
                        , offset: [
                            editor.offset().top - $(window).scrollTop() + 'px'
                            , editor.offset().left + 'px'
                        ]
                        , skin: 'layui-layer-border'
                        , content: ['<ul class="layui-form layui-form-pane" style="margin: 20px;">'
                            , '<li class="layui-form-item">'
                            , '<label class="layui-form-label">URL</label>'
                            , '<div class="layui-input-inline">'
                            , '<input required name="image" placeholder="支持直接粘贴远程图片地址" value="" class="layui-input">'
                            , '</div>'
                            , '<button type="button" class="layui-btn layui-btn-primary" id="uploadImg"><i class="layui-icon">&#xe67c;</i>上传图片</button>'
                            , '</li>'
                            , '<li class="layui-form-item" style="text-align: center;">'
                            , '<button type="button" lay-submit lay-filter="uploadImages" class="layui-btn">确认</button>'
                            , '</li>'
                            , '</ul>'].join('')
                        , success: function (layero, index) {
                            var image = layero.find('input[name="image"]');

                            //执行上传实例
                            upload.render({
                                elem: '#uploadImg'
                                , url: '/post/upload'
                                , size: 2000
                                , done: function (res) {
                                    if (res.code === 200) {
                                        image.val(res.data.uri);
                                    } else {
                                        layer.msg(res.msg, {icon: 5});
                                    }
                                }
                            });

                            form.on('submit(uploadImages)', function (data) {
                                var field = data.field;
                                if (!field.image) return image.focus();
                                layui.focusInsert(editor[0], 'img[' + field.image + '] ');
                                layer.close(index);
                            });
                        }
                    });
                }
                , href: function (editor) { //超链接
                    layer.prompt({
                        title: '请输入合法链接'
                        , shade: false
                        , fixed: false
                        , id: 'LAY_flyedit_href'
                        , offset: [
                            editor.offset().top - $(window).scrollTop() + 'px'
                            , editor.offset().left + 'px'
                        ]
                    }, function (val, index, elem) {
                        if (!/^http(s*):\/\/[\S]/.test(val)) {
                            layer.tips('这根本不是个链接，不要骗我。', elem, {tips: 1})
                            return;
                        }
                        layui.focusInsert(editor[0], ' a(' + val + ')[' + val + '] ');
                        layer.close(index);
                    });
                }
                , code: function (editor) { //插入代码
                    layer.prompt({
                        title: '请贴入代码或任意文本'
                        , formType: 2
                        , maxlength: 10000
                        , shade: false
                        , id: 'LAY_flyedit_code'
                        , area: ['800px', '360px']
                    }, function (val, index, elem) {
                        layui.focusInsert(editor[0], '[pre]\n' + val + '\n[/pre]');
                        layer.close(index);
                    });
                }
                , hr: function (editor) { //插入水平分割线
                    layui.focusInsert(editor[0], '[hr]');
                }
                , yulan: function (editor) { //预览
                    var content = editor.val();

                    content = /^\{html\}/.test(content)
                        ? content.replace(/^\{html\}/, '')
                        : fly.content(content);

                    layer.open({
                        type: 1
                        , title: '预览'
                        , shade: false
                        , area: ['100%', '100%']
                        , scrollbar: false
                        , content: '<div class="detail-body" style="margin:20px;">' + content + '</div>'
                    });
                }
            };

            layui.use('face', function (face) {
                options = options || {};
                fly.faces = face;
                $(options.elem).each(function (index) {
                    var that = this, othis = $(that), parent = othis.parent();
                    parent.prepend(html);
                    parent.find('.fly-edit span').on('click', function (event) {
                        var type = $(this).attr('type');
                        mod[type].call(that, othis, this);
                        if (type === 'face') {
                            event.stopPropagation()
                        }
                    });
                });
            });

        }

        , escape: function (html) {
            return String(html || '').replace(/&(?!#?[a-zA-Z0-9]+;)/g, '&amp;')
                .replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/'/g, '&#39;').replace(/"/g, '&quot;');
        }

        //内容转义
        , content: function (content) {
            //支持的html标签
            var html = function (end) {
                return new RegExp('\\n*\\[' + (end || '') + '(pre|hr|div|span|p|table|thead|th|tbody|tr|td|ul|li|ol|li|dl|dt|dd|h2|h3|h4|h5)([\\s\\S]*?)\\]\\n*', 'g');
            };
            content = fly.escape(content || '') //XSS
                .replace(/img\[([^\s]+?)\]/g, function (img) {  //转义图片
                    return '<img src="' + img.replace(/(^img\[)|(\]$)/g, '') + '">';
                }).replace(/@(\S+)(\s+?|$)/g, '@<a href="javascript:;" class="fly-aite">$1</a>$2') //转义@
                .replace(/face\[([^\s\[\]]+?)\]/g, function (face) {  //转义表情
                    var alt = face.replace(/^face/g, '');
                    return '<img alt="' + alt + '" title="' + alt + '" src="' + fly.faces[alt] + '">';
                }).replace(/a\([\s\S]+?\)\[[\s\S]*?\]/g, function (str) { //转义链接
                    var href = (str.match(/a\(([\s\S]+?)\)\[/) || [])[1];
                    var text = (str.match(/\)\[([\s\S]*?)\]/) || [])[1];
                    if (!href) return str;
                    var rel = /^(http(s)*:\/\/)\b(?!(\w+\.)*(sentsin.com|layui.com))\b/.test(href.replace(/\s/g, ''));
                    return '<a href="' + href + '" target="_blank"' + (rel ? ' rel="nofollow"' : '') + '>' + (text || href) + '</a>';
                }).replace(html(), '\<$1 $2\>').replace(html('/'), '\</$1\>') //转移HTML代码
                .replace(/\n/g, '<br>') //转义换行
            return content;
        }
    };

    //添加验证规则
    form.verify({
        tel : function(value){
            var myreg=/^[1][3,4,5,7,8][0-9]{9}$/;
            if (!myreg.test(value)){
                return "手机号码不正确";
            }
        },
        postcode : function(value){
            var myreg= /^[1-9][0-9]{5}$/;
            if (!myreg.test(value)){
                return "邮政编码不正确";
            }
        },
        userBirthday : function(value){
            var myreg = /^(\d{4})[\u4e00-\u9fa5]|[-\/](\d{1}|0\d{1}|1[0-2])([\u4e00-\u9fa5]|[-\/](\d{1}|0\d{1}|[1-2][0-9]|3[0-1]))*$/;
            if(!myreg.test(value)){
                return "出生日期格式不正确！";
            }
        }
    });

    //表单提交
    form.on('submit(jl)', function (data) {
        var action = $(data.form).attr('action'), button = $(data.elem);
        jl.req(action, data.field, function (res) {
            var end = function () {
                if (res.data.url) {
                    location.href = res.data.url;
                } else {
                    fly.form[action || button.attr('key')](data.field, data.form);
                }
            };
            if (res.code === 200 || res.code ===400 || res.code === 100) {
                button.attr('alert') ? layer.alert(res.msg, {
                    icon: 1,
                    time: 10 * 1000,
                    end: end
                }) : end();
            }
        });
        return false;
    });

    //加载特定模块
    if (layui.cache.page && layui.cache.page !== 'public') {
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


    addAddress();
    rightBuy();
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
                            ' <a href="/product/'+ item.id +'"><img lay-src="'+ item.imgs +'" alt="图片未加载"></a></div>' +
                            ' <div class="box-body"><h2 class="box-rtitle"><a href="/product/'+ item.id +'">'+ item.name +'</a></h2>' +
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
    
    function rightBuy() {
        $('.right_go').on('click',function () {
            var that = $(this);
            jl.req('/cart/add',{
                id : that.parent().data('id'),
                number : $('#num_input').val() || "1",
            },function (res) {
                layer.msg(res.msg);
                if (res.code === 200){
                    window.location.href = "/user/cart";
                }
            });
            return false;
        })
    }
    function addCart() {
        $(document).on('click', '.add_cart', function() {
            var that = $(this);
            // console.log(that.parent().data('id'));
            jl.req('/cart/add',{id :that.parent().data('id'), number : $('#num_input').val() || "1",},function (res) {
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
    function addAddress(){
        $('#new_address').on('click',function () {
            layer.open({
                 type : 2
                , title: '新增地址'
                ,area: ['400px', '500px']
                , shade: false
                ,content: ['/user/address']
            });
        })
    }
    form.on('submit(addAddress)',function (data) {
        var province = $('#province option:selected').text();//选中的值
        var city = $('#city option:selected').text();
        var area = $('#area option:selected').text();
        var dital = $('#dital').val();
        if ($('#area option:selected').val() == "" || $('#area option:selected').val() == null){
            layer.msg("请选择完整地址");
            return false;
        }
        var place = province +"-"+ city + "-"+ area + "-" + dital;
        jl.req('/user/address', {
            name: data.field.name,
            phone: data.field.phone,
            postcode: data.field.postcode,
            place: place
        }, function (res) {
            layer.msg(res.msg);
            if (res.code === 200) {
                window.parent.location.reload();
            }
        });
        return false;
    });
    exports('jl',jl);
});