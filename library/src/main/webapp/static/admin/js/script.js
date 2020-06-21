if (!/^http(s*):\/\//.test(location.href)) {
    alert('请先部署到 localhost 下再访问');
    window.location.href = 'login.html';
}


layui.use('form', function () {
    var form = layui.form,
        layer = layui.layer;
});

var vue = new Vue({
    el: '#app',
    data: {
        webname: config.webname,
        menu: [], //左边目录
        address: [], //左边跳转连接
        queryAllblackList: [], //黑名单列表
        user_list: [], //用户列表
        booklist: [], //显示的所有书列表
        thisbook: [], //修改书时当前这本
        lendlist: [], //借书记录
        all: 0, //总数
        cur: 1, //第几页
        totalPage: 25, //当前页条数
        list_be: 0,
        list_end: 0

    },
    //加载左侧菜单
    created: function () {

        if (config.debug) {
            $.ajax({
                url: config.menuUrl,
                dataType: 'text',
                success: (menu) => {
                    //创建本地session
                    menu = eval('(' + menu + ')');
                    //     sessionStorage.menu = JSON.stringify(menu);

                    this.menu = menu;
                    this.thisActive(); //菜单高亮
                    this.thisAttr(); //当前位置
                }
            })
        } else {
            let data = sessionStorage.menu;
            if (!data) {
                $.ajax({
                    url: config.menuUrl,
                    dataType: 'text',
                    success: (menu) => {
                        menu = eval('(' + menu + ')');
                        //   sessionStorage.menu = JSON.stringify(menu);
                        this.menu = menu;
                        this.thisActive();
                        this.thisAttr();
                    }
                })
            } else {
                //  this.menu = JSON.parse(data);
                this.thisActive();
                this.thisAttr();
            }
        }

        ///用户列表
        this.uslist();
        //黑名单
        this.bklist();
        this.getlenlist();
        this.bookall();
    },
    methods: {

        //记住收展
        onActive: function (pid, id = false) {
            let data;
            if (id === false) {

                data = this.menu[pid];

                if (data.url.length > 0) {
                    this.menu.forEach((v, k) => {
                        v.active = false;
                        v.list.forEach((v2, k2) => {
                            v2.active = false;
                        })
                    })

                    data.active = true;

                }

                data.hidden = !data.hidden;

            } else {

                this.menu.forEach((v, k) => {
                    v.active = false;
                    v.list.forEach((v2, k2) => {
                        v2.active = false;
                    })
                })

                data = this.menu[pid].list[id];
            }
            
            if (data.url.length > 0) {
                if (data.target) {
                    if (data.target == '_blank') {
                        window.open(data.url);
                    } else {
                        window.location.href = data.url;
                    }

                } else {
                    window.location.href = data.url;
                }

            }
        },
        //菜单高亮
        thisActive: function () {
            let pathname = window.location.pathname;
            let host = window.location.host;
            let pid = false;
            let id = false;
            this.menu.forEach((v, k) => {
                let url = v.url;
                if (url.length > 0) {
                    if (url[0] != '/' && url.substr(0, 4) != 'http') {
                        url = '/' + url;
                    }
                }
                if (pathname == url) {
                    pid = k;
                }
                v.list.forEach((v2, k2) => {
                    let url = v2.url;

                    if (url.length > 0) {
                        if (url[0] != '/' && url.substr(0, 4) != 'http') {
                            url = '/' + url;
                        }
                    }
                    if (pathname == url) {
                        pid = k;
                        id = k2;
                    }
                })
            })


            if (id !== false) {
                this.menu[pid].list[id].active = true;
            } else {
                if (pid !== false) {
                    this.menu[pid].active = true;
                }
            }



        },
        //当前位置
        thisAttr: function () {
            //当前位置
            let address = [{
                name: '用户管理',
                url: 'user_index.html'
            }];
            this.menu.forEach((v, k) => {
                v.list.forEach((v2, k2) => {
                    if (v2.active) {
                        address.push({
                            name: v.name,
                            url: 'javascript:;'
                        })
                        address.push({
                            name: v2.name,
                            url: v2.url,
                        })
                        this.address = address;
                    }
                })
            })
        },
//////////////////////////////////////////////////////////////////////////////////////////////
        // 用户列表
        uslist: function () {
            $.ajax({
                url: '/admin/queryAlluser',
                dataType: 'text',
                success: (menu) => {
                    menu = eval('(' + menu + ')');
                    this.user_list = menu;
                }
            })
        },
        //查找用户
        greet: function () {
            let str = document.getElementById('k_id').value;
            str = '/admin/queryuser?userId=' + str;
            ///用户
            $.ajax({
                url: str,
                dataType: 'text',
                success: (menu) => {
                    if (menu === "[null]")
                        this.user_list = [];
                    else {
                        menu = eval('(' + menu + ')');
                        this.user_list = menu;
                    }
                }
            })
        },
        // 拉黑列表
        bklist: function () {
            $.ajax({
                url: '/admin/queryAllblackList',
                dataType: 'text',
                success: (menu) => {
                    menu = eval('(' + menu + ')');
                    this.queryAllblackList = menu;
                }
            })
        },
        //拉黑解封按钮事件
        chbox: function (usid, s) {
            let str;

            if (s === 'h') {
                str = "/admin/removeblacklist?userId=" + usid;

            } else str = "/admin/addblacklist?userId=" + usid;

            $.ajax({
                url: str,
                dataType: 'text',
                success: (menu) => {
                    if (s === 'h')
                        this.bklist();
                    else
                        this.uslist();
                    
                }

            })
        },
//////////////////////////////////////////////////////////////////////////////////////////////
        //分页   
        btnClick: function (data) { //页码点击事件
            if (data != this.cur) {
                this.cur = data
            }
            //根据点击页数请求数据
            this.pageClick();
        },
        //过页
        pageClick: function () {

            if (this.all >= this.cur) {
                this.list_be = (this.totalPage) * (this.cur - 1);
                this.list_end = this.list_be + this.totalPage;


            } else {
                this.list_be = (this.totalPage) * (this.cur - 1);
                this.list_end = this.booklist.length;

            }
            console.log("cur : " + this.cur + "  list_be : " + this.list_be + "   list_end : " + this.list_end)


        },
 //////////////////////////////////////////////////////////////////////////////////////////////       
        //所有图书  
        bookall: function () {
            $.ajax({
                url: '/book/queryall',
                dataType: 'text',
                success: (list) => {
                    list = eval('(' + list + ')');

                    this.booklist = list;
                    this.all = this.booklist.length;
                    if (this.booklist.length / this.totalPage > 1) {

                        this.all = Math.ceil(this.booklist.length / this.totalPage);
                        this.list_end = this.totalPage;
                    } else {
                        this.all = 1;
                        this.list_end = this.booklist.length ;//- 1
                    }
                   
                }
            })
        },
        // 选择查找图书
        getbook: function () {
            let name = document.getElementById('slect_name').value;
            let sou = document.getElementById('s_id').value;
            $.ajax({
                url: '/book/queryall?' + name + '=' + sou,
                dataType: 'text',
                success: (list) => {
                    list = eval('(' + list + ')');

                    this.booklist = list;
                    this.all = this.booklist.length;
                    if (this.booklist.length / this.totalPage > 1) {

                        this.all = Math.ceil(this.booklist.length / this.totalPage) ;
                        this.list_end = this.totalPage;
                    } else {
                        this.all = 1;
                        this.list_end = this.booklist.length ;  //- 1
                    }
                    
                }
            })
        },
        //修改图书弹框
        rebook: function (id) {
            console.log(id);
            this.thisbook = this.booklist[id + this.list_be];
            
            layer.open({
                type: 1,
                area: '800px',
                content: $('#id') //这里content是一个DOM，注意：最好该元素要存放在body最外层，否则可能被其它的相对元素所影响
            });


        },
        //提交修改图书表单
        reb: function () {
            var title = document.getElementById('title').value;
            console.log(title)
            if (title == null || title == " " || title.length < 1) {

                msg(0, '请填写标题');
                return false;
            }
            var author = document.getElementById('author').value;
            console.log(author)
            if (author == null || author == " " || author.length < 1) {
                msg(0, '请填写作者');
                return false;
            }

            let formData = new FormData($('#id')[0]);
            $.ajax({
                url: "/book/updateBook",
                type: "POST",
                data: formData,
                async: false,
                cache: false,
                contentType: false,
                processData: false,
                success: function (data) {
                    msg(1, '添加成功');
                    this.bookall();
                },
                error: function (data) {
                    msg(0, '无法提交');
                }
            });
            return false;


        },

///////////////////////////////////////////////////////////////////////////////////////////////
        //获取借书记录
        getlenlist: function () {

            $.ajax({
                url: '/lend/queryAll',
                dataType: 'text',
                success: (list) => {
                    list = eval('(' + list + ')');

                    this.lendlist = list;
                    this.all = this.lendlist.length;
                    if (this.lendlist.length / this.totalPage > 1) {

                        this.all = Math.ceil(this.lendlist.length / this.totalPage);
                        this.list_end = this.totalPage;
                    } else {
                        this.all = 1;
                        this.list_end = this.lendlist.length;
                    }
                    //console.log(this.lendlist.length+"  "+this.list_end+"  "+this.list_be)
                }
            })

        },
        //修改借书记录弹框

        relend: function (id) {

            this.thisbook = this.lendlist[id + this.list_be];
          
            layer.open({
                type: 1,
                area: '800px',
                content: $('#tkid') //这里content是一个DOM，注意：最好该元素要存放在body最外层，否则可能被其它的相对元素所影响
            });


        },
        //提交修改借书记录表单
        retkl: function () {
            var userId = document.getElementById('userId').value;
            
            if (userId == null || userId == " " || userId.length < 1) {

                msg(0, '请填写用户id');
                return false;
            }
            var isbn = document.getElementById('isbn').value;
            console.log(isbn)
            if (isbn == null || isbn == " " || isbn.length < 1) {
                msg(0, '请填写isbn');
                return false;
            }

            let formData = new FormData($('#tkid')[0]);
            $.ajax({
                url: "/lend/admin/updateUserLendInfo",
                type: "POST",
                data: formData,
                async: false,
                cache: false,
                contentType: false,
                processData: false,
                success: function (data) {
                    msg(1, '修改成功');
                    this.getlenlist();
                },
                error: function (data) {
                    msg(0, '无法提交');
                }
            });
            return false;


        },
        //筛选借书记录
        getlen: function () {
            let name = document.getElementById('slect_name').value;
            let sou = document.getElementById('s_id').value;
            $.ajax({
                url: '/lend/queryByLimit?' + name + '=' + sou,
                dataType: 'text',
                success: (list) => {
                    list = eval('(' + list + ')');

                    this.lendlist = list;
                    this.all = this.lendlist.length;
                    if (this.lendlist.length / this.totalPage > 1) {

                        this.all = Math.ceil(this.lendlist.length / this.totalPage) ;
                        this.list_end = this.totalPage;
                    } else {
                        this.all = 1;
                        this.list_end = this.lendlist.length;//- 1
                    }
                   
                }
            })
        }
//////////////////////////////////////////////////////////////////////////////////////////////////
    },
    computed: {
        //分页
        indexs: function () {
            var left = 1;
            var right = this.all;
            var ar = [];
            if (this.all >= 5) {
                if (this.cur > 3 && this.cur < this.all - 2) {
                    left = this.cur - 2
                    right = this.cur + 2
                } else {
                    if (this.cur <= 3) {
                        left = 1
                        right = 5
                    } else {
                        right = this.all
                        left = this.all - 4
                    }
                }
            }
            while (left <= right) {
                ar.push(left)
                left++
            }
            return ar
        }
    }
})




