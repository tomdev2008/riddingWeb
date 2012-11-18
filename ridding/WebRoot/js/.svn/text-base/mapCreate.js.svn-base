//说明可以拖拽
var selfLocation =new Array();
var points;
var cityname;
var distance;
var directionsDisplay;
var directionsService;
var map;
var geocoder;
var nowPosition;
var canCreate=false;
var overlay;
var _html=
   '<ul id="addPointDiv" class="map-right-ul"><li><a  href="javascript:void(0);" onClick="pointadd(\'begin\');return;"><span>设为起点</span></a></li>\
	   <li ><a href="javascript:void(0);" onClick="pointadd(\'end\');return;" ><span>设为终点</span></a></li>\
	   <li ><a href="javascript:void(0);" onClick="pointadd(\'mid\');return;"><span>添加路过</span></a></li></ul>';

var currentLatLng={
	name:'',
	latlng:''
};
var markers=new Array();
/**
 *初始化已有骑行活动 
 */
function riddingInit(){
	$("#beginPosition").attr("value",beginLocation);
	$("#endPosition").attr("value",endLocation);
	$("#hdname").attr("value",riddingName);
	__count=1;
	for(i=0;i<locations.length;i=i+2){
		__latitude=locations[i];
		__longtitdue=locations[i+1];
		if(i==0){
			$("#beginPosition").attr("latitude",__latitude);
			$("#beginPosition").attr("longtitude",__longtitdue);
		}else if(i==locations.length-2){
			$("#endPosition").attr("latitude",__latitude);
			$("#endPosition").attr("longtitude",__longtitdue);
		}else{
			$("#mid-path").append('<li><span></span><input value="" type="text" dataname="'+'mid'+__count+'" id="'+'mid'+__count+'" class="position"/><a href="javascript:void(0);" class="js_midDel" style="padding-left:5px;">x</a></li>');
		    $("#mid-path li:last .position").attr("value",midLocations[__count-1]);
		    $("#mid-path li:last .position").attr("latitude",__latitude);
		    $("#mid-path li:last .position").attr("longtitude",__longtitdue);
		    __count++;
		}
	}
	loadUser();
};

/**
 * 初始化函数
 */
function initialize() {
	if(riddingId!=0){
		riddingInit();
	}
	geocoder = new google.maps.Geocoder();
	var rendererOptions= {
          draggable: true,
          map:map
      };
    directionsDisplay = new google.maps.DirectionsRenderer(rendererOptions);
    directionsService= new google.maps.DirectionsService();
    if (navigator.geolocation) {
         navigator.geolocation.getCurrentPosition(showMap);
    } else {
         document.getElementById("map_canvas").innerHTML = '<div id="help">google地图需要使用html5浏览器,比如<a target="_blank" href="http://www.firefox.com.cn/download/">火狐浏览器</a>、<a target="_blank" href="http://www.google.cn/intl/zh-CN/chrome/browser/">chrome浏览器</a>、<a target="_blank" href="http://info.msn.com.cn/ie9/">ie9浏览器</a></div>';
    }
    //监听路线改变
    google.maps.event.addListener(directionsDisplay, 'directions_changed', function() {
      computeTotalDistance(directionsDisplay.directions);
    }); 
};

/**
 * 显示地图
 */
function showMap(_position) {
	  nowPosition = new google.maps.LatLng(_position.coords.latitude, _position.coords.longitude);
      var options = {
        zoom: 9,
        center: nowPosition,
        mapTypeId: google.maps.MapTypeId.ROADMAP
      };
      map = new google.maps.Map(document.getElementById("map_canvas"), options);
      var marker = new google.maps.Marker({
         position: nowPosition,
         title:"当前位置"
      });
      marker.setMap(map);
      
      
      directionsDisplay.setMap(map);
	  /*增加层的方式*/
       addOverLay(map);
       //如果是编辑活动
       if(riddingId>0){
       	$("#genMap").click();
       	canCreate=false;
       }
};
/**
 * 添加标注
 */
function addMarker(_location,_image) {
    marker = new google.maps.Marker({
      position: _location,
      draggable:false,
      animation: google.maps.Animation.DROP,
      map: map,
      icon: _image
    });
    //google.maps.event.addListener(marker, 'click', toggleBounce);
    return marker;
};
/**
 * 让标记动起来
 */
function toggleBounce() {
  if (marker.getAnimation() != null) {
    marker.setAnimation(null);
  } else {
    marker.setAnimation(google.maps.Animation.BOUNCE);
    setTimeout(marker.setAnimation(null),5000);
  }
};


/**
 * 添加浮层
 */
function addOverLay(_map){
	var contextmenu = document.createElement("div"); 
	contextmenu.innerHTML = _html;
	contextmenu.index = 1;
	contextmenu.style.display = "none";
	_map.controls[google.maps.ControlPosition.TOP_LEFT].push(contextmenu);
    google.maps.event.addListener(map, 'rightclick', function(event) {  
           currentLatLng.latlng = event.latLng;  
           contextmenu.style.position="relative";
	       contextmenu.style.left=event.pixel.x+"px";	//平移显示以对应右键点击坐标
	       contextmenu.style.top=event.pixel.y+"px";
	       contextmenu.style.display = "block";
    });
    
    google.maps.event.addListener(map, 'click', function(event){
    	 contextmenu.style.display = "none";
    });
    
    google.maps.event.addListener(map, 'drag', function(event){
    	 contextmenu.style.display = "none";
    });
    
    google.maps.event.addDomListener(contextmenu, 'click', function(event) { 
          contextmenu.style.display = "none";
    });
    
};


