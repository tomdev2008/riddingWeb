var userList=new Array();
var sourceType=1;
/**
 * 点击邀请
 */
$("#invite").click(function(){
	$("#searchdiv").toggle("slow");
});

$("#searchbar").click(function(){
	$(this).val('');
});
/**
 * 点击完成
 */
$("#finishSearch").click(function(){
	if(riddingId!=0){
		$("#userList").empty();
	    dwr.engine._execute(cfg_host+"/ridding", 'RiddingPubBean', 'riddingUserList', riddingId ,loadUserCB);
	}
    $("#searchdiv").toggle("slow");
    
});

/**
 * 点击搜索
 */
$("#searchFriend").click(function(){
	if($("#searchbar").val()==''){
		return;
	}
	if($("#searchList li")){
		$("#searchList li").remove();
	}
	
	WB2.anyWhere(function(W){
			W.parseCMD("/search/suggestions/at_users.json?access_token="+userAccessToken, searchResult,{q:$("#searchbar").val(),range:0},{method:'get'});
    });
});

function searchhuiche(_keycode,_opt){
	if(_keycode==13){
		$("#searchFriend").click();
	}
};

/**
 * 返回结果回调
 */
function searchResult(_sResult,_bStatus){
	if(_bStatus){
		for(i=0;i<_sResult.length;i++){
			__html='<li id="'+_sResult[i].uid+'" style="height:50px;padding:12px 30px;border-bottom: 1px solid #E0E0E0;"><img src="'+cfg_host+'/image/duser.png" style="float:left"/><div style="float: left; width: 250px; padding: 15px; height: 20px;"><span>'+_sResult[i].nickname+'</span><input type="checkbox" style="float: right; width: 20px; height: 20px;" class="js_friendCheck"  /></div></li>';
			WB2.anyWhere(function(W){
	           W.parseCMD("/users/show.json?access_token="+userAccessToken, setAvator,{uid:_sResult[i].uid},{method:'get'});
            });
            $("#searchList").append(__html);
		}
	}
};
function setAvator(_sResult,_bStatus){
	if(_bStatus){
		$("#"+_sResult.id+" img").attr('src',_sResult.profile_image_url);
		$("#"+_sResult.id+" img").attr('alt',_sResult.screen_name);
		$("#"+_sResult.id).attr('data_name',_sResult.screen_name);
		$("#"+_sResult.id).attr('data_surl',_sResult.profile_image_url);
		$("#"+_sResult.id).attr('data_burl',_sResult.avatar_large);
	}
}
$(".js_friendCheck").live("click",function(){
	    __user={
			accessUserId:$(this).parent().parent().attr('id'),
			userName:$(this).parent().parent().attr('data_name'),
			nickName:$(this).parent().parent().attr('data_name'),
			sAvatorUrl:$(this).parent().parent().attr('data_surl'),
			bAvatorUrl:$(this).parent().parent().attr('data_burl'),
			userId:0
		}
	if(riddingId==0){ //如果是新建
		if($(this).attr('checked')){
			if($("#user_"+__user.accessUserId+"_"+sourceType).length==0){
				pushUser(__user);
				userList.push(__user);
			}
	  }else{
		if(userList){
			var temp=new Array();
	        var j=0;
			for(i=0;i<userList.length;i++){
				if(userList[i].accessUserId==$(this).parent().parent().attr('id')){
					$("#user_"+userList[i].accessUserId+"_"+sourceType).remove();
					continue;
					
				}
				temp[j++]=userList[i];
			}
			userList=temp;
		}
	  }
	}else{
		if($(this).attr('checked')){
			__users=new Array();
			__users.push(__user);
			dwr.engine._execute(cfg_host+"/ridding", 'RiddingBean', 'addUser', riddingId,__users, sourceType ,deleteUserCB);
		}else{
			//如果不是新建
		    dwr.engine._execute(cfg_host+"/ridding", 'RiddingBean', 'deleteUser', riddingId,$(this).parent().parent().attr('id'),sourceType ,deleteUserCB);
		}	
	}
});
/**
 * 删除用户回调函数
 */
function deleteUserCB(_flag){
	
}

/**
 * 插入用户
 */
function pushUser(_user){
	$("#userList").append("<li dataaccessUserId='"+_user.accessUserId+"' datauserId='"+_user.userId+"' class='js-inviteuser' style='height: 70px;width: 50px;margin: 10px; float: left;overflow: hidden;' id='user_"+_user.accessUserId+"_"+sourceType+"'><img src='"+_user.sAvatorUrl+"'  alt="+_user.userName+"/><p style='margin-top: -5px; text-align: center;'>"+_user.userName+"</p><span  class='js-userremove' style='position: relative; width: 5px; left: 42px; height: 5px; top: -73px;;display:none;color:#2E75BC;cursor: pointer;'>x</span></li>");
};
/**
 * 读取用户信息
 */
function loadUser(){
	dwr.engine._execute(cfg_host+"/ridding", 'RiddingPubBean', 'riddingUserList', riddingId ,loadUserCB);
};

function loadUserCB(_list){
	if(_list){
		for(i=0;i<_list.length;i++){
			__user={
				userId:_list[i].userId,
				sAvatorUrl:_list[i].sAvatorUrl==''?cfg_host+"/image/duser.png":_list[i].sAvatorUrl,
				userName:_list[i].nickName,
				nickName:_list[i].nickName,
				accessUserId:0
			}
			pushUser(__user);
			userList.push(__user);
		}
		
	}
};
/**
 * 点击删除
 */
$(".js-userremove").live("click",function(){
	if(riddingId>0){
		
		dwr.engine._execute(cfg_host+"/ridding", 'RiddingBean', 'deleteUserByUserId', riddingId,$(this).parent().attr('datauserid') ,deleteUserCB);
		$(this).parent().remove();
	}else{
		if(userList){
			var temp=new Array();
	        var j=0;
			for(i=0;i<userList.length;i++){
				if(userList[i].accessUserId==$(this).parent().attr('dataaccessuserid')){
					continue;
				}
				temp[j++]=userList[i];
			}
			userList=temp;
		}
	}
	$(this).parent().remove();	
});
/**
 * hover到用户上
 */
$(".js-inviteuser").live("hover",function(e){
	 $(".js-userremove").hide();
	 $(this).children(".js-userremove").show();
});

	
