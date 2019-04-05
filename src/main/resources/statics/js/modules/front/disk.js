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
        files:[],
        sysDisk: {}
    },
    created: function () {
        this.listDir();
        this.listFiles();
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
        listFiles: function(){
            var _this = this
            axios.get(baseURL +'front/disk/listFiles?diskId='+ _this.current.type)
                .then(function (res) {
                    console.log("files:"+res.data);
                    _this.files = res.data.files;
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
            this.listFiles();
        },
        see: function (id) {
            console.log("查看文件");
            var url = 'http://localhost:8012/onlinePreview?url=';
            axios.get(baseURL +'front/disk/downloadFile?fileId='+ id)
                .then(function (res) {
                    if (res.data.code == 0){
                        url = url + "http://localhost:8012/disk/"+ res.data.url;
                        console.log(res.data)
                    }
                })
                .catch(function (error) {
                    console.log(error)
                })
                .then(function () {
                    layer.open({
                        type: 2,
                        area: ['700px', '450px'],
                        fixed: false, //不固定
                        maxmin: true,
                        content: url
                    });
                });
        },
        reNameDir: function () {
            if(this.current.type == 0){
                layer.tips('请选择你需要修改的文件夹', '#reNameDir', {
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
                layer.tips('请选择某个类别再上传文件', '#uploadFile', {
                    tips: [1, '#3595CC'],
                    time: 4000
                });
                return;
            }
            localStorage.setItem('diskId', this.current.type);
            layer.open({
                type: 2,
                area: ['700px', '550px'],
                fixed: false, //不固定
                maxmin: true,
                content: 'upload.html'
            });
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
            this.listFiles();
        }
    }
});
