/**
 * ProjectName: manage-template
 * FileName: addRole
 * Author:   wyait
 * Description:
 * Date:     2020/5/8 15:08
 * Version: V1.0.0
 */
$(function() {
    getTreeData();

    onSubmit();
})

function getTreeData() {
    $.ajax({
        type: "get",
        url: "/auth/findPerms",
        success: function (data) {
            if (data !=null) {
                initTree(data);
            } else {
                layer.alert(data);
            }
        },
        error: function () {
            layer.alert("获取数据错误，请您稍后再试");
        }
    });
}

function initTree(data){
    // zTree 的参数配置
    var settingOther = {
        check: {
            enable: true,
            chkboxType:{ "Y":"p", "N":"s"}
        },
        data: {
            simpleData: {
                enable: true
            }
        }
    };
    $.fn.zTree.init($("#treePerm"), settingOther, data);

}

function onSubmit(){
    layui.use(['form' ,'tree','layer'], function(){
        var form = layui.form;
        var layer=layui.layer;

        form.on('submit(roleSubmit)', function(data){
            //获取选中的权限
            var treeObj = $.fn.zTree.getZTreeObj("treePerm");
            var nodes = treeObj.getCheckedNodes(true);
            //选中的复选框
            var nodeIds =new Array();
            for (var i = 0; i < nodes.length; i++) {
                nodeIds.push(nodes[i].id);
            }
            //校验是否授权
            var permIds = nodeIds.join(",");
            // console.log("permIds"+permIds)
            if(permIds==null || permIds==''){
                layer.alert("请给该角色添加权限菜单！")
                return false;
            }
            $("#permIds").val(permIds);

            $.ajax({
                type: "POST",
                data: $("#roleForm").serialize(),
                url: "/auth/addRole",
                success: function (data) {
                    if (data == "ok") {
                        layer.alert("操作成功",function(){
                            layer.closeAll();
                            load();
                        });
                    } else {
                        layer.alert(data);
                    }
                },
                error: function (data) {
                    layer.alert("操作请求错误，请您稍后再试");
                }
            });
            return false;
        });
        form.render();
    });
}