/**
 * 鼠标移上去的浮层
 */
$(".js-riddingLink").hover(
function(){
	$(".js-cover-"+$(this).attr('dataid')).css('opacity',0.7);
	$(".js-cover-"+$(this).attr('dataid')).css('-moz-opacity',0.7);
	$(".js-cover-"+$(this).attr('dataid')).css('filter','alpha(opacity=70)');
},
function(){
	$(".js-cover-"+$(this).attr('dataid')).css('opacity',0);
		$(".js-cover-"+$(this).attr('dataid')).css('-moz-opacity',0);
	$(".js-cover-"+$(this).attr('dataid')).css('filter','alpha(opacity=0)');
});

$(".js-riddingLink").click(function(){
	location.href=cfg_host+'/user/'+UD.hostUserId+'/ridding/'+$(this).attr('dataid')+'/'
});

$(".js-riddingCollectLink").click(function(){
	__riddingId= $(this).attr('dataid');
	dwr.engine._execute(cfg_host+"/ridding", 'RiddingShareBean', 'useOthersRidding', __riddingId ,useOthersRiddingCB);
   
});

function useOthersRiddingCB(_flag){
	if(_flag){
		alert('收藏成功');
	}
}