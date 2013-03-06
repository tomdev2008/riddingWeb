
$("#recom").click(function(){
	_riddingId= $("#recom_riddingId").val();
	_firstPicUrl=$("#recom_firstpicurl").val();
	_linkText=$("#recom_ad_text").val();
	_linkImageUrl=$("#recom_ad_image_url").val();
	_linkUrl=$("#recom_link_url").val();
	_weight=$("#recom_weight").val();
	dwr.engine._execute(cfg_host+"/ridding", 'BackendBean', 'addPublicRecom',_riddingId,_weight,_firstPicUrl,_linkText,_linkImageUrl,_linkUrl,submitWeiBoCB);
});

$("#recom_pic").click(function(){
	_riddingId= $("#recom_pic_riddingId").val();
	_text=$("#recom_pic_ad_text").val();
	_imageurl=$("#recom_pic_ad_image_url").val();
	_takepicdate=$("#recom_pic_ad_time").val();
	_location=$("#recom_pic_ad_location").val();
	dwr.engine._execute(cfg_host+"/ridding", 'BackendBean', 'addRiddingPicture',_riddingId,_imageurl,_text,_takepicdate,_location,0,submitWeiBoCB);
});


$(".recom_img_recom").click(function(){
	_id= $(this).attr("data-id");
	_picUrl=$(this).attr("data-url");
	dwr.engine._execute(cfg_host+"/ridding", 'BackendBean', 'updatePublicFirstPicUrl',_id,_picUrl,submitWeiBoCB);
});


$("#recom_pic_mianbao").click(function(){
	_riddingId= $("#recom_pic_mianbao_riddingId").val();
	_url=$("#recom_pic_mianbao_url_text").val();
	dwr.engine._execute(cfg_host+"/ridding", 'BackendBean', 'getRiddingPictureFromMianBao',_riddingId,_url,submitWeiBoCB);
});



function submitWeiBoCB(_flag){
	if(_flag){
		alert("操作成功");
		location.href=location.href;
	}
};