/**
 * 创建起点
 */
function pointadd(_type){
	if(_type=='begin'){
		clearMarker($("#beginPosition").attr('dataname'));
	}else if(_type=='end'){
		clearMarker($("#endPosition").attr("dataname"));
	}
	 url=''
	 marker=undefined;
	 _opt=undefined;
	 switch(_type){
	 	case 'begin':
	 	url=cfg_host+'/image/mapCreate/startPoint.png';
	 	marker=addMarker(currentLatLng.latlng,url);
	 	_opt=$("#beginPosition");
	 	break;
	 	case 'end':
	 	url=cfg_host+'/image/mapCreate/endPoint.png';
	 	marker=addMarker(currentLatLng.latlng,url);
	 	_opt=$("#endPosition");
	 	break;
	 	case 'mid':
	 	url='';//cfg_host+'/img/mapCreate/startPoint.png';
	 	marker=addMarker(currentLatLng.latlng,url);
	 	_opt=getANewMid();
	 	break;
	 }
	 _opt.attr("latitude",currentLatLng.latlng.Ya);
	 _opt.attr("longtitude",currentLatLng.latlng.Za);  //得到经纬度
	 type=_opt.attr("dataname");
	 setPoint(currentLatLng.latlng,_opt,marker,type);
};
/**
 * 通过dataname删除某个marker
 */
function clearMarker(_opt){
	if (markers) {
		temp=new Array();
	    var j=0;
        for (i=0;i<markers.length; i++) {
       	   if(markers[i].type==_opt){
       	     	markers[i].marker.setMap(null);
       	     	continue;
       	   }
       	   temp[j++]=markers[i];
       }
       markers=temp;
	 }
}
/**
 * 得到一个新的中间节点
 */
function getANewMid(){
	var count=$("#mid-path li").length+1;
	$("#mid-path").append('<li style="margin-top:5px;"><span></span><input editable="false" value="" type="text" dataname="'+'mid'+count+'" id="'+'mid'+count+'" class="position"/><a href="javascript:void(0);" class="js_midDel" style="padding-left:5px;">x</a></li>');
	$("#mid-path li:last").hide();
	return $("#"+'mid'+count+"");
}

/**
 * 左侧栏添加数据
 */
function setPoint(_latlng,_opt,_marker,_type){
	geocoder.geocode({'latLng': _latlng},function(results, status){
      if (status == google.maps.GeocoderStatus.OK) {
        if (results[0]) {
	        _opt.attr('value',results[0].formatted_address);
	        $("#mid-path li:last").show();
	        if(_marker){
	        	_marker.title=results[0].formatted_address;
	        	pushMarker={
	 	          marker:marker,
	 	          type:_type
	            }
	            markers.push(pushMarker); 
	        }   
        }
      } else {
        setTimeout(function(){setPoint(_latlng,_opt,_marker,_type);},1000);
        if(_marker){
        	_marker.setMap(null);
        }
      }
    });
};

/**
 * 计算路线，并画出
 */
function calRoute(_start,_end,_waypoints) {
    var request = {
         origin:_start,
         destination:_end,
         waypoints:_waypoints,
         provideRouteAlternatives: false,
         travelMode: google.maps.DirectionsTravelMode.WALKING
      };
    directionsService.route(request, function(result, status) {
      if (status == google.maps.DirectionsStatus.OK) {
        directionsDisplay.setDirections(result);
      }
    });
};
/**
 * 计算出路线的总距离
 */
function computeTotalDistance(result) { 
  var total = 0;
  var myroute = result.routes[0];
  for (i = 0; i < myroute.legs.length; i++) {
    total += myroute.legs[i].distance.value;
  }
  points=myroute.overview_polyline.points;
  distance=total;
  var index=0;
  beginLocation=myroute.legs[0].start_location;
  endLocation=myroute.legs[0].end_location;
  selfLocation[index++]={latitude:beginLocation.Ya,longtitude:beginLocation.Za};
  
  bb=result.ub||result.vb;
  waypoints=bb.waypoints||bb.waypoints;
  if(waypoints&&waypoints.length>0){
  	$("#mid-path").empty();
    for(i=0;i<waypoints.length;i++){
      selfLocation[index++]={latitude:waypoints[i].location.Ya,longtitude:waypoints[i].location.Za};
      count=$("#mid-path li").length+1;
      getANewMid();
      $("#mid-path li:last .position").attr("latitude",waypoints[i].location.Ya);
	  $("#mid-path li:last .position").attr("longtitude",waypoints[i].location.Za);  //得到经纬度
	  currentLatLng.latlng=new google.maps.LatLng(waypoints[i].location.Ya, waypoints[i].location.Za);
	  setPoint(currentLatLng.latlng,$("#"+'mid'+count+""));
	  $("#mid-path li:last").show();
    }
  }
  selfLocation[index++]={latitude:endLocation.Ya,longtitude:endLocation.Za};
  geocoder.geocode( { 'address': $("#beginPosition").val()}, function(results, status) {
      if (status == google.maps.GeocoderStatus.OK) {
         	for(i=0;i<results[0].address_components.length;i++){
         		if(results[0].address_components[i].types[0]=='locality'){
         			cityname=results[0].address_components[i].long_name;
         			break;
         		}
         	}
      } else {
      }
    });
};

