$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'front/file/list?parentId='+vm.parentId,
        datatype: "json",
        colModel: [
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true, hidden:true },
			{ label: '文件名', name: 'originalName', index: 'originalName', width: 80 ,
				formatter: function (value, grid, rows, state) {
				    var type = rows.type;
				    if (type == '0'){
                        return '<a href="javascript:void(0)" onclick="vm.subFile('+rows.id+')" style="color:blue;">'+value+'</a>';
                    }
                    return value;
                }},
			{ label: '大小', name: 'length', index: 'length', width: 80,
                formatter: function (value, grid, rows, state) {
				    var type = rows.type;
				    if (type == '0'){
				    	return "文件夹";
					}
                    return value;
                }},
			{ label: '修改时间', name: 'opTime', index: 'op_time', width: 80},
            { label: '其他', name: 'viewFlag', index: 'view_flag', width: 80,
				formatter: function (value, grid, rows, state) {
            	    var result = '';
            	    var type = rows.type;
            	    var viewFlag = rows.viewFlag;
                    result = '<a href="javascript:void(0);" style="color:#f60" onclick="vm.reNameView(\'' + rows.id + '\', \'' + rows.originalName + '\');">编辑 </a>';
            	    if (type == '1'){
                        '<a href="javascript:void(0);" style="color:#f60" onclick="modify(\'' + rows.id + '\');">下载 </a>'
					}else if (viewFlag ==1) {
                        result = result+ '<a href="javascript:void(0);" style="color:#f60" onclick="modify(\'' + rows.id + '\');">查看 </a>';
					}
                    return result;
                }}
        ],
		viewrecords: true,
        height: 400,
        rowNum: -1,
		rowList : [10,30,50],
        rownumbers: true,
        rownumWidth: 25,
        autowidth:true,
        multiselect: true,
        //pager: "#jqGridPager",
        jsonReader : {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount",
        },
        prmNames : {
            page:"page",
            rows:"limit",
            order: "order"
        },
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
        }
    });
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		showMkdir: false,
        showUpload: false,
        dirName: "/",
        originalDir: "/",
        parentId: 0,
        title: null,
		file: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
        mkdirView: function(){
			vm.showMkdir = true;
            vm.showList = false;
			vm.title = '创建文件夹';
            vm.file = {};
		},
        reNameView: function(id, originalName){
            console.log("id:"+id+"---name:"+originalName);
            vm.showMkdir = true;
            vm.showList = false;
            vm.title = '重名名';
            vm.file.id = id;
            vm.file.originalName = originalName;
        },
		uploadFile: function(){
			vm.showList = false;
			vm.showUpload = true;
			vm.title = "上传文件";
			vm.file = {};
		},
        subFile: function(id){
            vm.parentId = id;
            vm.reload();
			console.log("parentId:"+vm.parentId);
		},
		update: function (event) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";

            vm.getInfo(id)
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
			vm.showList = true;
			vm.showMkdir = false;
			vm.showUpload = false;
			var url = baseURL + 'front/file/list?parentId='+vm.parentId;
			console.log(url);
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
                page:page,
				url:url
            }).trigger("reloadGrid");
		}
	}
});
