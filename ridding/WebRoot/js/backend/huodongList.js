$(".huodong_delete").click(function(){
	_riddingId= $(this).attr("data-id");
	dwr.engine._execute(cfg_host+"/ridding", 'BackendBean', 'deleteRiddingById',_riddingId,submitCB);
});

function submitCB(_flag){
	
};

$("#search").click(function(){
    _userid=$("#search_userid").val();
    location.href=cfg_host+"/ridding/backend.do?action=huodongList&userid="+_userid;
});