function showMessage(msg,timeOut) {
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