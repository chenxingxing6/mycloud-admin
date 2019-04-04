var vm = new Vue({
    el:'#rrapp',
    data:{
        current:{
            name: "所有",
            type: 0
        },
        showList: true,
        title:null,
        showCreateDir:false,
        uploadFile: false,
        items:[],
        files:[
            {id:1,parentId:1,name:"文件名1.doc",type:1,time:"2014-10-13",url:"http://193.112.27.123:8012/onlinePreview?url=http://193.112.27.123:8012/demo/%E5%92%95%E6%B3%A1%E5%AD%A6%E9%99%A2Java%E6%9E%B6%E6%9E%84%E5%B8%88VIP%E8%AF%BE%E7%A8%8B%E5%A4%A7.png"},
            {id:2,parentId:1,name:"文件名2.doc",type:2,time:"2014-10-13",url:"http://193.112.27.123:8012/onlinePreview?url=http://193.112.27.123:8012/demo/XiaoYing_Video_1549677991268.mp4"},
            {id:3,parentId:1,name:"文件名3.doc",type:3,time:"2014-10-13",url:"http://193.112.27.123:8012/onlinePreview?url=http://193.112.27.123:8012/demo/鞠文娴-BINGBIAN病变.mp3"},
            {id:4,parentId:1,name:"文件名4.doc",type:4,time:"2014-10-13",url:"http://www.baidu.com"},
            {id:5,parentId:1,name:"文件名5.doc",type:5,time:"2014-10-13",url:"http://www.baidu.com"}
        ],
        fileList: [
            {name: 'food.jpeg', url: 'https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100'},
            {name: 'food2.jpeg', url: 'https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100'}
        ],
        sysDisk: {}
    },
    created: function () {
        console.log(this)
        this.listDir();
    },
    methods: {
        listDir: function(){
            var _this = this
            axios.get(baseURL +'front/disk/listDir')
                .then(function (res) {
                    console.log(res.data);
                    _this.items = res.data.diskDirs;
                })
                .catch(function (error) {
                    console.log(error);
                })
                .then(function () {
                });
        },
        clickFile: function (type, name) {
            this.current.name = name;
            this.current.type = type;
            if(type == 2){
                this.files = [];
            }
        },
        see: function (url) {
            layer.open({
                type: 2,
                area: ['700px', '450px'],
                fixed: false, //不固定
                maxmin: true,
                content: url
            });
        },
        reNameDir: function () {
            if(this.current.type == 0){
                layer.tips('请选择目录', '#reNameDir', {
                    tips: [1, '#3595CC'],
                    time: 4000
                });
                return;
            }
            this.title ="重命名";
            this.showCreateDir = true;
            this.showList = false;
            this.sysDisk.name = this.current.name;
            this.sysDisk.id = this.current.type;
        },
        delFile: function (id) {
            alert("del文件"+id);
        },
        createFile: function () {
            this.title ="创建目录";
            this.showCreateDir = true;
            this.showList = false;
            this.sysDisk = {};
        },
        uploadFileView: function () {
            if(this.current.type == 0){
                layer.tips('不能为所有', '#uploadFile', {
                    tips: [1, '#3595CC'],
                    time: 4000
                });
                return;
            }
            this.title = '导入文件';
            this.uploadFile = true;
            this.showList = false;
            /*layer.open({
                type: 2,
                area: ['780px', '650px'],
                fixed: false, //不固定
                maxmin: true,
                content: 'upload.html'
            });*/
        },
        saveOrUpdate: function (event) {
            var url = vm.sysDisk.id == null ? "front/disk/createDir" : "front/disk/updateDir";
            $.ajax({
                type: "POST",
                url: baseURL + url,
                contentType: "application/json",
                data: JSON.stringify(vm.sysDisk),
                success: function(r){
                    if(r.code === 0){
                        alert('操作成功', function(index){
                            vm.reload();
                        });
                    }else{
                        alert(r.msg);
                    }
                }
            });
        },
        del: function (event) {
            var ids = getSelectedRows();
            if(ids == null){
                return ;
            }
            confirm('确定要删除选中的记录？', function(){
                $.ajax({
                    type: "POST",
                    url: baseURL + "front/file/deleteFileOrFolder",
                    contentType: "application/json",
                    data: JSON.stringify(ids),
                    success: function(r){
                        if(r.code == 0){
                            alert('操作成功', function(index){
                                $("#jqGrid").trigger("reloadGrid");
                            });
                        }else{
                            alert(r.msg);
                        }
                    }
                });
            });
        },
        getInfo: function(id){
            $.get(baseURL + "front/file/info/"+id, function(r){
                vm.file = r.file;
            });
        },
        reload: function (event) {
            this.title = null;
            this.showCreateDir = false;
            this.showList = true;
            this.listDir();
        },
        handleRemove(file, fileList) {
            console.log(file, fileList);
        },
        handlePreview(file) {
            console.log(file);
        },
        handleExceed(files, fileList) {
            this.$message.warning('当前限制选择 3 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件');
        },
        beforeRemove(file, fileList) {
            return this.$confirm('确定移除 ${ file.name }？');
        }
    }
});
