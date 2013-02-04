
$("#recom").click(function(){
	_riddingId= $("#recom_riddingId").val();
	_firstPicUrl=$("#recom_firstpicurl").val();
	_weight=$("#recom_weight").val();
	dwr.engine._execute(cfg_host+"/ridding", 'BackendBean', 'addPublicRecom',_riddingId,_weight,_firstPicUrl,submitWeiBoCB);
});


$(".recom_img_recom").click(function(){
	_id= $(this).attr("data-id");
	_picUrl=$(this).attr("data-url");
	dwr.engine._execute(cfg_host+"/ridding", 'BackendBean', 'updatePublicFirstPicUrl',_id,_picUrl,submitWeiBoCB);
});



function submitWeiBoCB(_flag){
	if(_flag){
		alert("操作成功");
		location.href=location.href;
	}
};