var _photoUrls=new Array()

$("#recom").click(function(){
	_riddingId= $("#recom_riddingId").val();
	_userId=$("#recom_userId").val();
	_weight=$("#recom_weight").val();
	dwr.engine._execute(cfg_host+"/ridding", 'BackendBean', 'addPublicRecom',_riddingId,_userId,_weight,_recom_photoUrl,submitWeiBoCB);
});

function saveRecomReport(){
$("#recom_uploadFrame").ajaxSubmit(function(message) {
if(message!=''){
	
	$("#recom_coverimage").attr("src",cfg_imageHost+message);
_recom_photoUrl=message
$("#recom_coverimage").show();
}
});
return false;
};

function updateRecom(leaderUserId,riddingId){
	//还没做
};


function submitWeiBoCB(_flag){
	if(_flag){
		alert("操作成功");
		location.href=location.href;
	}
};