function showPrompt(msg, timeOut) {
    $.messager.show({
        title: '提示',
        msg: msg,
        timeout: timeOut,
        showType: 'slide',
        style: {
            right:'',
            top:document.body.scrollTop+document.documentElement.scrollTop,
            bottom:'',
            textAlign: 'center'
        }
    });
    $('.panel-tool').hide();
    $('.panel-body').css('border-style','none');
    $('.panel-body').css('background-image','linear-gradient(120deg, #a1c4fd 0%, #c2e9fb 100%)');
    $('.panel-htop').css('background-image','linear-gradient(120deg, #a1c4fd 0%, #c2e9fb 100%)');
}
function showError(msg, timeOut) {
    $.messager.show({
        title: '错误',
        msg: msg,
        timeout: timeOut,
        showType: 'slide',
        style: {
            right:'',
            top:document.body.scrollTop+document.documentElement.scrollTop,
            bottom:'',
            textAlign: 'center'
        }
    });
    $('.panel-tool').hide();
    $('.panel-body').css('border-style','none');
    $('.panel-body').css('background-image','linear-gradient(120deg, #a1c4fd 0%, #c2e9fb 100%)');
    $('.panel-htop').css('background-image','linear-gradient(120deg, #a1c4fd 0%, #c2e9fb 100%)');
}
$.extend($.fn.textbox.defaults.rules, {
    numberAndLetter: {
        validator: function(value, param) {
            return /^[0-9a-zA-Z]*$/.test(value);
        },
        message: '只能输入数字和字母'
    },
});