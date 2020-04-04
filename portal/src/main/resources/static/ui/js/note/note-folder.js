
function changeMenuState(node) {
    if(node.fatherId == 'wy') {
        $('#rename').hide();
        $('#remove').hide();
    } else {
        $('#rename').show();
        $('#remove').show();
    }
    if(node.iconCls != folder) {
        $('#add').hide();
        $('#upload').hide();
        $('#download').show();
    } else {
        $('#add').show();
        $('#upload').show();
        $('#download').hide();
    }
}

function addPortfolio(node) {
    $.ajax({
        url: "/portfolio",
        method: 'post',
        data: {"fatherId":node.fatherId,"name":node.text,"iconCls":node.iconCls},
        dataType: 'json',
        success: function (obj) {
            node.id = obj.data;
        },
        error: function () {
            alert(errorMessage);
            $(portfolio).tree('remove',node.target);
        }
    });
}

function renamePortfolio(node) {
    $.ajax({
        url: "/portfolio",
        method: 'put',
        data: {"id":node.id,"name":node.text},
        success: function () {
            if(node.iconCls != folder) {
                $(noteTitle).text(node.text)
            }
        },
        error: function () {
            alert(errorMessage);
            $(portfolio).tree('update', {
                target: node.target,
                text: node_text
            });
        }
    });
}

function initPortfolio() {
    $(portfolio).tree({
        url: '/portfolio?id=wy',
        method: 'get',
        onContextMenu: function(e, node){
            displayContent(node);
            e.preventDefault();
            $(portfolio).tree('select', node.target);
            changeMenuState(node);
            $('#mm').menu('show', {
                left: e.pageX,
                top: e.pageY
            });
        },
        onBeforeEdit: function(node) {
            node_text = node.text;
        },
        // 完成编辑文件名时触发的事件
        onAfterEdit: function(node) {
            // 如果是新增节点（node.id为0，说明是新增节点），则调用添加方法，否则调用重命名方法
            if(node.id == 0) {
                addPortfolio(node);
            } else {
                renamePortfolio(node);
            }
        },
        onClick: function(node){
            displayContent(node);
        }
    }).tree('options').url = "/portfolio";
}

function menuHandler(item){
    let tree = $(portfolio);
    let node = tree.tree('getSelected');

    switch(item.name) {
        case 'folder': {
            tree.tree('append', {
                parent: node.target,
                data: [{
                    id: 0,
                    state: 'closed',
                    text: '新文件夹',
                    fatherId: node.id,
                    iconCls: folder
                }]
            });
            let node02 = tree.tree('find',0);
            tree.tree('expand',node.target);
            tree.tree('beginEdit',node02.target);
            break;
        }
        case 'markdown': {
            tree.tree('append', {
                parent: node.target,
                data: [{
                    id: 0,
                    state: 'open',
                    text: '新文件',
                    fatherId: node.id,
                    iconCls: markdown
                }]
            });
            let node02 = tree.tree('find',0);
            tree.tree('expand',node.target);
            tree.tree('beginEdit',node02.target);
            break;
        }
        case 'rename': {
            tree.tree('beginEdit',node.target);
            break;
        }
        case 'remove': {
            $.messager.progress({
                title: '提示',
                msg: '正在删除文件，请稍候……',
                text: ''
            });
            $.ajax({
                url: "/catalog/removeFolder",
                data: {"fatherId": node.fatherId,"catalog_id": node.id},
                success: function (obj) {
                    if(obj.success){
                        tree.tree('remove',node.target);
                    } else {
                        alert(obj.data);
                    }
                },
                complete: function () {
                    $.messager.progress('close');
                }
            });
            break;
        }
        case 'upload': {
            $('#father_id_text').val(node.id);
            $('#upload_dlg').dialog('open').dialog('setTitle','上传文件');
            break;
        }
        case 'download': {

            break;
        }
    }
};

function uploadFile() {
    $.messager.progress({
        title: '提示',
        msg: '文件上传中，请稍候……',
        text: ''
    });
    $.ajax({
        type: "post",
        url: "/catalog/uploadFile",
        data: new FormData($('#file_form')[0]),
        processData: false,
        contentType: false,
        success: function (obj) {
            let node = obj.data;
            if(obj.success) {
                let parent = $(portfolio).tree('find',node.fatherId);
                $(portfolio).tree('append', {
                    parent: parent.target,
                    data: [{
                        id: node.catalog_id,
                        state: node.iconCls,
                        text: node.catalog_name,
                        fatherId: node.fatherId,
                        iconCls: node.iconCls
                    }]
                });
                $('#upload_dlg').dialog('close');
            } else {
                alert(node);
            }
        },
        error: function () {
            alert("异常!");
        },
        complete: function () {
            $.messager.progress('close');
        }
    });
}