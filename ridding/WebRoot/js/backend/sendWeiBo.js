var _photoUrl;
/**
 * 异步发送action
 */
function saveReport(){
$("#uploadFrame").ajaxSubmit(function(message) {
if(message!=''){
	$("#image").attr("src",cfg_imageHost+message);
_photoUrl=message
$("#image").show();
}
});
return false;
};





$("#submitWeiBo").click(function(){
__text=$("#weiboText").val();
__sendTime=$("#weiboSendTime").val();
__sourceType=$("#weiboSelector").val();
__weiboType=$("#weiboTypeSelector").val();
__riddingId=0;
if(__weiboType==1){
	__riddingId=$("#mapId").val();	
}
if(_photoUrl==''){
		alert('请先上传图片');
}else if(__text==''){
alert('请输入微博内容');
}else if(__sendTime==''){
alert('请输入发送时间');
}else{
	dwr.engine._execute(cfg_host+"/ridding", 'BackendBean', 'updateWeiBo',__text,__sendTime,_photoUrl,__sourceType,__weiboType,__riddingId,submitWeiBoCB);

}
});

function submitWeiBoCB(_flag){
	if(_flag){
		alert("操作成功");
		location.href=location.href;
	}
};


$("#sendApns").click(function(){
	__text=$("#apnsValue").val();
	if(__text==''){
		alert('发送内容');
		return;
	}
	dwr.engine._execute(cfg_host+"/ridding", 'BackendBean', 'sendApns',__text,submitWeiBoCB);
});