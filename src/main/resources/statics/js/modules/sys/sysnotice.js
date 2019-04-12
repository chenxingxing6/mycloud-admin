$(function () {
    $('.summernote').summernote({
        height : '220px',
        lang : 'zh-CN'
    });

    $("#jqGrid").jqGrid({
        url: baseURL + 'sys/sysnotice/list',
        datatype: "json",
        colModel: [
			{ label: '编号', name: 'id', index: 'id', width: 50, key: true },
			{ label: '公告标题', name: 'noticeTitle', index: 'notice_title', width: 80 },
            { label: '公告类型', name: 'noticeType', width: 60, index: 'notice_type',formatter: function(value, options, row){
                    return value === 1 ?
                        '<span class="label label-danger">通知</span>' :
                        '<span class="label label-success">公告</span>';
                }},
            { label: '状态', name: 'status', width: 60, index: 'status',formatter: function(value, options, row){
                    return value === 0 ?
                        '<span class="label label-success">正常</span>' :
                        '<span class="label label-danger">关闭</span>';
            }},
			{ label: '创建者', name: 'createUser', index: 'create_user', width: 80 },
			{ label: '创建时间', name: 'newTime', width: 80},
			{ label: '操作', name: '', index: 'remark', width: 80 ,
                formatter: function(value, grid, rows, state) {
                    var actions = [];
                    actions.push(' <a class="btn btn-primary btn-sm" onclick="vm.editById(\''+rows.id+'\')">编辑</a>');
                    actions.push(' <a class="btn btn-primary btn-sm" onclick="vm.delById(\''+rows.id+'\')">&nbsp;删除</a>');
                    return actions.join('');
                }
			}
        ],
		viewrecords: true,
        height: 385,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true,
        rownumWidth: 25,
        autowidth:true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader : {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
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
		title: null,
		q:{
			title:null,
			optuser:null
		},
		sysNotice: {}
	},
	methods: {
        query: function () {
            vm.reload();
        },
        add: function () {
            vm.showList = false;
            vm.title = "新增";
            vm.sysNotice = {};
        },
        update: function (event) {
            var id = getSelectedRow();
            if (id == null) {
                return;
            }
            vm.showList = false;
            vm.title = "修改";
            $.get(baseURL + "sys/sysnotice/info/" + id, function (r) {
                vm.sysNotice = r.sysNotice;
                $(".summernote").code(r.sysNotice.noticeContent);
            });
        },
        editById: function (id) {
            if (id == null) {
                return;
            }
            vm.showList = false;
            vm.title = "修改";
            $.get(baseURL + "sys/sysnotice/info/" + id, function (r) {
                vm.sysNotice = r.sysNotice;
                $(".summernote").code(r.sysNotice.noticeContent);
            });
        },
        delById: function (id) {
            if (id == null) {
                return;
            }
            confirm('确定要删除？', function () {
                $.ajax({
                    type: "POST",
                    url: baseURL + "sys/sysnotice/deleteById?id="+id,
                    contentType: "application/json",
                    success: function (r) {
                        if (r.code == 0) {
                            alert('操作成功', function (index) {
                                $("#jqGrid").trigger("reloadGrid");
                            });
                        } else {
                            alert(r.msg);
                        }
                    }
                });
            });
        },
        saveOrUpdate: function (event) {
            var comment = $(".summernote").code()
            vm.sysNotice.noticeContent = comment;
            console.log("comment" + comment)
            var url = vm.sysNotice.id == null ? "sys/sysnotice/save" : "sys/sysnotice/update";
            $.ajax({
                type: "POST",
                url: baseURL + url,
                contentType: "application/json",
                data: JSON.stringify(vm.sysNotice),
                success: function (r) {
                    if (r.code === 0) {
                        alert('操作成功', function (index) {
                            vm.reload();
                        });
                    } else {
                        alert(r.msg);
                    }
                }
            });
        },
        del: function (event) {
            var ids = getSelectedRows();
            if (ids == null) {
                return;
            }

            confirm('确定要删除选中的记录？', function () {
                $.ajax({
                    type: "POST",
                    url: baseURL + "sys/sysnotice/delete",
                    contentType: "application/json",
                    data: JSON.stringify(ids),
                    success: function (r) {
                        if (r.code == 0) {
                            alert('操作成功', function (index) {
                                $("#jqGrid").trigger("reloadGrid");
                            });
                        } else {
                            alert(r.msg);
                        }
                    }
                });
            });
        },
        getInfo: function (id) {
            $.get(baseURL + "sys/sysnotice/info/" + id, function (r) {
                vm.sysNotice = r.sysNotice;
            });
        },
        reload: function () {
            vm.showList = true;
            var title = vm.q.title;
            var optuser = vm.q.optuser;
            var page = $("#jqGrid").jqGrid('getGridParam','page');
            $("#jqGrid").jqGrid('setGridParam',{
                postData:{'optuser': optuser},
                page:page
            }).trigger("reloadGrid");
        }
    }
});
