<!DOCTYPE HTML>
<#escape x as x?html>
<#assign pageName = "sendWeiBo" />
<#include "head.ftl">
<style>
html,body,#gmap{height:100%; margin:0;}
body{font-size:83%;}
#help{padding-top:20%; text-align:center;}
</style>

<body>
<div>
<span>总数为:</span>
</div>
<table border="1">
<thead>
<tr><td>id</td><td>内容</td><td>图片</td><td>发送源</td><td>发送时间</td><td>状态</td><td>操作</td></tr>
</thead>
<tbody>
<div style="height:300px;">
    <h3>新建一条记录</h3> 
    <div>
    <input type="text" value="微博内容" style="width:900px;" id="weiboText"/>
    </div>
    <div>
     <span>微博发送时间:</span><input type="text" readonly="true" onfocus="jQuery(this).calendar()" maxlength="16" id="weiboSendTime">
    </div>
    <div id="calendar_div" style="position: absolute; left: 106px; top: 52px; display: none;"></div>
    <form id="uploadFrame" action="/user/${visitUserId}/photoUpload/" target="uploadFrame" method="post" enctype="multipart/form-data" size="37" class="t" onsubmit="return saveReport();">
         <input type="file" value="浏览" name="file" size="37" class="t file l "/>
         <input type="submit" value="上传图片"/>
    </form>
    <div>
        <img style="width: 100px; height: 100px;display:none;" src=""  id="image"/>
    </div>
    <div>
       <span>资源类型:</span>
       <select id="weiboSelector">
          <option value ="1" selected>新浪微博</option>
          <option value ="2">腾讯微博</option>
        </select>
    </div>
    <div>
       <span>微博类型:</span>
       <select id="weiboTypeSelector">
          <option value ="0" selected>普通微博</option>
          <option value ="1">骑行地图微博</option>
        </select>
        <div>
           <span>骑行活动Id</span><input type="text" id="mapId"/>
        </div>
    </div>
    <div>
       <input type="button" id="submitWeiBo" value="提交微博"/>
    </div>
</div>

<div>
    <p>发送iphone推送信息</p>
    <input type="text" id="apnsValue" style="width:200px;"/>
    <input type="button" id="sendApns" value="发送"/>
</div>

<#if weiboList?exists>
<#list weiboList as weibo>
<tr id="${weibo.id!0}">
<td>${weibo.id!0}</td>
<td>${weibo.text!0}</td>
<td><a href="" target="_blank"><img style="max-width: 200px; max-height: 200px;" src="${cfg_imageHost}${weibo.photoUrl!""}"/></a></td>
<td><#if weibo.sourceType==1>新浪微博</#if></td>
<td width="300">${weibo.sendTimeStr!""}</td>
<td ><span style="color:red"><#if weibo.status==0>未发布<#else>已发布</#if></span></td>
</#list>
</#if>
</tbody>
</table>
<#include "footer.ftl">
</body>
<#include "js.ftl">
</html>
</#escape>
