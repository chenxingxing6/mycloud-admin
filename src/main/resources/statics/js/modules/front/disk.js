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
        isUpdateFile: false,
        items:[],
        files:[],
        fe:[],
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
                    _this.files = res.data.files;
                    //console.log("files:"+_this.files);
                })
                .catch(function (error) {
                    console.log(error);
                })
                .then(function () {
                });
        },
        downFile: function(){
            console.log("下载文件"+this.fe.id);
            window.location.href="  http://localhost:9000/front/disk/userdown?fileId="+this.fe.id;

        },
        clickFile: function (type, name) {
            this.current.name = name;
            this.current.type = type;
            this.listFiles();
        },
        seeFileView: function (id) {
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
        goReNameDirView: function () {
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
        goUpdateFileView: function (id,fileName) {
            if(id == null || fileName == null){
                return ;
            }
            this.title ="修改文件";
            this.fe.id = id;
            this.fe.originalName = fileName;
            this.isUpdateFile = true;
            this.showList = false;
            console.log(this.fe);
        },
        goCreateFileView: function () {
            this.title ="创建目录";
            this.showCreateDir = true;
            this.showList = false;
            this.sysDisk = {};
        },
        goUploadFileView: function () {
            if(this.current.type == 0){
                layer.tips('请选择某个类别再上传文件', '#uploadFile', {
                    tips: [1, '#3595CC'],
                    time: 4000
                });
                return;
            }
            var _this = this;
            localStorage.setItem('diskId', this.current.type);
            layer.open({
                type: 2,
                area: ['600px', '450px'],
                fixed: false, //不固定
                maxmin: true,
                content: 'upload.html',
                end: function () {
                    layer.msg('上传成功!',{icon:1,time:1000});
                    _this.reload();
                }
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
        delFile: function (event) {
            var id = this.fe.id;
            if (id == null){
                return;
            }
            var _this = this;
            confirm('确定要删除该文件吗？', function(){
                axios.post(baseURL +'front/disk/delFile?fileId='+ id)
                    .then(function (res) {
                        console.log(res)
                        if (res.data.code == 0){
                            alert('删除成功', function(index){
                                _this.reload();
                            });
                        }else {
                            alert(res.msg)
                        }
                    })
                    .catch(function (error) {
                        alert(res.msg)
                    })
                    .then(function () {});
            });

        },
        updateFile: function (event) {
            var _this = this;
            axios.get(baseURL +'front/file/updateFileName',{
                params:{
                    fileId: vm.fe.id,
                    fileName: vm.fe.originalName,
                }
            })
            .then(function (res) {
                console.log(res)
                if (res.data.code === 0){
                    alert('更新成功', function(index){
                        _this.reload();
                    });
                }else {
                    alert(res.msg)
                }
            })
            .catch(function (error) {
                alert(res.msg)
            })
            .then(function () {});
        },
        getInfo: function(id){
            $.get(baseURL + "front/file/info/"+id, function(r){
                vm.file = r.file;
            });
        },
        reload: function (event) {
            this.title = null;
            this.showCreateDir = false;
            this.isUpdateFile = false,
            this.showList = true;
            this.listDir();
            this.listFiles();
        }
    }
});