$(".js_midDel").live("click",function(){
	$(this).parent().remove();
});

$(".position").live("keydown",function(){
	$($(this).parent()).children('.js-check').show();
});

$(".js-check").live("click",function(){
	__value=$($($(this).parent()).children('.position')).val()
	__opt=$(this);
	geocoder.geocode( { 'address': __value}, function(results, status) {
    if (status == google.maps.GeocoderStatus.OK) {
    	currentLatLng.latlng=results[0].geometry.location;
        pointadd(__opt.attr('data'));
        map.setCenter(currentLatLng.latlng);
    } 
    });
});

/**
 * 添加中点
 */
$("#addMidPosition").click(function(){
	getANewMid();
	$("#mid-path li:last").show();
});
/**
 * 设置当前位置为起点
 */
$("#nowPositionb").click(function(){
	currentLatLng.latlng=nowPosition;
	pointadd('begin');
});

$("#nowPositione").click(function(){
	currentLatLng.latlng=nowPosition;
	pointadd('end');
});

/**
 * 点击生成地图
 */
$("#genMap").click(function(){
	if(!$("#beginPosition").attr('latitude')||!$("#endPosition").attr('latitude')){
		return;
	}
	__begin = new google.maps.LatLng($("#beginPosition").attr('latitude'), $("#beginPosition").attr('longtitude'));
	__end= new google.maps.LatLng($("#endPosition").attr('latitude'), $("#endPosition").attr('longtitude'));
	__waypoints=new Array();
	if($("#mid-path li")){
		for(i=0;i<$("#mid-path li").length;i++){
			__position=$("#mid-path li:eq("+i+") .position");
			  if(__position&&__position.attr('latitude')>0&&__position.attr('longtitude')>0){
				 waypoint={
                 location:new google.maps.LatLng(__position.attr('latitude'), __position.attr('longtitude')),
                 stopover:false
                 }
              __waypoints.push(waypoint);
			  }else{
                 $("#mid-path li:eq("+i+") ").remove();
              }
		}
	}
	calRoute(__begin,__end,__waypoints);
	canCreate=true;
});
/**
 * 提交活动
 */
$("#submitMap").click(function(){
	if(!canCreate){
		alert('请再次点击生成按钮，以初始化数据!');
		return;
	}
	__name=$("#hdname").val();
	__beginLocation=$("#beginPosition").val();
	__endLocation=$("#endPosition").val();
	__midLocation=new Array();
	if($("#mid-path li")){
		for(i=0;i<$("#mid-path li").length;i++){
			__midLocation.push($("#mid-path li:eq("+i+") .position").val());
		}
	}
	if(__name==''||selfLocation==''||__beginLocation==''){
		alert('信息不全!');
		return;
	}
	if(riddingId==0){
		//创建活动
		dwr.engine._execute(cfg_host+"/ridding", 'RiddingBean', 'createRidding', selfLocation,__name,__beginLocation,distance,points,cityname,__endLocation,__midLocation,function(flag){callback(flag);});
	}else{
		//更新活动
		dwr.engine._execute(cfg_host+"/ridding", 'RiddingBean', 'updateRidding', selfLocation,__name,__beginLocation,distance,points,cityname,__endLocation,__midLocation,riddingId,function(flag){callback(flag);});
	}
	
});
/**
 * 回调函数 
 */
function callback(_riddingId){
	if(_riddingId<=0){
		alert('操作失败');
		location.href=cfg_host+"/user/"+UD.visitUserId+"/ridding/list/";
	}else{
		alert('创建成功');
		if(riddingId==0){
			uploadUser(_riddingId);
		}
		
		
	}
};

function callbackadd(_flag){
	location.href=cfg_host+"/user/"+UD.visitUserId+"/ridding/list/";
};
function uploadUser(_riddingId){
	dwr.engine._execute(cfg_host+"/ridding", 'RiddingBean', 'addUser', _riddingId,userList, sourceType ,function(flag){callbackadd(flag);});
};

/**
 * 键盘操作
 */
function huiche(_keycode,_opt){
	if(_keycode==13){
		__value=$($($(_opt).parent()).children('.position')).val()
	    geocoder.geocode( { 'address': __value}, function(results, status) {
            if (status == google.maps.GeocoderStatus.OK) {
    	        currentLatLng.latlng=results[0].geometry.location;
                pointadd($(_opt).attr('data'));
                map.setCenter(currentLatLng.latlng);
            } 
        });
	}
};





/**
 * 初始化函数
 */
new initialize();