layui.use('form', function () {
    var form = layui.form,
        layer = layui.layer;
});
var vue = new Vue({
    el: '#app',
    data: {
       
        booklist: [], //显示的所有书列表
        thisbook: [], //修改书时当前这本
        lendlist: [], //借书记录
        all: 0, //总数
        cur: 1, //第几页
        totalPage: 24, //当前页条数
        list_be: 0,
        list_end: 0,
        userInfo:[]

    },

    created: function () {
        
        this.bookall();
         this.getlen();
        if(!this.islogin())
             this.login();

    },
    methods: {
        
     
//////////////////////////////////////////////////////////////////////////////////////////////
// 弹出登录界面
        login: function(){
            layui.use("layer",function(){              
                var layer = layui.layer;  //layer初始化               
              
                layer.open({
                    type: 1,
                    title :'用户登录',
                    area: '650px',
                    content: $('#box') //这里content是一个DOM，注意：最好该元素要存放在body最外层，否则可能被其它的相对元素所影响
                });
             } )
        },
// 提交登陆
        take_login(){

            let user =document.getElementById("user").value;
            let psw =document.getElementById("psw").value;
            if(user==null||psw==null){
                layer.msg('请正确输入');
                return;
            }
            let url= "?userId= "+user+ "&password="+psw
            $.ajax({
                url: '/user/login'+url,
                dataType: 'text',
                success: (list) => {
                    list = eval('(' + list + ')');
                    if(list.status==200){
                        sessionStorage.allowLendNum=list.userInfo.allowLendNum; 
                        sessionStorage.userId=list.userInfo.userId;    
                        location.reload()                  
                    }
                    else {
                        layer.msg('密码错误');
                    }
                   
                }
            })
           
        },
//判断是否是登陆状态
        islogin(){
           
            if(sessionStorage.userId==undefined||sessionStorage.userId==null)
             return false;
            else return true;
           
        },
  // 退出登录
        outting(){
            sessionStorage.clear();
            location.reload()   
        },
///////////////////////////////////////

// 弹出记录窗口
        lend_list: function (){
            layui.use("layer",function(){              
                var layer = layui.layer;  //layer初始化               
                layer.open({
                    type: 1,
                    title :'用户登录',
                    area: '650px',
                    content: $('#book') 
                });
             } )
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
                  //  console.log(this.booklist)
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
        //图书弹框
        rebook: function (id) {
             if(!this.islogin()){
                this.login();
                return;
            }

            console.log(id);
            this.thisbook = this.booklist[id + this.list_be];
            
            layer.open({
                type: 1,
                title:'图书详情',
                area: '800px',
                content: $('#id') //这里content是一个DOM，注意：最好该元素要存放在body最外层，否则可能被其它的相对元素所影响
            });


        },
        //提交借书申请
        reb: function (id) {
             let url="?isbn="+this.thisbook.isbn+"&title="+this.thisbook.title+"&author="+this.thisbook.author+"&bookNum="+this.thisbook.bookNum;
           
             $.ajax({
                url: '/lend/lendBook'+url,
                dataType: 'text',
                success: (list) => {
                    list = eval('(' + list + ')');
                    if(list.status==200){

                        this.bookall();
                        sessionStorage.allowLendNum=sessionStorage.allowLendNum-1;
                        layer.msg("申请成功")  
	        this.getlen();
                    }  
                    else {    
                       	 layer.msg("申请失败")
		}  
                }
            })
             

        },

///////////////////////////////////////////////////////////////////////////////////////////////    
       
  //获取借书记录      
        getlen: function () {
            
            $.ajax({
                url: '/lend/queryLendInfo',
                dataType: 'text',
                success: (list) => {
                    list = eval('(' + list + ')');
                    this.lendlist = list;               
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

