$("#codeSearch").click(function(){
_code=$("#taobaoCode").val();

if(_code==''){
alert('请输入淘宝Code');
}else{
	dwr.engine._execute(cfg_host+"/ridding", 'UserPayBackendBean', 'getProfileByTaobaoCode',_code,CBCodeSearch);
}
});

function CBCodeSearch(_profileTaobaoVO){
	if(_profileTaobaoVO){
		$("#searchResult_userId").html(_profileTaobaoVO.profile.userId);
		$("#searchResult_userName").html(_profileTaobaoVO.profile.userName);
		for(i=0;i<_profileTaobaoVO.userPays.length;i++){
			_userPay=_profileTaobaoVO.userPays[i];
			_typeName="";
			if(_userPay.typeId==1){
				_typeName="天气服务";
			}
			$("#searchResult_tbody").empty();
			$("#searchResult_tbody").append("<tr><td>"+_userPay.id+"</td><td>"+_typeName+"</td><td>"+_userPay.beginTimeStr+"</td><td>"+_userPay.endTimeStr+"</td><td><span>增加日期:</span><input type=\"text\"/><input type=\"button\" value=\"增加\"/></td></tr>");
		}
	}
};

$("#add_btn").click(function(){
_code=$("#add_code").val();
_type=$("#add_type").val();
_long=$("#add_long").val();

if(_code==''){
alert('请输入淘宝Code');
}else if(_type==''){
alert('请输入升级Type');
}else if(_long==''){
alert('请输入升级时间长度');
}else{
	dwr.engine._execute(cfg_host+"/ridding", 'UserPayBackendBean', 'addUserPay',_code,_type,_long,CBadd_btn);
}
});


function CBCodeSearch(_flag){
	if(_flag){
		alert('成功');
	}
};