//弹窗
function msg(code = 1, msg = '', url = '', s = 3) {
    if (typeof code == 'object') {
        msg = code.msg;
        url = code.url || '';
        s = code.s || 3;
        code = code.code;
    }
    code = code == 1 ? 1 : 2;
    layer.msg(msg, {
        icon: code,
        offset: config.layerMsg.offset || 't',
        shade: config.layerMsg.shade || [0.4, '#000']
    });
    if (url) {
        setTimeout(function () {
            window.location.href = url;
        }, s * 1000);
    }
}
//显示图片
function imgshow(obj, n) {
    var file = obj.files[0];

    console.log(obj);
    console.log(file);
    console.log("file.size = " + file.size); //file.size 单位为byte

    var reader = new FileReader();

    //读取文件过程方法
    reader.onloadstart = function (e) {
        console.log("开始读取....");
    }
    reader.onprogress = function (e) {
        console.log("正在读取中....");
    }
    reader.onabort = function (e) {
        console.log("中断读取....");
    }
    reader.onerror = function (e) {
        console.log("读取异常....");
    }
    reader.onload = function (e) {
        console.log("成功读取....");
        if (n == 1) {
            var img = document.getElementById("img1");
            img.src = e.target.result;
            //或者 img.src = this.result;  //e.target == this
        }


    }
    reader.readAsDataURL(file)
}
//上传图书表单
function subfun() {
    var title = document.getElementById('title').value;
    console.log(title)
    if (title == null || title == " " || title.length < 1) {

        msg(0, '请填写标题');
        return false;
    }
    var author = document.getElementById('author').value;
    console.log(author)
    if (author == null || author == " " || author.length < 1) {
        msg(0, '请填写作者');
        return false;
    }

    let formData = new FormData($('#addbook_f')[0]);
    $.ajax({
        url: "/book/addBook",
        type: "POST",
        data: formData,
        async: false,
        cache: false,
        contentType: false,
        processData: false,
        success: function (data) {
            msg(1, '添加成功');
        },
        error: function (data) {
            msg(0, '无法提交');
        }
    });
    return false;
}