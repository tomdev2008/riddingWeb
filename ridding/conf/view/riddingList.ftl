<!DOCTYPE HTML>
<#escape x as x?html>
<#assign pageName = "riddingList" />
<#include "head.ftl">
<style>
html,body,#gmap{height:100%; margin:0;}
body{font-size:83%;}
#help{padding-top:20%; text-align:center;}
</style>
<body>
</script>
<#include "base-topbar.ftl">
<section class="main-bar" style="height:80px;">
  <div style="background: url('http://qiqunar.com.cn/image/top/bg-head-v2.png') repeat-x scroll 0 0 transparent;">  
    <div class="main-bar-width" style="height: 80px;width:95%;margin:auto;font-family: 微软雅黑,"Arial",宋体;">
        <div style="margin-left: 20px; margin-top: 15px;height: 50px;width: 300px;display: inline-block;">
            <a  href="/user/${hostUserId!0}/ridding/list/" title="${hostNickname!""}"><img style="border:none;width:50px;height:50px;" src="${hostSAvatorUrl!""}" alt="${hostNickname!""}"/></a>
            <div style="position: relative; left: 60px; top: -55px;_top: -50px;*top: -50px;">
                 <a href="/user/${hostUserId!0}/ridding/list/" title="${hostNickname!""}"><span style="font-size: 18px; font-weight: bold; color: rgb(102, 102, 102);">${hostNickname!""}</span></a>
            </div>
            <div style="position: relative; left: 60px; top: -50px;_top: -35px;*top: -35px; color: #999999;padding:0;margin:0;">
                <span>参与了${riddingCount!0}个骑行活动   总骑行距离${totalDistance!0}KM</span>
            </div>
        </div>
    </div>
  </div>  
</section>
<section class="main-bar">
   <div class="main-bar-width">
        <div style="margin:10px;height: 30px;">
            <a href="/user/${hostUserId}/ridding/create/" style="background: none repeat scroll 0 0 #1C9FE5;border: 1px solid #11A0EE;border-radius: 3px 3px 3px 3px;color: #FFFFFF;display: inline-block;height: 25px;line-height: 25px;margin-left: 8px;text-align: center;width: 100px;">创建骑行活动</a>
        </div>
    </div>
    <#if riddingCount gt 0>
    <ul style="margin: auto; width: 85%;">
    <#if riddingUserList?exists>
        <#list riddingUserList as riddingUser>
              <li class="item">
                  <div class="js-riddingLink" dataid="${riddingUser.riddingId!0}"  style="cursor: pointer;height: 260px; width: 260px;"   >
                      <img style="height:260px;width:260px;display:block;" src="${riddingUser.avatorPicUrl!""}" alt="${riddingUser.selfName?default("")}"/>             
                      <div class="cover js-cover-${riddingUser.riddingId!0}">
					      <p class="l1">起点:${substring(riddingUser.beginLocation?default(""),26)}</p>
					      <p class="l2">终点:${substring(riddingUser.endLocation?default(""),26)}</p>
				      </div>
                  </div>
                  <div class="ln">
                      <span class="item-name">${substring(riddingUser.selfName?default(""),20)}</span>
                  </div>
                  <div class="ln item-desc clearfix">
                      <div class="clearfix" style="width:260px;">
                          <span class="l" style="display: inline;width:100px;">创建于:${riddingUser.riddingCreateTime?number_to_date?string("yyyy-MM-dd")}</span>
                          <span class="r" style="display: inline;color:<#if riddingUser.isLeader()>red;<#else>green</#if>"><#if riddingUser.isLeader()>队长<#else>队员</#if></span>
                      </div>
                  </div>
                  <#if isMe?exists><#else><a class="js-riddingCollectLink l3" dataid="${riddingUser.riddingId!0}" href="javascript:void(0);" >使用</a></#if>
              </li>
        </#list>
       </#if>
    </ul>
    <#else>
    <div class="clearfix">
        <div style="margin:auto;width:750px;padding: 50px 0;color: #AAAAAA;">
            <div style="float: left;font-size: 60px;line-height: 80px;margin-right: 30px;">^_^</div>
            <div style="width: 0;margin-top: 20px;line-height: 0;height: 0;float: left;color: #F3F3F3;border-width: 20px;border-style: solid;border-color: #F3F3F3 #FFFFFF #F3F3F3 #F3F3F3;"></div>
            <div style="background: none repeat scroll 0 0 #FFFFFF;float: left;font-size: 26px;height: 80px; line-height: 80px;padding: 0 30px;">您还没有骑行活动噢~<a href="/user/${hostUserId}/ridding/create/" style="background: none repeat scroll 0pt 0pt rgb(28, 159, 229); border: 1px solid rgb(17, 160, 238); border-radius: 3px 3px 3px 3px; color: rgb(255, 255, 255); text-align: center; width: 100px; font-size: 12px; line-height: 25px; margin-left: 10px; display: inline-block;top: -5px;position: relative;">创建骑行活动</a></div>
        </div>
    </div>
    </#if>
</section>
<#include "footer.ftl">
</body>
<script>
var userAccessToken="${userAccessToken!0}";
</script>
</html>
</#escape>
<#include "js.ftl">