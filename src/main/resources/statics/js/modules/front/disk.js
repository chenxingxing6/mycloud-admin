var vm = new Vue({
	el:'#rrapp',
	data:{
		current:"所有",
		title:null,
        showCreateDir:false,
        showList:true,
        types:[
        	{name:"文件",type:1},
            {name:"图片",type:2},
            {name:"视频",type:3},
            {name:"音乐",type:4},
            {name:"文档",type:5}
		],
        files:[
            {id:1,parentId:1,name:"文件名1.doc",type:1,time:"2014-10-13",url:"http://www.baidu.com"},
            {id:2,parentId:1,name:"文件名2.doc",type:2,time:"2014-10-13",url:"http://www.baidu.com"},
            {id:3,parentId:1,name:"文件名3.doc",type:3,time:"2014-10-13",url:"http://www.baidu.com"},
            {id:4,parentId:1,name:"文件名4.doc",type:4,time:"2014-10-13",url:"http://www.baidu.com"},
            {id:5,parentId:1,name:"文件名5.doc",type:5,time:"2014-10-13",url:"http://www.baidu.com"}
        ],
		file:{name:""}
	},
	methods: {
        clickFile: function (type, name) {
			this.current = name;
			if(type == 2){
				this.files = null;
			}
		},
        see: function (url) {
            alert("see文件"+url);
        },
        delFile: function (id) {
            alert("del文件"+id);
        },
        createFile: function () {
        	if(this.current === '所有'){
                alert("不能为所有");
                return;
			}
			this.title ="创建目录";
        	this.showCreateDir = true;
        	this.showList = false;
        },
        uploadFile: function () {
            layer.open({
                type: 2,
                area: ['700px', '450px'],
                fixed: false, //不固定
                maxmin: true,
                content: 'upload.html'
            });
        },
		saveOrUpdate: function (event) {
        	var creatUrl = "front/file/createFile?dirName="+vm.dirName+"&originalDir="+vm.originalDir+"&parentId="+vm.parentId;
			var url = vm.file.id == null ? creatUrl : "front/file/updateFile";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.file),
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
		}
	}
});
