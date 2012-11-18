var rendererOptions= {
  draggable: false
};
var directionsDisplay;
var directionsService;
var map;
var geocoder;
/**
 * 初始化
 */
function initialize() {
	loadUser();
	geocoder = new google.maps.Geocoder();
    directionsDisplay = new google.maps.DirectionsRenderer(rendererOptions);
    directionsService= new google.maps.DirectionsService();
    if (navigator.geolocation) {
         showMap();
    } else {
         document.getElementById("map_canvas").innerHTML = '<div id="help">请使用html5浏览器</div>';
    }

};

/**
 * 点击生成地图
 */
function createRoute(){
	if(locations==''){
		return;
	}
	__begin = "";
	__end= ""
	__waypoints=new Array();
		for(i=0;i<locations.length;i=i+2){
			__latitude=locations[i];
			__longtitdue=locations[i+1];
			if(i==0){
				__begin=new google.maps.LatLng(__latitude,__longtitdue);
			}else if(i==locations.length-2){
				__end=new google.maps.LatLng(__latitude,__longtitdue);
			}else{
		      waypoint={
                 location:new google.maps.LatLng(__latitude, __longtitdue),
                 stopover:false
              }
              __waypoints.push(waypoint);
			}
				
		}
	calRoute(__begin,__end,__waypoints);
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
         travelMode: google.maps.TravelMode.WALKING
      };
    directionsService.route(request, function(result, status) {
      if (status == google.maps.DirectionsStatus.OK) {
        directionsDisplay.setDirections(result);
      }
    });
};
/**
 * 显示地图
 */
function showMap() {
	  nowPosition = new google.maps.LatLng(center[0],center[1]);
      var options = {
        zoom: 9,
        center: nowPosition,
        mapTypeId: google.maps.MapTypeId.ROADMAP
      };
      map = new google.maps.Map(document.getElementById("map_canvas"), options);
      directionsDisplay.setMap(map);
      createRoute();
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
    return marker;
};

function loadUser(){
	if(riddingId){
		dwr.engine._execute(cfg_host+"/ridding", 'RiddingPubBean', 'riddingUserList',riddingId ,loadUserCB);
	}
	
};
function loadUserCB(_list){
	if(_list!=''&&_list.length>0){
		for(i=0;i<_list.length;i++){
		   $("#userList").append("<li style='height: 70px;width: 50px;margin: 10px; float: left;overflow: hidden;' id='user_"+_list[i].userId+"'><a href='/user/"+_list[i].userId+"/ridding/list/' target='_blank''><img src="+_list[i].sAvatorUrl+" alt="+_list[i].nickName+"/></a><p style='margin-top: -5px; text-align: center;'>"+_list[i].nickName+"</p></li>");
	    }
	}
	
};

new initialize();