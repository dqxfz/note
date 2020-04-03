function cursorInit() {
    $.fn.extend({
        insertAtCaret: function(myValue){
            var $t=$(this)[0];
            if (document.selection) {
                this.focus();
                sel = document.selection.createRange();
                sel.text = myValue;
                this.focus();
            }
            else
            if ($t.selectionStart || $t.selectionStart == '0') {
                var startPos = $t.selectionStart;
                var endPos = $t.selectionEnd;
                var scrollTop = $t.scrollTop;
                $t.value = $t.value.substring(0, startPos) + myValue + $t.value.substring(endPos, $t.value.length);
                this.focus();
                $t.selectionStart = startPos + myValue.length;
                $t.selectionEnd = startPos + myValue.length;
                $t.scrollTop = scrollTop;
            }
            else {
                this.value += myValue;
                this.focus();
            }
        }
    });
}
function initPortfolio() {
    $(portfolio).tree({
        url: '/portfolio?fatherId=wy',
        method: 'get',
        onContextMenu: function(e, node){
            displayContent(node);
            e.preventDefault();
            $(portfolio).tree('select', node.target);
            if(node.father_id == 0) {
                $('#rename').hide();
                $('#remove').hide();
            } else {
                $('#rename').show();
                $('#remove').show();
            }
            if(node.catalog_type != 0) {
                $('#add').hide();
                $('#upload').hide();
                $('#download').show();
            } else {
                $('#add').show();
                $('#upload').show();
                $('#download').hide();
            }
            $('#mm').menu('show', {
                left: e.pageX,
                top: e.pageY
            });
        },
        onBeforeEdit: function(node) {
            node_text = node.text;
        },
        onAfterEdit: function(node) {
            if(node.id == "0") {
                $.ajax({
                    url: "/catalog/addFolder",
                    data: {"father_id":node.father_id,"catalog_name":node.text,"catalog_type":node.catalog_type},
                    success: function (obj) {
                        if(!obj.success){
                            tree.tree('remove',node.target);
                            alert(obj.data);
                        } else {
                            node.id = obj.data;
                        }
                    }
                });
            } else {
                $.ajax({
                    url: "/catalog/renameFolder",
                    data: {"catalog_id": node.id, "catalog_name": node.text},
                    success: function (obj) {
                        if(!obj.success) {
                            alert(obj.data);
                            $('#tt').tree('update', {
                                target: node.target,
                                text: node_text
                            });
                        } else {
                            if(node.catalog_type != 0) {
                                $('#note_title').text(node.text)
                            }
                        }
                    }
                });
            }
        },
        onClick: function(node){
            // if(node.type == 'FOLDER') {
            //     queryChildren(node);
            // } else {
            //     // displayContent(node);
            // }
        },
        onExpand: function (node) {
            queryChildren(node);
        },
        onCollapse: function (node) {
            removeChildren(node);
        }
    })
}
function queryChildren(node){
    $.ajax({
        url: "/portfolio?" + node.id,
        dataType: "json",
        success: function (data) {
            if(data) {
                $(portfolio).tree('append',{
                    parent: node.target,
                    data: data
                });
            }
        }
    });
}
function removeChildren(node) {

}
function displayContent(node) {
    if(node.catalog_type != 0) {
        $('#model_view').show();
        $('#model_edit').hide();
        if(node.id == current_file) {
            return;
        }
        current_file = node.id;
        $.ajax({
            url: "/catalog/getNoteContent",
            data: {"catalog_id": node.id},
            success: function (obj) {
                if (!obj.success) {
                    alert(obj.data);
                } else {
                    $('#note_title').text(node.text);
                    if(node.catalog_type == 3) {
                        $('#noteContent').val("此文件暂不支持预览");
                    } else {
                        $('#noteContent').val(obj.data);
                    }
                    convert();
                }
            }
        });
    }
}
function paseImg()
{
    document.getElementById('noteContent').addEventListener("paste",function(e){
        var clipboardData = window.clipboardData || e.clipboardData, items, item;
        if (clipboardData) {
            items = clipboardData.items;
            if (!items) {
                return;
            }
            item = items[0];
            if (item && item.kind === 'file' && item.type.match(/^image\//i)) {
                var blob = item.getAsFile(),
                    reader = new FileReader();
                reader.onloadend = function (e) {
                    uploadImage(e.target.result);
                };
                reader.readAsDataURL(blob);
            }
        }
    });
};

function uploadImage(base64) {
    var data = {
        base64 : base64
    };
    $.ajax({
        type : "POST",
        url : "/catalog/uploadImage",
        data : data,
        success : function(obj) {
            if(obj.success) {
                $('#noteContent').insertAtCaret(obj.data);
            } else {
                alert(obj.data);
            }
        },
        error : function() {
            alert("由于网络原因，上传失败。");
        }
    });
}
function menuHandler(item){
    var tree = $(portfolio);
    var node = tree.tree('getSelected');

    switch(item.name) {
        case 'folder': {
            tree.tree('append', {
                parent: node.target,
                data: [{
                    id: 0,
                    state: 'closed',
                    text: '新文件夹',
                    father_id: node.id,
                    catalog_type: 0
                }]
            });
            var node02 = tree.tree('find',0);
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
                    father_id: node.id,
                    catalog_type: 1
                }]
            });
            var node02 = tree.tree('find',0);
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
                data: {"father_id": node.father_id,"catalog_id": node.id},
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
            window.open("/catalog/downloadFile?catalog_id=" +  node.id);
            // $.ajax({
            //     url: "/catalog/getNoteContent",
            //     data: {"catalog_id": node.id},
            //     success: function (obj) {
            //         if (!obj.success) {
            //             alert(obj.data);
            //         } else {
            //             window.open(obj.data);
            //         }
            //     }
            // });
            // $.ajax({
            //     url: "/catalog/downloadFile",
            //     data: {"catalog_id": node.id},
            //     success: function (obj) {
            //
            //     }
            // });
            break;
        }
    }
};
function convert(){
    var text = $("#noteContent").val();
    var converter = new showdown.Converter();
    var html = converter.makeHtml(text);
    $('#noteDisplay').html(html);
    hljs.highlightBlock(document.getElementById("noteDisplay"));
    $('#model_view').html(html);
    hljs.highlightBlock(document.getElementById("model_view"));
}
function buttonHandler(btn) {
    var node = $(portfolio).tree('getSelected');
    if(node && node.catalog_type != 0) {
        var content = $('#noteContent').val();
        switch (btn) {
            case "save": {
                $.ajax({
                    url: "/catalog/modifyContent",
                    data: {catalog_id: node.id, catalog_content: content},
                    success: function (obj) {

                    }
                });
                break;
            }
            case "edit": {
                if(node.catalog_type != 3 && node.catalog_type != 2) {
                    $('#model_edit').show();
                    $('#model_view').hide();
                } else {
                    $('#model_edit').hide();
                    $('#model_view').show();
                }
                break;
            }
            case "more": {
                alert("more");
                break;
            }
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
            var node = obj.data;
            if(obj.success) {
                var parent = $(portfolio).tree('find',node.father_id);
                $(portfolio).tree('append', {
                    parent: parent.target,
                    data: [{
                        id: node.catalog_id,
                        state: node.catalog_type,
                        text: node.catalog_name,
                        father_id: node.father_id,
                        catalog_type: node.catalog_type
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