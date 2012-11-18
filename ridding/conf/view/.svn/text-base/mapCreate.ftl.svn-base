<!DOCTYPE HTML>
<#escape x as x?html>
<#assign pageName = "mapCreate" />
<#include "head.ftl">
<style>
html,body,#gmap{height:100%; margin:0;}
body{font-size:83%;}
#help{padding-top:20%; text-align:center;}
</style>
<body>

<#include "base-topbar.ftl">
<section class="main-bar">
    <div style="margin:10px;">
        <p><a style="width: 300px;" href="/user/${hostUserId}/ridding/list/">${hostNickname}的骑行活动</a>>>创建骑行活动</p><p style="position: absolute; background-color: rgb(255, 255, 224); border: 1px solid rgb(247, 203, 145); width: 300px; text-align: center; left: 600px; height: 25px; top: 42px; line-height: 25px;">在地图上点击右键,可以选择路线位置噢!</p>
    </div>
    <aside id="left_bar" class="left-bar">
        <div class="to-path" style="background-color:white;border-bottom: 1px solid #E0E0E0;min-height:250px;">
            <div class="to-path1">
                <p class="to-path1_1" style="">选择本次骑行的起点：</p>
            </div>
            <div class="to-path2 clearfix" style="display: block;">
                <ul> 
                   <li class="clearfix" style="width:100%;"><div class="clearfix"><span>起点:</span><a href="javascript:void(0);" class=" clearfix" id="nowPositionb">当前位置</a></div><input value="" type="text" id="beginPosition" dataname="起点" class="position" onkeydown="huiche(event.keyCode,this);" data="begin"/><a style="display:none;" href="javascript:void(0);;" class="js-check" data="begin"><img class="checkImg" src="/image/check.png"/></a></li>
                </ul>
            </div>
            <div class="to-path2 clearfix">
                <p>路过地点:<!--<a href="javascript:void(0);" class="to-path3_1" id="addMidPosition" >添加路过</a>--></p>
                <ul id="mid-path">
                </ul>
            </div>
            <div class="to-path2 clearfix">
                <ul>
                   <li class="clearfix" style="width:100%;"><div class="clearfix"><span>终点:</span><a href="javascript:void(0);" class=" clearfix" id="nowPositione">当前位置</a></div><input value="" type="text" id="endPosition" dataname="终点" class="position" onkeydown="huiche(event.keyCode,this);" data="end"/><a style="display:none;" href="javascript:void(0);;" class="js-check" data="end"><img class="checkImg" src="/image/check.png"/></a></li>
                <ul> 
            </div>
            <div class="to-path3">
                <a href="javascript:void(0);" class="to-path3_2 genMap" id="genMap" style="width:100px;" >生成路线</a>
            </div>
        </div>
        <div class="to-path">
           <p>填写活动名称:</p>
           <input value="" type="text" id="hdname" style=" width: 200px;"/>
        </div>
        <div class="to-path">
            <p>邀请本次骑行的骑友:<a href="javascript:void(0);" id="invite" >邀请</a></p>
            <ul id="userList" style="display: inline-block;"></ul>
            <p><a href="javascript:void(0);" id="submitMap" class="submit"><#if ridding?exists&&ridding.id gt 0>更新<#else>创建</#if>骑行活动</a></p>
            <p style="color:#999999;text-align: center;">(路线会同步到你和骑友的手机上)</p>
        </div>
    </aside>
    <aside id="right_bar" class="right-bar" style="border: 1px solid #E0E0E0;">
        <div id="map_canvas" class="right-bar1"></div>
    </aside>
    <div id="searchdiv" class="searchBar"  style="width:400px; float: left; height: 500px;display:none;position:absolute;margin-left:317px;background-color:white;border: 1px solid #E0E0E0;">
            <div style="border-bottom: 1px solid #E0E0E0;height: 40px;width:400px;">
                <a href="javascript:void(0);" id="finishSearch" class="searchFinish" style="width: 50px;margin:10px;"><span style="height:80px;width:50px;">完成</span></a>
            </div>
            <div style="height: 80px;border-bottom: 1px solid #E0E0E0;width:400px;border: 1px solid #E0E0E0;">
                <input value="" type="text" value="关注好友的昵称" id="searchbar" style="margin: 25px 10px 25px; height: 30px; width: 300px;" onkeydown="searchhuiche(event.keyCode,this);"/><a href="javascript:void(0);" id="searchFriend" class="doSearch" style="width:50px; display: inline-block;">查找</a>
            </div>
            <div style="overflow-y:auto;overflow-x:hidden;max-height:370px;"><ul id="searchList" style="width:400px;height:370px;"></ul></div>
     </div>
</section>

<#include "footer.ftl">
</body>
<script>
var riddingId=0;
<#if ridding?exists>
leaderId="${ridding.leaderUserId!0}";
riddingId="${ridding.id!0}";
var beginLocation="${iMap.beginLocation!""}";
var endLocation="${iMap.endLocation!""}";
var center=new Array();
<#list iMap.getCenterLocations() as centerLocation>
    center.push("${centerLocation!0}");
</#list>
var locations=new Array();
<#list iMap.getMapTapsList() as location>
    locations.push(""+${location!""}+"");
</#list>

var midLocations=new Array();
<#list iMap.getMidLocationList() as location>
    midLocations.push("${location!""}");
</#list>
var riddingName="${ridding.name!""}";
</#if>
var userAccessToken="${userAccessToken!0}";
</script>
<#include "js.ftl">
</html>
</#escape>


