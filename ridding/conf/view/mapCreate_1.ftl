<!DOCTYPE HTML>
<#escape x as x?html>
<#assign pageName = "mapCreate_1" />
<#include "head.ftl">
<style>
html,body,#gmap{height:100%; margin:0;}
body{font-size:83%;}
#help{padding-top:20%; text-align:center;}
</style>
<body style="background-color:#F3F3F3">
<#include "base-topbar.ftl">
<section class="main-bar">
    <#if ridding?exists>
    <div style="margin:10px;">
        <p><a href="/user/${hostUserId}/ridding/list/">${hostNickname}的骑行活动 -- </a>${ridding.name}</p>
    </div>
    <aside id="left_bar" class="left-bar">
        <div style="padding:20px 10px;">
            <div style="margin:10px 0;height:30px;border: 1px solid black;padding:5px;line-height: 30px;text-align: left;">
                <p style="margin: auto; height: 100%; width: 100%; overflow: hidden;">活动名称:${ridding.name!""}</p>
            </div>
            <div style="margin:10px 0;border: 1px solid black;line-height: 30px;text-align: left;padding:5px;">
                <p style="height: 40px; line-height: 50px;overflow: hidden;">行程:</p>
                <p style="height: 30px; overflow: hidden;">总距离:${iMap.distance!0}</p>
                <p style="max-height: 60px; overflow: hidden;">起点:${iMap.beginLocation!""}</p>
                <#if iMap.midLocationList?exists>
                <#list iMap.midLocationList as midLocation>
                    <#if midLocation_index==0><p style="max-height: 60px; overflow: hidden;">路过:</#if>
                    <span>${midLocation!""}</span>
                </#list>
                </p>
                </#if>
                <p style="max-height: 60px; overflow: hidden;">终点:${iMap.endLocation!""}</p>
            </div>
            <div style="">
                <p>队员:</p>
                <ul id="userList" style="display: inline-block;">
                </ul>
            </div>
        </div>
    </aside>
    <aside id="right_bar" class="right-bar">
        <div id="map_canvas" class="right-bar1"></div>
    </aside>
    </#if>
</section>
<#include "footer.ftl">
</body>
<script>
var riddingId=${ridding.id!0};
var center=new Array();
<#list iMap.getCenterLocations() as centerLocation>
    center.push(${centerLocation!0});
</#list>
var locations=new Array();
<#list iMap.getMapTapsList() as location>
    locations.push(""+${location!""}+"");
</#list>
var userAccessToken="${userAccessToken!0}";
</script>
<#include "js.ftl">
</html>
</#escape>